package ctd.security.support.condition;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import ctd.security.Condition;
import ctd.util.StringValueParser;
import ctd.util.context.ContextUtils;
import ctd.util.exception.CodedBaseException;

public class OverrideCondition extends Condition {
	private static final long serialVersionUID = 964159469153507576L;
	private HashMap<String,Object> overrides = new HashMap<String,Object>();
	
	public  static final String CONTEXT_OVERRIDE_KEY = "$overrideKeyName";
	
	@Override
	public Object run() throws CodedBaseException {
		String target = ContextUtils.get(CONTEXT_OVERRIDE_KEY,String.class);
		if(!StringUtils.isEmpty(target)){
			return getOverrideValue(target);
		}
		else{
			throw new IllegalArgumentException("OverrideKeyName is not inited in the threadContext.");
		}
	}

	@Override
	public Object getDefine() {
		return overrides;
	}
	
	public void add(String target,Object val){
		overrides.put(target, val);
	}
	
	public boolean isOverrided(String target){
		return overrides.containsKey(target);
	}
	
	public Object getOverrideValue(String target){
		Object v =  overrides.get(target);
		if(v instanceof String){
			return StringValueParser.parse((String)v,Object.class);
		}
		return v;
	}
	
}
