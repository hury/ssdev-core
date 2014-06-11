package ctd.test.thymeleaf;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import ctd.util.xml.XMLHelper;

public class ThymeleafTester2 {

	/**
	 * @param args
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws DocumentException, IOException {
		
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setTemplateMode("XML");
		resolver.setSuffix(".xml");
		resolver.setPrefix("ctd/test/thymeleaf/");
		resolver.setCharacterEncoding("utf-8");
		resolver.setCacheTTLMs(10000l);
		
		
		TemplateEngine templateEngine = new TemplateEngine(); 
		templateEngine.setTemplateResolver(resolver);
		
		
		
		Context ctx = new Context();
		
		Document doc = DocumentHelper.parseText("<root><item year='2013' lefts='678' dies='6'/><item year='2012' lefts='478' dies='3'/><item year='2011' lefts='68' dies='2'/></root>");
		
		ctx.setVariable("welcome", "test welcome");
		ctx.setVariable("doc", doc);
		String result = templateEngine.process("template", ctx);
		
		Document doc2 = DocumentHelper.parseText(result);
		XMLHelper.putDocument(new File("d:/word.docx"), doc2);
	}

}
