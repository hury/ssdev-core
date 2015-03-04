package ctd.schema.annotation;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ctd.schema.DictionaryIndicator;
import ctd.schema.EvaluatorFactory;
import ctd.schema.SchemaController;
import ctd.schema.SchemaItem;
import ctd.schema.constants.DataTypes;
import ctd.util.MvelTemplater;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

public class AnnotationClassSchemaBuilder {
	private static final Logger logger = LoggerFactory.getLogger(AnnotationClassSchemaBuilder.class);
	private static final ClassPool pool = ClassPool.getDefault();
	
	static{
		pool.importPackage("ctd.util.exp");
		pool.importPackage("ctd.dictionary");
		pool.importPackage("ctd.util.converter");
		
		ClassClassPath classPath = new ClassClassPath(AnnotationClassSchemaBuilder.class);
		pool.insertClassPath(classPath);
	}
	
	public static void build(Set<String> classnames){
		for(String classname : classnames){
			build(classname);
		}
	}
	
	private static void build(String classname){
		
		try {
			CtClass c = pool.getCtClass(classname);
			
			if(!c.hasAnnotation(Schema.class)){
				return;
			}
			
			Schema sa = (Schema)c.getAnnotation(Schema.class);
			String id = StringUtils.isEmpty(sa.id()) ? classname : sa.id();
			String alias = StringUtils.isEmpty(sa.alias()) ? id : sa.alias();
			
			ctd.schema.Schema sc = new ctd.schema.Schema();
			sc.setId(id);
			sc.setAlias(alias);
			sc.setAnnotationClass(classname);
			sc.setLastModify(System.currentTimeMillis());
			
		 	CtField[] fields =  c.getDeclaredFields();
		 	int i = 0;
		 	for(CtField f : fields){
		 		
		 		if(Modifier.isStatic(f.getModifiers())){
		 			continue;
		 		}
		 		
		 		CtClass tp = f.getType();
		 		String typeNm = tp.getSimpleName();
		 		
		 		if(typeNm.equals("Integer")){
		 			typeNm = "int";
		 		}
		 		else if(typeNm.equals("Character")){
		 			typeNm = "char";
		 		}
		 		else{
		 			typeNm = StringUtils.uncapitalize(typeNm);
		 		}
		 		
		 		if(!DataTypes.isSupportType(typeNm)){
		 			continue;
		 		}
		 		
		 		SchemaItem it = new SchemaItem();
		 		it.setIndex(i++);
		 		it.setId(f.getName());
		 		it.setType(typeNm);
		 		it.setSchemaId(sc.getId());
		 		
		 		sc.addItem(it);
		 		
		 		processPropertyAnnotation(c,f,sc,it);
		 		
		 		if(f.hasAnnotation(Column.class)){
		 			Column cola = (Column) f.getAnnotation(Column.class);
		 			if(!cola.nullable()){
		 				it.setRequired(true);
		 			}
		 		}
		 		
		 		if(f.hasAnnotation(Id.class)){
		 			it.setPkey(true);
		 			sc.setKey(it.getId());
		 			if(f.hasAnnotation(GeneratedValue.class)){
		 				it.setKeyGenerator(ctd.schema.Schema.KEY_GEN_AUTO);
		 			}
		 		}
		 		
		 		if(f.hasAnnotation(ItemProperty.class)){
		 			ItemProperty ita = (ItemProperty) f.getAnnotation(ItemProperty.class);
		 			if(!StringUtils.isEmpty(ita.alias())){
		 				it.setAlias(ita.alias());
		 			}
		 			if(!StringUtils.isEmpty(ita.defaultValue())){
		 				it.setDefaultValue(ita.defaultValue());
		 			}
		 			if(!StringUtils.isEmpty(ita.maxValue())){
		 				it.setMaxValue(ita.maxValue());
		 			}
		 			
		 			if(!StringUtils.isEmpty(ita.minValue())){
		 				it.setMinValue(ita.minValue());
		 			}
		 			if(ita.length() > 0){
		 				it.setLength(ita.length());
		 			}
		 		}
		 		
		 		if(f.hasAnnotation(Expression.class)){
		 			Expression expr = (Expression) f.getAnnotation(Expression.class);
		 			String exprStr = expr.value();
		 			it.setEvaluator(EvaluatorFactory.newExpressionEvaluator(exprStr));
		 			processExpression(exprStr,f,c,expr.updatable());
		 		}
		 		
		 		if(f.hasAnnotation(Dictionary.class)){
		 			DictionaryIndicator di = new DictionaryIndicator();
		 			Dictionary da = (Dictionary) f.getAnnotation(Dictionary.class);
		 			di.setId(da.id());
		 			di.setParentKey(da.parentKey());
		 			di.setSlice(da.sliceType());
		 			it.setDic(di);
		 			processDictionary(da.id(),f,c);
		 		}
		 	}
		 	c.toClass();
		 	c.detach();
		 	SchemaController.instance().add(sc);
		 	logger.info("build schema from annotation class[{}]", classname);
		} 
		catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	private static void processPropertyAnnotation(CtClass c,CtField f,ctd.schema.Schema sc,SchemaItem it){
		String fn = StringUtils.capitalize(f.getName());
		String methodName = "get" + fn;
		try{
			CtMethod m = c.getMethod(methodName, "()" + f.getSignature());
			if(m.hasAnnotation(Id.class)){
	 			it.setPkey(true);
	 			sc.setKey(it.getId());
	 			if(m.hasAnnotation(GeneratedValue.class)){
	 				it.setKeyGenerator(ctd.schema.Schema.KEY_GEN_AUTO);
	 				sc.setKeyGenerator(ctd.schema.Schema.KEY_GEN_AUTO);
	 			}
	 		}
			if(m.hasAnnotation(Column.class)){
	 			Column cola = (Column) m.getAnnotation(Column.class);
	 			if(!cola.nullable()){
	 				it.setRequired(true);
	 			}
	 			it.setLength(cola.length());
	 			it.setPrecision(cola.precision());
	 			
	 		}
			
		}
		catch(NotFoundException e){} 
		catch (ClassNotFoundException e){};
	}
	
	private static void processDictionary(String dicId,CtField f,CtClass c) throws Exception{
		String fn = StringUtils.capitalize(f.getName());
		String methodName = "get" + fn + "Text";
		
		try{
			c.getMethod(methodName, "()" + f.getSignature());
		}
		catch(NotFoundException e){
		
				Map<String,Object> ctx = new HashMap<>();
				ctx.put("fieldName", fn);
				ctx.put("dicId", dicId);
				String body = (String)MvelTemplater.run("/ctd/schema/annotation/template/DictionaryDisplayMethod.mvel", ctx);
				CtMethod m = CtNewMethod.make(body,c);
				
				ConstPool constpool = m.getMethodInfo().getConstPool();
				AnnotationsAttribute at = new AnnotationsAttribute(constpool,AnnotationsAttribute.visibleTag);
				Annotation annot = new Annotation("javax.persistence.Transient", constpool);
				at.addAnnotation(annot);
				m.getMethodInfo().addAttribute(at);
				
				c.addMethod(m);
		}
	}
	
	private static void processExpression(String exprStr,CtField f,CtClass c,boolean updatable) throws Exception{
		String fn = StringUtils.capitalize(f.getName());
		String methodName = "get" + fn;
		
		try{
			CtMethod m = c.getMethod(methodName, "()" + f.getSignature());
			c.removeMethod(m);
		}
		catch(NotFoundException e){
			
		};
		
		Map<String,Object> ctx = new HashMap<String,Object>();
		ctx.put("fieldName", f.getName());
		ctx.put("funcName", methodName);
		ctx.put("returnType", f.getType().getName());
		ctx.put("exprStr", exprStr);
		ctx.put("updatable", updatable);
		
		String body = (String)MvelTemplater.run("/ctd/schema/annotation/template/ExpressionMethod.mvel", ctx);

		CtMethod m = CtNewMethod.make(body,c);
		c.addMethod(m);
		
	}
	
}
