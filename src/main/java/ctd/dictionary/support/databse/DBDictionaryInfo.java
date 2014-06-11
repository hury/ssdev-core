package ctd.dictionary.support.databse;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ctd.controller.exception.ControllerException;
import ctd.schema.Schema;
import ctd.schema.SchemaController;
import ctd.schema.SchemaItem;
import ctd.schema.constants.DisplayModes;

public class DBDictionaryInfo{
	private String schemaId;
	private String keyField;
	private String textField;
	private String mCodeField;
	private String parentKeyField;
	private boolean loadAllFields;
	private List<String> propertyFields;
	private String orderBy;
	
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
	
	public String getMCode(){
		return this.mCodeField;
	}
	
	public void setLoadAllFields(boolean allFields){
		this.loadAllFields = allFields;
	}
	
	public boolean  isLoadAllFields(){
		return this.loadAllFields;
	}

	public List<String> getPropertyFields() {
		return propertyFields;
	}

	public void setPropertyFields(List<String> propertyFields) {
		this.propertyFields = propertyFields;
	}
	
	public List<String> getFields(){
		List<String> fields = new ArrayList<String>();
		if(loadAllFields){
			Schema sc;
			try {
				sc = SchemaController.instance().get(schemaId);
				List<SchemaItem> items = sc.getItems();
				for(SchemaItem it : items){
					if(it.getDisplayMode() == DisplayModes.NO_LIST_DATA){
						continue;
					}
					fields.add(it.getId());
				}
			}
			catch (ControllerException e) {
				throw new IllegalStateException("get schema[" + schemaId + "] failed.");
			}
			
		}
		else{
			fields.add(keyField);
			fields.add(textField);
			if(!StringUtils.isEmpty(mCodeField)){
				fields.add(mCodeField);
			}
			if(propertyFields != null && !propertyFields.isEmpty()){
				fields.addAll(propertyFields);
			}
		}
		return fields;
	}

	public String getParentKeyField() {
		return parentKeyField;
	}

	public void setParentKeyField(String parentKeyField) {
		this.parentKeyField = parentKeyField;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

}
