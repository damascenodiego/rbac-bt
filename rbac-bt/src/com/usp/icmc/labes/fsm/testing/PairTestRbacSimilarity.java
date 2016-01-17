package com.usp.icmc.labes.fsm.testing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
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
		//		double dss = 1-FsmTestCaseSimilarityUtils.getInstance().calcJaccardSimilarity(tci,tcj);
		double dss = FsmTestCaseSimilarityUtils.getInstance().calcNdtAvgLen(tci,tcj);
		if(tci.getPath().size()<tcj.getPath().size()){
			this.test00 = tci;
			this.test01 = tcj;
		}else{
			this.test00 = tcj;
			this.test01 = tci;
		}
		if(!(dss==0)){
			double[] ra_i = calcRA(acut,tci);
			double[] ra_j = calcRA(acut,tcj);

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

	double[] calcRA(RbacAcut acut, FsmTestCase tci) {
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

		Set<UserRoleAssignment> urMatching = new HashSet<>();
		Map<String, RbacRequest> rqMap = new HashMap<String, RbacRequest>();

		Properties acutProps = new Properties();

		for (FsmTransition tr: tci.getPath()) {
			rqMap.putIfAbsent(tr.getInput(), RbacUtils.getInstance().createRbacRequest(tr.getInput(),acut));
			RbacRequest rq = rqMap.get(tr.getInput());
			//			acut.request(rqMap.get(tr.getInput()));
			//			for (Object obj: acut.getPolicy().getProperties().keySet()) {
			for (RbacFaultType faultType: RbacFaultType.values()) {
				//				tr.getProperties().putIfAbsent(obj, acut.getPolicy().getProperties().get(obj));
				//				((Set) tr.getProperties().get(obj)).addAll((Set) tr.getProperties().get(obj));
				acutProps.putIfAbsent(faultType, new HashSet<>());
				((Set) acutProps.get(faultType)).addAll((Set) tr.getProperties().get(faultType));
			}
			removeMatching(notMatch,rq);
			calcMatchingUR(rq,acut.getPolicy(),urMatching);
		}
		//		acut.reset();

		int totalNotMatching = notMatch.size();

		double pad = ((double)totalMutableElements-totalNotMatching)/totalMutableElements;
		double as = 0;
		as += ((double)urMatching.size());
		if(acutProps.containsKey(RbacFaultType.SuFailed)) as += ((Set) acutProps.get(RbacFaultType.SuFailed)).size();
		if(acutProps.containsKey(RbacFaultType.SrFailed)) as += ((Set) acutProps.get(RbacFaultType.SrFailed)).size();
		if(acutProps.containsKey(RbacFaultType.SsodFailed)) as += ((Set) acutProps.get(RbacFaultType.SsodFailed)).size();
		double ac = 0;
		if(acutProps.containsKey(RbacFaultType.DuFailed)) ac += ((Set) acutProps.get(RbacFaultType.DuFailed)).size();
		if(acutProps.containsKey(RbacFaultType.DrFailed)) ac += ((Set) acutProps.get(RbacFaultType.DrFailed)).size();
		if(acutProps.containsKey(RbacFaultType.DsodFailed)) ac += ((Set) acutProps.get(RbacFaultType.DsodFailed)).size();
		//TODO AhFailed	if(acutProps.containsKey(RbacFaultType.AhFailed)) as += ((Set) acutProps.get(RbacFaultType.AhFailed)).size();
		double pr = 0;
		//TODO IhFailed	if(acutProps.containsKey(RbacFaultType.IhFailed)) pr += ((Set) acutProps.get(RbacFaultType.IhFailed)).size();
		//TODO PRFailed	if(acutProps.containsKey(RbacFaultType.PRFailed)) pr += ((Set) acutProps.get(RbacFaultType.PRFailed)).size();

		//		System.err.println(pad+" "+as+" "+ac+" "+pr);
		//		acut.getPolicy().getProperties().clear();

		double [] ra = {pad, as, ac,pr};
		return ra;

	}

	private void calcMatchingUR(RbacRequest rq, RbacPolicy policy, Set<UserRoleAssignment> urMatching) {
		for (UserRoleAssignment ur : policy.getUserRoleAssignment()) {
			if(ur.getRole().equals(rq.getRole()) && ur.getUser().equals(rq.getUser())) urMatching.add(ur);
		}
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