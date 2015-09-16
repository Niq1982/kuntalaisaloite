/* global $, jQuery */

(function ($) {
    'use strict';



    var setup = function (searchParameters) {

        var toggleBtn = $(searchParameters).parent().prev().find(".search-filter-toggle");
        toggleBtn.click(function() {
            $(searchParameters).toggleClass("dropdown-menu");})

    };

    $.fn.searchFilter = function() {
        setup(this);
    };

}(jQuery));