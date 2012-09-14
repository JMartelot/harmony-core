/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.command;

import japa.parser.ast.CompilationUnit;

import java.util.ArrayList;

import com.google.common.base.Strings;
import com.tactfactory.mda.Harmony;
import com.tactfactory.mda.TargetPlatform;
import com.tactfactory.mda.plateforme.AndroidAdapter;
import com.tactfactory.mda.plateforme.BaseAdapter;
import com.tactfactory.mda.plateforme.IosAdapter;
import com.tactfactory.mda.plateforme.RimAdapter;
import com.tactfactory.mda.plateforme.WinphoneAdapter;
import com.tactfactory.mda.template.ProjectGenerator;
import com.tactfactory.mda.utils.OsUtil;

/**
 * Structure project generator
 * 
 * can :
 * <ul>
 * <li>Make a structure</li>
 * <li>Remove project</li>
 * </ul>
 *
 */
public class ProjectCommand extends BaseCommand {
	/** Bundle name */
	public static String BUNDLE 		= "project";
	
	// Actions
	public static String ACTION_INIT 	= "init";
	public static String ACTION_REMOVE 	= "remove";
	
	// Commands
	public static String INIT_ANDROID 	= BUNDLE + SEPARATOR + ACTION_INIT + SEPARATOR + TargetPlatform.ANDROID.toLowerString();
	public static String INIT_IOS 		= BUNDLE + SEPARATOR + ACTION_INIT + SEPARATOR + TargetPlatform.IPHONE.toLowerString();
	public static String INIT_RIM 		= BUNDLE + SEPARATOR + ACTION_INIT + SEPARATOR + TargetPlatform.RIM.toLowerString();
	public static String INIT_WINPHONE 	= BUNDLE + SEPARATOR + ACTION_INIT + SEPARATOR + TargetPlatform.WINPHONE.toLowerString();
	public static String INIT_ALL		= BUNDLE + SEPARATOR + ACTION_INIT + SEPARATOR + TargetPlatform.ALL.toLowerString();

	public static String REMOVE_ANDROID = BUNDLE + SEPARATOR + ACTION_REMOVE + SEPARATOR + TargetPlatform.ANDROID.toLowerString();
	public static String REMOVE_IOS 	= BUNDLE + SEPARATOR + ACTION_REMOVE + SEPARATOR + TargetPlatform.IPHONE.toLowerString();
	public static String REMOVE_RIM 	= BUNDLE + SEPARATOR + ACTION_REMOVE + SEPARATOR + TargetPlatform.RIM.toLowerString();
	public static String REMOVE_WINPHONE= BUNDLE + SEPARATOR + ACTION_REMOVE + SEPARATOR + TargetPlatform.WINPHONE.toLowerString();
	public static String REMOVE_ALL 	= BUNDLE + SEPARATOR + ACTION_REMOVE + SEPARATOR + TargetPlatform.ALL.toLowerString();

	// Internal
	private static final String DEFAULT_PROJECT_NAME = "toto";
	private static final String DEFAULT_PROJECT_NAMESPACE = "com.tactfactory.toto";
	
	protected BaseAdapter adapterAndroid = new AndroidAdapter();
	protected BaseAdapter adapterIOS = new IosAdapter();
	protected BaseAdapter adapterRIM = new RimAdapter();
	protected BaseAdapter adapterWinPhone = new WinphoneAdapter();

	private static boolean userHasConfirmed = false;
	private static boolean isProjectInit = false;


	/**
	 * Prompt Project Name to the user
	 */
	private void initProjectName()
	{
		if (Strings.isNullOrEmpty(Harmony.projectName)) {
			String projectName = Harmony.getUserInput("Please enter your Project Name ["+DEFAULT_PROJECT_NAME+"]:");
			if(projectName!=null && projectName.length()!=0)
				Harmony.projectName = projectName;
			else
				Harmony.projectName = DEFAULT_PROJECT_NAME;
		}
	}
	
	/**
	 * Prompt Project Name Space to the user
	 */
	private void initProjectNameSpace()
	{
		if (Strings.isNullOrEmpty(Harmony.projectNameSpace)) {
			String projectNameSpace = Harmony.getUserInput("Please enter your Project NameSpace ["+DEFAULT_PROJECT_NAMESPACE+"]:");
			if(projectNameSpace!=null && projectNameSpace.length()!=0) {
				if(projectNameSpace.endsWith(Harmony.projectName))
					Harmony.projectNameSpace = projectNameSpace.replaceAll("\\.", "/");
				else {
					System.out.println("The NameSpace has to end with Project Name !");
					this.initProjectNameSpace();
				}
			}
			else {
				Harmony.projectNameSpace = DEFAULT_PROJECT_NAMESPACE.replaceAll("\\.", "/");
			}
		}
	}
	
