package com.usp.icmc.labes.rbac.model.masood.acut;

import com.usp.icmc.labes.rbac.model.masood.ansi.*;

public abstract class RbacRequest {

	private String type;
	private User user;
	private Role role;
	private Permission permission;

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
