package com.usp.icmc.labes.rbac.model;



public class ActivationHierarchy implements Hierarchy {
	
	private Role senior;
	private Role junior;
	
	public ActivationHierarchy(Role sr, Role jr) {
		this.senior = sr;
		this.junior = jr;
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
	public Role getJunior() {
		return junior;
	}
	public Role getSenior() {
		return senior;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((junior == null) ? 0 : junior.hashCode());
		result = prime * result + ((senior == null) ? 0 : senior.hashCode());
		return result;
	}
	
	public void setJunior(Role junior) {
		this.junior = junior;
	}
	public void setSenior(Role senior) {
		this.senior = senior;
	}
	@Override
	public String toString() {
		return "<A("+senior+">"+junior+")";
	}
}