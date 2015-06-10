package com.usp.icmc.labes.rbac.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class Sr implements RbacElement, RbacMutableElement, RbacCardinality {

	@XStreamAsAttribute
	private Role role;
	@XStreamAsAttribute
	private int staticConstr;
	

	public Sr(Role r, int stat){
		role=r;
		staticConstr = stat;
	}


	public Role getRole() {
		return role;
	}


	public int getCardinality() {
		return staticConstr;
	}


	public void setCardinality(int staticConstr) {
		this.staticConstr = staticConstr;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + staticConstr;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Sr))
			return false;
		Sr other = (Sr) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (staticConstr != other.staticConstr)
			return false;
		return true;
	}


	public String toString() {
		return "RoleConstraint [role=" + role + ", staticConstr="
				+ staticConstr + "]";
	}

}
