package com.ibm.wfm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DbInfo {
	public String baseTableName() default "";
	public String beanName() default "";
	public String parentBeanName() default "";
	public String parentBaseTableName() default "";
}
