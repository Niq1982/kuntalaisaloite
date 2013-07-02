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

<@l.main page>

    <div class="msg-block cf">
        <h1><@u.message "login.om.title" /></h1>

        <form name="login" accept-charset="UTF-8" action="${springMacroRequestContext.requestUri}" method="post">
            <input type="hidden" name="CSRFToken" value="${CSRFToken!""}"/>
            
            <div class="input-block-content no-top-margin">
                <label for="u" class="input-header">
                    <@u.message "login.om.user" />
                </label>
                <input type="text" maxlength="30" class="medium-fixed" value="" name="u" id="u" autofocus />
            </div>
            <div class="input-block-content no-top-margin">
                <label for="u" class="input-header">
                    <@u.message "login.om.password" />
                </label>
                <input type="password" maxlength="30" class="medium-fixed" value="" name="p" id="p" />
            </div>
            <br class="clear" />
            
            <button type="submit" name="Login" value="<@u.message "common.login" />" class="small-button"><span class="small-icon next"><@u.message "common.login" /></span></button>
        </form>
        <br />
    </div>

</@l.main>
</#escape>