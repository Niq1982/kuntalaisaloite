(function ($) {
  'use strict';

  $.fn.linkReplacer = function (options) {

    function checkProtocol(url) {
      return url.indexOf('www') === 0 ? 'http://' + url : url;
    }

    function replacer(match){
      return '<a href="' + checkProtocol(match) + '" target="_blank">' + match + '</a>';
    }

    function replaceLinks(text) {
      var exp = /\b((?:https?:\/\/|www\d{0,3}[.]|[a-z0-9.\-]+[.][a-z]{2,4}\/)(?:[^\s()<>]+|\(([^\s()<>]+|(\([^\s()<>]+\)))*\))+(?:\(([^\s()<>]+|(\([^\s()<>]+\)))*\)|[^\s`!()\[\]{};:'".,<>?«»“”‘’]))/ig;

      return text.replace(exp, replacer);
    }

    return this.each(function (index, element) {
      var container = $(element);
      container.html(replaceLinks(container.html()));
    });
  };

}(jQuery));