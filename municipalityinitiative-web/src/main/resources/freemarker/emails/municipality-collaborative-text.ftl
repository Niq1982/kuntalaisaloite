<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<#if (initiative.sentComment)?has_content>
    <@b.comment type initiative.sentComment "email.sentComment" />
</#if>

<@u.message "email.initiative" />


<@b.initiativeDetails type=type showProposal=true showDate=true showExtraInfo=true />

<@b.separator />

<@b.authorList type />

<@b.separator />

<@b.participants type />

<@b.separator />

<@b.attachments type/>

<@b.separator />

<@b.emailFooter type ".sentToMunicipality" />
