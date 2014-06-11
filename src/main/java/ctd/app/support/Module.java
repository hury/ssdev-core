package ctd.app.support;


import java.util.HashMap;
import java.util.List;

import ctd.app.ApplicationNode;
import ctd.util.converter.ConversionUtils;


public class Module extends ApplicationNode {
	private static final long serialVersionUID = -5866496056614584396L;
	private String script;
	private String implement;
	private Boolean runAsWindow;
	private HashMap<String,Object> properties;
	
	public List<Action> getActions(){
		if(deep >= getRequestDeep()){
			return null;
		}
		return getAuthorizedItems();
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
	
	public void setProperty(String nm,Object v){
		if(properties == null){
			properties = new HashMap<String,Object>();
		}
		properties.put(nm, v);
	}
	
	public Object getProperty(String nm){
		if(properties == null){
			return null;
		}
		return properties.get(nm);
	}
	
	public <T> T getProperty(String nm,Class<T> targetType){
		return ConversionUtils.convert(getProperty(nm), targetType);
	}
	
	public HashMap<String,Object> getProperties(){
		if(properties == null ||properties.size() == 0){
			return null;
		}
		return properties;
	}

	public String getImplement() {
		return implement;
	}

	public void setImplement(String implement) {
		this.implement = implement;
	}

	public Boolean isRunAsWindow() {
		return runAsWindow;
	}

	public void setRunAsWindow(boolean runAsWindow) {
		this.runAsWindow = runAsWindow;
	}
}
