package world.grendel.userlogindemo.dataobjects;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * UserRegisterDTO
 */
public class UserRegisterDTO {
    @NotEmpty
    @Length(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;
    @NotEmpty
    @Email(message = "Please enter a valid email!")
    private String email;
    @NotEmpty
    @Length(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;
    @NotEmpty
    private String confirmPassword;

    public UserRegisterDTO() {
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
