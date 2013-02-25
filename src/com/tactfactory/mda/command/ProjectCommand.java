/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.command;

import net.xeoh.plugins.base.annotations.Capabilities;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.tactfactory.mda.Console;
import com.tactfactory.mda.Harmony;
import com.tactfactory.mda.TargetPlatform;
import com.tactfactory.mda.meta.ApplicationMetadata;
import com.tactfactory.mda.plateforme.AndroidAdapter;
import com.tactfactory.mda.plateforme.BaseAdapter;
import com.tactfactory.mda.plateforme.IosAdapter;
import com.tactfactory.mda.plateforme.RimAdapter;
import com.tactfactory.mda.plateforme.WinphoneAdapter;
import com.tactfactory.mda.template.ApplicationGenerator;
import com.tactfactory.mda.template.ProjectGenerator;
import com.tactfactory.mda.utils.ConsoleUtils;

/**
 * Project Structure Generator.
 * 
 * can :
 * <ul>
 * <li>Make a structure</li>
 * <li>Remove project</li>
 * </ul>
 *
 */
@PluginImplementation
public class ProjectCommand extends BaseCommand {
	/** Bundle name. */
	public static final String BUNDLE 		= "project";
	
	/** Error Message. */
	private static final String ERROR_MSG = 
			"Please check your file browser or file editor and try again...";

	// Actions
	/** Init action. */
	public static final String ACTION_INIT 	= "init";
	/** Remove action. */
	public static final String ACTION_REMOVE 	= "remove";

	// Commands
	/** Command : PROJECT:INIT:ANDROID. */
	public static final String INIT_ANDROID 	= BUNDLE 
								+ SEPARATOR 
								+ ACTION_INIT 
								+ SEPARATOR 
								+ TargetPlatform.ANDROID.toLowerString();
	
	/** Command : PROJECT:INIT:IOS. */
	public static final String INIT_IOS 		= BUNDLE 
								+ SEPARATOR 
								+ ACTION_INIT 
								+ SEPARATOR 
								+ TargetPlatform.IPHONE.toLowerString();
	
	/** Command : PROJECT:INIT:RIM. */
	public static final String INIT_RIM 		= BUNDLE 
								+ SEPARATOR 
								+ ACTION_INIT 
								+ SEPARATOR 
								+ TargetPlatform.RIM.toLowerString();
	
	/** Command : PROJECT:INIT:WINPHONE. */
	public static final String INIT_WINPHONE 	= BUNDLE 
								+ SEPARATOR 
								+ ACTION_INIT 
								+ SEPARATOR 
								+ TargetPlatform.WINPHONE.toLowerString();
	
	/** Command : PROJECT:INIT:ALL. */
	public static final String INIT_ALL		= BUNDLE 
								+ SEPARATOR 
								+ ACTION_INIT 
								+ SEPARATOR 
								+ TargetPlatform.ALL.toLowerString();

	/** Command : PROJECT:REMOVE:ANDROID. */
	public static final String REMOVE_ANDROID = BUNDLE 
								+ SEPARATOR 
								+ ACTION_REMOVE 
								+ SEPARATOR 
								+ TargetPlatform.ANDROID.toLowerString();
	
	/** Command : PROJECT:REMOVE:IOS. */
	public static final String REMOVE_IOS 	= BUNDLE 
								+ SEPARATOR 
								+ ACTION_REMOVE 
								+ SEPARATOR 
								+ TargetPlatform.IPHONE.toLowerString();
	
	/** Command : PROJECT:REMOVE:RIM. */
	public static final String REMOVE_RIM 	= BUNDLE 
								+ SEPARATOR 
								+ ACTION_REMOVE 
								+ SEPARATOR 
								+ TargetPlatform.RIM.toLowerString();
	
	/** Command : PROJECT:REMOVE:WINPHONE. */
	public static final String REMOVE_WINPHONE = BUNDLE 
								+ SEPARATOR 
								+ ACTION_REMOVE 
								+ SEPARATOR 
								+ TargetPlatform.WINPHONE.toLowerString();
	
