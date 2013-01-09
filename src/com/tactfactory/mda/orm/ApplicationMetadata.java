/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.orm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.tactfactory.mda.Harmony;
import com.tactfactory.mda.plateforme.BaseAdapter;
import com.tactfactory.mda.template.TagConstant;

public class ApplicationMetadata implements BaseMetadata {
	/** Project name (demact) */
	public String projectName;
	
	/** Project NameSpace (com/tactfactory/mda/test/demact) */
	public String projectNameSpace;
	
	/** List of Entity of entity class */
	public LinkedHashMap<String, ClassMetadata> entities = new LinkedHashMap<String, ClassMetadata>();
	
	/** List of Entity of entity class */
	public LinkedHashMap<String, BaseMetadata> options = new LinkedHashMap<String, BaseMetadata>();
	
	/** List of string use in application */
	public TreeMap<String, TranslationMetadata> translates = new TreeMap<String, TranslationMetadata>();
	
	/**
	 * Transform the application to a map of strings and maps (for each field) given an adapter
	 * @param adapter The adapter used to customize the fields
	 * @return the map
	 */
	public HashMap<String, Object> toMap(BaseAdapter adapt){
		HashMap<String, Object> ret = new HashMap<String, Object>();
		HashMap<String, Object> entitiesMap = new HashMap<String, Object>();
		
		// Make Map for entities
		for(ClassMetadata cm : this.entities.values()){
			entitiesMap.put(cm.name, cm.toMap(adapt));
		}
		
		// Add root
		ret.put(TagConstant.PROJECT_NAME, 		this.projectName);
		ret.put(TagConstant.PROJECT_NAMESPACE, 	this.projectNameSpace.replaceAll("/", "\\."));

		ret.put(TagConstant.ENTITIES, 			entitiesMap);
		
		ret.put(TagConstant.ANDROID_SDK_DIR, Harmony.androidSdkPath);
		ret.put(TagConstant.ANT_ANDROID_SDK_DIR, new TagConstant.AndroidSDK("${sdk.dir}"));
		ret.put(TagConstant.OUT_CLASSES_ABS_DIR, "CLASSPATHDIR/");
		ret.put(TagConstant.OUT_DEX_INPUT_ABS_DIR, "DEXINPUTDIR/");
		
		// Add Extra bundle
		for(BaseMetadata bm : options.values()){
			ret.put(bm.getName(), bm.toMap(adapt));
		}
		
		return ret;
	}

	@Override
	public String getName() {
		return projectName;
	}
}
