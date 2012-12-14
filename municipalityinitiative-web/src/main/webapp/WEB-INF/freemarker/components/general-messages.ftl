<#import "/spring.ftl" as spring />
<#import "forms.ftl" as f />
<#import "utils.ftl" as u />

<#escape x as x?html> 
 
<#-- 
 * initiativeVoteInfo
 * 
 * Displays vote count. Shows votes all together and from this service.
 *
 * Requirements:
 * - Initiative state is ACCEPTED
 * - Voting is in progress (date requirement)
 * - Must have at least 1 vote all together
-->
<#macro initiativeVoteInfo>
    <#if votingInfo?? && votingInfo.votingInProggress || initiative.totalSupportCount gt 0>
        <#if initiative.supportCount != initiative.totalSupportCount && initiative.supportCount gt 0>
            <p><@u.messageHTML key="initiative.totalSupportCount" args=[initiative.totalSupportCount, initiative.supportCount] /></p>
        <#else>
            <p><@u.messageHTML key="initiative.supportCount" args=[initiative.totalSupportCount, initiative.supportCount] /></p>
        </#if>
    </#if>
</#macro>

<#-- 
 * initiativeVote
 * 
 * Displays one option at time:
 * - Vote-button
 * - User already voted
 * - Initiative is accepted, but voting is not yet started
 *
 * Requirements:
 * - Initiative state is ACCEPTED
-->
<#macro initiativeVote>
    <#-- User already voted -->
    <#if votingInfo?? && votingInfo.votingTime??>
        <#assign userAlreadyVoted>
            <span><@u.message "vote.userAlreadyVoted" /> <@u.localDate votingInfo.votingTime />.</span>            
        </#assign>
        <@u.systemMessageHTML userAlreadyVoted "info" />
        
    <#-- Vote button -->    
    <#elseif votingInfo?? && (votingInfo.allowVotingAction)>
        <#assign votingInfoMessage>
            <#assign href>${urls.helpIndex()}/<@u.message "HelpPage.INITIATIVE" /></#assign>
            <@u.messageHTML key="vote.info" args=[href] />
            <p><a href="${urls.vote(initiative.id)}" class="small-button green" title="<@u.message "vote.btn" />"><span class="small-icon save-and-send"><@u.message "vote.btn" /></span></a></p>
        </#assign>
        <@u.systemMessageHTML votingInfoMessage "info" />
        
    <#-- Initiative is accepted, but voting is not yet started -->
    <#elseif votingInfo?? && (initiative.state == InitiativeState.ACCEPTED && (votingInfo?? &&!votingInfo.votingStarted)) >
        <#assign initiativeAccepted>
            <#assign startDateLocal><@u.localDate initiative.startDate /></#assign>
            <#assign args=[startDateLocal] />
            <@u.messageHTML "initiative.stateInfo.accepted" args />
        </#assign>
        <@u.systemMessageHTML initiativeAccepted "info" />
    </#if>
</#macro>


<#-- 
 * votingSuspended
 * 
 * Voting is suspended. Initiative did not get at least 50 votes within the first month.
 *
 * Requirements:
 * - Initiative state is ACCEPTED
 * - Suspend date exceeded and less than 50 votes
-->
<#macro votingSuspended>
    <#if votingInfo?? && votingInfo.votingSuspended>
        <#assign votingSuspendedHTML>                
            <#assign suspendDate><#if initiative.startDate??><@u.localDate initiative.startDate.plus(requiredMinSupportCountDuration) /></#if></#assign>
            <@u.message key="initiative.votingSuspended" args=[suspendDate, minSupportCountForSearch] />
        </#assign>
        <@u.systemMessageHTML votingSuspendedHTML "info" />
    </#if>
</#macro>


<#-- 
 * votingEnded
 * 
 * Voting is ended. 6 month voting period is exceeded.
 *
 * Requirements:
 * - Initiative state is ACCEPTED
 * - Voting is started in the first place and then also ended.
-->
<#macro votingEnded>
    <#if votingInfo?? && votingInfo.votingStarted && votingInfo.votingEnded>
        <#assign votingEndedHTML>                
            <#assign endDate><#if initiative.startDate??><@u.localDate initiative.endDate /></#if></#assign>
            <@u.message key="initiative.votingEnded" args=[endDate] />
        </#assign>
        <@u.systemMessageHTML votingEndedHTML "info" />
    </#if>
</#macro>

<#-- 
 * supportStatementsRemoved
 * 
 * Support votes are removed from this service. Support vote counts are still visible.
 *
-->
<#macro supportStatementsRemoved>
<#if initiative.supportStatementsRemoved??>
    <#assign supportStatementsRemovedHTML>                
        <#assign supportStatementsRemovedDate><@u.localDate initiative.supportStatementsRemoved /></#assign>
        <@u.message key="initiative.supportStatementsRemoved" args=[supportStatementsRemovedDate] />
    </#assign>
    <@u.systemMessageHTML html=supportStatementsRemovedHTML type="info" cssClass="printable" />
</#if>
</#macro>


 </#escape> 
 
 