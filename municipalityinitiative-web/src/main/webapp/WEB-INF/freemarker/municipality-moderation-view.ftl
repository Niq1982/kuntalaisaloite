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

   <div class="input-block-content">
    <h2>Valitse kunta:</h2>
        <@f.municipalitySelect path="updateData.id" options=municipalities required="required" cssClass="municipality-select"/>
    </div>

<div class="input-block-content">
<h2>Aktiiviset kunnat:</h2>
    <#list municipalities as municipality>

        <#if municipality.active>

        <p>${municipality.nameFi} / ${municipality.nameSv} - Sähköposti: ${municipality.email!""}</p>

        </#if>

    </#list>
</div>

</@l.main>

</#escape>