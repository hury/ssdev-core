package ctd.config;

import java.util.Date;

import ctd.controller.support.AbstractConfigurable;

public class Config extends AbstractConfigurable {
	private static final long serialVersionUID = 9074851461670416365L;
	
	
	public String getString(String id){
		return getProperty(id, String.class);
	}
	
	public int getInt(String id){
		return getProperty(id, int.class);
	}
	
	public double getDouble(String id){
		return getProperty(id, double.class);
	}
	
	public Date getDate(String id){
		return getProperty(id, Date.class);
	}
	
	public byte getByte(String id){
		return getProperty(id, byte.class);
	}
	
	public boolean getBoolean(String id){
		return getProperty(id, boolean.class);
	}
	
	public char getChar(String id){
		return getProperty(id, char.class);
	}


}
