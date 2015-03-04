package ctd.security;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceNode implements Serializable{
	private static final long serialVersionUID = -5756434318986493239L;

	protected String id;
	protected Map<String,Permission> permissions = new ConcurrentHashMap<String,Permission>();
	
	protected ResourceNode parent;
	protected Map<String,ResourceNode> childNodes;
	
	public ResourceNode(String id){
		this.id = id;
	}
	
	public ResourceNode() {
		
	}

	public void setId(String id){
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public boolean hasChild(String id){
		if(childNodes == null){
			return false;
		}
		return childNodes.containsKey(id);
	}
	
	public ResourceNode getChild(String id){
		if(hasChild(id)){
			return childNodes.get(id);
		}
		return this;
	}
	
	public void appendChild(ResourceNode n){
		if(childNodes == null){
			childNodes = new ConcurrentHashMap<String,ResourceNode>();
		}
		childNodes.put(n.getId(), n);
		n.setParent(this);
	}

	public ResourceNode getParent() {
		return parent;
	}

	public void setParent(ResourceNode parent) {
		this.parent = parent;
	}

	public void addPermission(Permission p) {
		permissions.put(p.getPrincipal(), p);
	}
	
	public Permission lookupPermission(String principal){
		
		if(permissions.containsKey(principal)){
			return permissions.get(principal);
		}
		else{
			if(permissions.containsKey(Permission.OTHERS_PRINCIPAL)){
				return permissions.get(Permission.OTHERS_PRINCIPAL);
			}
			
			ResourceNode parent = this.getParent();
			if(parent != null){
				return parent.lookupPermission(principal);
			}
			return Permission.NegativePermission;
		}
	}
}