	/** Command : PROJECT:REMOVE:ALL. */
	public static final String REMOVE_ALL 	= BUNDLE 
								+ SEPARATOR 
								+ ACTION_REMOVE 
								+ SEPARATOR 
								+ TargetPlatform.ALL.toLowerString();

	// Internal	
	/** Android adapter. */
	private final BaseAdapter adapterAndroid = new AndroidAdapter();
	/** iOS adapter. */
	private final BaseAdapter adapterIOS = new IosAdapter();
	/** RIM adapter. */
	private final BaseAdapter adapterRIM = new RimAdapter();
	/** Windows Phone adapter. */
	private final BaseAdapter adapterWinPhone = new WinphoneAdapter();

	/** Has user confirmed ? */
	private boolean userHasConfirmed = false;
	
	/** Is project initialized ? */
	private boolean isProjectInit = false;

	/**
	 * Init Project Parameters (project name, namespace, android sdk path).
	 */
	public final void initProjectParam() {
		if (!this.isProjectInit) {
			while (!this.userHasConfirmed) {
				ConsoleUtils.display(">> Project Parameters");

				//Project Name
				if (!this.getCommandArgs().containsKey("name")) {
					Harmony.initProjectName();
				} else {
					ApplicationMetadata.INSTANCE.setName(
							this.getCommandArgs().get("name"));
				}
					
				//Project NameSpace
				if (!this.getCommandArgs().containsKey("namespace")) {
					Harmony.initProjectNameSpace();
				} else {
					ApplicationMetadata.INSTANCE.setProjectNameSpace(
							this.getCommandArgs().get("namespace")
								.replaceAll("\\.", "/"));
				}
					
				//Android sdk path
				if (!this.getCommandArgs().containsKey("androidsdk")) {
					Harmony.initProjectAndroidSdkPath();
				} else {
					ApplicationMetadata.setAndroidSdkPath(
							this.getCommandArgs().get("androidsdk"));
				}
					
				ConsoleUtils.displayDebug("Project Name: "	 
							+ ApplicationMetadata.INSTANCE.getName()
						 + "\nProject NameSpace: "			 
							+ ApplicationMetadata.INSTANCE.getProjectNameSpace()
						 + "\nAndroid SDK Path: "			 
							+ ApplicationMetadata.getAndroidSdkPath());

				// Confirmation
				if (ConsoleUtils.isConsole()) {
					final String accept =
							Harmony.getUserInput("Use below given parameters "
									 + "to process files? (y/n) ");
					
					if (!accept.contains("n")) {
						this.userHasConfirmed 			= true;
					} else {
						ApplicationMetadata.INSTANCE.setName("");
						ApplicationMetadata.INSTANCE.setProjectNameSpace("");
						ApplicationMetadata.setAndroidSdkPath("");
						this.getCommandArgs().clear();
					}
				} else {
					this.userHasConfirmed = true;
				}
			}
			
			this.userHasConfirmed = false;
			this.isProjectInit = true;
		}
	}

	/**
	 * Initialize Android Project folders and files.
	 * @return success of Android project initialization
	 */
	public final boolean initAndroid() {
		ConsoleUtils.display("> Init Project Google Android");

		this.initProjectParam();
		boolean result = false;

		try {
			if (new ProjectGenerator(this.adapterAndroid).makeProject()) {
				ConsoleUtils.displayDebug("Init Android Project Success!");
				
				new ApplicationGenerator(this.adapterAndroid)
							.generateApplication();
				result = true;
			} else {
				ConsoleUtils.displayError(
						new Exception("Init Android Project Fail!"));
			}
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		}
		
		return result;
	}

