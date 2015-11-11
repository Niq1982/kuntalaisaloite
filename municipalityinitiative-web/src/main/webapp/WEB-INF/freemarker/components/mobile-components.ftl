<#import "/spring.ftl" as spring />
<#import "layout.ftl" as l />
<#import "utils.ftl" as u />
<#import "forms.ftl" as f />
<#import "elements.ftl" as e />

<#escape x as x?html>

<#macro mobileSearch >
    <div class="open-filters"><@u.message "mobile.open.search.filters"></@u.message> <span class="arrow"></span> </br>
        <#if currentMunicipalities?? && currentMunicipalities.present && currentMunicipalities.value?size gt 0>
            <@u.printMunicipalities currentMunicipalities.value />
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
                </br>

                <h3><@u.message "mobile.type"/></h3>

                <@f.mobileCheckBox path=currentSearch.type  prefix="type.all" name="type" value="all" id="all"  />
                <@f.mobileCheckBox path=currentSearch.type  prefix="type.normal" name="type" value="normal" id="normal"  />
                <@f.mobileCheckBox path=currentSearch.type  prefix="type.council" name="type" value="council" id="council"  />
                <@f.mobileCheckBox path=currentSearch.type  prefix="type.citizen" name="type" value="citizen" id="citizen"  />
                </br>
                <h3><@u.message "mobile.show"/></h3>
                <@f.mobileCheckBox path=currentSearch.show  prefix="show.all" name="show" value="all" id="show-all"  />
                <@f.mobileCheckBox path=currentSearch.show  prefix="show.collecting" name="show" value="collecting" id="collecting"  />
                <@f.mobileCheckBox path=currentSearch.show  prefix="show.sent" name="show" value="sent" id="sent"  />

                <input class="run-search-mobile" type="submit" value="OK">

            </form>
        </div>
    </div>

</#macro>

<#macro frontPageLink>
<div class="mobile-links-holder">
    <a href="${urls.search()}" class="front-page-links-mobile"><@u.message "front.bigLinkMobile" /></a>
    <a href="${urls.help(HelpPage.ORGANIZERS.getUri(locale))}" class="front-page-links-mobile"><@u.message "front.bigLinkMobile2" /></a>
</div>
</#macro>

<#macro mobileFrontPageImageContainer>
    <div class="image-container-mobile">
        <#if requestMessages?? && (requestMessages?size > 0)>
        <@u.frontpageRequestMessage requestMessages />
    </#if>
    </div>

</#macro>


</#escape>
