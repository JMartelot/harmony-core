/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	com.tactfactory.mda.test.CoreTest.class,
	com.tactfactory.mda.test.project.AllTests.class, 
	com.tactfactory.mda.test.orm.AllTests.class })
public class AllTests {

}
