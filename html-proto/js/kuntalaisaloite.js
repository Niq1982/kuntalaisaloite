/**
 * TODO: Remove all initiattive-web stuff

*/




var generateModal, localization, validateForm, modalContent, modalType;

/**
 * 
 * Generate modal
 * ==============
 * - Gets modalContent containing all HTML
 * - Uses jsRender to render template into the modal container
 * - Shows modal template with the defined content
 * 
 * */
generateModal = function (modalContent, modalType) {
	$("#modal-container").html($("#modal-template").render(modalContent));
	$(".modal").loadModal(modalType);
	return false;
};

localization = {
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


//TODO: JS validation
// We do not use JS validation for the form at this moment.
// This JS validation is a special case for organizer emails.
// Should be refactored after the global JS validation is enabled
// Localize error message
validateForm = function (formBlock) {	
	if(formBlock === "organizers") {
		if( $('.email-input').find('.invalid').length > 0){
			var authorArea = $('.initiative-authors-area');
			if( authorArea.find('.msg-error').length === 0){
				authorArea.prepend('<div class="system-msg msg-error">Sähköpostiosoitteissa on virheitä.</div>');
			}
		} else {
			$('form#form-initiative').submit();
		}
	}
};


$(document).ready(function () {	
	// Define general variables
	var $body, speedFast, speedSlow, speedVeryFast, speedAutoHide, vpHeight, vpWidth, validateEmail, isIE7;
	$body = $('body');
	speedFast = '200';					// General speeds for animations
	speedVeryFast = '10';			 
	speedSlow = 'slow';		
	speedAutoHide = '15000';			// Delay for hiding success-messages (if enabled)
	vpHeight = $(window).height();		// Viewport height
	vpWidth =  $(window).width();		// Viewport width
	isIE7 = $('html').hasClass('ie7');	// Boolean for IE7. Used browser detection instead of jQuery.support().
	isIE8 = $('html').hasClass('ie8');	// Boolean for IE8. Used browser detection instead of jQuery.support().
	
/**
 * Common helpers
 * ==============
 */
	// Hide all elements that should not be seen with JS
	// Moved in CSS
	//$('.js-hide').hide();
	
	// Wait a while and hide removable message
	if ( $('.auto-hide').length > 0 ){
		setTimeout(function() {
			$('.auto-hide').fadeOut(speedSlow);
		}, speedAutoHide);
	}
	
	// Jump to errors-summary
	// NOTE: Disabled ATM, because needs to work for NOSCPRIPT-users also
	/*if ($('#errors-summary').length > 0) {
		window.location.hash="errors-summary";
	}*/
	
	// Validate emails
	validateEmail = function (email) {
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
	 *	TODO:	Finalize after function approved. 
	 *			What happens when JS validation is implemented?
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
			$submitInfo = $('<div class="system-msg msg-info">'+localization.getSubmitInfo(Init.getLocale())+'</div>');
			$submitWarning = $('<div id="submit-warning" class="system-msg msg-warning">'+localization.getSubmitWarning(Init.getLocale())+'</div>');
			
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
	

/**
 * Toggle form help texts
 * ======================
 *  - Help text fires up when user clicks help-icon or focuses to a text-input or a textarea
 *  - Both inputs and helps needs to be descendants of the same parent element ".input-block-content"
 *  - Adjust container's height if adjustment is needed
 *  Known issues:
 *  - In some case when removing link-rows help text overflows
 */
 /*
	var $allHelps = $('.input-block-extra');
	var toggleHelpTexts = function (elem,close) {
		var initPadding, initMargin, thisElem, elemParent, originalHeight, elemHeight, hd, parentBlock, elemParentTopPos;
		
		initPadding = 25; 	// hardcoded value from CSS: .input-block:			padding:25px
		initMargin = 35; 	// hardcoded value from CSS: .input-block-content:	margin-top:2em -> 35 (px)
		
		thisElem = elem.children('.input-block-extra-content');
		elemParent = elem.closest(".input-block-content");
		parentBlock = elem.closest(".input-block");
		originalHeight = parentBlock.height();
		elemHeight = thisElem.outerHeight();
		hd = elemParent.height() - elemHeight;

		if ( elem.is(':visible')){
			if (close){
				elem.fadeOut(speedFast, function(){
					// Reset the height if needed
					if (hd < 0){
						parentBlock.stop(false,true).animate({
							paddingBottom: initPadding+'px'
						}, speedFast, function(){
							parentBlock.removeAttr('style'); // clear styles after animation
						});
					}
				});
			}
		} else {
			$allHelps.fadeOut(speedFast);
			elem.fadeIn(speedFast);
			
			// Selenium htmlUnitDriver throws null pointer in elemParent.position() !?
			elemParentTopPos = 0;
			if( elemParent.position() != null){
				elemParentTopPos = elemParent.position().top;
			}
			
			elemHeight = elem.children('.input-block-extra-content').outerHeight();
			hd = (elemHeight + elemParentTopPos + initMargin) - parentBlock.height();

			// Adjust the height if needed
			if (hd > initPadding){
				parentBlock.stop(false,true).animate({
					paddingBottom: hd+"px"
				}, speedFast, function(){
					parentBlock.css('min-height',parentBlock.height()+'px'); // create min-height so that when opening another smaller help container would not collapse
				});
			}
		}
	};
		
	// Matches with data-name of the help-icon and the class-name of the help-text container
	$('.help').click(function (){
		var help, $thisHelp;
		help = $(this).data('name');
		$thisHelp = $('.input-block-extra.'+help);
		
		toggleHelpTexts($thisHelp,true);
	});
	
	// Matches class-name "input-block-extra" within the same block
	$('input[type=text],textarea').live('focus', function(){
		var $thisHelp = $(this).parents('.input-block-content:first').find('.input-block-extra:first');		 
		toggleHelpTexts($thisHelp,false);
	});
*/
	
/**
 *	Toggle dropdown menus
 *  =====================
 * */
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
	
/**
 * 
 * Change the font-size
 * ====================
 * - Uses jQuery Cookie: https://github.com/carhartl/jquery-cookie
 * - Toggles a class for body-element (font-size-small, font-size-medium, font-size-large)
 * - Sets and gets fontSizeClass-cookie
 * 
 * */
	var $fontSizeToggler = $('.font-size-toggle a');
	var fontSizeClass = "font-size-medium";
	
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
	
/**
 * 
 * Expand and minify form blocks
 * =============================
 * 
 * */
 	var showFormBlock = function(blockHeader){
 		var thisHeader, thisBlock, otherHeaders, otherBlocks;

		thisHeader = blockHeader;
 		thisBlock = thisHeader.next('.input-block');
 		otherHeaders = thisHeader.parent().siblings().children('.content-block-header');
 		otherBlocks = thisHeader.parent().siblings().children('.input-block');

 		otherBlocks.stop(false,true).slideUp({
			duration: speedFast, 
			easing: 'easeOutExpo'
		});
		otherHeaders.removeClass('open');

 		thisBlock.stop(false,true).slideToggle({
			duration: speedFast, 
			easing: 'easeOutExpo'
		});
		thisHeader.toggleClass('open');
 	};

 	var $formHeader = $('.content-block-header');

 	$formHeader.click(function(){
 		showFormBlock($(this));
 	});

 	// Action for wizard's continue button
 	proceedTo = function(step){
 		var blockHeader = $('#step-'+step).prev('.content-block-header');
		showFormBlock(blockHeader);

 		//return false;
 	};



/**
 * 
 * Add and remove link
 * ===================
 * - Uses jsRender to render link-row template
 * - Handles adding and removing a link-row
 *
 * TODO: remove from kuntalaisaloite if not needed
 * 
 * */
	var $linkContainer = $('#link-container');
	var index = $linkContainer.data("index");
	
	// TODO: Genetare better positioned remove link
	var createRemoveLink = function(elem){
		var linkHeight, $removeLink;
		
		linkHeight = elem.innerHeight();
		$removeLink = $('<a class="remove-link ignoredirty" style="height:'+linkHeight+'px">x</a>');
		elem.wrap('<div style="position:relative;">');
		elem.after($removeLink);
	};
	
	// Use jsRender to render a new link row
	var createLinkRow = function(){
		var links = { linkIndex:""+index };
		$linkContainer.append( $( "#linkTemplate" ).render( links ) );
		
		//createRemoveLink();
		
		// FIXME: This does not work as the data value is not correct --> Returns string?
		if($altLangLink.data('translation')){ 
			$('.alt-lang').show();
		} else {
		}
		index++;
	}
	
	// Create one as default and other ones with a click.
	if ( $linkContainer.length > 0) {
		createLinkRow();
	}
	
	$('#add-new-link').live('click', function(){
		createLinkRow();
        
		return false;
	});
	
	// Remove link (clear values and hide fields)
	$('.remove-link').live('click', function(){
		//var removedLink = $(this).parent();
		var removedLink = $(this).closest('.add-link');
		
		removedLink.slideUp(speedFast, function() {
			// Animation complete.
		}).find("input").attr("value","");
		
		return false;
	});

	

/**
* Chosen - Replacement for municipality select
* ============================================
*/	
	var municipalitySelect, homeMunicipalitySelect, selectedMunicipality, differentMunicipality, sameMunicipality;

	municipalitySelect =		 $('#municipality');
	homeMunicipalitySelect =	 $('#homeMunicipality');
	selectedMunicipalityElem =	 $('#selected-municipality');
	differentMunicipality =		 $('.different-municipality');
	sameMunicipality =			 $('.same-municipality');


 	// Initialize chosen
 	$(".chzn-select").chosen();

 	// Listen the first chosen element
	municipalitySelect.live('change', function() {
		var selectedMunicipality = $(this).val();

		// update text in the municipality data in Step 2
		selectedMunicipalityElem.text(selectedMunicipality);

		// if user has changed the homeMunicipality value we will not mess it up
		if ( !homeMunicipalitySelect.hasClass('updated') ){			
			homeMunicipalitySelect
			.val(selectedMunicipality)
			.trigger("liszt:updated"); // updates dynamically the second chosen element
		}
	});

	// Toggle suffrage fields according to user's choice
	homeMunicipalitySelect.live('change', function() {
		var thisSelect = $(this);

		thisSelect.addClass("updated");

		if( thisSelect.val() == municipalitySelect.val() ){
			differentMunicipality.hide();
			sameMunicipality.show();
		} else {
			differentMunicipality.show();
			sameMunicipality.hide();
		}
	});

/**
* Toggle collect people
* ====================
* - Toggles an element depending on the selection of other element (radiobutton or checkbox)
* - If the input is clicked hidden:
* 		* the input is disabled so that value will not be saved
* 		* the value is not removed so that the value can be retrieved
* 		  when clicked back to visible 
* - TODO: Bit HardCoded now. Make more generic if needed.
*/
var toggleArea, $toggleAreaLabel, radioTrue, $toggleField, toggleBlock;

toggleArea =		'.gather-people-details';
$toggleAreaLabel =	$('#gather-people-container label');
radioTrue =		'gather-people-true';
$toggleField =		$('#initiativeSecret');

toggleBlock = function(clicker, input){
	if( input.attr('id') == radioTrue){		
		clicker.siblings(toggleArea).slideDown({
			duration: speedVeryFast, 
			easing: 'easeOutExpo'
		});
		$toggleField.removeAttr('disabled');
	} else {
		clicker.siblings(toggleArea).slideUp({
			duration: speedVeryFast, 
			easing: 'easeOutExpo'
		});
		$toggleField.attr('disabled','disabled');
	}	
};

$toggleAreaLabel.each(function (){
	var clicker, input;
	clicker = $(this);
	input = clicker.find("input:first");
	
	if( input.is(':checked') && input.attr('id') == radioTrue){
		$toggleField.removeAttr('disabled');
		$(toggleArea).show();
	}
	
	clicker.click(function(){
		if(clicker.children('input[type="radio"]').length > 0){
			toggleBlock($(this), input);
		}
	});
	
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
	jQuery.fn.loadModal = function(modalType){
		var modal, topPos, modalFixed, maxTopPos, modalHeight, $modalContent;
		modal = $(this);
		$modalContent = modal.children('.modal-content');
		
		modalHeight = function(){
			return modal.height();
		}
		topPos = function(){
			if ((modalType == "full") && (modalHeight() < vpHeight) ) {
				return Math.floor((vpHeight-modalHeight())/2);
			} else if (modalType == "minimal") {
				// 10% of viewport's size seems to be fine
				return Math.floor(0.1 * vpHeight);
			} else {
				return 10; // 10 px
			}
			
		}
		modalFixed = function(){
			if(modalType == "full") {
				return false;
			} else {
				return true;
			} 
		}
		
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
		    },
		    closeOnClick: false,	// disable this for modal dialog-type of overlays
		    load: true				// load it immediately after the construction
		}).addClass(modalType);
		
		// Adjust modal after load
		adjustModal(modal, $modalContent, topPos());

		// Adjust modal when user resizes viewport
		$(window).bind('resize', function(){
			vpHeight = $(this).height();
			vpWidth = $(this).width();
			modal.css('top',topPos()+'px');
			
			adjustModal(modal, $modalContent, topPos());
		});
	};
	
	// Adjust modal's position and height
	var adjustModal = function(modal, $modalContent, topPos){
		var modalPosX;
		modalPosX = (vpWidth - modal.width())/2;
		modal.css('left',modalPosX);

		if (modalType == "minimal"){
			if (modal.height() > vpHeight) {
				modal.css('position','absolute');
			} else {
				modal.css('position','fixed');
			}
		}
	};
	
	

/**
 * 
 * Modal-loaders
 * =============
 * - Uses class-based actions
 * - Modals loaded also on page load if correct data exists
 * - Calls generateModal() with modalData variable which includes all HTML content for the modal
 * 
 * */

// ADD LOADERS HERE

	
/**
 * 
 * Datepicker: jQuery tool - Dateinput
 * ===================================
 * - Uses the global Init.getLocale() variable to determine the localization
 * 
 * */
	$.tools.dateinput.localize("fi", {
		  months: 'Tammikuu,Helmikuu,Maaliskuu,Huhtikuu, Toukokuu, Kesäkuu, Heinäkuu, Elokuu, Syyskuu, Lokakuu, Marraskuu, Joulukuu',
		  shortMonths:  'Tammi, Helmi, Maalis, Huhti, Touko, Kesä, Heinä, Elo, Syys, Loka, Marras, Joulu',
		  days:         'Sunnuntai, Maanantai, Tiistai, Keskiviikko, Torstai, Perjantai, Lauantai',
		  shortDays:    'Su, Ma, Ti, Ke, To, Pe, La'
	});
	$.tools.dateinput.localize("sv", {
		  months: 'Januari, Februari, Mars, April, Maj, Juni, Juli, Augusti, September, Oktober, November, December',
		  shortMonths:  'Jan, Feb, Mar, Apr, Maj, Jun, Jul, Aug, Sep, Okt, Nov, Dec',
		  days:         'Söndag, Måndag, Tisdag, Onsdag, Torsdag, Lördag',
		  shortDays:    'Sö, Må, Ti, On, To, Fr, Lö'
	});
	
	$.tools.dateinput.conf.lang = Init.getLocale();

	$(".datepicker").dateinput({
		format: 	Init.getDateFormat(),	// this is displayed to the user
		firstDay:	1,						// First day is monday
		offset:		[0, 0],
	 
		// a different format is sent to the server
		change: function() {
			var isoDate = this.getValue('yyyy-mm-dd');
			$("#backendValue").val(isoDate);
		}
	})
	.blur(function(){
		// Trim spaces in copy pasted value
		var trimmed = $.trim( $(this).val() );
		$(this).val(trimmed);
	});
	
/**
 * 
 * Tooltip: jQuery tools - Tooltip
 * ===============================
 * 
 * */
	
	$('.trigger-tooltip').tooltip({
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
 * TODO:
 * - Fix dynamic links (for example "Add new link")
 * - Check methods: refire and stash
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
		
//	    var rebox = function(){
//	    	generateModal(modalData.formModifiedNotification());
//	        //$.facebox(content);
//	        $(document).unbind('afterClose.facebox', rebox);
//	    }
//	    $(document).bind('afterClose.facebox', rebox);
	},

	// Stash returns the current contents of a dialog to be refired after the confirmation
	// Use to store the current dialog, when it's about to be replaced with the confirmation dialog.
	// This function can return false if you don't wish to stash anything.
	stash : function(){
		return false;
		
//	    var fb = $('#facebox .content');
//	    return ($.trim(fb.html()) == '' || fb.css('display') != 'block') ?
//	       false :
//	       fb.clone(true);
       
	}
};

// Listen forms that have class 'sodirty'
$('form.sodirty').dirtyForms();


/**
 * 
 * Validation: jQuery tools - Form validation
 * ==========================================
 * TODO:
 * - Will be implemented much later
 * - Localizations should be loaded from message.properties
 * 
 * */

$.tools.validator.localize("fi", {
	'*'			: 'Virheellinen arvo',
	':email'  	: 'Virheellinen s&auml;hk&ouml;postiosoite',
	':number' 	: 'Arvon on oltava numeerinen',
	':url' 		: 'Virheellinen URL',
	'[max]'	 	: 'Arvon on oltava pienempi, kuin $1',
	'[min]'		: 'Arvon on oltava suurempi, kuin $1',
	'[required]'	: 'Kent&auml;n arvo on annettava'
});
$.tools.validator.localize("sv", {
	'*'			: 'SV: Virheellinen arvo',
	':email'  	: 'SV: Virheellinen s&auml;hk&ouml;postiosoite',
	':number' 	: 'SV: Arvon on oltava numeerinen',
	':url' 		: 'SV: Virheellinen URL',
	'[max]'	 	: 'SV: Arvon on oltava pienempi, kuin $1',
	'[min]'		: 'SV: Arvon on oltava suurempi, kuin $1',
	'[required]'	: 'SV: Kent&auml;n arvo on annettava'
});


/**
* Custom validation for current user's roles
* ====================
* Disables selections that cannot be submitted.
* - User must be one of these: initiator, representative or reserve
* - But he/she cannot be representative and reserve at the same time
*/
/*
$.tools.validator.addMethod('rolesCheckbox', function (val, elt) {
    var valid = false;

    $('input[name=' + $(elt).attr('name') + ']:checkbox').each(function(inx, elt) {
        if (elt.checked === true) {
            valid = true;
            return;
        }
    });

    return valid;
}, function()  {
 
});*/


    
/*
$.tools.validator.fn("[name=currentAuthor.initiator]", "Value not equal with the $1 field", function(input) {
    var name = input.attr("data-equals"),
    field = this.getInputs().filter("[name=" + name + "]");
    return input.val() == field.val() ? true : [name];
    });
*/

/**
 * TODO: Clean the validation code 
 *  - If the custom-effect 'inline' is used, remove unneeded options
 *  - Error-messages gets multiplied after each failed form send
 */
/* NOTE: Disabled ATM (20.6.2012) to ease the testing of backend validation */ 
// Add novalidate attribute for preventing Browser's default validation
/*$("form.validate").attr("novalidate","novalidate")
.validator({
	effect: 'inline',
	errorInputEvent: 'keyup',
	lang: Init.getLocale(),
	position: 'top left',
	offset: [-12, 0],
	message: '<div><em/></div>' // em element is the arrow
		
}).bind("onFail", function(e, errors)  {
	
	// we are only doing stuff when the form is submitted
	if (e.originalEvent.type == 'submit') {
 
		// loop through Error objects and add highlight
		$.each(errors, function()  {
			var input = this.input;
			input.addClass("has-error").focus(function()  {
				input.removeClass("has-error");
			});
		});
	
		
		// TODO: Smooth scroll to the first error or to top of the form. The code below causes errors in some cases
		//var positionFirstError = $('form').position().top + $('.has-error:first').prev('label').position().top - 30;
		//console.log(positionFirstError);
		//$('html, body').animate({scrollTop: positionFirstError}, 800);

	}
	
});*/


// Custom effect for the validator
/*
 * TODO: ADD keyup-event for validating inputs.
 */
$.tools.validator.addEffect("inline", function(errors, event) {
 
	// add error before errorenous input
	$.each(errors, function(index, error) {
		error.input.before('<div class="system-msg msg-error">'+error.messages[0]+'</div>');
	});
	
	// validate field on edit
	$('input, textarea').keyup(function()  {
		var inputError = $(this).prev();
		
		if (inputError.hasClass("msg-error")){
			inputError.remove();
		}
	});
	
// the effect does nothing when all inputs are valid
}, function(inputs)  {
 
});










/**
* Bind checkbox
* =============
* - Binds checkbox '.binder' with submit button '.bind'.
* - Button is enabled when checkbox is checked otherwise disabled
*/
jQuery.fn.bindCheckbox = function(){
	var cb, btn, cbVal;
	
	cb = $(this);
	btn = cb.parents('form').find('.bind');
	cbVal = function(){
		if (cb.is(':checked')){
			btn.removeAttr('disabled').removeClass('disabled');
		} else {
			btn.attr('disabled','disabled').addClass('disabled');
		}
	};
	
	cbVal();
	cb.change(function(){
		cbVal();
	});
};
$('.binder').bindCheckbox();


/**
 * 
 * Show hide more details
 * ======================
 * 
 * */
$('.toggler').click(function(){
	var toggler = $(this);
	var toggled = toggler.parent().next('.toggled');
	
	toggler.find('span').toggleClass('less')
	toggler.next('h4').toggleClass('subtle'); // relies on h4
	toggler.switchContent(toggler.find('.text:first'));
	
	if(isIE7){
		toggled.stop(false,true).toggle(); // content disappeared in ie7 after animation
	} else {
		toggled.stop(false,true).slideToggle({
			duration: speedFast, 
			easing: 'easeOutExpo'
		});
	}
	
	return false;
});


});