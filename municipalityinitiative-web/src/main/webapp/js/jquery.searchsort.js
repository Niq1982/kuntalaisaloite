/* global $, jQuery */

(function ($) {
    'use strict';


    var setup = function (searchParameters) {

        var toggleBtn = $(searchParameters).prev().find(".search-filter-toggle");
        toggleBtn.click(function() {
            console.log("dropdown menu opening :3");
            $(searchParameters).toggle();})

    };

    $.fn.searchSort = function() {
        setup(this);
    };

}(jQuery));