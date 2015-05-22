package com.usp.icmc.labes.rbac.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;



public class PermissionRoleAssignment {
	
	@XStreamAsAttribute
	private Permission permission;
	@XStreamAsAttribute
	private Role role;
	
	public PermissionRoleAssignment(){
	}

	public PermissionRoleAssignment(Permission p, Role r){
		this.permission = p;
		this.role = r;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PermissionRoleAssignment))
			return false;
		PermissionRoleAssignment other = (PermissionRoleAssignment) obj;
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
		return true;
	}

	@Override
	public String toString() {
		return "("+permission.getName()+","+role.getName()+")";
	}
}

