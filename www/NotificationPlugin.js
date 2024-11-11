var exec = require('cordova/exec');

exports.showNotification = function(options, successCallback, errorCallback) {
    exec(successCallback, errorCallback, "NotificationPlugin", "showNotification", [options]);
};
