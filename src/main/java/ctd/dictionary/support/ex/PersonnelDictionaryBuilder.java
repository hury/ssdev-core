package ctd.dictionary.support.ex;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ctd.controller.exception.ControllerException;
import ctd.dictionary.DictionaryItem;
import ctd.dictionary.support.databse.DBDictionaryInfo;
import ctd.dictionary.support.databse.DBDictionaryItemLoader;
import ctd.dictionary.support.databse.HibernateSupportDBDictionaryItemLoader;
import ctd.util.JSONUtils;
import ctd.util.context.Context;
import ctd.util.context.ContextUtils;

public class PersonnelDictionaryBuilder {
	
	private static final List<?> cnds =JSONUtils.parse("['like',['$','manageUnit'],['$','%$tenantId']]",List.class);
	private static PersonnelDictionaryBuilder instance;
	private String schemaId;
	private String keyField;
	private String textField;
	private String mCodeField;


	private DBDictionaryItemLoader itemLoader;
	
	public PersonnelDictionaryBuilder(){
		instance = this;
		setItemLoader(new HibernateSupportDBDictionaryItemLoader());
	}
	
	public static PersonnelDictionaryBuilder instance(){
		if(instance == null){
			instance = new PersonnelDictionaryBuilder();
		}
		return instance;
	}
	
	public PersonnelDictionary createDictionary(String tenantId) throws ControllerException{
		if(StringUtils.isEmpty(schemaId)){
			return null;
		}
		PersonnelDictionary dictionary = new PersonnelDictionary(tenantId);
		
		DBDictionaryInfo info = new DBDictionaryInfo();
		info.setSchemaId(schemaId);
		info.setKeyField(keyField);
		info.setTextField(textField);
		info.setMCodeField(mCodeField);
		info.setLoadAllFields(true);
		itemLoader.setDictionaryInfo(info);
		
		ContextUtils.put(Context.TENANT_ID,tenantId);
		List<DictionaryItem> items = itemLoader.loadItems(cnds);
		for(DictionaryItem item : items){
			dictionary.addItem(item);
		}
		dictionary.setLastModify(System.currentTimeMillis());
		return dictionary;
	}
	

	public String getKeyField() {
		return keyField;
	}

	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}
	
	public void setMCodeField(String mCodeField) {
		this.mCodeField = mCodeField;
	}

	public DBDictionaryItemLoader getItemLoader() {
		return itemLoader;
	}

	public void setItemLoader(DBDictionaryItemLoader itemLoader) {
		this.itemLoader = itemLoader;
	}
	
	
}
