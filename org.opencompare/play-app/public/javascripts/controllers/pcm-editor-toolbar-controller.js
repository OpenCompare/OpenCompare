/**
 * Created by gbecan on 3/26/15.
 */

pcmApp.controller("PCMEditorToolbarController", function($rootScope, $scope) {

    $scope.saved = false;
    $scope.isInDatabase = false;
    $scope.validating = false;
    $scope.edit = false;
    $scope.isTitleSet = false;

    /**
     * Save PCM on the server
     */
    $scope.save = function() {
        $rootScope.$broadcast('save');
    };

    /**
     * Remove PCM from server
     */
    $scope.remove = function() {
        $rootScope.$broadcast('remove');
    };

    /**
     * Cancel edition
     */
    $scope.cancel = function() {
        $rootScope.$broadcast('cancel');
    };

    /**
     * Export
     */
    $scope.export = function(format) {
        $rootScope.$broadcast('export', format);
    };
    /**
     * Validate the type of each columns
     */
    $scope.validate= function() {
        $rootScope.$broadcast('validate');
    };

    $scope.setEdit = function(bool, reload) {
        $scope.edit = bool;
        $rootScope.$broadcast('setGridEdit', [bool, reload]);
    };

    $scope.$on('modified', function(event, args) {
        $scope.saved = false;
    });
    $scope.$on('validating', function(event, args) {
        $scope.validating = !$scope.validating;
    });

    $scope.$on('completelyValidated', function(event, args) {
        $scope.validated = true;
    });

    $scope.$on('saved', function(event, args) {
        $scope.saved = true;
        $scope.isInDatabase = true;
    });

    $scope.$on('setToolbarEdit', function(event, args) {
        $scope.edit = args;
    });

    $scope.$on('setPcmName', function(event, args) {
        $scope.isTitleSet = args.length > 0;
        $scope.pcmName = args;
    });

});