package com.usp.icmc.labes.rbac.model;



public class ActivationHierarchy implements RbacElement {
	
	private Role senior;
	private Role junior;
	
	public ActivationHierarchy(Role sr, Role jr) {
		this.senior = sr;
		this.junior = jr;
	}
	public String getName() {
		return "("+senior.getName()+">"+junior.getName()+")";
	}
	public Role getSenior() {
		return senior;
	}
	public void setSenior(Role senior) {
		this.senior = senior;
	}
	public Role getJunior() {
		return junior;
	}
	public void setJunior(Role junior) {
		this.junior = junior;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((junior == null) ? 0 : junior.hashCode());
		result = prime * result + ((senior == null) ? 0 : senior.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ActivationHierarchy))
			return false;
		ActivationHierarchy other = (ActivationHierarchy) obj;
		if (junior == null) {
			if (other.junior != null)
				return false;
		} else if (!junior.equals(other.junior))
			return false;
		if (senior == null) {
			if (other.senior != null)
				return false;
		} else if (!senior.equals(other.senior))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "<A("+senior+">"+junior+")";
	}
}