/**
 * Created by hvallee on 6/19/15.
 */

pcmApp.controller("UndoRedoCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, $compile, $modal) {

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
                    break;
                case 'renameFeature':
                    undoRenameFeature(parameters[0], parameters[1], parameters[2]);
                    break;
                case 'changeType':
                    undoChangeType(parameters[0], parameters[1]);
                    break;
                case 'addFeature':
                    undoAddFeature(parameters[0], parameters[2]);
                    break;
            }
            if($scope.commandsIndex <= 0){
                $scope.canUndo = false;
            }
        }
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
                    redoEdit(parameters[0], parameters[1], parameters[3], parameters[5]);
                    break;
                case 'removeProduct':
                    undoAddProduct(parameters[0]);
                    break;
                case 'addProduct':
                    redoAddProduct(parameters);
                    break;
                case 'removeFeature':
                    undoAddFeature(parameters[0].name, parameters[3]);
                    break;
                case 'renameFeature':
                    undoRenameFeature(parameters[1], parameters[0], parameters[2]);
                    break;
                case 'changeType':
                    undoChangeType(parameters[0], parameters[2]);
                    break;
                case 'addFeature':
                    redoAddFeature(parameters[0], parameters[1]);
            }
            $scope.commandsIndex++;
            $scope.canUndo = true;
            if($scope.commandsIndex >= $scope.commands.length){
                $scope.canRedo = false;
            }
        }
    };

    function undoEdit(productHashKey, featureName, oldValue,  oldRawValue) {
        var found = false;
        for(var i = 0; i < $scope.pcmData.length && !found; i++) {
            if ($scope.pcmData[i].$$hashKey == productHashKey) {
                $scope.pcmData[i][featureName] = oldValue;
                $scope.pcmDataRaw[i][featureName] = oldRawValue;
                found = true;
            }
        }
    }

    function redoEdit(productHashKey, featureName, newValue,  newRawValue) {
        var found = false;
        for(var i = 0; i < $scope.pcmData.length && !found; i++) {
            if ($scope.pcmData[i].$$hashKey == productHashKey) {
                $scope.pcmData[i][featureName] = newValue;
                $scope.pcmDataRaw[i][featureName] = newRawValue;
                found = true;
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
        var featureName = checkIfNameExists(featureName, $scope.gridOptions.columnDefs);
        $scope.pcmData.forEach(function (productData) {
            productData[featureName] = "";
        });
        $scope.pcmDataRaw.forEach(function (productData) {
            productData[featureName] = "";
        });
        $scope.columnsType[featureName] = featureType;
        $scope.validation[featureName] = [];
        $rootScope.$broadcast('modified');
    }

    function undoRenameFeature(oldFeatureName, featureName, index) {

        var codedFeatureName = convertStringToEditorFormat(featureName);
        var codedOldFeatureName = convertStringToEditorFormat(oldFeatureName);
        var found = false;
        for(var i = 0; !found && i < $scope.gridOptions.columnDefs.length; i++) {
            if($scope.gridOptions.columnDefs[i].name === codedFeatureName) {
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
                var colDef = $scope.newColumnDef(oldFeatureName, $scope.columnsType[codedFeatureName]);
                $scope.gridOptions.columnDefs.splice(index, 1, colDef);
            }
        }
        $scope.columnsType[codedOldFeatureName] = $scope.columnsType[codedFeatureName];
        $scope.validation[codedFeatureName] = [];
        if(codedOldFeatureName != codedOldFeatureName) {
            delete $scope.columnsType[codedFeatureName];
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





});
