package ctd.account.tenant;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import ctd.controller.support.AbstractConfigurable;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;


public class ManageUnit extends AbstractConfigurable{
	private static final long serialVersionUID = 5344390118571868091L;
	
	protected HashMap<String,ManageUnit> children = new LinkedHashMap<String,ManageUnit>();
	protected ManageUnit parent;
	protected Set<String> roles = new HashSet<String>();
	private int deep;
	private String name;
	private String type;
	
	public void appendChild(ManageUnit unit){
		unit.setParent(this);
		children.put(unit.getId(), unit);
	}
	
	public ManageUnit getChild(String id){
		return children.get(id);
	}
	
	public void addRoleId(String id){
		roles.add(id);
	}
	
	public boolean hasChlid(){
		return children.size() > 0;
	}
	
	public Collection<ManageUnit> getChildren(){
		if(deep >= getRequestDeep()){
			return null;
		}
		Collection<ManageUnit> c =  children.values();
		if(c.isEmpty()){
			return null;
		}
		return c;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	protected ManageUnit getParent() {
		return parent;
	}

	private void setParent(ManageUnit parent) {
		this.parent = parent;
		deep = parent.deep() + 1;
	}
	
	protected int getRequestDeep(){
		if(ContextUtils.hasKey(Context.REQUEST_UNIT_DEEP)){
			return (Integer)ContextUtils.get(Context.REQUEST_UNIT_DEEP);
		}
		return Integer.MAX_VALUE;
	}
	
	protected int deep(){
		return deep;
	}

}
