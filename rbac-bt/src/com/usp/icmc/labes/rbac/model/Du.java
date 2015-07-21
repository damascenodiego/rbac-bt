package com.usp.icmc.labes.rbac.model;


public class Du implements RbacElement, RbacMutableElement, RbacCardinality {

	private User user;
	private int dynamicConstr;
	
	public Du(User u,int dyn){
		user=u;
		dynamicConstr = dyn;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Du))
			return false;
		Du other = (Du) obj;
		if (dynamicConstr != other.dynamicConstr)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}


	public int getCardinality() {
		return dynamicConstr;
	}


	public User getUser() {
		return user;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dynamicConstr;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	public void setCardinality(int dynamicConstr) {
		this.dynamicConstr = dynamicConstr;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Du [user=" + user + ", dynamicConstr=" + dynamicConstr + "]";
	}

}
