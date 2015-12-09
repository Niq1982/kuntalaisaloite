<#import "email-utils.ftl" as u />

<#include "../includes/styles.ftl" />

<#escape x as x?html>

<#macro mainContentBlock title="">

    <table border="0" cellspacing="0" cellpadding="0" width="640" style="${blockBGcolor!""}; text-align:left; ${defaultFont!""}">
        <tr style="color:${blockBGcolor!""};">
            <td width="20" style="background:${blockBGcolor!""};"><@u.spacer "0" /></td>
            <td width="550" style="background:${blockBGcolor!""}; text-align:left;"><@u.spacer "15" /><h4 style="${bigTitle!""}">${title}</h4></th>
            <td width="20" style="background:${blockBGcolor!""};"><@u.spacer "0" /></td>
        </tr>
        <tr>
            <td colspan="3" style="background:${blockBGcolor!""};">
            <table border="0" cellspacing="0" cellpadding="0" style="width:100%;">
                <tr>
                    <td width="20" style="background:${blockBGcolor!""};"><@u.spacer "0" /></td>
                    <td style="background:${blockBGcolor!""}; ${defaultFont!""}; text-align:left;">

                        <#-- Email content -->
                        <#nested />
                                
                        <@u.spacer "5" />
                    </td>
                    <td width="20" style="background:${blockBGcolor!""};"><@u.spacer "0" /></td>
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

        <table border="0" cellspacing="0" cellpadding="0" width="640" style="background:${blockBGcolor!""}; text-align:left; ${defaultFont!""}">
            <tr>
                <td width="640" style="background:${blockBGcolor!""}; text-align:left;">
                <table border="0" cellspacing="0" cellpadding="0" style="width:100%; text-align:left;">
                    <tr>
                        <td width="20" style="background:${blockBGcolor!""};"><@u.spacer "0" /></td>
                        <td style="background:${blockBGcolor!""}; ${defaultFont!""}; text-align:left;">
                            <@u.spacer "5" />
                            
                            <#-- HTML content -->
                            <#nested />

                            <@u.spacer "5" />
                        </td>
                        <td width="20" style="background:${blockBGcolor!""};"><@u.spacer "0" /></td>
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
 -->
<#macro emailFooter type="" postFix="">
    <#if type=="html">
        <table border="0" cellspacing="0" cellpadding="0" width="640" style="border:0;">
        <tr>
            <td style="text-align:center; ${footerFont!""}">
    
                <p style="${footerFont!""}">
                    <@u.message "email.footer.sendFrom"+postFix />
                    <br/>
                    <#if initiative.state?? && initiative.state == "PUBLISHED" && initiative.fixState == "OK">
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
        
        <#if initiative.state?? && initiative.state == "PUBLISHED" && initiative.fixState == "OK">
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
        <#if showDate>
            <p style="${pBothMargins!""}"><@u.message "email.date.create" /> <@u.localDate initiative.createTime /></p>
            <p style="${pBothMargins!""}"><@u.message "email.date.published" /> <@u.localDate initiative.stateTime /></p>
        </#if>
        <#if showProposal><@u.text initiative.proposal /></#if>
        
        <#if showExtraInfo && (initiative.extraInfo)?has_content>
            <h4 style="${h4!""}"><@u.message "email.extraInfo" /></h4>
            <p style="${pBottomMargin!""}">${initiative.extraInfo}</p>
        </#if>
    <#else>
        "${initiative.name!""}"
        ${initiative.municipality.getLocalizedName(switchLocale!locale)!""}

        <#if showDate>
            <@u.message "email.date.create" /> <@u.localDate initiative.createTime />
            <@u.message "email.date.published" /> <@u.localDate initiative.stateTime/>
        </#if>

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

