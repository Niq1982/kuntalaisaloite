/* global $, jQuery */

(function ($) {
    'use strict';



    var setup = function (that) {

        $(that).find("a").each(function(index, element) {
            $(element).addClass("hide");
            console.log(index + ":th element is " + element);
        });
        var toggleBtn = $('<a href="#" class="search-filter-toggle" ></a>');

        $(that).append(toggleBtn);

        toggleBtn.click(function() {console.log("button clicked")})

    };

    $.fn.searchFilter = function (methodOrOptions) {

        setup(this);
    };

}(jQuery));