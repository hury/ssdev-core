package ctd.test.app;

import ctd.account.user.UserRoleTokenEntity;
import ctd.app.Application;
import ctd.app.ApplicationNode;
import ctd.app.support.Action;
import ctd.app.support.Category;
import ctd.app.support.Module;
import ctd.security.Mode;
import ctd.security.Permission;
import ctd.security.Repository;
import ctd.security.ResourceNode;
import ctd.util.JSONUtils;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public class AppItemTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		UserRoleTokenEntity ur = new UserRoleTokenEntity();
		ur.setId(123);
		ur.setRoleId("system");
		ContextUtils.put(Context.USER_ROLE_TOKEN, ur);
		//ContextUtils.put(Context.REQUEST_APPNODE_DEEP, 1);
		
		ResourceNode r =  Repository.getNode();
		Permission p = new Permission();
		p.setPrincipal("system");
		p.setMode(Mode.parseFromInt(1));
		r.addPermission(p);
		
		ResourceNode appNode = new ResourceNode("chis");
		r.appendChild(appNode);
		
		ResourceNode cataNode = new ResourceNode("catalog1");
		appNode.appendChild(cataNode);
		
		ResourceNode modNode1 =  new ResourceNode("module1");
		cataNode.appendChild(modNode1);
		
		ResourceNode actNode1 =  new ResourceNode("action1");
		modNode1.appendChild(actNode1);
		p = new Permission();
		p.setPrincipal("system");
		p.setMode(Mode.parseFromInt(0));
		actNode1.addPermission(p);
		
		ResourceNode modNode2 =  new ResourceNode("module2");
		cataNode.appendChild(modNode2);
		p = new Permission();
		p.setPrincipal("system");
		p.setMode(Mode.parseFromInt(0));
		modNode2.addPermission(p);
		
		
		ApplicationNode root = new Application();
		root.setId("chis");
		
		ApplicationNode chld1 = new Category();
		chld1.setId("catalog1");
		chld1.setName("主索引管理");
		root.appendChild(chld1);
		
		ApplicationNode chld2 = new Category();
		chld2.setId("catalog2");
		chld2.setName("服务总线");
		root.appendChild(chld2);
		
		Module chld11 = new Module();
		chld11.setId("module1");
		chld11.setScript("app.modules.list.SimpleList");
		chld1.appendChild(chld11);
		
		Module chld12 = new Module();
		chld12.setId("module2");
		chld12.setScript("app.modules.form.SimpleForm");
		chld1.appendChild(chld12);
		
		Action chld111 = new Action();
		chld111.setId("action1");
		chld11.appendChild(chld111);
		
		Action chld112 = new Action();
		chld112.setId("action2");
		chld11.appendChild(chld112);
		
		Action chld113 = new Action();
		chld113.setId("action3");
		chld11.appendChild(chld113);
		
		
		
		System.out.println(JSONUtils.toString(root));
		
		
	}

}
