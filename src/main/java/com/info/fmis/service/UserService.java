package com.info.fmis.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.info.fmis.dto.UserDTO;
import com.info.fmis.model.AuthRequest;
import com.info.fmis.model.User;
import com.info.fmis.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> userDetail = repository.findByUsername(username);

		// Converting userDetail to UserDetails
		return userDetail.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
	}

	public User addUser(UserDTO userInfo) {

		User obj = this.modelMapper.map(userInfo, User.class);

		obj.setPassword(encoder.encode(userInfo.getPassword()));
		obj = repository.save(obj);
		return obj;
	}

	public boolean isUserAlreadyRegistered(String username) {

		Optional<User> userDetail = repository.findByUsername(username);

		if (userDetail.isPresent()) {
			return true;
		}

		return false;
	}

	public List<User> findAll() {

		List<User> list = repository.findAll();
		return list;
	}


}
