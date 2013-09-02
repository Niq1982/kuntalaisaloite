<#import "utils.ftl" as u />
<#import "ribbons.ftl" as r />
<#import "some.ftl" as some />

<#escape x as x?html> 

<#--
 * error
 *
 * Error page for 404 and 500 errors.
 * 404: Page not found or user has no permission
 * 500: Internal server-error (Backend and Frontend)
 * 
 * @param page for example 'error.404.title'.
 -->
<#macro error page>
<!DOCTYPE HTML>
<html lang="${locale}">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title><@u.message page /> - <@u.message "siteName" /></title> 
    
    <link href="${urls.baseUrl}/favicon.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />
   
    <link rel="stylesheet" type="text/css" href="${urls.baseUrl}/css/normalize.css" />
    <link rel="stylesheet" type="text/css" href="${urls.baseUrl}/css/kuntalaisaloite.css" />
</head>
<body>
<div id="wrapper">

    <div id="header-tools">
        <div id="header-tools-content">
            <div id="aloite-links">
                <a href="https://www.kansalaisaloite.fi/${locale}"><@u.message "otherServices.initiative"/></a><span class="hide"> | </span><a class="active" href="${urls.baseUrl}/${locale}"><@u.message "otherServices.municipalityinitiative"/></a>
            </div>
        </div>
    </div>
    
    <div id="header">
        <div id="header-content">
            <a id="logo" href="${urls.baseUrl}/${locale}" accesskey="1" title="<@u.message "siteName" />">
                <span><@u.message "siteName" /></span>
            </a>        
        </div>
    </div>

    <div class="container">
        <div id="content">
          <#-- Main content -->
          <#nested />
        </div>
    </div>
    
    <div id="footer">
        <div class="container">
            <div class="block oikeusministerio">
                <p>
                    <@u.message "footer.ministerOfJustice"/><br/>
                    <#-- <a href="${urls.infoIndex()}/<@u.message "InfoPage.OM" />"><@u.message "common.readMore" /> &rarr;</a>-->
                </p>
            </div>
            <#--
            <div class="block viestintavirasto">
                <p>
                    <@u.messageHTML "footer.ficora"/><br/>
                    <a href="${urls.infoIndex()}/<@u.message "InfoPage.VIVI" />"><@u.message "common.readMore" /> &rarr;</a>
                </p>
            </div>
            -->
            <#-- TODO: links
            <div class="footer-links">
                <ul>
                </ul>
            </div>
            -->
            <br class="clear"/>
        </div>
        
        <a href="#header-tools" accesskey="3" id="back-to-top"><@u.message "accesskey.backToTop" /></a>
    </div>

</div>
</body>
</html>
</#macro>

<#--
 * main
 *
 * Normal page layout.
 * 
 * @param page for example 'page.management'
 * @param pageTitle for example initiative's title. Used in HTML title.
 * @param requestMessagesON can be assigned to show request messages in main macro (default is true)
 -->
<#macro main page pageTitle="" requestMessagesON=true>

<#-- Main navigation navigation items for HTML title, navigation and breadcrumb -->
<#assign naviItems = [
      {"naviName":"page.frontpage", "naviUrl":urls.frontpage()},
      {"naviName":"page.find", "naviUrl":urls.search()},
      {"naviName":"page.prepare", "naviUrl":urls.prepare()},
      {"naviName":"page.help", "naviUrl":urls.helpIndex()},
      {"naviName":"page.news", "naviUrl":urls.news()}

    ] />

<#assign currentPage>
<@compress single_line=true>
    <#if pageTitle!="">
       <#noescape>${pageTitle}</#noescape>
    <#else>
        <@u.message page />
    </#if>
</@compress>
</#assign>

