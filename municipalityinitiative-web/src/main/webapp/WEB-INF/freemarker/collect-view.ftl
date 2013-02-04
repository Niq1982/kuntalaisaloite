<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

<#escape x as x?html> 

<#assign participateFormHTML>
<@compress single_line=true>

    <#-- Participate form errors summary -->    
    <@u.errorsSummary path="participant.*" prefix="participant."/>

    <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-participate" class="sodirty <#if hasErrors>has-errors</#if>">

    <input type="hidden" name="municipality" value="${initiative.municipalityId}"/>
    
    <div class="input-block-content no-top-margin flexible">
        <@u.systemMessage path="initiative.ownDetails.description" type="info" showClose=false />  
    </div>
    
     <div class="input-block-content flexible">
        <@f.textField path="participant.participantName" required="required" optional=false cssClass="large" maxLength="512" />
        <@f.formCheckbox path="participant.showName" checked=true />
    </div>
    
    <div class="input-block-content flexible">
        <@f.municipalitySelect path="participant.homeMunicipality" options=municipalities required="required" cssClass="municipality-select" preSelected=initiative.municipalityId />
    </div>

    <div id="franchise" class="input-block-content flexible">
        <@f.radiobutton path="participant.franchise" required="required" options={"true":"initiative.franchise.true", "false":"initiative.franchise.false"} attributes="" />
    </div>
    
    <div id="municipalMembership" class="js-hide hidden">
        <div class="input-block-content flexible">
            <#assign href="#" />
            <@u.systemMessage path="participant.municipalMembership.info" type="info" showClose=false args=[href] />
        </div>
        <div class="input-block-content flexible">
            <@f.radiobutton path="participant.municipalMembership" required="" options={"true":"initiative.municipalMembership.true", "false":"initiative.municipalMembership.false"} attributes="" header=false />
        </div>
        <div class="input-block-content flexible js-hide is-not-member">
            <@u.systemMessage path="warning.participate.notMember" type="warning" showClose=false />
        </div>
    </div>

    
    <#-- Do not use NOSCRIPT here as it will be descendant of another NOSCRIPT. -->
    <div class="input-block-content js-hide">
        <#-- Hidden field for NOSCRIPT users. This element is removed with JS. -->
        <input type="hidden" name="municipalMembership" value="true" class="js-remove" />
        <label>
            <#assign href="#" />
            <input type="checkbox" name="placeholder" id="placeholder" checked="checked" disabled="disabled" /><span class="label"><@u.messageHTML key="initiative.checkMembership" args=[href] /></span>
        </label>
    </div>
    
    <div class="input-block-content flexible">
        <button id="submit-participate" type="submit" name="save" value="true" class="small-button" ><span class="small-icon save-and-send"><@u.message "action.save" /></span></button>
        <a href="${springMacroRequestContext.requestUri}#participants" class="push close"><@u.message "action.cancel" /></a>
    </div>
    
    </form>

</@compress>
</#assign>

<#-- TODO: Member lists as component -->
<#assign participantListFranchiseHTML>
<@compress single_line=true>
<#-- Testing modal with large list -->
<#--<div class="css-cols-3"><ul class="no-style"><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li><li>Matti Meikäläinen</li></ul></div>-->

    <#if participants??>
        <#list participants.franchise as participant>
            <#if participant_index == 0><ul class="participants no-style"></#if>
                <li>${participant}</li>
            <#if !participant_has_next></ul></#if>
        </#list>
    </#if>

</@compress>
</#assign>

<#assign participantListNoFranchiseHTML>
<@compress single_line=true>
    <#if participants??>
        <#list participants.noFranchise as participant>
            <#if participant_index == 0><ul class="participants no-style"></#if>
                <li>${participant}</li>
            <#if !participant_has_next></ul></#if>
        </#list>
    </#if>
</@compress>
</#assign>


<#assign topContribution>

    <#--
     * Initiative date and state
    -->    
    
    <span class="extra-info">
        <#if initiative.createTime??>
            <#assign createTime><@u.localDate initiative.createTime /></#assign>
            <@u.message key="initiative.date.create" args=[createTime] />
        </#if>
        <br />

        <#if initiative.sentTime.present>
            <#assign sentTime><@u.localDate initiative.sentTime.value /></#assign>
            <@u.message key="initiative.date.sent" args=[sentTime] />
        <#else>
            <@u.message "initiative.state.collecting" />
        </#if>
    </span>

</#assign>

