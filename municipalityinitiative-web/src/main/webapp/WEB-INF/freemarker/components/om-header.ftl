<#import "utils.ftl" as u />

<#if superSearchEnabled>
	<style>
		#searchIframe {
			width: 100%;
			border: none;
			height: 45px;
			z-index: 999;
			position:absolute;
			top :0px;
		}
		.placeholder {
			position:relative;
			width: 100%;
			height: 45px;
		}

	</style>

	<script type="text/javascript">

		window.onmessage = function(e){
            if (e.origin === "${urls.superSearchIFrameOrigin()}") {
                var message = e.data;
				if (message) {
                    var height = message.height;
                    if (height) {
                        $("#searchIframe").height(height);
                    }
				}

			}

		};

	</script>
	<div class="placeholder">
		<iframe id="searchIframe" src=${urls.getSuperSearchIframeUrl()}> </iframe>
    </div>
<#else>
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
</#if>