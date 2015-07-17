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

    $scope.$watch(function(){
        return $window.innerWidth;
    }, function(value) {
        $rootScope.$broadcast('reloadFeatureGroup');
    });

});