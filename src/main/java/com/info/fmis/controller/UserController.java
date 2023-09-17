package com.info.fmis.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.info.fmis.dto.CustomerDTO;
import com.info.fmis.dto.UserDTO;
import com.info.fmis.model.AuthRequest;
import com.info.fmis.model.User;
import com.info.fmis.service.JwtService;
import com.info.fmis.service.UserService;

@RestController
@RequestMapping("/auth")
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

	@PostMapping("/addNewUser")
	public ResponseEntity<UserDTO> addNewUser(@Valid @RequestBody UserDTO userInfo) {

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

	@PostMapping("/generateToken")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		if (authentication.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return jwtService.generateToken(authentication);
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}
	}
}
