package ctd.dictionary.support.ex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import ctd.account.tenant.ManageUnit;
import ctd.account.tenant.Tenant;
import ctd.account.tenant.TenantController;
import ctd.controller.exception.ControllerException;
import ctd.controller.support.AbstractConfigurable;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryItem;
import ctd.dictionary.SliceTypes;

public class ManageUnitDictionary extends AbstractConfigurable implements Dictionary {
	private static final long serialVersionUID = 5296241785479228728L;
	protected String tenantId;

	public ManageUnitDictionary(String tenantId){
		this.tenantId = tenantId;
		setId("manageUnit@" + tenantId);
		setName("管理单元");
	}
	
	public String getTenantId(){
		return tenantId;
	}
	
	@Override
	public void addItem(DictionaryItem item) {
		
	}
	
	@Override
	public void updateItem(DictionaryItem item) {
		
	}

	@Override
	public void removeItem(String key) {
		
	}

	@Override
	public DictionaryItem getItem(String key) {
		try {
			Tenant tenant = TenantController.instance().get(tenantId);
			ManageUnit unit = tenant.lookup(key);
			return toDictionaryItem(unit);
		} 
		catch (ControllerException e) {
			return null;
		}
	}
	
	protected DictionaryItem toDictionaryItem(ManageUnit unit){
		DictionaryItem item = new DictionaryItem();
		item.setKey(unit.getId());
		item.setText(unit.getName());
		item.setProperties(unit.getProperties());
		
		if(!unit.hasChlid()){
			item.setLeaf(true);
		}
		if(!item.hasProperty("iconCls")){
			item.setProperty("iconCls", "group");
		}
		
		return item;
	}

	@Override
	public boolean keyExist(String key) {
		try {
			Tenant tenant = TenantController.instance().get(tenantId);
			return tenant.contains(key);
		} 
		catch (ControllerException e) {
			return false;
		}
	}

	@Override
	public String getText(String key) {
		try {
			ManageUnit unit = TenantController.lookupManageUnit(key);
			return unit.getName();
		} 
		catch (ControllerException e) {
			return key;
		}
	}

	@Override
	public List<DictionaryItem> getSlice(String parentKey, int sliceType,String query) {
		
		List<DictionaryItem> ls = null;
		ManageUnit unit;
		try {
			Tenant tenant = TenantController.instance().get(tenantId);
			if(StringUtils.isEmpty(parentKey) || tenant.getId().equals(parentKey)){
				unit = tenant;
			}
			else{
				unit = tenant.lookup(parentKey);
			}
		}
		catch (ControllerException e) {
			throw new IllegalStateException("lookup tenantId[" + tenantId + "]unit[" + parentKey +"] failed");
		}
		
		switch(sliceType){
			case SliceTypes.CHILD_ALL:
				ls = getAllChilds(unit,query);
				break;
			case SliceTypes.CHILD_FOLDER:
				ls = getChildFolders(unit,query);
				break;
			case SliceTypes.CHILD_LEAF:
				ls = getChildLeaves(unit,query);
				break;
			case SliceTypes.ALL:
				ls = getAllItems(unit,query);
				break;
			
		}
		
		return ls;
	}
	
	@SuppressWarnings("unchecked")
	protected List<DictionaryItem> getAllItems(ManageUnit unit,String query){
		List<DictionaryItem> ls = new ArrayList<DictionaryItem>();
		try {
			String parentKey = unit.getId();
			Tenant tenant = TenantController.instance().get(tenantId);
			Document doc = tenant.getDefineDoc();
			StringBuilder xpath = new StringBuilder();
			if(!parentKey.equals(tenant.getId())){
				xpath.append("//unit[@id='").append(parentKey).append("']");
			}
			xpath.append("//unit[contains(@mCode,'").append(query).append("')]");
			List<Element> els = doc.getRootElement().selectNodes(xpath.toString());

			for(Element el : els){
				String id = el.attributeValue("id");
				DictionaryItem item = toDictionaryItem(tenant.lookup(id));
				ls.add(item);
			}
		} 
		catch (ControllerException e) {
			
		}
		return ls;
	}
	
	protected List<DictionaryItem> getAllChilds(ManageUnit unit,String query){
		List<DictionaryItem> ls = new ArrayList<DictionaryItem>();
		Collection<ManageUnit> units = unit.getChildren();
		for(ManageUnit c : units){
			ls.add(toDictionaryItem(c));
		}
		return ls;
	}
	
	protected List<DictionaryItem> getChildFolders(ManageUnit unit,String query){
		List<DictionaryItem> ls = new ArrayList<DictionaryItem>();
		Collection<ManageUnit> units = unit.getChildren();
		for(ManageUnit c : units){
			if(c.hasChlid()){
				ls.add(toDictionaryItem(c));
			}
		}
		return ls;
	}
	
	protected List<DictionaryItem> getChildLeaves(ManageUnit unit,String query){
		List<DictionaryItem> ls = new ArrayList<DictionaryItem>();
		Collection<ManageUnit> units = unit.getChildren();
		for(ManageUnit c : units){
			if(!c.hasChlid()){
				ls.add(toDictionaryItem(c));
			}
		}
		return ls;
	}
	
	@Override
	public Long getlastModify() {
		
		try {
			Tenant tenant = TenantController.instance().get(tenantId);
			return tenant.getlastModify();
		} 
		catch (ControllerException e) {
			return 0l;
		}
		
	}

	@Override
	public boolean isInited() {
		return true;
	}

	@Override
	public void init() throws ControllerException{
		
	}

	@Override
	public boolean isReloadable() {
		return false;
	}

	@Override
	public String getText(Object key) {
		return getText(String.valueOf(key));
	}

	@Override
	public void destory() {
	}

	

}
