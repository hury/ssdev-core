package ctd.schema.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ctd.dictionary.SliceTypes;

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD)
public @interface Dictionary {
	String id();
	String parentKey() default "";
	String render() default "";
	byte sliceType() default SliceTypes.CHILD_ALL;
}
