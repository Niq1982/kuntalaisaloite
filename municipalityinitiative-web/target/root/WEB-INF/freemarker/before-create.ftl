<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />

<#escape x as x?html> 
<@l.main "page.createNew">

    <h1><@u.message "page.beforeCreate" /></h1>

    <p><@u.message "beforeCreate.intro" /></p>

    <#--<p><@u.message "beforeCreate.introextra" /></p>-->

    <a href="${urls.login(urls.createNew())}" class="large-button gray"><@u.message "beforeCreate.authenticateAndCreate" /></a>
    <a href="${urls.helpIndex()}/<@u.message "HelpPage.INITIATIVE" />" class="large-button gray"><@u.message "beforeCreate.howToCreate" /></a>
    
    <br/><br/>
    
</@l.main>
</#escape> 
