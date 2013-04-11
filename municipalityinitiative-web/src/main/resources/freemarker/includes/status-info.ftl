<#import "../components/email-blocks.ftl" as b />

<#include "../includes/styles.ftl" />

<#--
 * Includes common status infos for generic emails.
 *
 * Includes both TEXT and HTML versions. TEXT version is used as a default value.
 *
 * Types:
 *  ACCEPTED_BY_OM_AND_SENT,
 *  ACCEPTED_BY_OM,
 *  REJECTED_BY_OM,
 *  INVITATION_ACCEPTED,
 *  INVITATION_REJECTED,
 *  PUBLISHED_COLLECTING,
 *  SENT_TO_MUNICIPALITY
 *
 *
-->

<#if emailMessageType == EmailMessageType.ACCEPTED_BY_OM_AND_SENT>
    <#-- TEXT -->
    <#assign statusInfo>
        <@u.message "email.status.info.ACCEPTED_BY_OM_AND_SENT.description" />
        
        <@u.message "email.status.info.ACCEPTED_BY_OM_AND_SENT.description.2" /> ${initiative.municipality.getLocalizedName(locale)!""}.
        
        <@b.statusInfoComment "text" initiative.comment!"" />
        
        <@b.publicViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <p style="${pBothMargins!""}"><@u.message "email.status.info.ACCEPTED_BY_OM_AND_SENT.description" /></p>
        <p style="${pBothMargins!""}"><@u.message "email.status.info.ACCEPTED_BY_OM_AND_SENT.description.2" /> <strong>${initiative.municipality.getLocalizedName(locale)!""}</strong>.</p>

        <@b.statusInfoComment "html" initiative.comment!"" />
        <@b.publicViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.ACCEPTED_BY_OM>
    <#-- TEXT -->
    <#assign statusInfo>
        <@u.message "email.status.info.ACCEPTED_BY_OM.description" />
        
        <@u.message "email.status.info.ACCEPTED_BY_OM.description.2" />
        
        <@b.statusInfoComment "text" initiative.comment!"" />
        
        <@b.adminViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <p style="${pBothMargins!""}"><@u.message "email.status.info.ACCEPTED_BY_OM.description" /></p>
        <p style="${pBothMargins!""}"><@u.message "email.status.info.ACCEPTED_BY_OM.description.2" /></p>
        
        <@b.statusInfoComment "html" initiative.comment!"" />
        <@b.adminViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.REJECTED_BY_OM>
    <#-- TEXT -->
    <#assign statusInfo>
        <@u.message "email.status.info.REJECTED_BY_OM.title" />
        
        <@b.statusInfoComment "text" initiative.comment!"" />

        <@b.adminViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <p style="${pBothMargins!""}"><@u.message "email.status.info.REJECTED_BY_OM.description" /></p>
        
        <@b.statusInfoComment "html" initiative.comment!"" />
        
        <@b.adminViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.INVITATION_ACCEPTED>
    <#-- TEXT -->
    <#assign statusInfo>
        TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        TODO
    </#assign>

<#elseif emailMessageType == EmailMessageType.INVITATION_REJECTED>
    <#-- TEXT -->
    <#assign statusInfo>
        TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        TODO
    </#assign>

<#elseif emailMessageType == EmailMessageType.PUBLISHED_COLLECTING>
    <#-- TEXT -->
    <#assign statusInfo>
        <@u.message "email.status.info.PUBLISHED_COLLECTING.description" />
        
        <@b.initiativeDetails type="text" showProposal=false />  
        
        <@b.publicViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <p style="${pBothMargins!""}"><@u.message "email.status.info.PUBLISHED_COLLECTING.description" /></p>
        
        <#-- TODO: publish date: initiative.stateTime-->
        <@b.initiativeDetails type="html" showProposal=false />  
        
        <@b.publicViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.SENT_TO_MUNICIPALITY>
    <#-- Optional title overrides default title -->
    <#assign statusTitle><@u.message "email.status.info."+emailMessageType+".title" /> ${initiative.municipality.getLocalizedName(locale)!""}</#assign>
    <#-- TEXT -->
    <#assign statusInfo>
    
        <#-- TODO: sent date: initiative.sentTime.value -->
        <@b.initiativeDetails "text" />
        
        <@b.contactInfo "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <#-- TODO: sent date initiative.sentTime.value -->
        <@b.initiativeDetails "html" />
        
        <@b.contactInfo "html" />
    </#assign>
    
</#if>

