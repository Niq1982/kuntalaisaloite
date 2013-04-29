<#import "../components/email-utils.ftl" as u />
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

<#assign messageKeyPrefix = "email.status.info."+emailMessageType />

<#if emailMessageType == EmailMessageType.ACCEPTED_BY_OM_AND_SENT>
    <#-- TEXT -->
    <#assign statusInfo>
        <@u.message messageKeyPrefix+".description" />
        
        <@u.message messageKeyPrefix+".description.2" /> ${initiative.municipality.getLocalizedName(locale)!""}.
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails type="text" showProposal=false showDate=false />
        
        <@b.publicViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description.2" /> <strong>${initiative.municipality.getLocalizedName(locale)!""}</strong>.</p>

        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails type="html" showProposal=false showDate=false />
        <@b.publicViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.ACCEPTED_BY_OM>
    <#-- TEXT -->
    <#assign statusInfo>
        <@u.message messageKeyPrefix+".description"/>
        
        <@u.message messageKeyPrefix+".description.2" />
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails type="text" showProposal=false showDate=false />
        
        <@b.adminViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description.2"/></p>
        
        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails type="html" showProposal=false showDate=false />
        <@b.adminViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.REJECTED_BY_OM>
    <#-- TEXT -->
    <#assign statusInfo>
        <@u.message messageKeyPrefix+".title"/>
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails type="text" showProposal=false showDate=false />

        <@b.adminViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description" /></p>
        
        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails type="html" showProposal=false showDate=false />
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
        <@u.message messageKeyPrefix+".description"/>
        
        <@b.initiativeDetails type="text" showProposal=false showDate=false />  
        
        <@b.publicViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTML>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        
        <@b.initiativeDetails type="html" showProposal=false showDate=false />  
        
        <@b.publicViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.SENT_TO_MUNICIPALITY>
    <#-- Optional title overrides default title -->
    <#assign statusTitle><@u.message messageKeyPrefix+".title" /> ${initiative.municipality.getLocalizedName(locale)!""}</#assign>
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



<#--
 *
 * Use global variable to switch to use swedish localizations
 *
-->
<#global switchLocale = altLocale />

<#if emailMessageType == EmailMessageType.ACCEPTED_BY_OM_AND_SENT>
    <#-- TEXT -->
    <#assign statusInfoSv>
        <@u.message messageKeyPrefix+".description" />
        
        <@u.message messageKeyPrefix+".description.2" /> ${initiative.municipality.getLocalizedName(switchLocale!locale)!""}.
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails type="text" showProposal=false showDate=false />
        
        <@b.publicViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTMLSv>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description.2" /> <strong>${initiative.municipality.getLocalizedName(switchLocale!locale)!""}</strong>.</p>

        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails type="html" showProposal=false showDate=false />
        <@b.publicViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.ACCEPTED_BY_OM>
    <#-- TEXT -->
    <#assign statusInfoSv>
        <@u.message messageKeyPrefix+".description"/>
        
        <@u.message messageKeyPrefix+".description.2" />
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails type="text" showProposal=false showDate=false />
        
        <@b.adminViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTMLSv>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description.2"/></p>
        
        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails type="html" showProposal=false showDate=false />
        <@b.adminViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.REJECTED_BY_OM>
    <#-- TEXT -->
    <#assign statusInfoSv>
        <@u.message messageKeyPrefix+".title"/>
        
        <@b.statusInfoComment "text" initiative.moderatorComment!"" />
        
        <@b.initiativeDetails type="text" showProposal=false showDate=false />

        <@b.adminViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTMLSv>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description" /></p>
        
        <@b.statusInfoComment "html" initiative.moderatorComment!"" />
        <@b.initiativeDetails type="html" showProposal=false showDate=false />
        <@b.adminViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.INVITATION_ACCEPTED>
    <#-- TEXT -->
    <#assign statusInfoSv>
        TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTMLSv>
        TODO
    </#assign>

<#elseif emailMessageType == EmailMessageType.INVITATION_REJECTED>
    <#-- TEXT -->
    <#assign statusInfoSv>
        TODO
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTMLSv>
        TODO
    </#assign>

<#elseif emailMessageType == EmailMessageType.PUBLISHED_COLLECTING>
    <#-- TEXT -->
    <#assign statusInfoSv>
        <@u.message messageKeyPrefix+".description"/>
        
        <@b.initiativeDetails type="text" showProposal=false showDate=false />  
        
        <@b.publicViewLink "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTMLSv>
        <p style="${pBothMargins!""}"><@u.message messageKeyPrefix+".description"/></p>
        
        <@b.initiativeDetails type="html" showProposal=false showDate=false />  
        
        <@b.publicViewLink "html" />
    </#assign>
    
<#elseif emailMessageType == EmailMessageType.SENT_TO_MUNICIPALITY>
    <#-- Optional title overrides default title -->
    <#assign statusTitleSv><@u.message messageKeyPrefix+".title" /> ${initiative.municipality.getLocalizedName(switchLocale!locale)!""}</#assign>
    <#-- TEXT -->
    <#assign statusInfoSv>
    
        <#-- TODO: sent date: initiative.sentTime.value -->
        <@b.initiativeDetails "text" />
        
        <@b.contactInfo "text" />
    </#assign>
    
    <#-- HTML -->
    <#assign statusInfoHTMLSv>
        <#-- TODO: sent date initiative.sentTime.value -->
        <@b.initiativeDetails "html" />
        
        <@b.contactInfo "html" />
    </#assign>
    
</#if>

<#global switchLocale = locale />