<#escape x as x?html> 
<!DOCTYPE HTML>
<html>
<head>

</head>
<body>
    <form action="${urls.login()}" method="post">
        <b>Om-tunnuksilla kirjautuminen: admin + admin</b><br/>
        <b>Muilla-tunnuksilla kirjautuminen: user + user</b><br/>
        <br/>

        <h4>Sisäänkirjautuminen</h4>

        <input type="hidden" name="target" value="${target}">
        <input type="hidden" name="CSRFToken" value="${CSRFToken!""}"/>
        <div>Tunnus: <input type="text" name="u" value="admin" /></div>
        <div>Salasana: <input type="password" name="p" value="admin" /></div>
        <input type="submit" name="Login" value="Kirjaudu"/>
    </form>
</body>
</html>
</#escape> 
