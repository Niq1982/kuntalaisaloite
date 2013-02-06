<#import "email-utils.ftl" as eu />

<#escape x as x?html>

<#macro emailHtml template title="">
<html>
<head>
    <title>${title}</title>
    
    <#-- Client-specific Styles -->
    <style type="text/css">
        <#-- Force Outlook to provide a "view in browser" button. -->
        #outlook a{padding:0;} 
             
        <#-- Force Hotmail to display emails at full width -->
        body{width:100% !important;}
        .ReadMsgBody{width:100%;}
        .ExternalClass{width:100%;} 
             
        <#-- Prevent Webkit and Windows Mobile platforms from changing default font sizes. -->
        body{-webkit-text-size-adjust:none; -ms-text-size-adjust:none;}
    </style>
    
</head>
<body style="background:#f0f0f0;">
<#-- Extra div as Gmail strips off body-tag -->
<div style="background:#f0f0f0;">

    <#-- Email content -->
    <#nested />

</div>
</body>
</html>
</#macro>

</#escape> 