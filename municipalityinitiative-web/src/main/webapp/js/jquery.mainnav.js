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
        , toggleBtn;

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

        collapseAdditionalHeaderContents = function(ul, b) {
            $(".logged-in-info").removeClass(HIDDEN);
            $(".additional-tools").removeClass(HIDDEN);

            $("li").remove(".additionalHeader");

            if (b) {
                var $li1 = $("<li class='"+DROP_CLASS+" additionalHeader' > </li>");
                var $li2 = $("<li class='"+DROP_CLASS+" additionalHeader' > </li>");

                $li1.append($(".logged-in-info").find("a").clone());
                $li2.append($(".additional-tools").find("a").clone());

                $(".logged-in-info").addClass(HIDDEN);
                $(".additional-tools").addClass(HIDDEN);

                ul.append($li1);
                ul.append($li2);

            }

        },

        handleResize = function (el) {
            var ul = el.find('ul').first()
                , li = ul.find('li')
                , droppedCount = 0
                , visible = false;


            li.each(function (index, element) {
                var el = $(element);

                if (el.position().top > li.first().position().top) {
                    el.addClass(DROP_CLASS);
                    droppedCount++;
                } else {
                    el.removeClass(DROP_CLASS);
                }
            });

            // Drop all if only one remaining
            if (li.length > 1 && $(li[1]).hasClass(DROP_CLASS)) {
                $(li[0]).addClass(DROP_CLASS);

            }

            collapseAdditionalHeaderContents(ul, droppedCount > 0);

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