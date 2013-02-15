<#import "../components/layout.ftl" as l />
<#import "../components/utils.ftl" as u />

<#escape x as x?html> 
<@l.main "page.news">
 
    <h1><@u.message "news.title" /></h1>
 
 <#if locale == "fi">
 

    
<#else>



</#if>
    
</@l.main>
</#escape> 

