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
        <#list map as link>
            <a href="${urls.help(link.uri)}" class="${(link.uri == helpPage)?string("active","")} ${cssClass} ${link_has_next?string("","last")}">
                <#-- NOTE: we could also use urls (fi/sv) to determine the class -->
                <#if link_index == 1>
                    <#assign iconClass="author" />
                <#elseif link_index == 2>
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
        <#list map as link>
        <a href="${urls.help(link.uri)}" class="${cssClass} ${link_has_next?string("","last")}">
        <#-- NOTE: we could also use urls (fi/sv) to determine the class -->
            <#if link_index == 1>
                <#assign iconClass="author" />
            <#elseif link_index == 2>
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
        <@navigationMobile categoryLinksMap['MAIN'] "" "box-mobile" />
            <@navigationMobile categoryLinksMap['KUNTALAISALOITE_FI'] "help.service.title" />
            <@navigationMobile categoryLinksMap['KUNTALAISALOITE'] "help.general.title" />
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

        <#-- InfoText content -->

        <div class="column col-3of4 last">
            <#if content??>
                <div class="help-text-content">
                    <h1>${content.subject!""}</h1>
                    <#noescape>${content.content!""}</#noescape>
                </div>
            </#if>
        </div>


        <#-- InfoText content mobile -->
        <#if content??>
            <div class="help-text-content-mobile bread-text">
                <h1>${content.subject!""}</h1>
                <#noescape>${content.content!""}</#noescape>
            </div>
        </#if>

    </div>

</@l.main>



</#escape>

