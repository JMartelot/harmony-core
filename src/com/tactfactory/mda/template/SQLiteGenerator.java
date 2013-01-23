/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.template;

import java.io.IOException;

import com.google.common.base.CaseFormat;
import com.tactfactory.mda.ConsoleUtils;
import com.tactfactory.mda.plateforme.BaseAdapter;
import com.tactfactory.mda.utils.PackageUtils;

public class SQLiteGenerator extends BaseGenerator {
	protected String localNameSpace;

	public SQLiteGenerator(BaseAdapter adapter) throws Exception {
		super(adapter);
		
		this.datamodel = this.appMetas.toMap(this.adapter);
		this.localNameSpace = this.appMetas.projectNameSpace+"/"+this.adapter.getData();
	}

	/**
	 * Generate Database Interface Source Code
	 */
	public void generateDatabase() {
		// Info
		ConsoleUtils.display(">> Generate Database");
		
		try {			
			this.makeSourceData(
					"TemplateSQLiteOpenHelper.java", 
					"%sSQLiteOpenHelper.java",
					false);
			
			this.makeSourceData(
					"base/TemplateSQLiteOpenHelperBase.java", 
					"base/%sSQLiteOpenHelperBase.java",
					true);
			
			this.makeSourceData(
					"base/ApplicationSQLiteAdapterBase.java", 
					"base/SQLiteAdapterBase.java",
					true);
			
		} catch (Exception e) {
			ConsoleUtils.displayError(e.getMessage());
		}
	}
	
	/** Make Java Source Code
	 * @param cfg Template engine
	 * @param template Template path file.
	 * @param filename
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void makeSourceData(String template, String filename, boolean override) {
		
		String fullFilePath = String.format("%s%s/%s",
						this.adapter.getSourcePath(),
						PackageUtils.extractPath(this.localNameSpace).toLowerCase(),
						String.format(filename, CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, this.appMetas.name)));
		
		String fullTemplatePath = this.adapter.getTemplateSourceProviderPath().substring(1) + template;
		
		super.makeSource(fullTemplatePath, fullFilePath, override);
	}
}
