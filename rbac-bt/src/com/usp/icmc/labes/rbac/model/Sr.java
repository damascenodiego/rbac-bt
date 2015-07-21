package com.usp.icmc.labes.rbac.model;


public class Sr implements RbacElement, RbacMutableElement, RbacCardinality {

	private Role role;
	private int staticConstr;
	

	public Sr(Role r, int stat){
		role=r;
		staticConstr = stat;
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


	public int getCardinality() {
		return staticConstr;
	}


	public Role getRole() {
		return role;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + staticConstr;
		return result;
	}


	public void setCardinality(int staticConstr) {
		this.staticConstr = staticConstr;
	}


	public String toString() {
		return "Sr [role=" + role + ", staticConstr="
				+ staticConstr + "]";
	}

}
