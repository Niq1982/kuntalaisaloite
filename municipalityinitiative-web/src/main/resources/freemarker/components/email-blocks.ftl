<#import "email-utils.ftl" as u />

<#escape x as x?html>

<#macro emailTemplate title="">

    <table border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, sans-serif;" width="100%" bgcolor="#f0f0f0">
    <tr>
        <td align="center">
            <@u.spacer "15" />

            <#-- TODO: IF comment (Saate) exists. use with emailInfo.comment. Also remove @Ignore at MailSendingEmailServiceTest#collectable_to_municipality_adds_comment_to_email
            <@comment "html" />
            <@u.spacer "15" />
            -->

            <table border="0" cellspacing="0" cellpadding="0" width="640" style="background:#fff; border-radius:5px; text-align:left; font-family:Arial, sans-serif;">
                <tr style="color:#fff;">
                    <td width="20" style="background:#fff;"><@u.spacer "0" /></td>
                    <td width="550" style="background:#fff; text-align:left;"><@u.spacer "15" /><h4 style="font-size:20px; margin:0; color:#087480; font-weight:normal; font-family:'PT Sans','Trebuchet MS',Helvetica,sans-serif">${title}</h4></th>
                    <td width="20" style="background:#fff;"><@u.spacer "0" /></td>
                </tr>
                <tr>
                    <td colspan="3" style="">
                    <table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
                        <tr>
                            <td width="20" ><@u.spacer "0" /></td>
                            <td style="font-size:12px; font-family:'PT Sans','Trebuchet MS',Helvetica,sans-serif;">

                                <#-- Email content -->
                                <#nested />
                                        
                                <@u.spacer "5" />
                            </td>
                            <td width="20"><@u.spacer "0" /></td>
                        </tr>
                    </table>
                    </td>
                </tr>
            </table>

            <p style="color:#686868; font-size:12px;"><@u.message "email.footer" /></p>

            <@u.spacer "15" />

        </td>
    </tr>
    </table>

</#macro>


<#--
 * initiativeDetails
 *
 * Common initiative details for top section of the email.
 *

 * @param type 'text' or 'html'
 -->
<#macro initiativeDetails type="">
    <#if type == "html">
        <h4 style="font-size:12px; margin:1em 0 0 0;">${emailInfo.name!""}</h4>
        <p style="margin:0 0 1em 0;"><@u.message "email.date.create" /> <@u.localDate emailInfo.createTime />
        <br/><@u.message "email.date.sent" /> <@u.localDate emailInfo.sentTime /></p>
        <@u.text emailInfo.proposal />
    <#else>
        <@u.message "email.initiative" />:
        "${emailInfo.name!""}"
        <@u.message "email.date.create" /> <@u.localDate emailInfo.createTime />

        ${emailInfo.proposal}
    </#if>
</#macro>

<#--
 * contactInfo
 *
 * Contact's name, email, phone and address
 *
 * @param type 'text' or 'html'
 -->
<#macro contactInfo type="">
    <#if type == "html">
        <h4 style="font-size:12px; margin:1em 0 0 0;"><@u.message "email.contact.info" /></h4>
        <p style="margin:0 0 1em 0;">${emailInfo.contactInfo.name!""}<br/>
        ${emailInfo.contactInfo.email!""}<br/>
        ${emailInfo.contactInfo.phone!""}<br/>
        ${emailInfo.contactInfo.address!""}</p>
    <#else>
        <@u.message "email.contact.info" />:
        ${emailInfo.contactInfo.name!""}
        ${emailInfo.contactInfo.email!""}
        ${emailInfo.contactInfo.phone!""}
        ${emailInfo.contactInfo.address!""}
    </#if>
</#macro>


<#--
 * emailBottom
 *
 * Common initiative details for bottom section of the email.
 *
 * @param lang 'fi' or 'sv'
 * @param type 'text' or 'html'
 * @param sentTo 'show' shows additional info
 *
 * TODO: Check if we need this.
 -->
 <#--
<#macro emailBottom lang="" type="" sentTo="">
    <#if lang == "fi">
        <#if type == "html">
            <p style="margin:1em 0 0.5em 0;">Aloite sijaitsee osoitteessa: <@u.link viewUrlFi /></p>
        <#else>
            Aloitteesi sijaitsee osoitteessa:
            ${viewUrlFi}
        </#if>
    <#elseif lang == "sv">
        <#if type == "html">
            <p style="margin:1em 0 0.5em 0;">Initiativet finns på adressen: <@u.link viewUrlSv /></p>
        <#else>
            Initiativet finns på adressen:
            ${viewUrlSv}
        </#if>
    </#if>
</#macro>
-->

<#--
 * comment
 *
 * Common initiative details for bottom section of the email.
 *
 * @param type 'text' or 'html'
 -->
<#macro comment type="">
    <#if type == "html">
        <table border="0" cellspacing="0" cellpadding="0" width="640" style="background:#fff; border-radius:5px; text-align:left; font-family:Arial, sans-serif;">
            <tr>
                <td colspan="3" style="">
                <table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
                    <tr>
                        <td width="20" ><@u.spacer "0" /></td>
                        <td style="font-size:12px; font-family:'PT Sans','Trebuchet MS',Helvetica,sans-serif;">
                            <h4 style="font-size:12px; margin:1em 0 0 0;"><@u.message "email.commentToMunicipality" /></h4>
                            <p style="margin:0.5em 0;">Tähän tulee saateen teksti, jahka se on tehty.</p>

                            <@u.spacer "5" />
                        </td>
                        <td width="20"><@u.spacer "0" /></td>
                    </tr>
                </table>
                </td>
            </tr>
        </table>
    <#else>
        <@u.message "email.commentToMunicipality" />:
        Tähän tulee saateen teksti, jahka se on tehty.
    </#if>
</#macro>

<#--
 * abstract
 *
 * First chapter of the initiative proposal.
 * 
 * @param lang 'fi' or 'sv'
 * @param type 'text' or 'html'
 *
 * TODO: Check if we need this.
 -->
<#macro abstract lang="" type="">
    <#if lang == "fi">
        <#if type == "html">
            <h4 style="font-size:12px; margin:1em 0 0.5em 0;">Tiivistelmä kuntalaisloitteesta</h4>
            <p style="margin:0.5em 0;"><@u.shortenText initiative.proposal "fi" "html" /></p>
            <p style="margin:0.5em 0;"><@u.link viewUrlFi "Näytä aloitteen koko sisältö &rarr;" /></p>
        <#else>
            Tiivistelmä kuntalaisloitteesta:
            <@u.shortenText initiative.proposal "fi" "text" />
            
            
            Näytä aloitteen koko sisältö:
            ${viewUrlFi}
        </#if>
    <#elseif lang == "sv">
        <#if type == "html">
            <h4 style="font-size:12px; margin:1em 0 0.5em 0;">Initiativets sammanfattning</h4>
            <p style="margin:0.5em 0;"><@u.shortenText initiative.proposal "sv" "html" /></p>
            <p style="margin:0.5em 0;"><@u.link viewUrlSv "Visa initiativets innehåll &rarr;" /></p>
        <#else>
            Sammandrag av invånarinitiativ:
            <@u.shortenText initiative.proposal "sv" "text" />
            
            
            Visa initiativets innehåll:
            ${viewUrlSv}
        </#if>
    </#if>
</#macro>

</#escape>