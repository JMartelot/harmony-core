/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.rest.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import static java.lang.annotation.ElementType.TYPE;

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
		
		private Security(int value) {
			this.value = value;
		}
		
		public int getValue(){
			return this.value;
		}
		
		public static Security fromValue(int value){
				for (Security type : Security.values()) {
					if (value == type.value) {
						return type;
					}    
				}
			
			return null;
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
