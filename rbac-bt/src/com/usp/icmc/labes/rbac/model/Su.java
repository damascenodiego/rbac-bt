package com.usp.icmc.labes.rbac.model;


public class Su implements RbacElement, RbacMutableElement, RbacCardinality {

	private User user;
	private int staticConstr;
	

	public Su(User u, int stat){
		user = u;
		staticConstr = stat;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Su))
			return false;
		Su other = (Su) obj;
		if (staticConstr != other.staticConstr)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}


	public int getCardinality() {
		return staticConstr;
	}


	public User getUser() {
		return user;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + staticConstr;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	public void setCardinality(int staticConstr) {
		this.staticConstr = staticConstr;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Su [user=" + user + ", staticConstr=" + staticConstr + "]";
	}

}
