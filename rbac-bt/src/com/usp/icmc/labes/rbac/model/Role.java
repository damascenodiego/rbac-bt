package com.usp.icmc.labes.rbac.model;




public class Role implements RbacElement {
	
	/* role name */
	private String name;
	/* static cardinality */

	public Role() {
	}
	
	public Role(String name) {
		this();
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Role))
			return false;
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "role("+name+")";
	}
}

