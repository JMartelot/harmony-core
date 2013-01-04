<#import "methods.tpl" as m>
package ${localnamespace};

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import ${namespace}.R;
import ${namespace}.entity.${name};

<#list relations as relation>
	<#if !relation.internal && !relation.hidden>
import ${namespace}.entity.${relation.relation.targetEntity};
	</#if>
</#list>

public class ${name}ListAdapter extends ArrayAdapter<${name}> {
	private final LayoutInflater mInflater;

	public ${name}ListAdapter(Context context) {
		super(context, R.layout.row_${name?lower_case});

		this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/** Set Array of ${name}
	 * 
	 * @param data the array
	 */
	public void setData(List<${name}> data) {
		this.clear();

		if (data != null) {
			for (${name} item : data) {
				this.add(item);
			}
		}
	}

	/** (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override 
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = this.mInflater.inflate(R.layout.row_${name?lower_case}, parent, false);

			holder = new ViewHolder();
			<#list fields as field>
				<#if !field.internal && !field.hidden>
					<#if field.type=="boolean">
			holder.${field.name}View = (CheckBox) convertView.findViewById(R.id.row_${name?lower_case}_${field.name?lower_case});
			holder.${field.name}View.setEnabled(false);
					<#else>
			holder.${field.name}View = (TextView) convertView.findViewById(R.id.row_${name?lower_case}_${field.name?lower_case});			
					</#if>
				</#if>
			</#list>
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		${name} item = getItem(position);
		if ( item != null && holder != null)
			holder.populate(item);

		return convertView;
	}

	/** Holder row */
	private static class ViewHolder {
		<#list fields as field>
			<#if !field.hidden && !field.internal>
				<#if field.type=="boolean">
		protected CheckBox ${field.name}View;
				<#else>
		protected TextView ${field.name}View;			
				</#if>
			</#if>
		</#list>

		/** Populate row with a ${name}
		 * 
		 * @param item ${name} data
		 */
		public void populate(${name} model) {

			<#list fields as field>
				<#if !field.internal && !field.hidden>
					<#if (!field.relation??)>
						<#if (field.type!="int") && (field.type!="boolean") && (field.type!="long") && (field.type!="ean") && (field.type!="zipcode") && (field.type!="float")>
			if(model.get${field.name?cap_first}()!=null)
				${m.setAdapterLoader(field)}
						<#else>
			${m.setAdapterLoader(field)}
						</#if>
					<#elseif (field.relation.type=="OneToOne" | field.relation.type=="ManyToOne")>
			this.${field.name}View.setText(String.valueOf(model.get${field.name?cap_first}().getId()) );
					<#elseif (field.relation.type=="OneToMany" | field.relation.type=="ManyToMany")>
			String ${field.name}String = "";
			for(${field.relation.targetEntity?cap_first} ${field.name}Entity : model.get${field.name?cap_first}()){
				${field.name}String += ${field.name}Entity.getId()+",";
			}
			this.${field.name}View.setText(String.valueOf(${field.name}String) );		
					</#if>
				</#if>
			</#list>

		}
	}
}
