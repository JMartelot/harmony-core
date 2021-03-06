/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.harmony.command;

import net.xeoh.plugins.base.Plugin;


/**
 * Interface defining the commmands class for all bundles.
 */
public interface Command extends Plugin {

	/**
	 * Execute specific action.
	 *
	 * @param action to realize
	 * @param args action arguments
	 * @param option of console
	 */
	void execute(String action, String[] args, String option);

	/**
	 * Display Bundle commands summary.
	 */
	void summary();

	/**
	 * Check availability of command.
	 *
	 * @param command to check
	 * @return true if found
	 */
	boolean isAvailableCommand(String command);
}
