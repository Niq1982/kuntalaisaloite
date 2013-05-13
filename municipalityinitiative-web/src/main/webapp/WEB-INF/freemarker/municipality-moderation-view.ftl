<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/edit-blocks.ftl" as edit />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.municipality.moderation" >

<h1>Hallinnoi palvelun kuntia</h1>

<div class="view-block cf">

    <div class="column col-1of2">
        <h2>Valitse kunta</h2>
        <@f.municipalitySelect path="updateData.id" options=municipalities required="" cssClass="manage-municipality-select" key="initiative.chooseMunicipality"  allowSingleDeselect=true />
    
        <#list municipalities as municipality>
            <#if municipality_index == 0><ul id="municipalities" class="no-style" style="display:none;"></#if>
                <li data-id="${municipality.id}" data-email="${municipality.email!""}" data-active="${municipality.active?string}">${municipality.nameFi} / ${municipality.nameSv}</li>
            <#if !municipality_has_next></ul></#if>
        </#list>
    
        <br/>
        <div class="pad">
            <form action="${springMacroRequestContext.requestUri}" id="municipality-form" method="POST">
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                
                <input type="hidden" id="id" name="id" value="1"/>
                
                <br/>
                
                <h3 id="selected-municipality" data-empty="<i>Ei valittua kuntaa</i>"><i>Ei valittua kuntaa</i></h3>
                
                <@f.formCheckbox path="updateData.active" />
    
                <br/>
                <@f.textField path="updateData.municipalityEmail" required="required" optional=false cssClass="large" maxLength=InitiativeConstants.CONTACT_EMAIL_MAX />
    
                <br/>
                <button type="submit" name="${UrlConstants.ACTION_ACCEPT_INITIATIVE}" class="small-button"><span class="small-icon save-and-send">Tallenna</span></button>
                <br/><br/>
            </form>
        </div>
    </div>


<div class="column col-1of2 last">

    <h2>Aktiiviset kunnat</h2>

    <#list municipalities as municipality>
        <#if municipality_index == 0><ul class="no-style"></#if>
            <#if municipality.active>
                <li>${municipality.nameFi} / ${municipality.nameSv} <#if municipality.email??>- ${municipality.email!""}</#if></li>
            </#if>
        <#if !municipality_has_next></ul></#if>
    </#list>
</div>

</div>

</@l.main>

</#escape>