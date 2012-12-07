package com.tactfactory.mda.orm;

import java.util.ArrayList;

import com.tactfactory.mda.plateforme.BaseAdapter;

public class MethodMetadata {
	/** Field name */
	public String name;
	
	/** Field type */
	public String type;
	
	/** Arguments types*/
	public ArrayList<String> argumentsTypes = new ArrayList<String>();
}