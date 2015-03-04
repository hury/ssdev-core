package ctd.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ctd.account.UserRoleToken;
import ctd.controller.support.AbstractConfigurable;
import ctd.schema.exception.ValidateException;
import ctd.security.Condition;
import ctd.security.Mode;
import ctd.security.Permission;
import ctd.security.Repository;
import ctd.security.ResourceNode;


public class Schema extends AbstractConfigurable{
	public static final String KEY_GEN_AUTO = "auto";
	public static final String KEY_GEN_ASSIGN = "assign";
	
	private static final long serialVersionUID = -271602734048406147L;
	private static final SchemaItemComparator schemaItemComparator = new SchemaItemComparator();
	
	private String annotationClass;
	private String entityName;
	private String alias;
	private String key;
	private String sortInfo;
	private String keyGenerator;
	private Map<String,SchemaItem> items = new ConcurrentHashMap<>();
	
	public Schema(){
		
	}
	
	public Schema(String id){
		this.id = id;
	}

	public String getEntityName() {	
		if(entityName != null){
			return entityName;
		}
		else{
			return id;
		}
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void addItem(SchemaItem it){
		it.setIndex(items.size());
		items.put(it.getId(), it);
	}

	
	public void removeItem(String id){
		items.remove(id);
	}
	
	public SchemaItem getItem(String id){
		return items.get(id);
	}
	
	public List<SchemaItem> getItems(){
		List<SchemaItem> beans = new ArrayList<SchemaItem>();
		beans.addAll(items.values());
		Collections.sort(beans, schemaItemComparator);
		return beans;
	}
	
	public boolean hasItem(String id){
		return items.containsKey(id);
	}
	
	public int getItemCount(){
		return items.size();
	}

	public String getAlias() {
		return alias;
	}
	
	public String getName(){
		return getAlias();
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSortInfo() {
		return sortInfo;
	}

	public void setSortInfo(String sortInfo) {
		this.sortInfo = sortInfo;
	}

	public String getKeyGenerator() {
		return keyGenerator;
	}

	public void setKeyGenerator(String keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	
	public Boolean isAutoKeyGenerator(){
		return KEY_GEN_AUTO.equals(keyGenerator);
	}
	
	public int getMode(){
		ResourceNode node = Repository.getNode(id);
		UserRoleToken ur = UserRoleToken.getCurrent();
		if(ur == null){
			return Mode.NoneAccessMode.getValue();
		}
		String principal = ur.getRoleId();
		return node.lookupPermission(principal).getMode().getValue();
	}
	
	public String getAnnotationClass() {
		return annotationClass;
	}

	public void setAnnotationClass(String annotationClass) {
		this.annotationClass = annotationClass;
	}

	public Condition lookupCondition(String action){
		ResourceNode node = Repository.getNode(id);
		String principal = UserRoleToken.getCurrent().getRoleId();
		return node.lookupPermission(principal).getCondition(action);
	}
	
	public Permission lookupPremission(){
		ResourceNode node = Repository.getNode(id);
		String principal = UserRoleToken.getCurrent().getRoleId();
		return node.lookupPermission(principal);
	}
	
	public void validate(Object o) throws ValidateException{
		
	}

}
