/**
 * Created by hvallee on 6/19/15.
 */

pcmApp.controller("FeatureGroupCtrl", function($rootScope, $scope, $window, $http, $timeout, uiGridConstants, $compile, $modal) {

    $scope.cols = {};

    $scope.isAFeature = function(col) {
        return col.name != ' ' && col.name != 'Product';
    };
    $scope.isAFeatureGroup = function(col) {console.log(col.name);
        return col.name != 'emptyFeatureGroup';
    };

    $scope.setCols = function() {
        var cols = $scope.gridOptions.columnDefs;
        for(var i = 2; i < cols.length; i ++) {
            $scope.cols[i-2] = {name: cols[i].name, isChecked: false};
        }
    };

    $scope.setRenameFeatureGroupModal = function(featureName) {
        $scope.featureName = featureName;
    };

    $scope.deleteUnusedFeatureGroups = function(){
        var featureGroups = [];
        var j = 0;
        for(var i = 0; i < $scope.gridOptions.columnDefs.length; i++) {
            if(featureGroups.indexOf($scope.gridOptions.columnDefs[i].superCol) == -1) {
                featureGroups[j] = $scope.gridOptions.columnDefs[i].superCol;
                j++;
            }
        }
        if(featureGroups.length < $scope.gridOptions.superColDefs.length) {
            for(var i = 0; i < $scope.gridOptions.superColDefs.length; i++) {
                if(featureGroups.indexOf($scope.gridOptions.superColDefs[i].name) == -1) {
                   $scope.gridOptions.superColDefs.splice(i, 1);
                }
            }
        }
    };

    $scope.$watch(function(){
        return $window.innerWidth;
    }, function(value) {
        $rootScope.$broadcast('reloadFeatureGroup');
    });

});