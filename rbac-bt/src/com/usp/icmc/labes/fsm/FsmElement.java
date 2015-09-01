package com.usp.icmc.labes.fsm;

import java.util.Properties;

public abstract class FsmElement {
	
	private Properties properties;
	
	private int id;
	
	public FsmElement() {
		properties = new Properties();
	}
	
	public FsmElement(int identif) {
		this();
		id = identif;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
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
		if (id != other.id)
			return false;
		return true;
	}
	
	public Properties getProperties() {
		return properties;
	}


	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