<!DOCTYPE HTML>
<!-- version ${appVersion} -->
<!--[if lt IE 7 ]> <html lang="${locale}" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="${locale}" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="${locale}" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="${locale}" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!-->
<html lang="${locale}" class="no-js">
<!--<![endif]-->

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        
    <#--
     * Disallow create-page indexing
     *
     * Precaution for spam bots. Disallowing create-form-page to be indexed
     * in search engines should prevent some spam-bots to find the form.
     * More powerful counter measures are found in forms.ftl within macros:
     * "securityFilters" and "notTooFastField".
     * Participate form can however be the major problem.
    -->
    <#assign disallowIndexing = (page == "page.createNew") />
    <#if disallowIndexing>
        <meta name="robots" content="noindex" />
    </#if>
    
    <title><#noescape>${currentPage}</#noescape> - <@u.message "siteName" /></title> 
    
    <#-- CSS class 'no-js' -> 'js'. This switch needs to be fast! -->
    <script>
        document.documentElement.className = document.documentElement.className.replace(/(\s|^)no-js(\s|$)/, '$1js$2');
    </script>
    
    <link rel="canonical" href="${currentUri}" />
    
    <#-- Get initiative's proposal's abstract if exists, otherwise show default description -->
    <#if initiative??>
        <meta name="description" content="<@u.shortenText initiative.proposal!"" "text" />" />
    <#else>
        <meta name="description" content="<@u.message "siteDescription" />" />
    </#if>  
    
    <#-- Open Graph -metas for SOME -->
    <meta property="og:site_name" content="<@u.message "siteName" />" />
    <meta property="og:url" content="${currentUri}" />
    
    <meta property="og:title" content="<@u.message "siteName" /> - <#noescape>${currentPage}</#noescape>" />

    <#if initiative??>
        <meta property="og:description" content="<@u.shortenText initiative.proposal!"" "text" />" />
    <#else>
        <meta property="og:description" content="<@u.message "siteDescription" />" />
    </#if>
    <meta property="og:image" content="${urls.baseUrl}/img/logo-share.png?version=${resourcesVersion}" />
    
    <link href="${urls.baseUrl}/favicon.ico?version=${resourcesVersion}" rel="shortcut icon" type="image/vnd.microsoft.icon" />
    
    <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/fonts.css?version=${resourcesVersion}" />
    <!--[if IE ]>
        <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/fonts-ie.css?version=${resourcesVersion}" />
    <![endif]-->
   
    <#if optimizeResources>
        <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/style.min.css?version=${resourcesVersion}" />
        <!--[if IE ]>
        <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/kuntalaisaloite-ie.css?version=${resourcesVersion}" />
        <![endif]-->
        
        <link rel="stylesheet" type="text/css" media="print" href="${urls.baseUrl}/css/print.css?version=${resourcesVersion}" />
    <#else>
        <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/normalize.css?version=${resourcesVersion}" />
        <noscript>
            <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/kuntalaisaloite.css" />
            <!--[if IE ]>
                <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/kuntalaisaloite-ie.css" />
            <![endif]-->
            <link rel="stylesheet" type="text/css" media="print" href="${urls.baseUrl}/css/print.css?version=${resourcesVersion}" />
        </noscript>
        <link rel="stylesheet" type="text/css" media="screen" href="${urls.baseUrl}/css/chosen.css" />
        <link rel="stylesheet/less" type="text/css" media="screen" href="${urls.baseUrl}/css/less-main/kuntalaisaloite.less" />
        <!--[if IE ]>
            <link rel="stylesheet/less" type="text/css" media="screen" href="${urls.baseUrl}/css/less-main/kuntalaisaloite-ie.less">
        <![endif]-->
        
        <link rel="stylesheet/less" type="text/css" media="print" href="${urls.baseUrl}/css/less-main/print.less" />
        
        <script src="${urls.baseUrl}/js/less-1.3.0.min.js" type="text/javascript"></script>
    </#if>

    <#--
     * Include wysiwyg editor resources: CSS styles
     *
     * - Styles are defined in the edit-view templates for static pages which are visible only for OM-users.
    -->
    <#noescape>${editorStyles!""}</#noescape>

    <#--<link href='https://fonts.googleapis.com/css?family=PT+Sans:400,700,400italic,700italic' rel='stylesheet' type='text/css' />-->    
