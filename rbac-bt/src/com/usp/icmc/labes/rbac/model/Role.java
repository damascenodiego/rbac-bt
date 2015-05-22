package com.usp.icmc.labes.rbac.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;



public class Role {
	
	/* role name */
	@XStreamAsAttribute
	private String name;
	/* static cardinality */
	@XStreamAsAttribute
	private int sr;
	/* dynamic cardinality */
	@XStreamAsAttribute
	private int dr;
	
	public Role() {
		sr = 0;
		dr = 0;
	}
	
	public Role(String name) {
		this();
		this.name = name;
	}

	public Role(String name, int staticCard, int dynamicCard) {
		this(name);
		this.sr = staticCard;
		this.dr = dynamicCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStaticCardinality() {
		return sr;
	}

	public void setStaticCardinality(int staticCardinality) {
		this.sr = staticCardinality;
	}

	public int getDynamicCardinality() {
		return dr;
	}

	public void setDynamicCardinality(int dynamicCardinality) {
		this.dr = dynamicCardinality;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dr;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + sr;
		return result;
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
		if (dr != other.dr)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sr != other.sr)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "role("+name+","+sr+","+dr+")";
	}
}

