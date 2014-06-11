package ctd.security;

import java.io.Serializable;

public class Mode implements Serializable{
	private static final long serialVersionUID = 8406591497388596746L;
	
	public static final int ACCESSIBLE_FLAG = 1;
	public static final int CREATEABLE_FLAG = 2;
	public static final int UPDATEABLE_FLAG = 4;
	public static final int REMOVEABLE_FLAG = 8;
	
	public static final Mode FullAccessMode = new Mode(15);
	public static final Mode NoneAccessMode = new Mode(0);
	
	private int value;
	private boolean accessible;
	private boolean creatable;
	private boolean updatable;
	private boolean removable;
	
	public static Mode parseFromInt(int v){
		if(v == 0){
			return Mode.NoneAccessMode;
		}
		else if((v & 15) == 15){
			return Mode.FullAccessMode;
		}
		else{
			Mode mode = new Mode();
			mode.setValue(v);
			return mode;
		}
	}
	
	public Mode(){
		
	}
	
	public Mode(int v){
		setValue(v);
	}
	
	public Mode(String v){
		setValue(Integer.valueOf(v, 2));
	}
	
	public Integer getValue(){
		return value;
	}
	
	public void setValue(Integer v){
		value = v;
		accessible = (value & ACCESSIBLE_FLAG) == ACCESSIBLE_FLAG;
		creatable  = (value & CREATEABLE_FLAG) == CREATEABLE_FLAG;
		updatable  = (value & UPDATEABLE_FLAG) == UPDATEABLE_FLAG;
		removable  = (value & REMOVEABLE_FLAG) == REMOVEABLE_FLAG;
	}
	
	public boolean isAccessible(){
		return accessible;
	}
	
	public boolean isCreatable(){
		return creatable;
	}
	
	public boolean isUpdatable(){
		return updatable;
	}
	
	public boolean isRemovable(){
		return removable;
	}
	
	@Override 
	public boolean equals(Object o){
		if(!o.getClass().equals(this.getClass())){
			return false;
		}
		return ((Mode)o).getValue().equals(value);
	}
	
	
}