</head>
<body class="${locale}">
    <p id="accesskeys">
        <#--
            Accesskeys:
            0: Accesskeys and help
            1: Frontpage
            2: Skip to content
            3: To page top
            4: Sitemap (not in use)
            5: Search
            6: Text version / Graphic version (not in use)
            7: Decrease font size
            8: Increase font size
            9: Feedback (not in use)
            p: Print
        -->
        
        <a href="#content" accesskey="2"><@u.message "accesskey.skipToContent" /></a> | 
        <a href="${naviItems[1].naviUrl}" accesskey="4"><@u.message "accesskey.search" /></a> | 
        <a href="${naviItems[3].naviUrl}" accesskey="0"><@u.message "accesskey.help" /></a> | 
        <a href="javascript:print()" accesskey="p"><@u.message "accesskey.print" /></a>
    </p>
    
    <div id="wrapper" <#if page == "page.frontpage">class="front"</#if>>
    
    <#-- NOTE: Extra title for test sites STARTS ----------------------------- -->
    <@r.topRibbon/>
    <#-- NOTE: Extra title for test sites ENDS ------------------------------- -->

    <div id="header-tools">
        <div id="header-tools-content">
            <div id="aloite-links">
                <a href="https://www.kansalaisaloite.fi/${locale}"><@u.message "otherServices.initiative"/></a><span class="hide"> | </span><a class="active" href="${urls.baseUrl}/${locale}"><@u.message "otherServices.municipalityinitiative"/></a>
            </div>
            <div class="other-services-container">
                <a class="other-services-select" href="http://www.otakantaa.fi/${locale}-FI" target="_blank"> <#-- Add class "dropdown-toggle" for dropdown -->
                    <@u.message "otherServices.otaKantaa"/><span class="icon-small arrow-right"></span>
                </a>
            </div>

            <div class="logged-in-info">
                <#if user.isLoggedIn()>
                    <#if user.isOmUser()>
                        <div class="header-dropdown">
                            <a href="#" class="header-tool-link user-name dropdown-toggle">${user.name} <span class="icon-small settings"></span></a>
                            <ul id="user-menu" class="dropdown-menu user-menu">
                                <li><a href="${urls.municipalityModeration()}"><@u.message "om.municipality.moderation" /></a></li>
                                <#if initiative??>
                                    <#if initiative.id?? && !initiative.sent><li><a href="${urls.moderation(initiative.id)}"><@u.message "om.initiative.moderation" /></a></li></#if>
                                </#if>
                            </ul>
                        </div>
                    </#if>
                     <#if user.isVerifiedUser()>
                        <div class="header-dropdown">
                            <a href="#" class="header-tool-link user-name dropdown-toggle">${user.contactInfo.name}<#if user.homeMunicipality.present>, ${user.homeMunicipality.value.getName(locale)}</#if>
                                <span class="icon-small settings"></span></a>
                            <ul id="user-menu" class="dropdown-menu user-menu">
                                <li><a href="${urls.ownInitiatives()}"><@u.message "page.ownInitiatives" /></a></li>
                            </ul>
                        </div>
                    </#if>
                    <a href="${urls.logout()}" id="logout" class="header-tool-link logout"><@u.message "common.logout"/><span class="icon-small logout"></span></a>

                <#elseif enableVerifiedInitiatives>
                    <a href="${urls.authenticate(currentRequestUri)}" title="<@u.message "authenticate.title"/>" class="header-tool-link login"><@u.message "authenticate.title"/></a>
                </#if>
            </div>

        </div>

    </div>


      
    <div id="header">
        <div id="header-content">

            <a id="logo" href="${urls.baseUrl}/${locale}" accesskey="1" title="<@u.message "siteName" />">
                <span><@u.message "siteName.logo" /></span>
            </a>

            <#-- Language toggle, text size -->
            <div id="header-additional-content">
                <div class="additional-tools">
                    <a href="${altUri!"/"+altLocale}" class="language-selection"><@u.message "lang.alternative"/></a>
                    <#-- NOTE: ATM the font-size-toggle works only with JS hence the links are also generated with JS. -->
                    <script type="text/javascript">                    
                        document.write('<span class="font-size"><@u.message "common.fontsize" />:</span>');
                        document.write('<span class="font-size-toggle"><a href="" title="<@u.message "common.fontsize.decrease"/>" class="gray font-size-small-link" accesskey="7"><span class="font-size-small">A</span></a><a href="" title="<@u.message "common.fontsize.default"/>" class="gray font-size-medium-link"><span class="font-size-medium">A</span></a><a href="" title="<@u.message "common.fontsize.increase"/>" class="gray font-size-large-link" accesskey="8"><span class="font-size-large">A</span></a></span>');
                    </script>
                </div>
            </div> 

            <#if (naviItems?size > 0) >
                <div id="main-navigation">
                    <ul>                    
                        <#list naviItems as item>
                            <li <#if item.naviName == page>class="active"</#if>><a href="${item.naviUrl}"><@u.message item.naviName /></a></li>
                        </#list>
                    </ul>
                </div>
            </#if>
        
        </div>
    </div>
    
    <#if page == "page.frontpage">
        <#nested />
    <#else>
        <div class="container">
            <div id="content">
    
                <#if requestMessagesON && requestMessages?? >
                    <@u.requestMessage requestMessages />
                </#if>
                
                <#-- Main content -->
                <#nested />
                
            </div>
        </div>
    </#if>
    
    <div id="footer">
        <div class="container">
            <div class="block oikeusministerio">
                <p>
                    <@u.message "footer.ministerOfJustice"/><br/>
                    <#-- <a href="${urls.infoIndex()}/<@u.enumDescription InfoPage.OM />"><@u.message "common.readMore" /> <span class="icon-small arrow-right-2"></span></a>-->
                </p>
            </div>
            <#--
            <div class="block viestintavirasto">
                <p>
                    <@u.messageHTML "footer.ficora"/><br/>
                    <a href="${urls.infoIndex()}/<@u.enumDescription InfoPage.VIVI />"><@u.message "common.readMore" /> <span class="icon-small arrow-right-2"></span></a>
                </p>
            </div>
            -->
            <#--
            <div class="footer-links">
                <ul>
                </ul>
            </div>
            -->
            <br class="clear"/>
        </div>
        
        <a href="#header-tools" accesskey="3" id="back-to-top"><@u.message "accesskey.backToTop" /></a>
    </div>
    
    <#-- NOTE: Extra footer for test sites STARTS ----------------------------- -->
    <@r.bottomRibbon/>
    <#-- NOTE: Extra footer for test sites ENDS ------------------------------- -->
    
    </div>
    
    <#-- Modal container is moved to here. Because it was easier to handle styles for IE7. -->
    <div id="modal-container"></div>

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
      <script type="text/javascript" src="${urls.baseUrl}/js/jquery.scrollTo.min.js?version=${resourcesVersion}"></script>
      <script type="text/javascript" src="${urls.baseUrl}/js/kuntalaisaloite.js?version=${resourcesVersion}"></script>
    </#if>

  <#--
     * Include wysiwyg editor resources: JavaScripts
     *
     * - JavaScripts are defined in the edit-view templates for static pages which are visible only for OM-users.
    -->
    <#noescape>${editorScripts!""}</#noescape>
    
    <#-- Initialize variables for JavaScript -->
    <script type="text/javascript">
    /*<![CDATA[*/
    
    var Init = {
        getLocale:function(){return "${locale}"},
        getDateFormat:function(){return "${springMacroRequestContext.getMessage('date.format')?string?lower_case}"}
    };
    
    /*]]>*/
    </script>

    <#if omPiwicId??>
        <!-- Piwik -->
        <script type="text/javascript">
          var _paq = _paq || [];
          _paq.push(["trackPageView"]);
          _paq.push(["enableLinkTracking"]);

          (function() {
            var u=(("https:" == document.location.protocol) ? "https" : "http") + "://log.otakantaa.fi/";
            _paq.push(["setTrackerUrl", u+"piwik.php"]);
            _paq.push(["setSiteId", "${omPiwicId}"]);
            var d=document, g=d.createElement("script"), s=d.getElementsByTagName("script")[0]; g.type="text/javascript";
            g.defer=true; g.async=true; g.src=u+"piwik.js"; s.parentNode.insertBefore(g,s);
          })();
        </script>
        <!-- End Piwik Code -->
    </#if>

</body>
</html> 
</#macro>
</#escape> 

