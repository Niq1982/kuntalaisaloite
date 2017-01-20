<#escape x as x?html>

    <#macro generate_notification notificationData>
    <div class="debug-ribbon top fixed ribbon-red" style="position: fixed;">
        <div class="container">
            <div class="container">
                <span>${notificationData.text!""}</span>&nbsp;<#if notificationData.link??><a href="${notificationData.link}" target="_blank">${notificationData.linkText}</a></#if>
            </div>
        </div>
    </div>
    </#macro>


    <#if notificationEdit??>
        <@generate_notification notificationEdit/>
    <#elseif notification??>
        <@generate_notification notification/>
    </#if>

</#escape>