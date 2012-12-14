<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/flow-state.ftl" as flow />


<#escape x as x?html>

<#assign pageTitle><@u.messageHTML "initiative.searchm.public.title" /></#assign>

<@l.main "page.searchm" pageTitle!"">

<h1>${pageTitle}</h1>

<p>lorem ipsum dolor ...</p>


</@l.main>


</#escape>