/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.bundles.rest.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import com.tactfactory.mda.utils.ConsoleUtils;

/**
 * To mark a entity for remote/central persistence/access the @Rest annotation is used.
 */
@Documented
@Retention(SOURCE)
@Target(TYPE)
@Inherited
public @interface Rest {
	
	/** Security access of REST query */
	public enum Security {
		NONE(0),
		SESSION(1);
		
		private int value;
		
		private Security(final int value) {
			this.value = value;
		}
		
		public int getValue(){
			return this.value;
		}
		
		public static Security fromValue(final int value){
			Security ret = null;
				for (final Security type : Security.values()) {
					if (value == type.value) {
						ret = type;
					}    
				}
			
			return ret;
		}
		
		public static Security fromName(String name){
			Security ret;
			if (name.lastIndexOf('.')>0) {
				name = name.substring(name.lastIndexOf('.')+1); // Take only what comes after the last dot
			}
			ConsoleUtils.displayDebug("Searching for Security : "+name);
			try {
				final Field field = Security.class.getField(name);	
				if (field.isEnumConstant()) {
					ConsoleUtils.displayDebug("Found Security : "+name);
					ret = (Security)field.get(Security.class);
				} else {
					ret = null;
				}
			} catch (final NoSuchFieldException e) {
				ret = null;
			} catch (final IllegalAccessException e) {
				ret = null;				
			}
			
			return ret;
		}
	}
	
	/**
	 * The uri for entity
	 * 
	 * @return (optional, defaults to entity name) The path route to use for entity.
	 * 
	 */
	String uri() default "";
	
	/**
	 * The security level
	 * 
	 * @return (optional, defaults to none) Security level to use.
	 */
	Security security() default Security.NONE;
}
