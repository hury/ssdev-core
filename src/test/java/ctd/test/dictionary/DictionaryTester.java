package ctd.test.dictionary;




import ctd.controller.exception.ControllerException;
import ctd.dictionary.Dictionary;
import ctd.dictionary.DictionaryItem;
import ctd.dictionary.loader.DictionaryLocalLoader;
import ctd.util.JSONUtils;

public class DictionaryTester {

	/**
	 * @param args
	 * @throws ControllerException 
	 */
	public static void main(String[] args) throws ControllerException {
		DictionaryLocalLoader loader = new DictionaryLocalLoader();
		Dictionary dic = loader.load("ctd.test.dictionary.gender");
		System.out.println(dic.getItem("1").getText());
	}

}