<#macro attachments type>
    <#if (attachmentCount > 0) || hasLocationAttached || hasVideoAttached>
        <#if type == "html">
            <h4 style="${h4!""}">
                <#if (attachmentCount > 0)>
                    <@u.messageHTML key="email.attachmentCount.total" args=[attachmentCount] />
                </#if>
                <#if hasLocationAttached>
                    <@u.messageHTML key="email.mapAttached" args=[attachmentCount] />
                </#if>
                <#if hasVideoAttached>
                    <#if hasLocationAttached>
                        <@u.messageHTML key="email.videoAttached" args=[1] />
                    <#else>
                        <@u.messageHTML key="email.videoAttached" args=[attachmentCount] />
                    </#if>
                </#if>
            </h4>
            <#if (attachmentCount > 0)>
                <p style="${pBottomMargin!""}"><@u.message "email.attachmentCount.info" /></p>
            </#if>
            <#if hasLocationAttached>
                <p style="${pBottomMargin!""}"><@u.message "email.mapAttached.info" /></p>
            </#if>
            <#if hasVideoAttached>
                <p style="${pBottomMargin!""}"><@u.message "email.videoAttached.info" /></p>
            </#if>
        <#else>
            <#if (attachmentCount > 0)>
                <@u.message key="email.attachmentCount.total" /> ${attachmentCount}
            </#if>
            <#if hasLocationAttached>
                <@u.message key="email.mapAttached" /> ${attachmentCount}
            </#if>
            <#if hasVideoAttached>
                <#if hasLocationAttached>
                    <@u.message key="email.videoAttached" args=[1] />
                <#else>
                    <@u.message key="email.videoAttached" args=[attachmentCount] />
                </#if>
            </#if>
            <#if (attachmentCount > 0)>
                <@u.message "email.attachmentCount.info" />
            </#if>
            <#if hasLocationAttached>
                <@u.message "email.mapAttached.info" />
            </#if>
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
            <@u.text content=msg style=pBottomMargin />
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
            <@u.text content=msg style=pBottomMargin + " font-style:italic;" />
        <#else>
            <@u.message "email.commentFromOM" />:
            <@u.text msg/>
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
            <h4 style="${h4!""}"><@u.message "email.management.title" /></h4>
            <p style="${pBottomMargin!""}"><@u.message "email.management.description" /></p>
            <p style="${pBothMargins!""}">
                <#assign title><@u.message "email.management.linkLabel" /></#assign>
                <@u.link urls.get(switchLocale!locale).loginToManagement(initiative.id) title />
            </p>
        <#else>
            <@u.message "email.management.title" />
        
            <@u.message "email.management.description" />
            
            ${urls.get(switchLocale!locale).loginToManagement(initiative.id)}
        </#if>
    <#else>
        <#assign adminURL = urls.get(switchLocale!locale).loginAuthor(managementHash)>
        <#if type == "html">
            <h4 style="${h4!""}"><@u.message "email.management.title" /></h4>
            <p style="${pBottomMargin!""}"><@u.message "email.adminViewLink" /></p>
            <p style="${pBothMargins!""} ${smallFont!""}"><@u.link adminURL adminURL /></p>
        <#else>
            <@u.message "email.adminViewLink" />
            
            ${adminURL}
        </#if>
    </#if>
</#macro>


<#macro municipalityDecisionLink type>
    <#assign municipalityDecisionUrl = urls.get(locale).loginMunicipality(municipalityDecisionHash)/>

    <#if type == "html">
        <h4 style="${h4!""}"><@u.message "email.municipalityDecisionLink" /></h4>
        <@u.link municipalityDecisionUrl municipalityDecisionUrl /></span>
    <#else>
        <@u.message "email.municipalityDecisionLink" />
        ${municipalityDecisionUrl}
    </#if>

</#macro>


<#macro municipalityDecision type>
    <#if type == "html">
        <h4 style="${h4!""}">${initiative.name!""}</h4>
        <p style="${pBottomMargin!""}">${initiative.municipality.getLocalizedName(switchLocale!locale)!""}</p>
        <@u.message "email.municipality.answered.body" />
        <@publicViewLink type/>
    <#else>
        "${initiative.name!""}"
         ${initiative.municipality.getLocalizedName(switchLocale!locale)!""}
        <@u.message "email.municipality.answered.body" />
        <@publicViewLink type />
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