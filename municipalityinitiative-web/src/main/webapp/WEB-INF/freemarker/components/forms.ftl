<#import "/spring.ftl" as spring />
<#import "utils.ftl" as u />

<#escape x as x?html> 

<#--
 * localizePath
 *
 * Localize path if it is a hash
 *
 * @param value is the spring.status.value of the current field
-->
<#macro localizePath path value="">
    <#if value?? && value?is_hash>
        <#global pathLocale>${path+"."+locale}</#global>
    <#else>
        <#global pathLocale>${path}</#global>
    </#if>
</#macro>

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
            <#noescape><div id="${spring.status.expression}" class="system-msg msg-error ${cssClass}">${error!''}</div></#noescape>
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
<#macro formLabel path required>
    <#assign labelKey = fieldLabelKey(path) />
    <#if spring.status.value?? && spring.status.value?is_hash>
        <#assign forAttr = spring.status.expression+"."+locale />
    <#else>
        <#assign forAttr = spring.status.expression />
    </#if>
            
    <label class="input-header" for="${forAttr!""}">
        <@u.message labelKey /> <#if required != ""><@u.icon type="required" size="small" /></#if>
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
<#macro textField path required cssClass="" attributes="" maxLength="" fieldType="text">
    <@spring.bind path />  
    <@localizePath path spring.status.value />
    
    <@formLabel pathLocale required>
        <#--
            TODO: add attribute required to attributes for jQueryTools validation
            <#if required?has_content>required="required"</#if>
        -->
        <@spring.bind pathLocale />
        <@showError />
        <@spring.formInput pathLocale, 'class="'+cssClass+'" maxlength="'+maxLength+'" '+attributes fieldType />
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
    <@localizePath path spring.status.value />
    
    <@showError cssClass=cssErrorClass />
    
    <@spring.formInput pathLocale, 'class="'+cssClass+'" maxlength="'+maxLength+'" '+attributes />
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
    <@localizePath path spring.status.value />  

    <@formLabel pathLocale required>
        <#--
            TODO: add attribute required to attributes for jQueryTools validation
            <#if required?has_content>required="required"</#if>
        -->
        <@spring.bind pathLocale />
        <@showError />
        <@spring.formTextarea pathLocale, 'class="'+cssClass+'"' />
    
    </@formLabel>
</#macro>

<#--
 * formCheckbox
 *
 * @param path the name of the field to bind to
 * @param attributes an additional string of arbitrary tags or text to be included within the HTML tag itself
 * @param prefix for custom messages
-->
<#macro formCheckbox path attributes="" prefix="">
    <@spring.bind path />
    <#assign id="${spring.status.expression}">
    <#assign isSelected = spring.status.value?? && spring.status.value?string=="true">
    <input type="hidden" name="_${id}" value="on"/>   
    
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
<#macro radiobutton path options required="" attributes="">
    <@spring.bind path />  
 
    <div class="input-header">
        <@u.message path /><#if required != ""> <@u.icon type="required" size="small" /></#if>
    </div>
    
    <#list options?keys as value>
        <label class="inline">
            <input type="radio" id="${options[value]}" name="${spring.status.expression}" value="${value}"
                <#if spring.stringStatusValue == value>checked="checked"</#if> ${attributes}
            <@spring.closeTag/>
            <@u.message "${options[value]}" /><br />
        </label>
    </#list>
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
 * currentAuthor
 *
 * Prints the edit block for current author's roles and contact details
 *
 * @param path is a string "initiative.currentAuthor"
 * @param realPath is a variable initiative.currentAuthor
 * @param mode is either 'modal' or 'full'
 * @param prefix for custom messages
 * @param cssClass for styling. Multiple classes are separated with a space
 *
 * FIXME:
 *      - Use correct path in user names
 *       - Remove realPath if possible somehow ?!
-->
<#macro currentAuthor path realPath="" mode="" prefix="" cssClass="">

    <div class="input-block-content">
        <div class="input-header">
            <@u.message "initiative.currentAuthor.contactDetails" /> <@u.icon type="required" size="small" />
        </div>

        <@spring.bind path+".contactInfo" />
        <@f.showError />
        
        <div class="initiative-own-details-area">
            <div class="column col-1of2">
                <label>
                    <@u.message "initiative.currentAuthor.contactInfo.email" />
                    <@spring.formInput path+'.contactInfo.email', 'class="medium" maxlength="'+InitiativeConstants.AUTHOR_EMAIL_MAX?string("#")+'"' />
                </label>
                
                <label>
                    <@u.message "initiative.currentAuthor.contactInfo.phone" />
                    <@spring.formInput path+'.contactInfo.phone', 'class="medium" maxlength="'+InitiativeConstants.AUTHOR_PHONE_MAX?string("#")+'"' />
                </label>
            </div>
            
            <div class="column col-1of2 last">
                <label>
                    <@u.message "initiative.currentAuthor.contactInfo.address" />
                    <#--<@spring.formTextarea path+'.contactInfo.address', 'class="address-field noresize" maxlength="'+InitiativeConstants.AUTHOR_ADDRESS_MAX?string("#")+'"' />-->
                    <#-- NOTE: maxlength 1024 will cause an error -->
                    <@spring.formTextarea path+'.contactInfo.address', 'class="address-field noresize" maxlength="1000"' />
                </label>
            </div>
        
        </div>
    </div>
        
</#macro>


</#escape> 
