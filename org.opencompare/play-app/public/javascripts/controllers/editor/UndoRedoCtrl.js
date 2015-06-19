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
                    var index = 0;
                    $scope.pcmData.forEach(function(product){
                        if(product.$$hashKey == parameters[0]) {
                            product[parameters[1]] = parameters[2];
                            $scope.pcmDataRaw[index][parameters[1]] =  parameters[4];
                        }
                        index++;
                    });
                    break;
                case 'removeProduct':
                    $scope.pcmData.splice(parameters[3], 0, parameters[1]);
                    $scope.pcmDataRaw.splice(parameters[3], 0, parameters[2]);
                    $timeout(function(){ $scope.scrollToFocus(parameters[2], 1); }, 100);// Not working without a timeout
                    $scope.setGridHeight();
                    break;
                case 'addProduct':
                    $scope.pcmData.forEach(function(product){
                        if(product.$$hashKey == parameters.$$hashKey) {
                            $scope.pcmData.splice($scope.pcmData.indexOf(product), 1);
                            $scope.pcmData.splice($scope.pcmDataRaw.indexOf(product), 1);
                        }
                    });
                    $scope.setGridHeight();
                    break;
                case 'removeFeature':
                    var values = parameters[1];
                    var rawValues = parameters[2];
                    $scope.gridOptions.columnDefs.splice(parameters[3], 0, parameters[0]);
                    $scope.pcmData.forEach(function(product){
                        var i = 0;
                        var found = false;
                        while(i < values.length && !found) {
                            if(product.$$hashKey == values[i][0]) {
                                product[parameters[0].name] = values[i][1];
                                $scope.pcmDataRaw[i][parameters[0].name] = rawValues[i][1];
                                found = true;
                            }
                            i++;
                        }
                    });
                    break;
                case 'renameFeature':
                    var oldFeatureName = parameters[0];
                    var featureName = parameters[1];
                    var codedFeatureName = convertStringToEditorFormat(featureName);
                    var codedOldFeatureName = convertStringToEditorFormat(oldFeatureName);
                    var index = parameters[2];
                    $scope.gridOptions.columnDefs.forEach(function(featureData) {
                        if(featureData.name === codedFeatureName) {
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
                    });
                    $scope.columnsType[codedOldFeatureName] = $scope.columnsType[codedFeatureName];
                    $scope.validation[codedFeatureName] = [];
                    if(codedOldFeatureName != codedOldFeatureName) {
                        delete $scope.columnsType[codedFeatureName];
                        delete $scope.validation[codedFeatureName];
                    }
                    break;
                case 'changeType':
                    var featureName = parameters[0];
                    var found = false;
                    for(var i = 0; i < $scope.gridOptions.columnDefs.length && !found; i++) {
                        if($scope.gridOptions.columnDefs[i].name == featureName) {
                            found = true;
                            $scope.gridOptions.columnDefs.splice(i, 1);
                            var colDef = $scope.newColumnDef(featureName, parameters[1]);
                            $timeout(function(){ $scope.gridOptions.columnDefs.splice(i-1, 0, colDef); }, 100);// Not working without a timeout
                        }
                    }
                    $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                    break;
                case 'addFeature':
                    $scope.pcmDataRaw.forEach(function (productData) {
                        delete productData[parameters[0]];
                    });
                    $scope.pcmData.forEach(function (productData) {
                        delete productData[parameters[0]];
                    });
                    $scope.gridOptions.columnDefs.splice(parameters[2], 1);
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
                    var index = 0;
                    $scope.pcmData.forEach(function(product){
                        if(product.$$hashKey == parameters[0]) {
                            product[parameters[1]] = parameters[3];
                            $scope.pcmDataRaw[index][parameters[1]] = parameters[5];
                        }
                        index++;
                    });
                    break;
                case 'removeProduct':
                    $scope.pcmData.forEach(function(product){
                        if(product.$$hashKey == parameters[0]) {
                            $scope.pcmData.splice($scope.pcmData.indexOf(product), 1);
                            $scope.pcmData.splice($scope.pcmDataRaw.indexOf(product), 1);
                        }
                    });
                    setGridHeight();
                    break;
                case 'addProduct':
                    $scope.pcmData.push(parameters);
                    $scope.pcmDataRaw.push(parameters);
                    $timeout(function(){ $scope.scrollToFocus($scope.pcmData.length-1, 1); }, 100);// Not working without a timeout
                    break;
                    setGridHeight();
                case 'removeFeature':
                    var featureName = parameters[0].name;
                    var index2 = 0;
                    $scope.pcmData.forEach(function () {
                        delete $scope.pcmData[index2][featureName];
                        delete $scope.pcmDataRaw[index2][featureName];
                        index2++;
                    });
                    $scope.gridOptions.columnDefs.splice(parameters[3], 1);
                    break;
                case 'renameFeature':
                    var oldFeatureName = parameters[0];
                    var featureName = parameters[1];
                    var index = parameters[2];
                    var codedFeatureName = convertStringToEditorFormat(featureName);
                    var codedOldFeatureName = convertStringToEditorFormat(oldFeatureName);
                    $scope.gridOptions.columnDefs.forEach(function(featureData) {
                        if(featureData.name === codedOldFeatureName) {
                            var index2 = 0;
                            $scope.pcmData.forEach(function (productData) {
                                productData[codedFeatureName] = productData[codedOldFeatureName];
                                $scope.pcmDataRaw[index2][codedFeatureName] = $scope.pcmDataRaw[index2][codedOldFeatureName];
                                if(codedFeatureName != codedOldFeatureName) {
                                    delete productData[codedOldFeatureName];
                                    delete $scope.pcmDataRaw[index][codedOldFeatureName];
                                }
                                index2++;
                            });
                            var colDef = $scope.newColumnDef(featureName, $scope.columnsType[codedOldFeatureName]);
                            $scope.gridOptions.columnDefs.splice(index, 1, colDef);
                        }
                    });
                    $scope.columnsType[codedFeatureName] = $scope.columnsType[codedOldFeatureName];
                    $scope.validation[codedFeatureName] = [];
                    if(featureName != oldFeatureName) {
                        delete $scope.columnsType[codedOldFeatureName];
                        delete $scope.validation[codedOldFeatureName];
                    }
                    break;
                case 'changeType':
                    var featureName = parameters[0];
                    var found = false;
                    for(var i = 0; i < $scope.gridOptions.columnDefs.length && !found; i++) {
                        if($scope.gridOptions.columnDefs[i].name == featureName) {
                            found = true;
                            $scope.gridOptions.columnDefs.splice(i, 1);
                            var colDef = newColumnDef(featureName, parameters[2]);
                            $timeout(function(){ $scope.gridOptions.columnDefs.splice(i-1, 0, colDef); }, 100);// Not working without a timeout
                        }
                    }
                    $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                    break;
                case 'addFeature':
                    var columnDef = $scope.newColumnDef(parameters[0], parameters[1]);
                    $scope.gridOptions.columnDefs.push(columnDef);
                    /* Initialize data */
                    var featureName = checkIfNameExists($scope.featureName, $scope.gridOptions.columnDefs);
                    $scope.pcmData.forEach(function (productData) {
                        productData[featureName] = "";
                    });
                    $scope.pcmDataRaw.forEach(function (productData) {
                        productData[featureName] = "";
                    });
                    $scope.columnsType[featureName] = parameters[1];
                    $scope.validation[featureName] = [];
                    $rootScope.$broadcast('modified');
            }
            $scope.commandsIndex++;
            $scope.canUndo = true;
            if($scope.commandsIndex >= $scope.commands.length){
                $scope.canRedo = false;
            }
        }
    };



});
