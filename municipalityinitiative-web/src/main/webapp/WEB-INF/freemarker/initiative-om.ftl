<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

<#include "components/management-messages.ftl" />

<#escape x as x?html> 

<#assign stateDate><@u.localDate initiative.stateDate /></#assign>    
<#assign startDate><@u.localDate initiative.startDate /></#assign>

<#assign topContribution>

<#--
 * supportVoteBatch
 * 
 * Defined in management-messages.ftl 
-->
<#noescape>${supportVoteBatch!""}</#noescape>

<#--
 * ACCEPT or REJECT Initiative
 * 
 * Launches a modal-window for confirmation.
 *
 * Initiative state: REVIEW
-->
<#if initiative.state == InitiativeState.REVIEW>
    <#assign initiativeWaitingForOM>
        <#if initiative.startDate.isAfter(initiative.stateDate.toLocalDate())>
            <#assign args=[stateDate,startDate] />
            <p><@u.messageHTML "initiative.waitingForOm" args /></p>
        <#else>
            <#assign args=[stateDate] />
            <p><@u.messageHTML "initiative.waitingForOm.noDate" args /></p>
        </#if>
        
        <a href="#" class="small-button green hidden om-accept-initiative"><span class="small-icon save-and-send"><@u.message "initiative.acceptInitiative.btn" /></span></a>
        <a href="#" class="small-button red hidden om-reject-initiative"><@u.message "initiative.rejectInitiative.btn" /></a>
    </#assign>
    
    <@u.systemMessageHTML initiativeWaitingForOM "info" />
</#if>

<#--
 * Confirm ACCEPT or REJECT Initiative
 * 
 * Modal: confirm ACCEPT or REJECT Initiative
 *
 * Includes NOSCRIPT
-->
<#if managementSettings?? && managementSettings.allowRespondByOM>
    <#-- Modal: Confirm accept initiative -->
    <#assign omAcceptInitiativeHtml>
        <@compress single_line=true>
            <p><@u.message "initiative.acceptInitiative.description" /> <i><@u.text initiative.name /></i></p>
            
            <form action="${springMacroRequestContext.requestUri}" method="POST">
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                <label class="input-header" for="comment"><@u.message "initiative.acceptInitiative.coveringNote" /></label>
                <textarea id="comment" name="comment" maxlength="${InitiativeConstants.STATE_COMMENT_MAX?string("#")}"></textarea>
                <label class="input-header"><@u.message "initiative.acceptanceIdentifier.label" />: <input type="text" required="required" name="acceptanceIdentifier" maxlength="${InitiativeConstants.ACCEPTANCE_IDENTIFIER_MAX?string("#")}" /></label>
                
                <br />

                <#-- Do we need this hidden parameter? -> submit name="action" value="accept-by-om"?!? -->
                <input type="hidden" name="action" value="accept-by-om" />
                <button type="submit" name="${UrlConstants.ACTION_ACCEPT_BY_OM}" value="true" class="small-button green disable-dbl-click-check"><span class="small-icon save-and-send"><@u.message "initiative.acceptInitiative.btn" /></span></button>
                
                <a class="push close"><@u.message "action.cancel" /></a>
            </form>
        </@compress>
    </#assign>
    
    <#-- OM does not need NOSCRIPT-version -->
    <#--<@u.systemMessageHTML omAcceptInitiativeHtml "info" "noscript" />-->
    
    <#-- Modal: Confirm reject initiative -->
    <#assign omRejectInitiativeHtml>
        <@compress single_line=true>
            <p><@u.message "initiative.rejectInitiative.description" /> <i><@u.text initiative.name /></i></p>

            <form action="${springMacroRequestContext.requestUri}" method="POST" >
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                <label class="input-header" for="comment"><@u.message "initiative.acceptInitiative.coveringNote" /></label>
                <textarea id="comment" name="comment" maxlength="4096"></textarea>
                
                <br /><br />
                
                <#-- Do we need this hidden parameter? -> submit name="action" value="reject-by-om"?!? -->
                <input type="hidden" name="action" value="reject-by-om" />
                <button type="submit" name="${UrlConstants.ACTION_REJECT_BY_OM}" value="true" class="small-button red"><@u.message "initiative.rejectInitiative.btn" /></button>
                <a class="push close"><@u.message "action.cancel" /></a>
            </form>
        </@compress>
    </#assign>
    
    <#-- OM does not need NOSCRIPT-version -->
    <#-- <@u.systemMessageHTML omRejectInitiativeHtml "info" "noscript" /> -->
</#if>

<#--
 * removeSupportVotes
 * 
 * Defined in management.ftl 
-->
<#noescape>${removeSupportVotes!""}</#noescape>


</#assign>

<#--
 * Assign the messages also to the bottom of the page.
-->
<#assign bottomContribution>
    <#if initiativeWaitingForOM??>
        <@u.systemMessageHTML initiativeWaitingForOM "info" />
    </#if>
</#assign>

<#include "initiative-management.ftl"/>
</#escape> 

