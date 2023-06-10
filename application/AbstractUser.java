package application;

public abstract class AbstractUser implements InterUser {
	protected String username;
	protected String password;
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
}
