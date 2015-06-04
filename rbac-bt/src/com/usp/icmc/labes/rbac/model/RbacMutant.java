package com.usp.icmc.labes.rbac.model;

import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.rbac.acut.RbacRequestActivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignPR;
import com.usp.icmc.labes.rbac.acut.RbacRequestAssignUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeactivateUR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignPR;
import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignUR;
import com.usp.icmc.labes.rbac.acut.RbacResponse;
import com.usp.icmc.labes.rbac.acut.RbacState;
import com.usp.icmc.labes.rbac.features.RbacAdministrativeCommands;
import com.usp.icmc.labes.rbac.features.RbacAdvancedReviewFunctions;
import com.usp.icmc.labes.rbac.features.RbacReviewFunctions;
import com.usp.icmc.labes.rbac.features.RbacSupportingSystemFunctions;
import com.usp.icmc.labes.utils.RbacUtils;

public class RbacMutant implements RbacTuple{

	private RbacTuple policy;
	private List<RbacMutableElement> originalList;
	private List<RbacMutableElement> mutantList;
	private RbacMutableElement originalElement;
	private RbacMutableElement mutantElement;

	public RbacMutant(RbacPolicy pol, RbacMutableElement orig, RbacMutableElement delt) {
		this(pol);
		this.originalElement = orig;
		this.mutantElement = delt;
	}

	public RbacMutant(RbacMutant mut, RbacMutableElement orig, RbacMutableElement delt) {
		this(mut);
		this.originalElement = orig;
		this.mutantElement = delt;
	}

	private RbacMutant(RbacTuple pol) {
		this.policy = pol;
		this.originalList = new ArrayList<RbacMutableElement>();
		this.mutantList = new ArrayList<RbacMutableElement>();
		setupMutant();
	}

	public RbacTuple getPolicy() {
		return policy;
	}

	private void setupMutant() {
		if(originalElement instanceof UserRoleAssignment && mutantElement instanceof UserRoleAssignment){
			originalList.addAll(policy.getUserRoleAssignment());
			mutantList.addAll(policy.getUserRoleAssignment());
		
		}else if(originalElement instanceof ActivationHierarchy && mutantElement instanceof ActivationHierarchy){
			originalList.addAll(policy.getActivationHierarchy());
			mutantList.addAll(policy.getActivationHierarchy());

		}else if(originalElement instanceof InheritanceHierarchy && mutantElement instanceof InheritanceHierarchy){
			originalList.addAll(policy.getInheritanceHierarchy());
			mutantList.addAll(policy.getInheritanceHierarchy());
			
		}else if(originalElement instanceof Su && mutantElement instanceof Su){
			originalList.addAll(policy.getSu());
			mutantList.addAll(policy.getSu());
			
		}else if(originalElement instanceof Du && mutantElement instanceof Du){
			originalList.addAll(policy.getDu());
			mutantList.addAll(policy.getDu());

		}else if(originalElement instanceof Sr && mutantElement instanceof Sr){
			originalList.addAll(policy.getSr());
			mutantList.addAll(policy.getSr());

		}else if(originalElement instanceof Dr && mutantElement instanceof Dr){
			originalList.addAll(policy.getDr());
			mutantList.addAll(policy.getDr());
			
		}else if(originalElement instanceof SSoDConstraint && mutantElement instanceof SSoDConstraint){
			originalList.addAll(policy.getSsodConstraint());
			mutantList.addAll(policy.getSsodConstraint());
			
		}else if(originalElement instanceof DSoDConstraint && mutantElement instanceof DSoDConstraint){
			originalList.addAll(policy.getDsodConstraint());
			mutantList.addAll(policy.getDsodConstraint());

		}
		mutantList.remove(originalElement);
		mutantList.add(mutantElement);
	}

	@Override
	public List<UserRoleAssignment> getUserRoleAssignment() {
		return policy.getUserRoleAssignment();
	}

	@Override
	public List<UserRoleActivation> getUserRoleActivation() {
		return policy.getUserRoleActivation();
	}

	@Override
	public List<PermissionRoleAssignment> getPermissionRoleAssignment() {
		return policy.getPermissionRoleAssignment();
	}

	@Override
	public List<User> getUser() {
		return policy.getUser();
	}

	@Override
	public List<Role> getRole() {
		return policy.getRole();
	}

	@Override
	public List<Permission> getPermission() {
		return policy.getPermission();
	}

	@Override
	public List<Su> getSu() {
		return policy.getSu();
	}

	@Override
	public List<Sr> getSr() {
		return policy.getSr();
	}

	@Override
	public List<Du> getDu() {
		return policy.getDu();
	}

	@Override
	public List<Dr> getDr() {
		return policy.getDr();
	}

	@Override
	public List<SSoDConstraint> getSsodConstraint() {
		return policy.getSsodConstraint();
	}

	@Override
	public List<DSoDConstraint> getDsodConstraint() {
		return policy.getDsodConstraint();
	}

	@Override
	public List<ActivationHierarchy> getActivationHierarchy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InheritanceHierarchy> getInheritanceHierarchy() {
		// TODO Auto-generated method stub
		return null;
	}
}
