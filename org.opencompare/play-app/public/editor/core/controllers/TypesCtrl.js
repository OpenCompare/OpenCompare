/**
 * Created by hvallee on 6/19/15.
 * Updated by hvallee on 8/17/15
 */

/**
 * TypesCtrl.js
 * Manage types in editor
 */
pcmApp.controller("TypesCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, $compile, $modal, typeService) {

    // Validate pcm type
    $scope.featureType = 'string';
    $scope.validation = [];
    $scope.validating = false;


    /**
     * Validate data based of type columns
     */
    $scope.validate = function() {
        /* change validation mode */
        $scope.validating = !$scope.validating;
        /* Init validation array */
        if($scope.pcmData.length > 0){
            var initValid = [];
            var index = 0;
            $scope.gridOptions.columnDefs.forEach(function (featureData){
                if(featureData.name != " " && featureData.name != "Product"){
                    $scope.validation[featureData.name] = [];
                    initValid[index] = featureData.name;
                    index++;
                }
            });
            /* Fill in validation array */
            index = 0;
            $scope.pcmData.forEach(function (productData){
                for(var i = 0; i < initValid.length; i++) {
                    var featureName = initValid[i];
                    if(featureName != " ") {
                        var type = '';
                        $scope.gridOptions.columnDefs.forEach(function (featureData){
                            if(featureData.name == featureName) {
                                type = featureData.type;
                            }
                        });
                        $scope.validation[featureName][index] =  typeService.validateType(productData[featureName], type);
                    }
                }
                index++;
            });
        }
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
        $rootScope.$broadcast("validating");
    };



});



