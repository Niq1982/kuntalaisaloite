<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />
<#import "components/forms.ftl" as f />

<#escape x as x?html> 

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * page = "page.initiative.public" or "page.initiative.unnamed"
 * pageTitle = initiative.name if exists, otherwise empty string
-->
<@l.main "page.initiative.manageAuthors" initiative.name!"">

    <@u.errorsSummary path="newInvitation.*" prefix="newInvitation."/>

    <div class="msg-block">
        <h2>Vastuuhenkilöt</h2>
        <p>Aloitteeseen voidaan lisätä vastuuhenkilöitä. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam aliquam leo velit, non placerat arcu. Nunc sagittis convallis sagittis.</p>
        <p>Morbi ut lectus a nulla euismod tincidunt. Mauris tincidunt augue a ligula faucibus rhoncus. Aenean posuere posuere feugiat</p>
    </div>

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.getName(locale)}</div>
    
    <@e.stateInfo initiative />
    
    <@returnPrevious />

    <div class="view-block ">
        <div class="initiative-content-row last">
            <h2><@u.message "authors.title" /></h2>
    
            <#list authors as a>
                <div class="author cf ${a_has_next?string("","last")}">
                    <div class="details">
                        <h4 class="header">${a.contactInfo.name}</h4>
                        <div class="email">${a.contactInfo.email}</div>
                    </div>
    
                    <div class="invitation">
                        <span class="status"><span class="icon-small confirmed"></span> <@u.message "invitation.accepted" /></span>
                    </div>
                </div>
            </#list>
        </div>
    </div>

    <div class="view-block last">
        <div class="initiative-content-row">
            <h2><@u.message "invitations.title" /> (vanhenevat minuutissa)</h2>

            <#list invitations as i>
                <div class="author cf ${i.rejected?string("rejected","")} ${i_has_next?string("","last")}">
                    <div class="details">
                         <h4 class="header">${i.name}</h4>
                         <div class="email">${i.email}</div>
                    </div>

                    <div class="invitation">
                        <#if i.rejected>
                            <span class="status push"><@u.message "invitation.rejected" /> <@u.localDate i.rejectTime.value /></span>
                        <#elseif i.expired>
                            <span class="status"><span class="icon-small unconfirmed"></span> <@u.message "invitation.expired" /></span>
                            <span class="action push"><a href="#" class=""><@u.message "invitation.resend" /></a></span>
                        <#else>
                            <span class="status"><span class="icon-small unconfirmed"></span> <@u.message "invitation.unconfirmed" /> <span class="bull">&bull;</span> <a href="#" class="cancel-invitation"><@u.message "invitation.cancel" /></a></span>
                            <span class="action push"><@u.message "invitation.sent" /> <@u.localDate i.invitationTime /></span>
                        </#if>
                    </div>
                </div>
            </#list>
            
        </div>
        
        <#-- Bind form for detecting validation errors -->
        <@spring.bind "newInvitation.*" />
        <#assign validationError = spring.status.error />
        
        <div class="initiative-content-row last">
            <div class="js-open-block hidden ${validationError?string("js-hide","")}">
                <a class="small-button gray js-btn-open-block" data-open-block="js-block-container" href="#"><span class="small-icon add"><@u.message "action.addAuthor" /></span></a>
            </div>
    
            <div class="cf js-block-container ${validationError?string("","js-hide")}">
                <h2><@u.message "invitations.addAuthor.title" /></h2>
                
                <div class="input-block-content no-top-margin">
                    <@u.systemMessage path="invitation.description" type="info" showClose=false />
                </div>
    
                <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-send" class="sodirty">
                    <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                    <input type="hidden" name="${UrlConstants.PARAM_MANAGEMENT_CODE}" value="${initiative.managementHash.value}"/>
                    
                    <div class="input-block-content no-top-margin">
                        <@f.textField path="newInvitation.authorName" required="required" optional=false cssClass="large" maxLength=InitiativeConstants.CONTACT_NAME_MAX />
                        <@f.textField path="newInvitation.authorEmail" required="required" optional=false cssClass="large" maxLength=InitiativeConstants.CONTACT_EMAIL_MAX />
                    </div>

                    <div class="input-block-content no-top-margin">
                        <button type="submit" name="${UrlConstants.ACTION_ACCEPT_INITIATIVE}" class="small-button"><span class="small-icon add"><@u.message "action.sendInvitation" /></span></button>
                        <a href="${springMacroRequestContext.requestUri}" class="push ${validationError?string("","js-btn-close-block hidden")}"><@u.message "action.cancel" /></a>
                    </div>
                    <br/><br/>
                </form>
            </div>
    
        </div>
    </div>
    
    <@returnPrevious />
    
</@l.main>

<#-- 
 * returnPrevious
 *
 *  If request header referer equals management
 *      previousPageURI is the management URI
 *  Otherwise
 *      previousPageURI is the public view URI
-->
<#macro returnPrevious>
    <p><a href="${urls.management(initiative.id)}">&laquo; <@u.message "participantList.return.management" /></a></p>
</#macro>



</#escape> 

