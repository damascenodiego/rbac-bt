package com.usp.icmc.labes.rbac.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SSoDConstraint implements SoDConstraint  {

	int ss;
	Set<Role> sodSet;

	public SSoDConstraint() {
		sodSet = new HashSet<Role>();
	}


	public SSoDConstraint(Collection<Role> set, int card) {
		this();
		Iterator<Role> roles = set.iterator();
		while(roles.hasNext()) {
			sodSet.add(roles.next());
		}
		this.ss = card;
	}

	public SSoDConstraint(Role[] set, int card) {
		this();
		for (int i = 0; i < set.length; i++) {
			sodSet.add(set[i]);
		}
		this.ss = card;
	}

	public int getCardinality() {
		return ss;
	}


	public Set<Role> getSodSet() {
		return sodSet;
	}


	public void setCardinality(int cardinality) {
		this.ss = cardinality;
	}


	public void setSodSet(Set<Role> sodSet) {
		this.sodSet = sodSet;
	}
	
	@Override
	public String toString() {
		return "SSoD({"+sodSet+"},"+ss+")";
	}
}