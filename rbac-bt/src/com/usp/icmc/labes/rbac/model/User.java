package com.usp.icmc.labes.rbac.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;



public class User implements RbacElement, RoleAssignable{

	/* user name */
	@XStreamAsAttribute
	private String name;
	/* static cardinality */
	@XStreamAsAttribute
	private int su;
	/* dynamic cardinality */
	@XStreamAsAttribute
	private int du;

	public User() {
		su = 0;
		du = 0;
	}

	public User(String name) {
		this();
		this.name = name;
	}

	public User(String name, int staticCard, int dynamicCard) {
		this(name);
		this.su = staticCard;
		this.du = dynamicCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStaticCardinality() {
		return su;
	}

	public void setStaticCardinality(int staticCardinality) {
		this.su = staticCardinality;
	}

	public int getDynamicCardinality() {
		return du;
	}

	public void setDynamicCardinality(int dynamicCardinality) {
		this.du = dynamicCardinality;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + du;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + su;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (du != other.du)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (su != other.su)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "user("+name+","+su+","+du+")";
	}
}
