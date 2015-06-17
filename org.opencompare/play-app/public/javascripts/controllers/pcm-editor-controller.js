/**
 * Created by gbecan on 17/12/14.
 */

pcmApp.controller("PCMEditorController", function($rootScope, $scope, $http, $timeout, uiGridConstants, $compile, $modal) {

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

    //Export
    $scope.export_content = null;

    //rawContent
    pcmRaw = [];

    // Slider filter
    $scope.slider = {
        options: {
            range: true,
        }
    };
    $scope.filterSlider = [];

    $scope.gridOptions = {
        columnDefs: [],
        data: 'pcmData',
        enableRowSelection: false,
        enableRowHeaderSelection: false,
        enableColumnResizing: true,
        enableFiltering: true,
        enableCellSelection: false,
        enableCellEdit: false,
        headerRowHeight: 60,
        enableVerticalScrollbar: 'ALWAYS',
        rowHeight: 28
    };

    /* Move object in array */
    Array.prototype.move = function (old_index, new_index) {
        if (new_index >= this.length) {
            var k = new_index - this.length;
            while ((k--) + 1) {
                this.push(undefined);
            }
        }
        this.splice(new_index, 0, this.splice(old_index, 1)[0]);
        return this;
    };

    $scope.gridOptions.onRegisterApi = function(gridApi){
        var contentValue;
        var rawValue;
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.colMovable.on.columnPositionChanged($scope,function(colDef, originalPosition, newPosition){
            $scope.gridOptions.columnDefs.move(originalPosition, newPosition);
            var commandParameters = [originalPosition, newPosition];
            $scope.newCommand('move', commandParameters);
            $rootScope.$broadcast('modified');
        });
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef) {
            rawValue = $scope.pcmDataRaw[$scope.pcmData.indexOf(rowEntity)][colDef.name];
            contentValue = rowEntity[colDef.name];
            rowEntity[colDef.name] = rawValue;
        });
        gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){

            if(rawValue != newValue) {
                $rootScope.$broadcast('modified');
                $scope.pcmData[$scope.pcmData.indexOf(rowEntity)][colDef.name] = getVisualRepresentation(newValue, $scope.pcmData.indexOf(rowEntity),
                                                            colDef.name, rowEntity.$$hashKey, contentValue, rawValue, newValue);
            }
            else {
                $scope.pcmData[$scope.pcmData.indexOf(rowEntity)][colDef.name] = contentValue;
            }
            /* Update value based on visual representation and raw */
            $scope.pcmDataRaw[$scope.pcmData.indexOf(rowEntity)][colDef.name] = newValue;
        });
    };

    $scope.setEdit = function(bool) {

        $scope.gridOptions.columnDefs = [];
        $scope.edit = bool;
        $timeout(function(){ initializeEditor($scope.pcm, $scope.metadata)}, 100);
        $rootScope.$broadcast('setToolbarEdit', bool);
    };

    if (typeof id === 'undefined' && typeof data === 'undefined') {
        /* Create an empty PCM */
        $scope.pcm = factory.createPCM();
        $scope.setEdit(true);
        initializeEditor($scope.pcm, $scope.metadata);

    } else if (typeof data != 'undefined')  {
        /* Load PCM from import */
        $scope.pcm = loader.loadModelFromString(data).get(0);
        $scope.metadata = data.metadata;
        initializeEditor($scope.pcm, $scope.metadata);
    } else {
        /* Load a PCM from database */
        $scope.loading = true;
        $http.get("/api/get/" + id).
            success(function (data) {
            $scope.pcm = loader.loadModelFromString(JSON.stringify(data.pcm)).get(0);
            $scope.metadata = data.metadata;
            initializeEditor($scope.pcm, $scope.metadata);
            })
            .finally(function () {
                $scope.loading = false;
            })
    }
    if (typeof modal != 'undefined') {
        // Open the given modal
        $modal.open({
            templateUrl: modalTemplatePath,
            controller: modal + "Controller",
        })
    }

    /**
     * Get the visual representation of a raw data
     * @param cellValue
     * @returns {Array.<T>|string|Blob|ArrayBuffer|*}
     */
    function getVisualRepresentation(cellValue, index, colName, hashkey, oldValue, oldRawValue, newRawValue) {
        $http.post("/api/extract-content", {
            type: 'wikipedia',
            rawContent: cellValue,
            responseType: "text/plain",
            transformResponse: function(d, e) { // Needed to not interpret matrix as json (begin with '{|')
                return d;
            }
        }).success(function(data) {
                    var commandParameters = [];
                    $scope.pcmData[index][colName] = data;
                    if (colName != "Product") {
                        commandParameters = [hashkey, colName, oldValue, data, oldRawValue, newRawValue];
                    }
                    else {
                        commandParameters = [hashkey, 'name', oldValue, data, oldRawValue, newRawValue];
                    }
                    $scope.newCommand('edit', commandParameters);
        });

        return 'Loading value...';
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
            enableColumnResizing: true,
            enableColumnMoving: $scope.edit,
            enableCellEdit: $scope.edit,
            enableCellEditOnFocus: $scope.edit,
            allowCellFocus: $scope.edit,
            minWidth: 150,
            maxWidth: '*',
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
                var rowValue = $scope.pcmDataRaw[$scope.pcmData.indexOf(row.entity)];
                if($scope.validating && validation[col.name] && !validation[col.name][$scope.pcmData.indexOf(row.entity)]) {
                    return 'warningCell';
                }
                else {
                    return getCellClass(rowValue[col.name]);
                }
            },
            cellTooltip: function(row, col) {
                var rawValue = $scope.pcmDataRaw[$scope.pcmData.indexOf(row.entity)];
                var contentValue = $scope.pcmData[$scope.pcmData.indexOf(row.entity)];
                if($scope.validating && validation[col.name] && !validation[col.name][$scope.pcmData.indexOf(row.entity)]) {
                    return "This value doesn't seem to match the feature type.";
                }
                else if(rawValue && getCellTooltip(rawValue[col.name])){
                    return getCellTooltip(rawValue[col.name]);
                }
                else if(contentValue) {
                    return contentValue[col.name];
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
                            if(cellValue == columnsFilters[featureName][index] || isEmptyCell(cellValue)) {
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
                filterLess.condition  = function (searchTerm, cellValue) {
                    if(columnsFilters[featureName]) {
                        return (parseFloat(cellValue.replace(/\s/g, "").replace(",", ".")) >= columnDef.filters[0].term || isEmptyCell(cellValue));
                    }
                    else {
                        return true;
                    }
                };
                columnDef.filterHeaderTemplate="<div class='ui-grid-filter-container'><button class='btn btn-default btn-sm' ng-click='grid.appScope.showFilter(col)' data-toggle='modal' data-target='#modalSlider'><i class='fa fa-sliders'></i></button><button  ng-show='grid.appScope.isFilterOn(col)' class='btn btn-default btn-xs' ng-click='grid.appScope.removeFilter(col)'><i class='fa fa-close'></i></button></div>";
                var filterGreater = [];
                filterGreater.condition  = function (searchTerm, cellValue) {
                    if(columnsFilters[featureName]) {
                        return (parseFloat(cellValue.replace(/\s/g, "").replace(",", ".")) <= columnDef.filters[1].term || isEmptyCell(cellValue));
                    }
                    else {
                        return true;
                    }
                };
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
                        return getBooleanValue(cellValue) == "yes" || isEmptyCell(cellValue);
                    }
                    else if(columnsFilters[featureName] == 2) {
                        return getBooleanValue(cellValue) == "no" || isEmptyCell(cellValue);
                    }
                    else {
                        return true;
                    }
                };
                break;

        }
        return columnDef;
    }

    function getCellClass (value) {
        if(value) {
            if(value.toLowerCase().indexOf('{{yes') != -1) {
                return 'yesCell';
            }
            else if(value.toLowerCase().indexOf('{{no') != -1) {
                return 'noCell';
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    function getCellTooltip (value) {
        if(value) {
            if(value.toLowerCase().indexOf('<ref') != -1) {
                var index = value.toLowerCase().indexOf('<ref');
                var refPart = value.substring(index+11);
                var endIndex = refPart.replace(/\s/g, '').indexOf('"/>');
                return refPart.substring(0, endIndex);
            }
            else if(value.toLowerCase().indexOf('<ref>{{') != -1) {
                var index = value.toLowerCase().indexOf('<ref>{{');
                var refPart = value.substring(index+11);
                var endIndex = refPart.replace(/\s/g, '').indexOf('}}</ref>');
                return refPart.substring(0, endIndex);
            }
            else if(value.toLowerCase().indexOf('<ref>{{') != -1) {
                var index = value.toLowerCase().indexOf('<ref>{{');
                var refPart = value.substring(index+11);
                var endIndex = refPart.replace(/\s/g, '').indexOf('}}</ref>');
                return refPart.substring(0, endIndex);
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }

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
        if(!name.toLowerCase() || name.toLowerCase() == "" || name.toLowerCase() == "N/A" || name.toLowerCase() == "?" || name.toLowerCase() == "unknown") {
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
    function initializeEditor(pcm, metadata) {
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
        // Return rawcontent
        var productsRaw = pcm.products.array.map(function(product) {
            var productDataRaw = {};
            features.map(function(feature) {
                var cell = findCell(product, feature);
                //console.log(cell.rawContent);
                productDataRaw.name = product.name; // FIXME : may conflict with feature name
                if(cell.rawContent && cell.rawContent != "") {
                    productDataRaw[feature.name] = cell.rawContent;
                }
                else {
                    productDataRaw[feature.name] = cell.content;// TODO: replace content with rawcontent when implemented
                }
            });
            return productDataRaw;
        });
        $scope.pcmDataRaw = productsRaw;
        $scope.pcmData = products;
        /* Define columns */
        var columnDefs = [];

        /* Column for each feature */
        var colIndex = 0;
            pcm.features.array.forEach(function (feature) {
                var colDef = newColumnDef(feature.name, getType(feature.name));
                columnDefs.push(colDef);
                colIndex++;
            });
        if(metadata) {
            $scope.pcmData = sortProducts($scope.pcmData, metadata.productPositions);
            columnDefs = sortFeatures(columnDefs, metadata.featurePositions);
        }
        $scope.gridOptions.columnDefs = columnDefs;


        var toolsColumn = {
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
                enableColumnMoving: false
            };

        /* Second column for the products */
        var productsColumn = {
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
        };

        /* Specific filter for products */
        productsColumn.filter = [];
        productsColumn.filter.condition = function(searchTerm, cellValue) {
            return(cellValue.toLowerCase().indexOf(searchTerm.toLowerCase()) != -1)
        };
        productsColumn.filter.placeholder = 'Find';
        $scope.gridOptions.columnDefs.splice(0, 0, toolsColumn);
        $scope.gridOptions.columnDefs.splice(1, 0, productsColumn);
    }

    function sortProducts(products, position) {
        var sortedProducts = [];
        position.sort(function (a, b) {
            if(a.position == -1) {
                return 1;
            }
            else if(b.position == -1) {
                return -1;
            }
            else {
                return a.position - b.position;
            }
        });
        for(var i = 0; i < position.length; i++) {
            products.forEach(function (product) {
                if(position[i].product == product.name) {
                    sortedProducts.push(product);
                }
            });
        }
        return sortedProducts;
    }

    function sortFeatures(columns, position){
        var sortedColumns = [];
        position.sort(function (a, b) {
            if(a.position == -1) {
                return 1;
            }
            else if(b.position == -1) {
                return -1;
            }
            else {
                return a.position - b.position;
            }
        });
        for(var i = 0; i < position.length; i++) {
            columns.forEach(function (feature) {
                if(position[i].feature == feature.name) {
                    sortedColumns.push(feature);
                }
            });
        }
        return sortedColumns;
    }

    function convertGridToPCM(pcmData) {
        var pcm = factory.createPCM();
        pcm.name = $scope.pcm.name;

        var featuresMap = {};
        var index = 0;
        pcmData.forEach(function(productData) {
            // Create product
            var product = factory.createProduct();
            product.name = productData.name;
            pcm.addProducts(product);
            $scope.gridOptions.columnDefs.forEach(function (featureData) {
                if(productData.hasOwnProperty(featureData.name)  && featureData.name !== "$$hashKey"
                    && featureData.name !== "Product") {
                    // Create feature if not existing
                    if (!featuresMap.hasOwnProperty(featureData.name)) {
                        var feature = factory.createFeature();
                        feature.name = featureData.name;
                        pcm.addFeatures(feature);
                        featuresMap[featureData.name] = feature;
                    }
                    var feature = featuresMap[featureData.name];

                    // Create cell
                    var cell = factory.createCell();
                    cell.feature = feature;
                    cell.content = productData[featureData.name];
                    cell.rawContent = $scope.pcmDataRaw[index][featureData.name];
                    product.addCells(cell);
                }
            });
            index++;
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
    };

    $scope.removeProduct = function(row) {

        var index = $scope.pcmData.indexOf(row.entity);
        var rawData = $scope.pcmDataRaw[index];
        $scope.pcmData.splice(index, 1);
        $scope.pcmDataRaw.splice(index, 1);
        pcmRaw = [];
        $rootScope.$broadcast('modified');
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

    $scope.$watch('pcm.name', function() {
        if($scope.edit) {
            $rootScope.$broadcast('setPcmName', $scope.pcm.name);
        }
    });

    /**
     * Save PCM on the server
     */
    $scope.save = function() {

        $scope.pcm = convertGridToPCM($scope.pcmData);
        $scope.metadata = generateMetadata($scope.pcmData, $scope.gridOptions.columnDefs);
        var jsonModel = JSON.parse(serializer.serialize($scope.pcm));

        var pcmObject = {};
        pcmObject.metadata = $scope.metadata;
        pcmObject.pcm = jsonModel;
        if (typeof id === 'undefined') {
            $http.post("/api/create", pcmObject).success(function(data) {
                id = data;
                console.log("model created with id=" + id);
                $rootScope.$broadcast('saved');
            });
        } else {
            $http.post("/api/save/" + id, pcmObject).success(function(data) {
                console.log("model saved");
                $rootScope.$broadcast('saved');
            });
        }
    };

    function generateMetadata(product, columns) {
        var metadata = {};
        metadata.featurePositions = [];
        metadata.productPositions = [];
        var index = 0;
        product.forEach(function (product) {
            var object = {};
            object.product = product.name;
            object.position = index;
            metadata.productPositions.push(object);
            index++;
        });
        index = 0;
        columns.forEach(function (column) {
            var object = {};
            object.feature = column.name;
            object.position = index;
            metadata.featurePositions.push(object);
            index++;
        });
        return metadata;
    }

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
        var type = columnsType[feature.name];
        switch(type) {

            case 'string':

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
                break;

            case 'number':

                var minAndMax = findMinAndMax($scope.featureToFilter);
                $scope.slider.options.min = minAndMax[0];
                $scope.slider.options.max = minAndMax[1];
                $scope.filterSlider[0] = minAndMax[0];
                $scope.filterSlider[1] = minAndMax[1];
                if(minAndMax[1] < 10) {
                    $scope.slider.options.step = 0.1;
                }
                else if(minAndMax[1] > 1000) {
                    $scope.slider.options.step = 10;
                }
                else {
                    $scope.slider.options.step = 1;
                }
                break;

        }

        $elm = angular.element(html);
        angular.element(document.body).prepend($elm);

        $compile($elm)($scope);
    };

    function findMinAndMax(featureName) {
        var min = 0;
        var max = 0;
        $scope.pcmData.forEach(function (product) {
            if(parseInt(product[featureName]) > max) {
                max = parseFloat(product[featureName].replace(/\s/g, "").replace(",", "."));
            }
            if(parseInt(product[featureName]) < min) {
                min = parseFloat(product[featureName].replace(/\s/g, "").replace(",", "."));
            }
        });
        return [min, max];
    }

    $scope.closeFilter = function() {

        var featureName = $scope.featureToFilter;

        var type = columnsType[featureName];
        switch(type) {

            case 'string':
                var selec = $scope.gridApi2.selection.getSelectedRows();
                if (selec.length == 0) {
                    $scope.gridOptions2.data.forEach(function (productData) {
                        selec.push(productData);
                    });
                }
                $scope.colFilter = [];
                $scope.colFilter.listTerm = [];

                selec.forEach(function (product) {
                    $scope.colFilter.listTerm.push(product.product);
                });
                columnsFilters[featureName] = [];
                columnsFilters[featureName] = $scope.colFilter.listTerm;
                $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                break;

            case 'number':
                $scope.gridOptions.columnDefs.forEach(function (feature) {
                    if(feature.name == featureName) {
                        feature.filters[0].term = $scope.filterSlider[0];
                        feature.filters[1].term = $scope.filterSlider[1]+1;
                    }
                });
                columnsFilters[featureName] = [];
                break;
        }
        if ($elm) {
            $elm.remove();
        }
    };

    $scope.removeFilter = function(col) {

        var featureName = col.name;
        var type = columnsType[featureName];
        switch(type) {
            case 'string':
                delete columnsFilters[featureName];
                break;
            case 'number':
                $scope.gridOptions.columnDefs.forEach(function (feature) {
                    if(feature.name == featureName) {
                        delete feature.filters[0].term;
                        delete feature.filters[1].term;
                    }
                });
                delete columnsFilters[featureName];
                break;
        }
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

    $scope.$on('import', function(event, args) {
        $scope.pcm = loader.loadModelFromString(JSON.stringify(args.pcm)).get(0);
        $scope.metadata = args.metadata;
        initializeEditor($scope.pcm, $scope.metadata);
    });

    $scope.$on('setGridEdit', function(event, args) {
        $scope.setEdit(args);
    });

    $scope.$on('export', function (event, args) {
        $scope.export_loading = true;
        $scope.pcm = convertGridToPCM($scope.pcmData);
        $scope.export_content = "";
        $http.post(
            "/api/export/" + args,
            {
                file: serializer.serialize($scope.pcm),
                title: $scope.pcm.title,
                productAsLines: true,
                separator: ',',
                quote: '"'
            }, {
                responseType: "text/plain",
                transformResponse: function(d, e) { // Needed to not interpret matrix as json (begin with '{|')
                    return d;
                }
            })
            .success(function(response, status, headers, config) {
                $scope.export_content = response;
            }).error(function(data, status, headers, config) {
                console.log(data)
            });
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
