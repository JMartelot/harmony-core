/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.tactfactory.mda.ConsoleUtils;
import com.tactfactory.mda.Harmony;
import com.tactfactory.mda.orm.ClassMetadata;
import com.tactfactory.mda.orm.FieldMetadata;
import com.tactfactory.mda.orm.SqliteAdapter;
import com.tactfactory.mda.plateforme.BaseAdapter;
import com.tactfactory.mda.utils.FileUtils;
import com.tactfactory.mda.utils.PackageUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ActivityGenerator {	
	protected List<ClassMetadata> metas;
	//protected ClassMetadata meta;
	protected List<Map<String, Object>> modelEntities;
	protected BaseAdapter adapter;
	protected String localNameSpace;
	protected boolean isWritable = true;
	protected HashMap<String, Object> datamodel = new HashMap<String, Object>();

	public ActivityGenerator(List<ClassMetadata> metas, BaseAdapter adapter) throws Exception {
		if (metas == null && adapter == null)
			throw new Exception("No meta or adapter define.");

		this.metas 		= metas;
		this.adapter	= adapter;
		
		// Make entities
		this.modelEntities = new ArrayList<Map<String, Object>>();
		for (ClassMetadata meta : this.metas) {
			if(!meta.fields.isEmpty()){
				//this.meta = meta;
				
				Map<String, Object> modelClass = meta.toMap(this.adapter);
				
				this.modelEntities.add(modelClass);
			}
		}
	}

	public ActivityGenerator(List<ClassMetadata> metas, BaseAdapter adapter, Boolean isWritable) throws Exception {
		this(metas, adapter);

		this.isWritable = isWritable;
	}
	
	public void generateAll() {
		ConsoleUtils.display(">> Generate CRUD view...");

		int i = 0;
		for(Map<String, Object> entity : this.modelEntities) {
			//this.meta = this.metas.get(i);
			//this.localNameSpace = this.adapter.getNameSpaceEntity(this.meta, this.adapter.getController());;
			this.localNameSpace = Harmony.projectNameSpace.replace('/', '.') +"."+ this.adapter.getController()+"."+((String)entity.get(TagConstant.NAME)).toLowerCase();

			// Make class
			this.datamodel.put("namespace", 					entity.get(TagConstant.SPACE));
			this.datamodel.put(TagConstant.NAME, 				entity.get(TagConstant.NAME));
			this.datamodel.put("localnamespace", 				this.localNameSpace);
			this.datamodel.put(TagConstant.FIELDS, 				entity.get(TagConstant.FIELDS));
			this.datamodel.put(TagConstant.RELATIONS, 			entity.get(TagConstant.RELATIONS));
			
			this.generateAllAction((String)entity.get(TagConstant.NAME));
			
			i++;
		}
	}
	
	/** List Action
	 * @param cfg
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateListAction(Configuration cfg, String entityName) throws IOException,
	TemplateException {
		ArrayList<String> javas = new ArrayList<String>();
		javas.add("%sListActivity.java");
		javas.add("%sListFragment.java");
		javas.add("%sListAdapter.java");
		javas.add("%sListLoader.java");
		
		ArrayList<String> xmls = new ArrayList<String>();
		xmls.add("activity_%s_list.xml");
		xmls.add("fragment_%s_list.xml");		
		xmls.add("row_%s.xml");
		

		for(String java : javas){
			this.makeSourceControler(cfg, 
					String.format(java, "Template"),
					String.format(java, entityName));
		}
		
		for(String xml : xmls){
			this.makeResourceLayout(cfg, 
					String.format(xml, "template"),
					String.format(xml, entityName.toLowerCase()));
		}

		this.updateManifest("ListActivity", entityName);
	}

	/** Show Action
	 * @param cfg
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateShowAction(Configuration cfg, String entityName) throws IOException,
	TemplateException {

		ArrayList<String> javas = new ArrayList<String>();
		javas.add("%sShowActivity.java");
		javas.add("%sShowFragment.java");
		
		ArrayList<String> xmls = new ArrayList<String>();
		xmls.add("activity_%s_show.xml");
		xmls.add("fragment_%s_show.xml");
		

		for(String java : javas){
			this.makeSourceControler(cfg, 
					String.format(java, "Template"),
					String.format(java, entityName));
		}
		
		for(String xml : xmls){
			this.makeResourceLayout(cfg, 
					String.format(xml, "template"),
					String.format(xml, entityName.toLowerCase()));
		}

		this.updateManifest("ShowActivity", entityName);
	}

	/** Edit Action
	 * @param cfg
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateEditAction(Configuration cfg, String entityName) throws IOException,
	TemplateException {
		
		ArrayList<String> javas = new ArrayList<String>();
		javas.add("%sEditActivity.java");
		javas.add("%sEditFragment.java");
		
		ArrayList<String> xmls = new ArrayList<String>();
		xmls.add("activity_%s_edit.xml");
		xmls.add("fragment_%s_edit.xml");
		

		for(String java : javas){
			this.makeSourceControler(cfg, 
					String.format(java, "Template"),
					String.format(java, entityName));
		}
		
		for(String xml : xmls){
			this.makeResourceLayout(cfg, 
					String.format(xml, "template"),
					String.format(xml, entityName.toLowerCase()));
		}

		this.updateManifest("EditActivity", entityName);
	}

	/** Create Action
	 * @param cfg
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void generateCreateAction(Configuration cfg, String entityName) throws IOException,
	TemplateException {
		
		ArrayList<String> javas = new ArrayList<String>();
		javas.add("%sCreateActivity.java");
		javas.add("%sCreateFragment.java");
		
		ArrayList<String> xmls = new ArrayList<String>();
		xmls.add("activity_%s_create.xml");
		xmls.add("fragment_%s_create.xml");
		

		for(String java : javas){
			this.makeSourceControler(cfg, 
					String.format(java, "Template"),
					String.format(java, entityName));
		}
		
		for(String xml : xmls){
			this.makeResourceLayout(cfg, 
					String.format(xml, "template"),
					String.format(xml, entityName.toLowerCase()));
		}


		this.updateManifest("CreateActivity", entityName);
	}

	/** All Actions (List, Show, Edit, Create) */
	public void generateAllAction(String entityName) {
		// Info
		ConsoleUtils.display(">>> Generate CRUD view for " +  entityName);

		try {

			// TODO Caution, freemarker template folder must have been specified
			// freemarker bug with '../' folder so, just remove first '.'
			Configuration cfg = new Configuration();						// Initialization of template engine
			cfg.setDirectoryForTemplateLoading(new File(Harmony.pathBase));

			if (this.isWritable ) {
				// Info
				ConsoleUtils.display("   with write actions");

				this.generateCreateAction(cfg, entityName);
				this.generateEditAction(cfg, entityName);
			}

			this.generateShowAction(cfg, entityName);
			this.generateListAction(cfg, entityName);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** Make Java Source Code
	 * @param cfg Template engine
	 * @param template Template path file. <br/>For list activity is "TemplateListActivity.java"
	 * @param filename
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void makeSourceControler(Configuration cfg, String template, String filename) 
			throws IOException, TemplateException {
		String filepath = String.format("%s%s/%s",
						this.adapter.getSourcePath(),
						PackageUtils.extractPath(this.localNameSpace).toLowerCase(),
						filename);
		if(!FileUtils.exists(filepath)){
			File file = FileUtils.makeFile(filepath);
			
			// Debug Log
			ConsoleUtils.displayDebug("Generate Source : " + file.getPath()); 
	
			// Create
			Template tpl = cfg.getTemplate(
					String.format("%s%s",
							this.adapter.getTemplateSourceControlerPath(),
							template));		// Load template file in engine
	
			OutputStreamWriter output = new FileWriter(file);
			tpl.process(datamodel, output);		// Process datamodel (with previous template file), and output to output file
			output.flush();
			output.close();
		}
	}

	/** Make Resource file
	 * 
	 * @param cfg Template engine
	 * @param template Template path file.
	 * @param filename Resource file. <br/>prefix is type of view "row_" or "activity_" or "fragment_" with <br/>postfix is type of action and extension file : "_list.xml" or "_edit.xml".
	 * @throws IOException
	 * @throws TemplateException
	 */
	private void makeResourceLayout(Configuration cfg, String template, String filename) throws IOException,
	TemplateException {
		String filepath = String.format("%s/%s", 
									this.adapter.getRessourceLayoutPath(),
									filename);
		if(!FileUtils.exists(filepath)){
			File file = FileUtils.makeFile(filepath);
	
			// Debug Log
			ConsoleUtils.displayDebug("Generate Ressource : " + file.getAbsoluteFile()); 
	
			// Create
			Template tpl = cfg.getTemplate(
					String.format("%s/%s",
							this.adapter.getTemplateRessourceLayoutPath().substring(1),
							template));
	
			OutputStreamWriter output = new FileWriter(file);
			tpl.process(this.datamodel, output);
			output.flush();
			output.close();
		}
	}

	/** Make Manifest file
	 * 
	 * @param cfg Template engine
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	private void makeManifest(Configuration cfg) throws IOException, TemplateException {
		File file = FileUtils.makeFile(this.adapter.getManifestPathFile());

		// Debug Log
		ConsoleUtils.displayDebug("Generate Manifest : " + file.getAbsoluteFile());

		// Create
		Template tpl = cfg.getTemplate(this.adapter.getTemplateManifestPathFile());
		OutputStreamWriter output = new FileWriter(file);
		tpl.process(datamodel, output);
		output.flush();
		output.close();
	}

	/**  Update Android Manifest
	 * 
	 * @param classFile
	 */
	private void updateManifest(String classFile, String entityName) {
		classFile = entityName + classFile;
		String pathRelatif = String.format(".%s.%s.%s",
				this.adapter.getController(), 
				entityName.toLowerCase(), 
				classFile );

		// Debug Log
		ConsoleUtils.displayDebug("Update Manifest : " + pathRelatif);

		try {
			SAXBuilder builder = new SAXBuilder();		// Make engine
			File xmlFile = FileUtils.makeFile(this.adapter.getManifestPathFile());
			Document doc = (Document) builder.build(xmlFile); 	// Load XML File
			Element rootNode = doc.getRootElement(); 			// Load Root element
			Namespace ns = rootNode.getNamespace("android");	// Load Name space (required for manipulate attributes)

			// Find Application Node
			Element findActivity = null;
			Element applicationNode = rootNode.getChild("application"); 	// Find a element
			if (applicationNode != null) {

				// Find Activity Node
				List<Element> activities = applicationNode.getChildren("activity"); 	// Find many elements
				for (Element activity : activities) {
					if (activity.hasAttributes() && activity.getAttributeValue("name",ns).equals(pathRelatif) ) {	// Load attribute value
						findActivity = activity;
						break;
					}
				}

				// If not found Node, create it
				if (findActivity == null) {
					findActivity = new Element("activity");				// Create new element
					findActivity.setAttribute("name", pathRelatif, ns);	// Add Attributes to element
					Element findFilter = new Element("intent-filter");
					Element findAction = new Element("action");
					Element findCategory = new Element("category");
					Element findData = new Element("data");

					findFilter.addContent(findAction);					// Add Child element
					findFilter.addContent(findCategory);
					findFilter.addContent(findData);
					findActivity.addContent(findFilter);
					applicationNode.addContent(findActivity);
				}

				// Set values
				findActivity.setAttribute("label", "@string/app_name", ns);
				Element filterActivity = findActivity.getChild("intent-filter");
				if (filterActivity != null) {
					String data = "";
					String action = "VIEW";

					if (pathRelatif.matches(".*List.*")) {
						data = "vnd.android.cursor.collection/";
					} else {
						data = "vnd.android.cursor.item/";

						if (pathRelatif.matches(".*Edit.*"))
							action = "EDIT";
						else 		

							if (pathRelatif.matches(".*Create.*"))
								action = "INSERT";
					}

					
					data += Harmony.projectNameSpace + adapter.getModel() + entityName;
					filterActivity.getChild("action").setAttribute("name", "android.intent.action."+ action, ns);
					filterActivity.getChild("category").setAttribute("name", "android.intent.category.DEFAULT", ns);
					filterActivity.getChild("data").setAttribute("mimeType", data, ns);
				}	
			}

			// Write to File
			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());				// Make beautiful file with indent !!!
			xmlOutput.output(doc, new FileWriter(xmlFile.getAbsoluteFile()));
		} catch (IOException io) {
			io.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}
}
