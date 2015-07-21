package com.usp.icmc.labes.rbac.model;


public class Dr implements RbacElement, RbacMutableElement, RbacCardinality {

	private Role role;
	private int dynamicConstr;
	

	public Dr(Role r,int dyn){
		role=r;
		dynamicConstr = dyn;
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
		if (dynamicConstr != other.dynamicConstr)
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}



	public int getCardinality() {
		return dynamicConstr;
	}



	public Role getRole() {
		return role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dynamicConstr;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	public void setCardinality(int dynamicConstr) {
		this.dynamicConstr = dynamicConstr;
	}


	@Override
	public String toString() {
		return "Dr [role=" + role + ", dynamicConstr=" + dynamicConstr + "]";
	}
}
