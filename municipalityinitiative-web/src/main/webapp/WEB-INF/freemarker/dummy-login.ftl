<#import "/spring.ftl" as spring />
<#import "components/forms.ftl" as f />
<#import "components/layout.ftl" as l />
<#import "components/utils.ftl" as u />
<#import "components/edit-blocks.ftl" as edit />

<#escape x as x?html>

<#--
 * Layout parameters for HTML-title and navigation.
 *
 * @param page is "page.prepare"
 * @param pageTitle can be assigned as custom HTML title
-->
<#assign page="page.prepare" />

<#--
 * Get current municipality as request parameter.
 * - Request parameter is for iFrame create-link
 * - We could want to try to guess user's municipality later on with different methods like GEO location, user's history etc..
-->
<#assign currentMunicipality = RequestParameters['municipality']!"" />

<@l.main page pageTitle!"">

    <script type="text/javascript">
        function submitForm(user, pswd){
            document.getElementById("u").setAttribute("value",user);
            document.getElementById("p").setAttribute("value",pswd);
            
            document.login.submit();
        }
    </script>

    <form name="login" accept-charset="UTF-8" action="${urls.moderatorLogin()}" method="post">
        <p>Om-tunnuksilla kirjautuminen: <b>admin</b> + <b>admin</b>, <a href="#" onClick="submitForm('admin', 'admin');">kirjaudu</a></p>
        <p>Muilla-tunnuksilla kirjautuminen: <b>user</b> + <b>user</b>, <a href="#" onClick="submitForm('user', 'user');">kirjaudu</a></p>
        
        <br/>

        <h4>Sisäänkirjautuminen</h4>

        <input type="hidden" name="CSRFToken" value="${CSRFToken!""}"/>
        <div>Tunnus: <input type="text" name="u" id="u" /></div>
        <div>Salasana: <input type="password" name="p" id="p" /></div>
        <input type="submit" name="Login" value="Kirjaudu"/>
    </form>

</@l.main>
</#escape>