package ctd.security;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;


import ctd.security.support.condition.ConditionActionTypes;
import ctd.security.support.condition.FilterCondition;
import ctd.security.support.condition.OverrideCondition;

public class ConditionFactory {
	
	@SuppressWarnings("unchecked")
	public static Condition createCondition(Element el){
		String tagName = el.getName();
		Condition c = null;
		if(tagName == "filter"){
			c = new FilterCondition();
			c.setAction(el.attributeValue("action",ConditionActionTypes.QUERY));
			String expDefine = el.attributeValue("exp");
			if(expDefine == null){
				expDefine = el.getTextTrim();
			}
			if(StringUtils.isEmpty(expDefine)){
				throw new IllegalArgumentException("No expression text found in element:" + el.asXML());
			}
			((FilterCondition)c).setExpr(expDefine);
		}
		else if(tagName == "override"){
			OverrideCondition o = new OverrideCondition();
			o.setAction(el.attributeValue("action",ConditionActionTypes.DEFINE_READ));
			List<Element> ls = (List<Element>)el.elements();
			if(ls.size() > 0){
				for(Element e : ls){
					o.add(e.attributeValue("target"),e.attributeValue("value"));
				}
			}
			else{
				o.add(el.attributeValue("target"),el.attributeValue("value"));
			}
			c = o;
		}
		return c;
	}
}