	/**
	 * Initialize IOS Project folders and files.
	 * @return success of IOS project initialization
	 */
	public final boolean initIOS() {
		ConsoleUtils.display("> Init Project Apple iOS");

		this.initProjectParam();
		boolean result = false;
		
		try {
			if (new ProjectGenerator(this.adapterIOS).makeProject()) {
				ConsoleUtils.displayDebug("Init IOS Project Success!");
				result = true;
			} else {
				ConsoleUtils.displayError(
						new Exception("Init IOS Project Fail!"));
			}
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		}
		
		return result;
	}

	/**
	 * Initialize RIM Project folders and files.
	 * @return success of RIM project initialization
	 */
	public final boolean initRIM() {
		ConsoleUtils.display("> Init Project BlackBerry RIM");

		this.initProjectParam();
		boolean result = false;
		
		try {
			if (new ProjectGenerator(this.adapterRIM).makeProject()) {
				ConsoleUtils.displayDebug("Init RIM Project Success!");
				result = true;
			} else {
				ConsoleUtils.displayError(
						new Exception("Init RIM Project Fail!"));
			}
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		}
		
		return result;
	}

	/**
	 * Initialize Windows Phone Project folders and files.
	 * @return success of Windows Phone project initialization
	 */
	public final boolean initWinPhone() {
		ConsoleUtils.display("> Init Project Windows Phone");

		this.initProjectParam();
		boolean result = false;
		
		try {
			if (new ProjectGenerator(this.adapterWinPhone).makeProject()) {
				ConsoleUtils.displayDebug("Init WinPhone Project Success!");
				result = true;
			} else {
				ConsoleUtils.displayError(
						new Exception("Init WinPhone Project Fail!"));
			}
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		}
		
		return result;
	}

	/**
	 * Initialize all project platforms.
	 */
	public final void initAll() {
		ConsoleUtils.display("> Init All Projects");

		this.initProjectParam();
		this.initAndroid();
		this.initIOS();
		//this.initRIM();
		//this.initWinPhone();		
	}

	/**
	 * Remove Android project folder.
	 */
	public final void removeAndroid() {
		if (!this.userHasConfirmed) {
			final String accept = 
					Harmony.getUserInput("Are you sure to Delete "
							 + "Android Project? (y/n) ");
			
			if (accept.contains("n")) {
				return; 
			}
			
			this.userHasConfirmed = true;
		}
		
		try {
			if (!new ProjectGenerator(this.adapterAndroid).removeProject()) {
				ConsoleUtils.display(ERROR_MSG);
			}
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		}
	}

	/**
	 * Remove IOS project folder.
	 */
	public final void removeIOS() {
		if (!this.userHasConfirmed) {
			final String accept = 
					Harmony.getUserInput("Are you sure to Delete "
							+ "Apple iOS Project? (y/n) ");
			
			if (accept.contains("n")) { return; }
			
			this.userHasConfirmed = true;
		}
		
		try {
			if (!new ProjectGenerator(this.adapterIOS).removeProject()) {
				ConsoleUtils.display(ERROR_MSG);
			}
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		}
	}

	/**
	 * Remove RIM project folder.
	 */
	public final void removeRIM() {
		if (!this.userHasConfirmed) {
			final String accept =
					Harmony.getUserInput(
							"Are you sure to Delete"
							+ " BlackBerry Rim Project? (y/n)");
			
			if (accept.contains("n")) { 
				return; 
			}
			
			this.userHasConfirmed = true;
		}
		
		try {
			if (!new ProjectGenerator(this.adapterRIM).removeProject()) {
				ConsoleUtils.display(ERROR_MSG);
			}
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		}
	}

	/**
	 * Remove Windows Phone project folder.
	 */
	public final void removeWinPhone() {
		if (!this.userHasConfirmed) {
			final String accept = Harmony.getUserInput(
					"Are you sure to Delete Windows Phone Project? (y/n) ");
			
			if (accept.contains("n")) { return; }
			
			this.userHasConfirmed = true;
		}
		
		try {
			if (!new ProjectGenerator(this.adapterWinPhone).removeProject()) {
				ConsoleUtils.display(ERROR_MSG);
			}
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		}
	}

