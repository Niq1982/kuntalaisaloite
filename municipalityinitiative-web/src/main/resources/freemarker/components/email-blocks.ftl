<#import "email-utils.ftl" as eu />

<#escape x as x?html>

<#macro emailTemplate lang="fi" title="">

    <table border="0" cellspacing="0" cellpadding="0" style="font-family:Arial, sans-serif;" width="100%" bgcolor="#f0f0f0">
    <tr>
        <td align="center">
            <@eu.spacer "15" />
            
            <#-- TODO: IF comment (Saate) exists
            <@comment "fi" "html" />
            <@eu.spacer "15" />
            -->
            
            <table border="0" cellspacing="0" cellpadding="0" width="640" style="background:#fff; border-radius:5px; text-align:left; font-family:Arial, sans-serif;">        
                <tr style="color:#fff;">
                    <td width="20" style="background:#fff;"><@eu.spacer "0" /></td>
                    <td width="550" style="background:#fff; text-align:left;"><@eu.spacer "15" /><h4 style="font-size:20px; margin:0; color:#087480; font-weight:normal; font-family:'PT Sans','Trebuchet MS',Helvetica,sans-serif">${title}</h4></th>
                    <td width="20" style="background:#fff;"><@eu.spacer "0" /></td>
                </tr>
                <tr>
                    <td colspan="3" style="">
                    <table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
                        <tr>
                            <td width="20" ><@eu.spacer "0" /></td>
                            <td style="font-size:12px; font-family:'PT Sans','Trebuchet MS',Helvetica,sans-serif;">
                                <#--<@eu.spacer "5" />-->
    
                                <#-- Email content -->
                                <#nested />
                                        
                                <@eu.spacer "5" />
                            </td>
                            <td width="20"><@eu.spacer "0" /></td>
                        </tr>
                    </table>
                    </td>
                </tr>
            </table>

            <#if lang == "fi">
                <p style="color:#686868; font-size:12px;">Epäiletkö että viesti tuli väärään osoitteeseen? Ole hyvä ja poista tämä viesti.</p>
            <#else>
                <p style="color:#686868; font-size:12px;">Misstänker du att meddelandet kom till fel adress? Var god och radera detta meddelandet.</p>
            </#if>
            <@eu.spacer "15" />
                    
        </td>
    </tr>
    </table>

</#macro>


<#--
 * initiativeDetails
 *
 * Common initiative details for top section of the email.
 * 
 * @param lang 'fi' or 'sv'
 * @param type 'text' or 'html'
 -->
<#macro initiativeDetails lang="" type="">
    <#if lang == "fi">
        <#if type == "html">
            <h4 style="font-size:12px; margin:1em 0 0 0;">${emailInfo.name!""}</h4>
            <p style="margin:0 0 1em 0;">Aloite luotu Kuntalaisaloite.fi-palveluun: <@eu.localDate emailInfo.createTime /><br/>
            <#if emailInfo.sentTime.present>Lähetetty kuntaan: <@eu.localDate emailInfo.sentTime.value /></#if></p>
            <@eu.text emailInfo.proposal />
        <#else>
            Kuntalaisaloite:
            "${emailInfo.name!""}"
            Aloite luotu: <@eu.localDate emailInfo.createTime />
            
            ${emailInfo.proposal}
        </#if>
    <#elseif lang == "sv">
        <#if type == "html">
            <h4 style="font-size:12px; margin:1em 0 0 0;">${emailInfo.name!""}</h4>
            <p style="margin:0 0 1em 0;">Initiativet har skapats: <@eu.localDate emailInfo.createTime /></p>
            <@eu.text emailInfo.proposal />
        <#else>
            Invånarinitiativ:
            "${emailInfo.name!""}"
            Invånarinitiativet har skapats: <@eu.localDate emailInfo.createTime />
        </#if>    
    </#if>        
</#macro>

<#--
 * contactInfo
 *
 * Contact's name, email, phone and address
 * 
 * @param lang 'fi' or 'sv'
 * @param type 'text' or 'html'
 -->
<#macro contactInfo lang="" type="">
    <#if lang == "fi">
        <#if type == "html">
            <h4 style="font-size:12px; margin:1em 0 0 0;">${localizations.getMessage("email.contact.info")}</h4>
            <p style="margin:0 0 1em 0;">${emailInfo.contactInfo.name!""}<br/>
            ${emailInfo.contactInfo.email!""}<br/>
            ${emailInfo.contactInfo.phone!""}<br/>
            ${emailInfo.contactInfo.address!""}</p>
        <#else>
            Yhteystiedot:
            ${emailInfo.contactInfo.name!""}
            ${emailInfo.contactInfo.email!""}
            ${emailInfo.contactInfo.phone!""}
            ${emailInfo.contactInfo.address!""}
        </#if>
    <#elseif lang == "sv">
        <#if type == "html">
            <h4 style="font-size:12px; margin:1em 0 0 0;">Kontaktuppgifter</h4>
            <p style="margin:0 0 1em 0;">${emailInfo.contactInfo.name!""}<br/>
            ${emailInfo.contactInfo.email!""}<br/>
            ${emailInfo.contactInfo.phone!""}<br/>
            ${emailInfo.contactInfo.address!""}</p>
        <#else>
            Kontaktuppgifter:
            ${emailInfo.contactInfo.name!""}
            ${emailInfo.contactInfo.email!""}
            ${emailInfo.contactInfo.phone!""}
            ${emailInfo.contactInfo.address!""}
        </#if>    
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
 -->
