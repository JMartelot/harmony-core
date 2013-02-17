/**
 * This file is part of the Harmony package.
 */
package com.tactfactory.mda.template;

import java.io.File;
import java.util.ArrayList;

import com.google.common.base.CaseFormat;
import com.tactfactory.mda.ConsoleUtils;
import com.tactfactory.mda.Harmony;
import com.tactfactory.mda.plateforme.BaseAdapter;
import com.tactfactory.mda.utils.FileUtils;

public class ProjectGenerator extends BaseGenerator {
	protected boolean isWritable = true;

	public ProjectGenerator(BaseAdapter adapter) throws Exception {
		super(adapter);

		this.datamodel = this.appMetas.toMap(this.adapter);
	}

	public ProjectGenerator(BaseAdapter adapter, Boolean isWritable) throws Exception {
		this(adapter);

		this.isWritable = isWritable;
	}

	/**
	 * Make Platform specific Project Structure
	 * @return success to make the platform project folder
	 */
	public boolean makeProject(){
		boolean result = false;
		if(this.adapter.getPlatform().equals("android")) {
			result = this.makeProjectAndroid();
		}
		else if(this.adapter.getPlatform().equals("ios")) {
			result = this.makeProjectIOS();
		}
		else if(this.adapter.getPlatform().equals("rim")) {
			result = this.makeProjectRIM();
		}
		else if(this.adapter.getPlatform().equals("winphone")) {
			result = this.makeProjectWinPhone();
		}

		return result;
	}
	
	/**
	 * Remove Platform specific Project Structure
	 * @return success to make the platform project folder
	 */
	public boolean removeProject(){
		boolean result = false;
		File dirproj = new File(String.format("%s/%s/", Harmony.pathProject, this.adapter.getPlatform()));
		
		int removeResult = FileUtils.deleteRecursive(dirproj);

		if(removeResult==0) {
			result = true;
			
			ConsoleUtils.displayDebug("Project "+this.adapter.getPlatform()+" removed!");
		} else {
			ConsoleUtils.displayError(new Exception("Remove Project "+this.adapter.getPlatform()+" return "+removeResult+" errors...\n"));
		}
		return result;
	}

	/**
	 * Generate HomeActivity File and merge it with datamodel
	 */
	public void generateHomeActivity() {
		ConsoleUtils.display(">> Generate HomeView & Strings...");

		String fullFilePath = this.adapter.getHomeActivityPathFile();
		String fullTemplatePath = this.adapter.getTemplateHomeActivityPathFile().substring(1);

		super.makeSource(fullTemplatePath, fullFilePath, true);
	}

	/**
	 * Make Android Project Structure
	 * @return success to make the platform project folder
	 */
	protected boolean makeProjectAndroid(){
		boolean result = false;

		// create project name space folders
		FileUtils.makeFolder(this.adapter.getSourcePath() + this.appMetas.projectNameSpace.replaceAll("\\.","/"));

		// create empty package entity
		FileUtils.makeFolder(this.adapter.getSourcePath() + this.appMetas.projectNameSpace.replaceAll("\\.","/")+"/entity/" );
		
		// create util folder
		FileUtils.makeFolder(this.adapter.getSourcePath() + this.appMetas.projectNameSpace.replaceAll("\\.","/")+"/harmony/util/" );
		
		// create libs folder
		FileUtils.makeFolder(this.adapter.getLibsPath());
		

		// create HomeActivity.java 
		super.makeSource(this.adapter.getHomeActivityPathFile(),
				this.adapter.getTemplateHomeActivityPathFile(),
				false);

		// create configs.xml
		super.makeSource(
				this.adapter.getTemplateRessourceValuesPath()+"configs.xml",
				this.adapter.getRessourceValuesPath()+"configs.xml",
				false);

		// create strings.xml
		super.makeSource(
				this.adapter.getTemplateStringsPathFile(),
				this.adapter.getStringsPathFile(),
				false);
		
		// create configs.xml
		super.makeSource(
				this.adapter.getTemplateRessourceValuesPath()+"styles.xml",
				this.adapter.getRessourceValuesPath()+"styles.xml",
				false);

		// create main.xml
		super.makeSource(
				this.adapter.getTemplateRessourceLayoutPath()+"main.xml",
				this.adapter.getRessourceLayoutPath()+"main.xml",
				false);
		
		// create HarmonyFragmentActivity
		super.makeSource(
				this.adapter.getTemplateSourcePath()+"harmony/view/HarmonyFragmentActivity.java",
				"./app/android/src/"+this.appMetas.projectNameSpace+"/harmony/view/"+"HarmonyFragmentActivity.java",
				false);
		
		// create HarmonyFragment
		super.makeSource(
				this.adapter.getTemplateSourcePath()+"harmony/view/HarmonyFragment.java",
				"./app/android/src/"+this.appMetas.projectNameSpace+"/harmony/view/"+"HarmonyFragment.java",
				false);
		
		// create HarmonyListFragment
		super.makeSource(
				this.adapter.getTemplateSourcePath()+"harmony/view/HarmonyListFragment.java",
				"./app/android/src/"+this.appMetas.projectNameSpace+"/harmony/view/"+"HarmonyListFragment.java",
				false);
		
		// create ProjectMenuBase
		super.makeSource(
				this.adapter.getTemplateSourcePath()+"menu/TemplateMenuBase.java",
				"./app/android/src/"+this.appMetas.projectNameSpace+"/menu/"+CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, this.appMetas.name) +"MenuBase.java",
				false);
		
