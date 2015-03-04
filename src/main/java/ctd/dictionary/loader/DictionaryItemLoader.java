package ctd.dictionary.loader;

import java.util.List;

import ctd.dictionary.DictionaryItem;

public interface DictionaryItemLoader{
	List<DictionaryItem> findAllDictionaryItem(int start,int limit);
	DictionaryItem getDictionaryItem(Object key);
}
