package ctd.dictionary.support.databse;

import java.util.List;
import java.util.Map;

import ctd.controller.exception.ControllerException;
import ctd.dictionary.DictionaryItem;

public interface DBDictionaryItemLoader {
	public List<DictionaryItem> loadItems(List<?> cnds) throws ControllerException;
	public DictionaryItem createDictionaryItem(Map<String,Object> map);
	void setDictionaryInfo(DBDictionaryInfo dictionaryInfo);
	DBDictionaryInfo getDictionaryInfo();
}
