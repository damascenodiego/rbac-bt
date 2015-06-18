package com.usp.icmc.labes.utils;

import java.util.ArrayList;
import java.util.List;

import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Dr;
import com.usp.icmc.labes.rbac.model.Du;
import com.usp.icmc.labes.rbac.model.MutantType;
import com.usp.icmc.labes.rbac.model.RbacMutant;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.RbacTuple;
import com.usp.icmc.labes.rbac.model.Role;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.Sr;
import com.usp.icmc.labes.rbac.model.Su;
import com.usp.icmc.labes.rbac.model.User;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;

public class RbacMutationUtils {

	static RbacMutationUtils instance;

	private RbacMutationUtils() {
	} 

	public static RbacMutationUtils getInstance() {
		if(instance ==null){
			instance = new RbacMutationUtils();
		}
		return instance;
	}

	public List<RbacMutant> generateMutants(RbacTuple pol, MutantType type){
		List<RbacMutant> result = new ArrayList<RbacMutant>();

		switch (type) {
		case UR_REPLACE_U:    return generate_mutants_UR_REPLACE_U(pol);
		case UR_REPLACE_R:    return generate_mutants_UR_REPLACE_R(pol);
		case UR_REPLACE_UR:   return generate_mutants_UR_REPLACE_UR(pol);
//		case PR_REPLACE_P:    return generate_mutants_PR_REPLACE_P(pol);
//		case PR_REPLACE_R:    return generate_mutants_PR_REPLACE_R(pol);
//		case PR_REPLACE_PR:   return generate_mutants_PR_REPLACE_PR(pol);
//		case AH_REPLACE_Sr:   return generate_mutants_AH_REPLACE_Sr(pol);
//		case AH_REPLACE_Jr:   return generate_mutants_AH_REPLACE_Jr(pol);
//		case AH_REPLACE_SrJr: return generate_mutants_AH_REPLACE_SrJr(pol);
//		case IH_REPLACE_Sr:   return generate_mutants_IH_REPLACE_Sr(pol);
//		case IH_REPLACE_Jr:   return generate_mutants_IH_REPLACE_Jr(pol);
//		case IH_REPLACE_SrJr: return generate_mutants_IH_REPLACE_SrJr(pol);
		case Su_INCREMENT:    return generate_mutants_Su_INCREMENT(pol);
		case Su_DECREMENT:    return generate_mutants_Su_DECREMENT(pol);
		case Du_INCREMENT:    return generate_mutants_Du_INCREMENT(pol);
		case Du_DECREMENT:    return generate_mutants_Du_DECREMENT(pol);
		case Sr_INCREMENT:    return generate_mutants_Sr_INCREMENT(pol);
		case Sr_DECREMENT:    return generate_mutants_Sr_DECREMENT(pol);
		case Dr_INCREMENT:    return generate_mutants_Dr_INCREMENT(pol);
		case Dr_DECREMENT:    return generate_mutants_Dr_DECREMENT(pol);
		case SSoD_REPLACE:    return generate_mutants_SSoD_REPLACE(pol);
		case DSoD_REPLACE:    return generate_mutants_DSoD_REPLACE(pol);
		case Ss_INCREMENT:    return generate_mutants_Ss_INCREMENT(pol);
		case Ss_DECREMENT:    return generate_mutants_Ss_DECREMENT(pol);
		case Ds_INCREMENT:    return generate_mutants_Ds_INCREMENT(pol);
		case Ds_DECREMENT:    return generate_mutants_Ds_DECREMENT(pol);
		default:
			return result;
		}
		
	}

	List<RbacMutant> generate_mutants_UR_REPLACE_U(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<User> users = pol.getUser();
		List<UserRoleAssignment> assignments = pol.getUserRoleAssignment();
		RbacMutant mutPol = null;
		for (User u : users) {
			for (UserRoleAssignment ur : assignments) {
				UserRoleAssignment mut = new UserRoleAssignment(u, ur.getRole());
				if(!ur.getUser().equals(u) && !assignments.contains(mut)){
					mutPol = new RbacMutant((RbacPolicy) pol, ur, mut, MutantType.UR_REPLACE_U);
					mutPol.setName(mutPol.getName()+"_"+mutNo);
					mutNo++;
					result.add(mutPol);
				}
			}

		}
		return result;
	}

