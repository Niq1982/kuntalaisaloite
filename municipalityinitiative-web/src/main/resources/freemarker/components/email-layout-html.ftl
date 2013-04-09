<#import "email-utils.ftl" as u />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#macro emailHtml template title="" footerLink=false>
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
<body style="background:${bodyBGcolor!""};">
<#-- Extra div as Gmail strips off body-tag -->
<div style="background:${bodyBGcolor!""};">

    <table border="0" cellspacing="0" cellpadding="0" style="font-family:${defaultFontFamily!"Arial, sans-serif"};" width="100%" bgcolor="${bodyBGcolor!""}">
    <tr>
        <td align="center">
            <@u.spacer "15" />

            <#-- Email content -->
            <#nested />
            
            <#if footerLink>
                <p style="${footerFont!""}"><@u.message "email.municipality.sendFrom" /><br/><@u.link urls.view(initiative.id) /></p>
                <br/>
            </#if>
            <p style="${footerFont!""}"><@u.message "email.footer" /></p>

            <@u.spacer "15" />
        </td>
    </tr>
    </table>

</div>
</body>
</html>
</#macro>

</#escape> 