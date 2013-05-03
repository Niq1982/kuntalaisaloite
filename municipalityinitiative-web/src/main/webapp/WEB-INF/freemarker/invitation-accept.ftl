<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />
<#import "components/forms.ftl" as f />
<#import "components/some.ftl" as some />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.initiative.public" initiative.name!"">

    <#-- PAHASTI VAIHEESSA -->

    <#--
     * invitationAcceptHtml
     * 
     * Accept or decline invitation.
     * Launches a modal-window for confirmation.
     *
     * NOSCRIPT-users gets confirmation form by request parameter 'invitation-decline'.
    -->
    <#assign invitationAcceptHtml>
        <h4>Kutsu</h4>
        
        <p>Ohjetta</p>
        
        <form action="${springMacroRequestContext.requestUri}" method="POST" >
            <input type="hidden" name="invitation" value="${invitationCode!""}"/>
    
            <a href="#" class="small-button green green save-and-send"><span class="small-icon save-and-send"><@u.message "invitation.accept" /></span></a>
            <a href="?invitation-decline=confirm" title="<@u.message "invitation.decline" />" class="small-button gray cancel invitation-decline-confirm"><@u.message "invitation.decline" /></a>
        </form>
    </#assign>
    
    <#if !RequestParameters['invitation-decline']??>
        <@u.systemMessageHTML invitationAcceptHtml "summary" />
    </#if>
    
    
    <#--
     * invitationDeclineConfirmHtml
     * 
     * Modal: Confirm decline invitation.
     *
     * NOSCRIPT-users gets confirmation form by request parameter 'invitation-decline=confirm'.
    -->
    <#assign invitationDeclineConfirmHtml>
        <@compress single_line=true>
        
            <@u.messageHTML "modal.invitationDecline.confirm" />
            <form action="${springMacroRequestContext.requestUri}" method="POST" >
                <input type="hidden" name="CSRFToken" value="${CSRFToken!}"/>
                <input type="hidden" name="invitation" value="${invitationCode!""}"/>
                <button type="submit" name="#" value="<@u.message "invitation.decline" />" class="small-button gray cancel"><@u.message "invitation.decline" /></button>
                <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
            </form>
        
        </@compress>
    </#assign>
    
    <#-- Confirm decline invitation for NOSCRIPT-users -->
    <#if RequestParameters['invitation-decline']?? && RequestParameters['invitation-decline'] == "confirm">
        <noscript>
            <div class="system-msg msg-info">
               <#noescape>${invitationDeclineConfirmHtml}</#noescape>
            </div>
            <br/>
        </noscript>
    </#if> 
        
    
    
    <#--
     * invitationAcceptHtml
     * 
     * Modal for accepting invitation.
     *
     * Includes NOSCRIPT
    -->
    <#assign invitationAcceptHtml>
        <@compress single_line=true>        
            <#--<@u.errorsSummary path="currentAuthor.*" prefix="initiative.currentAuthor."/>-->
            
            <h4 class="header">Kuntalaisaloite</h4>
            <p>${initiative.name!""}</p>
                
            <div class="column">
                <h4 class="header"><@u.message "initiative.currentAuthor.name" /></h4>
                <p>Esitäytetty nimi tähän ja inputti</p>
            </div>
            
            <div class="column last">
                <h4 class="header">Kotikunta</h4>
                <p>Kotikunta valinta</p>
            </div>
            <br class="clear" />
    
            <form action="${springMacroRequestContext.requestUri}" method="POST" >
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                <input type="hidden" name="invitation" value="${invitation.invitationCode!""}"/>
                <input type="hidden" name="action" value="confirm-accept-invitation"/>
                
                <div class="pad margin cf">
                
                    <#--<#assign href>${urls.help(HelpPage.INITIATIVE_STEPS.getUri(locale))}</#assign>
                    <@u.messageHTML key="invitation.accept.confirm.contactDetails" args=[href] />
                    <@f.currentAuthor path="currentAuthor" mode="modal" prefix="initiative" />-->
                </div>
                
                <#--<#assign href>${urls.help(HelpPage.ORGANIZERS.getUri(locale))}</#assign>
                <p><@u.messageHTML key="userConfirmation.invitation" args=[href] /></p>-->
                
                <button type="submit" name="" value="<@u.message "invitation.accept.confirm" />" class="small-button green save-and-send"><span class="small-icon save-and-send"><@u.message "invitation.accept.confirm" /></span></button>
                <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
            </form>                    
        </@compress>
    </#assign>
    
    <@u.systemMessageHTML invitationAcceptHtml "info" "noscript" />


    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.getName(locale)}</div>
    
    <@e.stateInfo initiative />

    <#-- VIEW BLOCKS -->
    <div class="view-block public first">
        <div class="initiative-content-row">
            <@e.initiativeView initiative />
        </div>
        <div class="initiative-content-row last">
            <@e.initiativeAuthor initiative />
        </div>
    </div>

    <#--
     * Social media buttons
    -->
    <#if initiative.state == InitiativeState.PUBLISHED>
        <@some.some pageTitle=initiative.name!"" />
    </#if>

</@l.main>

</#escape> 