	List<RbacMutant> generate_mutants_UR_REPLACE_R(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<Role> roles = pol.getRole();
		List<UserRoleAssignment> assignments = pol.getUserRoleAssignment();
		RbacMutant mutPol = null;
		for (Role r : roles) {
			for (UserRoleAssignment ur : assignments) {
				UserRoleAssignment mut = new UserRoleAssignment(ur.getUser(), r);
				if(!ur.getRole().equals(r) && !assignments.contains(mut)){
					mutPol = new RbacMutant((RbacPolicy) pol, ur, mut, MutantType.UR_REPLACE_R);
					mutPol.setName(mutPol.getName()+"_"+mutNo);
					mutNo++;
					result.add(mutPol);

				}
			}

		}
		return result;
	}

	List<RbacMutant> generate_mutants_UR_REPLACE_UR(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<User> users = pol.getUser();
		List<Role> roles = pol.getRole();
		List<UserRoleAssignment> assignments = pol.getUserRoleAssignment();
		RbacMutant mutPol = null;
		for (User u : users) {
			for (Role r : roles) {
				for (UserRoleAssignment ur : assignments) {
					UserRoleAssignment mut = new UserRoleAssignment(u, r);
					if(!ur.getUser().equals(u) && !ur.getRole().equals(r) && !assignments.contains(mut)){
						mutPol = new RbacMutant((RbacPolicy) pol, ur, mut, MutantType.UR_REPLACE_R);
						mutPol.setName(mutPol.getName()+"_"+mutNo);
						mutNo++;
						result.add(mutPol);

					}
				}
			}
		}
		return result;
	}


	//TODO List<RbacMutant> generate_mutants_PR_REPLACE_P(RbacTuple pol){
	//		List<RbacMutant> result = new ArrayList<RbacMutant>();
	//		return result;
	//	}

	//	TODO List<RbacMutant> generate_mutants_PR_REPLACE_R(RbacTuple pol){
	//		List<RbacMutant> result = new ArrayList<RbacMutant>();
	//		return result;
	//	} 

	//	TODO List<RbacMutant> generate_mutants_PR_REPLACE_PR(RbacTuple pol){
	//		List<RbacMutant> result = new ArrayList<RbacMutant>();
	//		return result;
	//	}

	//	TODO List<RbacMutant> generate_mutants_AH_REPLACE_Sr(RbacTuple pol){
	//		List<RbacMutant> result = new ArrayList<RbacMutant>();
	//		return result;
	//	}

	//	TODO List<RbacMutant> generate_mutants_AH_REPLACE_Jr(RbacTuple pol){
	//		List<RbacMutant> result = new ArrayList<RbacMutant>();
	//		return result;
	//	}

	//	TODO List<RbacMutant> generate_mutants_AH_REPLACE_SrJr(RbacTuple pol){
	//		List<RbacMutant> result = new ArrayList<RbacMutant>();
	//		return result;
	//	}

	//	TODO List<RbacMutant> generate_mutants_IH_REPLACE_Sr(RbacTuple pol){
	//		List<RbacMutant> result = new ArrayList<RbacMutant>();
	//		return result;
	//	}

	//	TODO List<RbacMutant> generate_mutants_IH_REPLACE_Jr(RbacTuple pol){
	//		List<RbacMutant> result = new ArrayList<RbacMutant>();
	//		return result;
	//	}

	//	TODO List<RbacMutant> generate_mutants_IH_REPLACE_SrJr(RbacTuple pol){
	//		List<RbacMutant> result = new ArrayList<RbacMutant>();
	//		return result;
	//	}

