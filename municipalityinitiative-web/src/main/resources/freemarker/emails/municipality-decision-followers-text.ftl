<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@b.contentBlock type>
    <@b.municipalityDecision type />
</@b.contentBlock>

<@b.contentBlock type>
${urls.unsubscribe(initiativeId, removeHash)}
</@b.contentBlock>


<@u.spacer "15" />