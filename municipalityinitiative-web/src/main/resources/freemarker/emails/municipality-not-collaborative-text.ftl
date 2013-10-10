<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.initiative" /> - ${initiative.municipality.getLocalizedName(locale)!""}

<#if (initiative.sentComment)?has_content>
    <@b.comment type initiative.sentComment "email.sentComment" />
    <@b.separator />
    
</#if>

<@b.initiativeDetails type=type showProposal=true showDate=true showExtraInfo=true />

<@b.separator />

<@b.authorList type />

<@b.separator />

<@b.publicViewLink type />

<@b.separator />

<@b.attachments type/>

<@b.separator />

<@b.emailFooter type />
