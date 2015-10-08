<#import "utils.ftl" as u />

<script type="text/javascript">
	<#if superSearchEnabled?? && superSearchEnabled>
		var showSuperSearch = true;
		var superSearchUrl = "${urls.getSuperSearchIframeUrl()}";
	<#else>
		var showSuperSearch = false;
	</#if>
</script>

<#if superSearchEnabled?? && superSearchEnabled>
	<style>
		#searchIframe {
			width: 100%;
			border: none;
			height: 45px;
			z-index: 999;
			position:absolute;
			top :0px;
		}
		.super-search-placeholder {
			position:relative;
			width: 100%;
			height: 45px;
			display: none;
		}

	</style>

	<script type="text/javascript">

		window.onmessage = function(e){
            if (e.origin === "${urls.superSearchIFrameOrigin()}") {
                var newHeight = e.data;
				if (newHeight) {
					$("#searchIframe").height(newHeight);
				}
			}
		};

	</script>

	<div class="super-search-placeholder"></div>

</#if>
<div class="om-header">
	<div id="headerNav" class="header-nav">
		<ul>
			<li>
				<a href="http://www.${(locale == "sv")?string("demokrati","demokratia")}.fi"><@u.message "otherServices.demokratia"/></a>

			<li>
				<a href="https://www.kansalaisaloite.fi/${locale}"><@u.message "otherServices.initiative"/></a>

			<li>
				<a class="active" href="${urls.baseUrl}/${locale}"><@u.message "otherServices.municipalityinitiative"/></a>

			<li>
				<a href="http://www.lausuntopalvelu.fi/"><@u.message "otherServices.lausuntopalvelu"/></a>

			<li>
				<a href="http://www.${(locale == "sv")?string("ungasideer","nuortenideat")}.fi"><@u.message "otherServices.nuortenIdeat"/></a>

			<li>
				<a href="http://www.otakantaa.fi/${locale}-FI"><@u.message "otherServices.otaKantaa"/></a>

			<li>
				<a href="http://www.vaalit.fi/${locale}"><@u.message "otherServices.vaalit"/></a>

			<li>
				<a href="http://www.yhdenvertaisuus.fi/${(locale == "sv")?string("vad_da_equality_fi","")}"><@u.message "otherServices.yhdenvertaisuus"/></a>
		</ul>
	</div>

</div>
