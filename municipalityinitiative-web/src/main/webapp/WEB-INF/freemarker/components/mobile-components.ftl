<#import "/spring.ftl" as spring />
<#import "layout.ftl" as l />
<#import "utils.ftl" as u />
<#import "forms.ftl" as f />
<#import "elements.ftl" as e />

<#escape x as x?html>

<#macro mobileSearch >
    <div class="open-filters"><b><@u.message "mobile.open.search.filters" /></b> <span class="arrow"></span> </br>
        <@u.message "mobile.filters.selected"/>&nbsp;
        <#if currentMunicipalities?? && currentMunicipalities.present && currentMunicipalities.get()?size gt 0>
            <@u.printMunicipalities currentMunicipalities.get() />
        <#else>
            <@u.message "mobile.allMunicipalities" />
        </#if>

        <#if currentSearch.type != "all">
            <br/><@u.message "type."+ currentSearch.type + ".mobile.checkbox"/><#if currentSearch.show != "all">,</#if>
        </#if>
        <#if currentSearch.show != "all">
            <#if currentSearch.type == "all">
                <br/>
            </#if>
            <@u.message "show."+ currentSearch.show + ".mobile.checkbox"/>
        </#if>


    </div>
    <div class="search-options-mobile">
        <div class="search-parameters-wrapper">
            <form action="${springMacroRequestContext.requestUri}" method="get">
                <h3><@u.message "mobile.municipalities"/></h3>

                <@f.municipalitySelect path="currentSearch.municipalities" options=municipalities required="" cssClass="" showLabel=false defaultOption="currentSearch.municipality.all" allowSingleDeselect=true onlyActive=true multiple=true id="mobileSelection "/>

                <br/>

                <h3><@u.message "mobile.type"/></h3>

                <@f.mobileCheckBox path=currentSearch.type  prefix="type.all" name="type" value="all" id="all"  />
                <@f.mobileCheckBox path=currentSearch.type  prefix="type.normal" name="type" value="normal" id="normal"  />
                <@f.mobileCheckBox path=currentSearch.type  prefix="type.citizen" name="type" value="citizen" id="citizen"  />
                <br/>
                <h3><@u.message "mobile.show"/></h3>
                <@f.mobileCheckBox path=currentSearch.show  prefix="show.all" name="show" value="all" id="show-all"  />
                <@f.mobileCheckBox path=currentSearch.show  prefix="show.collecting" name="show" value="collecting" id="collecting"  />
                <@f.mobileCheckBox path=currentSearch.show  prefix="show.sent" name="show" value="sent" id="sent"  />

                <br/>
                <h3><@u.message "mobile.sort" /></h3>
                <@f.mobileCheckBox path=currentSearch.orderBy prefix="orderBy.latest" name="orderBy" value="latest" id="latest"  />
                <@f.mobileCheckBox path=currentSearch.orderBy prefix="orderBy.oldest" name="orderBy" value="oldest" id="oldest"  />
                <@f.mobileCheckBox path=currentSearch.orderBy prefix="orderBy.mostParticipants" name="orderBy" value="mostParticipants" id="mostParticipants"  />
                <@f.mobileCheckBox path=currentSearch.orderBy prefix="orderBy.leastParticipants" name="orderBy" value="leastParticipants" id="leastParticipants"  />

                <input class="run-search-mobile" type="submit" value="OK">

            </form>
        </div>
    </div>

</#macro>


<#macro mobileFrontPageImageContainer imageNumber>
    <a href="${urls.help(HelpPage.ORGANIZERS.getUri(locale))}" class="image-container-mobile">
        <div class="mobile-image image-${imageNumber}-1" ></div>
        <div class="mobile-image image-${imageNumber}-2" ></div>
        <div class="mobile-image image-${imageNumber}-3" ></div>
        <div class="mobile-big-link-container">
            <span class="front-page-links-mobile"><span class="own-line"><@u.message "front.bigLink.Mobile1" /></span><span class="own-line"><@u.message "front.bigLink.Mobile2" /></span><strong><@u.message "front.bigLink.Mobile3" /></strong><span class="arrow"></span></span>
        </div>
        <#if requestMessages?? && (requestMessages?size > 0)>
        <@u.frontpageRequestMessage requestMessages />
    </#if>
    </a>

</#macro>

<#macro participantsBlock participantCount admin=false>

    <div id="participants" class="view-block public participants mobile last">
        <h2><@u.message key="initiative.participation.title" args=[participantCount.total] /></h2>

    <#--
     * Do NOT show participate button:
     *  - when modal request message is showed
     *  - when participate form is showed (RequestParameter for NOSCRIPT)
     *  - when the form has validation errors
     *  - when sent to municipality (initiative.sentTime.present)
    -->
    <#assign showParticipateForm = (RequestParameters['formError']?? && RequestParameters['formError'] == "participate")
    || (RequestParameters['participateForm']?? && RequestParameters['participateForm'] == "true") />

    <#--
     * Show participant counts and participate form
     *
     * - Hide when not published. OM sees this view in REVIEW state.
    -->

    <div class="initiative-content-row last">
        <@participantsMobile formHTML=participateFormHTML showForm=showParticipateForm admin=admin />
    </div>
