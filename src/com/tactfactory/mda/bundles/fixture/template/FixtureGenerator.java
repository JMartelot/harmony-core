/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.bundles.fixture.template;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import com.tactfactory.mda.bundles.fixture.metadata.FixtureMetadata;
import com.tactfactory.mda.meta.ClassMetadata;
import com.tactfactory.mda.plateforme.BaseAdapter;
import com.tactfactory.mda.template.BaseGenerator;
import com.tactfactory.mda.template.SQLiteGenerator;
import com.tactfactory.mda.template.TagConstant;
import com.tactfactory.mda.utils.ConsoleUtils;
import com.tactfactory.mda.utils.FileUtils;

/**
 * Fixture bundle generator.
 */
public class FixtureGenerator extends BaseGenerator {

	/**
	 * Constructor.
	 * @param adapter The adapter to use.
	 * @throws Exception 
	 */
	public FixtureGenerator(final BaseAdapter adapter) throws Exception {
		super(adapter);
		this.setDatamodel(this.getAppMetas().toMap(this.getAdapter()));
	}
	
	/**
	 * Load the fixtures.
	 */
	public final void load() {
		final File fixtAppSrc = new File("fixtures/app");
		final File fixtTestSrc = new File("fixtures/test");
		if (fixtAppSrc.exists()) {
			final File fixtAppDest = 
					new File(this.getAdapter().getAssetsPath() + "/app");
			final File fixtTestDest = 
					new File(this.getAdapter().getAssetsPath() + "/test");
			if (!fixtAppDest.exists()) {
				fixtAppDest.mkdir();
			}
			if (!fixtTestDest.exists()) {
				fixtTestDest.mkdir();
			}
			try {
				final FileFilter ff = new FileFilter() {
					@Override
					public boolean accept(final File arg0) {
						return arg0.getPath().endsWith(".xml")
								|| arg0.getPath().endsWith(".yml"); 
					}
				};
				FileUtils.copyDirectory(fixtAppSrc, fixtAppDest, ff);
				ConsoleUtils.displayDebug(
						"Copying fixtures/app into " + fixtAppDest.getPath());
				FileUtils.copyDirectory(fixtTestSrc, fixtTestDest, ff);
				ConsoleUtils.displayDebug(
						"Copying fixtures/test into " + fixtTestDest.getPath());
			} catch (final IOException e) {
				ConsoleUtils.displayError(e);
			}
		} else {
			ConsoleUtils.displayError(new Exception(
					"You must init the fixtures before loading them."
					+ " Use the command orm:fixture:init."));
		}
	}
	
	/**
	 * Generate the loaders and the base fixtures.
	 */
	public final void init() {
		 try {
			 final String fixtureType = ((FixtureMetadata) 
							 this.getAppMetas().getOptions()
							 	.get("fixture")).getType();
			 
			 //Copy JDOM Library
			this.updateLibrary("jdom-2.0.2.jar");
			this.updateLibrary("snakeyaml-1.10-android.jar");
			
			//Create base classes for Fixtures loaders
			this.makeSource("FixtureBase.java", "FixtureBase.java", true);
			this.makeSource("DataManager.java", "DataManager.java", true);
			
			//Update SQLiteOpenHelper
			new SQLiteGenerator(this.getAdapter()).generateDatabase();
			
			//Create each entity's data loader
			for (final ClassMetadata cm 
					: this.getAppMetas().getEntities().values()) {
				if (cm.getFields().size() > 0) {
					this.getDatamodel().put(TagConstant.CURRENT_ENTITY,
							cm.getName());
					this.makeSource("TemplateDataLoader.java",
							cm.getName() + "DataLoader.java",
							true);
					this.makeBaseFixture("TemplateFixture." + fixtureType, 
							cm.getName() + "." + fixtureType, 
							false);
				}
			}
		} catch (final Exception e) {
			ConsoleUtils.displayError(e);
		}
	}
	
	/**
	 * Delete the existing fixtures.
	 */
	public final void purge() {
		for (final ClassMetadata cm 
				: this.getAppMetas().getEntities().values()) {
			this.removeSource(cm.getName() + ".xml");
			this.removeSource(cm.getName() + ".yml");
		}
		
	}
	
	@Override
	protected final void makeSource(final String templateName,
			final String fileName, 
			final boolean override) {
		final String fullFilePath = 
				this.getAdapter().getSourcePath() 
				+ this.getAppMetas().getProjectNameSpace()
				+ "/" + this.getAdapter().getFixture() + "/" 
				+ fileName;
		
		final String fullTemplatePath = 
				this.getAdapter().getTemplateSourceFixturePath().substring(1) 
				+ templateName;
		
		super.makeSource(fullTemplatePath, fullFilePath, override);
	}
	
	/**
	 * Delete file in assets directory.
	 * @param fileName The filename.
	 */
	protected final void removeSource(final String fileName) {
		final String fullFilePath = 
				this.getAdapter().getAssetsPath() + "/" + fileName;
		final File f = new File(fullFilePath);
		f.delete();
	}
	
	/**
	 * Make base fixture.
	 * @param templateName The template name.
	 * @param fileName The destination file name.
	 * @param override True for overwrite existing fixture.
	 */
	protected final void makeBaseFixture(final String templateName,
			final String fileName, 
			final boolean override) {
		String fullFilePath = "fixtures/app/" + fileName;
		String fullTemplatePath = 
				this.getAdapter().getTemplateSourceFixturePath().substring(1) 
				+ templateName;
		super.makeSource(fullTemplatePath, fullFilePath, override);
		
		fullFilePath = "fixtures/test/" + fileName;
		fullTemplatePath =
				this.getAdapter().getTemplateSourceFixturePath().substring(1)
				+ templateName;
		super.makeSource(fullTemplatePath, fullFilePath, override);
	}
}
