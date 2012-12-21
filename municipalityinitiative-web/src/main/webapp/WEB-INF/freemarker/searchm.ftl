<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />


<#escape x as x?html>

<#assign pageTitle><@u.messageHTML "initiative.searchm.public.title" /></#assign>

<@l.main "page.searchm" pageTitle!"">

<h1>${pageTitle}</h1>

<p>lorem ipsum dolor ...</p>

<div class="municipalities">


    <#list municipalities as municipality>
        ${municipality.name}<br/>
    </#list>


</div>


</@l.main>


</#escape>