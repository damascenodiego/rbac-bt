package com.usp.icmc.labes.rbac.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class UserConstraint {

	@XStreamAsAttribute
	private User user;
	@XStreamAsAttribute
	private int staticConstr;
	@XStreamAsAttribute
	private int dynamicConstr;
	

	public UserConstraint(User u, int stat,int dyn){
		user = u;
		staticConstr = stat;
		dynamicConstr = dyn;
	}


	public User getUser() {
		return user;
	}


	public int getStaticConstr() {
		return staticConstr;
	}


	public int getDynamicConstr() {
		return dynamicConstr;
	}


	public void setStaticConstr(int staticConstr) {
		this.staticConstr = staticConstr;
	}


	public void setDynamicConstr(int dynamicConstr) {
		this.dynamicConstr = dynamicConstr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserConstraint other = (UserConstraint) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "UserConstraint [user=" + user + ", staticConstr="
				+ staticConstr + ", dynamicConstr=" + dynamicConstr + "]";
	}

}
