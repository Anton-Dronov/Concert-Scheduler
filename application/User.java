package application;

public class User extends AbstractUser {
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
}