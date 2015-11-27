<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.initiative" />

"${initiative.name!""}"
${initiative.municipality.getLocalizedName(switchLocale!locale)!""}
<@u.message "email.sentToMunicipality" />

<@b.separator />

<@b.attachments type/>

<@b.separator />

<@u.message key="email.participantCount.total" /> ${initiative.participantCount!"0"}

<@b.separator />

<@b.contentBlock type>
    ${urls.unsubscribe(initiativeId, removeHash)}
</@b.contentBlock>


<@b.separator />

<@b.emailFooter type ".sentToMunicipality" />
