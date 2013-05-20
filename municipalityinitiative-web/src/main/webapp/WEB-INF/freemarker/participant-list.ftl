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
<@l.main "page.initiative.participants" initiative.name!"">

    <h1 class="name">${initiative.name!""}</h1>
    
    <div class="municipality">${initiative.municipality.getName(locale)}</div>
    
    <@e.stateInfo initiative />
    
    <@returnPrevious />

    <#-- VIEW BLOCKS -->
    <div class="view-block single public cf">
        <h2><@u.message key="participantList.title" args=[participantCount.publicNames!""] /></h2>
        
        <@participantList participants />
        
        <br/><h2>Hallinta</h2>
        <@participantListManage participants />


        <#assign deleteParticipant>
            <@compress single_line=true>
                <form action="${springMacroRequestContext.requestUri}" method="POST" id="delete-participant-form">
                    <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>
                    
                    <input type="hidden" name="participantId" id="participantId" value=""/>
                    
                    <#-- TODO: add here selected participant -->
                    <ul id="selected-participant" class="no-style"></ul>
                    
                    <div class="input-block-content">
                        <button type="submit" name="${UrlConstants.ACTION_DELETE_PARTICIPANT}" id="modal-${UrlConstants.ACTION_DELETE_PARTICIPANT}" value="${UrlConstants.ACTION_DELETE_PARTICIPANT}" class="small-button"><span class="small-icon save-and-send"><@u.message "action.deleteParticipant.confirm" /></button>
                        <a href="${urls.participantList(initiative.id)}#delete-participant" class="push close"><@u.message "action.cancel" /></a>
                    </div>
                </form>
            </@compress>
        </#assign>
    </div>
    
    <@returnPrevious />
    
    <#--
     * Management VIEW modals
     * 
     * Uses jsRender for templating.
     * Same content is generated for NOSCRIPT and for modals.
     *
     * Modals:
     *  Delete participant
     *
     * jsMessage:
     *  Warning if cookies are disabled
    -->
    <@u.modalTemplate />
    <@u.jsMessageTemplate />
    
    <script type="text/javascript">
        var modalData = {};
        
        <#-- Modal: Request messages. Check for components/utils.ftl -->
        <#if deleteParticipant??>    
            modalData.deleteParticipant = function() {
                return [{
                    title:      '<@u.message "deleteParticipant.title" />',
                    content:    '<#noescape>${deleteParticipant?replace("'","&#39;")}</#noescape>'
                }]
            };
        </#if>

        var messageData = {};

        <#-- jsMessage: Warning if cookies are not enabled -->
        messageData.warningCookiesDisabled = function() {
            return [{
                type:      'warning',
                content:    '<h3><@u.message "warning.cookieError.title" /></h3><div><@u.messageHTML key="warning.cookieError.description" args=[urls.participantList(initiative.id)] /></div>'
            }]
        };
    </script>
</@l.main>

<#-- 
 * participantList
 *
 * Prints public participant name list with municipality and participate date.
 *
 * @param participants is participants object list
-->
<#macro participantList participants>
    <#list participants as participant>
        <#if participant_index == 0><ul class="participant-list no-style"></#if>
            <li><span class="date"><@u.localDate participant.participateDate!"" /></span> <span class="name-container"><span class="name">${participant.name!""} - ${participant.id}</span> <span class="home-municipality">- ${participant.homeMunicipality.getName(locale)!""}</span></span></li>
        <#if !participant_has_next></ul></#if>
    </#list>
</#macro>

<#-- 
 * participantListManage
 *
 * Prints public participant name list with municipality and participate date.
 *
 * @param participants is participants object list
-->
<#macro participantListManage participants>
    <#list participants as participant>

        <#if participant_index == 0><ul class="participant-list no-style"></#if>
            <li><span class="date"><@u.localDate participant.participateDate!"" /></span> <span class="name-container"><span class="name">${participant.name!""}</span> <span class="home-municipality"><span class="bull">&bull;</span> ${participant.homeMunicipality.getName(locale)!""}</span><span class="bull">&bull;</span>
            <#if !participant.isAuthor()>
                <a  href="#" class="js-delete-participant"
                    data-id="${participant.id!""}"
                    data-date="<@u.localDate participant.participateDate!"" />"
                    data-name="${participant.name!""}"
                    data-municipality="${participant.homeMunicipality.getName(locale)!""}">Poista</a></span></li>

            </#if>
        <#if !participant_has_next></ul></#if>
    </#list>
</#macro>

<#-- 
 * participantListColumns
 *
 * Same as participantList but it has an option to create columns
 *
 * @param participants is participants object list
-->
<#macro participantListColumns participants>
    <#assign participantCount = participants?size />
    <#assign cue = (participantCount/2)?ceiling />

    <#assign columns=1 />    
    <#if ( participantCount > 19)>
        <#assign columns=2 />
    </#if>

    <div class="column col-1of2">

        <#list participants as participant>
            <#if participant_index == 0><ul class="participant-list no-style"></#if>
                <li><span class="date"><@u.localDate participant.participateDate!"" /></span> <span class="name-container"><span class="name">${participant.name!""}</span> <span class="home-municipality">- ${participant.homeMunicipality.getLocalizedName(locale)!""}</span></span></li>
                
                <#if columns == 2 && participant_index == cue>
                    </ul></div>
                    <div class="column col-1of2 last"><ul class="participant-list no-style">
                </#if>
                
            <#if !participant_has_next></ul></#if>
        </#list>
            
    </div>
    <br class="clear" />

</#macro>

<#-- 
 * returnPrevious
 *
 *  If request header referer equals management
 *      previousPageURI is the management URI
 *  Otherwise
 *      previousPageURI is the public view URI
-->
<#macro returnPrevious>
    <#if previousPageURI == urls.getManagement(initiative.id)>
        <p><a href="${previousPageURI}">&laquo; <@u.message "participantList.return.management" /></a></p>
    <#else>
        <p><a href="${previousPageURI}">&laquo; <@u.message "participantList.return.view" /></a></p>
    </#if>
</#macro>


</#escape> 

