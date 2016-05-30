var exec = require('cordova/exec');

/**
 * @namespace cordova.plugins
 * @exports Gallery
 */
var Gallery = {};

Gallery.getAllPhotos = function(successCallback, errorCallback) {
  return exec(successCallback, errorCallback, "Gallery", "getAllPhotos", []);
};

module.exports = Gallery;
