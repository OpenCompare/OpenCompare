/**
 * Created by hvallee on 6/19/15.
 */

pcmApp.controller("FiltersCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, $compile, $modal) {

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

        var featureName = col.name;
        var type =  $scope.columnsType[featureName];
        switch(type) {
            case 'string':
                delete  $scope.columnsFilters[featureName];
                break;
            case 'number':
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
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    };

    $scope.applyProductFilter = function() {
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
        var type = $scope.columnsType[feature.name];
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

            case 'number':

                var minAndMax = findMinAndMax($scope.featureToFilter, $scope.pcmData);
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
        var codedFeatureName = convertStringToEditorFormat(featureName);

        var type =  $scope.columnsType[codedFeatureName];
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

            case 'number':
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
    };

    $scope.filterStringColumns = function(cellValue, codedFeatureName) {
        if($scope.columnsFilters[codedFeatureName]) {
            var inFilter = false;
            var index = 0;
            while(!inFilter && index < $scope.columnsFilters[codedFeatureName].length) {
                if(cellValue == $scope.columnsFilters[codedFeatureName][index] || isEmptyCell(cellValue)) {
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
            return (parseFloat(cellValue.replace(/\s/g, "").replace(",", ".")) >= columnDef.filters[0].term || isEmptyCell(cellValue));
        }
        else {
            return true;
        }
    };

    $scope.filterGreaterNumberColumns = function(cellValue, columnDef, codedFeatureName) {
        if($scope.columnsFilters[codedFeatureName]) {
            return (parseFloat(cellValue.replace(/\s/g, "").replace(",", ".")) <= columnDef.filters[1].term || isEmptyCell(cellValue));
        }
        else {
            return true;
        }
    };

    $scope.filterBooleanColumns = function(cellValue, codedFeatureName) {
        if($scope.columnsFilters[codedFeatureName] == 1) {
            return getBooleanValue(cellValue) == "yes" || isEmptyCell(cellValue);
        }
        else if($scope.columnsFilters[codedFeatureName] == 2) {
            return getBooleanValue(cellValue) == "no" || isEmptyCell(cellValue);
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

});





