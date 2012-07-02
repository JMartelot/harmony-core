package ${localnamespace};

import ${namespace}.R;

import android.os.Bundle;
import android.os.AsyncTask;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.*;

import java.util.Date;

import ${namespace}.entity.${name};

/** ${name} create fragment
 * 
 * @see android.app.Fragment
 */
public class ${name}CreateFragment extends Fragment {
	/* Model data */
	protected ${name} model;
	
	/* Fields View */
    <#list fields as field>
    protected ${field.customEditType} ${field.name}View; 
    </#list>
    
    /** Initialize view of fields 
     * 
     * @param view The layout inflating
     */
    protected void initializeComponent(View view) {
		<#foreach field in fields>
		this.${field.name}View = (${field.customEditType}) view.findViewById(R.id.${name?lower_case}_${field.name?lower_case}); 
		</#foreach>
    }
    
    /** Load data from model to fields view */
    public void loadData() {
    	<#foreach field in fields>
    		<#if (field.customEditType == "EditText") >
    			<#if (field.type == "String")>
		this.${field.name}View.setText(this.model.get${field.name?cap_first}()); 
				</#if>
				<#if (field.type == "Date")>
		this.${field.name}View.setText(this.model.get${field.name?cap_first}().toLocaleString()); 
				</#if>
				<#if (field.type == "int")>
		this.${field.name}View.setText(String.valueOf(this.model.get${field.name?cap_first}())); 
				</#if>
			</#if>
			<#if (field.customEditType == "CheckBox") >
		this.${field.name}View.setSelected(this.model.${field.name?uncap_first}()); 
			</#if>
		</#foreach>
    }
    
    /** Save data from fields view to model */
    public void saveData() {
    	<#foreach field in fields>
			<#if (field.customEditType == "EditText") >
				<#if (field.type == "String")>
		this.model.set${field.name?cap_first}(this.${field.name}View.getEditableText().toString());
				</#if>
				<#if (field.type == "Date")>
		this.model.set${field.name?cap_first}(new Date(this.${field.name}View.getEditableText().toString())); 
				</#if>
				<#if (field.type == "int")>
		this.model.set${field.name?cap_first}(Integer.parseInt(this.${field.name}View.getEditableText().toString()));
				</#if>
			</#if>
			<#if (field.customEditType == "CheckBox") >
		this.model.${field.name?uncap_first}(this.${field.name}View.isChecked());
			</#if>
		</#foreach>
    }
    
    /** Check data is valid
     * 
     * @return true if valid
     */
    public boolean validateData() {
    	return true;
    }

    /** Sets up the UI.
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {    	
    	// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_${name?lower_case}_create, container, false);
        
        this.initializeComponent(view);
        
        return view;
    }
    
    public static class CreateTask extends AsyncTask<Void, Void, Integer> {
		protected final Context context;
		protected final ${name}CreateFragment fragment;
		protected final ${name} entity;
		protected String errorMsg;
		protected ProgressDialog progress;
		
		public CreateTask(${name}CreateFragment fragment, ${name} entity) {
			this.fragment = fragment;
			this.context = fragment.getActivity();
			this.entity = entity;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			this.progress = ProgressDialog.show(context, "", ""); //this.context.getString(R.string.${name?lower_case}_progress_save));
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Integer doInBackground(Void... params) {
			Integer result = -1;
			
			try {
				// TODO to Implement
				// REST call
				//StootieWebServiceImpl service = new StootieWebServiceImpl(this.context);
				//result = service.createStoot(this.stoot);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			if (result == 0) {
				//this.fragment.resetAll(true);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
			    builder.setIcon(0);
			    builder.setMessage(""); //this.context.getString(R.string.${name?lower_case}_error_create));
			    builder.setPositiveButton(
			    		this.context.getString(android.R.string.yes), 
			    		new Dialog.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
						
			        }
			    });
			    builder.show();
			}
			
			this.progress.dismiss();
		}
	}
}