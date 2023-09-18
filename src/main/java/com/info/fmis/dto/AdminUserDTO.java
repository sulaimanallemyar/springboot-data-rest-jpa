package com.info.fmis.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminUserDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotNull
	private String username;
	
	private String email;
	
	@NotNull
	private String password;
	
	private String roles;
}