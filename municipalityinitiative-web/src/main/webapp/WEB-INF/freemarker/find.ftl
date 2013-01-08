<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 * 
 * @param page is "page.find"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.find" />

<@l.main "page.find" pageTitle!"">

<h1><@u.message page /></h1>

<p>lorem ipsum dolor ...</p>

<div class="municipalities">


    <table>
    <tr>
         <th>Kunta</th><th>Otsikko</th><th>Teksti</th>
    </tr>
    <#list initiatives as initiative>
    <tr>
        <td>${initiative.municipalityName}</td>
        <td>${initiative.name}</td>
        <td>${initiative.proposal}</td>
    </tr>
    </#list>


</div>


</@l.main>


</#escape>