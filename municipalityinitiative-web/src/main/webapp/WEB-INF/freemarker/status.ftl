<#import "components/layout.ftl" as l />

<#macro dateTime date="">
    <@compress single_line=true>
        <#if date?string!="">
            <#if date?is_hash>
                ${date.toString(springMacroRequestContext.getMessage('date-time.format'))!""}
            <#else>
                ${date?string(springMacroRequestContext.getMessage('date-time.format'))!""}
            </#if>
        </#if>
    </@compress>
</#macro>

 <#macro emailTime emailDto>
   <#if emailDto.succeeded.present>
        <@dateTime emailDto.succeeded.value/>
    <#elseif emailDto.lastFailed.present>
        <@dateTime emailDto.lastFailed.value/>
   <#else>
        TRIED - STATUS UNKNOWN
   </#if>
   <br/>
   ${emailDto.recipientsAsString}
 </#macro>

<#escape x as x?html> 
<@l.main "page.status">

    <h1>Status page</h1>
    <h1>Email sending: <#if isInFailureState>IN FAILURE STATE<#else>OK</#if></h1>
    <#if hasFailedEmails><h2>HAS FAILED EMAILS</h2></#if>

    <h3>Application</h3>
    <table class="data status">
        <tbody>
            <#list applicationInfoRows as infoRow>
            <tr>
                <td>${infoRow.key}</td>
                <td>${infoRow.value}</td>
            </tr>
            </#list>
        </tbody>
    </table>

    <#if showEmails??>

      <h3>Unsucceeded emails</h3>
      <table class="data status">
          <tbody>
              <#list notSucceededEmails as failedEmail>
              <tr>
                  <td><@emailTime failedEmail/></td>
                  <td>${failedEmail.subject}</td>
              </tr>
              </#list>
          </tbody>
          <form method="post" action="${urls.statusPage}"><input type="hidden" name="emails" value="true"><input type="submit" value="Resend all failed"></form>
      </table>

        <h3>Succeeded emails <a href="${urls.statusPage}?emails=${showEmails?number + 50}">More</a></h3>
        <table class="data status">
            <tbody>
                <#list succeededEmails as succeededEmail>
                <tr>
                  <td><@emailTime succeededEmail/></td>
                  <td>${succeededEmail.subject}</td>
                </tr>
                </#list>
            </tbody>
        </table>

    <#else>
        <p><a href="${urls.statusPage}?emails=0">Show emails</a></p>
    </#if>

    <h3>SchemaVersion</h3>
    <table class="data status">
        <tbody>
            <#list schemaVersionInfoRows as infoRow>
            <tr>
                <td>${infoRow.key}</td>
                <td>${infoRow.value}</td>
            </tr>
            </#list>
        </tbody>
    </table>

    <h3>System</h3>
    <table class="data status">
        <tbody>
            <#list systemInfoRows as infoRow>
            <tr>
                <td>${infoRow.key}</td>
                <td>${infoRow.value}</td>
            </tr>
            </#list>
        </tbody>
    </table>

    <h3>Configuration</h3>
    <table class="data status">
        <tbody>
            <#list configurationInfoRows as infoRow>
            <tr>
                <td>${infoRow.key}</td>
                <td>${infoRow.value}</td>
            </tr>
            </#list>
        </tbody>
    </table>

    <h3>Configuration Test</h3>
    <table class="data status">
        <tbody>
            <#list configurationTestInfoRows as infoRow>
            <tr>
                <td>${infoRow.key}</td>
                <td>${infoRow.value}</td>
            </tr>
            </#list>
        </tbody>
    </table>

</@l.main>
</#escape> 
