package ctd.dictionary.loader;

import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;


import ctd.controller.Configurable;
import ctd.controller.exception.ControllerException;
import ctd.controller.support.TenantSupportConfiguableLoader;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryItem;
import ctd.dictionary.support.DatabaseDictionary;
import ctd.dictionary.support.XMLDictionary;
import ctd.dictionary.support.databse.DBDictionaryInfo;
import ctd.dictionary.support.databse.DBDictionaryItemLoader;
import ctd.dictionary.support.databse.DBDictionaryItemLoaderFactory;
import ctd.util.PyConverter;
import ctd.util.converter.ConversionUtils;


public class DictionaryLocalLoader extends TenantSupportConfiguableLoader<Dictionary> {
	
	public DictionaryLocalLoader(){
		postfix = ".dic";
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Dictionary createInstanceFormDoc(String id,Document doc,long lastModi) throws ControllerException{
		
		Element root = doc.getRootElement();
		if(root == null){
			throw new ControllerException(ControllerException.PARSE_ERROR,"root element missing.");
		}
		String className = root.attributeValue("class","XMLDictionary");
		
		try {

			Dictionary dic = null;
			
			if(className.equals("XMLDictionary")){
				dic = new XMLDictionary();
				((XMLDictionary)dic).setDefineDoc(doc);
				List<Element> els = root.selectNodes("//item");
				for(Element el : els){
					DictionaryItem item = ConversionUtils.convert(el, DictionaryItem.class);
					if(StringUtils.isEmpty(item.getMCode())){
						String mCode = PyConverter.getFirstLetter(item.getText()); 
						item.setMCode(mCode);
						el.addAttribute("mCode",mCode);
					}
					Element parentEl = el.getParent();
					if(!parentEl.isRootElement()){
						item.setParent(parentEl.attributeValue("id"));
					}
					if(!el.hasContent()){
						item.setLeaf(true);
					}
					dic.addItem(item);
				}
			}
			else if(className.equals("DatabaseDictionary")){
				dic = new DatabaseDictionary();
				DBDictionaryInfo info = ConversionUtils.convert(root, DBDictionaryInfo.class);
				
				String loaderClassName = root.attributeValue("loaderClass", "ctd.dictionary.support.databse.HibernateSupportDictionaryItemLoader");
				DBDictionaryItemLoader loader = DBDictionaryItemLoaderFactory.createLoader(loaderClassName, info);
				((DatabaseDictionary)dic).setLoader(loader);
			}
			else{
				throw new IllegalStateException("dictionary class[" + className + "] is not support.");
			}

			dic.setId(id);
			dic.setName(root.attributeValue("name",id));
			dic.setLastModify(lastModi);
			setupProperties(dic,root);
			return dic;
		} 
		catch (Exception e){
			throw new ControllerException(e,ControllerException.PARSE_ERROR,"dictionary[" + id + "] class[" + className + "] init unknow error:"+ e.getMessage());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void setupProperties(Configurable o,Element el){
		List<Element> ls = el.selectNodes("properties/p");
		for(Element p : ls){
			o.setProperty(p.attributeValue("name"), p.getTextTrim());
		}
	}
}
