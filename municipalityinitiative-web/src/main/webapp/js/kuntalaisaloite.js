var localization,
	generateModal,
	generateJsMessage,
	jsMessages;
/**
 * Localization
 * ==============
 * - Returns localized texts for JavaScript-elements.
 *
 * */
localization = {

	chosenNoResults:function(locale){
		if (locale === 'sv'){
			return "Inga träffar";
		} else {
			return "Ei tuloksia";
		}
	},
	getSubmitInfo:function(locale){
		if (locale === 'sv'){
			return "Det verkar räcka längre än väntat att utföra funktionen. Vänligen vänta en stund.";
		} else {
			return "Toiminnon suorittaminen näyttäisi kestävän odotettua kauemmin. Ole hyvä ja odota hetki.";
		}
	},
	getSubmitWarning:function(locale){
		if (locale === 'sv'){
			return "Det gick inte att utföra funktionen. Vänligen försök på nytt om några minuter.";
		} else {
			return "Toimintoa ei voitu suorittaa. Ole hyvä ja yritä uudelleen muutaman minuutin kuluttua.";
		}
	},
	magnificPopup:function(locale){
	  if (locale === 'sv'){
	    return {
        prev: 'SV: Edellinen (Vasen nuoli näppäin)', // title for left button
        next: 'SV: Seuraava (Oikea nuoli näppäin)', // title for right button
        image: 'Bild',
        loadError: 'SV: Kuvaa ei voitu ladata.'
      };
    } else {
      return {
        prev: 'Edellinen (Vasen nuoli näppäin)', // title for left button
        next: 'Seuraava (Oikea nuoli näppäin)', // title for right button
        image: 'Kuva',
        loadError: 'Kuvaa ei voitu ladata.'
      };
    }
	}
};

/**
 * Generate modal
 * ==============
 * - Gets modalContent containing all HTML
 * - Uses jsRender to render template into the modal container
 * - Shows modal template with the defined content
 *
 * */
generateModal = function (modalContent, modalType, callback) {
	$("#modal-container").html($("#modal-template").render(modalContent));
	$(".modal").loadModal(modalType, callback);
	return false;
};

/**
 * Generate jsMessage
 * ==============
 * - object contains:
 *  - HTML content inside the message-container
 *  - type of the message eg. info, success, warning, error
 * - print the message before the defined elem eg. form
 *
 * */
generateJsMessage = function (elem, object) {
	elem.before($("#jsMessage-template").render(object));
	return false;
};


/**
 *
 * jsMessage-loaders
 * =================
 *
 * System messages that are loaded for JS-users.
 *
 * Load once on page. Loaded separately also for modals.
 *
 * */
jsMessages = (function(){
	// Check if user has cookies enabled. Some mobile browsers do not support navigator.cookieEnabled
	var cookieEnabled = navigator.cookieEnabled ||
			("cookie" in document && (document.cookie.length > 0 ||
					(document.cookie = "cookieTest").indexOf.call(document.cookie, "cookieTest") > -1));

	return {
		Load: function(){
			if( !cookieEnabled && typeof messageData !== 'undefined' && typeof messageData.warningCookiesDisabled !== 'undefined' ){
				generateJsMessage($('form'), messageData.warningCookiesDisabled());
			}
		}
	};
}());
jsMessages.Load();


/**
 * Load chosen
 * ===========
 * Little helper for loading chosen
 *
 * - Chosen.js requires that first option is empty. We empty the default value which is for NOSCRIPT-users.
 * - Initialize chosen with localized text
 * - Set option for clearing selection
 *
 * */
jQuery.fn.loadChosen = function(){
	var self = $(this),
		allowSingleDeselect = typeof self.attr('data-allow-single-deselect') !== 'undefined' && self.attr('data-allow-single-deselect') !== false;

	self.find('option:first').text('');
	self.chosen(
		{
			no_results_text: localization.chosenNoResults(Init.getLocale()),
			allow_single_deselect: allowSingleDeselect
		}
	);
};

/**
 * Disable or enable button
 * ========================
 *
 * */
jQuery.fn.disableButton = function(disable){
	var btn = $(this);

	if (disable) {
		btn.attr('disabled','disabled').addClass('disabled');
	} else {
		btn.removeAttr('disabled').removeClass('disabled');
	}
};

/**
 * Common delay
 * ============
 *
 * */
var delay = (function(){
	var timer = 0;
	return function(callback, ms){
		clearTimeout (timer);
		timer = setTimeout(callback, ms);
	};
})();

