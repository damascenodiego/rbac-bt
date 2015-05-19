package com.usp.icmc.labes.rbac.model;

import java.util.ArrayList;
import java.util.List;

public class UserRoleAssignment implements RbacElement {

	private User user;
	private Role role;
	private List<Role> activeRoles;

	public UserRoleAssignment(){
		activeRoles = new ArrayList<Role>();
	}

	public UserRoleAssignment(User u, Role r){
		this();
		this.user = u;
		this.role = r;
	}

	public String getName() {
		return "("+user.getName()+","+role.getName()+")";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Role> getActiveRoles() {
		return activeRoles;
	}

}
