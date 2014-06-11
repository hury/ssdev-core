package ctd.controller.notifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifierMessage implements Serializable {
	private static final long serialVersionUID = 6216941015036027135L;
	private final byte command;
	private final String instanceId;
	private Map<String,Object> updatedProperties;
	private List<Object> updatedItems;
	private long lastModify;
	
	public NotifierMessage(byte command){
		this(command,null);
	}
	
	public NotifierMessage(byte command,String instanceId){
		this.command = command;
		this.instanceId = instanceId;
	}

	public byte getCommand() {
		return command;
	}

	public String getInstanceId() {
		return instanceId;
	}
	
	public void addUpdatedProperty(String nm,Object value){
		if(updatedProperties == null){
			updatedProperties = new HashMap<>();
		}
		updatedProperties.put(nm, value);
	}
	
	public void addUpdatedItems(Object item){
		if(updatedItems == null){
			updatedItems = new ArrayList<>();
		}
		updatedItems.add(item);
	}
	
	public Map<String,Object> getUpdatedProperties(){
		return updatedProperties;
	}
	
	public List<Object> getUpdatedItems(){
		return updatedItems;
	}

	public long getLastModify() {
		return lastModify;
	}

	public void setLastModify(long lastModify) {
		this.lastModify = lastModify;
	}
	
}
