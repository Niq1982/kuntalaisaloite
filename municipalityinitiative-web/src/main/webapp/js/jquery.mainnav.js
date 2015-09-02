/* global $, jQuery */

(function ($) {
    'use strict';

    var DROP_CLASS = 'drop'
        , SHOW_BTN_CLASS = 'show'
        , HIDDEN = "hidden";

    var defaults = {
            btnTitle: 'Näytä lisää'
            , btnMenu : '<i class="om-header-nav-icon"></i>' // TEXT/HTML
        }
        , settings = {}
        , dropdownContainer
        , toggleBtn
        , mainHeaderItemsCount = 5;

    var setup = function (options) {
            settings = $.extend(defaults, options);
            dropdownContainer = $('<div class="header-nav-dropdown" />');
            toggleBtn = $('<a href="#" class="toggle-dropdown" title="' + settings.btnTitle + '">' + settings.btnMenu + '</a>');
        },

        toggleMenu = function (ul, show) {
            if (show) {
                dropdownContainer.html(ul.clone().addClass('dropdown-menu'));
            } else {
                dropdownContainer.html('');
            }
        },

        collapseAdditionalHeaderContents = function(ul, toggleAdditionalHeadersToDropDown) {
            var loggedInInfo = $(".logged-in-info"),
                additionalTools = $(".additional-tools");

            ul.children(".additionalHeader").remove();

            loggedInInfo.removeClass(HIDDEN);
            additionalTools.removeClass(HIDDEN);

            if (toggleAdditionalHeadersToDropDown) {
                ul.append($("<li class='"+DROP_CLASS+" additionalHeader' > </li>").append(loggedInInfo.find("a").clone()));
                ul.append($("<li class='"+DROP_CLASS+" additionalHeader' > </li>").append(additionalTools.find("a").clone()));

                loggedInInfo.addClass(HIDDEN);
                additionalTools.addClass(HIDDEN);

            }

        },

        handleResize = function (el) {
            var ul = el.find('ul').first()
                , li = ul.find('li')
                , droppedCount = 0
                , visible = false;


            li.each(function (index, element) {
                var el = $(element);
                console.log("el.position().top " + el.position().top + " li.first().position().top " + li.first().position().top);
                if (el.position().top > li.first().position().top) {
                    el.addClass(DROP_CLASS);
                    droppedCount++;
                } else {
                    el.removeClass(DROP_CLASS);
                }
            });
            console.log("----------");

            // Drop all if only two remaining
            if (li.length > 2 && $(li[2]).hasClass(DROP_CLASS)) {
                $(li[0]).addClass(DROP_CLASS);
                $(li[1]).addClass(DROP_CLASS);

            }

            collapseAdditionalHeaderContents(ul, $(li[mainHeaderItemsCount - 1]).hasClass(DROP_CLASS));

            toggleMenu(ul, false);
            toggleBtn.toggleClass(SHOW_BTN_CLASS, droppedCount > 0);
            ul.toggleClass('push', droppedCount > 0);

            toggleBtn.click(function (e) {
                e.preventDefault();
                visible = !visible;
                toggleMenu(ul, visible);
            });
        };

    // Public methods
    var methods = {
        init : function (options) {
            return this.each(function (index, element) {
                var $this = $(element);

                setup(options);

                $this.append(toggleBtn);
                $this.append(dropdownContainer);
            });
        },

        resize : function () {
            return this.each(function (index, element) {
                var $this = $(element);

                setTimeout(function () {
                    handleResize($this);
                }, 300);
            });
        }
    };

    $.fn.mainNav = function (methodOrOptions) {
        if ( methods[methodOrOptions] ) {
            return methods[ methodOrOptions ].apply( this, Array.prototype.slice.call( arguments, 1 ));
        } else if ( typeof methodOrOptions === 'object' || ! methodOrOptions ) {
            // Default to "init"
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' +  methodOrOptions + ' does not exist on jQuery.headerNav' );
        }
    };

}(jQuery));