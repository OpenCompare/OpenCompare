/**
 * Created by smangin on 19/05/15.
 */


pcmApp.controller("PCMImporterController", function($rootScope, $scope, $http) {

    // Default values
    $scope.file = null;
    $scope.config = {
        title: "",
        productAsLines: true,
        separator: ',',
        quote: '"',
    };
});
	



