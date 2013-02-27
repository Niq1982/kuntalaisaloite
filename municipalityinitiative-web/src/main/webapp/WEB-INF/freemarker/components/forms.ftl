<#import "/spring.ftl" as spring />
<#import "utils.ftl" as u />

<#escape x as x?html> 

<#--
 * showError
 *
 * Validation errors that are binded in specific field
 * 
 * @param cssClass for custom styling. Multiple classes are separated with a space
-->
<#macro showError cssClass="">
    <#if spring.status.error>
        <#list spring.status.errorMessages as error>
            <#noescape><div id="${spring.status.expression}-error" class="system-msg msg-error ${cssClass}">${error!''}</div></#noescape>
        </#list>
    </#if>
</#macro>


<#--
 * formLabel
 *
 * For-attribute needs to be fixed so that it would correspond with input's name (srping.status.expression).
 *  
 * @param path the name of the field to bind to
 * @param required generates an icon and can be used in JS-validation
 * @param optional additional information for label
-->
<#macro formLabel path required optional>
    <#assign labelKey = fieldLabelKey(path) />
    <#assign forAttr = spring.status.expression />
            
    <label class="input-header" for="${forAttr!""}">
        <@u.message labelKey /> <#if required != ""><@u.icon type="required" size="small" /></#if>
        <#if optional>
            <span class="instruction-text"><@u.message labelKey + ".optional" /></span>
        </#if>
    </label>

    <#nested/>
</#macro>

<#--
 * textField
 *
 * Textfield with label -option
 *
 * @param path the name of the field to bind to
 * @param required generates an icon and can be used in JS-validation
 * @param optional additional information for label
 * @param cssClass for custom styling. Multiple classes are separated with a space
 * @param attributes for example maxlength=\"7\"
 * @param fieldType text, date, email, ...
 * 
-->
<#macro textField path required optional cssClass="" attributes="" maxLength="" fieldType="text">
    <@spring.bind path />  
    
    <@formLabel path required optional>
        <#--
            TODO: add attribute required to attributes for jQueryTools validation
            <#if required?has_content>required="required"</#if>
        -->
        
        <@showError />
        <@spring.formInput path, 'class="'+cssClass+'" maxlength="'+maxLength+'" '+attributes fieldType />
    </@formLabel>
</#macro>

<#--
 * simpleTextField
 *
 * Simple TextField without label -option
 *
 * @param path the name of the field to bind to
 * @param cssClass for custom styling. Multiple classes are separated with a space
 * @param attributes for example 'maxlength="7"'
 * @param cssErrorClass for customization of the error-message
 * 
-->
<#macro simpleTextField path cssClass="" attributes="" maxLength="" cssErrorClass="">
    <@spring.bind path />  
    
    <@showError cssClass=cssErrorClass />
    
    <@spring.formInput path, 'class="'+cssClass+'" maxlength="'+maxLength+'" '+attributes />
</#macro>

<#--
 * textarea
 *
 * @param path the name of the field to bind to
 * @param required generates an icon and can be used in JS-validation
 * @param optional additional information for label
 * @param cssClass for custom styling. Multiple classes are separated with a space
-->
<#macro textarea path required optional cssClass="">
    <@spring.bind path />  

    <@formLabel path required optional>
        <#--
            TODO: add attribute required to attributes for jQueryTools validation
            <#if required?has_content>required="required"</#if>
        -->
        
        <@showError />
        <@spring.formTextarea path, 'class="'+cssClass+'"' />
    
    </@formLabel>
</#macro>

<#--
 * formCheckbox
 *
 * @param path the name of the field to bind to
 * @param attributes an additional string of arbitrary tags or text to be included within the HTML tag itself
 * @param prefix for custom messages
-->
<#macro formCheckbox path checked=false attributes="" prefix="">
    <@spring.bind path />
    <#assign id="${spring.status.expression}">
    <#if spring.status.value??>
        <#assign isSelected = spring.status.value?string=="true" />
    <#else>
        <#assign isSelected = checked />
    </#if>
    <input type="hidden" name="_${id}" value="on" />   
    
    <@showError />
     
    <label class="inline">
        <input type="checkbox" id="${id}" name="${id}"<#if isSelected> checked="checked"</#if> ${attributes}/>
        <@u.message (prefix!="")?string(prefix+".",'')+path /><br />
    </label>
</#macro>


<#--
 * radiobutton
 *
 * @param path the name of the field to bind to
 * @param options a Map of all the available values that can be selected from in the input field.
 * @param required generates an icon and can be used in JS-validation
 * @param attributes an additional string of arbitrary tags or text to be included within the HTML tag itself
