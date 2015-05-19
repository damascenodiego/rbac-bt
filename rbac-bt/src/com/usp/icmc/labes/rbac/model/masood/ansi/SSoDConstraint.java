package com.usp.icmc.labes.rbac.model.masood.ansi;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.usp.icmc.labes.rbac.model.RbacElement;

public class SSoDConstraint implements RbacElement {

	int cardinality;
	Set<Role> sodSet;

	public SSoDConstraint() {
		sodSet = new HashSet<Role>();
	}


	public SSoDConstraint(Role[] set, int card) {
		this();
		for (int i = 0; i < set.length; i++) {
			sodSet.add(set[i]);
		}
		this.cardinality = card;
	}

	public SSoDConstraint(Collection<Role> set, int card) {
		this();
		Iterator<Role> roles = set.iterator();
		while(roles.hasNext()) {
			sodSet.add(roles.next());
		}
		this.cardinality = card;
	}

	public String getName() {
		return String.join(", ", (CharSequence[]) this.sodSet.toArray());
	}


	public int getCardinality() {
		return cardinality;
	}


	public void setCardinality(int cardinality) {
		this.cardinality = cardinality;
	}


	public Set<Role> getSodSet() {
		return sodSet;
	}


	public void setSodSet(Set<Role> sodSet) {
		this.sodSet = sodSet;
	}
	
	@Override
	public String toString() {
		return "SSoD({"+sodSet+"},"+cardinality+")";
	}
}