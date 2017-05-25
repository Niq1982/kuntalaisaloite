<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />
<#import "components/progress.ftl" as prog />
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

    <#if user.hasRightToInitiative(initiative.id) && !initiative.sent>
        <@u.returnPrevious urls.management(initiative.id) "link.to.managementView" />
    <#else>
        <@u.returnPrevious url=urls.search() + "?orderBy=latestSent&show=sent" labelKey="link.to.searchView" useJsBack=true />
    </#if>

    <@e.initiativeTitle initiative />

    <@prog.progress initiative />

    <#if decisionInfo.present >
        <@e.decisionBlock decisionInfo.get() />
    </#if>

    <#-- VIEW BLOCKS -->
    <div class="view-block public first">
        <@e.initiativeView initiative />
    </div>

    <#assign canFollow = ( initiative.state == InitiativeState.PUBLISHED && !initiative.decisionDate.present && followEnabled) />
    <#assign showFollowForm = canFollow && (RequestParameters['formError']?? && RequestParameters['formError'] == "follow") />

    <div class="view-block public <#if !canFollow>last</#if>">
        <div class="initiative-content-row last">
            <h2><@u.message key="initiative.authors.title" args=[1] /></h2>
            <@e.initiativeAuthor authors />
        </div>
    </div>

    <#if canFollow>
        <div class="view-block public last" >
            <h2><@u.message key= "followInitiative.h2.title"  /></h2>
            <div class="initiative-content-row last">
                <@e.follow />
            </div>
        </div>
    </#if>


    <#if user.hasRightToInitiative(initiative.id) && !initiative.sent>
        <@u.returnPrevious urls.management(initiative.id) "link.to.managementView" />
    <#else>
        <@u.returnPrevious url=urls.search() + "?orderBy=latestSent&show=sent" labelKey="link.to.searchView" useJsBack=true />
    </#if>
<#--
   * Social media buttons
  -->
    <#if initiative.state == InitiativeState.PUBLISHED>
        <@some.some pageTitle=initiative.name!"" />
    </#if>



    <#assign followInitiativeFormHTML>
        <@compress single_line=true>
            <@u.message key="followSent.text"/>
        <form action="${springMacroRequestContext.requestUri}?formError=follow" method="POST">
            <input type="hidden" name="CSRFToken" value="${CSRFToken}"/>

            <div class="input-block-content">
                <@f.textField path="followInitiative.participantEmail" required="" optional=true cssClass="large" maxLength=InitiativeConstants.CONTACT_NAME_MAX />
            </div>
            <button id="participate" type="submit"  value="true" name="action-follow" class="small-button"><span class="small-icon save-and-send"><@u.message "action.save" /></span></button>
            <a href="${springMacroRequestContext.requestUri}" class="push close"><@u.message "action.cancel" /></a>
        </form>
        </@compress>
    </#assign>

    <@u.modalTemplate />
    <@u.jsMessageTemplate />

    <script type="text/javascript">
        var modalData = {};
        modalData.followInitiative = function(){
            return [{
                title: '<@u.message "followInitiative.title"/>',
                content: '<#noescape>${followInitiativeFormHTML?replace("'","&#39;")}</#noescape>'
            }]
        };
        <#if showFollowForm>
        modalData.followFormAutoLoad = function() {
            return [{
                title:     '<@u.message "followInitiative.title"/>',
                content:   '<#noescape>${followInitiativeFormHTML?replace("'","&#39;")}</#noescape>'
            }]
        };
        </#if>
    </script>

</@l.main>

</#escape> 