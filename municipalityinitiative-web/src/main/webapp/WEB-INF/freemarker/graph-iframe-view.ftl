<#import "/spring.ftl" as spring />
<#import "components/utils.ftl" as u />
<#import "components/elements.ftl" as e />

<#escape x as x?html>

    <#assign showTitle = false />
    <#if RequestParameters['showTitle']?? && RequestParameters['showTitle'] == "true">
    	<#assign showTitle = true />
    </#if>

    <#--
     * Set current municipality

    <#if currentMunicipality.present>
        <#assign pageTitle><@u.message "iframe.initiatives" /> ${currentMunicipality.value.getName(locale)}</#assign>
    <#else>

    </#if>
    -->
    <#if initiative??>
	    <#if initiative.name??>
	        <#assign page="page.initiative.public" />
	        <#assign pageTitle><@u.text initiative.name /></#assign>
	    <#else>
	        <#assign page="page.initiative.unnamed" />
	        <#assign pageTitle="" />
	    </#if>
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

<div class="container">
    <div id="header">
        <a class="logo small" id="logo" href="${urls.baseUrl}/${locale}" target="_blank" rel="external" title="<@u.message "siteName" />">
            <span><@u.message "siteName" /></span>
        </a>
	</div>

    <#if showTitle>
        <a href="${urls.view(initiative.id)}" target="_blank" rel="external">
    	   <h1 class="name"><@u.text initiative.name /></h1>
        </a>
	    <#if initiative.startDate??>
	        <span class="extra-info"><@u.localDate initiative.startDate /></span>
	    </#if>
	</#if>

    <#if votingInfo?? && votingInfo.votingInProggress || participantCount.total gt 0>
	    <div class="view-block">
		    <#if supportCountData??><@e.participantGraph initiative supportCountData participantCount.total /></#if>
		</div>
	</#if>

    </div>
</div>
    <#if optimizeResources>
    <script type="text/javascript" src="${urls.baseUrl}/js/script.min.js?version=${resourcesVersion}"></script>
    <#else>
    <script type="text/javascript" src="${urls.baseUrl}/js/jquery-1.7.2.min.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/jquery.easing.min.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/jquery.tools.min.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/jquery.cookie.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/jquery.dirtyforms.min.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/jsrender.min.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/chosen.jquery.min.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/jquery.magnific-popup.min.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/jquery.linkreplacer.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/moment.min.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/raphael.min.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/jquery.headernav.js?version=${resourcesVersion}"></script>
    <script type="text/javascript" src="${urls.baseUrl}/js/jquery.participantgraph.js?version=${resourcesVersion}"></script>

    <script type="text/javascript" src="${urls.baseUrl}/js/kuntalaisaloite.js?version=${resourcesVersion}"></script>
    </#if>


    <#-- Initialize variables for JavaScript -->
    <script type="text/javascript">
    /*<![CDATA[*/

    var Init = {
        getLocale:function(){return "${locale}"},
        getDateFormat:function(){return "${springMacroRequestContext.getMessage('date.format')?string?lower_case}"},
        getViewMode:function(){return "<#if managementSettings??>${managementSettings.editMode}<#else>VIEW</#if>"},
        getSupportCountJson:function(){return "${urls.baseUrl}/api/v1/supports/"}
    };

    /*]]>*/
    </script>

</body>
</html>

</#escape>