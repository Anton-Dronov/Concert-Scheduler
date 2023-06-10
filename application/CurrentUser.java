package application;

public class CurrentUser {
	private static String username;
	private static String password;
	public CurrentUser(String username, String password) {
		super();
		CurrentUser.username = username;
		CurrentUser.password = password;
	}
	public static String getUsername() {
		return username;
	}
	public static String getPassword() {
		return password;
	}
}

