package ctd.security.loader;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import ctd.controller.exception.ControllerException;
import ctd.controller.support.AbstractConfigurableLoader;
import ctd.security.CategoryNode;
import ctd.security.Condition;
import ctd.security.ConditionFactory;
import ctd.security.Permission;
import ctd.security.ResourceNode;
import ctd.util.converter.ConversionUtils;

public class CategoryNodeLocalLoader extends AbstractConfigurableLoader<CategoryNode> {
	
	public CategoryNodeLocalLoader(){
		postfix = ".res";
	}
	
	@Override
	public CategoryNode createInstanceFormDoc(String id, Document doc,long lastModi) throws ControllerException {
		Element root = doc.getRootElement();
		if(root == null){
			throw new ControllerException(ControllerException.PARSE_ERROR,"root element missing.");
		}
		try{
			CategoryNode node = ConversionUtils.convert(root, CategoryNode.class);
			node.setId(id);
			node.setLastModify(lastModi);
			setupProperties(node,root);
			parseChilds(node,root);
			return node;
			
		}
		catch (Exception e){
			e.printStackTrace();
			throw new ControllerException(e,ControllerException.PARSE_ERROR,"Application[" + id + "] init unknow error:"+ e.getMessage());
		}
	}
	

	
	@SuppressWarnings("unchecked")
	private void parseChilds(ResourceNode parent,Element el){
		List<Element> ls = el.elements();
		for(Element c : ls){
			if(c.getName().equals("permissions")){
				parsePremissions(parent,c);
			}
			else{
				ResourceNode child = ConversionUtils.convert(c, ResourceNode.class);
				parent.appendChild(child);
				parseChilds(child,c);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void parsePremissions(ResourceNode node,Element el){
		List<Element> ls = el.elements("p");
		for(Element e : ls){
			Permission p = ConversionUtils.convert(e, Permission.class);
			parseConditions(p,e);
			node.addPermission(p);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void parseConditions(Permission p,Element el){
		List<Element> ls = el.selectNodes("conditions/*");
		for(Element e : ls){
			Condition c = ConditionFactory.createCondition(e);
			p.addCondition(c);
		}
	}
}
