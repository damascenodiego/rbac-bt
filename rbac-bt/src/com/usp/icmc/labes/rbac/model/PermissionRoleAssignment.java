package com.usp.icmc.labes.rbac.model;




public class PermissionRoleAssignment implements RbacElement, RbacMutableElement {
	
	private Permission permission;
	private Role role;
	
	public PermissionRoleAssignment(){
	}

	public PermissionRoleAssignment(Permission p, Role r){
		this.permission = p;
		this.role = r;
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

	public Permission getPermission() {
		return permission;
	}

	public Role getRole() {
		return role;
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

	
	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "("+permission.getName()+","+role.getName()+")";
	}
}

