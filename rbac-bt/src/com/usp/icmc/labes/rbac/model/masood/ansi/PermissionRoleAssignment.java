package com.usp.icmc.labes.rbac.model.masood.ansi;

import com.usp.icmc.labes.rbac.model.RbacElement;


public class PermissionRoleAssignment implements RbacElement {
	
	private Permission permission;
	private Role role;
	
	public PermissionRoleAssignment(){
	}

	public PermissionRoleAssignment(Permission p, Role r){
		this.permission = p;
		this.role = r;
	}

	public String getName() {
		return "("+permission.getName()+","+role.getName()+")";
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

}
