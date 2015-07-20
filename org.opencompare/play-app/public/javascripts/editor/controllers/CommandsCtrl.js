/**
 * Created by hvallee on 6/19/15.
 */

pcmApp.controller("CommandsCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, sortFeaturesService, $compile, $modal) {


    /**
     * Create a new command in the undo/redo ctrl
     */
    $scope.newCommand = function(type, parameters){

        var command = [];
        command.push(type);
        command.push(parameters);
        $scope.commands[$scope.commandsIndex] = command;
        $scope.commandsIndex++;
        $scope.canUndo = true;
    };

    /**
     * Add a new feature
     */
    $scope.addFeature = function() {

        /* Initialize data */
        var featureName = checkIfNameExists($scope.featureName, $scope.gridOptions.columnDefs);
        var codedFeatureName = convertStringToEditorFormat(featureName);

        $scope.pcmData.forEach(function (productData) {
            productData[codedFeatureName] = "";
        });
        $scope.pcmDataRaw.forEach(function (productData) {
            productData[codedFeatureName] = "";
        });

        /* Define the new column*/
        var columnDef = $scope.newColumnDef(featureName, $scope.featureType);
        $scope.gridOptions.columnDefs.push(columnDef);
        $scope.columnsType[codedFeatureName] = $scope.featureType;
        $scope.validation[codedFeatureName] = [];

        /* Command for undo/redo */
        var parameters =  [featureName, $scope.featureType, $scope.gridOptions.columnDefs.length-1];
        $scope.newCommand('addFeature', parameters);

        /* Modified for save */
        $rootScope.$broadcast('modified');
        $rootScope.$broadcast('reloadFeatureGroup');
    };

    /**
     * Add a feature group
     */
    $scope.addFeatureGroup = function () {//fixme: check if emptyfeature is setted
        var selectedCols = $scope.cols;
        var found = false;
        for(var i = 0; i < $scope.gridOptions.superColDefs.length; i++) {
            var currentCol = $scope.gridOptions.superColDefs[i];
            if(currentCol.hasOwnProperty('name') && currentCol.name == "emptyFeatureGroup") {
                found = true;
            }
        }
        if(!found) {
            var emptyFeatureGroup = {
                name: "emptyFeatureGroup",
                displayName: " "
            };
            $scope.gridOptions.superColDefs.push(emptyFeatureGroup);
            $scope.gridOptions.columnDefs.forEach(function (col) {
                col.superCol = "emptyFeatureGroup";
            });
        }

        var newFeatureGroup = {
            name: $scope.featureName,
            displayName: $scope.featureName
        };
        $scope.gridOptions.superColDefs.splice(0, 0, newFeatureGroup);
        var index = 0;
        for(var col in selectedCols) {
            if(selectedCols[index].isChecked == true) {
                $scope.gridOptions.columnDefs[index+2].superCol = $scope.featureName;
            }
            index++;
        }
        $scope.gridOptions.columnDefs = sortFeaturesService.sortByFeatureGroup($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
        $scope.deleteUnusedFeatureGroups();
        $rootScope.$broadcast('reloadFeatureGroup');
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    };

    /**
     * Rename a feature
     */
    $scope.renameFeature = function() {

        var codedOldFeatureName =  convertStringToEditorFormat($scope.oldFeatureName);
        var featureName = checkIfNameExists($scope.featureName, $scope.gridOptions.columnDefs);
        var codedFeatureName = convertStringToEditorFormat(featureName);

        /* Find the feature in column defs */
        var index = 0;
        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            if(featureData.name === codedOldFeatureName) {
                if(codedOldFeatureName === $scope.featureName){
                    featureName = $scope.oldFeatureName;
                }
                var index2 = 0;
                /* Create a new feature with the new name and delete the old */
                $scope.pcmData.forEach(function (productData) {
                    productData[codedFeatureName] = productData[codedOldFeatureName];
                    $scope.pcmDataRaw[index2][codedFeatureName] = $scope.pcmDataRaw[index2][codedOldFeatureName];
                    if($scope.featureName != $scope.oldFeatureName) {
                        delete productData[codedOldFeatureName];
                        delete $scope.pcmDataRaw[index2][codedOldFeatureName]
                    }
                    index2++;
                });
                /* Add the new column to column defs */
                var colDef = $scope.newColumnDef(featureName, $scope.columnsType[codedOldFeatureName]);
                $scope.gridOptions.columnDefs.splice(index, 1, colDef);

                /* Command for undo/redo */
                var parameters = [$scope.oldFeatureName, featureName, index];
                $scope.newCommand('renameFeature', parameters);
            }
            index++;
        });
        $scope.columnsType[featureName] = $scope.columnsType[codedOldFeatureName];
        $scope.validation[featureName] = [];
        if($scope.featureName != $scope.oldFeatureName) {
            delete $scope.columnsType[codedOldFeatureName];
            delete $scope.validation[codedOldFeatureName];
        }
        /* re-init of scope parameters */
        $scope.featureName = "";
        $scope.oldFeatureName = "";

        /* Modified for save */
        $rootScope.$broadcast('modified');
    };

    /**
     * Rename a feature group
     */
    $scope.renameFeatureGroup = function(oldFeatureName, newFeatureName) {

        var codedOldFeatureName =  convertStringToEditorFormat(oldFeatureName);
        var featureName = checkIfNameExists(newFeatureName, $scope.gridOptions.columnDefs);
        var codedFeatureName = convertStringToEditorFormat(featureName);

        /* Find the feature in column defs */
        for(var i = 0; i < $scope.gridOptions.superColDefs.length; i++) {
            if($scope.gridOptions.superColDefs[i].name == codedOldFeatureName) {
                $scope.gridOptions.superColDefs[i].name = codedFeatureName;
                $scope.gridOptions.superColDefs[i].displayName = codedFeatureName;
                break;
            }
        }
        for(var i = 0; i <  $scope.gridOptions.columnDefs.length; i++) {
            if($scope.gridOptions.columnDefs[i].superCol === codedOldFeatureName) {
                $scope.gridOptions.columnDefs[i].superCol = codedFeatureName;
                break;
            }
        }

        /* re-init of scope parameters */
        $scope.featureName = "";
        $scope.oldFeatureName = "";

        /* Modified for save */
        $rootScope.$broadcast('modified');
        $rootScope.$broadcast('reloadFeatureGroup');
    };

    /**
     * Delete a feature sgroup
     * @param featureName
     */
    $scope.deleteFeatureGroup = function(featureName) {

        var index = 0;
        var indexToDelete = 0;
        for(var col in $scope.gridOptions.superColDefs) {
            if(col.hasOwnProperty('name')) {
                if(col.name == featureName) {
                    indexToDelete = index;
                }
                index++;
            }
        }
        if(index <= 1) {
            delete $scope.gridOptions.superColDefs;
        }
        else {
            delete $scope.gridOptions.superColDefs[indexToDelete];
        }
        for(var i = 0; i <  $scope.gridOptions.columnDefs.length; i++) {
            if($scope.gridOptions.columnDefs[i].superCol === featureName) {
                $scope.gridOptions.columnDefs[i].superCol = 'emptyFeatureGroup';
                break;
            }
        }
        $rootScope.$broadcast('modified');
        $rootScope.$broadcast('reloadFeatureGroup');
    };

    /**
     * Delete a feature
     * @param featureName
     */
    $scope.deleteFeature = function(featureName) {

        delete $scope.validation[featureName];
        var index = 0;
        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            if(featureData.name === featureName) {
                var parameters = [];
                var values = [];
                var rawValues = [];
                var index2 = 0;
                $scope.pcmData.forEach(function (productData) {
                    var value = [productData.$$hashKey, productData[featureName]];
                    var rawValue = [productData.$$hashKey, $scope.pcmDataRaw[index2][featureName]];
                    values.push(value);
                    rawValues.push(rawValue);
                    delete $scope.pcmData[index2][featureData.name];
                    delete $scope.pcmDataRaw[index2][featureData.name];
                    index2++;
                });
                parameters.push($scope.gridOptions.columnDefs[index]);
                parameters.push(values);
                parameters.push(rawValues);
                parameters.push(index);
                $scope.newCommand('removeFeature', parameters);
                $scope.gridOptions.columnDefs.splice(index, 1);
            }
            index++;
        });
        console.log("Feature is deleted");
        $rootScope.$broadcast('modified');
        $rootScope.$broadcast('reloadFeatureGroup');
    };

    /**
     * Change the type of a column
     */
    $scope.changeType = function () {

        var featureName = $scope.featureName;
        var codedFeatureName = convertStringToEditorFormat(featureName);
        var found = false;
        for(var i = 0; i < $scope.gridOptions.columnDefs.length && !found; i++) {
            if($scope.gridOptions.columnDefs[i].name == codedFeatureName) {
                var oldType = $scope.columnsType[codedFeatureName];
                found = true;
                $scope.gridOptions.columnDefs.splice(i, 1);
                var colDef = $scope.newColumnDef(featureName, $scope.featureType);
                $timeout(function(){ $scope.gridOptions.columnDefs.splice(i-1, 0, colDef); }, 100);// Not working without a timeout
                var parameters = [featureName, oldType, $scope.featureType];
                $scope.newCommand('changeType', parameters);
                $scope.columnsType[codedFeatureName] = $scope.featureType;
            }
        }
        $rootScope.$broadcast('reloadFeatureGroup');
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    };

    /**
     * Add a new product and focus on this new
     * @param row
     */
    $scope.addProduct = function() {

        var productData = {};
        var rawProduct = [];
        productData.name = "";

        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            if(featureData.name != " " && featureData.name != "Product") { // There must be a better way but working for now
                productData[featureData.name] = "";
                rawProduct[featureData.name] = "";
            }
        });
        $scope.pcmDataRaw.push(rawProduct);
        $scope.pcmData.push(productData);

        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
        $timeout(function(){ $scope.scrollToFocus($scope.pcmData.length-1, 1); }, 100);// Not working without a timeout
        console.log("Product added");
        $rootScope.$broadcast('modified');
        var parameters = $scope.pcmData[$scope.pcmData.length-1];
        $scope.newCommand('addProduct', parameters);
        $scope.setGridHeight();
    };

    /**
     * Remove a product
     * @param row
     */
    $scope.removeProduct = function(row) {

        var index = $scope.pcmData.indexOf(row.entity);
        var rawData = $scope.pcmDataRaw[index];
        $scope.pcmData.splice(index, 1);
        $scope.pcmDataRaw.splice(index, 1);
        $rootScope.$broadcast('modified');
        var parameters = [row.entity.$$hashKey, row.entity, rawData, index];
        $scope.newCommand('removeProduct', parameters);
        $scope.setGridHeight();
    };

});