/**
 * Created by hvallee on 6/19/15.
 * Updated by hvallee on 8/17/15
 */

/**
 * GridCtrl.js
 * Manage grid configuration
 */
pcmApp.controller("GridCtrl", function($rootScope, $scope, $window, $http, $timeout, uiGridConstants, $location, pcmApi,
                                              expandeditor, typeService, editorOptions, editorUtil, sortFeaturesService, chartService) {

    $scope.height = 300;
    $scope.minWidth = 130;
    $scope.enableEdit = editorOptions.enableEdit;
    $scope.enableExport = editorOptions.enableExport;
    $scope.enableTitle = true;
    $scope.enableShare = editorOptions.enableShare;
    $scope.file = [];

    $scope.FeaturGroupIndex = 1;

    $scope.gridOptions = {
        headerTemplate: '/assets/editor/templates/featureGroupHeader.html',
        superColDefs: [],
        columnDefs: [],
        data: 'pcmData',
        enableRowSelection: false,
        showColumnFooter: true,
        enableRowHeaderSelection: false,
        flatEntityAccess: true,
        enableColumnResizing: true,
        enableFiltering: true,
        enableCellSelection: false,
        enableCellEdit: false,
        headerRowHeight: 60,
        rowHeight: 35
    };
    $scope.columnMovedFunctions = [];
    $scope.beginCellEditFunctions = [];
    $scope.afterCellEditFunctions = [];
    $scope.onNavigateFunctions = [];

    $scope.loading = false;

    var contentValue;
    var rawValue;

    /* Grid event functions */
    function readImage(input) {
        if ( input.files && input.files[0] ) {
            var FR= new FileReader();
            FR.onload = function(e) {                     console.log(e.target.result);
                // $('#img').attr( "src", e.target.result );
                // $('#base').text( e.target.result );
            };
            FR.readAsDataURL( input.files[0] );
        }
    }

    $("#img").change(function(){
        readImage( this );
    });

    $scope.putRawDataInCell = function(rowEntity, colDef) {
        $scope.pcmData[$scope.pcmData.indexOf(rowEntity)][colDef.name] = $scope.pcmDataRaw[$scope.pcmData.indexOf(rowEntity)][colDef.name];
    };

    $scope.setVisualRepresentation = function(rowEntity, colDef, newValue, oldValue, rawValue) {

        if(newValue && rawValue != newValue) {
            $rootScope.$broadcast('modified');
            $scope.pcmData[$scope.pcmData.indexOf(rowEntity)][colDef.name] = getVisualRepresentation(newValue, $scope.pcmData.indexOf(rowEntity),
                colDef.name);

             if (colDef.name != "Product") {
                var commandParameters = [rowEntity.$$hashKey,  colDef.name, rawValue, newValue];
             }
             else {
                var commandParameters = [rowEntity.$$hashKey, 'name', rawValue, newValue];
             }
             $scope.newCommand('edit', commandParameters);
        }
        else {
            $scope.pcmData[$scope.pcmData.indexOf(rowEntity)][colDef.name] = oldValue;
        }
        /* Update value based on visual representation and raw */
        $scope.pcmDataRaw[$scope.pcmData.indexOf(rowEntity)][colDef.name] = newValue;
    };

    $scope.moveColumnData = function(colDef, originalPosition, newPosition) {

        $scope.gridOptions.columnDefs.move(originalPosition, newPosition);
        var commandParameters = [originalPosition, newPosition];

        $scope.newCommand('move', commandParameters);
        $rootScope.$broadcast('modified');
    };
    $scope.columnMovedFunctions.push($scope.moveColumnData);
    $scope.beginCellEditFunctions.push($scope.putRawDataInCell);
    $scope.afterCellEditFunctions.push($scope.setVisualRepresentation);

    /* Register grid functions */
    $scope.gridOptions.onRegisterApi = function(gridApi){


        //set gridApi on scope
        $scope.gridApi = gridApi;


        /* Called when columns are moved */
        gridApi.colMovable.on.columnPositionChanged($scope,function(colDef, originalPosition, newPosition){
            for(var i = 0; i <   $scope.columnMovedFunctions.length; i++) {
                $scope.columnMovedFunctions[i]();
            }
        });

        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef) {
            for(var i = 0; i <   $scope.beginCellEditFunctions.length; i++) {
                $scope.beginCellEditFunctions[i](rowEntity, colDef, contentValue, rawValue);
            }

        });

        gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
            for(var i = 0; i < $scope.pcmData.length; i++) {
                if($scope.pcmData[i].$$hashKey == rowEntity.$$hashKey) {
                    var rawValue = $scope.pcmDataRaw[i][colDef.name];
                }
            }
            for(var i = 0; i <   $scope.afterCellEditFunctions.length; i++) {
                $scope.afterCellEditFunctions[i](rowEntity, colDef, newValue, oldValue, rawValue);
            }
        });

        gridApi.cellNav.on.navigate($scope,function(rowEntity, colDef){
            for(var i = 0; i <   $scope.onNavigateFunctions.length; i++) {
                $scope.onNavigateFunctions[i](rowEntity, colDef);
            }
            var expandedFunctions = expandeditor.expandNavigateFunctions().navigateFunctions;
            for(var i = 0; i <   expandedFunctions.length; i++) {
                expandedFunctions[i](rowEntity, colDef);
            }
        });

        gridApi.colResizable.on.columnSizeChanged($scope,function(colDef, deltaChange){

            $scope.resizeFeatureGroup($scope.gridOptions.columnDefs, colDef, deltaChange);
        })

    };


    $scope.setGridHeight = function() {

        if($scope.pcmData) {
            if($scope.pcmData.length * 28 + 90 > $(window).height()* 2 / 3 && !editorUtil.GetUrlValue('enableEdit')) {
                $scope.height = $(window).height() * 2 / 3;
            }
            else if($scope.pcmData.length * 28 + 90 > $(window).height()) {
                $scope.height = $(window).height();
            }
            else{
                $scope.height = $scope.pcmData.length * 28 + 90;
            }
        }
    };

    function randomFeatureGroup (featureType) {
        var feature = '';
        switch(featureType) {
            case "string":
                feature = 'Feature Group 1';
                break;
            case "number":
                feature = 'Feature Group 2';
                break;
            case "boolean":
                feature = 'Feature Group 3';
                break;
        }
        return feature;
    }

    /**
     *  Create a new ColumnDef for the ui-grid
     * @param featureName
     * @param featureType
     * @returns colDef
     */
    $scope.newColumnDef = function(featureName, featureGroup, featureType) {
        if(!featureType) {
            featureType = "string";
        }
        if(featureGroup) {
            var featureGroupName = featureGroup
        }
        else {
            featureGroupName = 'empltyFeatureGroup';
        }
        var codedFeatureName = editorUtil.convertStringToEditorFormat(featureName);

        var columnDef = {
            name: codedFeatureName,
            displayName: featureName,
            width: '*',
            enableSorting: true,
            enableHiding: false,
            enableFiltering: true,
            enableColumnResizing: true,
            enableColumnMoving: false,
            enablePinning: false,
            enableCellEdit: $scope.edit,
            enableCellEditOnFocus: $scope.edit,
            allowCellFocus: true,
            superCol: featureGroupName,
            filter: {term: ''},
            minWidth: $scope.minWidth,
            cellTemplate: '<div ng-if="grid.appScope.gridOptions.rowHeight < 40" class="ngCellText" ng-class="col.colIndex()" style="overflow:hidden; text-overflow: ellipsis;white-space: nowrap;"><span ng-cell-text  translate="{{COL_FIELD}}" translate-values="{param_1: row.entity.param_1, param_2: row.entity.param_2, param_3: row.entity.param_3, param_4: row.entity.param_4}"></span></div>' +
            '<div ng-if="grid.appScope.gridOptions.rowHeight > 40" class="ngCellText" ng-class="col.colIndex()"><span ng-cell-text  translate="{{COL_FIELD}}" translate-values="{param_1: row.entity.param_1, param_2: row.entity.param_2, param_3: row.entity.param_3, param_4: row.entity.param_4}"></span></div>',
            menuItems: [
                {
                    title: 'Hide',
                    icon: 'fa fa-eye-slash',
                    action: function($event) {
                        $scope.gridOptions.columnDefs.forEach(function(featureData) {
                            if(featureData.name === codedFeatureName) {
                                columnDef.visible = false;
                                $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                            }
                        });
                        $rootScope.$broadcast('reloadFeatureGroup');
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
                    icon: 'fa fa-exchange',
                    action: function($event) {
                        $('#modalChangeType').modal('show');
                        $scope.oldFeatureName = featureName;
                        $scope.featureName = featureName;
                        $scope.gridOptions.columnDefs.forEach(function(featureData) {
                            if(featureData.name === codedFeatureName) {
                                $scope.featureType = featureData.type;
                            }
                        });
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
                },
                {
                    title: 'Unhide everything',
                    icon: 'fa fa-eye',
                    action: function($event) {
                        $scope.gridOptions.columnDefs.forEach(function(featureData) {
                            featureData.visible = true;
                        });
                        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                        $rootScope.$broadcast('reloadFeatureGroup');
                    }
                }
            ],
            cellClass: function(grid, row, col) {
                var rowValue = $scope.pcmDataRaw[$scope.pcmData.indexOf(row.entity)];
                if($scope.validating && $scope.validation[col.name] && !$scope.validation[col.name][$scope.pcmData.indexOf(row.entity)]) {
                    return 'warningCell';
                }
                else if(rowValue) {
                    return editorUtil.getCellClass(rowValue[col.name], featureType);
                }
            },
            cellTooltip: function(row, col) {
                var rawValue = $scope.pcmDataRaw[$scope.pcmData.indexOf(row.entity)];
                var contentValue = $scope.pcmData[$scope.pcmData.indexOf(row.entity)];
                if($scope.validating && $scope.validation[col.name] && !$scope.validation[col.name][$scope.pcmData.indexOf(row.entity)]) {
                    return "This value doesn't seem to match the feature type.";
                }
                else if(rawValue && editorUtil.getCellTooltip(rawValue[col.name])){
                    return editorUtil.getCellTooltip(rawValue[col.name]);
                }
                else if(contentValue) {
                    return contentValue[col.name];
                }
            }
        };
        switch(featureType) {
            case "string":
                columnDef.type = 'string';
                columnDef.filterHeaderTemplate="" +
                    "<div class='ui-grid-filter-container' ng-show='!grid.appScope.configurator'>" +
                    "   <button class='btn btn-primary mdi-action-search btn-flat btn-sm' style='padding: 4px 6px;' ng-click='grid.appScope.showFilter(col)'>" +
                    "   </button>" +
                    "   <button ng-show='grid.appScope.isFilterOn(col)' class='btn btn-default btn-sm fa fa-close' style='padding:5px' ng-click='grid.appScope.removeFilter(col)'>" +
                    "   </button>" +
                    "</div>";
                columnDef.filter.noTerm = true;
                columnDef.filter.condition = function (searchTerm,  cellValue) {
                    return $scope.filterStringColumns(cellValue, codedFeatureName);
                };
                columnDef.footerCellTemplate = "" +
                    "<div class='ui-grid-cell-contents'>" +
                    "<button class='btn btn-primary fa fa-pie-chart btn-sm' ng-class='{\"btn btn-primary btn-sm \" : grid.appScope.isInStringPieChart(col) == 1, \"btn btn-flat btn-primary btn-sm\": grid.appScope.isInStringPieChart(col) != 1}' ng-click='grid.appScope.stringPieChart(col)' style='margin: 0 1px 0 1px;padding:2px 5px 2px 5px;'>" +
                    "</button>" +
                    "<button class='btn btn-primary fa fa-connectdevelop btn-sm' ng-class='{\"btn btn-primary btn-sm \" : grid.appScope.isInStringRadarChart(col) == 1, \"btn btn-flat btn-primary btn-sm\": grid.appScope.isInStringRadarChart(col) != 1}' ng-click='grid.appScope.stringRadarChart(col)' style='margin: 0 1px 0 1px;padding:2px 5px 2px 5px;'>" +
                    "</button>" +
                    "</div>";
                break;
            case "num":
                columnDef.type = 'num'; /* Can't put number, it's interpreted by ui-grid as it, and we can't put unit because they're string */
                columnDef.sortingAlgorithm = function(a, b) {
                    var parsedA = parseFloat(a.replace(/\s/g, "").replace(",", "."));
                    var parsedB = parseFloat(b.replace(/\s/g, "").replace(",", "."));
                    return parsedA - parsedB;
                };
                var filterLess = [];
                filterLess.condition  = function (searchTerm,  cellValue) {
                    return $scope.filterLessNumberColumns(cellValue, columnDef, codedFeatureName);
                };
                columnDef.filterHeaderTemplate="" +
                    "<div class='ui-grid-filter-container' ng-show='!grid.appScope.configurator'>" +
                    "   <button class='btn btn-primary btn-sm btn-flat mdi-image-tune'  style='padding: 4px 6px;' ng-click='grid.appScope.showFilter(col)' data-toggle='modal' data-target='#modalSlider'>" +
                    "   </button>" +
                    "   <button  ng-show='grid.appScope.isFilterOn(col)' class='btn btn-default btn-sm fa fa-close' style='padding:5px' ng-click='grid.appScope.removeFilter(col)'>" +
                    "   </button>" +
                    "</div>";
                var filterGreater = [];
                filterGreater.condition  = function (searchTerm,  cellValue) {
                    return $scope.filterGreaterNumberColumns(cellValue, columnDef, codedFeatureName);
                };
                columnDef.filters = [];
                columnDef.filters.push(filterGreater);
                columnDef.filters.push(filterLess);
                columnDef.footerCellTemplate = "" +
                    "<div class='ui-grid-cell-contents'>" +
                    "<button class='btn btn-primary fa fa-line-chart btn-sm' ng-class='{\"btn btn-primary btn-sm \" : grid.appScope.isInLineChart(col) == 1, \"btn btn-flat btn-primary btn-sm\": grid.appScope.isInLineChart(col) != 1}' ng-click='grid.appScope.lineChart(col)' style='margin: 0 1px 0 1px;padding:2px 5px 2px 5px;'>" +
                    "</button>" +
                    "<button class='btn btn-primary fa fa-bar-chart btn-sm' ng-class='{\"btn btn-primary btn-sm \" : grid.appScope.isInBarChart(col) == 1, \"btn btn-flat btn-primary btn-sm\": grid.appScope.isInBarChart(col) != 1}' ng-click='grid.appScope.barChart(col)' style='margin: 0 1px 0 1px;padding:2px 5px 2px 5px;'>" +
                    "</button>" +
                    "</div>";
                break;
            case "bool":
                columnDef.type = 'bool';
                columnDef.sortingAlgorithm = function(a, b) {
                    if(typeService.getBooleanValue(a) == 'yes') {
                        return 1;
                    }
                    else  if(typeService.getBooleanValue(a) == 'no') {
                        if(typeService.getBooleanValue(b) == 'unknown') {
                            return 1;
                        }
                        else {
                            return -1;
                        }
                    }
                    else {
                        return b - a;
                    }
                };
                columnDef.filterHeaderTemplate="" +
                    "<div class='ui-grid-filter-container' ng-show='!grid.appScope.configurator'>" +
                    "<button class='btn btn-primary btn-sm' style='padding: 4px 6px;' ng-class='{\"btn btn-primary btn-sm \" : grid.appScope.isFilterOn(col) == 1, \"btn btn-flat btn-primary btn-sm\": grid.appScope.isFilterOn(col) != 1}' ng-click='grid.appScope.applyBooleanFilter(col, 1)' ><i class='mdi-navigation-check'></i></button>" +
                    "<button class='btn btn-danger btn-flat' style='padding: 4px 6px;' ng-class='{\"btn btn-danger btn-sm \" : grid.appScope.isFilterOn(col) == 2, \"btn btn-flat btn-danger btn-sm\": grid.appScope.isFilterOn(col) != 2}' btn-xs' ng-click='grid.appScope.applyBooleanFilter(col, 2)' ><i class='mdi-navigation-close'></i></button>" +
                    "</div>";
                columnDef.filter.noTerm = true;
                columnDef.filter.condition = function (searchTerm,  cellValue) {
                    return $scope.filterBooleanColumns(cellValue, codedFeatureName);
                };
                columnDef.footerCellTemplate = "" +
                    "<div class='ui-grid-cell-contents'>" +
                    "<button class='btn btn-primary fa fa-pie-chart btn-sm' ng-class='{\"btn btn-primary btn-sm \" : grid.appScope.isInPieChart(col) == 1, \"btn btn-flat btn-primary btn-sm\": grid.appScope.isInPieChart(col) != 1}' ng-click='grid.appScope.pieChart(col)' style='margin: 0 1px 0 1px;padding:2px 5px 2px 5px;'>" +
                    "</button>" +
                    "<button class='btn btn-primary fa fa-connectdevelop btn-sm' ng-class='{\"btn btn-primary btn-sm \" : grid.appScope.isInRadarChart(col) == 1, \"btn btn-flat btn-primary btn-sm\": grid.appScope.isInRadarChart(col) != 1}' ng-click='grid.appScope.radarChart(col)' style='margin: 0 1px 0 1px;padding:2px 5px 2px 5px;'>" +
                    "</button>" +
                    "</div>";
                break;
            case 'image':
                columnDef.type = 'image';
                columnDef.enableCellEdit = 'false;';
                columnDef.cellTemplate = '<form>'+
                    '<input ng-show="grid.appScope.edit" type="file" ng-model="grid.appScope.file" ng-change="grid.appScope.uploadFile(col, row)" base-sixty-four-input>'+
                    '</form>'+
                    '<img height="{{grid.appScope.gridOptions.rowHeight}}px"  ng-src=\"{{grid.getCellValue(row, col)}}\" lazy-src>';

                columnDef.enableFiltering = false;
                break;
        }
        return columnDef;
    };

    $scope.uploadFile = function(col, row) {
        if($scope.file) {
            $scope.pcmData.forEach(function(product) {
                if(product.$$hashKey == row.entity.$$hashKey) {
                    product[col.name] = "data:image/png;base64,"+$scope.file.base64;
                }
            });
        }
    };
    $scope.lineChart = function(col) {
        var visibleRows = $scope.gridApi.core.getVisibleRows($scope.gridApi.grid);
        $rootScope.$broadcast("lineChart", {col: col, pcmData: visibleRows});
    };

    $scope.barChart = function(col) {
        var visibleRows = $scope.gridApi.core.getVisibleRows($scope.gridApi.grid);
        $rootScope.$broadcast("barChart", {col: col, pcmData: visibleRows});
    };

    $scope.pieChart = function(col) {
        var visibleRows = $scope.gridApi.core.getVisibleRows($scope.gridApi.grid);
        $rootScope.$broadcast("pieChart", {col: col, pcmData: visibleRows});
    };

    $scope.radarChart = function(col) {
        var visibleRows = $scope.gridApi.core.getVisibleRows($scope.gridApi.grid);
        $rootScope.$broadcast("radarChart", {col: col, pcmData: visibleRows});
    };

    $scope.stringPieChart = function(col) {
        var visibleRows = $scope.gridApi.core.getVisibleRows($scope.gridApi.grid);
        $rootScope.$broadcast("stringPieChart", {col: col, pcmData: visibleRows});
    };

    $scope.stringRadarChart = function(col) {
        var visibleRows = $scope.gridApi.core.getVisibleRows($scope.gridApi.grid);
        $rootScope.$broadcast("stringRadarChart", {col: col, pcmData: visibleRows});
    };

    $scope.isInLineChart = function(col) {
        return chartService.isInLineChart(col.name);
    };

    $scope.isInBarChart = function(col) {
        return chartService.isInBarChart(col.name);
    };

    $scope.isInPieChart = function(col) {
        return chartService.isInPieChart(col.name);
    };

    $scope.isInRadarChart = function(col) {
        return chartService.isInRadarChart(col.name);
    };

    $scope.isInStringPieChart = function(col) {
        return chartService.isInStringPieChart(col.name);
    };

    $scope.isInStringRadarChart = function(col) {
        return chartService.isInStringRadarChart(col.name);
    };



    /**
     * Initialize the editor
     * @param pcm
     */
    $scope.initializeEditor = function(pcm, metadata, decode, loadFeatureGroups) {

        if(decode) {
            pcm = pcmApi.decodePCM(pcm); // Decode PCM from Base64
        }

        /* Convert PCM model to editor format */
        var features = pcmApi.getConcreteFeatures(pcm);

        $scope.pcmData = pcm.products.array.map(function(product) {
            var productData = {};
            features.map(function(feature) {
                var featureName = editorUtil.convertStringToEditorFormat(feature.name);
                if(!feature.name){
                    featureName = " ";
                }
                var cell = pcmApi.findCell(product, feature);
                productData.name = product.name; // FIXME : may conflict with feature name
                productData[featureName] = cell.content;
            });
            return productData;
        });

        // Return rawcontent
        $scope.pcmDataRaw = pcm.products.array.map(function(product) {
            var productDataRaw = {};
            features.map(function(feature) {
                var featureName =  editorUtil.convertStringToEditorFormat(feature.name);
                if(!feature.name){
                    featureName = " ";
                }
                var cell = pcmApi.findCell(product, feature);
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

        createColumns(pcm, metadata, features, loadFeatureGroups);
        setOptions();

        $scope.setGridHeight();
        if(loadFeatureGroups) {
            $scope.gridOptions.superColDefs = sortFeaturesService.sortFeatureGroupByName($scope.gridOptions.superColDefs);
            $timeout(function() {$scope.loadFeatureGroups($scope.gridOptions.columnDefs, $scope.gridOptions.superColDefs);}, 0);
        }

    };

    function createColumns(pcm, metadata, features, loadFeatureGroups) {
        /* Define columns */
        var columnDefs = [];

        var colIndex = 0;
        var hasFeatureGroups = false;

        features.map(function(feature) {

            var featureName = feature.name;
            if(!feature.name){
                featureName = " ";
            }
            var featureGroupName = "emptyFeatureGroup";
            if(feature.parentGroup) {
                featureGroupName = feature.parentGroup.name;
                hasFeatureGroups = true;
            }
            var colDef = $scope.newColumnDef(featureName, featureGroupName, typeService.getType(featureName, $scope.pcmData));
            columnDefs.push(colDef);
            colIndex++;
        });

        if(hasFeatureGroups && loadFeatureGroups) {
            $scope.gridOptions.superColDefs = [];
            var emptyfeatureGroup = {
                name: 'emptyFeatureGroup',
                displayName: ' '
            };
            $scope.gridOptions.superColDefs.push(emptyfeatureGroup);
            pcm.features.array.forEach(function (featureGroup) {
                var isAFeatureNotAFeatureGroup = false;
                features.map(function(feature) {
                    if(featureGroup.name == feature.name) {
                        isAFeatureNotAFeatureGroup = true;
                    }
                });
                if(!isAFeatureNotAFeatureGroup) {
                    var featureName = featureGroup.name;

                    var newFeatureGroup = {
                        name: featureName,
                        displayName: featureName
                    };
                    $scope.gridOptions.superColDefs.splice(0, 0, newFeatureGroup);
                }
            });
        }


        if(metadata) {
            $scope.pcmData = editorUtil.sortProducts($scope.pcmData, metadata.productPositions);
            $scope.pcmDataRaw = editorUtil.sortRawProducts($scope.pcmDataRaw, $scope.pcmData);
            columnDefs = editorUtil.sortFeatures(columnDefs, metadata.featurePositions);
        }

        var toolsColumn = {
            name: ' ',
            cellTemplate: '<div class="buttonsCell" ng-show="grid.appScope.edit">' +
            '<button role="button" class="btn btn-flat btn-default" ng-click="grid.appScope.removeProduct(row)"><i class="fa fa-times"></i></button>'+
            '</div>',
            enableCellEdit: false,
            enableFiltering: false,
            enableColumnResizing: false,
            pinnedLeft:true,
            enableSorting: false,
            enableHiding: false,
            enableColumnMenu: false,
            allowCellFocus: false,
            enableColumnMoving: false
        };
        switch($scope.edit) {
            case true:
                toolsColumn.width = 30;
                break;
            case false:
                toolsColumn.width = 1;
                break;
        }

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
            pinnedLeft:true,
            allowCellFocus: true,
            minWidth: 150,
            width: 150,
            menuItems: [
                {
                    title: 'Unhide everything',
                    icon: 'fa fa-eye',
                    action: function($event) {
                        $scope.gridOptions.columnDefs.forEach(function(featureData) {
                            featureData.visible = true;
                        });
                        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                    }
                }
            ]
        };

        /* Specific filter for products */
        productsColumn.filter = [];
        productsColumn.filter.term = '';
        productsColumn.filter.placeholder = 'Find';
        productsColumn.filterHeaderTemplate="" +
            "<div class='ui-grid-filter-container'>" +
            "   <input type='text' class='form-control floating-label' ng-change='grid.appScope.applyProductFilter()' ng-model='grid.appScope.productFilter' placeholder='Find'"+
            "</div>";
        productsColumn.footerCellTemplate="" +
            "<div class='ui-grid-cell-contents'>" +
            "<span>{{grid.appScope.gridApi.core.getVisibleRows($scope.gridApi.grid).length}} / {{grid.appScope.pcmData.length}}</span>"+
            "</div>";
        columnDefs.splice(0, 0, toolsColumn);
        columnDefs.splice(1, 0, productsColumn);


        if($scope.gridOptions.superColDefs.length > 0) {
            $scope.gridOptions.columnDefs = sortFeaturesService.sortByFeatureGroup(columnDefs, $scope.gridOptions.superColDefs);
        }
        else {
            $scope.gridOptions.columnDefs = columnDefs;
        }



    }

    function setOptions() {
        if(editorUtil.GetUrlValue('enableEdit') == 'false'){
            $scope.enableEdit = false;
        }
        if(editorUtil.GetUrlValue('enableExport') == 'false'){
            $scope.enableExport = false;
        }
        if(editorUtil.GetUrlValue('enableTitle') == 'false'){
            $scope.enableTitle = false;
        }
        if(editorUtil.GetUrlValue('enableShare') == 'false'){
            $scope.enableShare = false;
        }
        if(editorUtil.GetUrlValue('deleteAfterLoaded') == 'true'){
            if (typeof id !== 'undefined') {
                $http.get("/api/remove/" + id);
            }
        }
        if(!$scope.edit) {//Todo: replace by configuratorMode == true
            $rootScope.$broadcast("initConfigurator", {features: $scope.gridOptions.columnDefs, pcmData: $scope.pcmData});
        }
    }



    /**
     * Get the visual representation of a raw data
     * @param cellValue
     * @returns {Array.<T>|string|Blob|ArrayBuffer|*}
     */
    function getVisualRepresentation(cellValue, index, colName) {
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

            $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
        });

        return 'Loading value...';
    }


});

