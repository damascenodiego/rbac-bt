package com.usp.icmc.labes.fsm;

import java.util.Properties;

public abstract class FsmElement {
	
	private Properties properties;
	
	private String name;
	
	public FsmElement() {
		properties = new Properties();
	}
	
	public FsmElement(String n) {
		this();
		name = n;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FsmElement other = (FsmElement) obj;
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
	
	public Properties getProperties() {
		return properties;
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


	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "FsmElement [name=" + name + "]";
	}

	
}
