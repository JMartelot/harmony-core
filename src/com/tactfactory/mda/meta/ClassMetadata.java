/**
 * This file is part of the Harmony package.
 *
 * (c) Mickael Gaillard <mickael.gaillard@tactfactory.com>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package com.tactfactory.mda.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.tactfactory.mda.meta.TranslationMetadata.Group;
import com.tactfactory.mda.plateforme.BaseAdapter;
import com.tactfactory.mda.template.TagConstant;

/** Entity class metadata. */
public class ClassMetadata extends BaseMetadata {

	/** Used for join tables (ManyToMany relations). */
	private boolean internal = false;
	
	/** Namespace of entity class. */
	private String space = "";

	/** List of fields of entity class. */
	private Map<String, FieldMetadata> fields = 
			new LinkedHashMap<String, FieldMetadata>();

	/** List of ids of entity class. */
	private Map<String, FieldMetadata> ids = 
			new LinkedHashMap<String, FieldMetadata>();
	
	/** List of relations of entity class. */
	private Map<String, FieldMetadata> relations =
			new LinkedHashMap<String, FieldMetadata>();
		
	/** Class inherited by the entity class or null if none. */
	private String extendType;
	
	/** Implemented class list of the entity class. */
	private List<String> implementTypes = 
			new ArrayList<String>();
	
	/** Implemented class list of the entity class. */
	private List<MethodMetadata> methods = 
			new ArrayList<MethodMetadata>();

	/** Imports of the class. */
	private List<String> imports = new ArrayList<String>();
	
	/** 
	 * Add Component String of field. 
	 * @param componentName Component name
	 */
	public final void makeString(final String componentName) {
		final String key = this.getName().toLowerCase() 
				+ "_" + componentName.toLowerCase(Locale.ENGLISH);
		TranslationMetadata.addDefaultTranslation(
				key, this.getName(), Group.MODEL);
	}

	
	/**
	 * Transform the class to a map given an adapter.
	 * @param adapter The adapter used to customize the fields
	 * @return the map
	 */
	@Override
	public final Map<String, Object> toMap(final BaseAdapter adapter) {
		final Map<String, Object> model = new HashMap<String, Object>();
		
		model.put(TagConstant.SPACE,			this.space);
		//model.put(TagConstant.PROJECT_NAME,		Harmony.metas.name);
		model.put(TagConstant.NAME,				this.getName());
		model.put(TagConstant.EXTENDS,			this.extendType);
		model.put(TagConstant.CONTROLLER_NAMESPACE, 
				adapter.getNameSpaceEntity(this, adapter.getController()));
		model.put(TagConstant.DATA_NAMESPACE, 	
				adapter.getNameSpace(this, adapter.getData()));
		model.put(TagConstant.TEST_NAMESPACE, 	
				adapter.getNameSpace(this, adapter.getTest()));
		model.put(TagConstant.FIELDS,			
				this.toFieldArray(this.fields.values(), adapter));
		model.put(TagConstant.IDS,				
				this.toFieldArray(this.ids.values(), adapter));
		model.put(TagConstant.RELATIONS,		
				this.toFieldArray(this.relations.values(), adapter));
		model.put(TagConstant.INTERNAL,			"false");
		
		if (this.internal) {
			model.put(TagConstant.INTERNAL,		"true");
		}
		
		final Map<String, Object> optionsModel = new HashMap<String, Object>();
		for (final Metadata option : this.getOptions().values()) {
			optionsModel.put(option.getName(), option.toMap(adapter));
		}
		model.put(TagConstant.OPTIONS, optionsModel);
		
		return model;
	}
	
	/**
	 * @return the internal
	 */
	public final boolean isInternal() {
		return internal;
	}


	/**
	 * @param internal the internal to set
	 */
	public final void setInternal(final boolean internal) {
		this.internal = internal;
	}


	/**
	 * @return the space
	 */
	public final String getSpace() {
		return space;
	}


	/**
	 * @param space the space to set
	 */
	public final void setSpace(final String space) {
		this.space = space;
	}


	/**
	 * @return the fields
	 */
	public final Map<String, FieldMetadata> getFields() {
		return fields;
	}


	/**
	 * @param fields the fields to set
	 */
	public final void setFields(final Map<String, FieldMetadata> fields) {
		this.fields = fields;
	}


	/**
	 * @return the ids
	 */
	public final Map<String, FieldMetadata> getIds() {
		return ids;
	}


	/**
	 * @param ids the ids to set
	 */
	public final void setIds(final Map<String, FieldMetadata> ids) {
		this.ids = ids;
	}


	/**
	 * @return the relations
	 */
	public final Map<String, FieldMetadata> getRelations() {
		return relations;
	}


	/**
	 * @param relations the relations to set
	 */
	public final void setRelations(final Map<String, FieldMetadata> relations) {
		this.relations = relations;
	}


	/**
	 * @return the extendType
	 */
	public final String getExtendType() {
		return extendType;
	}


	/**
	 * @param extendType the extendType to set
	 */
	public final void setExtendType(final String extendType) {
		this.extendType = extendType;
	}


	/**
	 * @return the implementTypes
	 */
	public final List<String> getImplementTypes() {
		return implementTypes;
	}


	/**
	 * @param implementTypes the implementTypes to set
	 */
	public final void setImplementTypes(final List<String> implementTypes) {
		this.implementTypes = implementTypes;
	}


	/**
	 * @return the methods
	 */
	public final List<MethodMetadata> getMethods() {
		return methods;
	}


	/**
	 * @param methods the methods to set
	 */
	public final void setMethods(final List<MethodMetadata> methods) {
		this.methods = methods;
	}


	/**
	 * @return the imports
	 */
	public final List<String> getImports() {
		return imports;
	}


	/**
	 * @param imports the imports to set
	 */
	public final void setImports(final List<String> imports) {
		this.imports = imports;
	}


	/**
	 * Build a map from a collection of fields.
	 * @param c The collection of fields
	 * @param adapter The adapter to use.
	 * @return The fields map.
	 */
	private List<Map<String, Object>> toFieldArray(
			final Collection<FieldMetadata> c, 
			final BaseAdapter adapter) {
		final List<Map<String, Object>> result = 
				new ArrayList<Map<String, Object>>();
		Map<String, Object> subField = null;
		
		for (final FieldMetadata field : c) {
			//field.customize(adapter);
			
			subField = field.toMap(adapter);
			
			// Add field translate
			if (!field.isInternal() && !field.isHidden()) {
				field.makeString("label");
			}
			
			result.add(subField);
		}
		
		return result;
	}	
}