<#assign bottomContribution>

    <#-- TODO: Extra details when collecting participants. -->
    <#--
     * Show participants
    -->
    <div id="participants" class="view-block public last">
        <div class="initiative-content-row last">

            <h2><@u.message "participants.title" /></h2>
            <span class="user-count-total">${participantCount.total!""}</span>
            
            <#--
             * Disable participate button:
             *  - when modal request message is showed
             *  - when participate form is showed (NOSCRIPT)
             *  - when the form has validation errors
            -->
            <#if requestMessages?? && !(requestMessages?size > 0) && !((hasErrors?? && hasErrors) || (RequestParameters['participateForm']?? && RequestParameters['participateForm'] == "true"))>
                <div class="participate">
                    <a class="small-button js-participate" href="?participateForm=true#participate-form"><span class="small-icon save-and-send"><@u.message "action.participate" /></span></a>
                    <@u.link href="#" labelKey="action.participate.infoLink" cssClass="push" />
                </div>
            </#if>
            <br class="clear">

            <#if (hasErrors?? && hasErrors) || (RequestParameters['participateForm']?? && RequestParameters['participateForm'] == "true")>
                <#noescape><noscript>
                    <div id="participate-form" class="participate-form cf top-margin">
                        <h3><@u.message "participate.title" /></h3>
                        ${participateFormHTML!""}
                    </div>
                </noscript></#noescape>
            </#if>

            <div class="top-margin cf">
                <div class="column col-1of2">
                    <p><@u.message key="participantCount.rightOfVoting.total" args=[initiative.municipalityName!""] /><br />
                    <span class="user-count">${participantCount.rightOfVoting.total!""}</span><br />
                    <#if (participantCount.rightOfVoting.total > 0)>
                        <#if (participantCount.rightOfVoting.publicNames > 0)><a class="trigger-tooltip js-show-franchise-list" href="#" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.rightOfVoting.publicNames!""] /></a><br /></#if>
                        <#if (participantCount.rightOfVoting.privateNames > 0)><@u.message key="participantCount.privateNames" args=[participantCount.rightOfVoting.privateNames!""] /></p></#if>
                    </#if>
                </div>
                <div class="column col-1of2 last">
                    <p><@u.message key="participantCount.noRightOfVoting.total" args=[initiative.municipalityName!""] /><br />
                    <span class="user-count">${participantCount.noRightOfVoting.total!""}</span><br>
                    <#if (participantCount.noRightOfVoting.total > 0)>
                        <#if (participantCount.noRightOfVoting.publicNames > 0)><a class="trigger-tooltip js-show-no-franchise-list" href="#" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.noRightOfVoting.publicNames!""] /></a><br></#if>
                        <#if (participantCount.noRightOfVoting.privateNames > 0)><@u.message key="participantCount.privateNames" args=[participantCount.noRightOfVoting.privateNames!""] /></p></#if>
                    </#if>
                </div>
            </div>
            
        </div>     
    </div>

</#assign>

<#assign modalData>

    <#-- Modal: Participate initiative -->
    <#if participateFormHTML??>    
        modalData.participateForm = function() {
            return [{
                title:      '<@u.message "participate.title" />',
                content:    '<#noescape>${participateFormHTML?replace("'","&#39;")}</#noescape>'
            }]
        };
        
        <#-- Autoload modal if it has errors -->
        <#if hasErrors>
        modalData.participateFormInvalid = function() {
            return [{
                title:      '<@u.message "participate.title" />',
                content:    '<#noescape>${participateFormHTML?replace("'","&#39;")}</#noescape>'
            }]
        };
        </#if>
    </#if>
    
    <#-- TODO: Fix namelist columns in IE. -->
    modalData.participantListFranchise = function() {
        return [{
            title:      '<@u.message key="participantCount.rightOfVoting.total" args=[initiative.municipalityName!""] />',
            content:    '<#noescape><div class="css-cols-3 scrollable">${participantListFranchiseHTML}</div><br/><a href="index.html" class="small-button close"><@u.message "action.close" /></a></#noescape>'
        }]
    };
    
    modalData.participantListNoFranchise = function() {
        return [{
            title:      '<@u.message key="participantCount.noRightOfVoting.total" args=[initiative.municipalityName!""] />',
            content:    '<#noescape><div class="css-cols-3 scrollable">${participantListNoFranchiseHTML}</div><br/><a href="index.html" class="small-button close"><@u.message "action.close" /></a></#noescape>'
        }]
    };
    
</#assign>

<#include "initiative.ftl" />

</#escape> 