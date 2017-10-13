// Copyright © 2012-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Prevents obfuscation of any element of the annotated class.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { TYPE })
public @interface KeepAll
{
}