$(document).ready(function () {
	// Define general variables
	var $body =			$('body'),
		speedFast =		'200',						// General speeds for animations
		speedVeryFast =	'10',
		speedSlow =		'slow',
		speedAutoHide =	'15000',					// Delay for hiding success-messages (if enabled)
		vpHeight =		$(window).height(),			// Viewport height
		vpWidth = window.innerWidth,          // Viewport width
		isIE6 =			$('html').hasClass('ie6'),	// Boolean for IE6. Used browser detection instead of jQuery.support().
		isIE7 =			$('html').hasClass('ie7'),	// Boolean for IE7. Used browser detection instead of jQuery.support().
		isIE8 =			$('html').hasClass('ie8'),	// Boolean for IE8. Used browser detection instead of jQuery.support().
		isIE9 =			$('html').hasClass('ie9'),	// Boolean for IE9. Used browser detection instead of jQuery.support().
		locale =		Init.getLocale(),			// Current locale: fi, sv
		hideClass =		'js-hide',					// Set general hidden class
		fireParticipantGraph, headerNav, mainNav;

/**
 * Common helpers
 * ==============
 */
	// Wait a while and hide removable message
	if ( $('.auto-hide').length > 0 ){
		setTimeout(function() {
			$('.auto-hide').fadeOut(speedSlow);
		}, speedAutoHide);
	}
	if (typeof showSuperSearch !== 'undefined' && showSuperSearch && !isIE6 && !isIE7 && !isIE8) {
		$(".super-search-placeholder").show();
		$(".super-search-placeholder").append("<iframe id=\"searchIframe\" src=" + superSearchUrl  + " ></iframe>");
		$(".om-header").hide();
	}
	// Validate emails
	var validateEmail = function (email) {
		var re;
		re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		return re.test(email);
	};

	// Switch content between element's HTML and data-alttext -attribute
	// When elem-argument is used, function switches content inside elem.
	jQuery.fn.switchContent = function(elem){
		var switcher, temp;

		if ( elem === null){
			elem = false;
		}

		switcher = $(this);
		temp = switcher.data('alttext');

		if (elem){
			switcher.data('alttext', elem.html());
			elem.html(temp);
		} else {
			switcher.data('alttext', switcher.html());
			switcher.html(temp);
		}
	};


	// Fire participantGraph
	if (window.participantGraph && window.participantGraph.votes.length > 0) {
   	//if (window.participantGraph) {
   	  fireParticipantGraph = function(){
        $('#participantGraph').html('').participantGraph({
          data: window.participantGraph,
          color: '#949426',
          colorHl: '#e2a31c',
          colorToolTip: '#7a7a20',
          width : $('#participantGraph').parent().width() || 960,
          height : 250,
          leftgutter : 35,
          rightgutter : 30,
          bottomgutter : 20,
          topgutter : 20,
          cumulative : true,
          max : 50000
        });
   	  };
    }

    // OM header navigation
    headerNav = $('#headerNav');
    headerNav.headerNav({
      btnTitle: locale === 'sv' ? 'Visa mer' : 'Näytä lisää'
    });



	/**
	 *	Prevent double clicks
	 *
	 *  To disable this feature use CSS class: disable-dbl-click-check
	 *  To disalbe only the messages use CSS class: dbl-click-check-no-msg
	 */
	$("button").live('click', function () {
		var btnClicked, firstBtnInForm, $loader, $submitInfo, $submitWarning;
		btnClicked = $(this);

		if (btnClicked.attr('class').indexOf('email-auth-btn') > 0) {
			return;
		}
		// Disable in some cases
		if (!btnClicked.hasClass('disable-dbl-click-check')){
			// We may have more than one submit button. For example in create.
			firstBtnInForm = btnClicked.parents('form').find('button:first');
			siblingButtons = btnClicked.siblings('.small-button, .large-button');
			$loader = $('<span class="loader" />');
			$submitInfo = $('<div class="system-msg msg-info">'+localization.getSubmitInfo(locale)+'</div>');
			$submitWarning = $('<div id="submit-warning" class="system-msg msg-warning">'+localization.getSubmitWarning(locale)+'</div>');

			$("#submit-warning").remove(); // Clear warning

			btnClicked.css('position','relative'); // Set button relative for loader animation position

			// Separate classes for styling and function, so that would not mess up classes and functions.
			if (!btnClicked.hasClass("clicked")){
				btnClicked.addClass("disabled clicked");
				siblingButtons.addClass("disabled clicked");
				siblingButtons.click(function(){
					return false;
				});
				btnClicked.append($loader);

				if (!btnClicked.hasClass('dbl-click-check-no-msg')){
  				setTimeout(function() {
  					firstBtnInForm.before($submitInfo);
  				}, 5000);
  				setTimeout(function() {
  					btnClicked.removeClass("disabled clicked");
  					siblingButtons.removeClass("disabled clicked");
  					$loader.remove();
  					$submitInfo.remove();
  					firstBtnInForm.before($submitWarning);
  				}, 30000);
				}

			} else {
				return false;
			}
		}
	});

	// IndexOF fix for IE8
	if (!Array.prototype.indexOf)
	{
		Array.prototype.indexOf = function(elt /*, from*/)
		{
			var len = this.length >>> 0;

			var from = Number(arguments[1]) || 0;
			from = (from < 0)
				? Math.ceil(from)
				: Math.floor(from);
			if (from < 0)
				from += len;

			for (; from < len; from++)
			{
				if (from in this &&
					this[from] === elt)
					return from;
			}
			return -1;
		};
	}
	// Filter fix for IE8
	if (!Array.prototype.filter)
	{
		Array.prototype.filter = function(fun /*, thisp */)
		{
			"use strict";

			if (this === void 0 || this === null)
				throw new TypeError();

			var t = Object(this);
			var len = t.length >>> 0;
			if (typeof fun !== "function")
				throw new TypeError();

			var res = [];
			var thisp = arguments[1];
			for (var i = 0; i < len; i++)
			{
				if (i in t)
				{
					var val = t[i]; // in case fun mutates this
					if (fun.call(thisp, val, i, t))
						res.push(val);
				}
			}

			return res;
		};
	}

	// Console fix for IE
	if (typeof console === "undefined") {
		console = { log: function() {} };
	}

	// Reveal system message
	$('.reveal-msg').addClass('animate'); // for CSS3 transition used in frontpage logout-message

	// Remove system message
	$('.close-msg').click(function(){
		var parent = $(this).closest('.js-close-msg-target').fadeOut('speedSlow', function(){
			parent.remove();
		});
	});

	$('.replace-links').linkReplacer();


	// Action for external links
	$('a.external').click(function(){
		window.open( $(this).attr('href') );
		return false;
	});

	// TOP ribbon
	/*var $topRibbon = $('.debug-ribbon.top.fixed');
	$topRibbon
	.after('<div style="height:'+$topRibbon.outerHeight()+'px" />')
	.css('position','fixed');*/

	// Remove elements from DOM
	function jsRemove(){
		$('.js-remove').remove();
	}
	jsRemove();


/**
 *	Toggle dropdown menus
 *  =====================
 * */
(function(){
	var $dToggle, $dMenu, clickedVisible;
	$dToggle = $('.dropdown-toggle');
	$dMenu = $('.dropdown-menu');

	$dToggle.click(function (e) {
		if( !$(this).next($dMenu).is(':visible') ){
			clickedVisible = true;
		} else {
			clickedVisible = false;
		}

		clearMenus();

		if( clickedVisible ){
			$(this)
			.addClass('active')
			.next($dMenu).show();
		}
		return false;
	});
	// Off clicking closes menus
	$('body').on('click', function(e){
		if( $dMenu.is(':visible') ){
			clearMenus();
		}
	});
	var clearMenus = function(){
		$dMenu.hide();
		$dToggle.removeClass('active');
	};
}());

/**
 *
 * Change the font-size
 * ====================
 * - Uses jQuery Cookie: https://github.com/carhartl/jquery-cookie
 * - Toggles a class for body-element (font-size-small, font-size-medium, font-size-large)
 * - Sets and gets fontSizeClass-cookie
 *
 * */
(function() {
	var $fontSizeToggler, fontSizeClass;

	$fontSizeToggler = $('.font-size-toggle a');
	fontSizeClass = "font-size-medium";

	// Get cookie value for the fontSize
	if( $.cookie("fontSizeClass") !== null ){
		fontSizeClass = $.cookie("fontSizeClass");
	}

	$('.font-size-toggle .'+fontSizeClass).addClass('active');
	$body.addClass(fontSizeClass);

	$fontSizeToggler.click(function(){
		var thisItem = $(this);

		$fontSizeToggler.find('span').removeClass('active');
		thisItem.find('span').addClass('active');

		if ( thisItem.hasClass('font-size-small-link') ){
			fontSizeClass= "font-size-small";
		} else if ( thisItem.hasClass('font-size-large-link') ){
			fontSizeClass= "font-size-large";
		} else {
			fontSizeClass= "font-size-medium";
		}

		$body.removeClass("font-size-small font-size-medium font-size-large").addClass(fontSizeClass);

		// Set current fontSize as a cookie value.
		$.cookie("fontSizeClass", fontSizeClass, { expires: 30, path: '/' });

		return false;
	});
}());


/**
* Choose initiative type
* ======================
*
* Prepare initiative form functions
*
*/
var initiativeType = (function() {
	var type =			$('.initiative-type.enabled'),
		verifiableTypeBlock = $('#verified-initiative-block'),
		cbClass =		'.checkbox',
		cb =			type.find(cbClass),
		choose =		type.find('span[data-choose]'),
		radio =			type.find('input[type="radio"]'),
		verifiable =	$('label[data-verifiable="true"]'),
		emailBlock =	$('#prepare-form-email');
		vetumaBlock =	$('#prepare-form-vetuma');
		mask =			$('<div class="disable-mask" />'),

	typeNormalSelected = function(){
		return $('input[value="UNDEFINED"]').is(':checked');
	},

	// This could be general function for all validation errors
	checkErrors = function(){
		var hasErrors = ($('#errors-summary').length > 0);

		return {
			hasErrors: hasErrors,
			normal: hasErrors && typeNormalSelected(),
			verifiable: hasErrors && !typeNormalSelected()
		};
	},

	disableVerifiable = function(disable, errorClassToShow){
		if($("#form-preparation").attr('data-verified') === "false") {
			disable = true;
		}
		if (disable) {
            verifiableTypeBlock.append(mask);
			verifiable.find('.action').addClass(hideClass);
            verifiable.find('.disabledNotCitizen').removeClass(hideClass);
			verifiable.find('input[type="radio"]').removeAttr('checked');
			vetumaBlock.hide();
			resetTypes(true);
            if (municipalitySelection !== undefined) {
                municipalitySelection.preventContinuing(true, 'mask-send', $('.toggle-disable-send'));
			}

		} else {
			verifiable.find('.action').removeClass(hideClass);
            verifiable.find('.disabledNotCitizen').addClass(hideClass);
			mask.remove();
		}
	},

	resetTypes = function(resetAll){
		if( resetAll || !typeNormalSelected() ) {
			type.removeClass('selected').addClass('unselected');
			cb.removeClass('checked');
			choose.text(choose.data('choose'));
			radio.removeAttr('checked');
		}
		if (!resetAll) type.removeClass('unselected');
	},

	setSelected = function(obj) {
		var choose = obj.find('span[data-choose]');

		obj.removeClass('unselected').addClass('selected');
		obj.find(cbClass).addClass('checked');
		choose.text(choose.data('chosen'));
		// CSS hidden radiobutton does not get selected by clicking label on some browsers (IE)
		obj.find('input[type="radio"]').attr('checked','checked');
	},

	toggleSubmitBlock = function(){
		if( typeNormalSelected() ) {
			emailBlock.fadeIn();
			vetumaBlock.hide();
		} else {
			emailBlock.hide();
			vetumaBlock.fadeIn();
		}

		// Reset error messages when user switches initiative type
		$('.field-error').remove();
		$('input[type="text"], textarea').removeClass('error');
	};

	if(checkErrors().normal){
		emailBlock.show();
	}
	if(checkErrors().verifiable){
		vetumaBlock.show();
	}

	type.click(function(){
		var thisObj = $(this);

		if(!thisObj.hasClass('selected')) {
			resetTypes(true);
			setSelected(thisObj);
			toggleSubmitBlock();
            municipalitySelection.preventContinuing(false, 'mask-send', $('.toggle-disable-send'));
		} else {
			resetTypes(true);
			emailBlock.hide();
			vetumaBlock.hide();
            municipalitySelection.preventContinuing(true, 'mask-send', $('.toggle-disable-send'));
		}
		return false;
	});

	return {
		disableVerifiable: disableVerifiable
	};
}());

/**
* Municipality selection
* ======================
*
* - Initializes Chosen select
* - Updates home municipality automatically
* - Toggles home municipality membership radiobuttons
* - Prevents or allows proceeding to next step in the form
*/
var municipalitySelection = (function() {
	var chznSelect					= $(".chzn-select"),
		municipalitySelect			= $('#municipality'),					// Matches select-element
		homeMunicipalitySelect		= $('#homeMunicipality'),				// Matches select-element
		selectedMunicipalityElem	= $('#selected-municipality'),			// Municipality text in the second step in the form
		municipalityNotEqual		= $('.municipality-not-equal'),			// Membership selections if municipalitys are not same
		municipalMembershipRadios	= $("input[name=municipalMembership]"),	// Membership radiobuttons for initiative's municipality
        sameMunicipalitySelect 		= $("input[value=same-municipality]"),
        homeMunicipalitySelectDiv   = $("#home-municipality-select"),
    	otherMunicipalitySelect 	= $("input[value=other-municipality]"),
        verifiedHome				= $('#verifiedHomeMunicipality');

		// If form has validation errors: true / false
		validationErrors = $('#form-initiative').hasClass('has-errors'),

	// Checks which one of the selects we are using
	isHomeMunicipality = function(select){
		if (select.attr('id') === homeMunicipalitySelect.attr('id') ) {
			return true;
		} else {
			return false;
		}
	},
	equalMunicipalitys = function(){
		var selectHome		= $('#homeMunicipality'),
			select			= $('#municipality');

		if (sameMunicipalitySelect.prop('checked')) {
			return true;
		}

		// If user's home municipality is verified by VTJ
		if (verifiedHome.length > 0){
			if ( select.val() == "" || select.val() == verifiedHome.data('initiative-municipality') ) {
				return true;
			} else {
				return false;
			}
		// In the create form we have two selects
		} else if (select.length > 0){
			if ( select.val() == "" || selectHome.val() == select.val()) {
				return true;
			} else {
				return false;
			}
		// If only one select
		} else {
			if ( selectHome.val() == selectHome.data('initiative-municipality') ) {
				return true;
			} else {
				return false;
			}
		}
	},

	equalVerifiedMunicipalities = function () {
        var select			= $('#municipality');

        if ( verifiedHome.length > 0 && select.val() == verifiedHome.data('initiative-municipality') ) {
        	return true;
		}
		return false;
	},

	slideOptions = {
		duration: speedFast,
		easing: 'easeOutExpo'
	},

	// Initialize form state on page load
	init = function(){
		// IE7 not supported
		if(!isIE7){
			$(".chzn-select").loadChosen();
		}

        preventContinuing(true, 'mask', $('.toggle-disable'));
		if ($('#form-preparation').length > 0) {
            preventContinuing(true, 'mask-send', $('.toggle-disable-send'));
		}
		if ($('#form-participate').data('verified') == "" || $('#form-invitation').data('verified') == "") {
            preventContinuing(true, 'mask-send', $('.toggle-disable-send'));
		}

		updateSelectedMunicipality();

		authenticationSelectorOptions();

		if (validationErrors){
			toggleMembershipRadios();
		}

		if (typeof userMunicipalityVerifiedByVetuma !== 'undefined' && typeof userMunicipalityMatchesInitiativeMunicipality !== 'undefined') {
			userMunicipalityVerifiedByVetuma ? showMembership(!userMunicipalityMatchesInitiativeMunicipality) : showMembership(!equalMunicipalitys());

		}
		else {
			showMembership(!equalMunicipalitys());
		}


		// In case of validation errors
		initiativeType.disableVerifiable(!equalMunicipalitys());
	};

	// update text in the municipality data in the form step 2
	function updateSelectedMunicipality(){
		var selectedMunicipality = municipalitySelect.find('option:selected').text();
		if (selectedMunicipality !== "") {
			selectedMunicipalityElem.text(selectedMunicipality);
		}
	};

	function authenticationSelectorOptions() {
        $(".authentication-selection #vetuma-authentication-button").click(function() {
            $(".authentication-selection #vetuma-authentication-button").addClass("selected");
            $(".authentication-selection #email-authentication-button").removeClass("selected");
            $(".authentication-selection .participation-vetuma-login-container").show();
            $(".authentication-selection .participation-authentication-container").hide();
        });

        $(".authentication-selection #email-authentication-button").click(function() {
            $(".authentication-selection #email-authentication-button").addClass("selected");
            $(".authentication-selection #vetuma-authentication-button").removeClass("selected");
            $(".authentication-selection .participation-authentication-container").show();
            $(".authentication-selection .participation-vetuma-login-container").hide();
            if ($('#municipalMembership').is(':visible') && $('#form-invitation').data('homemunicipality') == false) {
                $('#municipalMembership').hide();
			}
        });
	}


	function updateHomeMunicipality() {
		var verifiedHome = $('#verifiedHomeMunicipality');

        if ($('#form-participate').length !== 0) {
			var homeMun = $('#form-participate').data('initiativemunicipality');
            $('#homeMunicipality').val(homeMun);
            return;
        }

        if ($('#form-invitation').length !== 0) {
            $('#homeMunicipality').val($('#form-invitation').data('initiativemunicipality'));
            return;
        }

        if (verifiedHome.length > 0 && verifiedHome.data('initiative-municipality') !== "") {
            $('#homeMunicipality').val(verifiedHome.data('initiative-municipality'));
            return;
        }

		if ($('#homeMunicipality').val() != "" && $("input[value=other-municipality]").prop('checked')) {
			return;
		}
        var val = $('#municipality').val();
        $('#homeMunicipality').val(val);
	}

	// Disable form
	function preventContinuing(prevent, maskClass, toggleDisable) {
		var authorEmail			= $('#participantEmail'),
			toggleDisableInput	= toggleDisable.find('input, select, textarea, button'),
			btnParticipate		= $("button#participate"),
            mask 				= $("." + maskClass);

		//btnParticipate.disableButton(prevent); // use general form validation
		mask.remove();

		if (prevent) {
			toggleDisableInput.attr('disabled','disabled');
			toggleDisable.addClass('disabled');
			var elem = "<div class='" + maskClass + "'/>";
			toggleDisable.prepend(elem);
		} else {
			toggleDisableInput.removeAttr('disabled');
			toggleDisable.removeClass('disabled');
		}
	};

	function disableSubmit(disable){
	  //$('button#action-send-confirm, button#participate').disableButton(disable);
	  $('button#modal-action-accept-invitation').disableButton(disable); // disable accept invitation button
		$('button#participate').disableButton(disable); // disable participate button
	}

	// Toggle the radiobutton selection for municipality membership
	function toggleMembershipRadios(){
		var	municipalMembership	= $('#municipalMembership');

		if( equalMunicipalitys() || ((typeof userMunicipalityMatchesInitiativeMunicipality !== 'undefined') && userMunicipalityMatchesInitiativeMunicipality)){
			municipalityNotEqual.stop(false,true).slideUp(slideOptions);
			disableSubmit(false);
			showMembership(false);
		} else {
			municipalityNotEqual.stop(false,true).slideDown(slideOptions);
			if (!validationErrors){
				preventContinuing(true, 'mask', $('.toggle-disable'));
			}
			if (Init.isVerifiedInitiative()) {
			  disableSubmit(true);
			}
			if(otherMunicipalitySelect.prop('checked')) {
                showMembership(true);
			}
		}
	};

	// Toggle warning for not being a member of the municipality
	function warningNotMember(show){
		var warning = $('.is-not-member');
        if (show){
			warning.fadeIn(speedFast);
		} else {
			warning.hide();
		}
	}

	// Toggle membership radiobuttons
	function showMembership(show){
		var	municipalMembership	= $('#municipalMembership'),
			radios = municipalMembership.find('[type="radio"]');

		if (show){
			municipalMembership.show();
			radios.attr('required','required');
		} else {
			municipalMembership.hide();
			radios.removeAttr('required');
		}
	}

	function resetHomeMunicipalitySelect() {
		var homeMunVisibleSelect = $("#homeMunicipality_chzn");
		homeMunVisibleSelect.find($(".chzn-single")).addClass('chzn-default');
		homeMunVisibleSelect.find($(".chzn-singe span")).text('Valitse kunta');
        $("#homeMunicipality")
            .val(-1)
            .trigger("liszt:updated");
	}

	// Assure that is member of the chosen municipality
	jQuery.fn.assureMembership = function(){
		var cb, btn, isNotMember;

		cb = $(this);
		//btn = $('#action-send-confirm, button#participate');
		btn = $('button#participate');
		isNotMember = function(){
			return ( $("input[name=municipalMembership]:checked").val() === "none" );
		};

		// Use live as this is fired also in the modal
		cb.live('change',function(){
			var disable = isNotMember(),
                homeMunicipalitySelectDiv   = $("#home-municipality-select");

            if (disable) {
                resetHomeMunicipalitySelect();
                homeMunicipalitySelectDiv.hide();
                preventContinuing(true, 'mask', $('.toggle-disable'));
                initiativeType.disableVerifiable(true);
            } else {
                if (verifiedHome.length === 0) {
                    homeMunicipalitySelectDiv.show();
                } else {
                    preventContinuing(false, 'mask', $('.toggle-disable'));
                    initiativeType.disableVerifiable(true);
                }
                removeSelectedFromHomeMunicipalitySelection();

                if ($('#form-invitation').data('homemunicipality') == true || $('#form-participate').data('homemunicipality') == true) {
                    preventContinuing(false, 'mask-send', $('.toggle-disable-send'));
				}
            }

			warningNotMember( disable );
		});
	};
	municipalMembershipRadios.assureMembership();

	function clearSelections() {
        sameMunicipalitySelect.prop('checked', false);
        otherMunicipalitySelect.prop('checked', false);
        showMembership(false);
        homeMunicipalitySelectDiv.hide();
        resetHomeMunicipalitySelect();
        preventContinuing(true, 'mask', $('.toggle-disable'));

    }

    function removeSelectedFromHomeMunicipalitySelection() {
		var municipalityChzn = $('#municipality_chzn');
		if (municipalityChzn.length !== 0) {
            var selectedElemId = municipalityChzn.find(".active-result.result-selected").attr('id');
            var elemId = selectedElemId.replace("municipality", "homeMunicipality");
            $('#' + elemId).hide();
		}
    }

    $('#municipality').live('change', function () {
        var radioMunicipalMembership = $("input[name=municipalMembership]"),
            participationCriterion = $("#participation-criterion");

        clearSelections();

        if (verifiedHome.length === 0) {
        	participationCriterion.show();
        } else if (participationCriterion.attr('class').indexOf("show") >= 0 && !equalMunicipalitys()) {
        	participationCriterion.hide();
        }

        if (verifiedHome.length !== 0 && equalMunicipalitys()) {
            toggleMembershipRadios();
        }

        var equalMun = equalMunicipalitys();

        initiativeType.disableVerifiable(!equalMun);
        radioMunicipalMembership.removeAttr('checked');

        var sameMunicipalityValue = sameMunicipalitySelect.prop('checked');
        var otherMunicipalityValue = otherMunicipalitySelect.prop('checked');


        if (equalMun) {
            preventContinuing(false, 'mask', $('.toggle-disable'));
        } else if (sameMunicipalityValue || otherMunicipalityValue || verifiedHome.length !== 0) {
            toggleMembershipRadios();
        }

        warningNotMember(false);

    });

    $('#homeMunicipality').live('change', function() {
        var initiativeTypes = $('.initiative-types');

		preventContinuing(false, 'mask', $('.toggle-disable'));

        if (initiativeTypes.length === 0) {
            preventContinuing(false, 'mask-send', $('.toggle-disable-send'));
        }

    });


	$('#participation-criterion').live('change', function() {

		var otherMunicipalitySelect 	= $("input[value=other-municipality]"),
			sameMunicipalitySelect	 	= $("input[value=same-municipality]"),
			radioMunicipalMembership 	= $("input[name=municipalMembership]"),
			homeMunicipalitySelectDiv	= $("#home-municipality-select"),
        	initiativeTypes 			= $('.initiative-types');

		if (otherMunicipalitySelect.prop('checked')) {
            resetHomeMunicipalitySelect();
            warningNotMember(false);
            radioMunicipalMembership.removeAttr('checked');
            preventContinuing(true, 'mask', $('.toggle-disable'));
            showMembership(true);
            initiativeType.disableVerifiable(true);
            preventContinuing(true, 'mask-send', $('.toggle-disable-send'));
		}

		if (sameMunicipalitySelect.prop('checked')) {
            updateHomeMunicipality();
		    showMembership(false);
            homeMunicipalitySelectDiv.hide();
            preventContinuing(false, 'mask', $('.toggle-disable'));
            var equalMun = equalMunicipalitys();
            initiativeType.disableVerifiable(!equalMun);
            if (initiativeTypes.length === 0) {
                preventContinuing(false, 'mask-send', $('.toggle-disable-send'));
            }
		}
	});

	$('.email-auth-btn').live('click', function() {
        handleEmailAuthentication();
	});

	function handleEmailAuthentication() {
        var prepareContent = $('.prepare-content');

        if (!$('#participantEmail').val()) {
            return;
        }

        $(".prepare-auth").hide();

        if (prepareContent.hasClass("hide")) {
            prepareContent.show();
        }

        initiativeType.disableVerifiable(true);
	}

    $('#form-preparation').submit(function(event) {

        if ($('#email-auth-container').is(':visible')) {
            handleEmailAuthentication();
            event.preventDefault();
		}

    });

	// Initialize form state on page load
	init();

	return {
		// Return Init-function for the modal
		init: init,
		equalMunicipalitys: equalMunicipalitys,
        preventContinuing: preventContinuing
    };

}());


/**
* Clear field errors
* ======================
*
* Remove red-border from fields with error
* when user is typing
*
*/

var clearFieldErrors = (function() {
	function init(){
		var input =			$('input[type="text"], textarea'),
			errorClass =	'error',
			hasErrors =		($('#errors-summary').length > 0),

		isEmail = function(e){
			return (e.data('type') === 'email');
		},
		toggleError = function(field, clear){
			if (clear) {
				field.removeClass(errorClass);
			} else {
				field.addClass(errorClass);
			}
		};

		input.keyup(function(e) {
			var thisField = $(this);

			if ( isEmail(thisField) ) {
				if (validateEmail(thisField.val())) {
					toggleError(thisField, true);
				} else if (hasErrors) {
					toggleError(thisField, false);
				}
			} else {
				toggleError(thisField, true);
			}
		});
	}

	init();

	// return for modal
	return {
		init:init
	};
}());

//clearFieldErrors.init();


/**
* Simple form validation
* ======================
*
* - Loops through fields that has attribute 'required' in given form
* - If field is defined, validate it only
* - If field is invalid, add class 'js-invalid'
* - IF form has invalid fields, add class 'js-invalid'
*
*/
(function() {
	jQuery.fn.validateForm = function(field){
		var form =			$(this),
			required =		form.find('[required]'),
			classInvalid =	'js-invalid',
			submitBtn =		form.find('[type="submit"]'),

		updateField = function(elem, valid){
			if (valid) {
				elem.removeClass(classInvalid);
			} else {
				elem.addClass(classInvalid);
			}
		},

		isFieldFilled = function(elem){
			return elem.val().replace(/\s+$/, '') !== ''; // trim last space
		}

		isRadioChecked = function(elem){
			return ($('[name="'+elem.attr('name')+'"]:checked').length > 0);
		},

		isOptionSelected = function(id){
			return (document.getElementById(id).selectedIndex > 0);
		},

		validateField = function(elem){
			if (elem[0].type === 'radio'){
				// FIXME: check for all radios within same group
				updateField(elem, isRadioChecked(elem));
			} else if (elem[0].type === 'checkbox') {
				updateField(elem, elem.is(':checked'));
			} else if (elem[0].type === 'select-one') {
				updateField(elem, isOptionSelected(elem.attr('id')));
			} else {
				updateField(elem, isFieldFilled(elem));
			}
		};

		// Validate only field if defined otherwise loop through entire form
		if (field !== null && field !== undefined) {
			if (field.attr('required')) {

				validateField(field);
				form = field.closest('.js-validate');
				submitBtn = form.find('[type="submit"]');
			}
		} else {
			required.each(function(){
				validateField($(this));
			});
		}

		if (form.find('.'+classInvalid).length > 0){
			submitBtn.disableButton(true);
			updateField(form, false);
		} else {
			submitBtn.disableButton(false);
			updateField(form, true);
		}
	}
}());


/**
* Validate forms
* ==============
*
* Validate forms that have class 'js-validate'
*
* TODO: Does not work correctly with both inline and modal forms
*/
/*var validateListener = (function() {
	var form =		$('.js-validate'),

	init = function(form){
		text =		form.find('[type="text"], textarea'),
		radio =		form.find('[type="radio"]'),
		checkbox =	form.find('[type="checkbox"]'),
		select =	form.find('select');

		form.validateForm();
	};

	init(form);

	text.live('keyup', function(){
		form.validateForm($(this));
	});

	form.find('[type="radio"]').live('change', function(){
		form.validateForm($(this));
	});

	checkbox.live('change', function(){
		form.validateForm($(this));
	});

	form.find('select').live('change', function(){
		console.log("SELECT");
		form.validateForm($(this));
	});

	return {
		init: function(form){
			init(form);
		}
	};

}());*/


/**
* Toggle form
* ===========
*
*/
(function() {
	var btn			= $('.js-btn-open-block'),
		block		= $('.js-block-container, .js-block-container-alt'),
		btnClose	= block.find('.js-btn-close-block'),
		tArea		= block.find('textarea');

	btn.click(function(){
		var container = $(this).closest('.toggle-container');
		var elemClass = $(this).data('open-block');
		var btnHolder = $(this).parent('.js-open-block');

		btnHolder.hide();
		container.find('.'+elemClass).fadeIn(speedFast);

		return false;
	});

	btnClose.click(function(){
		var container = $(this).closest('.toggle-container');
		container.find('.js-block-container, .js-block-container-alt').hide();

		container.find('.js-open-block').fadeIn(speedFast);

		return false;
	});

	tArea.focus(function(){
		$(this).addClass("expand");
	});

}());


/**
* External participant count
* ==========================
*
* Replace empty val with 0
*
*/
$('#externalParticipantCount').blur(function(){
	var input = $(this);
	if (input.val() === ""){
		input.val(0);
	}
});


/**
* Search form
* ===========
*
* Submit form on select.
* Used in frontpage and search-page
*
*/

$('.municipality-filter').change( function() {
	var thisSelect = $(this);

	$('#search-form').submit();

});

$('.search-form #municipalities').change(function() {

	// Hax go get the pretty url for municipality page

    if ($("#municipalities").val() && $("#municipalities").val().length == 1) {

        var newFormSrc = (window.location.pathname.substr(0, 4) == "/fi/"
                ? window.location.protocol + "//" + window.location.host + "/fi/kunta/"
                : window.location.protocol + "//" + window.location.host + "/sv/kommun/")
            + $("#municipalities option:selected").text().toLowerCase();

        $('#search-form').attr("action", newFormSrc);
        window.location.href = newFormSrc;
    }
    else {
        $('#search-form').submit();
	}

});

/**
 *
 * Modal: jQuery tools - Overlay
 * =============================
 * - Load modal with defined configurations
 * - Aligns modal to the middle of the viewport
 * - Adds scrollbars to modal content if it doesn't fit in the viewport
 * - Types
 *  - minimal: for simple confirms
 *  - full: for accept invitation
 *
 * FIXME:
 * - Form buttons are pushed to the bottom when scrollbars are visible. Add some space after the form.
 *
 * */
(function() {
	jQuery.fn.loadModal = function(modalType, callback){
		var maxTopPos,
			modal 			= $(this),
			$modalContent 	= modal.children('.modal-content'),
			$scrollable 	= $modalContent.children('.scrollable'), // Used when content can be very long. For examples in namelists.

		topPos = function(){
			if ((modalType === "full" || modalType === "fixed") && (modal.height() < vpHeight) ) {
				return Math.floor((vpHeight-modal.height())/2);
			} else if (modalType === "minimal") {
				// 10% of viewport's size seems to be fine
				return Math.floor(0.1 * vpHeight);
			} else {
				return 10; // 10 px
			}

		},
		modalFixed = function(){
			if(modalType === "full") {
				return false;
			} else {
				return true;
			}
		};

		modal.overlay({
			fixed: modalFixed(),	// modal position
			top: topPos(),			// custom top position
			mask: {					// custom mask
				color: '#000',			// you might also consider a "transparent" color for the mask
				loadSpeed: speedFast,	// load mask a little faster
				opacity: 0.5			// very transparent
			},
			onBeforeLoad: function() {						// In some cases close-link has href for NOSCRIPT-users.
				modal.find('.close').removeAttr("href");	// Removing href to prevent any actions. Preventing default-action did not work.
				$('.binder').each(function(){
					$(this).bindCheckbox();					// Bind checkbox with submit button (used in remove support votes for example)
				});

				jsMessages.Load();

				// TODO:
				//validateListener.init($('.js-validate'));

				if (callback) callback();					// Callback for dynamically updated data

				setTimeout(function () {
					jsRemove();

					if (!$('form').hasClass('has-errors')) {
						modal.find('input[type="text"]:first, textarea:first').focus();
					}
				}, 50);
			},
			onLoad: function(){
				tooltip.load();
				clearFieldErrors.init();
				if (renderMap) {
					renderMap();
					renderMap = null;
				}
			},
			closeOnClick: false,	// disable this for modal dialog-type of overlays
			load: true				// load it immediately after the construction
		}).addClass(modalType);

		// Adjust modal after load
		adjustModal(modal, modalType, $scrollable);

		// Adjust modal when user resizes viewport
		$(window).bind('resize', function(){
			vpHeight = $(this).height();
			//vpWidth = $(this).width();
			vpWidth = window.innerWidth;
			// Adjust only horizontal position
			//modal.css('top',topPos()+'px');

			adjustModal(modal, modalType, $scrollable);
		});
	};

	// Adjust modal's position and height
	var adjustModal = function(modal, modalType, $scrollable){
		var modalPosX;
		modalPosX = (vpWidth - modal.width())/2;
		if (modal.width() > vpWidth) {
			modal.css('left','1%');	// quick fix for mobile devices
		} else {
			modal.css('left',modalPosX);
		}

		$scrollable.css('max-height', 0.75*vpHeight); // Adjust value if needed

		if (modalType === "minimal"){

			if (modal.height() > vpHeight) {
				modal.css('position','absolute');
			} else {
				modal.css('position','fixed');
			}
		}
	};

}());

var modifyAuthorEmail = (function (currEmail) {
    var emailForm = $('#author-email-form'),
        details = $('.details'),
        emailInput = $('#new-email'),
		modifyAuthorEmailBtn = $("#modify-author-email"),
        modifyAuthorEmailCancel = $("#modify-author-email-cancel"),
		managementHashForm = $('#renew-management-hash-form');


    modifyAuthorEmailBtn.click(function () {
        emailInput.val(currEmail);
        details.hide();
        emailForm.show();
        $(this).hide();
        managementHashForm.hide();
    });

    modifyAuthorEmailCancel.click(function () {
    	modifyAuthorEmailBtn.show();
        details.show();
        emailForm.hide();
        managementHashForm.show();
    });
});

/**
 *
 * Modal-loaders
 * =============
 * - Uses class-based actions
 * - Modals loaded also on page load if correct data exists
 * - Calls generateModal() with modalData variable which includes all HTML content for the modal
 *
 * */

	// Initiative saved and ready to collect participants
	if( typeof modalData !== 'undefined' && typeof modalData.requestMessage !== 'undefined' ){
		generateModal(modalData.requestMessage(), 'minimal');
	}

	// Send initiative to review
	$('.js-send-to-review').click(function(){
		try {
			generateModal(modalData.sendToReviewDoNotCollect(), 'minimal');
			return false;
		} catch(e) {
			console.log(e);
		}
	});
	// Send initiative to review auto load
	if( typeof modalData !== 'undefined' && typeof modalData.sendToReviewDoNotCollectAutoLoad !== 'undefined' ){
		generateModal(modalData.sendToReviewDoNotCollectAutoLoad(), 'minimal');
	}

	// Send initiative to review and collect participants
	$('.js-send-to-review-collect').click(function(){
		try {
			generateModal(modalData.sendToReviewCollect(), 'full');
			return false;
		} catch(e) {
			console.log(e);
		}
	});

	// Send fix to review
	$('.js-send-fix-to-review').click(function(){
		try {
			generateModal(modalData.sendFixToReview(), 'minimal');
			return false;
		} catch(e) {
			console.log(e);
		}
	});

	// Start collecting participants
	$('.js-start-collecting').click(function(){
		try {
			generateModal(modalData.startCollecting(), 'minimal');
			return false;
		} catch(e) {
			console.log(e);
		}
	});

    // Delete initiative
    $('.js-delete-initiative').click(function() {
        try {
            generateModal(modalData.deleteInitiative(), 'minimal');
        } catch (e) {
            console.log(e);
        }
    });

    // Restore initiative
    $('.js-restore-initiative').click(function() {
        try {
            generateModal(modalData.restoreInitiative(), 'minimal');
        } catch (e) {
            console.log(e);
        }
    });

	// Send initiative to municipality
	$('.js-send-to-municipality').click(function(){
		try {
			generateModal(modalData.sendToMunicipality(), 'minimal');
			return false;
		} catch(e) {
			console.log(e);
		}
	});

	if( typeof modalData !== 'undefined' && typeof modalData.sendToMunicipalityAutoLoad !== 'undefined' ){
		generateModal(modalData.sendToMunicipalityAutoLoad(), 'minimal');
	}

	// Accept author invitation
	$('.js-accept-invitation').click(function(){
		try {
			generateModal(modalData.acceptInvitation(), 'full', municipalitySelection.init);
			return false;
		} catch(e) {
			console.log(e);
		}
	});

	if( typeof modalData !== 'undefined' && typeof modalData.acceptInvitationAutoLoad !== 'undefined' ){
		generateModal(modalData.acceptInvitationAutoLoad(), 'full', municipalitySelection.init);
	}

	// Accept author invitation
	$('.js-reject-invitation').click(function(){
		try {
			generateModal(modalData.confirmRejectInvitation(), 'minimal');
			return false;
		} catch(e) {
			console.log(e);
		}
	});


	// Participate initiative
	$('.js-participate').click(function(){
		try {
			generateModal(modalData.participateForm(), 'full', municipalitySelection.init);
			return false;
		} catch(e) {
			console.log(e);
		}
	});

	// Follow initiative
	$('.js-follow').click(function(){
		try {
			generateModal(modalData.followInitiative(), 'full');
			return false;
		} catch(e) {
			console.log(e);
		}
	});

	if( typeof modalData !== 'undefined' && typeof modalData.participateFormAutoLoad !== 'undefined' ){
		generateModal(modalData.participateFormAutoLoad(), 'full', municipalitySelection.init);
	}

	if (typeof modalData !== 'undefined' && typeof modalData.followFormAutoLoad !== 'undefined') {
		generateModal(modalData.followFormAutoLoad(), 'full');
	}
	// Contact author
	$('.js-contact-author').click(function(){
		try {
			generateModal(modalData.contactAuthor(), 'full');
			return false;
		} catch(e) {
			console.log(e);
		}
	});

	if( typeof modalData !== 'undefined' && typeof modalData.contactAuthorFormAutoLoad !== 'undefined' ){
		generateModal(modalData.contactAuthorFormAutoLoad(), 'full');
	}

	$('.js-renew-management-hash').click(function(){
        var authorVerified = $(this).data("verified");
        $('.js-renew-management-hash.active').removeClass('active');
        $(this).addClass('active');

        if (!authorVerified) {
            try {
                generateModal(modalData.renewManagementHash(), 'full', renewManagementHash().getAuthor($(this)));
                return false;
            } catch(e) {
                console.log(e);
            }
        } else {
            try {
                generateModal(modalData.modifyVerifiedAuthorEmail(), 'full', renewManagementHash().getAuthor($(this)));
                return false;
            } catch(e) {
                console.log(e);
            }
		}
	});

	$('.js-renew-municipality-management-hash').click(function(){
		try {
			generateModal(modalData.renewMunicipalityManagementHash(), 'full', renewManagementHash.getInitiativeInformation);
			return false;
		} catch(e) {
			console.log(e);
		}
	});


	// Edit municipality
	$('.js-edit-municipality').click(function(){
		$('.municipalities .active').removeClass('active');
		$(this).addClass('active');

		try {
			generateModal(modalData.editMunicipalityDetails(), 'full', editMunicipality.getActive);
			return false;
		} catch(e) {
			console.log(e);
		}
	});

	if( typeof modalData !== 'undefined' && typeof modalData.editMunicipalityDetailsInvalid !== 'undefined' ){
		generateModal(modalData.editMunicipalityDetailsInvalid(), 'full');
	}

	// Delete participant
	$('.js-delete-participant').click(function(){
		$('.js-delete-participant.active').removeClass('active');
		$(this).addClass('active');

		try {
			generateModal(modalData.deleteParticipant(), 'full', deleteParticipant.getParticipant);
			return false;
		} catch(e) {
			console.log(e);
		}
	});

	// Delete author
	$('.js-delete-author').click(function(){
		$('.js-delete-author.active').removeClass('active');
		$(this).addClass('active');

		try {
			generateModal(modalData.deleteAuthor(), 'full', deleteAuthor.getAuthor);
			return false;
		} catch(e) {
			console.log(e);
		}
	});

	//Delete author
  $('.js-delete-attachment').click(function(e){
    e.preventDefault();

    $('.js-delete-attachment.active').removeClass('active');
    $(this).addClass('active');

    try {
      generateModal(modalData.deleteAttachment(), 'full', deleteAttachment.getAttachment);
      return false;
    } catch(e) {
      console.log(e);
    }
  });


  $('#load-more').click(function() {
      var description = $('#municipality-description-text-wrap'),
          oldDescriptionClass = description.attr('class');
      if (oldDescriptionClass.indexOf('more') >= 0) {
          description.attr('class', oldDescriptionClass.replace('more', ''));
      } else {
          description.attr('class', oldDescriptionClass + 'more');
      }
  });

/**
 *
 * Tooltip: jQuery tools - Tooltip
 * ===============================
 *
 * */



var tooltip = (function() {
	return {
		load : function(){
			$('.trigger-tooltip[title]').tooltip({
				animation:	true,
				effect:		'fade',
				placement:	'top right', // FIXME: this doesn't seem to work correctly
				offset:		[-5, 0],
				trigger:	'hover',
				predelay:	500			// Small delay before showing tooltip for more relaxed behaviour.
			}).dynamic({
				left: { direction: 'right' },
				right: { direction: 'left' },
				top: { direction: 'bottom' }
			});
		}
	};
})();


tooltip.load();

/**
 *
 *  Mobile tooltip
 *  http://osvaldas.info/elegant-css-and-jquery-tooltip-responsive-mobile-friendly
 */
$( function() {
	var targets = $( '[rel~=tooltip]' ),
		target  = false,
		tooltip = false,
		title   = false;

	targets.bind( 'mouseenter', function()
	{
		target  = $( this );
		tip     = target.attr( 'title' );
		tooltip = $( '<div id="tooltip"></div>' );

		if( !tip || tip == '' )
			return false;

		target.removeAttr( 'title' );
		tooltip.css( 'opacity', 0 )
			.html( tip )
			.appendTo( 'body' );

		var init_tooltip = function()
		{
			if( $( window ).width() < tooltip.outerWidth() * 1.5 )
				tooltip.css( 'max-width', $( window ).width() / 2 );
			else
				tooltip.css( 'max-width', 340 );

			var pos_left = target.offset().left + ( target.outerWidth() / 2 ) - ( tooltip.outerWidth() / 2 ),
				pos_top  = target.offset().top - tooltip.outerHeight() - 20;

			if( pos_left < 0 )
			{
				pos_left = target.offset().left + target.outerWidth() / 2 - 20;
				tooltip.addClass( 'left' );
			}
			else
				tooltip.removeClass( 'left' );

			if( pos_left + tooltip.outerWidth() > $( window ).width() )
			{
				pos_left = target.offset().left - tooltip.outerWidth() + target.outerWidth() / 2 + 20;
				tooltip.addClass( 'right' );
			}
			else
				tooltip.removeClass( 'right' );

			if( pos_top < 0 )
			{
				var pos_top  = target.offset().top + target.outerHeight();
				tooltip.addClass( 'top' );
			}
			else
				tooltip.removeClass( 'top' );

			tooltip.css( { left: pos_left, top: pos_top } )
				.animate( { top: '+=10', opacity: 1 }, 50 );
		};

		init_tooltip();
		$( window ).resize( init_tooltip );


		var remove_tooltip = function()
		{
			tooltip.animate( { top: '-=10', opacity: 0 }, 50, function()
			{
				$( this ).remove();
			});

			target.attr( 'title', tip );
		};

		target.bind( 'mouseleave', remove_tooltip );
		tooltip.bind( 'click', remove_tooltip );

		$( window ).scroll( remove_tooltip );
	});
});

/**
 * DirtyForms jQuery plugin
 * ========================
 * http://mal.co.nz/code/jquery-dirty-forms/
 *
 * - Checks if form is modified and fires up a notification
 * - To improve usage, LiveQuery could be used
 *   http://brandonaaron.net/code/livequery/docs
 * - $.DirtyForms.dialog overrides plugin's default facebox-dialog
 *
 * */

$.DirtyForms.dialog = {
	// Selector is a selector string for dialog content. Used to determine if event targets are inside a dialog
	selector : '.modal .modal-content',

	// Fire starts the dialog
	fire : function(message, title){
		try {
			generateModal(modalData.formModifiedNotification(), "minimal");
		} catch(e) {
			console.log(e);
		}
	},
	// Bind binds the continue and cancel functions to the correct links
	bind : function(){
		$('.modal .close').click(function(e){
			$.DirtyForms.choiceContinue = false;
			$.DirtyForms.choiceCommit(e);
		});
		$('.modal .continue').click(function(e){
			$.DirtyForms.choiceContinue = true;
			$.DirtyForms.choiceCommit(e);
		});
		$(document).bind('decidingcancelled.dirtyforms', function(){
			$(document).trigger('close.facebox');
		});
	},

	// Refire handles closing an existing dialog AND fires a new one
	refire : function(content){
		return false;
	},

	// Stash returns the current contents of a dialog to be refired after the confirmation
	// Use to store the current dialog, when it's about to be replaced with the confirmation dialog.
	// This function can return false if you don't wish to stash anything.
	stash : function(){
		return false;
	}
};

(function() {
	var ignoreHelper = {
		ignoreAnchorSelector: 'a[rel="external"], .modal a, a#openMap, .map-marker a,  #open-remove-location a, #edit, #save, .toggle-dropdown'
	};

	$.DirtyForms.helpers.push(ignoreHelper);
})();

// Listen forms that have class 'sodirty'
$('form.sodirty').dirtyForms();


/**
* Manage municipalities
* =====================
*
* TODO: Finalize
*
*/
var editMunicipality = (function() {
	// remove this if municipality dropdown select is not used
	$('.manage-municipality-select').change( function() {
		var thisSelect = $(this);
		var form = $('#municipality-form');
		var municipality = $('#municipalities').find('li[data-id='+thisSelect.val()+']');
		var selMunicipality = $('#selected-municipality');

		if ( municipality.data('id') !== undefined ) {
			selMunicipality.text(municipality.text());
		} else {
			selMunicipality.html(selMunicipality.data('empty'));
		}

		form.find('#id').attr('value',municipality.data('id'));
		if( municipality.data('active') === true ){
			form.find('#active').attr('checked','checked');
		} else {
			form.find('#active').removeAttr('checked');
		}

		form.find('#municipalityEmail').val(municipality.data('email'));
	});

	$('.js-edit-municipality').click( function(){
		$('.municipalities .active').removeClass('active');
		$('.field-error').remove(); // do not keep errors when user clicks to open modal
		$(this).addClass('active');
	});

	$('.js-toggle-active').click(function(e){
		e.preventDefault();

		$(this).find('span:first').toggleClass('less');
		$('.municipalities tbody tr.not-active').toggleClass(hideClass);
	});

	return {
		getActive: function(){
			var municipality = $('.municipalities .active');
			var form = $('#municipality-form');
			var selMunicipality = $('#selected-municipality');

			if ( municipality.data('id') !== undefined ) {
				selMunicipality.text(municipality.text());
			} else {
				selMunicipality.html(selMunicipality.data('empty'));
			}
			form.find('#id').attr('value',municipality.data('id'));
			form.find('input[type=radio][name=active][value='+municipality.data('active')+']').attr('checked','checked');
			form.find('#municipalityEmail').val(municipality.data('email'));
			form.find('#municipalityDescriptionFi').val(municipality.data('description'));
			form.find('#municipalityDescriptionSv').val(municipality.data('descriptionsv'));
		}
	};

}());

/**
* Delete participant
* ==================
*/
var deleteParticipant = (function() {
	return {
		getParticipant: function(){
			var participant =			$('.js-delete-participant.active'),
				municipality =			$('.municipalities .active'),
				form = 					    $('#delete-participant-form'),
				selParticipant =		$('#selected-participant'),
				participantInput =		$('#participantId'),
                isVerified =		$('#verified'),
				participantNameId =  'participant-name',
				participantDetails =	'<li><span class="date">' + participant.data("date") + '</span>' +
										'<span class="name-container"><span id="' + participantNameId + '" class="name"></span>';

				if (participant.data("municipality")) {
					participantDetails += '<span class="home-municipality"><span class="bull">&bull;</span>' + participant.data("municipality") + '</span>';
				}
				participantDetails += '</li>';

			selParticipant.html(participantDetails);
			// Avoid XSS
      		$('#'+participantNameId).text(participant.data('name'));

			participantInput.val(participant.data("id"));
            isVerified.val(participant.data("verified"));

		}
	};

}());

/**
* Delete author
* =============
*/
var deleteAuthor = (function() {
	return {
		getAuthor: function(){
			var author = 				$('.js-delete-author.active'),
				form =					$('#delete-author-form'),
				selAuthor =				$('#selected-author'),
				authorInput =			$('#authorId'),
				authorVerified = 		$('#authorVerified'),
				authorNameId =  'author-name',
				authorDetails =			'<h4 id="' + authorNameId + '" class="header"></h4><div class="contact-info">' +
										author.data("email") + '<br/>' +
										author.data("address") + (author.data("address") !== "" ? '<br/>' : '') +
										author.data("phone") +'</div>';

			selAuthor.html(authorDetails);
			// Avoid XSS
      $('#'+authorNameId).text(author.data('name'));

            authorVerified.val(author.data('verified'));
			authorInput.val(author.data("id"));
		}
	};

}());

/**
* Delete attachment
* ================
*/
var deleteAttachment = (function() {
  return {
    getAttachment: function(){
      var attachment =        $('.js-delete-attachment.active'),
          form =              $('#delete-attachment-form'),
          selAttachment =     $('#selected-attachment'),
          attachmentInput =   $('#attachmentId'),
          attachmentNameId =  'attachment-name',
          typeImage =         attachment.data('type') === 'image' ? true : false,
          attachmentDetails = '';

      if (typeImage) {
        attachmentDetails = '<p id="' + attachmentNameId + '"></p><img src="' + attachment.data("src") + '" />';
      } else {
        attachmentDetails = '<p class="pdf-attachment"><img src="/img/pdficon_large.png"/> <span id="' + attachmentNameId + '" class="pdf-label"></span></p>';
      }

      selAttachment.html(attachmentDetails);
      // Avoid XSS
      $('#'+attachmentNameId).text(attachment.data('name'));

      attachmentInput.val(attachment.data('id'));
    }
  };

}());


/**
* Renew author management hash
* ============================
*/
function renewManagementHash() {
	return {
		getAuthor: function (author) {
            return function() {
                var form = $('#delete-author-form'),
                    selAuthor = $('#selected-author'),
                    authorInput = $('#authorId'),
                    authorInput2 = $('#authorIdEmailUpdate'), //This is ugly selector but better for e2e tests
                    authorDetails = '<h4 class="header">' + author.data("name") + '</h4><div class="contact-info">' +
                        author.data("email") + '<br/>' +
                        author.data("address") + (author.data("address") !== "" ? '<br/>' : '') +
                        author.data("phone") + '</div>';

                selAuthor.html(authorDetails);

                authorInput.val(author.data("id"));
                authorInput2.val(author.data("id"));
                modifyAuthorEmail(author.data("email"));
			}
		}
	}
}

/**
 *  Renew municipality management hash
 */
var renewMunicipalityManagementHash = (function() {
	return {
		getInitiativeInformation: function() {

		}
	}
}());

/**
* Generate iFrame
* ==================
*/
if (window.hasIFrame){

(function() {
	var reset =				$('.js-reset-iframe'),
		refresh =			$('.js-update-iframe'),
		iframeContainer =	$("#iframe-container"),
		municipality =		$('#municipality'),
		lang =				$('input[name="language"]'),
		defaultLang	=		$('input[name="language"][value="'+locale+'"]'),
		limit =				$('#limit'),
		width =				$('#width'),
		height =			$('#height'),
		currentLang =		locale,
		bounds =			window.bounds,

	generateIframe = function (params) {
		iframeContainer.html($("#iframe-template").render(params));
		return false;
	},

	checkBounds = function(elem){
		var min, max, def;

		switch(elem.attr('id')) {
			case limit.attr('id'):
				min = bounds.min.limit;
				max = bounds.max.limit;
				break;
			case width.attr('id'):
				min = bounds.min.width;
				max = bounds.max.width;
			  	break;
			case height.attr('id'):
				min = bounds.min.height;
				max = bounds.max.height;
				break;
			default:
				// nop
		}

		if (!/^\d+$/.test(elem.val())) {
			elem.val(min); // set to min if not even a number
		}
		if (elem.val() < min) {
			elem.val(min);
		}
		if (elem.val() > max) {
			elem.val(max);
		}
	},

	params = function() {
		return [{
			municipalities:	municipality.val(),
			lang:			$('input[name="language"]:checked').val(),
			limit:			limit.val(),
			width:			width.val(),
			height:			height.val()
		}]
	},

	refreshFields = function(data){
		municipality.val(data.municipality).trigger('liszt:updated');
		lang.removeAttr('checked');
		defaultLang.attr('checked','checked');
		limit.val(data.limit);
		width.val(data.width);
		height.val(data.height);
	};

	generateIframe(params());

	municipality.change(function(){
		generateIframe(params());
	});

	limit.add(width).add(height).keyup(function(){
		var thisObj = $(this);

		delay(function(){
			checkBounds(thisObj);
			generateIframe(params());
		}, 1000 );
	});

	lang.change(function(){
		generateIframe(params());
	});

	reset.click(function(e){
		e.preventDefault();

		if (window.defaultData) {
			refreshFields(window.defaultData);
			generateIframe(window.defaultData);
		}
	});

	refresh.click(function(e){
		e.preventDefault();

		generateIframe(params());
	});

}());
}
/**
 *  Single inititative (graph) iframe
 *
 */
if (window.hasIGraphFrame) {

	(function () {
		var reset =         $('.js-reset-iframe'),
			refresh =         $('.js-update-iframe'),
			iframeContainer = $("#iframe-container"),
			initiativeId =    $('#initiativeId'),
			lang =            $('input[name="language"]'),
			defaultLang =     $('input[name="language"][value="' + locale + '"]'),
			showTitle =       $('input[name="showTitle"]'),
			width =           $('#width'),
			height =          $('#height'),
			currentLang =     locale,
			bounds =          window.bounds,
			api =             window.defaultData.api,

			getInitiative = function (api, initiativeId, callback) {
				if (initiativeId !== '') {
					$.getJSON(api + '/' + initiativeId).done(function (data) {
						if (callback) {
							callback(data);
						}
					}).fail(function (e) {
						if (callback) {
							callback(null);
						}
						//myConsole.log('Error loading data: ' + e.status + ' - ' + e.statusText);
					});
				} else {
					if (callback) {
						callback(null);
					}
				}
			},

			populateInitiativeDetails = function (params) {
				getInitiative(api, params.initiativeId, function (data) {
					var initiativeName = '';

					if (data) {
						initiativeName = data.name[params.lang] || data.name.fi || '';
					}

					$('#initiative-name').text(initiativeName);
				});
			},

			generateIframe = function (params) {
				populateInitiativeDetails(params);
				iframeContainer.html($("#iframe-template").render(params));
				return false;
			},

			checkBounds = function (elem) {
				var min, max, def,
					val = parseInt(elem.val(), 10);

				switch (elem.attr('id')) {
					case width.attr('id'):
						min = bounds.min.width;
						max = bounds.max.width;
						break;
					case height.attr('id'):
						min = bounds.min.height;
						max = bounds.max.height;
						break;
					default:
					// nop
				}

				if (!/^\d+$/.test(val)) {
					elem.val(min); // set to min if not even a number
				}
				if (val < min) {
					elem.val(min);
				}
				if (val > max) {

					elem.val(max);
				}
			},

			getParams = function () {
				return {
					showPreview: initiativeId.val() !== '' && initiativeId.val() !== '0',
					initiativeId: initiativeId.val(),
					lang:     $('input[name="language"]:checked').val(),
					showTitle: $('input[name="showTitle"]:checked').val() === 'on',
					width:      width.val(),
					height:     height.val()
				};
			},

			refreshFields = function (data) {
				initiativeId.val(data.initiativeId).trigger('liszt:updated');
				lang.removeAttr('checked');
				showTitle.removeAttr('checked');
				defaultLang.attr('checked', 'checked');
				width.val(data.width);
				height.val(data.height);
			};

		generateIframe(getParams());

		initiativeId.change(function () {
			generateIframe(getParams());
		});

		width.add(height).change(function () {
			delay(function () {
				checkBounds(width);
				checkBounds(height);
				generateIframe(getParams());
			}, 100);
		});

		showTitle.change(function () {
			generateIframe(getParams());
		});

		lang.change(function () {
			generateIframe(getParams());
		});

		reset.click(function (e) {
			e.preventDefault();

			if (window.defaultData) {
				$.extend(window.defaultData, {
					initiativeId: initiativeId.val()
				});

				refreshFields(window.defaultData);
				generateIframe(getParams());
			}
		});

		refresh.click(function (e) {
			e.preventDefault();

			generateIframe(getParams());
		});

	}());
}





	/**
* Image popup
* ===========
*
*/
(function() {

  $('.thumbnail-list').magnificPopup({
    delegate: '.thumbnail a', // child items selector, by clicking on it popup will open
    type: 'image',
    gallery: {
      enabled: true, // set to true to enable gallery

      preload: [1,2], // will load 2 next items and 1 that is before current

      navigateByImgClick: true,

      arrowMarkup: '<a title="%title%" type="button" class="mfp-arrow mfp-arrow-%dir%"></a>', // markup of an arrow button

      tPrev: localization.magnificPopup(locale).prev, // title for left button
      tNext: localization.magnificPopup(locale).next, // title for right button
      tCounter: '<span class="mfp-counter">' + localization.magnificPopup(locale).image + ': %curr% / %total%</span>' // markup of counter
    },
    image: {
      tError: localization.magnificPopup(locale).loadError
    },
    prelodaer: true
    // other options
  });

}());

var closeSearchFilter;

/*
* Search filter  in mobile
*
*/
if ($(".search-options-mobile").length > 0) {
	(function () {

		var filters = $(".search-options-mobile");

		var openSearchFilter = function () {
			$("body").addClass("searchFiltersOpen");
			filters.addClass("open");

		};
		closeSearchFilter = function () {
			filters.removeClass("open");
			$("body").removeClass("searchFiltersOpen");

		};

		$(".open-filters").click(function () {
			if (filters.hasClass("open")) {
				closeSearchFilter();
			} else {
				openSearchFilter();
			}

		});
	})();
}


/*
 * Main menu in mobile
 *
*/
$(".toggle-dropdown").click(function () {
	$("#main-navigation").toggleClass("open");
		if (closeSearchFilter) {
			closeSearchFilter();
		}
});


/**
 * Map selector for selecting location on map for initiative.
 *
 */
var getMapContainer = function() {
	var markers= [],
		tempLocations = [],
		map,
		searchresults,
		geocoder= new google.maps.Geocoder(),
		viewOnly = false,
		centerOfFinland = {"lat": 64.9146659, "lng": 26.0672554},
	    // For key navigation in result list
		selectedResultIndex = -1,

		// functions
		initWithAddress,
		initWithCoordinates,
		initMap,
		removeLocation,
		getLocationFromAddress,
		getAddressFromLocation,
		parseAddressFromAddressComponents,
		createSelectedLocationListElement,
		filterOutResultsByType,
		updateResultsList,
		placeMarker,
		removeMarkers,
		removeMarker,
		removeCurrentSelectionInResultList,
		highlightCurrentSelectionInResultList,
		indexIsInSearchResultListRange,
		selectListElementWithArrow,
		selectResultFromList,
		emptyResultList,
		modifyResultList,
		setViewOnly,
		selectSearchResultFromList,
		enableSaveAndClose,
		modified,
		isModified,
		getTempLocationByCoordinates,
		getTempLocations,
		centerMapToPosition,
		removeMarkerByPosition;


	initWithAddress = function(address) {
		selectedLocationsVisualization.init();
		tempLocations = [];

		getLocationFromAddress(address, function (results, status) {
			if (results !== undefined && results !== null && results.length > 0) {
				initMap([results[0].geometry.location]);
			} else {
				initMap([centerOfFinland]);
			}
		});

	};

	initWithCoordinates = function(locations) {
		selectedLocationsVisualization.init();
		tempLocations = [];

		$.each( locations, function(index, value) {
			var location = new google.maps.LatLng(value.lat, value.lng);
			tempLocations.push(location);
			var fallBackPosition;
			if (value.address) {
				fallBackPosition = value.address;
			} else {
				fallBackPosition = location.lat().toFixed(3) + ", " + location.lng().toFixed(3);
			}
			createSelectedLocationListElement(location, fallBackPosition);
		});
		initMap(tempLocations);

	};


	initMap = function(coordinates) {

		var mapOptions = {
			center: coordinates[0],
			zoom: 14
		};

		if(viewOnly) {
			map = new google.maps.Map(document.getElementById('map-canvas-view'),
				mapOptions);
		} else {
			map = new google.maps.Map(document.getElementById('map-canvas'),
				mapOptions);

			google.maps.event.addListener(map, 'click', function (e) {
				var fallBackAddress = e.latLng.lat().toFixed(3) + ", " + e.latLng.lng().toFixed(3);
				tempLocations.push(e.latLng);

				createSelectedLocationListElement(tempLocations[tempLocations.length - 1], fallBackAddress);
				placeMarker(tempLocations[tempLocations.length - 1], map);
				enableSaveAndClose(true);
			});

		}

		removeMarkers();

		var bounds = new google.maps.LatLngBounds();

		$.each(tempLocations, function(index, value) {

			placeMarker(value, map);
			bounds.extend(value)
		});
		// If there is only one location, map will center to given location.
		if(tempLocations.length > 1) {
			map.fitBounds(bounds);
		}

		modified = false;

	};

	removeMarkers = function() {
		$.each(markers, function(index, value) {
			value.setMap(null);
		});
		markers = [];
	};

	removeMarkerByPosition = function(location) {
		var indexToRemove = -1;
		for (var i = 0; i < markers.length; i++) {
			if (compareLocations(markers[i].position, location)) {
				indexToRemove = i;
			}
		}
		if (indexToRemove > -1) {
			removeMarker(markers[indexToRemove]);
		}

	};

	removeMarker = function(marker) {
		marker.setMap(null);
		var index = markers.indexOf(marker);
		if (index !== -1){
			markers.splice(index, 1);
		}
	};

	placeMarker = function(position, map) {
		var marker = new google.maps.Marker({
			position: position,
			map: map
		});

		position.marker = marker;

		if (!viewOnly){
			marker.addListener('click', function() {
				removeLocation(marker.position);
			});
		}

		markers.push(marker);

		centerMapToPosition(position);

	};

	centerMapToPosition = function(position) {
		if(position) {
			map.panTo(position);
		}
	};

	getLocationFromAddress = function(address, callback) {
		var geocoderequst = {"address": address};

		if (map !== undefined) {
			geocoderequst.bounds = map.getBounds();
		}
		geocoderequst.componentRestrictions = {'country': 'FI'};

		geocoder.geocode(geocoderequst, callback);
	};

	getAddressFromLocation = function(location, callback) {
		var geocoderequst = {"location": location};

		geocoderequst.language = "fi";

		geocoder.geocode(geocoderequst, callback);
	};

	parseAddressFromAddressComponents = function(address_components) {

		var selectComponentByType = function (components, type) {
			var matches = $.grep(components, function (element) {
				return element.types.indexOf(type) > -1
			});
			if (matches && matches.length > 0) {
				return matches[0].long_name;
			} else {
				return "";
			}
		};

		var streetName = selectComponentByType(address_components, "route");
		var streetNumber = selectComponentByType(address_components, "street_number");
		var postalCode = selectComponentByType(address_components, "postal_code");
		var municipality = selectComponentByType(address_components, "administrative_area_level_3");

		var address = streetName + " " + streetNumber;

		streetNumber !== "" ? address += ", " : address += " ";

		address += postalCode + " " + municipality;

		return address;

	};
	createSelectedLocationListElement = function(location, fallbackAddress){

		getAddressFromLocation(location, function(results, status) {
			if (results && results.length > 0) {
				var address = parseAddressFromAddressComponents(results[0].address_components);
				location.address = address;
				selectedLocationsVisualization.addLocation(address, location);

			} else {
				location.address = fallbackAddress;
				selectedLocationsVisualization.addLocation(fallbackAddress, location);
			}
		});
	};

	filterOutResultsByType = function(results, type) {
		function doesNotContainType(type) {
			return function(value) {return value.types.indexOf(type) === -1;};
		}
		return results.filter(doesNotContainType(type));
	};

	updateResultsList = function(rawresults) {
		var results = filterOutResultsByType(rawresults, 'country');
		if (results !== undefined && results !== null) {
			modifyResultList(results);
		}

	};
	emptyResultList = function() {
		modifyResultList([]);
	};

	modifyResultList = 	function(results) {
		searchresults = results;
		selectedResultIndex = -1;

		$("#result-list").empty();

		if (searchresults.length > 0) {
			var $ul = $("<ul>");
			$("#result-list").append($ul);

			$.each(searchresults, function(index, value) {
				var $li = $('<li>' + value.formatted_address + '</li>');
				$li.data("item-index", index);
				$ul.append($li);
			});
		}

	};

	removeCurrentSelectionInResultList = function() {
		$(".selected").removeClass("selected");
	};

	highlightCurrentSelectionInResultList = function () {
		//JQuery nth is 1 indexed
		$("#result-list").find("ul li:nth-child("+ (selectedResultIndex + 1) +")").addClass("selected");
	};

	indexIsInSearchResultListRange = function (index) {
		return searchresults && index >= 0 && index < searchresults.length;
	};

	selectListElementWithArrow = function(offset) {
		if (searchresults && searchresults.length > 0) {

			if (indexIsInSearchResultListRange(selectedResultIndex)) {
				removeCurrentSelectionInResultList();
			}

			if (indexIsInSearchResultListRange(selectedResultIndex + offset)) {
				selectedResultIndex += offset;
			} else if ((selectedResultIndex + offset) < 0) {
				selectedResultIndex = -1;
			} else if ((selectedResultIndex + offset) >= searchresults.length) {
				selectedResultIndex = searchresults.length - 1;
			}

			if (indexIsInSearchResultListRange(selectedResultIndex)){
				highlightCurrentSelectionInResultList();
			}
		}
	};

	selectResultFromList = function(index) {
		if (indexIsInSearchResultListRange(index)) {
			var selectedListItem = searchresults[index];
			var address = parseAddressFromAddressComponents(selectedListItem.address_components);
			var location = selectedListItem.geometry.location;
			location.address = address;
			selectedLocationsVisualization.addLocation(address, location);

			tempLocations.push(location);
			placeMarker(location, map);
			enableSaveAndClose(true);
		}
	};

	setViewOnly = function(b) {
		viewOnly = b;
	};

	selectSearchResultFromList = function() {
		if (indexIsInSearchResultListRange(selectedResultIndex)) {
			selectResultFromList(selectedResultIndex);
			return true;
		} else{
			return false;
		}
	};

	enableSaveAndClose = function(b) {
		modified = b;
		$("#save-and-close").disableButton(!b);
	};


	removeLocation = function(location) {
		var index = findLocation(tempLocations, location);

		if (index !== tempLocations.length) {
			tempLocations.splice(index, 1);
			selectedLocationsVisualization.removeLocation(location);
			removeMarkerByPosition(location);

		}
		enableSaveAndClose(tempLocations.length > 0);
	};

	isModified  = function() {
		return modified;
	};

	getTempLocationByCoordinates = function(location){
		function matchesLocation(location) {
			return function(value) {return compareLocations(value, location);};
		}
		var locations = tempLocations.filter(matchesLocation(location));
		if (locations.length > 0) {
			return locations[0];
		} else {
			return null;
		}
	};

	getTempLocations = function() {
		return tempLocations;
	};



	return {
		initWithAddress: initWithAddress,
		initWithCoordinates: initWithCoordinates,
		setViewOnly: setViewOnly,
		getLocationFromAddress: getLocationFromAddress,
		updateResultsList: updateResultsList,
		selectListElementWithArrow: selectListElementWithArrow,
		selectSearchResultFromList: selectSearchResultFromList,
		emptyResultList: emptyResultList,
		selectResultFromList: selectResultFromList,
		isModified: isModified,
		getTempLocationByCoordinates: getTempLocationByCoordinates,
		getTempLocations: getTempLocations,
		centerMapToPosition: centerMapToPosition,
		removeLocation: removeLocation
	};

};

var locationFormFields = (function() {
	var $locationContainer = $('#new-locations');
	var $oldLocationsContainer = $('#old-locations');
	var index = 0;
	var selectedLocations = [];

	var emptyAllRows = function(){
		index = 0;
		$oldLocationsContainer.empty();
		$locationContainer.empty();
		selectedLocations = [];
	};

	var createLocationRow = function (lat, lng, address) {
		var location = {
			newLocationIndex: index.toString(),
			locationLat : lat,
			locationLng: lng
		};
		index += 1;
		$locationContainer.append($("#locationTemplate").render(location));
		selectedLocations.push({lat : lat, lng : lng, address: address});

	};

	var getSelectedLocations = function() {
		return selectedLocations;
	};

	$.each( $('.locationRow'), function(index, value) {
		selectedLocations.push({lat : $(value).find("[id$=lat]").val(), lng : $(value).find("[id$=lng]").val()});
	});


	return {
		emptyAllRows: emptyAllRows,
		createLocationRow: createLocationRow,
		getSelectedLocations: getSelectedLocations
	}

})();


var selectedLocationsVisualization = (function(){
	var locations = [];

	return {
		init : function () {
			locations = [];
		},
		addLocation : function(address, coordinates) {

			locations.push(coordinates);
			var $li = $("<li class='map-marker'>"+ address +"<span class='icon-small icon-16 cancel'></span> </li>");
			$li.data("address", address);
			$li.data("coordinates", coordinates);
			$("#selectedLocations ul").append($li);
		},
		removeLocation: function(coordinates) {
			var index = findLocation(locations, coordinates);
			if (index < locations.length) {
				locations.splice(index, 1);
				//JQuery nth is 1 indexed
				$("#selectedLocations").find("ul li:nth-child("+ (index + 1) +")").remove();
			}
		}

	}

})();

var renderMap;

function compareLocations(location1, location2) {
	if (!location1 || !location2) {
		return false;
	}
	return (location1.lat() === location2.lat()) && (location1.lng() === location2.lng());
}
function findLocation(locations, location) {
	for (var i = 0; i < locations.length; i++) {
		if (compareLocations(locations[i], location)) {
			return i;
		}
	}
	return i;
}

(function() {
	var ARROWUP = 38,
		ARROWDOWN = 40,
		ENTER = 13,
		DOWN = 1,
		UP = -1,
		selectLocation = $("#select-location"),
		editLocation = $("#open-remove-location"),
		removeSelectedLocation = $("#remove-selected-location"),
		saveAndClose = $("#save-and-close"),
		searchField = $("#user-entered-address"),
		mapContainer,
		mapViewContainer;



	// Public view
	if (typeof initiative !== 'undefined' && typeof initiative.locations !== 'undefined' ) {
		mapViewContainer = getMapContainer();
		mapViewContainer.setViewOnly(true);
		mapViewContainer.initWithCoordinates(initiative.locations);
	}

	// Map selection controllers are hidden from no-script users
	$("#map-selection").removeClass("no-visible");


	$("#openMap").click(function(){
		mapContainer = getMapContainer();
		renderMap = function() {mapContainer.initWithAddress(modalData.initiaveMunicipality);};
		generateModal(modalData.mapContainer(), 'full');
	});

	$("#show-selected-location").click(function() {

		mapContainer = getMapContainer();

		renderMap = function() {mapContainer.initWithCoordinates(locationFormFields.getSelectedLocations());};

		generateModal(modalData.mapContainer(), 'full');
	});

	removeSelectedLocation.click(function() {
		selectLocation.removeClass("no-visible");
		editLocation.addClass("no-visible");
		mapContainer = null;
		locationFormFields.emptyAllRows();

	});



	var runSearch = function() {
		mapContainer.getLocationFromAddress($("#user-entered-address").val(),
			function (results, status) {
				if (results !== undefined && results !== null) {
					mapContainer.updateResultsList(results);
				}
			});
	};

	$("#user-entered-address").live("click", function() {
		if($("#user-entered-address").val()) {
			runSearch();
		}
	});

	$("#search-locations-button").live("click", function() {
		runSearch();
	});

	searchField.live('input propertychange keydown', function(event){

		switch (event.which) {
			case ARROWDOWN:
				mapContainer.selectListElementWithArrow(DOWN);
				break;
			case ARROWUP:
				mapContainer.selectListElementWithArrow(UP);
				break;
			case ENTER:
				if (!mapContainer.selectSearchResultFromList()) {
					runSearch();
				}
				break;
			default:
				mapContainer.emptyResultList();
		}
	});

	$("#result-list li").live('click', function(event) {
		var index = $(this).data("item-index");
		if (index !== undefined) {
			mapContainer.selectResultFromList(index);
		}
	});

	saveAndClose.die('click').live('click', function () {

		if (mapContainer.isModified() && mapContainer.getTempLocations().length > 0) {
			$('.modal .close').trigger('click');

			locationFormFields.emptyAllRows();

			$.each(mapContainer.getTempLocations(), function(index, value) {

				locationFormFields.createLocationRow(value.lat(), value.lng(), value.address);
			});

			selectLocation.addClass("no-visible");
			editLocation.removeClass("no-visible");
		}
	});

	$(document).delegate("#map-modal", "click", function(){
		mapContainer.emptyResultList();
	});

	$("#selectedLocations ul li ").live("click", function(e){
		var coordinates = $(this).data("coordinates");
		if (coordinates) {
			var location = mapContainer.getTempLocationByCoordinates(coordinates);
			if (location) {
				mapContainer.centerMapToPosition(location);
			}
		}

	});

	$("#selectedLocations ul li.map-marker .cancel").live("click", function(e){
		var coordinates = $(this).parent().data("coordinates");
		if (coordinates) {
			var location =  mapContainer.getTempLocationByCoordinates(coordinates);
			if (location) {
				mapContainer.removeLocation(location);
			}
		}
	});


})();

(function() {

	var YOUTUBEBASEURL = "https://www.youtube.com/embed/";
	var VIMEOURL = "https://player.vimeo.com/video";

	var entityMap = {
		"&": "&amp;",
		"<": "&lt;",
		">": "&gt;",
		'"': '&quot;',
		"'": '&#39;',
		"/": '&#x2F;'
	};


	var INVALID_URL_PARAMETER = -1;
	var INVALID_HOST = -2;


	var escapeHtml = function (string) {
		return String(string).replace(/[&<>"'\/]/g, function (s) {
			return entityMap[s];
		});
	};

	function invalidUrlParameters(queryParam, videoIDPos) {
		return queryParam.indexOf("&") > -1 || queryParam.indexOf("?") > -1 || queryParam.indexOf("#") > -1 || queryParam.indexOf("^") > -1;
	}

	function convertToYoutubeEmbed(a) {
		var url = null, videoIDPos = -1;
		var queryParam = a.search;
		var path = a.pathname;
		if (queryParam.indexOf("v=") > 0) {
			videoIDPos = queryParam.indexOf("v=") + 2;

			if (invalidUrlParameters(queryParam.substring(videoIDPos), videoIDPos)) {
				url = INVALID_URL_PARAMETER;
			}
			else {
				url = [YOUTUBEBASEURL, escapeHtml(queryParam.substring(videoIDPos))].join('');
			}

		} else if (path.indexOf("embed") > 0) {
			videoIDPos = path.indexOf("embed") + 6;
			if (queryParam.length  > 0) {
				url = INVALID_URL_PARAMETER;
			} else {
				url = [YOUTUBEBASEURL, escapeHtml(path.substring(videoIDPos))].join('');
			}
		} else if (a.hostname === "youtu.be") {
			if (queryParam.length  > 0) {
				url =  INVALID_URL_PARAMETER;
			} else {
				url = [YOUTUBEBASEURL, escapeHtml(path)].join('');
			}
		}

		return url;
	}

	function convertToVimeoEmbed(a) {
		var url = null, path = a.pathname;
		if (path) {
			url = [VIMEOURL + escapeHtml(path)].join('');
		}
		return url;

	}

	function validateVideoLink(url) {

		var a = $('<a>', {href: url})[0];

		if (a.hostname === "www.youtube.com" || a.hostname === "youtu.be") {

			return convertToYoutubeEmbed(a);
		}
		else if (a.hostname === "vimeo.com") {

			return convertToVimeoEmbed(a);
		}

		else {
			return INVALID_HOST;
		}
	}


	$(".videoUrl").on('input propertychange', function () {
		var videoInput = $("#videoUrl"),
			videoContainer = $("#videoContainer");

		videoContainer.empty();

		if (videoInput.val()) {
			var url = validateVideoLink(videoInput.val());
			if (url === null) {
				videoContainer.append("<p>" +invalidUrlWarning +"</p>");
			} else if ( url === INVALID_HOST ) {
				videoContainer.append("<p>" + videoWarning + "</p>");


			} else if (url === INVALID_URL_PARAMETER) {
				videoContainer.append("<p>" +invalidUrlWarning +"</p>");

			} else {
				videoContainer.append("<iframe src=" + url + " width='100%' height='447' />");
			}
		}

	});

	$(".show-video-form").click(function() {
		$(".video-form").toggleClass("hide-form");
		if ($(".video-form").hasClass("hide-form")) {
			$(".show-video-form").show();
		} else {
			$(".show-video-form").hide();
		}

	});


})();

$(window).on("orientationchange", function(event){
	if (closeSearchFilter) {
		closeSearchFilter();
	}
	var chosenPlugins = $(".chzn-done");

	var chosenContainer = $(".chzn-container");
	if (chosenContainer.length > 0) {
		chosenContainer.width("100%");
		chosenContainer.find("*").each(function() {
			//Dont resize the logo
			if (!$(this).is("b") && !$(this).children("b").length > 0) {
				$(this).css({width: "100%",  "box-sizing" : "border-box"});
			}

		});
	}
});

$(window).on('resize', function () {

  if (fireParticipantGraph !== undefined) {
    fireParticipantGraph();
  }

  if (headerNav !== undefined) {
    headerNav.headerNav('resize');
  }


}).trigger('resize');


});