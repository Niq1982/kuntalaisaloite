<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@u.message "email.followConfirm.title" />

"${initiative.name!""}"
${initiative.municipality.getLocalizedName(switchLocale!locale)!""}

<@b.separator />

<@b.contentBlock type>
<@u.message "email.followConfirm" />
<@u.messageHTML key="email.followConfirm.text" />
</@b.contentBlock>

<@b.separator />

<@b.contentBlock type>
${urls.unsubscribe(initiativeId, removeHash)}
</@b.contentBlock>

