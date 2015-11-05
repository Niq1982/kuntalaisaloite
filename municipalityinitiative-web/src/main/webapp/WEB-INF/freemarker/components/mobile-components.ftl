<#import "/spring.ftl" as spring />
<#import "layout.ftl" as l />
<#import "utils.ftl" as u />
<#import "forms.ftl" as f />
<#import "elements.ftl" as e />

<#escape x as x?html>

<#macro mobileSearch >
    <div class="open-filters">Rajaa hakua <span class="arrow"></span> </br>
        <#if currentMunicipalities?? && currentMunicipalities.present && currentMunicipalities.value?size gt 0>
            Kunnat:
            <@u.printMunicipalities currentMunicipalities.value />
        <#else>
            Kaikki kunnat
        </#if>

    </div>
    <div class="search-options-mobile">
        <div class="search-parameters-wrapper">
            <!--<div class="search-filters-title"><span class="arrow"></span>Rajaa hakua</div>-->
            <form action="${springMacroRequestContext.requestUri}" method="get">
                <h3>Kunnat</h3>
                <@f.municipalitySelect path="currentSearch.municipalities" options=municipalities required="" cssClass="" showLabel=false defaultOption="currentSearch.municipality.all" allowSingleDeselect=true onlyActive=true multiple=true id="mobileSelection "/>
                </br>

                <h3>Aloitteen tyyppi</h3> </br>

                <@f.mobileCheckBox path=currentSearch.type  prefix="type.all" name="type" value="all" id="all"  />
                <@f.mobileCheckBox path=currentSearch.type  prefix="type.normal" name="type" value="normal" id="normal"  />
                <@f.mobileCheckBox path=currentSearch.type  prefix="type.council" name="type" value="council" id="council"  />
                <@f.mobileCheckBox path=currentSearch.type  prefix="type.citizen" name="type" value="citizen" id="citizen"  />

                <h3>Aloitteen tila</h3></br>
                <@f.mobileCheckBox path=currentSearch.show  prefix="show.all" name="show" value="all" id="show-all"  />
                <@f.mobileCheckBox path=currentSearch.show  prefix="show.collecting" name="show" value="collecting" id="collecting"  />
                <@f.mobileCheckBox path=currentSearch.show  prefix="show.sent" name="show" value="sent" id="sent"  />

                <input class="run-search-mobile" type="submit" value="OK"> </br>

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
