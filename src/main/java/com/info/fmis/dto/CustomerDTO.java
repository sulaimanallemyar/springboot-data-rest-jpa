package com.info.fmis.dto;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerDTO implements Serializable{

	private Long id;
	
	@NotNull
	private String name;
	
	private String lastName;
	
	private String fatherName;
	
	private String note;
}