<#import "../components/layout.ftl" as l />
<#import "../components/utils.ftl" as u />

<#escape x as x?html> 

<#-- Pages navigation items -->
<#assign staticPages = [
    {"staticPage":HelpPage.GENERAL, "pageName":"general", "cssClass":""},
    {"staticPage":HelpPage.PREPARING, "pageName":"preparing", "cssClass":""},
    {"staticPage":HelpPage.WEB, "pageName":"web", "cssClass":""},
    {"staticPage":HelpPage.FAQ, "pageName":"faq", "cssClass":""},
    {"staticPage":HelpPage.INITIATIVE, "pageName":"initiative", "cssClass":"separator"},
    {"staticPage":HelpPage.PROGRESS, "pageName":"progress", "cssClass":""}
<#--    ,{"staticPage":HelpPage.SERVICE_FAQ, "pageName":"service-faq", "cssClass":""}-->
] />


<#if helpPage == HelpPage.PREPARING>
    <#assign page="preparing" />
<#elseif helpPage == HelpPage.WEB>
    <#assign page="web" />
<#elseif helpPage == HelpPage.FAQ> 
    <#assign page="faq" />
<#elseif helpPage == HelpPage.INITIATIVE> 
    <#assign page="initiative" />
<#elseif helpPage == HelpPage.PROGRESS> 
    <#assign page="progress" />
<#elseif helpPage == HelpPage.SERVICE_FAQ> 
    <#assign page="service-faq" />
<#else>
    <#assign page="general" />
</#if>

<#assign pageTitle><@u.message "page.help."+page+".title" /></#assign>

<#--
 * Layout parameters for HTML-title
 * 
 * @param page is for example "page.help.general.title"
 * @param pageTitle used in HTML title.
-->
<@l.main "page.help" pageTitle!"">
      
    <div class="columns cf">
        <div class="column col-1of4 navigation">    

            <h3 class="navi-title"><@u.message "help.general.title" /></h3>
            
            <#if (staticPages?size > 0) >
                <ul class="navi block-style">                   
                    <#list staticPages as item>
                        <#if item.cssClass == "separator">
                            </ul><br/>
                            <h3 class="navi-title"><@u.message "help.service.title" /></h3>
                            <ul class="navi block-style">
                        </#if>
                        
                        <li class="${item.cssClass}"><a href="${urls.helpIndex()}/<@u.message "HelpPage."+item.staticPage />" <#if item.pageName == page>class="active"</#if>><@u.message "page.help."+item.pageName+".title" /></a></li>
                    </#list>
                </ul>
            </#if>
            
        </div>
        
        <div class="column col-3of4 last">        
            <h1><@u.message "page.help."+page+".title" /></h1>            
            
            <#include "include_help_"+page+".ftl">
        </div>
    </div>  

</@l.main>
</#escape> 