	/**
	 * Prompt Project Android SDK Path to the user
	 */
	private void initProjectAndroidSdkPath()
	{
		if (Strings.isNullOrEmpty(Harmony.androidSdkPath)) {
			String sdkPath = Harmony.getUserInput("Please enter AndroidSDK full path [/root/android-sdk/]:");
			if(sdkPath!=null && sdkPath.length()!=0){
				Harmony.androidSdkPath = sdkPath;
			} else {
				if(OsUtil.isWindows()) {
					if(!OsUtil.isX64()) {
						System.out.println("Detected: OS Windows x86");
						Harmony.androidSdkPath = String.format("%s/%s/","C:/Program Files (x86)","android-sdk");
					} else if(OsUtil.isX64()) {
						System.out.println("Detected: OS Windows x64");
						Harmony.androidSdkPath = String.format("%s/%s/","C:/Program Files","android-sdk");
					} else {
						System.out.println("Detected: OS Windows unkn arch");
						Harmony.androidSdkPath = String.format("%s/%s/","C:/Program Files","android-sdk");
					}
				}
				else if(OsUtil.isLinux()) {
					System.out.println("Detected: OS Linux");
					Harmony.androidSdkPath = "/root/android-sdk/";
				}
			}
		}
	}

	/**
	 * Init Project Parameters (project name, namespace, android sdk path)
	 */
	public void initProjectParam()
	{
		if(!isProjectInit) {

			while(!userHasConfirmed)
			{
				System.out.println(">> Project Parameters");
				this.initProjectName();
				this.initProjectNameSpace();
				this.initProjectAndroidSdkPath();
				
				if(Harmony.DEBUG) {
					System.out.println("Project Name: " 	 + Harmony.projectName);
					System.out.println("Project NameSpace: " + Harmony.projectNameSpace);
					System.out.println("Android SDK Path: "+ Harmony.androidSdkPath);
				}
				
				if (Harmony.isConsole) {
					String accept = Harmony.getUserInput("Use below given parameters to process files? (y/n) ");
					if(!accept.contains("n")) {
						userHasConfirmed = true;
					} else {
						Harmony.projectName = null;
						Harmony.projectNameSpace = null;
						Harmony.androidSdkPath = null;
					}
				} else {
					userHasConfirmed = true;
				}
			}
			userHasConfirmed = false;
			isProjectInit = true;
		}
	}
	
