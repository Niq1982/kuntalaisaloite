<#escape x as x?html> 
<!DOCTYPE HTML>
<html>
<head>

</head>
<body>

    <script type="text/javascript">
        function submitForm(user, pswd){
            document.getElementById("u").setAttribute("value",user);
            document.getElementById("p").setAttribute("value",pswd);
            
            document.login.submit();
        }
    </script>

    <form name="login" action="${urls.login()}" method="post">
        <p>Om-tunnuksilla kirjautuminen: <b>admin</b> + <b>admin</b>, <a href="#" onClick="submitForm('admin', 'admin');">kirjaudu</a></p>
        <p>Muilla-tunnuksilla kirjautuminen: <b>user</b> + <b>user</b>, <a href="#" onClick="submitForm('user', 'user');">kirjaudu</a></p>
        
        <br/>

        <h4>Sisäänkirjautuminen</h4>

        <input type="hidden" name="target" value="${target}">
        <input type="hidden" name="CSRFToken" value="${CSRFToken!""}"/>
        <div>Tunnus: <input type="text" name="u" id="u" /></div>
        <div>Salasana: <input type="password" name="p" id="p" /></div>
        <input type="submit" name="Login" value="Kirjaudu"/>
    </form>
</body>
</html>
</#escape> 
