<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />

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

    <#-- VIEW BLOCKS -->
    <div class="view-block single public">
        <h2>Hallinnoi vastuuhenkilöitä</h2>
        
        <div class="author cf">
            <div class="details">
                <h4 class="header">Ville Vastuunkantaja</h4>
                <div class="email">ville.vastuunkantaja@solita.fi</div>
            </div>
            
            <div class="invitation">
                <div class="status"><span class="icon-small unconfirmed"></span> Odottaa hyväksyntää <a href="#" class="cancel-invitation">peru kutsu</a></div>
                <div class="action push">Kutsu lähetetty 3.5.2013</div>
            </div>
        </div>
        
        <div class="author cf">
            <div class="details">
                <h4 class="header">Ville Vastuunkantaja</h4>
                <div class="email">ville.vastuunkantaja@solita.fi</div>
            </div>
            
            <div class="invitation">
                <div class="status"><span class="icon-small confirmed"></span> Kutsu hyväksytty</div>
            </div>
        </div>
        
        <div class="author cf">
            <div class="details">
                <h4 class="header">Ville Vastuunkantaja</h4>
                <div class="email">ville.vastuunkantaja@solita.fi</div>
            </div>
            
            <div class="invitation">
                <div class="status"><span class="icon-small unconfirmed"></span> Kutsu vanhentunut 3.5.2013 <a href="#" class="resend-invitation">lähetä kutsu uudelleen</a></div>
            </div>
        </div>
        
        <div class="author rejected cf">
            <div class="details">
                <h4 class="header">Ville Vastuunkantaja</h4>
                <div class="email">ville.vastuunkantaja@solita.fi</div>
            </div>
            
            <div class="invitation">
                <div class="status"><span class="icon-small unconfirmed"></span> Kutsu hylätty</div>
            </div>
        </div>
        
        <div class="js-open-block hidden">
            <a class="small-button gray js-btn-open-block" data-open-block="js-block-container" href="#"><span class="small-icon save-and-send"><@u.message "action.addAuthor" /></span></a>
        </div>

        <div class="cf js-block-container js-hide">
            <div class="input-block-content no-top-margin">
                <div class="system-msg msg-info">
                    Kutsu aloitteeseen vastuuhenkilö. Täytä vähintään sähköpostiosoite, sillä kutsu lähetetään antamaasi osoitteeseen.
                </div>
            </div>
        

            <form action="${springMacroRequestContext.requestUri}" method="POST" id="form-send" class="sodirty">
                <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                <input type="hidden" name="${UrlConstants.PARAM_MANAGEMENT_CODE}" value="${initiative.managementHash.value}"/>
                
                <div class="input-block-content">
                    <label for="participantEmail" class="input-header">
                        Etu- ja sukunimi
                    </label>
                    <input type="text" maxlength="100" class="large" value="" name="authorName" id="authorName">

                    <label for="participantEmail" class="input-header">
                        Sähköpostiosoite <span class="icon-small required trigger-tooltip"></span>
                    </label>
                    <input type="text" maxlength="100" class="large" value="" name="authorEmail" id="authorEmail">

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

