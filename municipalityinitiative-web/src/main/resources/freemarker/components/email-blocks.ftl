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
 * emailFooter
 *
 * Prints footer texts in email.
 * Show view link if initiative is published
 *
 * @param type 'text' or 'html'
 -->
<#macro emailFooter type="" postFix="">
    <#if type=="html">
        <p style="${footerFont!""}"><@u.message "email.footer.sendFrom"+postFix />
        <#if initiative.state?? && initiative.state == "PUBLISHED">
            <@u.message "email.footer.viewLink" /><br/><@u.link urls.get(switchLocale!locale).view(initiative.id) />
        </#if>
        </p>
        <br/>
        <p style="${footerFont!""}"><@u.message "email.footer" /></p>
    <#else>
        <@u.message "email.footer.sendFrom"+postFix />
        <#if initiative.state?? && initiative.state == "PUBLISHED">
            <@u.message "email.footer.viewLink" />
            
            ${urls.get(switchLocale!locale).view(initiative.id)}
        </#if>
        
        <@u.message "email.footer" />
    </#if>
</#macro>

<#--
 * initiativeDetails
 *
 * Common initiative details for top section of the email.
 *
 * @param type 'text' or 'html'
 -->
<#macro initiativeDetails type="" showProposal=true showDate=true>
    <#if type == "html">
        <h4 style="${h4!""}">${initiative.name!""}</h4>
        <p style="${pBottomMargin!""}">${initiative.municipality.getLocalizedName(switchLocale!locale)!""}</p>
        <#if showDate><p style="${pBothMargins!""}"><@u.message "email.date.create" /> <@u.localDate initiative.createTime /></#if>
        <#--<br/><@u.message "email.date.sent" /> <@u.localDate initiative.sentTime />--></p>
        <#if showProposal><@u.text initiative.proposal /></#if>
    <#else>
        "${initiative.name!""}"
        ${initiative.municipality.getLocalizedName(switchLocale!locale)!""}
        
        <#if showDate><@u.message "email.date.create" /> <@u.localDate initiative.createTime /></#if>

        <#if showProposal>${initiative.proposal}</#if>
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
    <#assign obj=initiative.author.contactInfo />
    <#if type == "html">
        <h4 style="${h4!""}"><@u.message "email.contact.info" /></h4>
        <p style="${pBottomMargin!""}">${obj.name!""}<br/>
        <#if obj.email?? && obj.email != ""><@u.link "mailto:"+obj.email obj.email /><br/></#if>
        <#if obj.phone?? && obj.phone != "">${obj.phone!""}<br/></#if>
        <#if obj.address?? && obj.address != ""><#noescape>${obj.address?replace('\n','<br/>')!""}</#noescape></#if>
        </p>
    <#else>
        <@u.message "email.contact.info" />:
        ${obj.name!""}
        ${obj.email!""}
        ${obj.phone!""}
        ${obj.address!""}
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
        <h4 style="${h4!""}"><@u.messageHTML key="email.participantCount.total" /> ${initiative.participantCount!"0"}</h4>
        <#if initiative.participantCount?? && (initiative.participantCount > 0)>
            <p style="${pBottomMargin!""}"><@u.message "email.participantCount.attachment" /></p>
        </#if>
    <#else>
        <@u.message key="email.initiative.participantCount" /> ${initiative.participantCount!"0"}

        <#if initiative.participantCount?? && (initiative.participantCount > 0)>
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
    <#if msg != "">
        <#if type == "html">
            <h4 style="${h4!""}"><@u.message "email.commentFromOM" /></h4>
            <p style="${pBottomMargin!""} font-style:italic;">${msg}</p>
        <#else>
            <@u.message "email.commentFromOM" />:
            ${msg}
        </#if>
    </#if>
</#macro>

<#macro publicViewLink type="">
    <#assign viewURL = urls.get(switchLocale!locale).view(initiative.id) />

    <#if type == "html">
        <p style="${pBothMargins!""}"><@u.message "email.publicViewLink" /><br/>
        <@u.link viewURL viewURL /></span>
    <#else>
        <@u.message "email.publicViewLink" />
        
        ${viewURL}
    </#if>
</#macro>

<#macro adminViewLink type="">
    <#assign adminURL = urls.get(switchLocale!locale).loginAuthor(initiative.id, initiative.managementHash.value) />
    
    <#if type == "html">
        <p style="${pBothMargins!""}"><@u.message "email.adminViewLink" /></p>
        <p style="${pBothMargins!""} ${smallFont!""}"><@u.link adminURL adminURL /></p>
    <#else>
        <@u.message "email.adminViewLink" />
        
        ${adminURL}
    </#if>
</#macro>

</#escape>