<#import "../components/layout.ftl" as l />
<#import "../components/utils.ftl" as u />
<#import "../components/wysiwyg-editor.ftl" as editor />

<#global editorStyles><@editor.styles /></#global>
<#global editorScripts><@editor.scripts /></#global>

<#escape x as x?html>

<#assign pageTitle>${content.subject!""}</#assign>

<#--
 * Navigation for subpages (OM edit view)
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
            <li>
                <a href="${urls.helpEdit(link.uri)}" <#if link.uri == helpPage>class="active"</#if>>
                    <#if ((link.subject!"")?has_content)>
                        ${link.subject}
                    <#else>
                        <@u.message "editor.emptyTitlePlaceholder" />
                    </#if>
                </a>
            </li>
            </#list>
        </ul>
    </#if>
</#macro>

<#--
 * Layout parameters for HTML-title
 *
 * @param page is for example "page.help.general.title"
 * @param pageTitle used in HTML title.
 * @param requestMessagesON disable request messages in layout.ftl
-->
<@l.main page="page.help" pageTitle=pageTitle!"" requestMessagesON=false>

    <div class="columns cf">
        <div class="column col-1of4 navigation">
            <@navigation categoryLinksMap['MAIN'] "" "box" />
            <@navigation categoryLinksMap['KUNTALAISALOITE_FI'] "help.service.title" />
            <@navigation categoryLinksMap['KUNTALAISALOITE'] "help.general.title" />
            <@navigation categoryLinksMap['TIEDOTTEET'] "help.news.title" />
        </div>

        <div class="column col-3of4 last">
            <@editor.buttons />

            <#if content.modifierName?? || content.modifyTime??>
                <@editor.modifier content.modifierName!"" content.modifyTime!"" />
            </#if>

            <h1>
                <#if ((content.subject!"")?has_content)>
                    ${content.subject}
                <#else>
                    <@u.message "editor.emptyTitlePlaceholder" />
                </#if>
            </h1>

            <#-- Define editable content -->
            <div id="content-editable">
                <#if ((content.content!"")?has_content)>
                    <#noescape>${content.content!""}</#noescape>
                <#else>
                    <@u.message "editor.emptyContentPlaceholder" />
                </#if>
            </div>

            <#-- Include content editor -->
            <div id="wysihtml5-container" class="bootstrap-icons js-hide hidden" style="margin-top:1em;">
                <@editor.form content!"" />
            </div>
        </div>

    </div>

</@l.main>
</#escape>

