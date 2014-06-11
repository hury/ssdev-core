package ctd.dictionary.support.ex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ctd.account.tenant.ManageUnit;
import ctd.account.tenant.Tenant;
import ctd.account.tenant.TenantController;
import ctd.controller.exception.ControllerException;
import ctd.dictionary.DictionaryItem;


public class PersonnelDictionary extends ManageUnitDictionary {
	protected static final Logger logger = LoggerFactory.getLogger(PersonnelDictionary.class);
	private static final long serialVersionUID = -7687294291396751360L;
	protected HashMap<String,DictionaryItem> items = new LinkedHashMap<String, DictionaryItem>();
	private boolean inited;
	

	public PersonnelDictionary(String tenantId) {
		super(tenantId);
		setId("personnel@" + tenantId);
		setName("人员信息");
	}
	
	@Override
	public void addItem(DictionaryItem item){
		item.setLeaf(true);
		items.put(item.getKey(), item);
	}
	
	public void updateItem(DictionaryItem item){
		addItem(item);
		if(this.inited){
			onItemUpdate(item);
		}
	}
	
	@Override
	public void removeItem(String key) {
		items.remove(key);
		if(this.inited){
			onItemRemoved(key);
		}
	}
	
	@Override
	public DictionaryItem getItem(String key) {
		return items.get(key);
	}
	
	public String getText(String key){
		if(items.containsKey(key)){
			return items.get(key).getText();
		}
		return "";
	}
	
	
	@Override
	protected DictionaryItem toDictionaryItem(ManageUnit unit){
		DictionaryItem item = super.toDictionaryItem(unit);
		item.setLeaf(false);
		return item;
	}
	
	protected List<DictionaryItem> getAllChilds(ManageUnit unit,String query){
		List<DictionaryItem> ls = null;
		if(unit.hasChlid()){
			ls = super.getAllChilds(unit, query);
			ls.addAll(getPersonnelItems(unit));
		}
		else{
			ls = getPersonnelItems(unit);
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
			xpath.append("//employee[contains(@mCode,'").append(query).append("')]");
			List<Element> els = doc.getRootElement().selectNodes(xpath.toString());

			for(Element el : els){
				String id = el.attributeValue("id");
				ls.add(getItem(id));
			}
		} 
		catch (ControllerException e) {
			
		}
		return ls;
	}
	
	@Override
	public boolean isInited(){
		return inited;
	}
	
	@SuppressWarnings("unchecked")
	private List<DictionaryItem> getPersonnelItems(ManageUnit unit){
		List<DictionaryItem> ls = new ArrayList<DictionaryItem>();
		
		try {
			Tenant tenant = TenantController.instance().get(tenantId);
			Document doc = tenant.getDefineDoc();
			Element unitEl = (Element)doc.getRootElement().selectSingleNode("//unit[@id='" + unit.getId() + "']");
			if(unitEl != null){
				List<Element> staffEls = (List<Element>)unitEl.selectNodes("employee");
				for(Element staffEl : staffEls){
					ls.add(getItem(staffEl.attributeValue("id")));
				}
			}
		} 
		catch (ControllerException e) {
			
		}
		
		return ls;
	}
	
	private void configDictionaryItem(DictionaryItem item){
		if("2".equals(item.getProperty("gender",String.class))){
			item.setProperty("iconCls", "female");
		}
		else{
			item.setProperty("iconCls", "male");
		}
	}
	
	@Override
	public void init() throws ControllerException{
		
		Tenant  tenant = TenantController.instance().get(tenantId);
		Document doc = tenant.getDefineDoc();
		Collection<DictionaryItem> c = items.values();
		for(DictionaryItem item : c){
			configDictionaryItem(item);
			String unitId = item.getProperty("manageUnit",String.class);
			Element unitEl = (Element)doc.getRootElement().selectSingleNode("//unit[@id='" + unitId + "']");
			if(unitEl != null){
				Element staffEl = unitEl.addElement("employee");
				staffEl.addAttribute("id", item.getKey());
				staffEl.addAttribute("mCode",item.getMCode());
			}
		}
		inited = true;
	};
	
	private void onItemUpdate(DictionaryItem item){

		try {
			Tenant tenant = TenantController.instance().get(tenantId);
			Document doc = tenant.getDefineDoc();
			Element root = doc.getRootElement();
			
			Element staffEl = (Element)root.selectSingleNode("//employee[@id='" + item.getKey() +"']");
			if(staffEl != null){
				staffEl.getParent().remove(staffEl);
			}
			configDictionaryItem(item);
			String unitId = item.getProperty("manageUnit",String.class);
			Element unitEl = (Element)root.selectSingleNode("//unit[@id='" + unitId + "']");
			if(unitEl != null){
				staffEl = unitEl.addElement("employee");
				staffEl.addAttribute("id", item.getKey());
				staffEl.addAttribute("mCode",item.getMCode());
			}
		} 
		catch (Exception e) {
			logger.error("item[" + item.getKey() + "] update failed.",e);
		}
		
	}
	
	private void onItemRemoved(String key){
		try {
			Tenant tenant = TenantController.instance().get(tenantId);
			Document doc = tenant.getDefineDoc();
			Element root = doc.getRootElement();
			Element staffEl = (Element)root.selectSingleNode("//employee[@id='" + key +"']");
			if(staffEl != null){
				staffEl.getParent().remove(staffEl);
			}
		} 
		catch (Exception e) {
			logger.error("item[" + key + "] remove failed.",e);
		}
	}
	
	@Override
	public Long getlastModify() {
		return lastModi;
	}

}