	List<RbacMutant> generate_mutants_Su_INCREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<Su> origLst = pol.getSu();
		for (Su cc : origLst) {
			Su mutEl = new Su(cc.getUser(), cc.getCardinality()+1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Su_INCREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);

		}
		return result;
	} 

	List<RbacMutant> generate_mutants_Su_DECREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<Su> origLst = pol.getSu();
		for (Su cc : origLst) {
			Su mutEl = new Su(cc.getUser(), (cc.getCardinality()<=0)?0:cc.getCardinality()-1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Su_DECREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	} 

	List<RbacMutant> generate_mutants_Du_INCREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<Du> origLst = pol.getDu();
		for (Du cc : origLst) {
			Du mutEl = new Du(cc.getUser(), cc.getCardinality()+1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Du_INCREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	} 

	List<RbacMutant> generate_mutants_Du_DECREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<Du> origLst = pol.getDu();
		for (Du cc : origLst) {
			Du mutEl = new Du(cc.getUser(), (cc.getCardinality()<=0)?0:cc.getCardinality()-1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Du_DECREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	} 

	List<RbacMutant> generate_mutants_Sr_INCREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<Sr> origLst = pol.getSr();
		for (Sr cc : origLst) {
			Sr mutEl = new Sr(cc.getRole(), cc.getCardinality()+1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Sr_INCREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	} 

	List<RbacMutant> generate_mutants_Sr_DECREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<Sr> origLst = pol.getSr();
		for (Sr cc : origLst) {
			Sr mutEl = new Sr(cc.getRole(), (cc.getCardinality()<=0)?0:cc.getCardinality()-1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Sr_DECREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	} 


	List<RbacMutant> generate_mutants_Dr_INCREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<Dr> origLst = pol.getDr();
		for (Dr cc : origLst) {
			Dr mutEl = new Dr(cc.getRole(), cc.getCardinality()+1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Dr_INCREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	} 

	List<RbacMutant> generate_mutants_Dr_DECREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<Dr> origLst = pol.getDr();
		for (Dr cc : origLst) {
			Dr mutEl = new Dr(cc.getRole(), (cc.getCardinality()<=0)?0:cc.getCardinality()-1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Dr_DECREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	} 


	List<RbacMutant> generate_mutants_SSoD_REPLACE(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<SSoDConstraint> origLst = pol.getSSoDConstraint();
		List<Role> roles = pol.getRole();
		List<Role> set = null; 
		for (Role rMut : roles) {
			for (SSoDConstraint cc : origLst) {
				if(!cc.getSodSet().contains(rMut)){
					for (Role rOrig : cc.getSodSet()) {
						set = new ArrayList<Role>();
						set.addAll(cc.getSodSet());
						set.remove(rOrig);
						set.add(rMut);
						SSoDConstraint mutEl = new SSoDConstraint(set, cc.getCardinality());
						if(origLst.contains(mutEl)) continue;
						RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.SSoD_REPLACE);
						mutPol.setName(mutPol.getName()+"_"+mutNo);
						mutNo++;
						result.add(mutPol);
					}
				}
			}

		}
		return result;
	}

	List<RbacMutant> generate_mutants_DSoD_REPLACE(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<DSoDConstraint> origLst = pol.getDSoDConstraint();
		List<Role> roles = pol.getRole();
		List<Role> set = null; 
		for (Role rMut : roles) {
			for (DSoDConstraint cc : origLst) {
				if(!cc.getSodSet().contains(rMut)){
					for (Role rOrig : cc.getSodSet()) {
						set = new ArrayList<Role>();
						set.addAll(cc.getSodSet());
						set.remove(rOrig);
						set.add(rMut);
						DSoDConstraint mutEl = new DSoDConstraint(set, cc.getCardinality());
						if(origLst.contains(mutEl)) continue;
						RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.DSoD_REPLACE);
						mutPol.setName(mutPol.getName()+"_"+mutNo);
						mutNo++;
						result.add(mutPol);
					}
				}
			}

		}
		return result;
	}

	List<RbacMutant> generate_mutants_Ss_INCREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<SSoDConstraint> origLst = pol.getSSoDConstraint();
		for (SSoDConstraint cc : origLst) {
			SSoDConstraint mutEl = new SSoDConstraint(cc.getSodSet(), cc.getCardinality()+1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Ss_INCREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	}

	List<RbacMutant> generate_mutants_Ss_DECREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<SSoDConstraint> origLst = pol.getSSoDConstraint();
		for (SSoDConstraint cc : origLst) {
			SSoDConstraint mutEl = new SSoDConstraint(cc.getSodSet(), (cc.getCardinality()<=1)?1:cc.getCardinality()-1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Ss_DECREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	}

	List<RbacMutant> generate_mutants_Ds_INCREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<DSoDConstraint> origLst = pol.getDSoDConstraint();
		for (DSoDConstraint cc : origLst) {
			DSoDConstraint mutEl = new DSoDConstraint(cc.getSodSet(), cc.getCardinality()+1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Ds_INCREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	}

	List<RbacMutant> generate_mutants_Ds_DECREMENT(RbacTuple pol){
		int mutNo = 0;
		List<RbacMutant> result = new ArrayList<RbacMutant>();
		List<DSoDConstraint> origLst = pol.getDSoDConstraint();
		for (DSoDConstraint cc : origLst) {
			DSoDConstraint mutEl = new DSoDConstraint(cc.getSodSet(), (cc.getCardinality()<=1)?1:cc.getCardinality()-1);
			if(origLst.contains(mutEl)) continue;
			RbacMutant mutPol = new RbacMutant((RbacPolicy) pol, cc, mutEl, MutantType.Ds_DECREMENT);
			mutPol.setName(mutPol.getName()+"_"+mutNo);
			mutNo++;
			result.add(mutPol);
		}
		return result;
	}


}