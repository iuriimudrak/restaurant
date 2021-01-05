package ru.iuriimudrak.restaurant.to;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserTo extends BaseTo implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(min = 2, max = 100)
	private String name;

	@Email
	@NotBlank
	@Size(max = 100)
	private String email;

	@NotBlank
	@Size(min = 5, max = 100, message = "length must be between 5 and 100 characters")
	private String password;

	public UserTo(Integer id, String name, String email, String password) {
		super(id);
		this.name = name;
		this.email = email;
		this.password = password;
	}
}
