package ctd.dictionary;

import java.util.List;

import ctd.controller.Configurable;

public interface Dictionary extends Configurable{
	
	public void addItem(DictionaryItem item);
	
	public void updateItem(DictionaryItem item);
	
	public void removeItem(String key);
	
	public DictionaryItem getItem(String key);
	
	public boolean keyExist(String key);
	
	public String getText(String key);
	
	public String getText(Object key);
	
	public List<DictionaryItem> getSlice(String parentKey,int sliceType,String query);
	
	public boolean isReloadable();
	
	public void destory();

}
