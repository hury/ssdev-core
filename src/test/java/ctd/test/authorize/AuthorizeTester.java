package ctd.test.authorize;

import ctd.security.Mode;
import ctd.security.Permission;
import ctd.security.ResourceNode;

public class AuthorizeTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceNode root = new ResourceNode("Root");
		
		Permission p1 = new Permission("1111"); 
		p1.setPrincipal("system");
		root.addPermission(p1);
		
		Permission p2 = new Permission("0001"); 
		p2.setPrincipal(Permission.OTHERS_PRINCIPAL);
		root.addPermission(p2);
		
		
		
		ResourceNode cld1 = new ResourceNode("app1");
		root.appendChild(cld1);
		
		Permission p3 = new Permission("0111"); 
		p3.setPrincipal(Permission.OTHERS_PRINCIPAL);
		cld1.addPermission(p3);
		
		System.out.println(cld1.lookupPermission("sean").getMode().isRemovable());
		
		
	}

}
