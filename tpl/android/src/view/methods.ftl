<#function setLoader field>
	<#assign type=field.type />
	<#assign ret="this."+field.name+"View" />
	<#if (type=="boolean")>
		<#assign ret=ret+".setChecked(this.model.is"+field.name?cap_first+"());" />
	<#else>
		<#assign getter="this.model.get"+field.name?cap_first+"()" />
		<#assign ret=ret+".setText(" />
		<#if (type?lower_case=="string" || type?lower_case=="email" || type?lower_case=="login" || type?lower_case=="password" || type?lower_case=="city" || type?lower_case=="text" || type?lower_case=="phone" || type?lower_case=="country")>
			<#assign ret=ret+getter />
		<#elseif (type?lower_case == "int" || type?lower_case == "integer" ||  type?lower_case=="ean" || type?lower_case=="zipcode" || type?lower_case=="float" || type?lower_case == "double" || type?lower_case == "long" || type?lower_case == "char" || type?lower_case == "byte" || type?lower_case == "short" || type?lower_case == "character")>
			<#assign ret=ret+"String.valueOf("+getter+")" /> 
		<#elseif (field.harmony_type?lower_case == "enum")>
			<#if (enums[field.type].id??)>
				<#assign ret=ret+"String.valueOf("+getter+".getValue())" /> 
			<#else>
				<#assign ret=ret+getter+".name()" /> 
			</#if>
		</#if>
		<#assign ret=ret+");" />
	</#if>
	<#return ret />
</#function>

<#function setAdapterLoader field>
	<#assign type=field.type />
	<#assign ret="this."+field.name+"View" />
	<#if (type?lower_case=="boolean")>
		<#assign ret=ret+".setChecked(model.is"+field.name?cap_first+"());" />
	<#elseif (type?lower_case=="datetime" || type?lower_case=="date" || type?lower_case=="time")>
		<#if (field.harmony_type=="datetime")>
			<#assign ret=ret+".setText(DateUtils.formatDateTimeToString(model.get"+field.name?cap_first+"()));" />
		</#if>
		<#if (field.harmony_type=="date")>
			<#assign ret=ret+".setText(DateUtils.formatDateToString(model.get"+field.name?cap_first+"()));" />
		</#if>
		<#if (field.harmony_type=="time")>
			<#assign ret=ret+".setText(DateUtils.formatTimeToString(model.get"+field.name?cap_first+"()));" />
		</#if>
	<#else>
		<#assign getter="model.get"+field.name?cap_first+"()" />
		<#assign ret=ret+".setText(" />
		<#if (type?lower_case=="string" || type?lower_case=="email" || type?lower_case=="login" || type?lower_case=="password" || type?lower_case=="city" || type?lower_case=="text" || type?lower_case=="phone" || type?lower_case=="country")>
			<#assign ret=ret+getter />
		<#elseif  (type?lower_case == "int" || type?lower_case == "integer" || type?lower_case=="ean" || type?lower_case=="zipcode" || type?lower_case=="float" || type?lower_case == "double" || type?lower_case == "long" || type?lower_case == "char" || type?lower_case == "byte" || type?lower_case == "short" || type?lower_case == "character")>
			<#assign ret=ret+"String.valueOf("+getter+")" />
		<#elseif (field.harmony_type?lower_case == "enum")>
			<#if (enums[field.type].id??)>
				<#assign ret=ret+"String.valueOf("+getter+".getValue())" /> 
			<#else>
				<#assign ret=ret+getter+".name()" /> 
			</#if>
		</#if>
		<#assign ret=ret+");" /> 
	</#if>
	<#return ret/>
</#function>

<#function setSaver field>
	<#assign type=field.type />
	<#assign ret="this.model.set"+field.name?cap_first+"(" />
	<#if (type?lower_case=="boolean")>
		<#assign ret=ret+"this."+field.name+"View.isChecked());" />
	<#elseif (type?lower_case == "datetime")>
		<#if (field.harmony_type=="date")>
			<#assign ret=ret+"DateUtils.formatStringToDate(this."+field.name+"DateView.getEditableText().toString()));" />
		<#elseif (field.harmony_type=="time")>
			<#assign ret=ret+"DateUtils.formatStringToTime(this."+field.name+"TimeView.getEditableText().toString()));" />
		<#elseif (field.harmony_type?lower_case=="datetime")>
			<#assign ret=ret+"DateUtils.formatStringToDateTime(
				this."+field.name+"DateView.getEditableText().toString(),
				this."+field.name+"TimeView.getEditableText().toString()));" />
		</#if>
	<#else>
		<#assign getter="this."+field.name+"View.getEditableText().toString()" />
		<#if (type?lower_case=="string" || type?lower_case=="email" || type?lower_case=="login" || type?lower_case=="password" || type?lower_case=="city" || type?lower_case=="text" || type?lower_case=="phone" || type?lower_case=="country")>
			<#assign ret=ret+getter />
		<#elseif (type?lower_case == "int" || type?lower_case == "integer")>
			<#assign ret=ret+"Integer.parseInt("+getter+")" />
		<#elseif (type?lower_case=="long")>
			<#assign ret=ret+"Long.parseLong("+getter+")" />
		<#elseif (type?lower_case=="float")>
			<#assign ret=ret+"Float.parseFloat("+getter+")" />
		<#elseif (type?lower_case=="double")>
			<#assign ret=ret+"Double.parseDouble("+getter+")" />
		<#elseif (type?lower_case=="short")>
			<#assign ret=ret+"Short.parseShort("+getter+")" />
		<#elseif (type?lower_case=="char")>
			<#assign ret=ret+getter+".charAt(0)" />
		<#elseif (type?lower_case=="byte")>
			<#assign ret=ret+"Byte.parseByte("+getter+")" />
		<#elseif (type?lower_case=="character")>
			<#assign ret=ret+getter+".charAt(0)" />
		<#elseif (field.harmony_type?lower_case == "enum")>
			<#assign enumType = enums[field.type] />
			<#if (enumType.id??)>
				<#assign idEnum = enumType.fields[enumType.id] />
				<#if (idEnum.type?lower_case == "int" || idEnum.type?lower_case == "integer") >
					<#assign ret=ret+field.type+".fromValue(Integer.parseInt("+getter+"))" />
				<#else>
					<#assign ret=ret+field.type+".fromValue("+getter+")" />
				</#if>
			<#else>
				<#assign ret=ret+field.type+".valueOf("+getter+")" />
			</#if>
		</#if>
		<#assign ret=ret+");" />
	</#if>
	<#return ret />
</#function>


<#function isInArray array var>
	<#list array as item>
		<#if (item==var)>
			<#return true />
		</#if>
	</#list>
	<#return false />
</#function>

<#function getAllMothers tab entity>
	<#if entity.mother??>
		<#return (getAllMothers(tab, entities[entity.mother]) + [entity]) />
	<#else>
		<#return ([entity]) />		
	</#if>
</#function>
<#function getCompleteNamespace entity>
	<#assign result = "" />
	<#assign motherClasses = getAllMothers([], entity) />
	<#assign cond = true />
	<#list motherClasses as motherClass>
		<#assign result = result + motherClass.name />
		<#if motherClass_has_next>
			<#assign result = result + "." />
		</#if>
	</#list>
	
	<#return result>
</#function>

<#function getAllFields class>
	<#assign fields = class.fields />
	<#if class.extends?? && entities[class.extends]??>
		<#assign fields = fields + getAllFields(entities[class.extends]) />
	</#if>
	<#return fields />
</#function>
