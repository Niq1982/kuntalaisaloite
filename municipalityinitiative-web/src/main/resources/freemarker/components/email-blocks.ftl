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
 * @param postFix
 * @param managementHash if given, managementlink will be shown instead of regular public link
 * @param showManagement default as true, used in verified initiatives
 -->
<#macro emailFooter type="" postFix="" managementHash="" showManagement=true>
    <#if type=="html">
        <table border="0" cellspacing="0" cellpadding="0" width="640" style="border:0;">
        <tr>
            <td style="text-align:center; ${footerFont!""}">
    
        <p style="${footerFont!""}"><@u.message "email.footer.sendFrom"+postFix /><br/>

        <#if showManagement && initiative.type.verifiable && !initiative.sent>
            <#assign title><@u.message "email.footer.managementLink" /></#assign>
            <@u.link urls.get(switchLocale!locale).loginToManagement(initiative.id) title />
        <#elseif managementHash?has_content && !initiative.sent>
            <@u.message "email.footer.managementLink" /><br/><@u.link urls.get(switchLocale!locale).loginAuthor(managementHash) />
        <#elseif initiative.state?? && initiative.state == "PUBLISHED" && initiative.fixState == "OK">
            <@u.message "email.footer.viewLink"+postFix /><br/><@u.link urls.get(switchLocale!locale).view(initiative.id) />
        </#if>
        </p>
        <br/>
        <p style="${footerFont!""}"><@u.message "email.footer" /></p>
        
        </td>
        </tr>
        </table>
    <#else>
        <@u.message "email.footer.sendFrom"+postFix />
        
        <#if showManagement && initiative.type.verifiable && !initiative.sent>
            <@u.message "email.footer.managementLink" />
            
            ${urls.get(switchLocale!locale).loginToManagement(initiative.id)}
        <#elseif managementHash?has_content && !initiative.sent>
            <@u.message "email.footer.managementLink" />
            
            ${urls.get(switchLocale!locale).loginAuthor(managementHash)}
        <#elseif initiative.state?? && initiative.state == "PUBLISHED" && initiative.fixState == "OK">
            <@u.message "email.footer.viewLink"+postFix />
            
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
<#macro initiativeDetails type="" showProposal=false showDate=false showExtraInfo=false>
    <#if type == "html">
        <h4 style="${h4!""}">${initiative.name!""}</h4>
        <p style="${pBottomMargin!""}">${initiative.municipality.getLocalizedName(switchLocale!locale)!""}</p>
        <#if showDate><p style="${pBothMargins!""}"><@u.message "email.date.create" /> <@u.localDate initiative.createTime /></p></#if>
        <#if showProposal><@u.text initiative.proposal /></#if>
        
        <#if showExtraInfo && (initiative.extraInfo)?has_content>
            <h4 style="${h4!""}"><@u.message "email.extraInfo" /></h4>
            <p style="${pBottomMargin!""}">${initiative.extraInfo}</p>
        </#if>
    <#else>
        "${initiative.name!""}"
        ${initiative.municipality.getLocalizedName(switchLocale!locale)!""}
        
        <#if showDate><@u.message "email.date.create" /> <@u.localDate initiative.createTime /></#if>

        <#if showProposal>${initiative.proposal}</#if>
        
        <#if showExtraInfo && (initiative.extraInfo)?has_content>
            <@u.message "email.extraInfo" />
            
            ${initiative.extraInfo}
        </#if>
    </#if>
</#macro>

<#--
 * authorList
 *
 * Contact details for all initiative authors
 *
 * @param type 'text' or 'html'
 -->
<#macro authorList type="">
    <#list authors as author>
        <#assign obj=author.contactInfo />
        <#if type == "html">
            <h4 style="${h4!""}"><@u.message key="email.authors" args=[authors?size] /></h4>
            <p style="${pBottomMargin!""}">${obj.name!""}, <@u.solveMunicipality municipality=author.municipality/><br/>
            <#if obj.email?? && obj.email != ""><@u.link "mailto:"+obj.email obj.email /><br/></#if>
            <#if obj.phone?? && obj.phone != "">${obj.phone!""}<br/></#if>
            <#if obj.address?? && obj.address != ""><#noescape>${obj.address?replace('\n','<br/>')!""}</#noescape><br/></#if>
            <br/>
            </p>
        <#else>
            <@u.message key="email.authors" args=[authors?size] />:
            ${obj.name!""}, <@u.solveMunicipality municipality=author.municipality/>
            ${obj.email!""}
            ${obj.phone!""}
            ${obj.address!""}
        </#if>
    </#list>
</#macro>

<#--
 * contactInfo
 *
 * Contact's name, email, phone and address
 *
 * @info is the contactInfo object
 * @param type 'text' or 'html'
 -->
<#macro contactInfo info type="">
    <#if type == "html">
        <p style="${pBottomMargin!""}">${info.name!""}<br/>
        <#if info.email?? && info.email != ""><@u.link "mailto:"+info.email info.email /><br/></#if>
        <#if info.phone?? && info.phone != "">${info.phone!""}<br/></#if>
        <#if info.address?? && info.address != ""><#noescape>${info.address?replace('\n','<br/>')!""}</#noescape><br/></#if>
        <br/>
        </p>
    <#else>
        ${info.name!""}
        ${info.email!""}
        ${info.phone!""}
        ${info.address!""}
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
        <@u.message key="email.participantCount.total" /> ${initiative.participantCount!"0"}

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

<#--
 * publicViewLink
 *
 -->
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

<#--
 * adminViewLink
 *
 -->
<#macro adminViewLink type="" verified=false>
    <#if verified>
        <#if type == "html">
            <p style="${pBothMargins!""}"><@u.message "email.adminViewLink" /></p>
            <p style="${pBothMargins!""}">
                <#assign title><@u.message "email.footer.managementLink" /></#assign>
                <@u.link urls.get(switchLocale!locale).loginToManagement(initiative.id) title />
            </p>
        <#else>
            <@u.message "email.adminViewLink" />
            
            ${urls.get(switchLocale!locale).loginToManagement(initiative.id)}
        </#if>
    <#else>
        <#assign adminURL = urls.get(switchLocale!locale).loginAuthor(managementHash)>
        <#if type == "html">
            <p style="${pBothMargins!""}"><@u.message "email.adminViewLink" /></p>
            <p style="${pBothMargins!""} ${smallFont!""}"><@u.link adminURL adminURL /></p>
        <#else>
            <@u.message "email.adminViewLink" />
            
            ${adminURL}
        </#if>
    </#if>
</#macro>

<#--
 * separator
 *
 * Plain text block separator
 -->
<#macro separator>
----------------------------------------------------------
</#macro>

</#escape>