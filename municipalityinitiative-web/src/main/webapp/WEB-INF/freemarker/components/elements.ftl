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
            <@u.message "initiative.state.collecting" />
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
            <@u.message "initiative.state.collecting" />
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
            <p><@u.message key="participantCount.rightOfVoting.total" args=[initiative.municipalityName!""] /><br />
            <span class="user-count">${participantCount.rightOfVoting.total!""}</span><br />
            <#if (participantCount.rightOfVoting.total > 0)>
                <#if (participantCount.rightOfVoting.publicNames > 0)><a class="trigger-tooltip" href="${urls.participantList(initiative.id)}" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.rightOfVoting.publicNames!""] /></a><br /></#if>
                <#if (participantCount.rightOfVoting.privateNames > 0)><@u.message key="participantCount.privateNames" args=[participantCount.rightOfVoting.privateNames!""] /></p></#if>
            </#if>
        </div>
        <div class="column col-1of2 last">
            <p><@u.message key="participantCount.noRightOfVoting.total" args=[initiative.municipalityName!""] /><br />
            <span class="user-count">${participantCount.noRightOfVoting.total!""}</span><br>
            <#if (participantCount.noRightOfVoting.total > 0)>
                <#if (participantCount.noRightOfVoting.publicNames > 0)><a class="trigger-tooltip js-show-no-franchise-list" href="${urls.participantList(initiative.id)}?show=others" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.noRightOfVoting.publicNames!""] /></a><br></#if>
                <#if (participantCount.noRightOfVoting.privateNames > 0)><@u.message key="participantCount.privateNames" args=[participantCount.noRightOfVoting.privateNames!""] /></p></#if>
            </#if>
        </div>
    </div>
    
</#macro>

</#escape> 