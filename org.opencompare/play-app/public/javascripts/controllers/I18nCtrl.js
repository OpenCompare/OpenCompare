/**
 * Created by gbecan on 6/23/15.
 */

pcmApp.config(function ($translateProvider) {
    $translateProvider.useSanitizeValueStrategy('escaped');
    $translateProvider.useLoader('i18nLoader');
    $translateProvider.preferredLanguage('oc');
});

pcmApp.factory('i18nLoader', function($http, $q) {
    return function(options) {
        var deferred = $q.defer();

        $http.get("/api/i18n").success(function (data) {
            return deferred.resolve(data);
        });

        return deferred.promise;
    }
});


pcmApp.controller("I18nCtrl", function($scope, $http) {

    $scope.changeLanguage = function(langKey) {
        $http.get("/api/i18n/" + langKey).success(function (data) {
            window.location.reload();
        });
    };

});