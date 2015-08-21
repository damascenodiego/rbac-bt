package com.usp.icmc.labes.fsm.testing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.usp.icmc.labes.fsm.FsmTransition;
import com.usp.icmc.labes.rbac.acut.RbacAcut;
import com.usp.icmc.labes.rbac.acut.RbacRequest;
import com.usp.icmc.labes.utils.FsmTestCaseSimilarityUtils;
import com.usp.icmc.labes.utils.RbacUtils;
import com.usp.icmc.labes.utils.RbacUtils.RbacFaultType;
import com.usp.icmc.labes.rbac.model.ActivationHierarchy;
import com.usp.icmc.labes.rbac.model.DSoDConstraint;
import com.usp.icmc.labes.rbac.model.Dr;
import com.usp.icmc.labes.rbac.model.Du;
import com.usp.icmc.labes.rbac.model.InheritanceHierarchy;
import com.usp.icmc.labes.rbac.model.PermissionRoleAssignment;
import com.usp.icmc.labes.rbac.model.RbacElement;
import com.usp.icmc.labes.rbac.model.RbacMutableElement;
import com.usp.icmc.labes.rbac.model.RbacPolicy;
import com.usp.icmc.labes.rbac.model.SSoDConstraint;
import com.usp.icmc.labes.rbac.model.SoDConstraint;
import com.usp.icmc.labes.rbac.model.Sr;
import com.usp.icmc.labes.rbac.model.Su;
import com.usp.icmc.labes.rbac.model.UserRoleActivation;
import com.usp.icmc.labes.rbac.model.UserRoleAssignment;

public class PairTestRbacSimilarity extends PairTestSimilarity {

	public PairTestRbacSimilarity(FsmTestCase tci, FsmTestCase tcj, RbacAcut acut) {
		int ndt = FsmTestCaseSimilarityUtils.getInstance().calcNdt(tci,tcj);
		double avgij = (tci.getPath().size()+tcj.getPath().size())/2.0;
		double dss = ndt/avgij;
		if(tci.getPath().size()<tcj.getPath().size()){
			this.test00 = tci;
			this.test01 = tcj;
		}else{
			this.test00 = tcj;
			this.test01 = tci;
		}
		if(!(dss==0)){
			double[] ra_i = calcRA(acut,tci,avgij);
			double[] ra_j = calcRA(acut,tcj,avgij);

			double appValue = 0;
			for (double d : ra_i) appValue += d;
			for (double d : ra_j) appValue += d;

			double priorityValue = 0;
			if (ra_i[0] == ra_j[0] && ra_i[0] == 1) priorityValue = 3;
			else if ((ra_i[0] == 1 ^ ra_j[0] == 1)) priorityValue = 2;
			else if ((0 < ra_i[0] && ra_i[0] < 1) && (0 < ra_j[0] && ra_j[0] < 1)) priorityValue = 1;

			similarity = dss + appValue + priorityValue;
		}else{
			similarity = 0;
		}

	}

