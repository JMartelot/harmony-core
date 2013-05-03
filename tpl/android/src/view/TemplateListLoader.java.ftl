<#assign curr = entities[current_entity] />
package ${curr.controller_namespace};

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import ${curr.namespace}.entity.${curr.name};
import ${project_namespace}.provider.${curr.name?cap_first}ProviderAdapter;
import ${project_namespace}.criterias.${curr.name?cap_first}Criterias;

/**
 * ${curr.name} Loader.
 */
public class ${curr.name}ListLoader extends AsyncTaskLoader<List<${curr.name}>> {
	
	private ${curr.name?cap_first}Criterias criterias;
	private List<${curr.name}> m${curr.name}s;
	private Context context;

	/**
	 * Constructor.
	 */
	public ${curr.name}ListLoader(final Context context, final ${curr.name?cap_first}Criterias crit) {
		super(context);
		this.context = context;
		this.criterias = crit;
	}

	/**
	 * This is where the bulk of our work is done.  This function is
	 * called in a background thread and should generate a new set of
	 * data to be published by the loader.
	 */
	@Override 
	public List<${curr.name}> loadInBackground() {
		List<${curr.name}> result = new ArrayList<${curr.name}>();

		// TODO Query of data
		ContentResolver prov = this.getContext().getContentResolver();
		Bundle b = new Bundle();
		b.putSerializable("crits", this.criterias);
		Bundle provResult = prov.call(${curr.name}ProviderAdapter.${curr.name?upper_case}_URI,
				${curr.name}ProviderAdapter.METHOD_QUERY_${curr.name?upper_case},
				null,
				b);
		
		result = (List<${curr.name}>) provResult.getSerializable(${curr.name}ProviderAdapter.ITEM_KEY);
		
		return result;
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override 
	public void deliverResult(final List<${curr.name}> items) {
		if (this.isReset() && items != null) {
			// An async query came in while the loader is stopped.  We
			// don't need the result.
			
			this.onReleaseResources(items);
		}
		final List<${curr.name}> oldItems = items;
		this.m${curr.name}s = items;

		if (this.isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(items);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldItems != null) {
			this.onReleaseResources(oldItems);
		}
	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override 
	protected void onStartLoading() {
		if (this.m${curr.name}s != null) {
			// If we currently have a result available, deliver it
			// immediately.
			this.deliverResult(this.m${curr.name}s);
		}

		if (this.takeContentChanged() || this.m${curr.name}s == null) {
			// If the data has changed since the last time it was loaded
			// or is not currently available, start a load.
			this.forceLoad();
		}
	}

	/**
	 * Handles a request to stop the Loader.
	 */
	@Override 
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		this.cancelLoad();
	}

	/**
	 * Handles a request to cancel a load.
	 */
	@Override 
	public void onCanceled(List<${curr.name}> items) {
		super.onCanceled(items);

		this.onReleaseResources(items);
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override 
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		this.onStopLoading();

		// At this point we can release the resources associated with '${curr.name}'
		// if needed.
		if (this.m${curr.name}s != null) {
			this.onReleaseResources(this.m${curr.name}s);
			this.m${curr.name}s = null;
		}
	}

	/**
	 * Helper function to take care of releasing resources associated
	 * with an actively loaded data set.
	 */
	protected void onReleaseResources(List<${curr.name}> messages) {
		// For a simple List<> there is nothing to do.  For something
		// like a Cursor, we would close it here.
	}
}
