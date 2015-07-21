package com.usp.icmc.labes.rbac.model;


public class UserRoleAssignment implements RbacElement, RbacMutableElement {

	private User user;
	private Role role;

	public UserRoleAssignment(){
	}

	public UserRoleAssignment(User u, Role r){
		this();
		this.user = u;
		this.role = r;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserRoleAssignment))
			return false;
		UserRoleAssignment other = (UserRoleAssignment) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	public Role getRole() {
		return role;
	}

	public User getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "("+user.getName()+","+role.getName()+")";
	}
}