<#macro emailBottom lang="" type="" sentTo="">
    <#if lang == "fi">
        <#if type == "html">
            <p style="margin:1em 0 0.5em 0;">Aloite sijaitsee osoitteessa: <@eu.link viewUrlFi /></p>
        <#else>
            Aloitteesi sijaitsee osoitteessa:
            ${viewUrlFi}        
        </#if>
    <#elseif lang == "sv">
        <#if type == "html">
            <p style="margin:1em 0 0.5em 0;">Initiativet finns på adressen: <@eu.link viewUrlSv /></p>
        <#else>
            Initiativet finns på adressen:
            ${viewUrlSv}
        </#if>    
    </#if>        
</#macro>

<#--
 * comment
 *
 * Common initiative details for bottom section of the email.
 * 
 * @param lang 'fi' or 'sv'
 * @param type 'text' or 'html'
 -->
<#macro comment lang="" type="">
    <#if lang == "fi">
        <#if type == "html">
            

            <table border="0" cellspacing="0" cellpadding="0" width="640" style="background:#fff; border-radius:5px; text-align:left; font-family:Arial, sans-serif;">        
                <#--<tr style="color:#fff;">
                    <td width="20" style="background:#fff;"><@eu.spacer "0" /></td>
                    <td width="550" style="background:#fff; text-align:left;"><@eu.spacer "15" /><h4 style="font-size:20px; margin:0; color:#087480; font-weight:normal; font-family:'PT Sans','Trebuchet MS',Helvetica,sans-serif">${title}</h4></th>
                    <td width="20" style="background:#fff;"><@eu.spacer "0" /></td>
                </tr>-->
                <tr>
                    <td colspan="3" style="">
                    <table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
                        <tr>
                            <td width="20" ><@eu.spacer "0" /></td>
                            <td style="font-size:12px; font-family:'PT Sans','Trebuchet MS',Helvetica,sans-serif;">
                                <#--<@eu.spacer "5" />-->
    
                                <h4 style="font-size:12px; margin:1em 0 0 0;">Saate</h4>
                                <p style="margin:0.5em 0;">Tähän tulee saateen teksti, jahka se on tehty.</p>
                                        
                                <@eu.spacer "5" />
                            </td>
                            <td width="20"><@eu.spacer "0" /></td>
                        </tr>
                    </table>
                    </td>
                </tr>
            </table>



        <#else>
            Aloitteesi sijaitsee osoitteessa:
            ${viewUrlFi}        
        </#if>
    <#elseif lang == "sv">
        <#if type == "html">
            <p style="margin:1em 0 0.5em 0;">Initiativet finns på adressen: <@eu.link viewUrlSv /></p>
        <#else>
            Initiativet finns på adressen:
            ${viewUrlSv}
        </#if>    
    </#if>        
</#macro>

<#--
 * abstract
 *
 * First chapter of the initiative proposal.
 * 
 * @param lang 'fi' or 'sv'
 * @param type 'text' or 'html'
 -->
<#macro abstract lang="" type="">
    <#if lang == "fi">
        <#if type == "html">
            <h4 style="font-size:12px; margin:1em 0 0.5em 0;">Tiivistelmä kuntalaisloitteesta</h4>
            <p style="margin:0.5em 0;"><@eu.shortenText initiative.proposal "fi" "html" /></p>
            <p style="margin:0.5em 0;"><@eu.link viewUrlFi "Näytä aloitteen koko sisältö &rarr;" /></p>
        <#else>
            Tiivistelmä kuntalaisloitteesta:
            <@eu.shortenText initiative.proposal "fi" "text" />
            
            
            Näytä aloitteen koko sisältö:
            ${viewUrlFi}
        </#if>
    <#elseif lang == "sv">
        <#if type == "html">
            <h4 style="font-size:12px; margin:1em 0 0.5em 0;">Initiativets sammanfattning</h4>
            <p style="margin:0.5em 0;"><@eu.shortenText initiative.proposal "sv" "html" /></p>
            <p style="margin:0.5em 0;"><@eu.link viewUrlSv "Visa initiativets innehåll &rarr;" /></p>
        <#else>
            Sammandrag av invånarinitiativ:
            <@eu.shortenText initiative.proposal "sv" "text" />
            
            
            Visa initiativets innehåll:
            ${viewUrlSv}
        </#if>
    </#if>
</#macro>

</#escape>