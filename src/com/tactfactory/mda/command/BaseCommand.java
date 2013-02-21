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
import java.util.HashMap;

import com.tactfactory.mda.meta.ApplicationMetadata;
import com.tactfactory.mda.meta.ClassMetadata;
import com.tactfactory.mda.parser.BaseParser;
import com.tactfactory.mda.parser.ClassCompletor;
import com.tactfactory.mda.parser.JavaModelParser;
import com.tactfactory.mda.utils.ConsoleUtils;

/** 
 * Common Command structure 
 */
public abstract class BaseCommand implements Command {
	private ArrayList<BaseParser> registeredParsers 
			= new ArrayList<BaseParser>();
	
	protected static final String SEPARATOR = ":";
	
	protected HashMap<String, String> commandArgs;
	private JavaModelParser javaModelParser;
	
	/**
	 * Gets the Metadatas of all the entities actually in the package entity
	 * You can register your own bundle parsers 
	 * with the method this.javaModelParser.registerParser() 
	 */
	public void generateMetas() {
		ConsoleUtils.display(">> Analyse Models...");
		this.javaModelParser = new JavaModelParser();
		for (final BaseParser parser : this.registeredParsers) {
			this.javaModelParser.registerParser(parser);
		}
		// Parse models and load entities into CompilationUnits
		try {
			this.javaModelParser.loadEntities();
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		}

		// Convert CompilationUnits entities to ClassMetaData
		if (this.javaModelParser.getEntities().size() > 0) {
			for (final CompilationUnit mclass 
					: this.javaModelParser.getEntities()) {
				this.javaModelParser.parse(mclass);
			}
	
			// Generate views from MetaData 
			if (this.javaModelParser.getMetas().size() > 0) {				
				for (final ClassMetadata meta 
						: this.javaModelParser.getMetas()) {
					ApplicationMetadata.INSTANCE.entities.put(meta.name, meta);
				}
				new ClassCompletor(
						ApplicationMetadata.INSTANCE.entities).execute();
			}
		} else {
			ConsoleUtils.displayWarning("No entities found in entity package!");
		}
	}
	
	public final void registerParser(final BaseParser parser) {
		this.registeredParsers.add(parser);
	}
}
