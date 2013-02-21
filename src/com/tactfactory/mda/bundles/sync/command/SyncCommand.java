/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.bundles.sync.command;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.tactfactory.mda.Console;
import com.tactfactory.mda.bundles.rest.parser.RestCompletor;
import com.tactfactory.mda.bundles.rest.parser.RestParser;
import com.tactfactory.mda.bundles.sync.parser.SyncParser;
import com.tactfactory.mda.bundles.sync.template.SyncGenerator;
import com.tactfactory.mda.command.BaseCommand;
import com.tactfactory.mda.meta.ApplicationMetadata;
import com.tactfactory.mda.plateforme.AndroidAdapter;
import com.tactfactory.mda.utils.ConsoleUtils;

@PluginImplementation
public class SyncCommand extends BaseCommand{
	
	//bundle name
	public final static String BUNDLE = "sync";
	public final static String SUBJECT = "generate";

	//actions
	public final static String ACTION_SERVICE = "service";

	//commands
	public final static String GENERATE_SERVICE	= BUNDLE + SEPARATOR + SUBJECT + SEPARATOR + ACTION_SERVICE;

	@Override
	public void execute(final String action, final String[] args, final String option) {
		ConsoleUtils.display("> Sync Generator");

		this.commandArgs = Console.parseCommandArgs(args);
		if (action.equals(GENERATE_SERVICE)) {
			try {
				this.generateAdapters();
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				ConsoleUtils.displayError(e);
			}
		}
	}
	
	/**
	 * Generate java code files from parsed Entities
	 */
	protected void generateAdapters() {
		//Harmony.metas.entities = getMetasFromAll();
		this.generateMetas();
		if (ApplicationMetadata.INSTANCE.entities!=null){
			try {
				new SyncGenerator(new AndroidAdapter()).generateAll();
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				ConsoleUtils.displayError(e);
			}
		}

	}
	
	@Override
	public void generateMetas(){
		this.registerParser(new RestParser());
		this.registerParser(new SyncParser());
		super.generateMetas();
		new RestCompletor().generateApplicationRestMetadata(ApplicationMetadata.INSTANCE);
	}
	
	

	@Override
	public void summary() {
		ConsoleUtils.display("\n> SYNC \n" +
				"\t" + GENERATE_SERVICE + "\t => Generate Adapters");
		
	}

	@Override
	public boolean isAvailableCommand(final String command) {
		return command.equals(GENERATE_SERVICE);
	}
}
