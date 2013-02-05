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
        <#assign createTime><@u.localDate initiative.createTime /></#assign>
        <#if initiative.createTime??><@u.message key="initiative.date.create" args=[createTime] /></#if>
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
    <#-- TODO: This should come from bottomContribution. When different views for one person and multiple person initiatives are ready. -->
    
    <div class="view-block public">
        <h2><@u.message "sendToMunicipality.title" /></h2>
        
    <#--<div class="system-msg msg-summary cf">-->
        <div class="js-send-to-municipality hidden">
            <#assign href="#" />
            <p><@u.messageHTML key="sendToMunicipality.description" args=[href] /></p>
            <button type="submit" name="action-send" class="small-button"><span class="small-icon mail"><@u.message "action.send" /></span></button>
        </div>

        <div id="send-to-municipality-form" class="send-to-municipality cf js-send-to-municipality-form js-hide">
            
            <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-send" class="sodirty">        
                
                <div class="input-block-content no-top-margin">
                    <@f.textarea path="sendToMunicipality.comment" required="" optional=false />
                </div>
                
                <div class="input-block-content">
                    <@u.systemMessage path="sendToMunicipality.contactInfo.description" type="info" showClose=false />  
                </div>

                <div class="input-block-content">
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
                    
                    <div id="contact-update-fields" class="manage-block js-contact-update-fields js-hide">
                        <#-- TODO: Link could be created with JS
                        <a href="#" id="close-update-contact-info" class="manage" title="Sulje muokkaus"><span class="small-icon-plain cancel"></a>
                         -->
                        
                        <a href="#" id="close-update-contact-info" class="small-button hidden"><span class="small-icon cancel">Peruuta muokkaus</span></a>
                        
                        <@f.contactInfo path="sendToMunicipality.contactInfo" mode="full" showName=true />
                    </div>
                </div>
                
                <div class="input-block-content">
                    <button type="submit" name="action-send" class="small-button"><span class="small-icon mail"><@u.message "action.send" /></span></button>
                    <a href="${springMacroRequestContext.requestUri}#participants" class="push close"><@u.message "action.cancel" /></a>
                </div>
                <br/><br/>

            </form>
        </div>
    <#--</div>-->
    </div>

</#assign>

<#assign modalData>
    
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