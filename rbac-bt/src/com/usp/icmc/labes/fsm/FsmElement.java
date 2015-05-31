package com.usp.icmc.labes.fsm;

import java.util.Properties;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class FsmElement {
	
	private Properties properties;
	
	@XStreamAsAttribute
	private String name;
	
	public FsmElement() {
		properties = new Properties();
	}
	
	public FsmElement(String n) {
		this();
		name = n;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((properties == null) ? 0 : properties.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FsmElement))
			return false;
		FsmElement other = (FsmElement) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		return true;
	}
	
	
	
}
