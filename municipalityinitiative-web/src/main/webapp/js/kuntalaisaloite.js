
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
    	if (locale == 'sv'){
    		return "Inga träffar"
    	} else {
    		return "Ei tuloksia"
    	}
	},
	getSubmitInfo:function(locale){
    	if (locale == 'sv'){
    		return "Det verkar räcka längre än väntat att utföra funktionen. Vänligen vänta en stund."
    	} else {
    		return "Toiminnon suorittaminen näyttäisi kestävän odotettua kauemmin. Ole hyvä ja odota hetki."
    	}
	},
	getSubmitWarning:function(locale){
    	if (locale == 'sv'){
    		return "Det gick inte att utföra funktionen. Vänligen försök på nytt om några minuter."
    	} else {
    		return "Toimintoa ei voitu suorittaa. Ole hyvä ja yritä uudelleen muutaman minuutin kuluttua."
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
			if( !cookieEnabled && typeof messageData != 'undefined' && typeof messageData.warningCookiesDisabled != 'undefined' ){
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
	var $body = 		$('body'),
		speedFast = 	'200',						// General speeds for animations
		speedVeryFast = '10',			 
		speedSlow = 	'slow',		
		speedAutoHide = '15000',					// Delay for hiding success-messages (if enabled)
		vpHeight = 		$(window).height(),			// Viewport height
		vpWidth =  		$(window).width(),			// Viewport width
		isIE7 = 		$('html').hasClass('ie7'),	// Boolean for IE7. Used browser detection instead of jQuery.support().
		isIE8 =			$('html').hasClass('ie8'),	// Boolean for IE8. Used browser detection instead of jQuery.support().
		locale = 		Init.getLocale(),			// Current locale: fi, sv
		hideClass = 	'js-hide';					// Set general hidden class

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
		
    	if ( elem == null){
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

	/**
	 *	Prevent double clicks
	 * 
	 */
	$("button").live('click', function () {
		var btnClicked, firstBtnInForm, $loader, $loader, $submitInfo, $submitWarning;
		btnClicked = $(this);
		
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
	        } else {
	            return false;
	        }
		}
    });
	
	// Console fix for IE
	if (typeof console == "undefined") {
	    console = { log: function() {} };
	}
	
	// Remove system message
	$('.close-msg').click(function(){
		var parent = $(this).closest('div').fadeOut('speedSlow', function(){
			parent.remove();
		}); 
	});
	
	// Action for external links
	$('a[rel=external]').click(function(){
		window.open( $(this).attr('href') );
		return false;
	});
	
	// TOP ribbon
	var $topRibbon = $('.debug-ribbon.top.fixed');
	$topRibbon
	.after('<div style="height:'+$topRibbon.outerHeight()+'px" />')
	.css('position','fixed');

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
	if( $.cookie("fontSizeClass") != null ){
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
* Municipality selection
* ======================
* 
* - Initializes Chosen select
* - Updates home municipality automatically
* - Toggles home municipality membership radiobuttons
* - Prevents or allows proceeding to next step in the form
*/	
var municipalitySelection = (function() {
	var chznSelect, municipalitySelect, homeMunicipalitySelect,
		selectedMunicipality, municipalityNotEqual, municipalMembershipRadios,
		validationErrors,
		isHomeMunicipality, equalMunicipalitys, slideOptions;

	chznSelect					= $(".chzn-select");
	municipalitySelect			= $('#municipality');					// Targets select-element
	homeMunicipalitySelect		= $('#homeMunicipality');				// Targets select-element
	selectedMunicipalityElem	= $('#selected-municipality'); 			// Municipality text in the second step in the form
	municipalityNotEqual		= $('.municipality-not-equal');			// Membership selections if municipalitys are not same
	municipalMembershipRadios	= $("input[name=municipalMembership]"); // Membership radiobuttons for initiative's municipality
	
	// If form has validation errors: true / false
	validationErrors = $('#form-initiative').hasClass('has-errors');
	
	// Checks which one of the selects we are using
	isHomeMunicipality = function(select){
		if (select.attr('id') === homeMunicipalitySelect.attr('id') ) {
			return true;
		} else {
			return false;
		}
	};
	equalMunicipalitys = function(){
		var selectHome	= $('#homeMunicipality'),
			select		= $('#municipality');

		// In the create form we have two selects
		if (select.length > 0){
			if ( selectHome.val() == select.val() ) {
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
	};
	slideOptions = {
		duration: speedFast, 
		easing: 'easeOutExpo'
	};
	
	// Initialize form state on page load
	var init = function(){
		// IE7 not supported
		if(!isIE7){
			$(".chzn-select").loadChosen();
		}
		
		updateSelectedMunicipality();
		
		if (validationErrors){
			toggleMembershipRadios(homeMunicipalitySelect);
		}
		
		if (!equalMunicipalitys() && !$("input[name=municipalMembership]").is(':checked')){
			disableSubmit(true);
		} else {
			disableSubmit(false);
		}
		
		showMembership(equalMunicipalitys());
	};
	
	// update text in the municipality data in the form step 2
	function updateSelectedMunicipality(){
		var selectedMunicipality = municipalitySelect.find('option:selected').text();
		if (selectedMunicipality != "") {
			selectedMunicipalityElem.text(selectedMunicipality);
		}
	};
	
 	// Update home municipality automatically
	function updateHomeMunicipality(select){
		var selectedMunicipalityId, selectedMunicipalityName;
		
		selectedMunicipalityId = select.val();
		selectedMunicipalityName = select.find('option:selected').text();

		updateSelectedMunicipality();

		// Update home municipality automatically only if it is empty.
		if ( homeMunicipalitySelect.val() == ""){
			homeMunicipalitySelect
			.val(selectedMunicipalityId)
			.trigger("liszt:updated"); // updates dynamically the second chosen element
		}
	};
	
	// Disable form
	function preventContinuing(prevent){
		
		var btnParticipate 		= $("button#participate");								// Participate button
		
		var typeInput 			= $('.initiative-type:first-child input'),
			authorEmail			= $('#participantEmail'),
			toggleDisable		= $('.toggle-disable'),
			toggleDisableInput  = toggleDisable.find('input, select, textarea, button'),
			btnParticipate 		= $("button#participate");
		
		btnParticipate.disableButton(prevent);
		
		typeInput.disableButton(prevent);
		
		if (prevent) {
			toggleDisableInput.attr('disabled','disabled');
			toggleDisable.addClass('disabled');
		} else {
			toggleDisableInput.removeAttr('disabled');
			toggleDisable.removeClass('disabled');
		}
	};
	
	// Disable or enable submitting "Save and collect"
	function disableSubmit(disable){
		$('button#action-send-confirm, button#participate').disableButton(disable);
	}
	
	// Toggle the radiobutton selection for municipality membership
	function toggleMembershipRadios(select){
		var	municipalMembership	= $('#municipalMembership');
		
		if( equalMunicipalitys() ){
			municipalityNotEqual.stop(false,true).slideUp(slideOptions);
			preventContinuing(false);
			disableSubmit(true);
			showMembership(true);
		} else {
			municipalityNotEqual.stop(false,true).slideDown(slideOptions);
			if (!validationErrors){
				preventContinuing(true);
			}
			disableSubmit(false);
			showMembership(false);
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
		var	municipalMembership	= $('#municipalMembership');
		
		if (show){
			municipalMembership.addClass(hideClass);
		} else {
			municipalMembership.removeClass(hideClass);
		}		
	}

	// Assure that is member of the chosen municipality
	jQuery.fn.assureMembership = function(){
		var cb, btn, isNotMember;
		
		cb = $(this);
		btn = $('#button-next-2, button#participate');
		isNotMember = function(){			
			return ( $("input[name=municipalMembership]:checked").val() === "none" );
		};
		
		// Use live as this is fired also in the modal
		cb.live('change',function(){
			var disable = isNotMember();
			
			btn.disableButton( disable );
			preventContinuing( disable);
			warningNotMember( disable );
		});
	};
	municipalMembershipRadios.assureMembership();
	
	// Listen municipality selects
	$('.municipality-select').live('change', function() {
		var thisSelect			= $(this),
			checkedMembership	= $("input[name=municipalMembership]:checked"),
			radioMunicipalMembership = $("input[name=municipalMembership]");
		
		// Update home municipality automatically
		if (!isHomeMunicipality(thisSelect)){
			updateHomeMunicipality(thisSelect);
			homeMunicipalitySelect.data("initiative-municipality",municipalitySelect.val());
		} else {
			homeMunicipalitySelect.addClass("updated");
		}
		
		// Clear radiobutton on change.
		radioMunicipalMembership.removeAttr('checked');
		
		toggleMembershipRadios(thisSelect);
		warningNotMember(false);
		
		// Disable / enable proceeding to the next form steps
		if ( checkedMembership.length === 0){
			preventContinuing(!equalMunicipalitys());
		} else {
			municipalMembershipRadios.removeAttr('checked');
		}
	});
	
	// Initialize form state on page load
	init();
	
	return {
		// Return Init-function for the modal
		init: init
    };
	
}());


/**
* Preparing phase of the initiative
* =================================
* 
* Check that all fields are filled
* - Selected both municipalities
* - Membership selection if visible
* - Selected initiative type
* - Added valid email address
* 
*/
(function() {
	var form 				= $('#form-preparation'),
		submit 				= form.find('#action-send-confirm'),
		input 				= form.find('input,select'),
		email				= form.find('#participantEmail'),
		municipalitySelect	= form.find('#municipality'),
		homeMunicipalitySelect	= form.find('#homeMunicipality'),
		typeInput 			= form.find('.initiative-type input'),
		membership			= form.find('#municipalMembership'),
		membershipRadio		= membership.find('input[type=radio]'),
		fillInAll			= form .find('.fill-in-all');
	
	submit.disableButton(true);
	
	input.change(function(){
		if (allFieldsFilled()) {
			submit.disableButton(false);
			fillInAll.addClass(hideClass);
		} else {
			submit.disableButton(true);
			fillInAll.removeClass(hideClass);
		}
	});
	email.keyup(function(){
		if (allFieldsFilled()) {
			submit.disableButton(false);
			fillInAll.addClass(hideClass);
		} else {
			submit.disableButton(true);
			fillInAll.removeClass(hideClass);
		}
	});
	
	var allFieldsFilled = function(){
		var selectOK = memberRadioOK = typeRadioOK = emailOK = true;
		
		municipalitySelect.each(function() {
			if(municipalitySelect.val() === "") {
				selectOK = false
			} else {
				selectOK = selectOK && true;
			}
		});
		
		// FIXME: IS visible is fired too late
		if (membership.is(":visible")){
			membershipRadio.each(function(){
				if($(this).is(':checked')) {
					memberRadioOK = true;
					return false;
				} else {
					memberRadioOK = false;
				}
			});
		}
		
		email.each(function() {
			var emailField = $(this);
			
			if (validateEmail(emailField.val())) {
				
				emailField.addClass('valid').removeClass('invalid');
				emailOK = true;
			} else {
				if (emailField.val() !== ""){
					emailField.addClass('invalid').removeClass('valid');
				}
				emailOK = false;
			}
		});
		
		typeInput.each(function(){
			if($(this).is(':checked')) {
				typeRadioOK = true;
				return false;
			} else {
				typeRadioOK = false;
			}
		});

		return selectOK && memberRadioOK && typeRadioOK && emailOK;
	};
	
}());

/**
* Toggle form
* ===========
* 
* FIXME: Alternative block does not work
*/
(function() {
	var btnHolder	= $('.js-open-block'),
		btn			= $('.js-btn-open-block'),
		block 		= $('.js-block-container, .js-block-container-alt'),
		btnClose 	= block.find('.js-btn-close-block'),
		tArea		= block.find('textarea');

	btn.click(function(){
		var elemClass = $(this).data('open-block');
		
		btnHolder.hide();
		$('.'+elemClass).fadeIn(speedFast);
		
		return false;
	});
	
	btnClose.click(function(){
		block.hide();
		btnHolder.fadeIn(speedFast);
		
		return false;
	});
	
	tArea.focus(function(){
		$(this).addClass("expand");
	});
	
}());
	
	
	
/**
* Search form
* ===========
* 
*/

//Listen search form select
$('.municipality-filter').change( function() {
	var thisSelect = $(this);
	
	// Set a small delay so that focus is correctly fired after chance-event.
	// Free text search not yet implemented
	//setTimeout(function () { $('#search').focus(); }, 50);
	
	$('#search-form').submit();
	
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
		var modal, topPos, modalFixed, maxTopPos, modalHeight, $modalContent, $scrollable;
		modal = $(this);
		
		$modalContent = modal.children('.modal-content');
		$scrollable = $modalContent.children('.scrollable'); // Used when content can be very long. For examples in namelists.
		
		modalHeight = function(){
			return modal.height();
		};
		
		topPos = function(){
			if ((modalType == "full" || modalType == "fixed") && (modalHeight() < vpHeight) ) {
				return Math.floor((vpHeight-modalHeight())/2);
			} else if (modalType == "minimal") {
				// 10% of viewport's size seems to be fine
				return Math.floor(0.1 * vpHeight);
			} else {
				return 10; // 10 px
			}
			
		};
		modalFixed = function(){
			if(modalType == "full") {
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
		    	
		    	if (callback) callback();					// Callback for dynamically updated data
		    	
		    	// TODO: Test this properly. We might want to use this.
		    	setTimeout(function () {
		    		jsRemove();
		    		
		    		if (!$('form').hasClass('has-errors')) {
		    			modal.find('input[type="text"]:first, textarea:first').focus();
		    		}
	    		}, 50);
		    },
		    closeOnClick: false,	// disable this for modal dialog-type of overlays
		    load: true				// load it immediately after the construction
		}).addClass(modalType);
		
		// Adjust modal after load
		adjustModal(modal, modalType, $modalContent, $scrollable);

		// Adjust modal when user resizes viewport
		$(window).bind('resize', function(){
			vpHeight = $(this).height();
			vpWidth = $(this).width();
			modal.css('top',topPos()+'px');
			
			adjustModal(modal, modalType, $modalContent, $scrollable);
		});
	};
	
	// Adjust modal's position and height
	var adjustModal = function(modal, modalType, $modalContent,$scrollable){
		var modalPosX;
		modalPosX = (vpWidth - modal.width())/2;
		modal.css('left',modalPosX);
		
		$scrollable.css('max-height', 0.75*vpHeight); // Adjust value if needed

		if (modalType == "minimal"){
			
			if (modal.height() > vpHeight) {
				modal.css('position','absolute');
			} else {
				modal.css('position','fixed');
			}
		}
	};
	
}());
	

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
	if( typeof modalData != 'undefined' && typeof modalData.requestMessage != 'undefined' ){
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
	
	// Send initiative to review and collect participants
	$('.js-send-to-review-collect').click(function(){
		try {
			generateModal(modalData.sendToReviewCollect(), 'minimal');
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
	
	// Send initiative to municipality
	$('.js-send-to-municipality').click(function(){		
		try {
			generateModal(modalData.sendToMunicipality(), 'minimal');
			return false;
		} catch(e) {
			console.log(e);
		}
	});
	
	// Accept author invitation
	$('.js-accept-invitation').click(function(){
		try {
			generateModal(modalData.acceptInvitation(), 'full', municipalitySelection.init);
			return false;
		} catch(e) {
			console.log(e);
		}
	});
	
	if( typeof modalData != 'undefined' && typeof modalData.acceptInvitationInvalid != 'undefined' ){
		generateModal(modalData.acceptInvitationInvalid(), 'full');
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
	
	if( typeof modalData != 'undefined' && typeof modalData.participateFormInvalid != 'undefined' ){
		generateModal(modalData.participateFormInvalid(), 'full');
	}
	
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
	
	if( typeof modalData != 'undefined' && typeof modalData.editMunicipalityDetailsInvalid != 'undefined' ){
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

	
/**
 * 
 * Tooltip: jQuery tools - Tooltip
 * ===============================
 * 
 * */
	
	$('.trigger-tooltip[title]').tooltip({
		animation:	true,
		effect:		'fade',
		placement:	'top right', // FIXME: this doesn't seem to work correctly
		offset:		[-5, 0],
		trigger:	'hover'
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
	ignoreAnchorSelector: 'a[rel="external"], .modal a', // Ignore external links
		
	// Selector is a selector string for dialog content. Used to determine if event targets are inside a dialog
	selector : '.modal .modal-content',

	// Fire starts the dialog
	fire : function(message, title){
		try {
			generateModal(modalData.formModifiedNotification(), "minimal");
		} catch(e) {
			// TODO: What to do in here? Should we just skip confirmation?
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
		$('.msg-error').remove(); // do not keep errors when user clicks to open modal
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
			var participant = 			$('.js-delete-participant.active'),
				municipality = 			$('.municipalities .active'),
				form = 					$('#delete-participant-form'),
				selParticipant = 		$('#selected-participant'),
				participantInput = 		$('#participantId'),
				participantDetails = 	'<li><span class="date">' + participant.data("date") + '</span>' +
										'<span class="name-container"><span class="name">' + participant.data("name") + '</span>' +
										'<span class="home-municipality"><span class="bull">&bull;</span>' + participant.data("municipality") + '</span></li>';

			selParticipant.html(participantDetails);
			
			participantInput.val(participant.data("id"));
		}
	};

}());

/**
* Delete author
* ==================
*/
var deleteAuthor = (function() {
	return {
		getAuthor: function(){
			var author = 				$('.js-delete-author.active'),
				form = 					$('#delete-author-form'),
				selAuthor = 			$('#selected-author'),
				authorInput =	 		$('#authorId'),
				authorDetails = 		'<h4 class="header">'  + author.data("name") + '</h4><div class="contact-info">' +
										author.data("email") + '<br/>' +
										author.data("address") + (author.data("address") != "" ? '<br/>' : '') +
										author.data("phone") +'</div>';

			selAuthor.html(authorDetails);
			
			authorInput.val(author.data("id"));
		}
	};

}());


/**
* Generate iFrame
* ==================
*/
if (window.hasIFrame){
	
(function() {
	var reset = 			$('.js-reset-iframe'),
		refresh = 			$('.js-update-iframe'),
		iframeContainer = 	$("#iframe-container"),
		municipality = 		$('#municipality'),
		lang = 				$('.iframe-lang'),
		limit = 			$('#limit'),
		width = 			$('#width'),
		height = 			$('#height'),
		currentLang =		locale,
		bounds = 			window.bounds,
		
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
	        municipality: 	municipality.val(),
	        lang:			currentLang,
	        limit:			limit.val(),
	        width:			width.val(),
	        height:			height.val()
		}]
    },
    
    refreshFields = function(data){
		municipality.val(data.municipality).trigger("liszt:updated");
		lang.each(function(){
			var thisObj = $(this);
			
			if (thisObj.data('lang') == locale) {
				thisObj.addClass(hideClass);
			} else {
				thisObj.removeClass(hideClass);
			}
		});
		limit.val(data.limit);
		width.val(data.width);
		height.val(data.height);
    };
    
    generateIframe(params());
    
    municipality.change(function(){
    	generateIframe(params());
    });

    lang.click(function(e){
    	e.preventDefault();
    	
    	var thisObj = $(this);
    	
    	lang.removeClass(hideClass);
    	thisObj.addClass(hideClass);
    	
    	currentLang = thisObj.data('lang');
    	
    	generateIframe(params());
    });
    
    limit.add(width).add(height).keyup(function(){
    	var thisObj = $(this);
    	
        delay(function(){
        	checkBounds(thisObj);
        	generateIframe(params());
        }, 1000 );
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

});