<#import "utils.ftl" as u />

<#escape x as x?html> 

<#-- 
 * stateDates
 * 
 * Generates initiative's state dates
 *
 * @param thisInitiative is initiative
-->
<#macro stateDates thisInitiative>
    
    <span class="extra-info">
        <#if thisInitiative.collectable && thisInitiative.createTime??>
            <#assign createTime><@u.localDate thisInitiative.createTime /></#assign>
            <@u.message key="initiative.date.create" args=[createTime] />
            <br />
        </#if>

        <#if thisInitiative.sentTime.present>
            <#assign sentTime><@u.localDate thisInitiative.sentTime.value /></#assign>
            <@u.message key="initiative.date.sent" args=[sentTime] />
        <#elseif thisInitiative.collectable>
            <@u.message "initiative.state.collecting.long" />
        </#if>
    </span>

</#macro>

<#-- 
 * initiativeStateInfo
 * 
 * Generates initiative's state with dates
 *
 * @param thisInitiative is initiative
-->
<#macro initiativeStateInfo thisInitiative>
    
    <span class="state">
        <#if thisInitiative.sentTime.present>
            <#assign sentTime><@u.localDate thisInitiative.sentTime.value /></#assign>
            <@u.message key="initiative.date.sent" args=[sentTime] />
        <#elseif thisInitiative.collectable>
            <@u.message "initiative.state.collecting.long" />
        <#else>
            <#assign createTime><@u.localDate thisInitiative.createTime /></#assign>
            <@u.message key="initiative.date.create" args=[createTime] />
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
            <p><@u.message key="participantCount.franchise.total" args=[initiative.municipalityName!""] /><br />
            <span class="user-count">${participantCount.franchise.total!""}</span><br />
            <#if (participantCount.franchise.total > 0)>
                <#if (participantCount.franchise.publicNames > 0)><a class="trigger-tooltip" href="${urls.participantList(initiative.id)}" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.franchise.publicNames!""] /></a><br /></#if>
                <#if (participantCount.franchise.privateNames > 0)><@u.message key="participantCount.privateNames" args=[participantCount.franchise.privateNames!""] /></p></#if>
            </#if>
        </div>
        <div class="column col-1of2 last">
            <p><@u.message key="participantCount.noFranchise.total" args=[initiative.municipalityName!""] /><br />
            <span class="user-count">${participantCount.noFranchise.total!""}</span><br>
            <#if (participantCount.noFranchise.total > 0)>
                <#if (participantCount.noFranchise.publicNames > 0)><a class="trigger-tooltip js-show-no-franchise-list" href="${urls.participantList(initiative.id)}?show=others" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.noFranchise.publicNames!""] /></a><br></#if>
                <#if (participantCount.noFranchise.privateNames > 0)><@u.message key="participantCount.privateNames" args=[participantCount.noFranchise.privateNames!""] /></p></#if>
            </#if>
        </div>
    </div>
    
</#macro>

</#escape> 