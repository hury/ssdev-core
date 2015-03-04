package ctd.dictionary.loader;

import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;


import ctd.controller.exception.ControllerException;
import ctd.controller.support.TenantSupportConfiguableLoader;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryItem;
import ctd.dictionary.support.LoadingXMLDictionary;
import ctd.dictionary.support.XMLDictionary;
import ctd.util.AppContextHolder;
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
		try {
			String clz = root.attributeValue("class");
			XMLDictionary dic = null;
			if(StringUtils.isEmpty(clz)){
				dic = new XMLDictionary();
				dic.setDefineDoc(doc);
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
			else{
				dic = (LoadingXMLDictionary) Class.forName(clz).newInstance();
				String loaderBeanId = root.attributeValue("loader");
				if(StringUtils.isEmpty(loaderBeanId)){
					throw new IllegalStateException("loadingDictionary[" + id + "] loader not setup.");
				}
				DictionaryItemLoader loader = AppContextHolder.getBean(loaderBeanId,DictionaryItemLoader.class);
				((LoadingXMLDictionary)dic).setLoader(loader);
			}
			
			dic.setName(root.attributeValue("name",id));
			setupProperties(dic,root);
			dic.setId(id);
			dic.setLastModify(lastModi);
			return dic;
		} 
		catch (Exception e){
			throw new ControllerException(e,ControllerException.PARSE_ERROR,"dictionary[" + id + "] init unknow error:"+ e.getMessage());
		}
		
	}
	
}
