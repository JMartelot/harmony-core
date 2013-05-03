package ${local_namespace}.base;

import ${local_namespace}.*;
import ${project_namespace}.R;
import ${project_namespace}.${project_name?cap_first}Application;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
/**
 * ${project_name?cap_first}ProviderBase.
 */
public class ${project_name?cap_first}ProviderBase extends ContentProvider {
	protected static String TAG = "${project_name?cap_first}Provider";
	protected String URI_NOT_SUPPORTED;
	
	/** Tools / Common.
	 * 
	 */

	public    static Integer baseVersion = 0;
	public    static String baseName = "";
	protected static String item;
	protected static String authority = "${project_namespace}.provider";
	protected static UriMatcher uriMatcher = 
			new UriMatcher(UriMatcher.NO_MATCH);
	
	/** Adapter to SQLite
	 * 
	 */
	<#list entities?values as entity>
		<#if (entity.fields?size>0) >
	protected ${entity.name?cap_first}ProviderAdapter ${entity.name?uncap_first}Provider;
		</#if>
	</#list>
	protected SQLiteDatabase db;
	
	protected Context mContext;

	@Override
	public boolean onCreate() {
		boolean result = true;
		
		this.mContext = getContext();
		URI_NOT_SUPPORTED = this.getContext().getString(
				R.string.uri_not_supported);
		
		try {
		<#assign firstGo = true />
		<#list entities?values as entity>
			<#if (entity.fields?size>0) >
				<#if (firstGo)>
			this.${entity.name?uncap_first}Provider = 
			new ${entity.name?cap_first}ProviderAdapter(this.mContext);
			this.db = this.${entity.name?uncap_first}Provider.getDb();
					<#assign firstGo = false />
				<#else>
			this.${entity.name?uncap_first}Provider = 
			new ${entity.name?cap_first}ProviderAdapter(
					this.mContext, 
					this.db);
				</#if>
			</#if>
		</#list>
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Get the entity from the URI.
	 * @return A String representing the entity name 
	 */
	@Override
	public String getType(final Uri uri) {
		String result = null;
		final String single = 
				"vnc.android.cursor.item/" + authority + ".";
		final String collection = 
				"vnc.android.cursor.collection/" + authority + ".";
		
		switch (uriMatcher.match(uri)) {
		<#list entities?values as entity>
			<#if (entity.fields?size>0) >
		
		// ${entity.name} type mapping
		case ${entity.name?cap_first}ProviderAdapter
									.${entity.name?upper_case}_ONE:
			result = single + "${entity.name?lower_case}";
			break;
		case ${entity.name?cap_first}ProviderAdapter
									.${entity.name?upper_case}_ALL:
			result = collection + "${entity.name?lower_case}";
			break;
			</#if>
		</#list>
		
		default:
			throw new IllegalArgumentException(URI_NOT_SUPPORTED + uri);
		}
		
		return result;
	}
	
	/**
	 * Deletes matching tokens with the given URI.
	 * @return The number of tokens deleted
	 */
	@Override
	public int delete(final Uri uri, final String selection, 
			final String[] selectionArgs) {
		int result = 0;
		this.db.beginTransaction();
		try {
			switch (uriMatcher.match(uri)) {
		<#list entities?values as entity>
			<#if (entity.fields?size>0) >
		
			// ${entity.name}
			case ${entity.name?cap_first}ProviderAdapter
								.${entity.name?upper_case}_ONE:
				try {
					result = this.${entity.name?uncap_first}Provider.delete(uri, 
							selection, 
							selectionArgs);
				} catch (Exception e) {
					throw new IllegalArgumentException(URI_NOT_SUPPORTED + uri);
				}
				break;
			case ${entity.name?cap_first}ProviderAdapter
								.${entity.name?upper_case}_ALL:
				result = this.${entity.name?uncap_first}Provider.delete(uri, 
							selection, 
							selectionArgs);
				break;
			</#if>
		</#list>
		
			default:
				throw new IllegalArgumentException(URI_NOT_SUPPORTED + uri);
			}

			this.db.setTransactionSuccessful();
		} finally {
			this.db.endTransaction();
		}
		
		if (result > 0) {
			this.getContext().getContentResolver().notifyChange(uri, null);
		}
		return result;
	}
	
	/**
	 * Insert ContentValues with the given URI.
	 * @return The URI to the inserted ContentValue
	 */
	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		Uri result = null;

		this.db.beginTransaction();
		try {

			switch (uriMatcher.match(uri)) {
		<#list entities?values as entity>
			<#if (entity.fields?size>0) >
		
			// ${entity.name}
			case ${entity.name?cap_first}ProviderAdapter
									.${entity.name?upper_case}_ALL:
				result = this.${entity.name?uncap_first}Provider.insert(uri, 
						values);
				break;
			</#if>
		</#list>
		
			default:
				throw new IllegalArgumentException(URI_NOT_SUPPORTED + uri);
			}
			this.db.setTransactionSuccessful();
		} finally {
			this.db.endTransaction();
		}
		if (result != null) {
			this.getContext().getContentResolver().notifyChange(result, null);
		}

		return result;
	}
	
	/**
	 * Query the table given by the uri parameter.
	 * @return A Cursor pointing to the result of the query
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor result = null;

		this.db.beginTransaction();
		try {
		
			switch (uriMatcher.match(uri)) {
		<#list entities?values as entity>
			<#if (entity.fields?size>0) >
		
			// ${entity.name}
			case ${entity.name?cap_first}ProviderAdapter
							.${entity.name?upper_case}_ONE:
				result = this.${entity.name?uncap_first}Provider.query(uri, 
					projection, 
					selection, 
					selectionArgs, 
					sortOrder);
				break;
			case ${entity.name?cap_first}ProviderAdapter
							.${entity.name?upper_case}_ALL:
				result = this.${entity.name?uncap_first}Provider.query(uri, 
					projection, 
					selection, 
					selectionArgs, 
					sortOrder);
				break;
			</#if>
		</#list>
		
			default:
				throw new IllegalArgumentException(URI_NOT_SUPPORTED + uri);
			}

			this.db.setTransactionSuccessful();
		} finally {
			this.db.endTransaction();
		}

		return result;
	}
	
	/**
	 * Update the given URI with the new ContentValues.
	 * @return The number of token updated
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int result = 0;
		
		this.db.beginTransaction();
		try {

			switch (uriMatcher.match(uri)) {
		<#list entities?values as entity>
			<#if (entity.fields?size>0) >
		
			// ${entity.name}
			case ${entity.name?cap_first}ProviderAdapter
							.${entity.name?upper_case}_ONE:
				result = this.${entity.name?uncap_first}Provider.update(uri,
					values,
					selection,
					selectionArgs);
				break;
			case ${entity.name?cap_first}ProviderAdapter
							.${entity.name?upper_case}_ALL:
				result = this.${entity.name?uncap_first}Provider.update(uri,
					values,
					selection,
					selectionArgs);
				break;
			</#if>
		</#list>
		
			default:
				throw new IllegalArgumentException(URI_NOT_SUPPORTED + uri);
			}
			this.db.setTransactionSuccessful();
		} finally {
			this.db.endTransaction();
		}
		
		if (result > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return result;
	}
	
	//-------------------------------------------------------------------------
	
	/** Utils function.
	 * 
	 */	
	public static final Uri generateUri(String typePath) {
		return Uri.parse("content://" + authority + "/" + typePath);
	}
	
	/** Utils function.
	 * 
	 */
	public static final Uri generateUri() {
		return Uri.parse("content://" + authority);
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#call<br />
	 * (java.lang.String, java.lang.String, android.os.Bundle)
	 */
	@Override
	public Bundle call(String method, String arg, Bundle extras) {
		<#list entities?values as entity>			
			<#if (entity.fields?size>0) >
		if (method.equals(${entity.name?cap_first}ProviderAdapter
				.METHOD_INSERT_${entity.name?upper_case})) {
			return this.${entity.name?uncap_first}Provider.insert(arg, extras);
		}
			
		else if (method.equals(${entity.name?cap_first}ProviderAdapter
				.METHOD_DELETE_${entity.name?upper_case})) {
			return this.${entity.name?uncap_first}Provider.delete(arg, extras);
		}
		else if (method.equals(${entity.name?cap_first}ProviderAdapter
				.METHOD_UPDATE_${entity.name?upper_case})) {
			return this.${entity.name?uncap_first}Provider.update(arg, extras);
		}
		
		else if (method.equals(${entity.name?cap_first}ProviderAdapter
				.METHOD_QUERY_${entity.name?upper_case})) {
			if (extras.containsKey("id")) {
				return this.${entity.name?uncap_first}Provider.query(arg, 
						extras);
			} else {
				return this.${entity.name?uncap_first}Provider.queryAll(arg, 
						extras);	
			}
		} else
		
			</#if>
		</#list>
		{
			return super.call(method, arg, extras);
		}
	}

	public static UriMatcher getUriMatcher() {
		return uriMatcher;
	}
}
