/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.command;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.tactfactory.mda.Harmony;
import com.tactfactory.mda.utils.ConsoleUtils;

/**
 * General Harmony commands.
 *
 */
@PluginImplementation
public class GeneralCommand extends BaseCommand {
	/** List command. */
	public static final String LIST = "list";
	/** Help command. */
	public static final String HELP = "help";

	/**
	 * Display Help Message.
	 */
	public final void help() {
		ConsoleUtils.display("Usage:\n" 
				+ "\tconsole [--options] command [--parameters]\n" 
				+ "\nUsage example:\n" 
				+ "\tconsole --verbose project:init:android --nam =test " 
				+ "--namespac =com/tact/android/test --sdkdi =/root/android\n" 
				+ "\nPlease use 'console list' to display available commands !"
				);
	}

	/**
	 * Display list of All commands.
	 */
	public final void list() {
		final Command general = 
				Harmony.getInstance().getCommand(GeneralCommand.class);
		
		ConsoleUtils.display("Available Commands:");
		general.summary();
		
		for (final Command baseCommand : Harmony.getInstance().getCommands()) {
			if (baseCommand != general) {
				baseCommand.summary();
			}
		}
	}
	
	@Override
	public final void summary() {
		ConsoleUtils.display("\n> General \n" 
				+ "\t" + HELP + "\t\t\t => Display this help message\n" 
				+ "\t" + LIST + "\t\t\t => List all commands");
	}

	@Override
	public final void execute(final String action, 
			final String[] args, 
			final String option) {
		if (action.equals(LIST)) {
			this.list();
		} else
			
		if (action.equals(HELP)) {
			this.help();
		}
	}

	@Override
	public final boolean isAvailableCommand(final String command) {
		return 	command.equals(LIST)
				|| command.equals(HELP);
	}

}
