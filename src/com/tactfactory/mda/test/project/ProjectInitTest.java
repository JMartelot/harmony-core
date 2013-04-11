/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.test.project;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.tactfactory.mda.Harmony;
import com.tactfactory.mda.command.ProjectCommand;
import com.tactfactory.mda.test.CommonTest;
import com.tactfactory.mda.utils.ConsoleUtils;
import com.tactfactory.mda.utils.TactFileUtils;

/** 
 * Test class for project initialization.
 */
public class ProjectInitTest extends CommonTest {
	
	/**
	 * Initialization of the test.
	 * @throws Exception 
	 */
	@BeforeClass
	public static void setUpBefore() throws Exception {
		CommonTest.setUpBefore();
	}

	@Before
	@Override
	public final void setUp() throws Exception {
		super.setUp();
	}

	@After
	@Override
	public final void tearDown() throws Exception {
		super.tearDown();

		ConsoleUtils.display("################################  Cleaner !! ################################");
		final File dirproj = new File(Harmony.getProjectAndroidPath());
		TactFileUtils.deleteRecursive(dirproj);
	}
	
	/**
	 * Test the initialization of the android project.
	 */
	@Test
 	public final void initAndroid() {
		System.out.println("\nTest Project init Android");
		System.out.println(SHARP_DELIMITOR);
		
		// Generate Project
		CommonTest.getHarmony().findAndExecute(ProjectCommand.INIT_ANDROID,
				null,
				null);
		
		CommonTest.hasFindFile("android/AndroidManifest.xml");
		CommonTest.hasFindFile("android/build.xml");
		CommonTest.hasFindFile("android/lint.xml");
		//this.isFindFile("android/local.properties");
		CommonTest.hasFindFile("android/proguard-project.txt");
		CommonTest.hasFindFile("android/project.properties");
		
		CommonTest.hasFindFile("android/src");
		CommonTest.hasFindFile("android/src/com/tactfactory/mda/test/demact");
		CommonTest.hasFindFile(
				"android/src/com/tactfactory/mda/test/demact/entity");
		//this.isFindFile("android/res/");
		
		CommonTest.hasFindFile("android/libs");
		//CommonTest.hasFindFile("android/libs/android-support-v4.jar");
		CommonTest.hasFindFile("android/libs/harmony.jar");
		CommonTest.hasFindFile("android/libs/joda-time-2.1.jar");
		
		CommonTest.hasFindFile("android/res");
		CommonTest.hasFindFile("android/res/values");
		CommonTest.hasFindFile("android/res/values/configs.xml");
		CommonTest.hasFindFile("android/res/values/strings.xml");
	}
	
	/**
	 * Test the initialization of the iPhone project.
	 */
	@Ignore
	@Test
	public final void initIphone() {
		System.out.println("\nTest Project init iphone");
		System.out.println(SHARP_DELIMITOR);
		
		CommonTest.getHarmony().findAndExecute(
				ProjectCommand.INIT_IOS, null, null);
		
		// TODO add asserts (for folder/file exist..)
	}
	
	/**
	 * Test the initialization of the RIM project.
	 */	
	@Ignore
	@Test
	public final void initRim() {
		System.out.println("\nTest Project init RIM");
		System.out.println(SHARP_DELIMITOR);
		
		CommonTest.getHarmony().findAndExecute(
				ProjectCommand.INIT_RIM, null, null);
		
		// TODO add asserts (for folder/file exist..)
	}
	
	/**
	 * Test the initialization of the Windows Phone project.
	 */
	@Ignore
	@Test
	public final void initWinphone() {
		System.out.println("\nTest Project init Windows Phone");
		System.out.println(SHARP_DELIMITOR);
		
		
		CommonTest.getHarmony().findAndExecute(ProjectCommand.INIT_WINPHONE, 
									null, 
									null);
		
		// TODO add asserts (for folder/file exist..)
	}

}
