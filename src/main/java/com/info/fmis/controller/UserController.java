package com.info.fmis.controller;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import com.info.fmis.dto.AdminUserDTO;
import com.info.fmis.dto.UserDTO;
import com.info.fmis.model.AuthRequest;
import com.info.fmis.model.User;
import com.info.fmis.service.JwtService;
import com.info.fmis.service.UserService;

import net.minidev.json.JSONArray;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome this endpoint is not secure";
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> getUsers() {

		List<User> list = userService.findAll();
		List<UserDTO> result = list.stream().map(user -> modelMapper.map(user, UserDTO.class))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(result);
	}

	@PostMapping("/signup")
	public ResponseEntity<UserDTO> addNewUser(@Valid @RequestBody AdminUserDTO userInfo) {

		if (userService.isUserAlreadyRegistered(userInfo.getUsername())) {
//			throw new user
		}

		User obj = userService.addUser(userInfo);
		UserDTO result = this.modelMapper.map(obj, UserDTO.class);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/user/userProfile")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String userProfile() {
		return "Welcome to User Profile";
	}

	@GetMapping("/admin/adminProfile")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String adminProfile() {
		return "Welcome to Admin Profile";
	}

	@PostMapping("/signin")
	public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		
        Authentication authentication = null;

        try {
    		authentication = authenticationManager.authenticate(
    				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			return ResponseEntity.ok().body(jwtService.generateToken(authentication));
        } catch (BadCredentialsException e) {
            return BadCredentialsException("Invalid username or password");
        }
	}

	private ResponseEntity<String> BadCredentialsException(String message) {
		
        JSONObject response = new JSONObject();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        response.put("data", new JSONArray());
        response.put("status", false);
        response.put("error", message);
        response.put("status_code", 400);
        response.put("timestamp", Instant.now());
        
        return new ResponseEntity<>(response.toString(), httpHeaders, HttpStatus.BAD_REQUEST);
	}
}
