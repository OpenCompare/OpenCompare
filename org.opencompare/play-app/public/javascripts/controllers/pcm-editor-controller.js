/**
 * Created by gbecan on 17/12/14.
 */

pcmApp.controller("PCMEditorController", function($rootScope, $scope, $http, $timeout, uiGridConstants, $compile) {

    // Load PCM
    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();

    // Validate pcm type
    var columnsType = [];
    var validation = [];
    $scope.validating = false;

    //Custom filters
    var $elm;
    var columnsFilters = [];
    $scope.loading = false;
    $scope.featureType = 'string';

    //Undo-redo
    $scope.commands = [];
    $scope.commandsIndex = 0;
    $scope.canUndo = false;
    $scope.canRedo = false;

    //Edit mode
    $scope.edit = false;
    var pcmRaw = [];

    $scope.gridOptions = {
        columnDefs: [],
        data: 'pcmData',
        enableRowSelection: false,
        enableRowHeaderSelection: false,
        enableColumnResizing: false,
        enableFiltering: true,
        enableCellSelection: false,
        rowHeight: 28
    };

    $scope.gridOptions.onRegisterApi = function(gridApi){
        var contentValue;
        var rawValue;
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef) {
            if(pcmRaw[rowEntity.$$hashKey]) {
                rawValue = pcmRaw[rowEntity.$$hashKey][colDef.name];
                contentValue = rowEntity[colDef.name];
                rowEntity[colDef.name] = rawValue;
            }
            else {
                pcmRaw = [];
                for(var i = 0; i < $scope.pcmData.length; i++) {
                    pcmRaw[$scope.pcmData[i].$$hashKey] = $scope.pcmDataRaw[i];
                }
                if(pcmRaw[rowEntity.$$hashKey]) {
                    rawValue = pcmRaw[rowEntity.$$hashKey][colDef.name];
                    contentValue = rowEntity[colDef.name];
                    rowEntity[colDef.name] = rawValue;
                }

            }
        });
        gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
            if(rawValue != newValue) {
                var commandParameters = [];
                if (colDef.name != "Product") {
                    commandParameters = [rowEntity.$$hashKey, colDef.name, contentValue, getVisualRepresentation(newValue), rawValue, newValue];
                }
                else {
                    commandParameters = [rowEntity.$$hashKey, 'name', contentValue, getVisualRepresentation(newValue), rawValue, newValue];
                }
                console.log(commandParameters);
                $scope.newCommand('edit', commandParameters);
                $rootScope.$broadcast('modified');
                pcmRaw = [];
            }
            /* Update value based on visual representation and raw */
            $scope.pcmData[$scope.pcmData.indexOf(rowEntity)][colDef.name] = getVisualRepresentation(newValue);
            $scope.pcmDataRaw[$scope.pcmData.indexOf(rowEntity)][colDef.name] = newValue;
        });
    };

    if (typeof id === 'undefined' && typeof data === 'undefined') {
        /* Create an empty PCM */
        $scope.pcm = factory.createPCM();
        initializeEditor($scope.pcm)

    } else if (typeof data != 'undefined')  {
        /* Load PCM from import */
        $scope.pcm = loader.loadModelFromString(data).get(0);
        initializeEditor($scope.pcm)

    } else {
        /* Load a PCM from database */
        $scope.loading = true;
        $http.get("/api/get/" + id).
            success(function (data) {
            $scope.pcm = loader.loadModelFromString(JSON.stringify(data)).get(0);
            initializeEditor($scope.pcm)
            })
            .finally(function () {
                $scope.loading = false;
            })
    }

    /**
     * Get the visual representation of a raw data
     * @param cellValue
     * @returns {Array.<T>|string|Blob|ArrayBuffer|*}
     */
    function getVisualRepresentation(cellValue) {
        /*$http.get("/api/visualRepresentation/" + cellValue).success(function(data) {
            window.location.href = "/";
        });*/
        var newValue = cellValue.slice(2, -2);
        return newValue;
    }

    /**
     *  Create a new ColumnDef for the ui-grid
     * @param featureName
     * @param featureType
     * @returns colDef
     */
    function newColumnDef(featureName, featureType) {
        if(!featureType) {
            featureType = "string";
        }
        columnsType[featureName] = featureType;
        var columnDef = {
            name: featureName,
            enableSorting: true,
            enableHiding: false,
            enableFiltering: true,
            enableCellEdit: $scope.edit,
            enableCellEditOnFocus: $scope.edit,
            allowCellFocus: $scope.edit,
            minWidth: 150,
            filter: {term: ''},
            menuItems: [
                {
                    title: 'Hide/Unhide',
                    icon: 'fa fa-eye',
                    action: function($event) {
                        $scope.gridOptions.columnDefs.forEach(function(featureData) {
                            if(featureData.name === featureName) {
                                if(featureData.maxWidth == '20') {
                                    featureData.maxWidth = '*';
                                    featureData.minWidth = '150';
                                    featureData.displayName = featureData.name;
                                    featureData.enableFiltering = true;
                                    featureData.cellClass = function() {
                                        return 'showCell';
                                    };
                                    $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                                }
                                else {
                                    featureData.maxWidth = '20';
                                    featureData.minWidth = '20';
                                    featureData.displayName = "";
                                    featureData.enableFiltering = false;
                                    featureData.cellClass = function() {
                                        return 'hideCell';
                                    };
                                    $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                                }
                            }
                        });
                    }
                },
                {
                    title: 'Rename Feature',
                    shown: function () {
                        return $scope.edit;
                    },
                    icon: 'fa fa-pencil',
                    action: function($event) {
                        $('#modalRenameFeature').modal('show');
                        $scope.oldFeatureName = featureName;
                        $scope.featureName = featureName;
                    }
                },
                {
                    title: 'Change Type',
                    shown: function () {
                        return $scope.edit;
                    },
                    icon: 'fa fa-exchange',
                    action: function($event) {
                        $('#modalChangeType').modal('show');
                        $scope.oldFeatureName = featureName;
                        $scope.featureName = featureName;
                        $scope.featureType = columnsType[featureName];
                    }
                },
                {
                    title: 'Delete Feature',
                    shown: function () {
                        return $scope.edit;
                    },
                    icon: 'fa fa-trash-o',
                    action: function($event) {
                        $scope.deleteFeature(featureName);
                    }
                }
            ],
            cellClass: function(grid, row, col) {
                if(validation[col.name] && !validation[col.name][$scope.pcmData.indexOf(row.entity)] && $scope.validating) {
                    return 'warningCell';
                }
            },
            cellTooltip: function(row, col) {
                    if(validation[col.name] && !validation[col.name][$scope.pcmData.indexOf(row.entity)]) {
                        return "This value doesn't seem to match the feature type.";
                    }
                }
        };
        switch(featureType) {
            case "string":
                columnDef.filterHeaderTemplate="<div class='ui-grid-filter-container'><button class='btn btn-default btn-sm' ng-click='grid.appScope.showFilter(col)'><i class='fa fa-search'></i></button><button ng-show='grid.appScope.isFilterOn(col)' class='btn btn-default btn-xs' ng-click='grid.appScope.removeFilter(col)'><i class='fa fa-close'></i></button></div>";
                columnDef.filter.noTerm = true;
                columnDef.filter.condition = function (searchTerm, cellValue) {
                    if(columnsFilters[featureName]) {
                        var inFilter = false;
                        var index = 0;
                        while(!inFilter && index < columnsFilters[featureName].length) {
                            if(cellValue == columnsFilters[featureName][index]) {
                                inFilter = true;
                            }
                            index++;
                        }
                        return inFilter;
                    }
                    else {
                        return true;
                    }
                };
                break;
            case "number":
                var filterLess = [];
                filterLess.condition = uiGridConstants.filter.LESS_THAN;
                filterLess.placeholder= '<';
                var filterGreater = [];
                filterGreater.condition = uiGridConstants.filter.GREATER_THAN;
                filterGreater.placeholder= '> or =';
                columnDef.filters = [];
                columnDef.filters.push(filterGreater);
                columnDef.filters.push(filterLess);
                break;
            case "boolean":
                var filterName = 'filter'+featureName.replace(/[&-/\s]/gi, '');
                columnDef.filterHeaderTemplate="<div class='ui-grid-filter-container'><span class='filterLabel'>Yes&nbsp;</span><input type='checkbox' ng-change='grid.appScope.applyBooleanFilter(col, "+filterName+")' ng-model='"+filterName+"'  ng-true-value='1' ng-false-value='0'>&nbsp; &nbsp; <span class='filterLabel'>No&nbsp;</span><input type='checkbox' ng-change='grid.appScope.applyBooleanFilter(col, "+filterName+")' ng-model='"+filterName+"'  ng-true-value='2' ng-false-value='0'></div>";
                columnDef.filter.noTerm = true;
                columnDef.filter.condition = function (searchTerm, cellValue) {
                    if(columnsFilters[featureName] == 1) {
                        return getBooleanValue(cellValue) == "yes";
                    }
                    else if(columnsFilters[featureName] == 2) {
                        return getBooleanValue(cellValue) == "no";
                    }
                    else {
                        return true;
                    }
                };
                break;

        }
        return columnDef;
    }

    /**
     * Return the type of a column
     * @param featureName
     * @returns {string}
     */
    function getType (featureName) {
        var rowIndex = 0;
        var isInt = 0;
        var isBool = 0;
        var isString = 0;

        while($scope.pcmData[rowIndex]) {
            if($scope.pcmData[rowIndex][featureName]) {
                if (!angular.equals(parseInt($scope.pcmData[rowIndex][featureName]), NaN)) {
                    isInt++;
                }
                else if (isBooleanValue($scope.pcmData[rowIndex][featureName])) {
                    isBool++;
                }
                else if (!isEmptyCell($scope.pcmData[rowIndex][featureName])) {
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
    }

    function getBooleanValue(name){
        if(name.toLowerCase() === "yes" || name.toLowerCase() === "true") {
            return "yes";
        }
        else  if(name.toLowerCase() === "no" || name.toLowerCase() === "false") {
            return "no";
        }
        else {
            return "unknown";
        }
    }

    function isEmptyCell(name) {
        if(!name || name == "" || name == "N/A" || name == "?") {
            return true;
        }
        else {
            return false;
        }
    }

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
                    validation[featureData.name] = [];
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
                        validation[featureName][index] =  $scope.validateType(productData[featureName], columnsType[featureName]);
                    }
                }
                index++;
            });
        }
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
        $rootScope.$broadcast("validating");
    };

    /**
     * Initialize the editor
     * @param pcm
     */
    function initializeEditor(pcm) {
        $rootScope.$broadcast('setPcmName', $scope.pcm.name);
        /* Convert PCM model to editor format */
        var features = getConcreteFeatures(pcm);
        var products = pcm.products.array.map(function(product) {
            var productData = {};
            features.map(function(feature) {
                var cell = findCell(product, feature);
                productData.name = product.name; // FIXME : may conflict with feature name
                productData[feature.name] = cell.content;
            });
            return productData;
        });
        // Return data rawcontent
        var productsRaw = pcm.products.array.map(function(product) {
            var productDataRaw = {};
            features.map(function(feature) {
                var cell = findCell(product, feature);
                productDataRaw.name = product.name; // FIXME : may conflict with feature name
                productDataRaw[feature.name] = '{{'+cell.content+'}}';// TODO: replace content with rawcontent when implemented
            });
            return productDataRaw;
        });
        $scope.pcmDataRaw = productsRaw;
        $scope.pcmData = products;
        /* Define columns */
        var columnDefs = [];
            /* First column is for the remove button */
            columnDefs.push({
                name: ' ',
                cellTemplate: '<div class="buttonsCell" ng-show="grid.appScope.edit">' +
                '<button role="button" ng-click="grid.appScope.removeProduct(row)"><i class="fa fa-times"></i></button>'+
                '</div>',
                enableCellEdit: false,
                enableFiltering: false,
                enableSorting: false,
                enableHiding: false,
                width: 30,
                enableColumnMenu: false,
                allowCellFocus: false,
                enableColumnMoving: false,
                EXCESS_ROWS: 10
            });

        /* Second column for the products */
        columnDefs.push({
            name: 'Product',
            field: "name",
            cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                return 'productCell';
            },
            enableSorting: true,
            enableHiding: false,
            enableColumnMoving: false,
            enableCellEdit: $scope.edit,
            enableCellEditOnFocus: $scope.edit,
            allowCellFocus: $scope.edit,
            minWidth: 150
        });

        /* Specific filter for products */
        columnDefs[1].filter = [];
        columnDefs[1].filter.condition = function(searchTerm, cellValue) {
            return(cellValue.toLowerCase().indexOf(searchTerm.toLowerCase()) != -1)
        };
        columnDefs[1].filter.placeholder = 'Find';

        /* Column for each feature */
        var colIndex = 0;
            pcm.features.array.forEach(function (feature) {
                var colDef = newColumnDef(feature.name, getType(feature.name));
                columnDefs.push(colDef);
                colIndex++;
            });
        $scope.gridOptions.columnDefs = columnDefs;

        /* Setting the grid size */
        if($scope.gridApi) {
            $scope.gridApi.grid.gridHeight = $(window).height()/3*2;
        }
    }

    function convertGridToPCM(pcmData) {
        var pcm = factory.createPCM();
        pcm.name = $scope.pcm.name;

        var featuresMap = {};

        pcmData.forEach(function(productData) {
            // Create product
            var product = factory.createProduct();
            product.name = productData.name;
            pcm.addProducts(product);

            // Create cells
            for (var featureData in productData) { // FIXME : order is not preserved
                if (productData.hasOwnProperty(featureData)
                    && featureData !== "$$hashKey"
                    && featureData !== "name") { // FIXME : not really good for now... it can conflict with feature names

                    // Create feature if not existing
                    if (!featuresMap.hasOwnProperty(featureData)) {
                        var feature = factory.createFeature();
                        feature.name = featureData;
                        pcm.addFeatures(feature);
                        featuresMap[featureData] = feature;
                    }
                    var feature = featuresMap[featureData]

                    // Create cell
                    var cell = factory.createCell();
                    cell.feature = feature;
                    cell.content = productData[featureData];
                    product.addCells(cell);
                }
            }
        });
        return pcm;
    }

    $scope.addFeature = function() {

        if(!$scope.featureType) {
            $scope.featureType = "string";
        }

        /* Initialize data */
        var featureName = $scope.checkIfNameExists($scope.featureName);
        $scope.pcmData.forEach(function (productData) {
            productData[featureName] = "";
        });
        $scope.pcmDataRaw.forEach(function (productData) {
            productData[featureName] = "";
        });

        /* Define the new column*/
        var columnDef = newColumnDef(featureName, $scope.featureType);
        $scope.gridOptions.columnDefs.push(columnDef);
        columnsType[featureName] = $scope.featureType;
        validation[featureName] = [];

        /* Command for undo/redo */
        var parameters =  [featureName, $scope.featureType, $scope.gridOptions.columnDefs.length-1];
        $scope.newCommand('addFeature', parameters);

        /* Modified for save */
        $rootScope.$broadcast('modified');
    };

    $scope.renameFeature = function() {

        var featureName = $scope.checkIfNameExists($scope.featureName);
        var index = 0;
        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            if(featureData.name === $scope.oldFeatureName) {
                if($scope.oldFeatureName === $scope.featureName){
                    featureName = $scope.oldFeatureName;
                }
                var index2 = 0;
                $scope.pcmData.forEach(function (productData) {
                    productData[featureName] = productData[$scope.oldFeatureName];
                    $scope.pcmDataRaw[index2][featureName] = $scope.pcmDataRaw[index2][$scope.oldFeatureName];
                    if($scope.featureName != $scope.oldFeatureName) {
                        delete productData[$scope.oldFeatureName];
                        delete $scope.pcmDataRaw[index2][$scope.oldFeatureName]
                    }
                    index2++;
                });
                var colDef = newColumnDef(featureName, columnsType[$scope.oldFeatureName]);
                $scope.gridOptions.columnDefs.splice(index, 1, colDef);

                /* Command for undo/redo */
                var parameters = [$scope.oldFeatureName, featureName, index];
                $scope.newCommand('renameFeature', parameters);
            }
            index++;
        });
        columnsType[featureName] = columnsType[$scope.oldFeatureName];
        validation[featureName] = [];
        if($scope.featureName != $scope.oldFeatureName) {
            delete columnsType[$scope.oldFeatureName];
            delete validation[$scope.oldFeatureName];
        }
        /* re-init of scope parameters */
        $scope.featureName = "";
        $scope.oldFeatureName = "";

        /* Modified for save */
        $rootScope.$broadcast('modified');
    };

    $scope.changeType = function () {

        var featureName = $scope.featureName;
        var found = false;
        for(var i = 0; i < $scope.gridOptions.columnDefs.length && !found; i++) {
            if($scope.gridOptions.columnDefs[i].name == featureName) {
                var oldType = columnsType[featureName];
                found = true;
                $scope.gridOptions.columnDefs.splice(i, 1);
                var colDef = newColumnDef(featureName, $scope.featureType);
                $timeout(function(){ $scope.gridOptions.columnDefs.splice(i-1, 0, colDef); }, 100);// Not working without a timeout
                var parameters = [featureName, oldType, $scope.featureType];
                $scope.newCommand('changeType', parameters);
                columnsType[featureName] = $scope.featureType;
            }
        }
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    };

    $scope.checkIfNameExists = function(name) {

        var newName = "";
        if(!name) {
            newName = "New Feature";
        }
        else {
            newName = name;
        }
        var index = 0;
        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            var featureDataWithoutNumbers = featureData.name.replace(/[0-9]/g, '');
            if(featureDataWithoutNumbers === newName ){
                index++;
            }
        });
        if(index != 0) {
            newName = newName + index;
        }
        return newName;
    };

    $scope.validateType = function (productName, featureType) {

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
    };

    function isBooleanValue(productName) {

        return((productName.toLowerCase() === "yes") ||  (productName.toLowerCase() === "true") ||  (productName.toLowerCase() === "no") ||  (productName.toLowerCase() === "false"));
    }

    $scope.deleteFeature = function(featureName) {

        delete validation[featureName];
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
    };

    /**
     * Add a new product and focus on this new
     * @param row
     */
    $scope.addProduct = function() {

        var productData = {};
        productData.name = "";

        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            if(featureData.name == " " || featureData.name == "Product") { // There must be a better way but working for now
                delete productData[featureData.name];
            }
            else{
                productData[featureData.name] = "";
            }
        });
        $scope.pcmData.push(productData);
        $scope.pcmDataRaw.push(productData);
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
        $timeout(function(){ $scope.scrollToFocus($scope.pcmData.length-1, 1); }, 100);// Not working without a timeout
        console.log("Product added");
        $rootScope.$broadcast('modified');
        var parameters = $scope.pcmData[$scope.pcmData.length-1];
        $scope.newCommand('addProduct', parameters);
    };

    $scope.removeProduct = function(row) {

        var index = $scope.pcmData.indexOf(row.entity);
        $scope.pcmData.splice(index, 1);
        $scope.pcmDataRaw.splice(index, 1);
        pcmRaw = [];
        $rootScope.$broadcast('modified');
        var rawData = $scope.pcmDataRaw[index];
        var parameters = [row.entity.$$hashKey, row.entity, rawData, index];
        $scope.newCommand('removeProduct', parameters);
    };


    $scope.scrollToFocus = function( rowIndex, colIndex ) {

        $scope.gridApi.cellNav.scrollToFocus( $scope.pcmData[rowIndex], $scope.gridOptions.columnDefs[colIndex]);
    };

    $scope.newCommand = function(type, parameters){

        var command = [];
        command.push(type);
        command.push(parameters);
        $scope.commands[$scope.commandsIndex] = command;
        $scope.commandsIndex++;
        $scope.canUndo = true;
    };

    $scope.undo = function() {

        if($scope.commandsIndex > 0) {
            $scope.commandsIndex--;
            $scope.canRedo = true;
            var command = $scope.commands[$scope.commandsIndex];
            var parameters = command[1];
            switch(command[0]) {
                case 'edit':
                    var index = 0;
                    $scope.pcmData.forEach(function(product){
                        if(product.$$hashKey == parameters[0]) {
                            product[parameters[1]] = parameters[2];
                            $scope.pcmDataRaw[index][parameters[1]] =  parameters[4];
                        }
                        index++;
                    });
                    pcmRaw = [];
                    break;
                case 'removeProduct':
                    $scope.pcmData.splice(parameters[3], 0, parameters[1]);
                    $scope.pcmDataRaw.splice(parameters[3], 0, parameters[2]);
                    pcmRaw = [];
                    $timeout(function(){ $scope.scrollToFocus(parameters[2], 1); }, 100);// Not working without a timeout
                    break;
                case 'addProduct':
                    $scope.pcmData.forEach(function(product){
                        if(product.$$hashKey == parameters.$$hashKey) {
                            $scope.pcmData.splice($scope.pcmData.indexOf(product), 1);
                            $scope.pcmData.splice($scope.pcmDataRaw.indexOf(product), 1);
                        }
                    });
                    pcmRaw = [];
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
                    var index = parameters[2];
                    $scope.gridOptions.columnDefs.forEach(function(featureData) {
                        if(featureData.name === featureName) {
                            var index2 = 0;
                            $scope.pcmData.forEach(function (productData) {
                                productData[oldFeatureName] = productData[featureName];
                                $scope.pcmDataRaw[index2][oldFeatureName] = $scope.pcmDataRaw[index2][featureName];
                                if(featureName != oldFeatureName) {
                                    delete productData[featureName];
                                    delete $scope.pcmDataRaw[index2][featureName];
                                }
                                index2++;
                            });
                            var colDef = newColumnDef(oldFeatureName, columnsType[featureName]);
                            $scope.gridOptions.columnDefs.splice(index, 1, colDef);
                        }
                    });
                    columnsType[oldFeatureName] = columnsType[featureName];
                    validation[featureName] = [];
                    if(featureName != oldFeatureName) {
                        delete columnsType[featureName];
                        delete validation[featureName];
                    }
                    break;
                case 'changeType':
                    var featureName = parameters[0];
                    var found = false;
                    for(var i = 0; i < $scope.gridOptions.columnDefs.length && !found; i++) {
                        if($scope.gridOptions.columnDefs[i].name == featureName) {
                            found = true;
                            $scope.gridOptions.columnDefs.splice(i, 1);
                            var colDef = newColumnDef(featureName, parameters[1]);
                            $timeout(function(){ $scope.gridOptions.columnDefs.splice(i-1, 0, colDef); }, 100);// Not working without a timeout
                        }
                    }
                    $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                    break;
                case 'addFeature':
                    $scope.gridOptions.columnDefs.splice(parameters[2], 1);
                    /* Initialize data */
                    var featureName = $scope.checkIfNameExists($scope.featureName);
                    $scope.pcmData.forEach(function (productData) {
                        delete productData[parameters[0]];
                    });
                    $scope.pcmDataRaw.forEach(function (productData) {
                        delete productData[parameters[0]];
                    });

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
                    break;
                case 'addProduct':
                    $scope.pcmData.push(parameters);
                    $scope.pcmDataRaw.push(parameters);
                    $timeout(function(){ $scope.scrollToFocus($scope.pcmData.length-1, 1); }, 100);// Not working without a timeout
                    break;
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
                    $scope.gridOptions.columnDefs.forEach(function(featureData) {
                        if(featureData.name === oldFeatureName) {
                            var index2 = 0;
                            $scope.pcmData.forEach(function (productData) {
                                productData[featureName] = productData[oldFeatureName];
                                $scope.pcmDataRaw[index2][featureName] = $scope.pcmDataRaw[index2][oldFeatureName];
                                if(featureName != oldFeatureName) {
                                    delete productData[oldFeatureName];
                                    delete $scope.pcmDataRaw[index][oldFeatureName];
                                }
                                index2++;
                            });
                            var colDef = newColumnDef(featureName, columnsType[oldFeatureName]);
                            $scope.gridOptions.columnDefs.splice(index, 1, colDef);
                        }
                    });
                    columnsType[featureName] = columnsType[oldFeatureName];
                    validation[featureName] = [];
                    if(featureName != oldFeatureName) {
                        delete columnsType[oldFeatureName];
                        delete validation[oldFeatureName];
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
                    var columnDef = newColumnDef(parameters[0], parameters[1]);
                    $scope.gridOptions.columnDefs.push(columnDef);
                    /* Initialize data */
                    var featureName = $scope.checkIfNameExists($scope.featureName);
                    $scope.pcmData.forEach(function (productData) {
                        productData[featureName] = "";
                    });
                    $scope.pcmDataRaw.forEach(function (productData) {
                        productData[featureName] = "";
                    });
                    columnsType[featureName] = parameters[1];
                    validation[featureName] = [];
                    $rootScope.$broadcast('modified');
            }
            $scope.commandsIndex++;
            $scope.canUndo = true;
            if($scope.commandsIndex >= $scope.commands.length){
                $scope.canRedo = false;
            }
        }
    };

    /**
     * Save PCM on the server
     */
    $scope.save = function() {

        $scope.pcm = convertGridToPCM($scope.pcmData)
        var jsonModel = serializer.serialize($scope.pcm);

        if (typeof id === 'undefined') {
            $http.post("/api/create", JSON.parse(jsonModel)).success(function(data) {
                id = data;
                console.log("model created with id=" + id);
                $rootScope.$broadcast('saved');
            });
        } else {
            $http.post("/api/save/" + id, JSON.parse(jsonModel)).success(function(data) {
                console.log("model saved");
                $rootScope.$broadcast('saved');
            });
        }
    };

    /**
     * Remove PCM from server
     */
    $scope.remove = function() {

        if (typeof id !== 'undefined') {
            $http.get("/api/remove/" + id).success(function(data) {
                window.location.href = "/";
                console.log("model removed");
            });
        }
    };

    $scope.setEdit = function(bool) {

        $scope.gridOptions.columnDefs = [];
        $scope.edit = bool;
        $timeout(function(){ initializeEditor($scope.pcm)}, 100);
        $rootScope.$broadcast('setToolbarEdit', bool);
    };

    /**
     * Cancel edition
     */
    $scope.cancel = function() {

        window.location = "/view/" + id;
    };

    $scope.isFiltered = (function () {

        return $scope.ListToFilter.indexOf(productData[feature.name]) != -1;
    });

    $scope.showFilter = function(feature) {

        $scope.featureToFilter = feature.name;
        $scope.ListToFilter = [];
        $scope.pcmData.forEach( function ( productData ) {
            if ($scope.ListToFilter.indexOf(productData[feature.name] ) === -1 ) {
                $scope.ListToFilter.push(productData[feature.name]);
            }
        });
        $scope.ListToFilter.sort();
        $scope.gridOptions2 = {
            data: [],
            enableColumnMenus: false,
            onRegisterApi: function( gridApi) {
                $scope.gridApi2 = gridApi;
                if (columnsFilters[feature.name]){
                    $timeout(function() {
                        columnsFilters[feature.name].forEach( function( product ) {
                            var entities = $scope.gridOptions2.data.filter( function( row ) {
                                return row.product === product;
                            });
                            if( entities.length > 0 ) {
                                $scope.gridApi2.selection.selectRow(entities[0]);
                            }
                        });
                    });
                }
            }
        };
        $timeout(function() {
            $scope.ListToFilter.forEach(function (product) {
                $scope.gridOptions2.data.push({product: product});
            });
        }, 100);

        var html = '' +
            '<div class="modal" id="modalCustomFilter" ng-style="{display: \'block\'}">' +
                '<div class="modal-dialog">' +
                    '<div class="modal-content">' +
                        '<div class="modal-header">' +
            'Filter' +
            '</div>' +
                        '<div class="modal-body">' +
                            '<div id="grid2" ui-grid="gridOptions2" ui-grid-selection class="modalGrid"></div>' +
                        '</div>' +
                    '<div class="modal-footer">' +
                        '<button type="button" class="btn btn-primary" ng-click="closeFilter()">Filter</button>' +
                    '</div>' +
                '</div>' +
            '   </div>' +
            '</div>';
        $elm = angular.element(html);
        angular.element(document.body).prepend($elm);

        $compile($elm)($scope);
    };

    $scope.closeFilter = function() {

        var featureName = $scope.featureToFilter;
        var selec = $scope.gridApi2.selection.getSelectedRows();
        if(selec.length == 0) {
            $scope.gridOptions2.data.forEach(function (productData){
                selec.push(productData);
            });
        }
        $scope.colFilter = [];
        $scope.colFilter.listTerm = [];

        selec.forEach( function( product ) {
            $scope.colFilter.listTerm.push( product.product );
        });
        columnsFilters[featureName] = [];
        columnsFilters[featureName] = $scope.colFilter.listTerm;
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
        if ($elm) {
            $elm.remove();
        }
    };

    $scope.removeFilter = function(col) {

        var featureName = col.name;
        delete columnsFilters[featureName];
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    };

    $scope.applyBooleanFilter = function(col, value){

        columnsFilters[col.name] = value;
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    };

    $scope.isFilterOn = function(col) {

        return columnsFilters[col.name];
    };

    // Bind events from toolbar to functions of the editor

    $scope.$on('save', function(event, args) {
        $scope.save();
    });

    $scope.$on('remove', function(event, args) {
        $scope.remove();
    });

    $scope.$on('cancel', function(event, args) {
        $scope.cancel();
    });

    $scope.$on('validate', function(event, args) {
        $scope.validate();
    });

    $scope.$on('setGridEdit', function(event, args) {
        $scope.setEdit(args);
    });

})

    .directive('embeddedEditor', function() {
        return {
            templateUrl: 'pcm-editor.html'
        };
    })

    .directive('openCompareEditor', function() {
        return {
            templateUrl: '/assets/editor/pcm-editor.html'
        };
    });
