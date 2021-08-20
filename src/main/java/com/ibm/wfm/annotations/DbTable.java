package com.ibm.wfm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DbTable {
	public String columnName();
	public boolean isId() default false;
	public int keySeq() default -1;
	public boolean isScd() default false;
	public int foreignKey() default -1;
}
