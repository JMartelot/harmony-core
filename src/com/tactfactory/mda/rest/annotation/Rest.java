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
 * To mark a property for relational persistence the @Column annotation is used. 
 * This annotation usually requires at least 1 attribute to be set, the type. 
 */
@Documented
@Retention(SOURCE)
@Target(TYPE)
@Inherited
public @interface Rest {
	
	/**
	 * The uri for type to use for the column
	 * 
	 * @return (optional, defaults to type mapping) The database type to use for the column.
	 * 
	 * @see com.tactfactory.mda.orm.SqliteAdapter for mapping list
	 */
	String uri() default "";
}
