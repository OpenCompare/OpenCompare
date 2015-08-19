/**
 * Created by hvallee on 6/19/15.
 * Updated by hvallee on 8/17/15
 */

/**
 * FiltersCtrl.js
 * Manage all grid filters
 */
pcmApp.controller("FiltersCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, editorUtil, chartService) {

    //Custom filters
    var $elm;
    $scope.columnsFilters = [];
    $scope.productFilter = "";

    /* Initialize for filter */
    $scope.gridOptions2 = {
        enableColumnMenus: false,
        onRegisterApi: function( gridApi) {
            $scope.gridApi2 = gridApi;
        }
    };

    // Slider filter
    $scope.slider = {
        options: {
            range: true
        }
    };
    $scope.filterSlider = [];

    $scope.removeFilter = function(col) {

        var featureName = col.colDef.name;
        var type =  col.colDef.type;

        switch(type) {
            case 'string':
                delete  $scope.columnsFilters[featureName];
                break;
            case 'num':
                $scope.gridOptions.columnDefs.forEach(function (feature) {
                    if(feature.name == featureName) {
                        delete feature.filters[0].term;
                        delete feature.filters[1].term;
                    }
                });
                delete  $scope.columnsFilters[featureName];
                break;
        }
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    };

    $scope.applyBooleanFilter = function(col, value){
        if($scope.columnsFilters[col.name] == value) {
            $scope.columnsFilters[col.name] = 0;
        }
        else {
            $scope.columnsFilters[col.name] = value;
        }
        // We empty tables, because data to represent has changed, and we can't just update because of tab system
        $rootScope.$broadcast("closeCharts", false);
        chartService.initArrays();


        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    };

    $scope.applyProductFilter = function() {
        // We empty tables, because data to represent has changed, and we can't just update because of tab system
        $rootScope.$broadcast("closeCharts", false);
        chartService.initArrays();
        $scope.gridOptions.columnDefs[1].filter.term = $scope.productFilter;
    };

    $scope.isFilterOn = function(col) {

        return $scope.columnsFilters[col.name];
    };

    $scope.checkFilterSliderMin = function() {
        if($scope.filterSlider[0] > $scope.filterSlider[1]){
            $scope.filterSlider[0] = $scope.filterSlider[1];
        }
    };

    $scope.checkFilterSliderMax = function() {
        if($scope.filterSlider[1] < $scope.filterSlider[0]){
            $scope.filterSlider[1] = $scope.filterSlider[0];
        }
    };

    $scope.showFilter = function(feature) {

        $scope.featureToFilter = feature.name;
        $scope.ListToFilter = [];
        $scope.gridOptions2.data = [];
        var type = feature.colDef.type;
        switch(type) {

            case 'string':
                $scope.gridApi2.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                $scope.pcmData.forEach( function ( productData ) {
                    if ($scope.ListToFilter.indexOf(productData[feature.name] ) === -1 ) {
                        $scope.ListToFilter.push(productData[feature.name]);
                    }
                });
                $scope.ListToFilter.sort();
                $timeout(function() {
                    $scope.ListToFilter.forEach(function (product) {
                        $scope.gridOptions2.data.push({product: product});
                    });
                }, 100);

                $('#modalStringFilter').modal('show');
                break;

            case 'num':

                var minAndMax = editorUtil.findMinAndMax($scope.featureToFilter, $scope.pcmData);
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
    };

    $scope.closeFilter = function() {
        var featureName = $scope.featureToFilter;
        var codedFeatureName = editorUtil.convertStringToEditorFormat(featureName);
        var type = '';

        $scope.gridOptions.columnDefs.forEach(function (feature) {
            if(feature.name == featureName) {
                type = feature.type;
            }
        });
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
                $scope.columnsFilters[codedFeatureName] = [];
                $scope.columnsFilters[codedFeatureName] = $scope.colFilter.listTerm;
                $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                break;

            case 'num':
                $scope.gridOptions.columnDefs.forEach(function (feature) {
                    if(feature.name == codedFeatureName) {
                        feature.filters[0].term = $scope.filterSlider[0];
                        feature.filters[1].term = $scope.filterSlider[1]+1;
                    }
                });
                $scope.columnsFilters[codedFeatureName] = [];
                break;
        }
        if ($elm) {
            $elm.remove();
        }
        // We empty tables, because data to represent has changed, and we can't just update because of tab system
        $rootScope.$broadcast("closeCharts", false);
        chartService.initArrays();
    };

    $scope.filterStringColumns = function(cellValue, codedFeatureName) {
        if($scope.columnsFilters[codedFeatureName]) {
            var inFilter = false;
            var index = 0;
            while(!inFilter && index < $scope.columnsFilters[codedFeatureName].length) {
                if(cellValue == $scope.columnsFilters[codedFeatureName][index] || editorUtil.isEmptyCell(cellValue)) {
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

    $scope.filterLessNumberColumns = function(cellValue, columnDef, codedFeatureName) {
        if($scope.columnsFilters[codedFeatureName]) {
            return (parseFloat(cellValue.replace(/\s/g, "").replace(",", ".")) >= columnDef.filters[0].term || editorUtil.isEmptyCell(cellValue));
        }
        else {
            return true;
        }
    };

    $scope.filterGreaterNumberColumns = function(cellValue, columnDef, codedFeatureName) {
        if($scope.columnsFilters[codedFeatureName]) {
            return (parseFloat(cellValue.replace(/\s/g, "").replace(",", ".")) <= columnDef.filters[1].term || editorUtil.isEmptyCell(cellValue));
        }
        else {
            return true;
        }
    };

    $scope.filterBooleanColumns = function(cellValue, codedFeatureName) {
        if($scope.columnsFilters[codedFeatureName] == 1) {
            return getBooleanValue(cellValue) == "yes"; //|| isEmptyCell(cellValue);
        }
        else if($scope.columnsFilters[codedFeatureName] == 2) {
            return getBooleanValue(cellValue) == "no"|| editorUtil.isEmptyCell(cellValue);
        }
        else {
            return true;
        }
    };


    function getBooleanValue (name){

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

    /* Updates from configurator */
    $scope.$on('updateFilterFromConfigurator', function (event, args) {
        switch(args.type) {
            case 'string':
                if(args.values.length > 0) {

                    $scope.columnsFilters[args.feature] = args.values;
                }
                else {
                    delete $scope.columnsFilters[args.feature];
                }
                break;
            case 'bool':
                if(args.values == true) {
                    $scope.columnsFilters[args.feature] = 1;
                }
                else {
                    delete $scope.columnsFilters[args.feature];
                }
                break;
            case 'num':
                $scope.columnsFilters[args.feature] = [];
                $scope.gridOptions.columnDefs.forEach(function (col) {
                    if(col.name == args.feature) {
                        col.filters[0].term = args.values[0];
                        col.filters[1].term = args.values[1];
                    }
                });

                break;
        }

        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    });


});





