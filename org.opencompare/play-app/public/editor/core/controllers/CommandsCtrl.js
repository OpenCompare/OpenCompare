/**
 * Created by hvallee on 6/19/15.
 */

pcmApp.controller("CommandsCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, featureGroupService, sortFeaturesService, editorUtil) {


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
    $scope.addFeature = function(featureName) {
        if(!featureName) {
            /* Initialize data */
            featureName = editorUtil.checkIfNameExists($scope.featureName, $scope.gridOptions.columnDefs);
        }
        var codedFeatureName = editorUtil.convertStringToEditorFormat(featureName);
        if($scope.featureType == "image") {
            $scope.pcmData.forEach(function (productData) {
                productData[codedFeatureName] = "data:image/png;base64,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";
            });
        }
        else {
            $scope.pcmData.forEach(function (productData) {
                productData[codedFeatureName] = "";
            });
        }

        $scope.pcmDataRaw.forEach(function (productData) {
            productData[codedFeatureName] = "";
        });

        /* Define the new column*/
        var columnDef = $scope.newColumnDef(featureName, null, $scope.featureType);
        $scope.gridOptions.columnDefs.push(columnDef);
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
    $scope.addFeatureGroup = function () {
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
        $scope.gridOptions.superColDefs = sortFeaturesService.sortFeatureGroupByName($scope.gridOptions.superColDefs);
        var index = 0;
        var colsToAssign = [];
        for(var col in selectedCols) {
            if(selectedCols[index].isChecked == true) {
                colsToAssign.push($scope.gridOptions.columnDefs[index+2].name);
                $scope.gridOptions.columnDefs[index+2].superCol = $scope.featureName;
            }
            index++;
        }
        $scope.deleteUnusedFeatureGroups();
        $scope.gridOptions.columnDefs = sortFeaturesService.sortByFeatureGroup($scope.gridOptions.columnDefs);

        /* Command for undo/redo */
        var parameters = [$scope.featureName, colsToAssign];
        $scope.newCommand('addFeatureGroup', parameters);
        $rootScope.$broadcast('modified');
        $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);

    };

    /**
     * Rename a feature
     */
    $scope.renameFeature = function() {

        var codedOldFeatureName =  editorUtil.convertStringToEditorFormat($scope.oldFeatureName);
        var featureName = editorUtil.checkIfNameExists($scope.featureName, $scope.gridOptions.columnDefs);
        var codedFeatureName = editorUtil.convertStringToEditorFormat(featureName);

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
                var colDef = $scope.newColumnDef(featureName, featureData.superCol, featureData.type);
                $scope.gridOptions.columnDefs.splice(index, 1, colDef);

                /* Command for undo/redo */
                var parameters = [$scope.oldFeatureName, featureName, index];
                $scope.newCommand('renameFeature', parameters);
            }
            index++;
        });
        $scope.validation[featureName] = [];
        if($scope.featureName != $scope.oldFeatureName) {
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

        var codedOldFeatureName =  editorUtil.convertStringToEditorFormat(featureGroupService.getCurrentFeatureGroup());
        var featureName = editorUtil.checkIfNameExists(newFeatureName, $scope.gridOptions.columnDefs);
        var codedFeatureName = editorUtil.convertStringToEditorFormat(featureName);

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
            }
        }
        /* Command for undo/redo */
        var parameters = [codedOldFeatureName, codedFeatureName];
        $scope.newCommand('renameFeatureGroup', parameters);
        /* re-init of scope parameters */
        $scope.featureName = "";

        /* Modified for save */
        $rootScope.$broadcast('modified');
        $rootScope.$broadcast('reloadFeatureGroup');
    };

    /**
     * Delete a feature sgroup
     * @param featureName
     */
    $scope.deleteFeatureGroup = function(featureName) {

        for(var i = 0; i < $scope.gridOptions.superColDefs.length; i++) {
            if($scope.gridOptions.superColDefs[i].name == featureName) {
                $scope.gridOptions.superColDefs.splice(i, 1);
                break;
            }
        }
        var colsToAssign = [];
        for(var i = 0; i <  $scope.gridOptions.columnDefs.length; i++) {
            if($scope.gridOptions.columnDefs[i].superCol === featureName) {
                colsToAssign.push($scope.gridOptions.columnDefs[i].name);
                $scope.gridOptions.columnDefs[i].superCol = 'emptyFeatureGroup';
            }
        }
        /* Command for undo/redo */
        var parameters = [featureName, colsToAssign];
        $scope.newCommand('deleteFeatureGroup', parameters);

        $scope.gridOptions.columnDefs = sortFeaturesService.sortByFeatureGroup($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);

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
        var codedFeatureName = editorUtil.convertStringToEditorFormat(featureName);
        var found = false;
        for(var i = 0; i < $scope.gridOptions.columnDefs.length && !found; i++) {
            if($scope.gridOptions.columnDefs[i].name == codedFeatureName) {
                var oldType =$scope.gridOptions.columnDefs[i].type;
                found = true;
                $scope.gridOptions.columnDefs.splice(i, 1);
                var colDef = $scope.newColumnDef(featureName, $scope.gridOptions.columnDefs[i].superCol, $scope.featureType);
                $timeout(function(){ $scope.gridOptions.columnDefs.splice(i-1, 0, colDef); }, 100);// Not working without a timeout
                var parameters = [featureName, oldType, $scope.featureType];
                $scope.newCommand('changeType', parameters);
            }
        }
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    };

    /**
     * Add a new product and focus on this new
     * @param row
     */
    $scope.addProduct = function(productName) {

        var productData = {};
        var rawProduct = [];
        if(productName) {
            productData.name = productName;
        }
        else {
            productData.name = "";
        }

        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            if(featureData.name != " " && featureData.name != "Product") { // There must be a better way but working for now
                productData[featureData.name] = "";
                productData.name= "";
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