package ctd.schema.loader;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import ctd.controller.exception.ControllerException;
import ctd.controller.support.AbstractConfigurableLoader;
import ctd.schema.DictionaryIndicator;
import ctd.schema.EvaluatorFactory;
import ctd.schema.Schema;
import ctd.schema.SchemaItem;
import ctd.schema.constants.DataTypes;
import ctd.util.converter.ConversionUtils;


public class SchemaLocalLoader extends AbstractConfigurableLoader<Schema> {
	
	public SchemaLocalLoader(){
		postfix = ".sc";
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Schema createInstanceFormDoc(String id,Document doc,long lastModi) throws ControllerException{
		
		Element root = doc.getRootElement();
		if(root == null){
			throw new ControllerException(ControllerException.PARSE_ERROR,"root element missing.");
		}
		try{
			Schema sc = ConversionUtils.convert(root, Schema.class);
			sc.setId(id);
			sc.setLastModify(lastModi);
			
			List<Element> els = root.selectNodes("properies/p");
			for(Element el : els){
				String type = el.attributeValue("type","string");
				Object v = DataTypes.toTypeValue(type, el.getTextTrim());
				sc.setProperty(el.attributeValue("name"), v);
			}
			
			els = root.selectNodes("item");

			for(Element el : els){
				SchemaItem si = ConversionUtils.convert(el, SchemaItem.class);
				si.setSchemaId(id);
				
				Element dicEl = el.element("dic");
				if(dicEl != null){
					DictionaryIndicator dic = ConversionUtils.convert(dicEl, DictionaryIndicator.class);
					si.setDic(dic);
				}
				Element setEl = el.element("set");
				if(setEl != null){
					si.setEvaluator(EvaluatorFactory.newInstance(setEl));
				}
				if(si.isPkey() != null){
					sc.setKey(si.getId());
					sc.setKeyGenerator(si.getKeyGenerator());
				}
				sc.addItem(si);
			}
			
			return sc;
		}
		catch(Exception e){
			throw new ControllerException(e,ControllerException.PARSE_ERROR,"schema[" + id + "] init unknow error:"+ e.getMessage());
		}	
	}
}
