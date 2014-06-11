package ctd.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import ctd.account.UserRoleToken;
import ctd.security.Permission;
import ctd.security.Repository;
import ctd.security.ResourceNode;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;


public abstract class ApplicationNode implements Serializable{
	private static final long serialVersionUID = 5829201367508285016L;
	protected ApplicationNode parent;
	protected HashMap<String,ApplicationNode> items = new LinkedHashMap<String,ApplicationNode>();
	
	protected String id;
	protected String name;
	protected String icon;
	protected int deep;
	
	public void appendChild(ApplicationNode item){
		item.setParent(this);
		items.put(item.getId(), item);
	}
	
	public ApplicationNode getChild(String id){
		return items.get(id);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends ApplicationNode> List<T> getAuthorizedItems(){
		List<T> ls = new ArrayList<T>();
		String[] paths = getNodePath();
		ResourceNode node = Repository.getNode(paths);
		String principal = UserRoleToken.getCurrent().getRoleId();
		Collection<ApplicationNode> c = items.values();
		for(ApplicationNode item : c){
			Permission p =  node.getChild(item.getId()).lookupPermission(principal);
			if(p.getMode().isAccessible()){
				ls.add((T) item);
			}
		}
		return ls;
	}
	
	protected int getRequestDeep(){
		if(ContextUtils.hasKey(Context.REQUEST_APPNODE_DEEP)){
			return (Integer)ContextUtils.get(Context.REQUEST_APPNODE_DEEP);
		}
		return Integer.MAX_VALUE;
	}
	
	protected String[] getNodePath(){
		int size = deep + 1;
		String[] paths = new String[size];
		ApplicationNode item  = this;
		while(item != null){
			paths[item.deep()] = item.getId(); 
			item = item.getParent();
		}
		return paths;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	private ApplicationNode getParent() {
		return parent;
	}

	public int deep() {
		return deep;
	}
	
	public void setParent(ApplicationNode parent){
		this.parent = parent;
		deep = parent.deep() + 1;
	}
	
	public int hashCode(){
		return deep * 31 + id.hashCode();
	}
}
