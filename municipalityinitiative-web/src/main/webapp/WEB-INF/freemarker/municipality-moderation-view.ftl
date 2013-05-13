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
        <@f.municipalitySelect path="updateData.id" options=municipalities required="required" cssClass="municipality-select" />
    
        <#list municipalities as municipality>
            <#if municipality_index == 0><ul id="municipalities" class="no-style" style="display:none;"></#if>
                <li data-id="${municipality.id}" data-email="${municipality.email!""}" data-active="${municipality.active?string}">${municipality.nameFi} / ${municipality.nameSv}</li>
            <#if !municipality_has_next></ul></#if>
        </#list>
    
        <br/>
        <div class="pad">
            <form action="${springMacroRequestContext.requestUri}" method="POST">
                <#--<input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                <input type="hidden" name="${UrlConstants.PARAM_MANAGEMENT_CODE}" value="${initiative.managementHash.value}"/>-->
                
                <br/>
                <label class="inline">
                    <input type="checkbox" name="contactInfo.showName" id="contactInfo.showName">
                    <span class="label">Aktiivinen</span>
                </label>
    
                <br/>
                <label for="participantEmail" class="input-header">
                    Sähköpostiosoite <span class="icon-small required trigger-tooltip"></span>
                </label>
                <input type="text" maxlength="100" class="large" value="" name="participantEmail" id="participantEmail">
    
                <br/>
                <button type="submit" name="${UrlConstants.ACTION_ACCEPT_INITIATIVE}" class="small-button"><span class="small-icon save-and-send">Tallenna</span></button>
                <a href="${springMacroRequestContext.requestUri}#participants" class="push js-btn-close-block hidden"><@u.message "action.cancel" /></a>
                <br/><br/>
            </form>
        </div>
    </div>


<div class="column col-1of2 last">

    <h2>Aktiiviset kunnat</h2>

    <#list municipalities as municipality>
        <#if municipality_index == 0><ul class="no-style"></#if>
            <#if municipality.active>
                <li>${municipality.nameFi} / ${municipality.nameSv} - Sähköposti: ${municipality.email!""}</li>
            </#if>
        <#if !municipality_has_next></ul></#if>
    </#list>
</div>

</div>

</@l.main>

</#escape>