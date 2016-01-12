<#import "../components/layout.ftl" as l />
<#import "../components/utils.ftl" as u />

<#if omUser>
    <#import "../components/wysiwyg-editor.ftl" as editor />
    <#global editorStyles><@editor.styles /></#global>
</#if>

<#escape x as x?html>

<#if content??>
    <#assign pageTitle>${content.subject}</#assign>
<#else>
	<#-- Static page for the info graph -->
    <#assign pageTitle><@u.message "infoGraph.title" /></#assign>
</#if>

<#--
 * Navigation for subpages (public view)
 *
 * @param map is the hashMap for navigation items
 * @param titleKey is for navigation block title
 * @param cssClass is used for box-style links
-->
<#macro navigation map titleKey="" cssClass="">
    <#if cssClass == "box">
        <a href="${urls.helpIndex()}" class="${(helpPage == "lyhyesti" || helpPage == "")?string("active","")} ${cssClass}">
            <span class="help-nav-icon icon-info"><@u.message "infoGraph.title" /></span>
        </a>
        <#list map as link>
            <a href="${urls.help(link.uri)}" class="${(link.uri == helpPage)?string("active","")} ${cssClass} ${link_has_next?string("","last")}">
                <#-- NOTE: we could also use urls (fi/sv) to determine the class -->
                <#if link_index == 0>
                    <#assign iconClass="author" />
                <#elseif link_index == 1>
                    <#assign iconClass="participants" />
                <#else>
                    <#assign iconClass="info" />
                </#if>
            
                <span class="help-nav-icon icon-${iconClass}">${link.subject}</span>
            </a>
        </#list>
    <#else>
        <#if titleKey?has_content><h3 class="navi-title"><@u.message titleKey /></h3></#if>
        <ul class="navi block-style">
            <#list map as link>
                <li><a href="${urls.help(link.uri)}" <#if link.uri == helpPage>class="active"</#if>>${link.subject}</a></li>
            </#list>
        </ul>
    </#if>
</#macro>

<#macro navigationMobile map titleKey="" cssClass="">
    <#if cssClass == "box-mobile">
        <a href="${urls.helpInitiativeGraphMobile()}" class="${cssClass}">
            <span class="help-nav-icon icon-info"><@u.message "infoGraph.title" /></span>
        </a>
        <#list map as link>
        <a href="${urls.help(link.uri)}" class="${cssClass} ${link_has_next?string("","last")}">
        <#-- NOTE: we could also use urls (fi/sv) to determine the class -->
            <#if link_index == 0>
                <#assign iconClass="author" />
            <#elseif link_index == 1>
                <#assign iconClass="participants" />
            <#else>
                <#assign iconClass="info" />
            </#if>

            <span class="help-nav-icon icon-${iconClass}">${link.subject}</span>
        </a>
        </#list>
    <#else>
        <#if titleKey?has_content><h3 class="navi-title"><@u.message titleKey /></h3></#if>
        <ul class="navi block-style">
            <#list map as link>
                <li><a href="${urls.help(link.uri)}" <#if link.uri == helpPage>class="active"</#if>>${link.subject}</a></li>
            </#list>
        </ul>
    </#if>
</#macro>

<#macro helpNavigation>
    <div class="column col-1of4 extra-margin navigation">
        <@navigation categoryLinksMap['MAIN'] "" "box" />
        <@navigation categoryLinksMap['KUNTALAISALOITE_FI'] "help.service.title" />
        <@navigation categoryLinksMap['KUNTALAISALOITE'] "help.general.title" />
    </div>
</#macro>

<#macro helpNavigationMobile>
    <div class="navigation-mobile" >
        <#if content?? || (showInfoGraph?? && showInfoGraph)>
            <a href="${urls.helpIndex()}" class="back-link"> <@u.message "mobile.help.frontpage"/> </a>
        <#else>
            <@navigationMobile categoryLinksMap['MAIN'] "" "box-mobile" />
            <@navigationMobile categoryLinksMap['KUNTALAISALOITE_FI'] "help.service.title" />
            <@navigationMobile categoryLinksMap['KUNTALAISALOITE'] "help.general.title" />
        </#if>
    </div>
</#macro>
<#--
 * Layout parameters for HTML-title
 *
 * @param page is for example "page.help.general.title"
 * @param pageTitle used in HTML title.
-->
<@l.main "page.help" pageTitle!"">

    <div class="columns cf">

        <@helpNavigation />
        <@helpNavigationMobile />

        <#if omUser && helpPage!= "">
            <div class="editor-buttons bootstrap-icons hidden">
                <a href="${urls.helpEdit(helpPage)}" class="btn" href="#"><@u.message "editor.switchToEdit" />&nbsp;&nbsp;<i class="icon-chevron-right"></i></a>
            </div>
        </#if>

        <#-- Static content for the info graph -->
        <#include "include_help_content.ftl" />
        <#include "include_help_content_mobile.ftl" />

    </div>

</@l.main>



</#escape>

