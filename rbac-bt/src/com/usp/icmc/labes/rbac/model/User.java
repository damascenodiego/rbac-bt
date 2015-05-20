package com.usp.icmc.labes.rbac.model;



public class User implements RbacElement{

	/* user name */
	private String name;
	/* static cardinality */
	private int staticCardinality;
	/* dynamic cardinality */
	private int dynamicCardinality;

	public User() {
		staticCardinality = 0;
		dynamicCardinality = 0;
	}

	public User(String name) {
		this();
		this.name = name;
	}

	public User(String name, int staticCard, int dynamicCard) {
		this(name);
		this.staticCardinality = staticCard;
		this.dynamicCardinality = dynamicCard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStaticCardinality() {
		return staticCardinality;
	}

	public void setStaticCardinality(int staticCardinality) {
		this.staticCardinality = staticCardinality;
	}

	public int getDynamicCardinality() {
		return dynamicCardinality;
	}

	public void setDynamicCardinality(int dynamicCardinality) {
		this.dynamicCardinality = dynamicCardinality;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dynamicCardinality;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + staticCardinality;
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
		if (dynamicCardinality != other.dynamicCardinality)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (staticCardinality != other.staticCardinality)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "user("+name+","+staticCardinality+","+dynamicCardinality+")";
	}
}
