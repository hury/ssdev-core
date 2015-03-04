package ctd.app.loader;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import ctd.app.Application;
import ctd.app.ApplicationNode;
import ctd.app.support.Action;
import ctd.app.support.Category;
import ctd.app.support.Module;
import ctd.controller.exception.ControllerException;
import ctd.controller.support.AbstractConfigurableLoader;
import ctd.schema.constants.DataTypes;
import ctd.util.converter.ConversionUtils;

public class ApplicationLocalLoader extends AbstractConfigurableLoader<Application> {
	
	public ApplicationLocalLoader(){
		postfix = ".app";
	}
	
	@Override
	public Application createInstanceFormDoc(String id, Document doc,long lastModi) throws ControllerException {
		Element root = doc.getRootElement();
		if(root == null){
			throw new ControllerException(ControllerException.PARSE_ERROR,"root element missing.");
		}
		try{
			Application app = ConversionUtils.convert(root, Application.class);
			app.setId(id);
			app.setLastModify(lastModi);
			
			setupProperties(app,root);
			parseChilds(app,root);
			return app;
		}
		catch (Exception e){
			throw new ControllerException(e,ControllerException.PARSE_ERROR,"Application[" + id + "] init unknow error:"+ e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void parseChilds(ApplicationNode appNode,Element el){
		List<Element> ls = (List<Element>)el.elements("catagory");
		for(Element catEl : ls){
			Category category = ConversionUtils.convert(catEl, Category.class);
			appNode.appendChild(category);
			parseChilds(category,catEl);
		}
		
		ls = (List<Element>)el.elements("module");
		for(Element modEl : ls){
			Module mod = ConversionUtils.convert(modEl,Module.class);
			appNode.appendChild(mod);
			parseChilds(mod,modEl);
		}
		
		if(el.getName().equals("module")){
			setupProperties((Module)appNode,el);
			ls = (List<Element>)el.elements("action");
			for(Element actEl : ls){
				Action action = ConversionUtils.convert(actEl,Action.class);
				appNode.appendChild(action);
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void setupProperties(Module o,Element el){
		List<Element> ls = el.selectNodes("properties/p");
		for(Element p : ls){
			String type = p.attributeValue("type","string");
			Object v = DataTypes.toTypeValue(type, p.getTextTrim());
			o.setProperty(p.attributeValue("name"), v);
		}
	}


}
