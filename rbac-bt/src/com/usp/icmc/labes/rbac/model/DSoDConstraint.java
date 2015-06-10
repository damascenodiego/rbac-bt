package com.usp.icmc.labes.rbac.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DSoDConstraint implements SoDConstraint {

	int ds;
	Set<Role> sodSet;

	public DSoDConstraint() {
		sodSet = new HashSet<Role>();
	}


	public DSoDConstraint(Role[] set, int card) {
		this();
		for (int i = 0; i < set.length; i++) {
			sodSet.add(set[i]);
		}
		this.ds = card;
	}

	public DSoDConstraint(Collection<Role> set, int card) {
		this();
		Iterator<Role> roles = set.iterator();
		while(roles.hasNext()) {
			sodSet.add(roles.next());
		}
		this.ds = card;
	}
	
	public int getCardinality() {
		return ds;
	}


	public void setCardinality(int cardinality) {
		this.ds = cardinality;
	}


	public Set<Role> getSodSet() {
		return sodSet;
	}


	public void setSodSet(Set<Role> sodSet) {
		this.sodSet = sodSet;
	}
	
	@Override
	public String toString() {
		return "DSoD({"+sodSet+"},"+ds+")";
	}
}