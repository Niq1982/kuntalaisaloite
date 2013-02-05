<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />

<#escape x as x?html> 

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
        <br /><@u.message "initiative.state.collecting" />
    </span>

</#assign>

<#assign bottomContribution>

    <#-- TODO: As component -->
    <#--
     * Show participants
    -->
    <div id="participants" class="view-block public">
        <div class="initiative-content-row last">

            <h2><@u.message "participants.title" /></h2>
            <span class="user-count-total">${participantCount.total!""}</span>
            <br class="clear" />

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
    
    <#--
     * Show management block
    -->
    <div class="view-block public">
        <h2><@u.message "sendToMunicipality.title" /></h2>
        
        <#-- Participate form errors summary -->
        <div class="input-block-content no-top-margin">    
            <@u.errorsSummary path="sendToMunicipality.*" prefix="sendToMunicipality."/>
        </div>
        
        <#if !hasErrors>
            <div class="js-send-to-municipality hidden">
                <#assign href="#" />
                <p><@u.messageHTML key="sendToMunicipality.description" args=[href] /></p>
                <button type="submit" name="action-send" class="small-button"><span class="small-icon mail"><@u.message "action.send" /></span></button>
            </div>
        </#if>

        <div id="send-to-municipality-form" class="send-to-municipality cf js-send-to-municipality-form <#if !hasErrors>js-hide</#if>">

            <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-send" class="sodirty">        
                
                <div class="input-block-content <#if !hasErrors>no-top-margin</#if>">
                    <@f.textarea path="sendToMunicipality.comment" required="" optional=false />
                </div>
                
                <div class="input-block-content">
                    <@u.systemMessage path="sendToMunicipality.contactInfo.description" type="info" showClose=false />  
                </div>

                <div class="input-block-content">
                    <#if !hasErrors>
                        <div id="contact-prefilled" class="manage-block hidden">
                            <a href="#" id="update-contact-info" class="small-button"><span class="small-icon edit">Muokkaa yhteystietoja</span></a>
                            
                            <div class="input-header">
                                <@u.message "sendToMunicipality.contactInfo" />
                            </div>
                            
                            <p>
                                <#if sendToMunicipality.contactInfo.name??><strong>${sendToMunicipality.contactInfo.name!""}</strong><br/></#if>
                                <#if sendToMunicipality.contactInfo.email??>${sendToMunicipality.contactInfo.email!""}<br/></#if>
                                <#if sendToMunicipality.contactInfo.address??>${sendToMunicipality.contactInfo.address!""}<br/></#if>
                                <#if sendToMunicipality.contactInfo.phone??>${sendToMunicipality.contactInfo.phone!""}</#if>
                            </p>
                        </div>
                    </#if>
                    
                    <div id="contact-update-fields" class="manage-block js-contact-update-fields <#if !hasErrors>js-hide</#if>">
                        <#if !hasErrors>
                            <a href="#" id="close-update-contact-info" class="small-button hidden"><span class="small-icon cancel">Peruuta muokkaus</span></a>
                        </#if>
                        
                        <@f.contactInfo path="sendToMunicipality.contactInfo" mode="full" showName=true />
                    </div>
                </div>
                
                <div class="input-block-content">
                    <button type="submit" name="action-send" class="small-button"><span class="small-icon mail"><@u.message "action.send" /></span></button>
                    <#if !hasErrors>
                        <a href="${springMacroRequestContext.requestUri}#participants" class="push close hidden"><@u.message "action.cancel" /></a>
                    <#else>
                        <a href="${urls.management(initiative.id, initiative.managementHash)}" class="push"><@u.message "action.cancel" /></a>
                    </#if>
                </div>
                <br/><br/>

            </form>
        </div>
    </div>

</#assign>

<#assign modalData>
    
    <#-- TODO:
     *  1. Fix namelist columns in IE
     *  2. Show list for NOSCRIPT users
     *
    -->
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