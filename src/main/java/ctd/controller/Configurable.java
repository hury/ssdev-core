package ctd.controller;

import java.io.Serializable;
import java.util.Map;

import ctd.controller.exception.ControllerException;

public interface Configurable extends Serializable{
	public String getId();
	public void setId(String id);
	public String getName();
	public void setName(String name);
	public void setProperty(String nm,Object v);
	public Object getProperty(String nm);
	public Map<String, Object> getProperties();
	public Long getlastModify();
	public void setLastModify(Long lastModi);
	public <T> T  getProperty(String nm, Class<T> targetType);
	public boolean isInited();
	public void init() throws ControllerException;
}
