package ctd.schema;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import ctd.account.UserRoleToken;
import ctd.controller.Controllers;
import ctd.schema.constants.DataTypes;
import ctd.schema.constants.DisplayModes;
import ctd.security.Mode;
import ctd.security.Repository;
import ctd.security.ResourceNode;
import ctd.util.StringValueParser;
import ctd.util.converter.ConversionUtils;
import ctd.util.exception.CodedBaseException;

public class SchemaItem implements Serializable{
	private static final long serialVersionUID = 4557229734515290036L;
	
	private String schemaId;
	private String id;
	private String alias;
	private Boolean updatable;
	private Boolean isVirtual;
	private Boolean isRequired;
	private Boolean isPkey;
	private String keyGenerator;
	private String type;
	private Integer length;
	private Integer precision;
	private Evaluator evaluator;
	private int displayMode = DisplayModes.ALL;
	private DictionaryIndicator dic;
	private Object defaultValue;
	private Object maxValue;
	private Object minValue;
	
	public SchemaItem(){
		
	}
	
	public void setSchemaId(String schemaId){
		this.schemaId = schemaId;
	}
	
	public SchemaItem(String id){
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public Boolean isCodedValue(){
		return !(dic == null);
	}
	
	public Boolean isEvalValue(){
		return evaluator != null;
	}

	public Boolean isVirtual() {
		return isVirtual;
	}

	public void setVirtual(Boolean isVirtual) {
		this.isVirtual = isVirtual;
	}

	public Boolean isRequired() {
		return isRequired;
	}

	public void setRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getType() {
		if(StringUtils.isEmpty(type)){
			return DataTypes.STRING;
		}
		return type;
	}
	
	private Class<?> getTypeClass(){
		return DataTypes.getTypeClass(type);
	}

	public void setType(String type) {
		if(!DataTypes.isSupportType(type)){
			throw new IllegalArgumentException("type[" + type + "] is unsupported");
		}
		this.type = StringUtils.uncapitalize(type);
	}

	public int getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(Integer displayMode) {
		this.displayMode = displayMode;
	}

	public Boolean isUpdatable() {
		if(updatable != null && updatable == false){
			return false;
		}
		if(isPkey != null && isPkey == true){
			return false;
		}
		int mode = getMode();
		return (mode & Mode.UPDATEABLE_FLAG) == Mode.UPDATEABLE_FLAG;
	}

	public void setUpdatable(Boolean updateable) {
		this.updatable = updateable;
	}

	public DictionaryIndicator getDic() {
		return dic;
	}

	public void setDic(DictionaryIndicator dic) {
		this.dic = dic;
	}

	public Boolean isPkey() {
		return isPkey;
	}

	public void setPkey(Boolean isPkey) {
		this.isPkey = isPkey;
	}

	public String getKeyGenerator() {
		return keyGenerator;
	}

	public void setKeyGenerator(String keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision;
	}
	
	public Object getDefaultValue() {
		if(defaultValue == null){
			return defaultValue;
		}
		if(isCodedValue()){
			HashMap<String,String> obj = new HashMap<String,String>();
			String key = ConversionUtils.convert(parseConfigValue(defaultValue),String.class);
			String text =  toDisplayValue(defaultValue);
			obj.put("key", key);
			obj.put("text", text);
			return obj;
		}
		return parseConfigValue(defaultValue);
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue =defaultValue;
	}
	
	public Object getMaxValue() {
		return parseConfigValue(maxValue);
	}

	public void setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
	}

	public Object getMinValue() {
		return parseConfigValue(minValue);
	}
	
	public void setMinValue(Object minValue) {
		this.minValue = minValue;
	}
	
	private Object parseConfigValue(Object v){
		if(v instanceof String){
			return StringValueParser.parse((String)v, getTypeClass());
		}
		else{
			return ConversionUtils.convert(v, getTypeClass());
		}
	}
	
	public String toDisplayValue(Object v){
		String key = ConversionUtils.convert(v, String.class);
		if(isCodedValue()){
			try {
				return Controllers.getDictionary(dic.getId()).getText(key);
			} 
			catch (Exception e) {
				//throw new IllegalStateException("dictionary[" + dic.getId() + "] load failed.");
			}
		}
		return key;
	}
	
	public Object toPersistValue(Object source){
		return DataTypes.toTypeValue(getType(),source);
	}
	
	public Object eval() throws CodedBaseException{
		if(evaluator == null){
			return null;
		}
		return toPersistValue(evaluator.eval());
	}
	
	public int getMode(){
		ResourceNode node = Repository.getNode(schemaId,id);
		String principal = UserRoleToken.getCurrent().getRoleId();
		return node.lookupPermission(principal).getMode().getValue();
	}

	public void setEvaluator(Evaluator evaluator) {
		this.evaluator = evaluator;
	}

	
}
