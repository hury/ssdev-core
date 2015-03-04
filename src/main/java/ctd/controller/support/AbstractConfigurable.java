package ctd.controller.support;

import java.util.HashMap;
import java.util.Map;

import ctd.controller.Configurable;
import ctd.controller.exception.ControllerException;
import ctd.util.converter.ConversionUtils;

public abstract class AbstractConfigurable implements Configurable {
	private static final long serialVersionUID = 4078730957151852441L;
	protected Long lastModi;
	protected String id;
	protected String name;
	protected Map<String,Object> properties;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setProperty(String nm,Object v){
		if(properties == null){
			properties = new HashMap<String,Object>();
		}
		properties.put(nm, v);
	}
	
	@Override
	public Object getProperty(String nm){
		if(properties == null){
			return null;
		}
		return properties.get(nm);
	}
	
	@Override
	public <T> T getProperty(String nm,Class<T> targetType){
		return ConversionUtils.convert(getProperty(nm), targetType);
	}
	

	@Override
	public Map<String,Object> getProperties(){
		if(properties == null || properties.size() == 0){
			return null;
		}
		return properties;
	}

	@Override
	public Long getlastModify() {
		return lastModi;
	}

	@Override
	public void setLastModify(Long lastModi) {
		this.lastModi = lastModi;
	}
	
	public boolean isInited(){
		return true;
	}
	
	public void init() throws ControllerException{
		
	};

}
