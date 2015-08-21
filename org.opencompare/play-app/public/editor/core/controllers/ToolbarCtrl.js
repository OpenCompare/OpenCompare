/**
 * Created by gbecan on 3/26/15.
 * Updated by hvallee on 8/17/15
 */

/**
 * ToolbarCtrl.js
 * Manage editor toolbar
 */
pcmApp.controller("ToolbarCtrl", function($rootScope, $scope, $modal) {

    $scope.saved = false;
    $scope.isInDatabase = false;
    $scope.validating = false;
    $scope.edit = false;
    $scope.configurator = false;
    $scope.lineView = true;
    $scope.isTitleSet = false;
    $scope.launchFromCreator = false;

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

    $scope.setConfigurator = function(bool) {
        $scope.configurator = bool;
        $rootScope.$broadcast('setConfiguratorMode', bool);
    };

    $scope.openCreateFeatureGroupModal = function() {

        $scope.$modalInstance = $modal.open({
            templateUrl: '/assets/editor/templates/modalCreateFeatureGroup.html',
            scope: $scope,
            controller: 'ToolbarCtrl'
        });
    };

    $scope.addFeatureGroup = function(featureGroup, features) {
        $rootScope.$broadcast('addFeatureGroup', {"featureGroup": featureGroup, "features": features});
        $scope.$modalInstance.close();
    };

    $scope.cancelModal = function() {
        $scope.$modalInstance.close();
    };

    /** Set the line view in configurator mode */
    $scope.$on('setLineView', function(event, arg) {
        $scope.lineView = arg;
    });

    /** Launch grid creation in creator mode */
    $scope.$on('launchFromCreator', function(event, args) {
        $scope.launchFromCreator = true;
    });

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

    $scope.increaseHeight = function(height) {
        $rootScope.$broadcast('increaseHeight', height);
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
        if(args) {
            $scope.id = args;
        }
    });

    $scope.$on('savedFromCreator', function(event, args) {
        $scope.saved = true;
        $scope.isInDatabase = true;
        $scope.id = args;
    });

    $scope.$on('setToolbarEdit', function(event, args) {
        $scope.edit = args;
    });

    $scope.$on('launchCreation', function(event, args) {
        $scope.edit = true;
        $scope.isTitleSet = true;
        $scope.pcmName = args.title;

    });

    $scope.$on('setPcmName', function(event, args) {
        $scope.isTitleSet = args.length > 0;
        $scope.pcmName = args;
    });

});