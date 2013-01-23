package com.tactfactory.mda.search.command;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.tactfactory.mda.Console;
import com.tactfactory.mda.ConsoleUtils;
import com.tactfactory.mda.Harmony;
import com.tactfactory.mda.command.BaseCommand;
import com.tactfactory.mda.plateforme.AndroidAdapter;
import com.tactfactory.mda.search.parser.SearchParser;
import com.tactfactory.mda.search.template.SearchGenerator;

@PluginImplementation
public class SearchCommand  extends BaseCommand{
	
	//bundle name
	public final static String BUNDLE = "search";
	public final static String SUBJECT = "generate";

	//actions
	public final static String ACTION_LOADERS = "loaders";

	//commands
	public static String GENERATE_LOADERS	= BUNDLE + SEPARATOR + SUBJECT + SEPARATOR + ACTION_LOADERS;

	@Override
	public void execute(String action, String[] args, String option) {
		ConsoleUtils.display("> Adapters Generator");

		this.commandArgs = Console.parseCommandArgs(args);
		if (action.equals(GENERATE_LOADERS)) {
			try {
				this.generateLoaders();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Generate java code files from parsed Entities
	 */
	protected void generateLoaders() {

		this.generateMetas();
		if(Harmony.metas.entities!=null){
			try {
				new SearchGenerator(new AndroidAdapter()).generateAll();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void generateMetas(){
		this.registerParser(new SearchParser());
		super.generateMetas();
		//new SearchCompletor().generateApplicationRestMetadata(Harmony.metas);
	}
	
	

	@Override
	public void summary() {
		ConsoleUtils.display("\n> Search \n" +
				"\t" + GENERATE_LOADERS + "\t => Generate Loaders");
		
	}

	@Override
	public boolean isAvailableCommand(String command) {
		return (command.equals(GENERATE_LOADERS));
	}

}