	double[] calcRA(RbacAcut acut, FsmTestCase tci, double avgij) {
		Set<RbacMutableElement>  notMatch = new HashSet<>();
		notMatch.addAll(acut.getPolicy().getUserRoleAssignment());
		notMatch.addAll(acut.getPolicy().getPermissionRoleAssignment());
		notMatch.addAll(acut.getPolicy().getActivationHierarchy());
		notMatch.addAll(acut.getPolicy().getInheritanceHierarchy());
		notMatch.addAll(acut.getPolicy().getSu());
		notMatch.addAll(acut.getPolicy().getDu());
		notMatch.addAll(acut.getPolicy().getSr());
		notMatch.addAll(acut.getPolicy().getDr());
		notMatch.addAll(acut.getPolicy().getDSoDConstraint());
		notMatch.addAll(acut.getPolicy().getSSoDConstraint());

		int totalMutableElements = notMatch.size();

		int urMatches = 0;
		Map<String, RbacRequest> rqMap = new HashMap<String, RbacRequest>();

		for (FsmTransition tr: tci.getPath()) {
			rqMap.putIfAbsent(tr.getInput(), RbacUtils.getInstance().createRbacRequest(tr.getInput(),acut));
			RbacRequest rq = rqMap.get(tr.getInput());
			acut.request(rqMap.get(tr.getInput()));
			removeMatching(notMatch,rq);
			urMatches+=calcMatchingUR(rq,acut.getPolicy());
		}
		acut.reset();

		int totalNotMatching = notMatch.size();

		double pad = ((double)totalMutableElements-totalNotMatching)/totalMutableElements;
		double as = 0;
		as += ((double)urMatches)/avgij;
		if(acut.getPolicy().getProperties().containsKey(RbacFaultType.SuFailed.name())) as += ((Set) acut.getPolicy().getProperties().get(RbacFaultType.SuFailed.name())).size();
		if(acut.getPolicy().getProperties().containsKey(RbacFaultType.SrFailed.name())) as += ((Set) acut.getPolicy().getProperties().get(RbacFaultType.SrFailed.name())).size();
		if(acut.getPolicy().getProperties().containsKey(RbacFaultType.SsodFailed.name())) as += ((Set) acut.getPolicy().getProperties().get(RbacFaultType.SsodFailed.name())).size();
		double ac = 0;
		if(acut.getPolicy().getProperties().containsKey(RbacFaultType.DuFailed.name())) as += ((Set) acut.getPolicy().getProperties().get(RbacFaultType.DuFailed.name())).size();
		if(acut.getPolicy().getProperties().containsKey(RbacFaultType.DrFailed.name())) as += ((Set) acut.getPolicy().getProperties().get(RbacFaultType.DrFailed.name())).size();
		if(acut.getPolicy().getProperties().containsKey(RbacFaultType.DsodFailed.name())) as += ((Set) acut.getPolicy().getProperties().get(RbacFaultType.DsodFailed.name())).size();
		//TODO AhFailed	if(acut.getPolicy().getProperties().containsKey(RbacFaultType.AhFailed.name())) as += ((Set) acut.getPolicy().getProperties().get(RbacFaultType.AhFailed.name())).size();
		double pr = 0;
		//TODO IhFailed	if(acut.getPolicy().getProperties().containsKey(RbacFaultType.AhFailed.name())) as += ((Set) acut.getPolicy().getProperties().get(RbacFaultType.AhFailed.name())).size();
		//TODO PRFailed	if(acut.getPolicy().getProperties().containsKey(RbacFaultType.AhFailed.name())) as += ((Set) acut.getPolicy().getProperties().get(RbacFaultType.AhFailed.name())).size();

		//		System.err.println(pad+" "+as+" "+ac+" "+pr);
		acut.getPolicy().getProperties().clear();

		double [] ra = {pad, as, ac,pr};
		return ra;

	}

	private int calcMatchingUR(RbacRequest rq, RbacPolicy policy) {
		int totMatch = 0;
		for (UserRoleAssignment ur : policy.getUserRoleAssignment()) {
			if(ur.getRole().equals(rq.getRole()) || ur.getUser().equals(rq.getUser())) totMatch++;
		}
		return totMatch;
	}

	private void removeMatching(Set<RbacMutableElement> notMatch, RbacRequest rq) {
		Set<RbacMutableElement>  match = new HashSet<>();
		for (RbacMutableElement el : notMatch) {
			if(el instanceof UserRoleAssignment){
				if	(
						((UserRoleAssignment) el).getUser().equals(rq.getUser())||
						((UserRoleAssignment) el).getRole().equals(rq.getRole())
						) match.add(el);
			}else if(el instanceof PermissionRoleAssignment){
				if	(
						((PermissionRoleAssignment) el).getPermission().equals(rq.getPermission()) ||
						((PermissionRoleAssignment) el).getRole().equals(rq.getRole())
						) match.add(el);	
			}else if(el instanceof ActivationHierarchy){
				if	(
						((ActivationHierarchy) el).getJunior().equals(rq.getRole()) ||
						((ActivationHierarchy) el).getSenior().equals(rq.getRole())
						) match.add(el);
			}else if(el instanceof InheritanceHierarchy){
				if	(
						((InheritanceHierarchy) el).getJunior().equals(rq.getRole()) ||
						((InheritanceHierarchy) el).getSenior().equals(rq.getRole())
						) match.add(el);
			}else if(el instanceof Su){
				if	(
						((Su) el).getUser().equals(rq.getUser())
						) match.add(el);
			}else if(el instanceof Du){
				if	(
						((Du) el).getUser().equals(rq.getUser())
						) match.add(el);
			}else if(el instanceof Sr){
				if	(
						((Sr) el).getRole().equals(rq.getRole())
						) match.add(el);
			}else if(el instanceof Dr){
				if	(
						((Dr) el).getRole().equals(rq.getRole())
						) match.add(el);
			}else if(el instanceof SSoDConstraint || el instanceof DSoDConstraint){
				if	(
						((SoDConstraint)el).getSodSet().contains(rq.getRole())
						) match.add(el);
			}
		}
		notMatch.removeAll(match);
	}
}