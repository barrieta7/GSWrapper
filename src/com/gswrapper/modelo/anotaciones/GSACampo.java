package com.gswrapper.modelo.anotaciones;

import java.lang.annotation.*;

/**
 * 
 * GSACampo.java
 *
 * @author Borja Arrieta Zueco
 * @version 1.0v
 * @date 2019-07-19
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface GSACampo {

	String value() default "";
}
