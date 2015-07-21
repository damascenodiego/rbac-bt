package com.usp.icmc.labes.rbac.acut;

import com.usp.icmc.labes.rbac.model.Permission;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.User;

public abstract class RbacRequest {
	
	public static final String ASSIGN_UR    	= "AS";
	public static final String DEASSIGN_UR    	= "DS";

	public static final String ACTIVATE_UR    	= "AC";
	public static final String DEACTIVATE_UR    = "DC";

	public static final String ASSIGN_PR    	= "AP";
	public static final String DEASSIGN_PR    	= "DP";
	
//	public static final String ASSIGN_UR    	= "ASSIGN_UR";
//	public static final String DEASSIGN_UR    	= "DEASSIGN_UR";
//
//	public static final String ACTIVATE_UR    	= "ACTIVATE_UR";
//	public static final String DEACTIVATE_UR    = "DEACTIVATE_UR";
//
//	public static final String ASSIGN_PR    	= "ASSIGN_PR";
//	public static final String DEASSIGN_PR    	= "DEASSIGN_PR";

	private String type;
	private User user;
	private Role role;
	private Permission permission;

	public RbacRequest() { }
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RbacRequest))
			return false;
		RbacRequest other = (RbacRequest) obj;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	public Permission getPermission() {
		return permission;
	}

	public Role getRole() {
		return role;
	}

	public String getType() {
		return type;
	}

	public User getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	protected void setPermission(Permission permission) {
		this.permission = permission;
	}

	protected void setRole(Role role) {
		this.role = role;
	}

	protected void setType(String type) {
		this.type = type;
	}

	protected void setUser(User user) {
		this.user = user;
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
