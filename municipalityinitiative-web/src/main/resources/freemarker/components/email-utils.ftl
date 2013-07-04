<#import "/spring.ftl" as spring />

<#escape x as x?html> 


<#--
 * message
 *
 * Macro for generating PlainText messages. NOTE that these messages are escaped.
 * Keep in one line for TEXT version or use 'compress single_line=true'
 *
 * @param key is the localization key
 * @param args is the list of arguments for the message
-->
<#macro message key args=[]>${localizations.getMessage(key, switchLocale!locale, args)}</#macro>


<#-- 
 * messageHTML
 * 
 * Macro for generating HTML messages. NOTE that these messages are unescaped.
 *
 * @param key is the localization key
 * @param args list of arguments for the message 
-->

<#macro messageHTML key args=[]><#noescape>${localizations.getMessage(key, switchLocale!locale, args)}</#noescape></#macro>


<#-- 
 * text
 *  
 * Line breaks are replaced with HTML line-breaks (<br/>).
 * NOTE that returned value is unescaped.
 *
 * @param content is the content as string 
-->
<#macro text content style="margin:1em 0 1em 0;">
<@compress single_line=true>
    <#-- FIXME: Does not create paragraph breaks. -->
    <#assign escapedText>${content!""}</#assign>
    <#noescape><p style="${style}">${escapedText?replace('\n\n','</p><p>')?replace('\n','<br/>')}</p></#noescape>
</@compress>
</#macro>


<#--
 * shortenText
 *
 * First chapter of the initiative proposal. Summary length is defined in SummaryMethod.java
 * 
 * @param locale 'fi' or 'sv'
 * @param type 'text' or 'html'
 -->
<#macro shortenText localizedMap locale="fi" type="text">   
<@compress single_line=true>
    <#assign altLocale="sv" />
    <#if locale == "sv"><#assign altLocale="fi" /></#if>
    <#assign inputText>${summaryMethod(localizedMap[locale]!localizedMap[altLocale]!"")}</#assign>
    <#if type == "html">
        <#noescape>${inputText?replace('\n','<br/>')}</#noescape>
    <#else>
        ${inputText}
    </#if>
</@compress>
</#macro>

<#--
 * button
 *
 * Generates a stylish button for HTML emails.
 * 
 * @param message is the label of the button
 * @param url is the URI of the button
 * @param color gray or green. Define more when needed.
 -->
<#macro button message url color="">
    <#if color="green">
        <#assign bgColor="#76b522" />
        <#assign borderColor="#387d0e" />
        <#assign textColor="#ffffff" />
    <#else>
        <#-- Gray as default -->
        <#assign bgColor="#e2e2e2" />
        <#assign borderColor="#cccccc" />
        <#assign textColor="#ffffff" />
    </#if>

    <a href="${url}" style="color:${textColor}; text-decoration:none">
        <span style="background:${bgColor}; border:1px solid ${borderColor}; font-size:13px; font-family:Arial, sans-serif;">
        &nbsp;&nbsp;&nbsp;${message}&nbsp;&nbsp;&nbsp;
        </span>
    </a>

</#macro>

<#--
 * titleBlock
 *
 * Generates a colored block for the title
 * NOT in use ATM. IF used restyle for the new visuals
 * 
 * @param title is the text for the title block
 -->
<#macro titleBlock title="">
<#noescape>
    <table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr style="color:#fff;">
            <th width="20" style="background:#aaa; border-top-left-radius:5px;"><@spacer "0" /></th>
            <th style="background:#aaa; text-align:left;"><h4 style="font-size:13px; margin:1em 0; font-family:Arial,sans-serif;">${title}</h4></th>
            <th width="20" style="background:#aaa; border-top-right-radius:5px;"><@spacer "0" /></th>
        </tr>
    </table>
</#noescape>
</#macro>

<#--
 * link
 *
 * Generates a link so that we could ensure that link styles wouldn't differ in various email-clients.
 * 
 * @param title is the label of the link
 * @param url is the URI of the link
 *
 * FIXME: Known issue: Gmail changes this anchor to a span and then wraps the span with it's own anchor
 -->
<#macro link url title="">
    <#noescape><a href="${url}" style="color:#0089f2; text-decoration:none;">${(title!="")?string(title,url)}</a></#noescape>
</#macro>

<#--
 * spacer
 *
 * Generates a spacer with defined height.
 * 
 * @param height is the height of the spacer
 * @param cssStyle is for customizing CSS-styles
 -->
<#macro spacer height cssStyle="">
    <div style="min-height:${height}px; font-size:${height}px; line-height:${height}px; ${cssStyle}">&#160;</div>
</#macro>

<#-- 
 * localDate
 * 
 * Prints date in the format defined in messages.properties.
 * For example 'dd.MM.yyyy'
 *
 * @param date 
-->
<#macro localDate date="">
<@compress single_line=true>
    <#if date?is_hash>
        ${date.toString(localizations.getMessage("date.format", args))!""}
    <#else>
        ${date?string(localizations.getMessage("date.format", args))!""}
    </#if>
</@compress>
</#macro>

<#-- 
 * enumDescription
 * 
 * Prints a localized message for an enum.
 * For example enum with name 'FlowState' and value 'DRAFT' generates key 'FlowState.DRAFT'.
 *
 * @param key is the value of the enum, for example 'DRAFT'
 * @param args is the list of arguments for the message 
-->
<#macro enumDescription key args=[]>
    <@message key.class.simpleName + "." + key args />        
</#macro>

<#macro solveMunicipality municipality>
    <#if municipality.present>${municipality.value.nameFi} / ${municipality.value.nameSv}<#else>Ei kuntaa / SV Ei kuntaa</#if>
</#macro>

</#escape> 
