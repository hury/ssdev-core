package ctd.test.schema.annotation;

import java.lang.reflect.Method;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.thymeleaf.util.StringUtils;

import ctd.schema.annotation.Expression;
import ctd.util.JSONUtils;
import ctd.util.context.ContextUtils;
import ctd.util.converter.ConversionUtils;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class RuntimeHelperTester {

	/**
	 * @param args
	 * @throws NotFoundException 
	 * @throws ClassNotFoundException 
	 * @throws ExprException 
	 * @throws CannotCompileException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws NotFoundException, ClassNotFoundException, ExprException, CannotCompileException, InstantiationException, IllegalAccessException {
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("/ctd/test/schema/annotation/spring.xml");
		
		ClassPool pool = ClassPool.getDefault();
		pool.importPackage("ctd.util.exp");
		pool.importPackage("ctd.util.converter");
		
		
		CtClass beanCls = pool.get("ctd.test.schema.annotation.Bean");
		
		CtField[] fields = beanCls.getDeclaredFields();
		
		for(CtField f : fields){
			CtClass type = f.getType();
			if(f.hasAnnotation(Expression.class)){
				Expression expr = (Expression) f.getAnnotation(Expression.class);
				StringBuilder body = new StringBuilder("public int get" + StringUtils.capitalize(f.getName()) + "()");
				body.append("{if(($w)lastModify != null){return ($r)lastModify;};return ($r)ExpressionProcessor.instance().run(\"" + expr.value() + "\");}");
				System.out.println(body.toString());
				CtMethod m = CtNewMethod.make(body.toString(),beanCls);
				beanCls.addMethod(m);
				System.out.println("de=" + m.getMethodInfo().getDescriptor());
			}
		}
		System.out.println(pool.getClassLoader().hashCode());
		beanCls.toClass();
		
		//Bean b = (Bean) beanCls.toClass().newInstance();
		Bean b = new Bean();
		
		
		Method[] methods = b.getClass().getDeclaredMethods();
		for(Method m : methods){
			System.out.println(m.getName());
		}
		
		System.out.println(JSONUtils.toString(b));
		
	}

}
