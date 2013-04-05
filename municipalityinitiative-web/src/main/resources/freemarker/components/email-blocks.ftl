<#import "email-utils.ftl" as u />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#macro mainContentBlock title="">

    <table border="0" cellspacing="0" cellpadding="0" width="640" style="${blockBGcolor!""}; border-radius:5px; text-align:left; ${defaultFont!""}">
        <tr style="color:${blockBGcolor!""};">
            <td width="20" style="background:${blockBGcolor!""};"><@u.spacer "0" /></td>
            <td width="550" style="background:${blockBGcolor!""}; text-align:left;"><@u.spacer "15" /><h4 style="${bigTitle!""}">${title}</h4></th>
            <td width="20" style="background:${blockBGcolor!""};"><@u.spacer "0" /></td>
        </tr>
        <tr>
            <td colspan="3" style="background:${blockBGcolor!""};">
            <table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
                <tr>
                    <td width="20" ><@u.spacer "0" /></td>
                    <td style="${defaultFont!""}">

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

</#macro>


<#--
 * contentBlock
 *
 * White content block
 *
 * @param type 'text' or 'html'
 -->
<#macro contentBlock type="">
    <#if type == "html">

        <table border="0" cellspacing="0" cellpadding="0" width="640" style="background:${blockBGcolor!""}; border-radius:5px; text-align:left; ${defaultFont!""}">
            <tr>
                <td width="640">
                <table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
                    <tr>
                        <td width="20" ><@u.spacer "0" /></td>
                        <td style="${defaultFont!""}">
                            <@u.spacer "5" />
                            
                            <#-- HTML content -->
                            <#nested />

                            <@u.spacer "5" />
                        </td>
                        <td width="20"><@u.spacer "0" /></td>
                    </tr>
                </table>
                </td>
            </tr>
        </table>

    <#else>
        <#nested />
    </#if>
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
        <h4 style="${h4!""}">${emailInfo.name!""}</h4>
        <p style="${pBottomMargin!""}"><@u.message "email.date.create" /> <@u.localDate emailInfo.createTime />
        <br/><@u.message "email.date.sent" /> <@u.localDate emailInfo.sentTime /></p>
        <@u.text emailInfo.proposal />
    <#else>
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
        <h4 style="${h4!""}"><@u.message "email.contact.info" /></h4>
        <p style="${pBottomMargin!""}">${emailInfo.contactInfo.name!""}<br/>
        <#if emailInfo.contactInfo.email?? && emailInfo.contactInfo.email != "">${emailInfo.contactInfo.email!""}<br/></#if>
        <#if emailInfo.contactInfo.phone?? && emailInfo.contactInfo.phone != "">${emailInfo.contactInfo.phone!""}<br/></#if>
        <#if emailInfo.contactInfo.address?? && emailInfo.contactInfo.address != "">${emailInfo.contactInfo.address!""}</#if>
        </p>
    <#else>
        <@u.message "email.contact.info" />:
        ${emailInfo.contactInfo.name!""}
        ${emailInfo.contactInfo.email!""}
        ${emailInfo.contactInfo.phone!""}
        ${emailInfo.contactInfo.address!""}
    </#if>
</#macro>

<#--
 * participants
 *
 * Print the participant counts.
 *
 * @param type 'text' or 'html'
 -->
<#macro participants type="">
    <#if type == "html">
        <h4 style="${h4!""}"><@u.messageHTML key="email.participantCount.total" /> ${emailInfo.participantCount!""}</h4>
        <p style="${pBothMargins!""}">
            <#if emailInfo.participantFranchiseCount?? && (emailInfo.participantFranchiseCount > 0)>
                <@u.message "email.participantCount.franchise.total" /> <strong>${emailInfo.participantFranchiseCount!""}</strong> 
            <#else>
                <@u.message key="email.participantCount.franchise.total.empty" />
            </#if>
            <br />
            <#if emailInfo.participantNoFranchiseCount?? && (emailInfo.participantNoFranchiseCount > 0)>
                <@u.message key="email.participantCount.noFranchise.total" /> <strong>${emailInfo.participantNoFranchiseCount!""}</strong>
            <#else>
                <@u.message key="email.participantCount.noFranchise.total.empty" />
            </#if>
        </p>
        <#if emailInfo.participantCount?? && (emailInfo.participantCount > 0)>
            <p style="${pBothMargins!""}"><@u.message "email.participantCount.attachment" /></p>
        </#if>
    <#else>
        <@u.message key="email.participantCount.total" /> ${emailInfo.participantCount!""}
        
        <#if emailInfo.participantFranchiseCount?? && (emailInfo.participantFranchiseCount > 0)>
            <@u.message key="email.participantCount.franchise.total" /> ${emailInfo.participantFranchiseCount!""}
        <#else>
            <@u.message key="email.participantCount.franchise.total.empty" />
        </#if>
        
        <#if emailInfo.participantNoFranchiseCount?? && (emailInfo.participantNoFranchiseCount > 0)>
            <@u.message key="email.participantCount.noFranchise.total" /> ${emailInfo.participantNoFranchiseCount!""}
        <#else>
            <@u.message key="email.participantCount.noFranchise.total.empty" />
        </#if>
        
        
        <#if emailInfo.participantCount?? && (emailInfo.participantCount > 0)>
            <@u.message "email.participantCount.attachment" />
        </#if>
    </#if>
</#macro>

<#--
 * comment
 *
 * Common initiative details for bottom section of the email.
 *
 * @param type 'text' or 'html'
 * @param msg is the textual comment
 -->
<#macro comment type="" msg="" key="">
    <#if type == "html">
        
        <@contentBlock "html">
            <h4 style="${h4!""}"><@u.message key /></h4>
            <p style="${pBottomMargin!""}">${msg}</p>
        </@contentBlock>
        
    <#else>
        <@u.message key />:
        ${msg}
    </#if>
</#macro>

<#--
 * Common blocks for status-info emails
 *
 * - statusInfoComment
 *
 -->
<#macro statusInfoComment type="" msg="">
    <#if type == "html">
        <h4 style="${h4!""}"><@u.message "email.commentFromOM" /></h4>
        <p style="${pBottomMargin!""}">${msg}</p>
    <#else>
        <@u.message "email.commentFromOM" />:
        ${msg}
    </#if>
</#macro>

<#macro publicViewLink type="">
    <#if type == "html">
        <p style="${pBothMargins!""}">
            Alla on linkki aloitteesi julkiselle sivulle Kuntalaisaloite.fi-palvelussa.<br />
            [LINKKI TÄHÄN]
        </p>
    <#else>
        Alla on linkki aloitteesi julkiselle sivulle Kuntalaisaloite.fi-palvelussa.
        [LINKKI TÄHÄN]
    </#if>
</#macro>

<#macro adminViewLink type="">
    <#if type == "html">
        <p style="${pBothMargins!""}">
            Siirry kuntalaisaloitteen ylläpito-sivulle alla olevalla linkillä<br/>
            [LAITETAANKO YLLÄPITO-LINKKI TÄHÄN?]
        </p>
    <#else>
        Siirry kuntalaisaloitteen ylläpito-sivulle alla olevalla linkillä
        [LAITETAANKO YLLÄPITO-LINKKI TÄHÄN?]
    </#if>
</#macro>

</#escape>