//package test.com.usp.icmc.labes;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import org.junit.Test;
//
//import com.usp.icmc.labes.rbac.acut.RbacAcut;
//import com.usp.icmc.labes.rbac.acut.RbacRequest;
//import com.usp.icmc.labes.rbac.acut.RbacRequestActivateUR;
//import com.usp.icmc.labes.rbac.acut.RbacRequestAssignUR;
//import com.usp.icmc.labes.rbac.acut.RbacRequestDeactivateUR;
//import com.usp.icmc.labes.rbac.acut.RbacRequestDeassignUR;
//import com.usp.icmc.labes.rbac.model.RbacPolicy;
//import com.usp.icmc.labes.rbac.model.Role;
//import com.usp.icmc.labes.rbac.model.User;
//import com.usp.icmc.labes.utils.PolicyUnderTestUtils;
//
//public class RandomRbacAcuTest {
//
//	@Test
//	public void test() {
//		
//		RbacPolicy pol = PolicyUnderTestUtils.getInstance().create_Masood2010Example1();
//		RbacAcut acut1 = new RbacAcut(pol);
//		RbacAcut acut2 = new RbacAcut(pol);
//		RbacAcut acut3 = new RbacAcut(pol);
//		RbacAcut acut4 = new RbacAcut(acut1);
//		
//		List<RbacRequest> input = new ArrayList<RbacRequest>();
//		
//		for (Role rol: pol.getRole()) {
//			for (User usr: pol.getUser()) {
//				input.add(new RbacRequestAssignUR(usr, rol));
//				input.add(new RbacRequestDeassignUR(usr, rol));
//				input.add(new RbacRequestActivateUR(usr, rol));
//				input.add(new RbacRequestDeactivateUR(usr, rol));
//			}
//		}
//		Random rnd = new Random();
//		
//		for (int i = 0; i < 1000; i++) {
//			RbacRequest rq = input.get(rnd.nextInt(input.size()));
//			
//			assertEquals(acut3.request(rq),acut4.request(rq));
//			
//			assertEquals(acut3.getCurrentState(),acut4.getCurrentState());
//			
//			assertEquals(pol, acut1);
//			assertEquals(pol, acut2);
//			assertEquals(acut1, acut2);
//			
//			assertEquals(acut3, acut4);
//		}
//		
//	}
//
//}
