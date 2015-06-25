/**
 * Created by gbecan on 6/23/15.
 */

pcmApp.config(['$translateProvider', function ($translateProvider) {

    var enTranslations = {
      "edit" : "Edit"
    };

    var frTranslations = {
        "edit" : "Éditer"
    };

    $translateProvider.translations('en', enTranslations);
    $translateProvider.translations('fr', frTranslations);
    $translateProvider.preferredLanguage('en');
    $translateProvider.useSanitizeValueStrategy('escaped');
    $translateProvider.useCookieStorage();
}]);

pcmApp.controller("I18nCtrl", function($scope, $translate) {

    $scope.currentLang = "EN";

    $scope.changeLanguage = function(langKey) {
        $scope.currentLang = langKey.toUpperCase();
        $translate.use(langKey);
    };


});