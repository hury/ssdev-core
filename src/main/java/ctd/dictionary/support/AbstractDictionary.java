package ctd.dictionary.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ctd.controller.exception.ControllerException;
import ctd.controller.support.AbstractConfigurable;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryItem;

public abstract class AbstractDictionary extends AbstractConfigurable implements Dictionary{
	private static final long serialVersionUID = 5186888641454350567L;
	protected Map<String,DictionaryItem> items = new HashMap<>();
	protected Map<String,Object> properties = new ConcurrentHashMap<>();
	protected String searchField = "mCode";
	protected String searchFieldEx = "text";
	protected String id;
	protected boolean inited = true;
	protected boolean reloadable = true;
	
	protected char searchExSymbol = '.';
	protected char searchKeySymbol = '/';
	
	public AbstractDictionary(){
		
	}
	
	public AbstractDictionary(String id){
		this.id = id;
	}
	
	public void addItem(DictionaryItem item){
		items.put(item.getKey(), item);
	}
	
	public void updateItem(DictionaryItem item){
		addItem(item);
		if(this.inited){
			onItemUpdate(item);
		}
	}
	
	public void removeItem(String key){
		items.remove(key);
		if(this.inited){
			onItemRemoved(key);
		}
	}
	
	protected  void onItemUpdate(DictionaryItem item){
		
	}
	
	protected  void onItemRemoved(String key){
		
	}
	
	public DictionaryItem getItem(String key){
		return items.get(key);
	}
	
	public boolean keyExist(String key){
		return items.containsKey(key);
	}
	
	public String getText(String key){
		if(items.containsKey(key)){
			return items.get(key).getText();
		}
		return "";
	}
	
	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getSearchFieldEx() {
		return searchFieldEx;
	}

	public void setSearchFieldEx(String searchFieldEx) {
		this.searchFieldEx = searchFieldEx;
	}

	public char getSearchExSymbol() {
		return searchExSymbol;
	}

	public void setSearchExSymbol(char searchExSymbol) {
		this.searchExSymbol = searchExSymbol;
	}

	public char getSearchKeySymbol() {
		return searchKeySymbol;
	}

	public void setSearchKeySymbol(char searchKeySymbol) {
		this.searchKeySymbol = searchKeySymbol;
	}
	
	public abstract List<DictionaryItem> getSlice(String parentKey,int sliceType,String query);
	
	@Override
	public boolean isInited(){
		return inited;
	}
	
	@Override
	public void init() throws ControllerException{
		inited = true;
	};
	
	public boolean isReloadable(){
		return reloadable;
	}
}
