package ctd.account.tenant.loader;

import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;

import ctd.account.tenant.ManageUnit;
import ctd.account.tenant.Tenant;
import ctd.account.tenant.TenantHomeUtils;
import ctd.controller.support.AbstractConfigurableLoader;
import ctd.controller.exception.ControllerException;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryController;
import ctd.dictionary.support.ex.ManageUnitDictionary;
import ctd.util.PyConverter;
import ctd.util.converter.ConversionUtils;

public class TenantLocalLoader extends AbstractConfigurableLoader<Tenant> {
	
	private final static String FILENAME = "manageUnit.org";
	
	@Override
	protected String getPathFromId(String id){
		return TenantHomeUtils.getHomePath(id) + FILENAME;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Tenant createInstanceFormDoc(String id, Document doc, long lastModi) throws ControllerException {
		
		Element root = doc.getRootElement();
		if(root == null){
			throw new ControllerException(ControllerException.PARSE_ERROR,"root element missing.");
		}
		try{
			Tenant tenant = ConversionUtils.convert(root, Tenant.class);
			tenant.setId(id);
			tenant.setLastModify(lastModi);
			tenant.setProfileHome(TenantHomeUtils.getHomePath(id));
			
			List<Element> installedApps = root.selectNodes("installedApps/app");
			for(Element appEl : installedApps){
				tenant.addInstalledApp(appEl.attributeValue("id"));
			}
			parseChilds(tenant,tenant,root);
			tenant.setDefineDoc(doc);
			registerDictionary(tenant);
			
			return tenant;
		}
		catch(Exception e){
			throw new ControllerException(e,ControllerException.PARSE_ERROR,"Tenant[" + id + "] init unknow error:"+ e.getMessage());
		}	
	}
	
	@SuppressWarnings("unchecked")
	private void parseChilds(Tenant tenant,ManageUnit parent,Element el){
		setupProperties(parent,el);
		List<Element> children = el.elements("unit");
		String prefixTenantId = parent.getId() + Tenant.SEPARATOR;
		for(Element u : children){
			ManageUnit unit = ConversionUtils.convert(u, ManageUnit.class);
			
			if(!unit.getId().startsWith(prefixTenantId)){
				unit.setId(prefixTenantId + unit.getId());
				u.addAttribute("id", unit.getId());
			}
			parent.appendChild(unit);
			tenant.addToMapping(unit);
			parseChilds(tenant,unit,u);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setupProperties(ManageUnit unit,Element el){
		List<Element> ls = el.selectNodes("properties/p");
		for(Element p : ls){
			unit.setProperty(p.attributeValue("name"), p.getTextTrim());
		}
		if(el.attributeValue("mCode") == null){
			el.addAttribute("mCode", PyConverter.getFirstLetter(unit.getName()));
		}
	}
	
	private void registerDictionary(Tenant tenant) throws ControllerException{
		String tenantId = tenant.getId();
		Dictionary dictionary = new ManageUnitDictionary(tenantId);
		DictionaryController.instance().add(dictionary);

//		dictionary = PersonnelDictionaryBuilder.instance().createDictionary(tenantId);
//		if(dictionary != null){
//			DictionaryController.instance().add(dictionary);
//		}
	}
}
