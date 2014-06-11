package ctd.account.tenant;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;

import ctd.account.UserRoleToken;
import ctd.app.Application;
import ctd.app.ApplicationController;
import ctd.controller.exception.ControllerException;
import ctd.security.Permission;
import ctd.security.Repository;
import ctd.security.ResourceNode;

public class Tenant extends ManageUnit {
	public final static String SEPARATOR = "-";
	public final static String PLACEHOLDER = "@";
	private static final long serialVersionUID = 4997223091020530637L;
	private Set<String> installedApps = new LinkedHashSet<String>();
	private HashMap<String,ManageUnit> mapping = new HashMap<String,ManageUnit>();
	private String profileHome;
	private Document defineDoc;
	
	public void setDefineDoc(Document doc){
		this.defineDoc = doc;
	}
	
	public Document getDefineDoc(){
		return defineDoc;
	}
	
	public ManageUnit lookup(String id){
		return mapping.get(id);
	}
	
	public boolean contains(String id){
		return mapping.containsKey(id);
	}
	
	public void addToMapping(ManageUnit unit){
		mapping.put(unit.getId(), unit);
	}
	
	public void addInstalledApp(String id){
		installedApps.add(id);
	}
	
	public List<Application> findAuthorizedApps() throws ControllerException{
		List<Application> apps = new ArrayList<Application>();
		ResourceNode node = Repository.getNode();
		String principal = UserRoleToken.getCurrent().getRoleId();
		for(String id : installedApps){
			
			Permission p =  node.getChild(id).lookupPermission(principal);
			
			if(p.getMode().isAccessible()){
				apps.add(ApplicationController.instance().get(id));
			}
		}
		return apps;
	}

	public String getProfileHome() {
		return profileHome;
	}

	public void setProfileHome(String profileHome) {
		this.profileHome = profileHome;
	}
	
}
