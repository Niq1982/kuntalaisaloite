<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<#if (initiative.sentComment)?has_content>
    <@b.comment type initiative.sentComment "email.sentComment" />
</#if>

<@u.message "email.initiative" />


<@b.initiativeDetails type=type showProposal=true showDate=true showExtraInfo=true />

<@b.separator />

<@b.separator />

<@b.attachments type/>

<@b.separator />

<@b.contentBlock type>
    ${urls.unsubscribe(initiativeId, removeHash)}
</@b.contentBlock>


<@b.separator />

<@b.emailFooter type ".sentToMunicipality" />
