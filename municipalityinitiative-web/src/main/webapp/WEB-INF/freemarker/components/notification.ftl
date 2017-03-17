<#escape x as x?html>

    <#macro generate_notification notificationData>
    <div class="notification-container">
        <div class="notification-wrapper">
            <div class="notification">
                <span>${notificationData.text!""}</span>&nbsp;<#if notificationData.link??><a href="${notificationData.link}" target="_blank">${notificationData.linkText}</a></#if>
            </div>
        </div>
        <button id="notification-close-button" class="notification-close-button disable-dbl-click-check">
          <span class="button-text">SULJE</span>
          <span class="button-marker">X</span>
        </button>
    </div>
    </#macro>


    <#if notificationEdit??>
        <@generate_notification notificationEdit/>
    <#elseif notification??>
        <@generate_notification notification/>
    </#if>

</#escape>