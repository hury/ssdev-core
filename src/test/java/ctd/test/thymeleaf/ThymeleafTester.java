package ctd.test.thymeleaf;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafTester {

	/**
	 * @param args
	 * @throws DocumentException 
	 */
	public static void main(String[] args) throws DocumentException {
		
		ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setTemplateMode("XHTML");
		resolver.setSuffix(".html");
		resolver.setPrefix("ctd/test/thymeleaf/");
		resolver.setCharacterEncoding("utf-8");
		resolver.setCacheTTLMs(10000l);
		
		
		TemplateEngine templateEngine = new TemplateEngine(); 
		templateEngine.setTemplateResolver(resolver);
		
		
		
		Context ctx = new Context();
		
		Document doc = DocumentHelper.parseText("<root><product year='6'/><product year='7'/><product id='8'/><product id='9'/></root>");
		System.out.println(doc.asXML());
		ctx.setVariable("welcome", "test welcome");
		ctx.setVariable("r", doc);
		String result = templateEngine.process("template", ctx);
		System.out.println(result);
	}

}
