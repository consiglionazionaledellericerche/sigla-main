package it.cnr.contab.cmis.acl;

public class Role {
	private String roleName;

	protected Role(String roleName) {
		super();
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}
	
}