-->
<#macro radiobutton path options required="" attributes="" header=true>
    <@spring.bind path />  
 
    <#if header>
        <div class="input-header">
            <@u.message path /><#if required != ""> <@u.icon type="required" size="small" /></#if>
        </div>
    </#if>
    
    <@showError />
    
    <#list options?keys as value>
        <label>
            <input type="radio" id="${options[value]}" name="${spring.status.expression}" value="${value}"
                <#if spring.stringStatusValue == value>checked="checked"</#if> ${attributes}
            <@spring.closeTag/>
            <span class="label"><@u.message "${options[value]}" /></span>
        </label>
    </#list>
</#macro>

<#--
 * municipalitySelect
 *
 * Show a selectbox (dropdown) input element allowing a single value to be chosen
 * from a list of options.
 *
 * @param path the name of the field to bind to
 * @param options a map (value=label) of all the available options
 * @param attributes any additional attributes for the element (such as class
 *        or CSS styles or size
 * @param preSelected the predefined value for the select
-->
<#macro municipalitySelect path options required="" cssClass="" attributes="" preSelected="" showLabel=true defaultOption="initiative.chooseMunicipality">
    <@spring.bind path />
    
    <#if showLabel>
        <@formLabel path required false />
    </#if>
    
    <@showError />
    
    <#--
     * This is crucial for JavaScript. It sets correct value both in double selects and single selects
     * with pre selected values and when error occurs in the form.
    -->
    <#if preSelected?string != "">
        <#assign data = preSelected />
    <#else>
        <#assign data = spring.status.value!"" />
    </#if>

    <select name="${spring.status.expression}" id="${spring.status.expression}" ${attributes} class="chzn-select ${cssClass}" data-initiative-municipality="${data}" data-placeholder="<@u.message defaultOption />">
        <option value=""><@u.message defaultOption /></option>
        <#list options as option>
            <option value="${option.id}"<@checkSelected option.id preSelected />>${option.name}</option>
        </#list>
    </select>
</#macro>

<#--
 * checkSelected
 *
 * Check a value in a list to see if it is the currently selected value.
 * If so, add the 'selected="selected"' text to the output.
 * Handles values of numeric and string types.
 * This function is used internally but can be accessed by user code if required.
 *
 * @param value the current value in a list iteration
 * @param preSelected option. If spring.status.value has value select it.
-->
<#macro checkSelected value preSelected>
    <#if spring.stringStatusValue?has_content>
        <#if spring.stringStatusValue?is_number && spring.stringStatusValue == value?number>selected="selected"</#if>
        <#if spring.stringStatusValue?is_string && spring.stringStatusValue == value?string>selected="selected"</#if>
    <#else>
        <#if preSelected?is_number && value?is_number && preSelected == value?number>selected="selected"</#if>
        <#if preSelected?is_string && preSelected == value?string>selected="selected"</#if>
    </#if>
</#macro>

<#--
 * helpText
 *
 * Help texts for the edit-form
 *
 * @param path the name of the field to bind to
 * @param noscript use 'noscript' if message for noscript-users
-->
<#macro helpText path href="">
    <h4><@u.message path+".title" /></h4>
    <@u.messageHTML key=path+".description" args=[href] />
</#macro>


<#--
 * contactInfo
 *
 * Prints the edit block for current author's roles and contact details
 *
 * @param path is a string "initiative.currentAuthor"
 * @param realPath is a variable initiative.currentAuthor
 * @param mode is either 'modal' or 'full'
 * @param prefix for custom messages
 * @param cssClass for styling. Multiple classes are separated with a space
 * @param showName true or false for showing name-field

-->
<#macro contactInfo path realPath="" mode="" prefix="" cssClass="" showName=false>

    <div class="input-header">
        <@u.message path />
    </div>
    
    <#if showName>
        <@textField path=path+".name" required="required" optional=false cssClass="large" maxLength=InitiativeConstants.CONTACT_NAME_MAX />
    </#if>

    <div class="initiative-contact-details">
        <div class="column col-1of2">
            <@textField path=path+".email" required="required" optional=false cssClass="medium"  maxLength=InitiativeConstants.CONTACT_EMAIL_MAX />
            
            <@textField path=path+".phone" required="" optional=false cssClass="medium"  maxLength=InitiativeConstants.CONTACT_PHONE_MAX />
        </div>
        
        <div class="column col-1of2 last">
            <label>
                <@u.message path+".address" />
                <@spring.formTextarea path+".address", 'class="address-field noresize" maxlength="'+InitiativeConstants.CONTACT_ADDRESS_MAX+'"' />
            </label>
        </div>
    
    </div>
        
</#macro>

</#escape> 
