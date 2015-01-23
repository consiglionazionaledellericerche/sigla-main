package it.cnr.contab.cmis.acl;

import java.util.HashMap;
import java.util.Map;


public class Permission {
	private String userName;
	private ACLType role;

	public static Permission construct(String userName, ACLType role){
		return new Permission(userName, role);
	}
	
	protected Permission(String userName, ACLType role) {
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
	public ACLType getRole() {
		return role;
	}
	public void setRole(ACLType role) {
		this.role = role;
	}
	
	public static Map<String, ACLType> convert(Permission... permissions) {
		Map<String, ACLType> result = new HashMap<String, ACLType>();
		for (Permission permission : permissions) {
			result.put(permission.getUserName(), permission.getRole());
		}		
		return result;
	}
	
}
