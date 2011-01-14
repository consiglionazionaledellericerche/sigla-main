package it.cnr.contab.cmis.acl;

public class Permission {
	private String userName;
	private Role role;

	public static Permission construct(String userName, Role role){
		return new Permission(userName, role);
	}
	
	protected Permission(String userName, Role role) {
		super();
		this.userName = userName;
		this.role = role;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
}
