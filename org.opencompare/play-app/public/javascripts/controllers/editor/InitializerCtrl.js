/**
 * Created by hvallee on 6/19/15.
 */

pcmApp.controller("InitializerCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, $compile, $modal) {

    $scope.height = 300;

    $scope.gridOptions = {
        columnDefs: [],
        data: 'pcmData',
        enableRowSelection: false,
        enableRowHeaderSelection: false,
        flatEntityAccess: true,
        enableColumnResizing: true,
        enableFiltering: true,
        enableCellSelection: false,
        enableCellEdit: false,
        headerRowHeight: 60,
        enableVerticalScrollbar: 'ALWAYS',
        rowHeight: 28
    };


    $scope.loading = false;

    $scope.gridOptions.onRegisterApi = function(gridApi){

        var contentValue;
        var rawValue;
        //set gridApi on scope
        $scope.gridApi = gridApi;

        /* Called when columns arem oved */
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

    /**
     *  Create a new ColumnDef for the ui-grid
     * @param featureName
     * @param featureType
     * @returns colDef
     */
    $scope.newColumnDef = function(featureName, featureType) {
        if(!featureType) {
            featureType = "string";
        }
        var codedFeatureName = convertStringToEditorFormat(featureName);
        $scope.columnsType[codedFeatureName] = featureType;
        var columnDef = {
            name: codedFeatureName,
            displayName: featureName,
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
                            if(featureData.name === codedFeatureName) {
                                if(featureData.maxWidth == '20') {
                                    featureData.maxWidth = '*';
                                    featureData.minWidth = '150';
                                    featureData.displayName = convertStringToPCMFormat(featureData.name);
                                    featureData.enableFiltering = true;
                                    featureData.cellClass = function() {
                                        return 'showCell';
                                    };
                                }
                                else {
                                    featureData.maxWidth = '20';
                                    featureData.minWidth = '20';
                                    featureData.displayName = "";
                                    featureData.enableFiltering = false;
                                    featureData.cellClass = function() {
                                        return 'hideCell';
                                    };
                                }
                            }
                        });
                        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
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
                        $scope.featureType = $scope.columnsType[codedFeatureName];
                    }
                },
                {
                    title: 'Delete Feature',
                    shown: function () {
                        return $scope.edit;
                    },
                    icon: 'fa fa-trash-o',
                    action: function($event) {
                        $scope.deleteFeature(codedFeatureName);
                    }
                }
            ],
            cellClass: function(grid, row, col) {
                var rowValue = $scope.pcmDataRaw[$scope.pcmData.indexOf(row.entity)];
                if($scope.validating && $scope.validation[col.name] && !$scope.validation[col.name][$scope.pcmData.indexOf(row.entity)]) {
                    return 'warningCell';
                }
                else if(rowValue) {
                    return getCellClass(rowValue[col.name]);
                }
            },
            cellTooltip: function(row, col) {
                var rawValue = $scope.pcmDataRaw[$scope.pcmData.indexOf(row.entity)];
                var contentValue = $scope.pcmData[$scope.pcmData.indexOf(row.entity)];
                if($scope.validating && $scope.validation[col.name] && !$scope.validation[col.name][$scope.pcmData.indexOf(row.entity)]) {
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
                columnDef.filterHeaderTemplate="" +
                    "<div class='ui-grid-filter-container'>" +
                    "   <button class='btn btn-default btn-sm' ng-click='grid.appScope.showFilter(col)'>" +
                    "       <i class='fa fa-search'></i>" +
                    "   </button>" +
                    "   <button ng-show='grid.appScope.isFilterOn(col)' class='btn btn-default btn-xs' ng-click='grid.appScope.removeFilter(col)'>" +
                    "       <i class='fa fa-close'></i>" +
                    "   </button>" +
                    "</div>";
                columnDef.filter.noTerm = true;
                columnDef.filter.condition = function (searchTerm,  cellValue) {
                    return $scope.filterStringColumns(cellValue, codedFeatureName);
                };
                break;
            case "number":
                var filterLess = [];
                filterLess.condition  = function (searchTerm,  cellValue) {
                    return $scope.filterLessNumberColumns(cellValue, columnDef, codedFeatureName);
                };
                columnDef.filterHeaderTemplate="" +
                    "<div class='ui-grid-filter-container'>" +
                    "   <button class='btn btn-default btn-sm' ng-click='grid.appScope.showFilter(col)' data-toggle='modal' data-target='#modalSlider'>" +
                    "       <i class='fa fa-sliders'></i>" +
                    "   </button>" +
                    "   <button  ng-show='grid.appScope.isFilterOn(col)' class='btn btn-default btn-xs' ng-click='grid.appScope.removeFilter(col)'>" +
                    "       <i class='fa fa-close'></i>" +
                    "   </button>" +
                    "</div>";
                var filterGreater = [];
                filterGreater.condition  = function (searchTerm,  cellValue) {
                    return $scope.filterGreaterNumberColumns(cellValue, columnDef, codedFeatureName);
                };
                columnDef.filters = [];
                columnDef.filters.push(filterGreater);
                columnDef.filters.push(filterLess);
                break;
            case "boolean":
                var filterName = 'filter'+featureName.replace(/[&-/\s]/gi, '');
                columnDef.filterHeaderTemplate="" +
                    "<div class='ui-grid-filter-container'>" +
                    "   <span class='filterLabel'>Yes&nbsp;</span>" +
                    "   <input type='checkbox' ng-change='grid.appScope.applyBooleanFilter(col, "+filterName+")' ng-model='"+filterName+"'  ng-true-value='1' ng-false-value='0'>&nbsp; &nbsp; " +
                    "   <span class='filterLabel'>No&nbsp;</span>" +
                    "   <input type='checkbox' ng-change='grid.appScope.applyBooleanFilter(col, "+filterName+")' ng-model='"+filterName+"'  ng-true-value='2' ng-false-value='0'>" +
                    "</div>";
                columnDef.filter.noTerm = true;
                columnDef.filter.condition = function (searchTerm,  cellValue) {
                    return $scope.filterBooleanColumns(cellValue, codedFeatureName);
                };
                break;

        }
        return columnDef;
    };

    /**
     * Initialize the editor
     * @param pcm
     */
    $scope.initializeEditor = function(pcm, metadata) {
        /* Convert PCM model to editor format */
        var features = getConcreteFeatures(pcm);
        $scope.pcmData = pcm.products.array.map(function(product) {
            var productData = {};
            features.map(function(feature) {
                var featureName = convertStringToEditorFormat(feature.name);
                if(!feature.name){
                    featureName = " ";
                }
                var cell = findCell(product, feature);
                productData.name = product.name; // FIXME : may conflict with feature name
                productData[featureName] = cell.content;
            });
            return productData;
        });
        // Return rawcontent
        $scope.pcmDataRaw = pcm.products.array.map(function(product) {
            var productDataRaw = {};
            features.map(function(feature) {
                var featureName =  convertStringToEditorFormat(feature.name);
                if(!feature.name){
                    featureName = " ";
                }
                var cell = findCell(product, feature);
                productDataRaw.name = product.name; // FIXME : may conflict with feature name
                if(cell.rawContent && cell.rawContent != "") {
                    productDataRaw[featureName] = cell.rawContent;
                }
                else {
                    productDataRaw[featureName] = cell.content;// TODO: replace content with rawcontent when implemented
                }
            });
            return productDataRaw;
        });
        $rootScope.$broadcast('setPcmName', $scope.pcm.name);

        createColumns(pcm, metadata);

        $scope.setGridHeight();
    };

    function createColumns(pcm, metadata) {
        /* Define columns */
        var columnDefs = [];

        /* Column for each feature */
        var colIndex = 0;
        pcm.features.array.forEach(function (feature) {
            var featureName = feature.name;
            if(!feature.name){
                featureName = " ";
            }
            var colDef = $scope.newColumnDef(featureName, $scope.getType(featureName, $scope.pcmData));
            columnDefs.push(colDef);
            colIndex++;
        });
        if(metadata) {
            $scope.pcmData = sortProducts($scope.pcmData, metadata.productPositions);
            $scope.pcmDataRaw = sortRawProducts($scope.pcmDataRaw, $scope.pcmData);
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
            $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
        });

        return 'Loading value...';
    }


});

