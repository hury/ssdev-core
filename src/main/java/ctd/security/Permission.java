package ctd.security;

import java.io.Serializable;
import java.util.HashMap;

import ctd.security.support.NegativePermission;
import ctd.security.support.PositivePermission;

public class Permission implements Serializable{
	private static final long serialVersionUID = -2686922205189954596L;
	
	public static final String PREFIX_USER = "u@";
	public static final String PREFIX_ROLE = "r@";
	public static final String PREFIX_TENANT = "t@";
	public static final String OTHERS_PRINCIPAL = "$Others";
	
	public static final Permission NegativePermission = new NegativePermission();
	public static final Permission PositivePermission = new PositivePermission();
	
	private String principal;
	private Mode mode;
	private HashMap<String,Condition> conditions = new HashMap<String,Condition>();
	
	public Permission(){
		
	}
	
	public Permission(String modeStr){
		this(Integer.parseInt(modeStr, 2));
	}
	
	public Permission(String principal,String modeStr){
		this(Integer.parseInt(modeStr, 2));
		this.principal = principal;
	}
	
	public Permission(String principal,int mode){
		this(mode);
		this.principal = principal;
	}
	
	public Permission(int modeVal){
		setMode(Mode.parseFromInt(modeVal));
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public void setMode(int modeVal){
		setMode(Mode.parseFromInt(modeVal));
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	public Condition getCondition(String action){
		return conditions.get(action);
	}
	
	public void addCondition(Condition c){
		conditions.put(c.getAction(), c);
	} 
}