	/**
	 * Remove all project platforms.
	 */
	public final void removeAll() {
		if (!this.userHasConfirmed) {
			final String accept = 
					Harmony.getUserInput(
							"Are you sure to Delete All Projects? (y/n) ");
			
			if (accept.contains("n")) { return; }
			
			this.userHasConfirmed = true;
		}
		
		try {
			boolean removedAndroid = 
					new ProjectGenerator(this.adapterAndroid).removeProject();
			boolean removedIOS =
					new ProjectGenerator(this.adapterIOS).removeProject();
			boolean removedRIM = 
					new ProjectGenerator(this.adapterRIM).removeProject();
			boolean removedWin =
					new ProjectGenerator(this.adapterWinPhone).removeProject();
			if (!removedAndroid
				|| !removedIOS
				|| !removedRIM
				|| !removedWin) {
				ConsoleUtils.display(ERROR_MSG);
			}
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		} finally {
			this.userHasConfirmed = false;
		}
	}

	@Override
	public final void summary() {
		ConsoleUtils.display("\n> PROJECT \n"  
				+ "\t" + INIT_ANDROID 
				+ "\t => Init Android project directory\n"
				
				+ "\t" + INIT_IOS 
				+ "\t => Init Apple IOS project directory\n"  
				
				+ "\t" + INIT_RIM
				+ "\t => Init BlackBerry project directory\n" 
				
				+ "\t" + INIT_WINPHONE 
				+ "\t => Init Windows Phone project directory\n"
				
				+ "\t" + INIT_ALL 
				+ "\t => Init All project directories\n" 
				
				+ "\t" + REMOVE_ANDROID 
				+ "\t => Remove Google Android project directory\n"
				
				+ "\t" + REMOVE_IOS 
				+ "\t => Remove Apple IOS project directory\n"
				
				+ "\t" + REMOVE_RIM 
				+ "\t => Remove BlackBerry project directory\n"
				
				+ "\t" + REMOVE_WINPHONE
				+ "\t => Remove Windows Phone project directory\n"
				
				+ "\t" + REMOVE_ALL
				+ "\t => Remove All project directories\n");
	}

	@Override
	public final void execute(final String action,
			final String[] args, 
			final String option) {
		this.setCommandArgs(Console.parseCommandArgs(args));
		
		if (action.equals(INIT_ANDROID)) {
			this.initAndroid();
		} else

		if (action.equals(INIT_IOS)) {
			this.initIOS();
		} else

//		if (action.equals(INIT_RIM)) {
//			this.initRIM();
//		} else

//		if (action.equals(INIT_WINPHONE)) {
//			this.initWinPhone();
//		} else

		if (action.equals(INIT_ALL)) {
			this.initAll();
		} else

		if (action.equals(REMOVE_ANDROID)) {
			this.removeAndroid();
		} else

		if (action.equals(REMOVE_IOS)) {
			this.removeIOS();
		} else

//		if (action.equals(REMOVE_RIM)) {
//			this.removeRIM();
//		} else

//		if (action.equals(REMOVE_WINPHONE)) {
//			this.removeWinPhone();
//		} else

		if (action.equals(REMOVE_ALL)) {
			this.removeAll();
		}
	}

	@Override
	@Capabilities
	public final boolean isAvailableCommand(final String command) {
		return  command.equals(INIT_ANDROID) 
				|| command.equals(INIT_IOS) 
				//|| command.equals(INIT_RIM) 
				//|| command.equals(INIT_WINPHONE) 
				|| command.equals(INIT_ALL) 
				|| command.equals(REMOVE_ANDROID) 
				|| command.equals(REMOVE_IOS) 
				//|| command.equals(REMOVE_RIM)
				//|| command.equals(REMOVE_WINPHONE) 
				|| command.equals(REMOVE_ALL);
	}
}