		// create ProjectMenu
		super.makeSource(
				this.adapter.getTemplateSourcePath()+"menu/TemplateMenu.java",
				"./app/android/src/"+this.appMetas.projectNameSpace+"/menu/"+CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, this.appMetas.name) +"Menu.java",
				false);
		
		// create ProjectMenu
		super.makeSource(
				this.adapter.getTemplateSourcePath()+"menu/MenuWrapperBase.java",
				"./app/android/src/"+this.appMetas.projectNameSpace+"/menu/" + "MenuWrapperBase.java",
				false);

		// copy libraries
		this.updateLibrary("joda-time-2.1.jar");
		this.updateLibrary("guava-12.0.jar");
		this.updateLibrary("jsr305.jar");
		
		/// copy sherlock library
		//TODO test if git is install
		ArrayList<String> command = new ArrayList<String>();
		command.add("git"); 						// Command/Tools
		command.add("clone");						// Command action
		command.add("https://github.com/JakeWharton/ActionBarSherlock.git"); // command depot
		command.add("app/android/libs/sherlock"); 	// Command destination folder
		ConsoleUtils.launchCommand(command);
		command.clear();
		command.add("cd");
		command.add("app/android/libs/sherlock");
		ConsoleUtils.launchCommand(command);
		command.clear();
		command.add("git");
		command.add("checkout");
		command.add("4.2.0");
		ConsoleUtils.launchCommand(command);
		
		/*try {
			File dirSherlock = new File(String.format("%s/%s", Harmony.pathLibs, "sherlock-4.2"));
			File dirSherlockDest = new File(String.format("%s/%s", this.adapter.getLibsPath(), "sherlock-4.2"));
			FileUtils.copyDirectory(dirSherlock, dirSherlockDest);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		/// copy Harmony library
		FileUtils.copyfile(new File(String.format("%s/%s",Harmony.pathHarmony,"harmony.jar")),
				new File(String.format("%s/%s",this.adapter.getLibsPath(),"harmony.jar")));
		
		// copy utils
		this.updateUtil("DateUtils.java");

		// Update newly created files with datamodel
		File dirTpl = new File(this.adapter.getTemplateProjectPath());
		if(dirTpl.exists() && dirTpl.listFiles().length!=0) {
			result = true;
			
			for(int i=0;i<dirTpl.listFiles().length;i++) {
				if(dirTpl.listFiles()[i].isFile()) {
					super.makeSource(
							this.adapter.getTemplateProjectPath() + dirTpl.listFiles()[i].getName(),
							String.format("%s/%s/", Harmony.pathProject, this.adapter.getPlatform())+dirTpl.listFiles()[i].getName(),
							false);
				}
			}
		}
		return result;
	}

	/**
	 * Make IOS Project Structure
	 * @return success to make the platform project folder
	 */
	protected boolean makeProjectIOS(){
		boolean result = false;
		//Generate base folders & files
		File dirProj = FileUtils.makeFolderRecursive(
				String.format("%s/%s/%s/", Harmony.pathTemplate , this.adapter.getPlatform(), this.adapter.getProject()),
				String.format("%s/%s/", Harmony.pathProject, this.adapter.getPlatform()),
				true);
		
		if(dirProj.exists() && dirProj.listFiles().length!=0)
			result = true;

		return result;
	}

	/**
	 * Make RIM Project Structure
	 * @return success to make the platform project folder
	 */
	protected boolean makeProjectRIM(){
		boolean result = false;

		return result;
	}

	/**
	 * Make Windows Phone Project Structure
	 * @return success to make the platform project folder
	 */
	protected boolean makeProjectWinPhone(){
		boolean result = false;

		return result;
	}
}
