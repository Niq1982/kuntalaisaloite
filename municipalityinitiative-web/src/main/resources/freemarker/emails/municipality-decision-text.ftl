<#import "../components/email-utils.ftl" as u />
<#import "../components/email-blocks.ftl" as b />

<#assign type="text" />

<@b.contentBlock type>
Kunta on vastannut aloitteeseesi palvelussa. Käy katsomassa vastaus osoitteessa:
    <@b.publicViewLink type />
</@b.contentBlock>


<@u.spacer "15" />