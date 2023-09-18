package com.info.fmis.service;

import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.info.fmis.dto.AdminUserDTO;
import com.info.fmis.model.User;
import com.info.fmis.repository.UserRepository;

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

	public User addUser(AdminUserDTO userInfo) {

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
