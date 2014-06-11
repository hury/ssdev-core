package ctd.dictionary.support;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import ctd.controller.exception.ControllerException;
import ctd.dictionary.DictionaryItem;
import ctd.dictionary.support.databse.DBDictionaryItemLoader;

public class DatabaseDictionary extends XMLDictionary {
	private static final long serialVersionUID = 796381667251526463L;
	private DBDictionaryItemLoader loader;
	private List<?> initCnds;

	public DatabaseDictionary(){
		inited = false;
	}

	public DBDictionaryItemLoader getLoader() {
		return loader;
	}

	public void setLoader(DBDictionaryItemLoader loader) {
		this.loader = loader;
	}
	
	@Override
	public void init() throws ControllerException{
		if(loader == null){
			throw new ControllerException("DatabaseDictionary[" + id + "] loader not setup.");
		}
		Document doc = DocumentHelper.createDocument();
		doc.addElement("dic");
		setDefineDoc(doc);
		
		List<DictionaryItem> ls = loader.loadItems(initCnds);
		for(DictionaryItem item : ls){
			addItem(item);
		}
		for(DictionaryItem item : ls){
			createEl(item);
		}
		setLastModify(System.currentTimeMillis());
		inited = true;
	}
	
	private Element createEl(DictionaryItem item){
		String parentKey = item.getParent();
		Element parentEl = null;
		if(StringUtils.isEmpty(parentKey)){
			parentEl = defineDoc.getRootElement();
		}
		else{
			parentEl = (Element)defineDoc.getRootElement().selectSingleNode("//item[@key='" + parentKey + "']");
		}
		if(parentEl == null){
			DictionaryItem parentItem = getItem(parentKey);
			if(parentItem == null){
				throw new IllegalStateException("dictionary[" + id + "] item[" + item.getKey() + "] parent[" + parentKey +"] not found.");
			}
			parentEl = createEl(parentItem);
		}
		Element el = parentEl.addElement("item");
		el.addAttribute("key", item.getKey());
		el.addAttribute("text", item.getText());
		el.addAttribute("mCode", item.getMCode());
		Map<String,Object> properties = item.getProperties();
		if(properties != null){
			Set<String> keys = properties.keySet();
			for(String nm : keys){
				Element p = DocumentHelper.createElement("p");
				p.addAttribute("name", nm);
				p.setText(item.getProperty(nm, String.class));
			}
		}
		return el;
	}

	public List<?> getInitCnds() {
		return initCnds;
	}

	public void setInitCnds(List<?> initCnds) {
		this.initCnds = initCnds;
	};

}