</div>

</#macro>

#--
* participants
*
* Generates participants block with optional participate button and form
*
* @param formHTML is the markup for the form for NOSCRIPT-users
* @param showForm is boolean for toggling form visibility
* @param admin is boolean for toggling participate button and participant manage -link
-->
<#macro participantsMobile formHTML="" showForm=true admin=false>
    <#assign participateSuccess=false />
    <#list requestMessages as requestMessage>
        <#if requestMessage == RequestMessage.PARTICIPATE>
            <#assign participateSuccess=true />
        </#if>
    </#list>

    <#if user.hasRightToInitiative(initiative.id) && admin>
        <h3>
        <span class="switch-view"><a href="${urls.participantListManage(initiative.id)}" class="trigger-tooltip" title="<@u.message "manageParticipants.tooltip" />"><@u.message "manageParticipants.title" /></a></span>
        </h3>
    </#if>

    <#if  !initiative.sentTime.present && !user.hasRightToInitiative(initiative.id)>
        <#if user.hasParticipatedToInitiative(initiative.id)>
            <@u.systemMessage path="warning.already.participated" type="warning" />
        <#elseif initiative.verifiable && user.isVerifiedUser() && !user.municipalityOkForVerifiedParticipation(initiative.id, initiative.municipality)>
            <@u.systemMessage path="warning.participant.too.young.to.verified.participation" type="warning" />
        <#elseif initiative.verifiable && user.isVerifiedUser() && user.tooYoungForVerifiedParticipation()>
            <@u.systemMessage path="warning.too.young.to.verified.participation" type="warning" />
        <#elseif initiative.verifiable && ((user.isVerifiedUser() && !user.homeMunicipality.present) || !user.isVerifiedUser()) >
            <@u.systemMessage path="participate.verifiable.info"+user.isVerifiedUser()?string(".verifiedUser","") type="info" />
        </#if>
    </#if>
    <#if initiative.sentTime.present>
        <@u.systemMessage path="participate.sentToMunicipality" type="info" />
    </#if>

    <@participantInformationMobile/>

    <#if !admin>
    <@e.participateButton participateSuccess showForm/>
    </#if>

    <#-- NOSCRIPT participate -->
    <#if showForm>
        <#noescape><noscript>
            <div id="participate-form" class="form-container cf top-margin">
                <h3><@u.message "participate.title" /></h3>
            ${formHTML!""}
            </div>
        </noscript></#noescape>
    </#if>


</#macro>


<#macro participantInformationMobile>
    <div class="participants-block">
        <span class="user-count-total">${participantCount.total+initiative.externalParticipantCount}</span>
        <span><@u.message "participants"/></span>
    </div>
    <div class="participants-block separate">
            <span class="user-count-sub-total">
                <span class="total-names-names"><@u.message key="participantCount.thisService" args=[participantCount.total]/></span><br/>
                <span class="private-names left-padding"><@u.message key="participantCount.citizen" args=[initiative.participantCountCitizen]/></span><br/>
                <#if (participantCount.publicNames > 0)><span class="public-names left-padding"><a class="trigger-tooltip" href="${urls.participantList(initiative.id)}" title="<@u.message key="participantCount.publicNames.show"/>"><@u.message key="participantCount.publicNames" args=[participantCount.publicNames] /></a></span><br/></#if>
                <#if (initiative.externalParticipantCount > 0)><span class="private-names"><@u.message key="participantCount.externalNames" args=[initiative.externalParticipantCount]/></span></#if>
            </span>
    </div>
</#macro>

<#macro mobileSearchResult initiative manage=false>
    <#if manage>
        <a href="${urls.management(initiative.id)}" class="search-result-mobile">
    <#else>
        <a href="${urls.view(initiative.id)}" class="search-result-mobile">
    </#if>
            <div class="search-result-info-mobile">
                <span class="date"> <@u.localDate initiative.stateTime!"" /></span>
                <span class="municipality-search-result">${initiative.municipality.getName(locale)!""}</span>
                <#if !initiative.public>
                    <span class="state"><@u.message "searchResults.notPublic" /></span>
                <#elseif !initiative.sentTime.present>
                    <span class="state"><@u.message "initiative.state.collecting" /></span>
                <#else>
                    <#assign sentTime><@u.localDate initiative.sentTime.get()!"" /></#assign>
                    <span class="state"><@u.message key="initiative.date.sent" args=[sentTime] /></span>
                </#if>
                <span class="title"><span class="name"><@u.limitStringLength initiative.name!"" 150 /></span></span>

                <span class="initiative-type">
                    <@u.message "initiative.initiativeType."+initiative.type />
                </span>
            </div>

           <span class="participants">
                <span class="participants-container">
                    <#if !initiative.public>
                        <span class="no-participants"><@u.message "searchResults.notPublic" /></span>
                    <#elseif !initiative.collaborative>
                        <span class="no-participants"><@u.message "searchResults.notCollaborative" /></span>
                    <#else>
                        <span class="participant-count">${initiative.participantCount!""}</span>
                    </#if>
                </span>
            </span>
    </a>
</#macro>

</#escape>
