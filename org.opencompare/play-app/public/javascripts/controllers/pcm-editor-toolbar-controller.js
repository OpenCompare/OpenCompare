/**
 * Created by gbecan on 3/26/15.
 */

pcmApp.controller("PCMEditorToolbarController", function($rootScope, $scope) {


    /**
     * Save PCM on the server
     */
    $scope.save = function() {
        console.log("saving...");
        $rootScope.$broadcast('save');

    };

    /**
     *Remove PCM from server
     */
    $scope.remove = function() {
        $rootScope.$broadcast('remove');
    };


    /**
     * Validate the type of each columns
     */
    $scope.validate=function(){
        $rootScope.$broadcast('validate');
    };

});