/**
 * Created by hvallee on 6/19/15.
 * Updated by hvallee on 8/17/15.
 */

/**
 * UndoRedoCtrl.js
 * Manage undo/redo
 */
pcmApp.controller("UndoRedoCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, sortFeaturesService, editorUtil) {

    //Undo-redo
    $scope.commands = [];
    $scope.commandsIndex = 0;
    $scope.canUndo = false;
    $scope.canRedo = false;

    $scope.undo = function() {

        if($scope.commandsIndex > 0) {
            $scope.commandsIndex--;
            $scope.canRedo = true;
            var command = $scope.commands[$scope.commandsIndex];
            var parameters = command[1];

            switch(command[0]) {
                case 'move':
                    $scope.gridOptions.columnDefs.move(parameters[1], parameters[0]);
                    break;
                case 'edit':
                    undoEdit(parameters[0], [parameters[1]],  parameters[2]);
                    break;
                case 'removeProduct':
                    undoRemoveProduct(parameters[1], parameters[2], parameters[3]);
                    break;
                case 'addProduct':
                    undoAddProduct(parameters.$$hashKey);
                    break;
                case 'removeFeature':
                    undoRemoveFeature(parameters[0], parameters[1], parameters[2], parameters[3]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'renameFeature':
                    undoRenameFeature(parameters[0], parameters[1], parameters[2]);
                    break;
                case 'changeType':
                    undoChangeType(parameters[0], parameters[1]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'addFeature':
                    undoAddFeature(parameters[0], parameters[2]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'addFeatureGroup':
                    undoAddFeatureGroup(parameters[0]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'deleteFeatureGroup':
                    undoDeleteFeatureGroup(parameters[0], parameters[1]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'renameFeatureGroup':
                    undoRenameFeatureGroup(parameters[0], parameters[1]);
                    break;
                case 'assignFeatureGroup':
                    undoAssignFeatureGroup(parameters[0], parameters[1]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);;
                    break;
            }
            if($scope.commandsIndex <= 0){
                $scope.canUndo = false;
            }
        }
        $rootScope.$broadcast('modified');
    };

    $scope.redo = function() {

        if($scope.commandsIndex < $scope.commands.length) {
            var command = $scope.commands[$scope.commandsIndex];
            var parameters = command[1];
            switch(command[0]) {
                case 'move':
                    $scope.gridOptions.columnDefs.move(parameters[0], parameters[1]);
                    break;
                case 'edit':
                    redoEdit(parameters[0], parameters[1], parameters[3]);
                    break;
                case 'removeProduct':
                    undoAddProduct(parameters[0]);
                    break;
                case 'addProduct':
                    redoAddProduct(parameters);
                    break;
                case 'removeFeature':
                    undoAddFeature(parameters[0].name, parameters[3]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'renameFeature':
                    undoRenameFeature(parameters[1], parameters[0], parameters[2]);
                    break;
                case 'changeType':
                    undoChangeType(parameters[0], parameters[2]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'addFeature':
                    redoAddFeature(parameters[0], parameters[1]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'addFeatureGroup':
                    undoDeleteFeatureGroup(parameters[0], parameters[1]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'deleteFeatureGroup':
                    undoAddFeatureGroup(parameters[0]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'renameFeatureGroup':
                    undoRenameFeatureGroup(parameters[1], parameters[0]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
                case 'assignFeatureGroup':
                    redoAssignFeatureGroup(parameters[0], parameters[1]);
                    $scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);
                    break;
            }
            $scope.commandsIndex++;
            $scope.canUndo = true;
            if($scope.commandsIndex >= $scope.commands.length){
                $scope.canRedo = false;
            }
        }
        $rootScope.$broadcast('modified');
    };

    function undoEdit(productHashKey, featureName, oldValue) {

        for(var i = 0; i < $scope.pcmData.length; i++) {
            if ($scope.pcmData[i].$$hashKey == productHashKey) {

                $http.post("/api/extract-content", {
                    type: 'wikipedia',
                    rawContent: oldValue,
                    responseType: "text/plain",
                    transformResponse: function(d, e) { // Needed to not interpret matrix as json (begin with '{|')
                        return d;
                    }
                }).success(function(data) {
                    $scope.pcmData[i][featureName] = data;
                });
                $scope.pcmDataRaw[i][featureName] = oldValue;
                console.log(oldValue);
                break;
            }
        }
    }

    function redoEdit(productHashKey, featureName, newValue) {

        for(var i = 0; i < $scope.pcmData.length; i++) {
            if ($scope.pcmData[i].$$hashKey == productHashKey) {

                $http.post("/api/extract-content", {
                    type: 'wikipedia',
                    rawContent: newValue,
                    responseType: "text/plain",
                    transformResponse: function(d, e) { // Needed to not interpret matrix as json (begin with '{|')
                        return d;
                    }
                }).success(function(data) {
                    $scope.pcmData[i][featureName] = data;
                });
                $scope.pcmDataRaw[i][featureName] = newValue;
                break;
            }
        }
    }

    function undoRemoveProduct(product, rawProduct, index) {
        $scope.pcmData.splice(index, 0, product);
        $scope.pcmDataRaw.splice(index, 0, rawProduct);
        $timeout(function(){ $scope.scrollToFocus(index, 1); }, 100);// Not working without a timeout
        $scope.setGridHeight();
    }

    function undoAddProduct(productHashKey) {
        var found = false;
        for(var i = 0; i < $scope.pcmData.length && !found; i++) {
            if ($scope.pcmData[i].$$hashKey == productHashKey) {
                $scope.pcmData.splice($scope.pcmData.indexOf($scope.pcmData[i]), 1);
                $scope.pcmDataRaw.splice($scope.pcmData.indexOf($scope.pcmData[i]), 1);
                found = true;
            }
        }
        $scope.setGridHeight();
    }

    function redoAddProduct(parameters) {
        $scope.pcmData.push(parameters);
        $scope.pcmDataRaw.push(parameters);
        $timeout(function(){ $scope.scrollToFocus($scope.pcmData.length-1, 1); }, 100);// Not working without a timeout
        $scope.setGridHeight();
    }

    function undoRemoveFeature(feature, products, rawProducts, featureIndex) {
        $scope.gridOptions.columnDefs.splice(featureIndex, 0, feature);
        $scope.pcmData.forEach(function(product){
            var i = 0;
            var found = false;
            while(i < products.length && !found) {
                if(product.$$hashKey == products[i][0]) {
                    product[feature.name] = products[i][1];
                    $scope.pcmDataRaw[i][feature.name] = rawProducts[i][1];
                    found = true;
                }
                i++;
            }
        });
    }

    function redoAddFeature(featureName, featureType) {
        var columnDef = $scope.newColumnDef(featureName, featureType);
        $scope.gridOptions.columnDefs.push(columnDef);
        /* Initialize data */
        var featureName = editorUtil.checkIfNameExists(featureName, $scope.gridOptions.columnDefs);
        $scope.pcmData.forEach(function (productData) {
            productData[featureName] = "";
        });
        $scope.pcmDataRaw.forEach(function (productData) {
            productData[featureName] = "";
        });
        $scope.validation[featureName] = [];
        $rootScope.$broadcast('modified');
    }

    function undoRenameFeature(oldFeatureName, featureName, index) {

        var codedFeatureName = editorUtil.convertStringToEditorFormat(featureName);
        var codedOldFeatureName = editorUtil.convertStringToEditorFormat(oldFeatureName);
        var found = false;
        var type = '';
        for(var i = 0; !found && i < $scope.gridOptions.columnDefs.length; i++) {
            if($scope.gridOptions.columnDefs[i].name === codedFeatureName) {
                type = $scope.gridOptions.columnDefs[i].type;
                found = true;
                var index2 = 0;
                $scope.pcmData.forEach(function (productData) {
                    productData[codedOldFeatureName] = productData[codedFeatureName];
                    $scope.pcmDataRaw[index2][codedOldFeatureName] = $scope.pcmDataRaw[index2][codedFeatureName];
                    if(featureName != codedOldFeatureName) {
                        delete productData[codedFeatureName];
                        delete $scope.pcmDataRaw[index2][codedFeatureName];
                    }
                    index2++;
                });
                var colDef = $scope.newColumnDef(oldFeatureName, type);
                $scope.gridOptions.columnDefs.splice(index, 1, colDef);
            }
        }
        $scope.validation[codedFeatureName] = [];
        if(codedOldFeatureName != codedOldFeatureName) {
            delete $scope.validation[codedFeatureName];
        }
    }

    function undoChangeType(featureName, oldType) {
        var found = false;
        for(var i = 0; i < $scope.gridOptions.columnDefs.length && !found; i++) {
            if($scope.gridOptions.columnDefs[i].name == featureName) {
                found = true;
                $scope.gridOptions.columnDefs.splice(i, 1);
                var colDef = $scope.newColumnDef(featureName, oldType);
                $timeout(function(){ $scope.gridOptions.columnDefs.splice(i-1, 0, colDef); }, 100);// Not working without a timeout
            }
        }
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    }

    function undoAddFeature(featureName, index) {
        $scope.pcmDataRaw.forEach(function (productData) {
            delete productData[featureName];
        });
        $scope.pcmData.forEach(function (productData) {
            delete productData[featureName];
        });
        $scope.gridOptions.columnDefs.splice(index, 1);
    }

    function undoAddFeatureGroup(featureName) {
        for(var i = 0; i < $scope.gridOptions.columnDefs.length; i++) {
            if($scope.gridOptions.columnDefs[i].superCol == featureName) {
                $scope.gridOptions.columnDefs[i].superCol = 'emptyFeatureGroup';
            }
        }
        var index = 0;
        for (var col in $scope.gridOptions.superColDefs) {
            if($scope.gridOptions.superColDefs[index] && $scope.gridOptions.superColDefs[index].name == featureName) {
                $scope.gridOptions.superColDefs.splice(index, 1);
                break;
            }
            index++;
         }
    }

    function undoDeleteFeatureGroup(featureName, cols) {

        for(var i = 0; i < $scope.gridOptions.columnDefs.length; i++) {
            for(var j = 0; j < cols.length; j++) {
                if(cols[j] == $scope.gridOptions.columnDefs[i].name ) {
                    $scope.gridOptions.columnDefs[i].superCol = featureName;
                    break;
                }
            }
        }

        var featureGroup = {
            name: featureName,
            displayName: featureName
        };
        $scope.gridOptions.superColDefs.push(featureGroup);


        $scope.gridOptions.superColDefs = sortFeaturesService.sortFeatureGroupByName($scope.gridOptions.superColDefs);
        $scope.deleteUnusedFeatureGroups();
        $scope.gridOptions.columnDefs = sortFeaturesService.sortByFeatureGroup($scope.gridOptions.columnDefs);
    }

    function undoRenameFeatureGroup(oldFeatureName, newFeatureName) {

        for(var i = 0; i < $scope.gridOptions.columnDefs.length; i++) {
            if($scope.gridOptions.columnDefs[i].superCol == newFeatureName) {
                $scope.gridOptions.columnDefs[i].superCol = oldFeatureName;
                break;
            }
        }
        var index = 0;
        for (var col in $scope.gridOptions.superColDefs) {
            if($scope.gridOptions.superColDefs[index] && $scope.gridOptions.superColDefs[index].name == newFeatureName) {
                $scope.gridOptions.superColDefs[index].name = oldFeatureName;
                $scope.gridOptions.superColDefs[index].displayName = oldFeatureName;
                break;
            }
            index++;
        }

        $scope.gridOptions.superColDefs = sortFeaturesService.sortFeatureGroupByName($scope.gridOptions.superColDefs);
    }





});
