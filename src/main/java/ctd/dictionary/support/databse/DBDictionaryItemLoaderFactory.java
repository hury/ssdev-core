package ctd.dictionary.support.databse;

import ctd.controller.exception.ControllerException;

public class DBDictionaryItemLoaderFactory {

	public static DBDictionaryItemLoader createLoader(String className,DBDictionaryInfo info) throws ControllerException{
		
		try {
			DBDictionaryItemLoader loader =	(DBDictionaryItemLoader) Class.forName(className).newInstance();
			loader.setDictionaryInfo(info);
			return loader;
		} 
		catch (ClassNotFoundException e) {
			throw new ControllerException("dictionary itemloader class[" + className + "] not found");
		}
		catch(Exception e){
			throw new ControllerException(e,"create dictionaryItemLoader["  + className + "] failed.");
		}
		
	
	}
}
