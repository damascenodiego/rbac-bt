package com.usp.icmc.labes.rbac.acut;

import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;

public abstract class RbacRequest {
	
	public static final String ASSIGN_UR    	= "ASSIGN_UR";
	public static final String DEASSIGN_UR    	= "DEASSIGN_UR";

	public static final String ACTIVATE_UR    	= "ACTIVATE_UR";
	public static final String DEACTIVATE_UR    = "DEACTIVATE_UR";

	public static final String ASSIGN_PR    	= "ASSIGN_PR";
	public static final String DEASSIGN_PR    	= "DEASSIGN_PR";

	private String type;
	private User user;
	private Role role;
	private Permission permission;

	public RbacRequest() { }
	
	public String getType() {
		return type;
	}

	protected void setType(String type) {
		this.type = type;
	}

	public User getUser() {
		return user;
	}

	protected void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	protected void setRole(Role role) {
		this.role = role;
	}

	public Permission getPermission() {
		return permission;
	}

	protected void setPermission(Permission permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		String output = null;
		if(user!=null && permission==null) 
			output = type+"("+user.getName()+","+role.getName()+")";
		if(user==null && permission!=null) 
			output = type+"("+permission.getName()+","+role.getName()+")";
		return output;
	}
}
