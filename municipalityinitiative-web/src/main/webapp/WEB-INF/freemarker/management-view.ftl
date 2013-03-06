<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />

<#escape x as x?html> 

<#--
 * Top Info elements for the top section of the initiative's management-view page
-->
<#assign topInfo>
    <#--
     * Warning message for the management page.
    -->
    <@u.systemMessage path="management.warning" type="warning" args=[urls.view(initiative.id)] showClose=false />
</#assign>

<#--
 * Elements for the top section of the initiative's management-view page
-->
<#assign topContribution>

</#assign>

<#--
 * Elements for the bottom section of the initiative's management-view page
-->
<#assign bottomContribution>

    <#--
     * Show participant counts
    -->
    <div id="participants" class="view-block public">
        <div class="initiative-content-row last">

            <h2><@u.message "participants.title" /></h2>
            <span class="user-count-total">${participantCount.total!""}</span>
            <br class="clear" />

            <@e.participantCounts />
            
        </div>     
    </div>
    
    <#--
     * Show management block
    -->
    <div class="view-block public">
        <h2><@u.message "sendToMunicipality.title" /></h2>

        <#-- Check is form has errors -->
        <@spring.bind "sendToMunicipality.*" />
        <#assign hasErrors = false />
        <#if spring.status.error>
            <#assign hasErrors = true />
        </#if>
        
        <#-- Participate form errors summary -->
        <#if hasErrors>
            <div class="input-block-content no-top-margin">    
                <@u.errorsSummary path="sendToMunicipality.*" prefix="sendToMunicipality."/>
            </div>
        <#else>
            <div class="js-send-to-municipality hidden">
                <#assign href="#" />
                <p><@u.messageHTML key="sendToMunicipality.description" args=[href] /></p>
                <a href="#" id="js-send-to-municipality" class="small-button"><span class="small-icon mail"><@u.message "action.send" /></span></a>
            </div>
        </#if>

        <div id="send-to-municipality-form" class="send-to-municipality cf js-send-to-municipality-form <#if !hasErrors>js-hide</#if>">

            <noscript>
                <@f.cookieWarning springMacroRequestContext.requestUri />
            </noscript>

            <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-send" class="sodirty">
                <@f.securityFilters />
                
                <div class="input-block-content <#if !hasErrors>no-top-margin</#if>">
                    <@f.textarea path="sendToMunicipality.comment" required="" optional=false />
                </div>
                
                <div class="input-block-content">
                    <@u.systemMessage path="sendToMunicipality.contactInfo.description" type="info" showClose=false />  
                </div>

                <div class="input-block-content">
                    <@f.contactInfo path="sendToMunicipality.contactInfo" mode="full" showName=true />
                </div>
                
                <div class="input-block-content">
                    <button type="submit" name="action-send" class="small-button"><span class="small-icon mail"><@u.message "action.send" /></span></button>
                    <#if !hasErrors>
                        <a href="${springMacroRequestContext.requestUri}#participants" class="push close hidden"><@u.message "action.cancel" /></a>
                    <#else>
                        <#-- In case of errors cancel-link clears data by refreshing management page. -->
                        <a href="${urls.management(initiative.id, initiative.managementHash.value)}" class="push hidden"><@u.message "action.cancel" /></a>
                    </#if>
                </div>
                <br/><br/>
            </form>
        </div>
    </div>

</#assign>

<#include "initiative.ftl" />

</#escape> 