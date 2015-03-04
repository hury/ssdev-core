package ctd.security;

import java.util.HashMap;
import java.util.Map;

import ctd.controller.Configurable;
import ctd.controller.exception.ControllerException;
import ctd.util.converter.ConversionUtils;

public class CategoryNode extends ResourceNode implements Configurable {
	private static final long serialVersionUID = -3750355314383712048L;
	private Long lastModi;
	private Map<String,Object> properties = new HashMap<String,Object>();
	
	public CategoryNode(){
		super();
	}
	
	public CategoryNode(String id) {
		super(id);
	}


	@Override
	public void setProperty(String nm,Object v){
		properties.put(nm, v);
	}
	
	@Override
	public Object getProperty(String nm){
		return properties.get(nm);
	}
	
	@Override
	public <T> T getProperty(String nm,Class<T> targetType){
		return ConversionUtils.convert(getProperty(nm), targetType);
	}
	
	@Override
	public Map<String,Object> getProperties(){
		if(properties.size() == 0){
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

	@Override
	public boolean isInited() {
		return true;
	}

	@Override
	public void init() throws ControllerException {

	}

	@Override
	public String getName() {
		return id;
	}

	@Override
	public void setName(String name) {
		
	}

}
