package ctd.dictionary.support.databse;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ctd.controller.exception.ControllerException;
import ctd.dictionary.DictionaryItem;
import ctd.util.PyConverter;
import ctd.util.converter.ConversionUtils;

public abstract class AbstractDBDictionaryItemLoader implements DBDictionaryItemLoader {
	protected DBDictionaryInfo dictionaryInfo;

	@Override
	public abstract List<DictionaryItem> loadItems(List<?> cnds) throws ControllerException;

	
	
	
	public void configDictionaryItem(DictionaryItem di,String nm,Object v){
		
		
		if(nm.equals(dictionaryInfo.getKeyField())){
			di.setKey(ConversionUtils.convert(v, String.class));
		}
		else if(nm.equals(dictionaryInfo.getTextField())){
			String text = ConversionUtils.convert(v, String.class);
			di.setText(text);
			if(StringUtils.isEmpty(dictionaryInfo.getMCode())){
				di.setMCode(PyConverter.getFirstLetter(text));
			}
		}
		else if (nm.equals(dictionaryInfo.getParentKeyField())){
			di.setParent(ConversionUtils.convert(v,String.class));
		}
		else if(nm.equals(dictionaryInfo.getMCode())){
			di.setMCode(ConversionUtils.convert(v,String.class));
		}
		else{
			di.setProperty(nm, v);
		}
	}
	
	@Override
	public DBDictionaryInfo getDictionaryInfo() {
		return dictionaryInfo;
	}
	
	@Override
	public void setDictionaryInfo(DBDictionaryInfo dictionaryInfo) {
		this.dictionaryInfo = dictionaryInfo;
	}
}
