<#import "email-utils.ftl" as u />
<#import "email-blocks.ftl" as b />

<#include "../includes/styles.ftl" />

<#--
 * Includes common status infos for generic emails.
 *
 * Includes both TEXT and HTML versions. TEXT version is used as a default value.
 *
 * Types:
 *  SENT_TO_REVIEW
 *  SENT_FIX_TO_REVIEW
 *  ACCEPTED_BY_OM_AND_SENT
 *  ACCEPTED_BY_OM
 *  ACCEPTED_BY_OM_FIX
 *  REJECTED_BY_OM
 *  INVITATION_ACCEPTED: TODO
 *  INVITATION_REJECTED: TODO
 *  PUBLISHED_COLLECTING
 *  SENT_TO_MUNICIPALITY
 *
-->

<#macro statusInfo messageType renderType="html">

<#assign messageKeyPrefix = "email.status.info."+messageType />

<#if messageType == EmailMessageType.SENT_TO_REVIEW>
    <#if renderType == "html">
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description.2"/></p>

        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails "html" />
    <#else>
        <@u.message messageKeyPrefix+".description"/>

        <@u.message messageKeyPrefix+".description.2" />
        
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails "text" />
    </#if>

<#elseif messageType == EmailMessageType.SENT_FIX_TO_REVIEW>
    <#if renderType == "html">
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description.2"/></p>

        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails "html" />
    <#else>
        <@u.message messageKeyPrefix+".description"/>
        
        <@u.message messageKeyPrefix+".description.2" />
        
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails "text" />
    </#if>
<#elseif messageType == EmailMessageType.ACCEPTED_BY_OM_AND_SENT>
    <#if renderType == "html">
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description.2" /> <strong>${initiative.municipality.getLocalizedName(locale)!""}</strong>.</p>

        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails "html" />
        <@b.publicViewLink "html" />
    <#else>
        <@u.message messageKeyPrefix+".description" />
        
        <@u.message messageKeyPrefix+".description.2" /> ${initiative.municipality.getLocalizedName(locale)!""}.
        
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails "text" />
        
        <@b.publicViewLink "text" />
    </#if>
    
<#elseif messageType == EmailMessageType.ACCEPTED_BY_OM>
    <#if renderType == "html">
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description.2"/></p>
        
        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails "html" />
    <#else>
        <@u.message messageKeyPrefix+".description"/>
        
        <@u.message messageKeyPrefix+".description.2" />
        
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails "text" />
    </#if>

<#elseif messageType == EmailMessageType.ACCEPTED_BY_OM_FIX>
    <#if renderType == "html">
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description.2"/></p>
        
        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails "html" />
    <#else>
        <@u.message messageKeyPrefix+".description"/>
        
        <@u.message messageKeyPrefix+".description.2" />
        
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails "text" />
    </#if>
    
<#elseif messageType == EmailMessageType.REJECTED_BY_OM>
    <#if renderType == "html">
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description" /></p>
        
        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails "html" />
    <#else>
        <@u.message messageKeyPrefix+".description" />
    
    
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails "text" />
    </#if>

<#elseif messageType == EmailMessageType.INVITATION_ACCEPTED>
    <#-- TEXT -->
    <#assign statusInfo>
        TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        TODO
    </#assign>

<#elseif messageType == EmailMessageType.INVITATION_REJECTED>
    <#-- TEXT -->
    <#assign statusInfo>
        TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        TODO
    </#assign>

<#elseif messageType == EmailMessageType.PUBLISHED_COLLECTING>
    <#if renderType == "html">
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        
        <@b.initiativeDetails "html" />  
        
        <@b.publicViewLink "html" />
    <#else>
        <@u.message messageKeyPrefix+".description"/>
        
        <@b.initiativeDetails "text" />  
        
        <@b.publicViewLink "text" />
    </#if>

<#elseif messageType == EmailMessageType.SENT_TO_MUNICIPALITY>
    <#if renderType == "html">
        <@b.initiativeDetails type="html" showProposal=true showDate=true showExtraInfo=true />
        

        <h4 style="${h4!""}"><@u.message "email.municipality.emailAddress" /></h4>
        <p style="${pBottomMargin!""}"><@u.link "mailto:"+municipalityEmail!"" municipalityEmail!"" /></p>
    <#else>
        <@b.initiativeDetails type="text" showProposal=true showDate=true showExtraInfo=true />
        
        ----
        
        <@u.message "email.municipality.emailAddress" />
        ${municipalityEmail!""}
    </#if>
    
</#if>

</#macro>