<#import "utils.ftl" as u />

<#escape x as x?html> 

<#-- 
 * initiativeView
 * 
 * Generates initiative's public view block
 *
 * @param initiative is initiative
-->
<#macro initiativeView initiative>
    <h2><@u.message "initiative.proposal.title" /></h2>

    <@u.text initiative.proposal!"" />

    <#if (initiative.extraInfo)?has_content>
        <h2><@u.message "initiative.extraInfo.title" /></h2>
        <@u.text initiative.extraInfo!"" />
    </#if>
</#macro>

<#-- 
 * initiativeAuthor
 * 
 * Generates initiative's public author name
 *
 * @param initiative is initiative
-->
<#macro initiativeAuthor publicAuthors>
    <h3><@u.message key="initiative.authors.title" args=[publicAuthors.publicNames+publicAuthors.privateNames] /></h3>

    <#if (publicAuthors.publicNames > 0)>
        <#list publicAuthors.publicAuthors as publicAuthor>
            <div class="column ${((publicAuthor_index + 1) % 3 == 0)?string("last","")}">
                <h4 class="header">${publicAuthor.name}</h4>
                <p>${publicAuthor.municipality.getName(locale)}</p>
            </div>
            <#if ((publicAuthor_index + 1) % 3 == 0) || !publicAuthor_has_next><br class="clear" /></#if>
        </#list>
    
    </#if>
    <#if (publicAuthors.publicNames == 0) && (publicAuthors.privateNames == 1)>
        <p><@u.message key="authors.onlyOnePrivate" /></p>
    <#elseif (publicAuthors.privateNames > 0)>
        <p><@u.messageHTML key="authors.privateAuthors" args=[publicAuthors.publicNames, publicAuthors.privateNames] /></p>
    </#if>
</#macro>

<#-- 
 * initiativeAuthor
 * 
 * Generates initiative's contact info for private views
 *
 * @param contactInfo is author.contactInfo
-->
<#macro initiativeContactInfo authorList>
    <h2><@u.message key="initiative.authors.title" args=[authorList?size] /></h2>
    
    <@u.systemMessage path="initiative.authors.contactinfo.notPublic" type="info" showClose=false />
    <br />
    
    <#list authorList as a>
        <div class="column ${((a_index + 1) % 3 == 0)?string("last","")}">
            <p><strong>${a.contactInfo.name!""}</strong>, ${a.municipality.getName(locale)}<br />
            ${a.contactInfo.email!""}<br />
            <#if a.contactInfo.address?? && a.contactInfo.address != ""><#noescape>${a.contactInfo.address?replace('\n','<br/>')!""}</#noescape><br /></#if>
            ${a.contactInfo.phone!""}</p>
        </div>
        <#if ((a_index + 1) % 3 == 0) || !a_has_next><br class="clear" /></#if>
    </#list>
</#macro>



<#-- 
 * stateInfo
 * 
 * Generates initiative's state dates
 *
 * @param initiative is initiative
-->
<#macro stateInfo initiative>
    
    <span class="extra-info">
        <#if initiative.sentTime.present>
            <#assign sentTime><@u.localDate initiative.sentTime.value /></#assign>
            <@u.message key="initiative.date.sent" args=[sentTime] />
        <#else>
            <#assign createTime><@u.localDate initiative.createTime /></#assign>
            <@u.message key="initiative.date.create" args=[createTime] />
            <#assign stateTime><@u.localDate initiative.stateTime/></#assign>
            
            <#if initiative.fixState != FixState.OK>
                <span class="bull">&bull;</span> <@u.message key="initiative.fixStateInfo."+initiative.fixState />
            <#elseif initiative.state??>
                <span class="bull">&bull;</span> <@u.message key="initiative.stateInfo."+initiative.state args=[stateTime]/>
                <#if initiative.state == InitiativeState.PUBLISHED && initiative.collaborative><@u.message key="initiative.stateInfo.collecting" /></#if>
            </#if>
        </#if>
    </span>

</#macro>

</#escape> 