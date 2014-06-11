package ctd.test.schema;

import ctd.controller.service.ConfigurableType;
import ctd.util.converter.ConversionUtils;

public class ConverEmTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ConfigurableType t =ConversionUtils.convert("DICTIONARY", ConfigurableType.class);
		System.out.println(t);
	}

}
