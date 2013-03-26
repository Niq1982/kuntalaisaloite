<#import "utils.ftl" as u />

<#escape x as x?html> 

<#-- 
 * initiativeView
 * 
 * Generates initiative's public view block
 *
 * @param thisInitiative is initiative
-->
<#macro initiativeView thisInitiative>
    <div class="initiative-content-row">
        <h2><@u.message "initiative.content.title" /></h2>
        
        <@u.text thisInitiative.proposal!"" />
    </div>
</#macro>

<#-- 
 * initiativeAuthor
 * 
 * Generates initiative's public author name
 *
 * @param thisInitiative is initiative
-->
<#macro initiativeAuthor thisInitiative>
     <#if thisInitiative.showName>
         <div class="initiative-content-row last">
            <h2><@u.message "initiative.author.title" /></h2>
            <p>${thisInitiative.authorName!""}</p>
        </div>
    </#if>
</#macro>

<#-- 
 * stateInfo
 * 
 * Generates initiative's state dates
 *
 * @param thisInitiative is initiative
-->
<#macro stateInfo thisInitiative>
    
    <span class="extra-info">
        <#if thisInitiative.sentTime.present>
            <#assign sentTime><@u.localDate thisInitiative.sentTime.value /></#assign>
            <@u.message key="initiative.date.sent" args=[sentTime] />
        <#else>
            <#assign createTime><@u.localDate thisInitiative.createTime /></#assign>
            <@u.message key="initiative.date.create" args=[createTime] />
            <br />
            <@u.message "initiative.stateInfo."+initiative.state />
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
            <p><@u.message key="participantCount.franchise.total" args=[initiative.municipality.name!""] /><br />
            <span class="user-count">${participantCount.franchise.total!""}</span><br />
            <#if (participantCount.franchise.total > 0)>
                <#if (participantCount.franchise.publicNames > 0)><a class="trigger-tooltip" href="${urls.participantList(initiative.id)}" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.franchise.publicNames!""] /></a><br /></#if>
                <#if (participantCount.franchise.privateNames > 0)><@u.message key="participantCount.privateNames" args=[participantCount.franchise.privateNames!""] /></p></#if>
            </#if>
        </div>
        <div class="column col-1of2 last">
            <p><@u.message key="participantCount.noFranchise.total" args=[initiative.municipality.name!""] /><br />
            <span class="user-count">${participantCount.noFranchise.total!""}</span><br>
            <#if (participantCount.noFranchise.total > 0)>
                <#if (participantCount.noFranchise.publicNames > 0)><a class="trigger-tooltip js-show-no-franchise-list" href="${urls.participantList(initiative.id)}?show=others" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.noFranchise.publicNames!""] /></a><br></#if>
                <#if (participantCount.noFranchise.privateNames > 0)><@u.message key="participantCount.privateNames" args=[participantCount.noFranchise.privateNames!""] /></p></#if>
            </#if>
        </div>
    </div>
    
</#macro>

</#escape> 