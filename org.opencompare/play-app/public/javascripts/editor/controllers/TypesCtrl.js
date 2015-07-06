/**
 * Created by hvallee on 6/19/15.
 */
pcmApp.controller("TypesCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, $compile, $modal) {

    // Validate pcm type
    $scope.columnsType = [];
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
                        $scope.validation[featureName][index] =  validateType(productData[featureName], $scope.columnsType[featureName]);
                    }
                }
                index++;
            });
        }
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
        $rootScope.$broadcast("validating");
    };


    /**
     * Return the type of a column
     * @param featureName
     * @returns {string}
     */
    $scope.getType = function(featureName, data) {
        var rowIndex = 0;
        var isInt = 0;
        var isBool = 0;
        var isString = 0;
        var codedFeatureName = convertStringToEditorFormat(featureName);
        while(data[rowIndex]) {
            if(data[rowIndex][codedFeatureName]) {
                if (!angular.equals(parseInt(data[rowIndex][codedFeatureName]), NaN)) {
                    isInt++;
                }
                else if (isBooleanValue(data[rowIndex][codedFeatureName])) {
                    isBool++;
                }
                else if (!isEmptyCell(data[rowIndex][codedFeatureName])) {
                    isString++;
                }
            }
            rowIndex++;
        }
        var type = "";
        if(isInt > isBool) {
            if(isInt > isString) {
                type = "number";
            }
            else {
                type = "string";
            }
        }
        else if(isBool > isString) {
            type = "boolean";
        }
        else {
            type = "string";
        }
        return type;
    };

    function validateType (productName, featureType) {

        var type = "";
        if(!angular.equals(parseInt(productName), NaN)) {
            type = "number";
        }
        else if(isBooleanValue(productName)) {
            type = "boolean";
        }
        else if(!isEmptyCell(productName)){
            type = "string";
        }
        else {
            type = "none"
        }
        if(type == "none") {
            return true;
        }
        else if (featureType == "string") {
            return true;
        }
        else {
            return type === featureType;
        }
    }



    function isBooleanValue (productName) {

        return((productName.toLowerCase() === "yes") ||  (productName.toLowerCase() === "true") ||  (productName.toLowerCase() === "no") ||  (productName.toLowerCase() === "false"));
    }
});



