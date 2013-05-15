<#import "../components/layout.ftl" as l />
<#import "../components/utils.ftl" as u />

<#escape x as x?html>
<@l.error errorMessage+".title">

    <h1><@u.message errorMessage+".description"/></h1>
    <p><@u.messageHTML key=errorMessage+".data"/></p>
    <p><img src="http://gtoss.com/wp-content/uploads/2013/03/cats_hiding_24.jpg"/></p>

</@l.error>
</#escape>