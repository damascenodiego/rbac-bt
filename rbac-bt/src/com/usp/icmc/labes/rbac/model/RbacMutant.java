package com.usp.icmc.labes.rbac.model;


import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class RbacMutant extends RbacPolicy{

	@XStreamOmitField
	private RbacTuple policy;
	@XStreamOmitField
	private RbacMutableElement originalElement;
	@XStreamOmitField
	private RbacMutableElement mutantElement;
	@XStreamOmitField
	private MutantType type;

	public RbacMutant(RbacPolicy pol, RbacMutableElement orig, RbacMutableElement delt, MutantType t) {
		this(pol);
		this.mutantElement = delt;
		this.originalElement = orig;
		type = t;
		setupMutant();
		this.setName(pol.getName()+"_"+getType().name());
	}

	public RbacMutant(RbacMutant mut, RbacMutableElement orig, RbacMutableElement delt, MutantType t) {
		this((RbacTuple) mut);
		this.mutantElement = delt;
		this.originalElement = orig;
		type = t;
		setupMutant();
		this.setName(getName()+"_"+getType().name());
	}

	private RbacMutant(RbacTuple pol) {
		super();
		this.policy = pol;
		this.setName(((RbacPolicy)pol).getName());

	}

	public RbacTuple getPolicy() {
		return policy;
	}



	private void setupMutant() {
		getUser().addAll(policy.getUser());
		getRole().addAll(policy.getRole());
		getPermission().addAll(policy.getPermission());
		getUserRoleAssignment().addAll(policy.getUserRoleAssignment());
		getUserRoleActivation().addAll(policy.getUserRoleActivation());
		getPermissionRoleAssignment().addAll(policy.getPermissionRoleAssignment());
		getActivationHierarchy().addAll(policy.getActivationHierarchy());
		getInheritanceHierarchy().addAll(policy.getInheritanceHierarchy());
		getSu().addAll(policy.getSu());
		getDu().addAll(policy.getDu());
		getSr().addAll(policy.getSr());
		getDr().addAll(policy.getDr());
		getSSoDConstraint().addAll(policy.getSSoDConstraint());
		getDSoDConstraint().addAll(policy.getDSoDConstraint());

		if(originalElement instanceof UserRoleAssignment && mutantElement instanceof UserRoleAssignment){
			int index = getUserRoleAssignment().indexOf(originalElement);
			getUserRoleAssignment().set(index,(UserRoleAssignment) mutantElement);
			getUserRoleAssignment().remove(originalElement);

		}else if(originalElement instanceof UserRoleActivation && mutantElement instanceof UserRoleActivation){
			int index = getUserRoleActivation().indexOf(originalElement);
			getUserRoleActivation().set(index,(UserRoleActivation) mutantElement);
			getUserRoleActivation().remove(originalElement);

		}else if(originalElement instanceof ActivationHierarchy && mutantElement instanceof ActivationHierarchy){
			int index = getActivationHierarchy().indexOf(originalElement);
			getActivationHierarchy().set(index,(ActivationHierarchy) mutantElement);
			getActivationHierarchy().remove(originalElement);

		}else if(originalElement instanceof InheritanceHierarchy && mutantElement instanceof InheritanceHierarchy){
			int index = getInheritanceHierarchy().indexOf(originalElement);
			getInheritanceHierarchy().set(index,(InheritanceHierarchy) mutantElement);
			getInheritanceHierarchy().remove(originalElement);

		}else if(originalElement instanceof Su && mutantElement instanceof Su){
			int index = getSu().indexOf(originalElement);
			getSu().set(index,(Su) mutantElement);
			getSu().remove(originalElement);

		}else if(originalElement instanceof Du && mutantElement instanceof Du){
			int index = getDu().indexOf(originalElement);
			getDu().set(index,(Du) mutantElement);
			getDu().remove(originalElement);

		}else if(originalElement instanceof Sr && mutantElement instanceof Sr){
			int index = getSr().indexOf(originalElement);
			getSr().set(index,(Sr) mutantElement);
			getSr().remove(originalElement);

		}else if(originalElement instanceof Dr && mutantElement instanceof Dr){
			int index = getDr().indexOf(originalElement);
			getDr().set(index,(Dr) mutantElement);
			getDr().remove(originalElement);

		}else if(originalElement instanceof SSoDConstraint && mutantElement instanceof SSoDConstraint){
			int index = getSSoDConstraint().indexOf(originalElement);
			getSSoDConstraint().set(index,(SSoDConstraint) mutantElement);
			getSSoDConstraint().remove(originalElement);

		}else if(originalElement instanceof DSoDConstraint && mutantElement instanceof DSoDConstraint){
			int index = getDSoDConstraint().indexOf(originalElement);
			getDSoDConstraint().set(index,(DSoDConstraint) mutantElement);
			getDSoDConstraint().remove(originalElement);
		}
	}

	public MutantType getType() {
		return type;
	}

	public void setType(MutantType type) {
		this.type = type;
	}

}
