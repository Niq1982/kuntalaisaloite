<#import "../components/layout.ftl" as l />
<#import "../components/utils.ftl" as u />

<#escape x as x?html>
<@l.error "error.410.title">

    <h1><@u.message "error.410.title"/></h1>
    <p><@u.messageHTML key=errorMessage/></p>

</@l.error>
</#escape>