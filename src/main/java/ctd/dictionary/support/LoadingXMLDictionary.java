package ctd.dictionary.support;

import ctd.dictionary.loader.DictionaryItemLoader;

public abstract class LoadingXMLDictionary extends XMLDictionary {
	private static final long serialVersionUID = 1L;

	public abstract void setLoader(DictionaryItemLoader loader);
}
