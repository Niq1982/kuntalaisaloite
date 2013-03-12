<#import "components/layout.ftl" as l /> 
<#import "components/utils.ftl"as u /> 
<#escape x as x?html> <@l.main "page.api">

<h1>Open Data API</h1>

<p>The Open Data API provides the same information about initiatives as the site's user interface does. This service contains two Open Data access points: one for listing public initiatives and
one for details of an individual initiative. Both interfaces support <a href="http://www.json.org/">JSON</a> 
and <a href="http://en.wikipedia.org/wiki/JSONP">JSONP</a> formats.</p>

<h3>List of Initiatives</h3>

<p><a href="${urls.initiatives()}">${urls.initiatives()}</a></p>

<p>Returns a <a href="${urls.search()}">list</a> of initiatives with <a href="#list">Basic properties</a>.
Id of an initiative is an URI of initiative details in <a href="http://www.json.org/">JSON</a> format.</p>

<p>Parameters <tt>${UrlConstants.JSON_OFFSET}</tt> and <tt>${UrlConstants.JSON_LIMIT}</tt> may be used to restrict the results. ${UrlConstants.DEFAULT_INITIATIVE_JSON_RESULT_COUNT} initiatives will be returned by default. Maximum amount of initiatives to return is ${UrlConstants.MAX_INITIATIVE_JSON_RESULT_COUNT}.

<p>If <tt>${UrlConstants.JSON_MUNICIPALITY}</tt> parameter is given, only initiatives for given municipality id are returned. </p>

<p><a href="${urls.initiatives()}?${UrlConstants.JSON_OFFSET}=20&${UrlConstants.JSON_LIMIT}=10&${UrlConstants.JSON_MUNICIPALITY}=35">${urls.initiatives()}?${UrlConstants.JSON_OFFSET}=20&${UrlConstants.JSON_LIMIT}=10&${UrlConstants.JSON_MUNICIPALITY}=35</a></p>

<h3>Initiative Details</h3>
<p>${urls.baseUrl}${UrlConstants.INITIATIVE}</p>

<p><a href="#details">Details</a> of the initiative in <a href="http://www.json.org/">JSON</a> format.</p>

<h3>Municipalities</h3>
<p>All municipalities are listed at <a href="${urls.municipalities()}">${urls.initiatives()}</a>.

<h3>JSONP</h3>

<p>All interfaces support also <a href="http://en.wikipedia.org/wiki/JSONP">JSONP</a> format. Callback is given with <tt>${UrlConstants.JSONP_CALLBACK}</tt> parameter. E.g.<br/>
<a href="${urls.initiatives()}?${UrlConstants.JSONP_CALLBACK}=myCallback">${urls.initiatives()}?${UrlConstants.JSONP_CALLBACK}=myCallback</a>.</p>


<h3 id="details">Initiative Details</h3>

<table class="data">
  <thead>
    <tr>
        <th>Example
        </th>
        <th>Data Type
        </th>
        <th>Description
        </th>
    </tr>
  </thead>
  <tbody>
    <#list initiative as i>
    <tr>
        <td class="apiExample" style="padding-left:${i.indent}0px">
            ${i.value}
        </td>
        <td><@u.message i.localizationKey +".datatype" /></td>
        <td><@u.message i.localizationKey /></td>
        </tr>
    </#list>
  </tbody>
</table>

<h3 id="list">Basic properties for initiative list</h3>

<table class="data">
  <thead>
    <tr>
        <th>Example
        </th>
        <th>Data Type
        </th>
        <th>Description
        </th>
    </tr>
  </thead>
  <tbody>
    <#list initiativeList as i>
    <tr>
        <td class="apiExample" style="padding-left:${i.indent}0px">
            ${i.value}
        </td>
        <td><@u.message i.localizationKey +".datatype" /></td>
        <td><@u.message i.localizationKey /></td>
        </tr>
    </#list>
  </tbody>
</table>

<h3 id="list">Municipalities</h3>

<table class="data">
  <thead>
    <tr>
        <th>Example
        </th>
        <th>Data Type
        </th>
        <th>Description
        </th>
    </tr>
  </thead>
  <tbody>
    <#list municipalities as i>
    <tr>
        <td class="apiExample" style="padding-left:${i.indent}0px">
            ${i.value}
        </td>
        <td><@u.message i.localizationKey +".datatype" /></td>
        <td><@u.message i.localizationKey /></td>
        </tr>
    </#list>
  </tbody>
</table>

</@l.main> </#escape>
