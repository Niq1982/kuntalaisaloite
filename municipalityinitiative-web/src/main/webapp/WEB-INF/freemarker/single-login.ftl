<#escape x as x?html>
<!DOCTYPE HTML>
<html>
<head>

</head>
<body>
    <form action="${urls.login(id)}" method="post">
        <b>Olet kirjautumassa sisään yksittäisen aloitteen tekijäksi<br/>
        <input type="hidden" name="CSRFToken" value="${CSRFToken!""}"/>
        <input type="hidden" name="management" value="${managementHash}"/>
        <input type="submit" name="Login" value="Kirjaudu"/>
    </form>
</body>
</html>
</#escape>
