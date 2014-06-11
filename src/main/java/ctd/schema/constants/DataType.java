package ctd.schema.constants;

import java.math.BigDecimal;
import java.util.Date;

import ctd.util.converter.ConversionUtils;

public enum DataType {
	STRING("string",String.class),
	INT("int",Integer.class),
	LONG("long",Long.class),
	DOUBLE("double",Double.class),
	BIGDECIMAL("bigDecimal",BigDecimal.class),
	DATE("date",Date.class),
	DATETIME("datetime",Date.class),
	CHAR("char",Character.class),
	BOOLEAN("boolean", Boolean.class);
	
	
	private String name;
	private Class<?> type;
	
	private DataType(String name,Class<?> type){
		this.name = name;
		this.type = type;
	}
	
	public String getName(){
		return name;
	}
	
	public Class<?> getTypeClass(){
		return type;
	}
	
	public Object toTypeValue(Object val){
		return ConversionUtils.convert(val,type);
	}
	
	public boolean isNumberType(){
		return Number.class.isAssignableFrom(type);
	}
	
	public String toString(){
		return name;
	}
	
	public static boolean isSupportType(String type){
		try{
			DataType.valueOf(type.toUpperCase());
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public static Class<?> getTypeClass(String type){
		try{
			return DataType.valueOf(type.toUpperCase()).getTypeClass();
		}
		catch(Exception e){
			return null;
		}
	}
	
	public static boolean isNumberType(String type){
		try{
			return DataType.valueOf(type.toUpperCase()).isNumberType();
		}
		catch(Exception e){
			return false;
		}
	}
}

