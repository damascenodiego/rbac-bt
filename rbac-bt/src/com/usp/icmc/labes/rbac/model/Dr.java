package com.usp.icmc.labes.rbac.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class Dr implements RbacElement, RbacMutableElement {

	@XStreamAsAttribute
	private Role role;
	@XStreamAsAttribute
	private int dynamicConstr;
	

	public Dr(Role r,int dyn){
		role=r;
		dynamicConstr = dyn;
	}


	public Role getRole() {
		return role;
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
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Dr))
			return false;
		Dr other = (Dr) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Dr [role=" + role + ", dynamicConstr=" + dynamicConstr + "]";
	}
}
