package com.usp.icmc.labes.rbac.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class Du implements RbacElement, RbacMutableElement {

	@XStreamAsAttribute
	private User user;
	@XStreamAsAttribute
	private int dynamicConstr;
	
	public Du(User u,int dyn){
		user=u;
		dynamicConstr = dyn;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public int getDynamicConstr() {
		return dynamicConstr;
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
		if (!(obj instanceof Du))
			return false;
		Du other = (Du) obj;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Du [user=" + user + ", dynamicConstr=" + dynamicConstr + "]";
	}

}
