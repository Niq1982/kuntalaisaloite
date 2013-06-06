<#import "/spring.ftl" as spring />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * @param page is "page.login"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.login" />

<@l.main page pageTitle!"">

    <div class="msg-block">
        <h1><@u.message "login.title" /></h1>
    
        <p><@u.message "login.description" /></p>
    
        <form action="${urls.login()}" method="post">
            <input type="hidden" name="CSRFToken" value="${CSRFToken!""}"/>
            <input type="hidden" name="management" value="${managementHash}"/>
            
            <button type="submit" name="Login" value="<@u.message "login.continue" />" class="small-button"><span class="small-icon next"><@u.message "login.continue" /></span></button>
        </form>
        
        <br/>
    </div>

</@l.main>
</#escape>
