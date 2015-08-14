/**
 * Created by gbecan on 18/02/15.
 */


pcmApp.controller("NavbarController", function($scope, $location) {

    $scope.isActive = function (viewLocation) {
        return $location.absUrl().indexOf(viewLocation) > -1 ;
    };

});