/* global $, jQuery */

(function ($) {
    'use strict';



    var setup = function (searchParameters) {


        var toggleBtn = $('<a href="#" class="search-filter-toggle" ></a>');

        $(searchParameters).after(toggleBtn);

        toggleBtn.click(function() {$(searchParameters).toggleClass("dropdown-menu");})

    };

    $.fn.searchFilter = function() {
        setup(this);
    };

}(jQuery));