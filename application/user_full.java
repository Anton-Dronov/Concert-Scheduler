package application;

public class user_full extends User implements InterFull{
	private String phone_number;
	private String email;
	private String type;
	public user_full(String username, String password) {
		super(username, password);
	}
	public user_full(String username, String password, String phone_number, String email, String type) {
		super(username, password);
		this.phone_number = phone_number;
		this.email = email;
		this.type = type;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public String getEmail() {
		return email;
	}
	public String getType() {
		return type;
	}
}