	/**
	 * Initialize Android Project folders and files
	 * @return success of Android project initialization
	 */
	public boolean initAndroid()
	{
		System.out.println("\nInit Project Google Android");
		System.out.println("---------------------------\n");
		
		this.initProjectParam();
		boolean result = false;

		try {
			if(new ProjectGenerator(this.adapterAndroid).makeProject()){
				System.out.println("Init Android Project Success!");
				result = true;
			} else {
				System.out.println("Init Android Project Fail!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Initialize IOS Project folders and files
	 * @return success of IOS project initialization
	 */
	public boolean initIOS()
	{
		System.out.println("\nInit Project Apple iOS");
		System.out.println("----------------------\n");

		this.initProjectParam();
		boolean result = false;
		try {
			if(new ProjectGenerator(this.adapterIOS).makeProject()){
				System.out.println("Init IOS Project Success!");
				result = true;
			} else {
				System.out.println("Init IOS Project Fail!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Initialize RIM Project folders and files
	 * @return success of RIM project initialization
	 */
	public boolean initRIM()
	{
		System.out.println("\nInit Project BlackBerry RIM");
		System.out.println("---------------------------\n");

		this.initProjectParam();
		boolean result = false;
		try {
			if(new ProjectGenerator(this.adapterRIM).makeProject()){
				System.out.println("Init RIM Project Success!");
				result = true;
			} else {
				System.out.println("Init RIM Project Fail!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Initialize Windows Phone Project folders and files
	 * @return success of Windows Phone project initialization
	 */
	public boolean initWinPhone()
	{
		System.out.println("\nInit Project Windows Phone");
		System.out.println("--------------------------\n");

		this.initProjectParam();
		boolean result = false;
		try{
			if(new ProjectGenerator(this.adapterWinPhone).makeProject()){
				System.out.println("Init WinPhone Project Success!");
				result = true;
			} else {
				System.out.println("Init WinPhone Project Fail!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Initialize all project platforms
	 */
	public void initAll()
	{
		System.out.println("\nInit All Projects");
		System.out.println("-----------------\n");
		this.initProjectParam();
		this.initAndroid();
		this.initIOS();
		//this.initRIM();
		//this.initWinPhone();		
	}

	/**
	 * Remove Android project folder
	 */
	public void removeAndroid()
	{
		if(!userHasConfirmed) {
			String accept = Harmony.getUserInput("Are you sure to Delete Android Project? (y/n) ");
			if(accept.contains("n")) {
				return;
			}
			userHasConfirmed = true;
		}
		try {
			new ProjectGenerator(this.adapterAndroid).removeProject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Remove IOS project folder
	 */
	public void removeIOS()
	{
		if(!userHasConfirmed) {
			String accept = Harmony.getUserInput("Are you sure to Delete Apple iOS Project? (y/n) ");
			if(accept.contains("n")) {
				return;
			}
			userHasConfirmed = true;
		}
		try {
			new ProjectGenerator(this.adapterIOS).removeProject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Remove RIM project folder
	 */
	public void removeRIM()
	{
		if(!userHasConfirmed) {
			String accept = Harmony.getUserInput("Are you sure to Delete BlackBerry Rim Project? (y/n) ");
			if(accept.contains("n")) {
				return;
			}
			userHasConfirmed = true;
		}
		try {
			new ProjectGenerator(this.adapterRIM).removeProject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Remove Windows Phone project folder
	 */
	public void removeWinPhone()
	{
		if(!userHasConfirmed) {
			String accept = Harmony.getUserInput("Are you sure to Delete Windows Phone Project? (y/n) ");
			if(accept.contains("n")) {
				return;
			}
			userHasConfirmed = true;
		}
		try {
			new ProjectGenerator(this.adapterWinPhone).removeProject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Remove all project platforms
	 */
	public void removeAll()
	{
		if(!userHasConfirmed) {
			String accept = Harmony.getUserInput("Are you sure to Delete All Projects? (y/n) ");
			if(accept.contains("n")) {
				return;
			}
			userHasConfirmed = true;
		}
		try {
			new ProjectGenerator(this.adapterAndroid).removeProject();
			new ProjectGenerator(this.adapterIOS).removeProject();
			new ProjectGenerator(this.adapterRIM).removeProject();
			new ProjectGenerator(this.adapterWinPhone).removeProject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			userHasConfirmed = false;
		}
	}
	
	/** @see BaseCommand#summary() */
	@Override
	public void summary() {
		System.out.print("\n> Project\n");
		System.out.print("\t"+INIT_ANDROID+"\t => Init Android project directory\n");
		System.out.print("\t"+INIT_IOS+"\t => Init Apple IOS project directory\n");
		System.out.print("\t"+INIT_RIM+"\t => Init BlackBerry project directory\n");
		System.out.print("\t"+INIT_WINPHONE+"\t => Init Windows Phone project directory\n");
		System.out.print("\t"+INIT_ALL+"\t => Init All project directories\n");
		
		System.out.print("\t"+REMOVE_ANDROID+"\t => Remove Google Android project directory\n");
		System.out.print("\t"+REMOVE_IOS+"\t => Remove Apple IOS project directory\n");
		System.out.print("\t"+REMOVE_RIM+"\t => Remove BlackBerry project directory\n");
		System.out.print("\t"+REMOVE_WINPHONE+"\t => Remove Windows Phone project directory\n");
		System.out.print("\t"+REMOVE_ALL+"\t => Remove All project directories\n");
	}

	/** @see BaseCommand#execute(String, ArrayList) */
	@Override
	public void execute(String action, ArrayList<CompilationUnit> entities) {
		
		if (action.equals(INIT_ANDROID)) {
			this.initAndroid();
		} else

		if (action.equals(INIT_IOS)) {
			this.initIOS();
		} else
			
		if (action.equals(INIT_RIM)) {
			this.initRIM();
		} else
			
		if (action.equals(INIT_WINPHONE)) {
			this.initWinPhone();
		} else
			
		if (action.equals(INIT_ALL)) {
			this.initAll();
		} else

		if (action.equals(REMOVE_ANDROID)) {
			this.removeAndroid();
		} else

		if (action.equals(REMOVE_IOS)) {
			this.removeIOS();
		} else

		if (action.equals(REMOVE_RIM)) {
			this.removeRIM();
		} else

		if (action.equals(REMOVE_WINPHONE)) {
			this.removeWinPhone();
		} else

		if (action.equals(REMOVE_ALL)) {
			this.removeAll();
		} else
			
		{
			
		}
	}

	/** @see BaseCommand#isAvailableCommand(String) */
	@Override
	public boolean isAvailableCommand(String command) {
		return (command.equals(INIT_ANDROID) ||
				command.equals(INIT_IOS) ||
				//command.equals(INIT_RIM) ||
				//command.equals(INIT_WINPHONE) ||
				command.equals(INIT_ALL) ||
				command.equals(REMOVE_ANDROID) ||
				command.equals(REMOVE_IOS) ||
				command.equals(REMOVE_RIM) ||
				command.equals(REMOVE_WINPHONE) ||
				command.equals(REMOVE_ALL)
				);
	}

}