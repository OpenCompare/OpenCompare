/**
 * Created by gbecan on 17/12/14.
 */


pcmApp.controller("PCMEditorController", function($rootScope, $scope, $http, $timeout, uiGridConstants, $compile ) {

    // Load PCM
    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();

    // Validate pcm type
    var columnsType = [];
    var validation = [];

    //Custom filters
    var $elm;
    var columnsFilters = [];

    $scope.gridOptions = {
        columnDefs: [],
        data: 'pcmData',
        enableRowSelection: false,
        enableCellSelection : true,
        enableCellEditOnFocus : true,
        enableRowHeaderSelection: false,
        enableColumnResizing: false,
        enableFiltering: true,
        headerRowHeight: 300,
    };

    $scope.gridOptions.height = $scope.gridOptions.data.length*20+30;

    $scope.gridOptions.onRegisterApi = function(gridApi){
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, oldValue, newValue){
            if(colDef.name != "Product" && oldValue != newValue) {
                if(!$scope.validateType(rowEntity[colDef.name], columnsType[colDef.name])) {
                    if(!validation[colDef.name]) {
                        validation[colDef.name] = [];
                    }
                    validation[colDef.name][$scope.pcmData.indexOf(rowEntity)] = false;
                    $rootScope.$broadcast('warning');
                }
                else{
                    if(!validation[colDef.name]) {
                        validation[colDef.name] = [];
                    }
                    validation[colDef.name][$scope.pcmData.indexOf(rowEntity)] = true;
                    if(isCompletelyValidated()){
                        $rootScope.$broadcast('completelyValidated');
                    }
                }
                $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
                $rootScope.$broadcast('modified');
            }
        });
    };

    if (typeof id === 'undefined' && typeof data === 'undefined') {
        // Create example PCM
        $scope.pcm = factory.createPCM();
        /* var exampleFeature = factory.createFeature();
        exampleFeature.name = "Feature";
        $scope.pcm.addFeatures(exampleFeature);

        var exampleFeature1 = factory.createFeature();
        exampleFeature1.name = "Feature1";
        $scope.pcm.addFeatures(exampleFeature1);

        var exampleProduct = factory.createProduct();
        exampleProduct.name = "Product";
        $scope.pcm.addProducts(exampleProduct);

        var exampleCell = factory.createCell();
        exampleCell.feature = exampleFeature;
        exampleCell.content = "Yes";
        exampleProduct.addValues(exampleCell);

        var exampleCell1 = factory.createCell();
        exampleCell1.feature = exampleFeature1;
        exampleCell1.content = "No";
        exampleProduct.addValues(exampleCell1);*/
        initializeEditor($scope.pcm)

    } else if (typeof data != 'undefined')  {
        $scope.pcm = loader.loadModelFromString(data).get(0);
        initializeEditor($scope.pcm)

    } else {
        $http.get("/api/get/" + id).success(function (data) {
            $scope.pcm = loader.loadModelFromString(JSON.stringify(data)).get(0);
            initializeEditor($scope.pcm)
        });
    }

    function newColumnDef(featureName, featureType) {
        if(!featureType) {
            featureType = "string";
        }
        columnsType[featureName] = featureType;
        var columnDef = {
            name: featureName,
            enableCellEdit: true,
            enableSorting: true,
            enableHiding: false,
            enableFiltering: true,
            filter: {term: ''},
            menuItems: [
                {
                    title: 'Hide/Unhide',
                    icon: 'fa fa-eye',
                    action: function($event) {
                        $scope.gridOptions.columnDefs.forEach(function(featureData) {
                            if(featureData.name === featureName) {
                                if(featureData.maxWidth == '15') {
                                    featureData.maxWidth = '*';
                                    featureData.displayName = featureData.name;
                                    featureData.enableFiltering = true;
                                    featureData.cellClass = function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                        return 'showCell';
                                    }
                                    $scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN);
                                }
                                else {
                                    featureData.maxWidth = '15';
                                    featureData.displayName = "";
                                    featureData.enableFiltering = false;
                                    featureData.cellClass = function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                        return 'hideCell';
                                    }
                                    $scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN);
                                }
                            }
                        });
                    }
                },
                {
                    title: 'Rename Feature',
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
                        $scope.featureType = columnsType[featureName];
                    }
                },
                {
                    title: 'Delete Feature',
                    icon: 'fa fa-trash-o',
                    action: function($event) {
                        $scope.deleteFeature(featureName);
                    }
                }
            ],
            cellClass: function(grid, row, col) {
                if(!validation[col.name][$scope.pcmData.indexOf(row.entity)]) {
                    return 'warningCell';
                }
            },
            cellTooltip: function(row, col) {
                    if(!validation[col.name][$scope.pcmData.indexOf(row.entity)]) {
                        return "This value doesn't seem to match the feature type, validate if you want to keep it.";
                    }
                    else {
                        //return $scope.grid.getCellValue(row, col);
                    }
                }
        };
        switch(featureType) {
            case "string":
                columnDef.filterHeaderTemplate="<div class='ui-grid-filter-container'><button ng-click='grid.appScope.showFilter(col)'>Filter</button><button ng-click='grid.appScope.removeFilter(col)'><i class='fa fa-close'></i></button></div>";
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
                }
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
                columnDef.filterHeaderTemplate="<div class='ui-grid-filter-container'><input type='checkbox' ng-change='grid.appScope.applyBooleanFilter(col, "+filterName+")' ng-model='"+filterName+"'  ng-true-value='1' ng-false-value='0'></div>";
                columnDef.filter.noTerm = true;
                columnDef.filter.condition = function (searchTerm, cellValue) {
                    if(columnsFilters[featureName] == 1) {
                        if(getBooleanValue(cellValue)) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                    else {
                        return true;
                    }
                }
                break;

        }
        return columnDef;
    };

    function getType (featureName) {
        rowIndex = 0;
        var isInt = 0;
        var isBool = 0;
        var isString = 0;

        while($scope.pcmData[rowIndex]) {
            if(!angular.equals(parseInt($scope.pcmData[rowIndex][featureName]), NaN)) {
                isInt++;
            }
            else if(($scope.pcmData[rowIndex][featureName].toLowerCase() === "yes") ||  ($scope.pcmData[rowIndex][featureName].toLowerCase() === "no")) {
                isBool++;
            }
            else if(!isEmptyCell($scope.pcmData[rowIndex][featureName])){
                isString++;
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
    };

    function getBooleanValue(name){
        if(name.toLowerCase() === "yes") {
            return true;
        }
        else {
            return false;
        }
    }

    function isEmptyCell(name) {
        if(!name || name == "" || name == "N/A" || name == "?") {
            return true;
        }
        else {
            return false;
        }
    };

    function isCompletelyValidated(){
        var valid = true;
        $scope.gridOptions.columnDefs.forEach(function (featureData) {
            if(!validation[featureData.name]) {
                validation[featureData.name] = [];
            }
            for (var i = 0; i < $scope.pcmData.length; i++) {
                if(valid) {
                    valid = validation[featureData.name][i];
                }
            };
        });
        return valid;
    };

    $scope.validate = function() {
        $scope.gridOptions.columnDefs.forEach(function (featureData){
            console.log(featureData.filters);
            validation[featureData.name] = []
            for(var i = 0; i < $scope.pcmData.length; i++) {
                validation[featureData.name][i] = true;
            }
        });
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
        $rootScope.$broadcast("completelyValidated");
    };

    function initializeEditor(pcm) {

        // Convert PCM model to editor format
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

        $scope.pcmData = products;
        var productNames = pcm.products.array.map(function (product) {
            return product.name
        });
        // Define columns
        var columnDefs = [];
        var index = 0;
        columnDefs.push({
            name: ' ',
            cellTemplate: '<div class="buttonsCell">' +
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
        });

        columnDefs.push({
            name: 'Product',
            field: "name",
            cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                return 'productCell';
            },
            enableCellEdit: true,
            enableSorting: true,
            enableHiding: false,
            enableColumnMoving: false
        });
        columnDefs[1].filter = [];
        columnDefs[1].filter.condition = function(searchTerm, cellValue) {
            if(cellValue.toLowerCase().indexOf(searchTerm) != -1) {
                return true;
            }
            else {
                return false;
            }
        }
        columnDefs[1].filter.placeholder = 'Find';
        var colIndex = 0;
            pcm.features.array.forEach(function (feature) {
                var colDef = newColumnDef(feature.name, getType(feature.name));
                columnDefs.push(colDef);
                colIndex++;
            });

        $scope.gridOptions.columnDefs = columnDefs;
        if($scope.pcmData.length > 0){
            $scope.gridOptions.columnDefs.forEach(function (featureData){
                validation[featureData.name] = [];
                for(var i = 0; i < $scope.pcmData.length; i++) {
                    validation[featureData.name][i] = true;
                }
            });
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
                    product.addValues(cell);
                }
            }
        });
        return pcm;
    }

    $scope.addFeature = function() {
        if(!$scope.featureType) {
            $scope.featureType = "string";
        }
        var featureName = $scope.checkIfNameExists($scope.featureName);
        $scope.pcmData.forEach(function (productData) {
            productData[featureName] = "";
        });
        var columnDef = newColumnDef(featureName, $scope.featureType);
        $scope.gridOptions.columnDefs.push(columnDef);
        columnsType[featureName] = $scope.featureType;
        validation[featureName] = [];
        for(var i = 0; i < $scope.pcmData.length; i++) {
            validation[featureName][i] = true;
        }
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
                $scope.pcmData.forEach(function (productData) {
                    productData[featureName] = productData[$scope.oldFeatureName];
                    if($scope.featureName != $scope.oldFeatureName) {
                        delete productData[$scope.oldFeatureName];
                    }
                });
                var colDef = newColumnDef(featureName, $scope.featureType);
                $scope.gridOptions.columnDefs.splice(index, 1, colDef)
            }
            index++;
        });
        columnsType[featureName] = columnsType[$scope.oldFeatureName];
        validation[featureName] = [];
        for(var i = 0; i < $scope.pcmData.length; i++) {
            validation[featureName][i] = true;
        }
        if($scope.featureName != $scope.oldFeatureName) {
            delete columnsType[$scope.oldFeatureName];
            delete validation[$scope.oldFeatureName];
        }
        $rootScope.$broadcast('modified');
    };

    $scope.changeType = function () {
        var featureName = $scope.featureName;
        columnsType[featureName] = $scope.featureType;
    };

    $scope.checkIfNameExists = function(name) {

        if(!name) {
            var newName = "New Feature";
        }
        else {
            var newName = name;
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
        else if((productName.toLowerCase() === "yes") ||  (productName.toLowerCase() === "no")) {
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

    $scope.validateEverything = function () {
        validation = [];
    }

    $scope.deleteFeature = function(featureName) {
        delete validation[featureName];
        var index = 0;
        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            if(featureData.name === featureName) {
                var index2 = 0;
                $scope.pcmData.forEach(function () {
                    delete $scope.pcmData[index2][featureData.name];
                    index2++;
                });
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
    $scope.addProduct = function(row) {
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
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
        $timeout(function(){ $scope.scrollToFocus($scope.pcmData.length-1, 1); }, 100);// Not working without a timeout
        console.log("Product added");
        $rootScope.$broadcast('modified');
    };

    $scope.removeProduct = function(row) {
        var index = $scope.pcmData.indexOf(row.entity);
        $scope.pcmData.splice(index, 1);
        console.log("Product removed")
        $rootScope.$broadcast('modified');
    };


    $scope.scrollToFocus = function( rowIndex, colIndex ) {
        $scope.gridApi.cellNav.scrollToFocus( $scope.pcmData[rowIndex], $scope.gridOptions.columnDefs[colIndex]);
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

    /**
     * Cancel edition
     */
    $scope.cancel = function() {
        window.location = "/view/" + id;
    };

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
        console.log(columnsFilters[featureName]);
    };

    $scope.removeFilter = function(col) {
        var featureName = col.name;
        console.log(columnsFilters[featureName]);
        delete columnsFilters[featureName];
        console.log(columnsFilters[featureName]);
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
    };

    $scope.applyBooleanFilter = function(col, value){
        columnsFilters[col.name] = value;
        $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
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

});
	



