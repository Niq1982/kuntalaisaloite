<#import "../components/layout.ftl" as l />
<#import "../components/utils.ftl" as u />

<#escape x as x?html> 


<#-- JATKA LOUNAAN JÄLKEEN -->
<#-- Pages navigation items -->
<#assign staticPages = [
    {"staticPage":InfoPage.OM, "pageName":"om", "cssClass":""},
    {"staticPage":InfoPage.VIVI, "pageName":"vivi", "cssClass":""},
    {"staticPage":InfoPage.TERMS, "pageName":"terms", "cssClass":""},
    {"staticPage":InfoPage.FEEDBACK, "pageName":"feedback", "cssClass":""},
    {"staticPage":InfoPage.PERSON_REGISTER, "pageName":"person-register", "cssClass":""},
    {"staticPage":InfoPage.PRIVACY, "pageName":"privacy", "cssClass":""},
    {"staticPage":InfoPage.DEVELOPERS, "pageName":"developers", "cssClass":""}
    {"staticPage":InfoPage.MUNICIPALITY_INITIATIVE, "pageName":"municipality-initiative", "cssClass":""}
] />

<#if infoPage == InfoPage.VIVI>
    <#assign page="vivi" />
<#elseif infoPage == InfoPage.TERMS>
    <#assign page="terms" />
<#elseif infoPage == InfoPage.PERSON_REGISTER>
    <#assign page="person-register" />
<#elseif infoPage == InfoPage.PRIVACY>
    <#assign page="privacy" />
<#elseif infoPage == InfoPage.DEVELOPERS>
    <#assign page="developers" />
<#elseif infoPage == InfoPage.FEEDBACK>
    <#assign page="feedback" />
<#elseif infoPage == InfoPage.MUNICIPALITY_INITIATIVE>
    <#assign page="municipality-initiative" />
<#elseif infoPage == InfoPage.ORGANIZER_ASSURANCE>
    <#assign page="organizer-assurance" />
<#elseif infoPage == InfoPage.BRIEFLY_IN_ENGLISH>
    <#assign page="briefly-in-english" />
<#else>
    <#assign page="om" />
</#if>

<#assign pageTitle><@u.message "page.info."+page+".title" /></#assign>

<#--
 * Layout parameters for HTML-title
 * 
 * @param page is for example "page.info.general.title"
 * @param pageTitle used in HTML title.
-->
<@l.main "page.info" pageTitle!"">
      
    <div class="columns cf">
        <div class="column col-1of4 navigation">
                  
            <h3 class="navi-title"><@u.message "info.title" /></h3>
                 
            <#if (staticPages?size > 0) >
                <ul class="navi block-style">                   
                    <#list staticPages as item>
                        <li class="${item.cssClass}"><a href="${urls.infoIndex()}/<@u.message "InfoPage."+item.staticPage />" <#if item.pageName == page>class="active"</#if>><@u.message "page.info."+item.pageName+".title" /></a></li>
                    </#list>
                </ul>
            </#if>

        </div>
        
        <div class="column col-3of4 last">        
            <h1><@u.message "page.info."+page+".title" /></h1>            
            
            <#include "include_info_"+page+".ftl">
        </div>
    </div>  

</@l.main>
</#escape> 

