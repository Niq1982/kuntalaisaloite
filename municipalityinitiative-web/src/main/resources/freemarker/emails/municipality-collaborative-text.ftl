<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<#if (initiative.sentComment)?has_content>
    <@b.comment type initiative.sentComment "email.sentComment" />
</#if>

<@u.message "email.initiative" />


<@b.initiativeDetails type=type showProposal=true showDate=true showExtraInfo=true />

----

<@b.authorList type />

----

<@b.participants type />

----

<@b.emailFooter type ".sentToMunicipality" />
