<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/pagination.ftl" as p />
<#import "components/forms.ftl" as f />
<#import "components/elements.ftl" as e />

<#escape x as x?html>

    <#--
     * IE 8 and below does not support media-queries. Added a fix for rensponsive layout for IE8 and less.
     * If width-parameter is included within the url, we will add breakpoint classes in body.
     *
    -->
    <#if RequestParameters['width']??>
        <#-- If paramater is not a numerical value fallback with default width -->
        <#attempt>
            <#assign viewportWidth = RequestParameters['width']?number>
        <#recover>
            <#assign viewportWidth = 250 />
        </#attempt>
    
        <#if (viewportWidth < 300)>
            <#assign bodyWidthClass="small" />
        <#elseif (viewportWidth < 480)>
            <#assign bodyWidthClass="medium" />
        <#else>
            <#assign bodyWidthClass="large" />
        </#if>
    <#else>
        <#assign bodyWidthClass="small" />
    </#if>
    
    <#--
     * Set current municipalities
    -->
    <#if municipalities.present>
        <#assign pageTitle><@u.message "iframe.initiatives" />
            <#assign i = 0/>
            <#list municipalities.value as m>
                <#if i = municipalitiesSize - 2>
                    ${m.getName(locale)} <@u.message "iframe.and" />
                <#elseif i lt municipalitiesSize - 1 >
                    ${m.getName(locale)},
                <#else>
                    ${m.getName(locale)}
                </#if>
                <#assign i = i + 1>
            </#list>
        </#assign>
    <#else>
        <#assign pageTitle><@u.message "page.iframe" /></#assign>
    </#if>   

<!DOCTYPE HTML>
<!--[if lt IE 7 ]> <html lang="${locale}" class="ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="${locale}" class="ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="${locale}" class="ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="${locale}" class="ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="${locale}">
<!--<![endif]-->


<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><#noescape>${pageTitle}</#noescape> - <@u.message "siteName" /></title> 
    
    <link href="${urls.baseUrl}/favicon.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />
   
    <#if optimizeResources>
        <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/style-iframe.min.css?version=${resourcesVersion}" />
        <!--[if IE ]>
        <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/kuntalaisaloite-ie.css?version=${resourcesVersion}" />
        <![endif]-->
    <#else>
        <link rel="stylesheet" type="text/css" href="${urls.baseUrl}/css/normalize.css" />
        <noscript>
            <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/kuntalaisaloite-iframe.css" />
            <!--[if IE ]>
                <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/kuntalaisaloite-ie.css" />
            <![endif]-->
        </noscript>
        <link rel="stylesheet/less" type="text/css" media="screen" href="${urls.baseUrl}/css/less-main/kuntalaisaloite-iframe.less" />
        <!--[if IE ]>
            <link rel="stylesheet/less" type="text/css" media="screen" href="${urls.baseUrl}/css/less-main/kuntalaisaloite-ie.less">
        <![endif]-->
        <script src="${urls.baseUrl}/js/less-1.7.5.min.js" type="text/javascript"></script>
    </#if>
</head>

<body class="${bodyWidthClass!""} ${locale}">

<div id="wrapper" class=
    <#if municipalitiesSize gt 3>
        "several-municipalities-${municipalitiesSize}"
    <#elseif municipalitiesSize gt 100>
        "several-municipalities-scrollable"
     </#if> >

    <div id="header" class="container">
        <a id="logo" href="${urls.baseUrl}/${locale}" target="_blank" rel="external" title="<@u.message "siteName" />">
            <span><@u.message "siteName" /></span>
        </a>

        <#if municipalities.present>
            <h3><#noescape>${pageTitle!""}</#noescape></h3>
        <#else>
            <h3>&nbsp;</h3>
        </#if>
    </div>

    <div id="content-wrapper" class="container">
        <div class="mashup-buttons cf">
            <div class="column col-1of2">
                <a href="${urls.search()}
                <#if municipalities.present>
                    ${queryString.getWithMunicipalities(municipalities.value)}"
                </#if>
                   target="_blank" rel="external" class="small-button"><span class="small-icon next"><@u.message "iframe.browseInitiatives" />
                </span></a>
                
                <span class="description"><@u.message "iframe.browseInitiatives.description" /></span>
            </div>
            <div class="column col-1of2 last">
                <a href="${urls.prepare()}" target="_blank" rel="external" class="small-button"><span class="small-icon add"><@u.message "iframe.createInitiative" /></span></a>
            </div>
        </div>
        
        <h1><@u.message "iframe.mostRecent" /></h1>
        
        <div class="search-results">
        <#if initiatives?? && (initiatives?size > 0)>
            <#list initiatives as initiative>
                <#if initiative_index == 0><ul></#if>
                <li <#if initiative_index == 0>class="first"</#if>>
                    <span class="date trigger-tooltip" title="<@u.message "searchResults.initiative.date."+initiative.state />" ><@u.localDate initiative.stateTime!"" /></span>
                    <span>${initiative.municipality.getName(locale)}</span>
                    <span class="title"><a href="${urls.view(initiative.id)}" target="_blank" rel="external" class="name"><@u.limitStringLength initiative.name!"" 150 /></a></span>
                </li>
                <#if !initiative_has_next></ul></#if>
            </#list>

        <#-- Search results EMPTY -->
        <#else>
            <@u.message "iframe.noInitiatives" />
        </#if>
        
        </div>
    </div>
    
</div>

</body>
</html>

</#escape>