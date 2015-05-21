package com.usp.icmc.labes.rbac.model;

public class UserRoleActivation implements RbacElement {

	private User user;
	private Role role;

	public UserRoleActivation(){
	}

	public UserRoleActivation(User u, Role r){
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserRoleActivation))
			return false;
		UserRoleActivation other = (UserRoleActivation) obj;
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

	@Override
	public String toString() {
		return getName();
	}
}
