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
 *  COLLECTING,
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
        
        <@b.statusInfoComment "text" initiative.comment!"" />
        
        <@b.adminViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <p style="${pBothMargins!""}"><@u.message "email.status.info.ACCEPTED_BY_OM.description" /></p>
        
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
        <p style="${pBothMargins!""}"><@u.message "email.status.info.REJECTED_BY_OM.title" /></p>
        
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

<#elseif emailMessageType == EmailMessageType.COLLECTING>
    <#-- TEXT -->
    <#assign statusInfo><@u.message "email.status.info.COLLECTING.description" /></#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <p style="${pBothMargins!""}"><@u.message "email.status.info.COLLECTING.description" /></p>
        
        <#-- TODO: Localize / create a macro-->
        <h4 style="${h4!""}">${initiative.name!""}</h4>
        Aloite luotu Kuntalaisaloite.fi-palveluun: <@u.localDate initiative.createTime />
        Keräys aloitettu: 04.04.2013 [TODO]  
        
        <@b.publicViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.SENT_TO_MUNICIPALITY>
    <#-- TEXT -->
    <#assign statusInfo>
        <#-- TODO: Localize / create a macro-->
        <h4 style="${h4!""}">${initiative.name!""}</h4>
        Aloite luotu Kuntalaisaloite.fi-palveluun: <@u.localDate initiative.createTime />
        Lähetetty kuntaan: 04.04.2013
        
        [SISÄLTÖ]
        
        Yhteystiedot
        [YHTEYSTIEDOT]
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        Vähemmän koivuja Joutsan keskustaan
        Aloite luotu Kuntalaisaloite.fi-palveluun: 04.04.2013 
        Lähetetty kuntaan: 04.04.2013
        
        [SISÄLTÖ]
        
        Yhteystiedot
        [YHTEYSTIEDOT]    
    </#assign>
    
</#if>

