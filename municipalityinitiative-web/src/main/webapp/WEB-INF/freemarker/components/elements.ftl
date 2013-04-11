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
    <div class="initiative-content-row">
        <h2><@u.message "initiative.proposal.title" /></h2>
        
        <@u.text initiative.proposal!"" />
    </div>
</#macro>

<#-- 
 * initiativeAuthor
 * 
 * Generates initiative's public author name
 *
 * @param initiative is initiative
-->
<#macro initiativeAuthor initiative>
     <#if initiative.showName>
        <h2><@u.message "initiative.author.title" /></h2>
        <p>${initiative.authorName!""}</p>
    </#if>
</#macro>

<#-- 
 * initiativeAuthor
 * 
 * Generates initiative's contact info for private views
 *
 * @param contactInfo is author.contactInfo
-->
<#macro initiativeContactInfo contactInfo>
    <h2 class="inline-style"><@u.message "initiative.contactinfo.title" /></h2><span class="push">Ei näy julkisessa näkymässä</span>
    <p>${contactInfo.name!""}<br />
    ${contactInfo.email!""}<br />
    <#if contactInfo.address?? && contactInfo.address != ""><#noescape>${contactInfo.address?replace('\n','<br/>')!""}</#noescape><br /></#if>
    ${contactInfo.phone!""}</p>
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
            <#if initiative.state??><span class="bull">&bull;</span> <@u.message key="initiative.stateInfo."+initiative.state args=[stateTime]/></#if>
        </#if>
    </span>

</#macro>

<#-- 
 * participantCounts
 * 
 * Generates participant count infos
 *
-->
<#macro participantCounts>

    <div class="top-margin cf">
        <div class="column col-1of2">
            <p><@u.message "participantCount.franchise.total"/><br />
            <span class="user-count">${participantCount.franchise.total!""}</span><br />
            <#if (participantCount.franchise.total > 0)>
                <#if (participantCount.franchise.publicNames > 0)><a class="trigger-tooltip" href="${urls.participantList(initiative.id)}" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.franchise.publicNames!""] /></a><br /></#if>
                <#if (participantCount.franchise.privateNames > 0)><@u.message key="participantCount.privateNames" args=[participantCount.franchise.privateNames!""] /></p></#if>
            </#if>
        </div>
        <div class="column col-1of2 last">
            <p><@u.message "participantCount.noFranchise.total" /><br />
            <span class="user-count">${participantCount.noFranchise.total!""}</span><br>
            <#if (participantCount.noFranchise.total > 0)>
                <#if (participantCount.noFranchise.publicNames > 0)><a class="trigger-tooltip js-show-no-franchise-list" href="${urls.participantList(initiative.id)}?show=others" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.noFranchise.publicNames!""] /></a><br></#if>
                <#if (participantCount.noFranchise.privateNames > 0)><@u.message key="participantCount.privateNames" args=[participantCount.noFranchise.privateNames!""] /></p></#if>
            </#if>
        </div>
    </div>
    
</#macro>

</#escape> 