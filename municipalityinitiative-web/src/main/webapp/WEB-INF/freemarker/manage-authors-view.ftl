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

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.getName(locale)}</div>
    
    <@e.stateInfo initiative />
    
    <@returnPrevious />

  <div class="view-block single public">
            <h2>Nykyiset vastuuhenkilöt</h2>

            <#list authors as a>

              <div class="author cf">
                  <div class="details">
                      <h4 class="header">${a.contactInfo.name}</h4>
                      <div class="email">${a.contactInfo.email}</div>
                  </div>

                  <div class="invitation">
                      <div class="status">Hyväksytty vastuuhenkilöksi</div>
                  </div>
              </div>

            </#list>
    </div>

    <div class="view-block single public">
            <h2>Avoimet kutsut (vanhenevat minuutissa)</h2>

            <#list invitations as i>

              <div class="author cf">
                  <div class="details">
                      <h4 class="header">${i.name}</h4>
                      <div class="email">${i.email}</div>
                  </div>

                  <div class="invitation">
                  <#if i.expired>
                      <div class="status"><span class="icon-small unconfirmed"></span> Kutsu vanhentunut <a href="#" class="resend-invitation">lähetä kutsu uudelleen</a></div>
                  <#else>
                      <div class="status"><span class="icon-small unconfirmed"></span> Odottaa hyväksyntää <a href="#" class="cancel-invitation">peru kutsu</a></div>
                      <div class="action push">Kutsu lähetetty ${i.invitationTime}</div>
                  </#if>

                  </div>
              </div>

            </#list>

        
        <div class="js-open-block hidden">
            <a class="small-button gray js-btn-open-block" data-open-block="js-block-container" href="#"><span class="small-icon save-and-send"><@u.message "action.addAuthor" /></span></a>
        </div>

        <div class="cf js-block-container js-hide">
            <div class="input-block-content no-top-margin">
                <div class="system-msg msg-info">
                    Kutsu aloitteeseen vastuuhenkilö. Täytä vähintään sähköpostiosoite, sillä kutsu lähetetään antamaasi osoitteeseen.
                </div>

                <@u.errorsSummary path="newInvitation.*" prefix="newInvitation."/>

            </div>
        



            <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-send" class="sodirty">
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                <input type="hidden" name="${UrlConstants.PARAM_MANAGEMENT_CODE}" value="${initiative.managementHash.value}"/>
                
                <div class="input-block-content">
                    <@f.textField path="newInvitation.authorName" required="required" optional=false cssClass="large" maxLength=InitiativeConstants.CONTACT_NAME_MAX key="contactInfo.name" />
                    <@f.textField path="newInvitation.authorEmail" required="required" optional=false cssClass="large" maxLength=InitiativeConstants.CONTACT_EMAIL_MAX key="contactInfo.email" />


                </div>
                
                <div class="input-block-content no-top-margin">
                    <button type="submit" name="${UrlConstants.ACTION_ACCEPT_INITIATIVE}" class="small-button"><span class="small-icon save-and-send"><@u.message "action.sendInvitation" /></span></button>
                    <a href="${springMacroRequestContext.requestUri}#participants" class="push js-btn-close-block hidden"><@u.message "action.cancel" /></a>
                </div>
                <br/><br/>
            </form>
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

