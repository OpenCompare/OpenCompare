/**
 * Created by gbecan on 17/12/14.
 */


pcmApp.controller("PCMEditorController", function($rootScope, $scope, $http, $timeout, uiGridConstants) {

    // Load PCM
    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();

    $scope.gridOptions = {
        columnDefs: [],
        data: 'pcmData',
        enableRowSelection: false,
        enableCellSelection : true,
        enableCellEditOnFocus : true,
        enableRowHeaderSelection: false,
        enableColumnResizing: false,
        enableFiltering: true,
        headerRowHeight: 200
    };

    $scope.gridOptions.onRegisterApi = function(gridApi){
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.cellNav.on.navigate($scope,function(newRowCol, oldRowCol){
            console.log('navigation event');
        });
        gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef){
            $rootScope.$broadcast('modified');
        });
    };

    if (typeof id === 'undefined') {
        // Create example PCM
        $scope.pcm = factory.createPCM();
        /*var exampleFeature = factory.createFeature();
        exampleFeature.name = "Feature";
        $scope.pcm.addFeatures(exampleFeature);

        var exampleProduct = factory.createProduct();
        exampleProduct.name = "Product";
        $scope.pcm.addProducts(exampleProduct);

        var exampleCell = factory.createCell();
        exampleCell.feature = exampleFeature;
        exampleCell.content = "Yes";
        exampleProduct.addValues(exampleCell);*/

        initializeEditor($scope.pcm)

    } else {

        $http.get("/api/get/" + id).success(function (data) {
            $scope.pcm = loader.loadModelFromString(JSON.stringify(data)).get(0);
            initializeEditor($scope.pcm)
        });

    }

    function initializeEditor(pcm) {

        // Convert PCM model to editor format
        var features = getConcreteFeatures(pcm);

        var products = pcm.products.array.map(function(product) {
            var productData = {};

            features.map(function(feature) {
                var cell = findCell(product, feature);
                productData.name = product.name; // FIXME : may conflict with feature name
                productData[feature.name] = cell.content;
                //productData.rowHeaderCol = product.name;
            });

            return productData;
        });

        $scope.pcmData = products;
        var isNumber = [];
        var isBoolean = [];
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
            enableHiding: true,
            enableColumnMoving: false
        });
        // TODO : define the first column as row header (following code might help)
        // $scope.gridAPI.core.addRowHeaderColumn( { name: 'rowHeaderCol', displayName: 'Product', cellTemplate: cellTemplate} );
            pcm.features.array.forEach(function (feature) {
                var colDef = $scope.newColumnDef(feature.name, $scope.featureType);
                columnDefs.push(colDef);
            });

        $scope.gridOptions.columnDefs = columnDefs;

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
        var featureName = $scope.checkIfNameExists($scope.featureName);
        $scope.pcmData.forEach(function (productData) {
            productData[featureName] = "";
        });
        var columnDef = $scope.newColumnDef(featureName, $scope.featureType);
        $scope.gridOptions.columnDefs.push(columnDef);
        $rootScope.$broadcast('modified');
    };

    $scope.renameFeature = function() {
        var featureName = $scope.checkIfNameExists($scope.featureName);
        var index = 0;
        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            if(featureData.name === $scope.oldFeatureName) {
                $scope.pcmData.forEach(function (productData) {
                    productData[featureName] = productData[$scope.oldFeatureName];
                    delete productData[$scope.oldFeatureName];
                });

                var colDef = $scope.newColumnDef(featureName, $scope.featureType);
                $scope.gridOptions.columnDefs.splice(index, 1, colDef)
            }
            index++;
        });
        $rootScope.$broadcast('modified');
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

    $scope.deleteFeature = function(featureName) {
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

    $scope.newColumnDef = function(featureName, featureType) {
            if(!featureType) {
                featureType = "";
            }
            var columnDef = {
                name: featureName,
                enableCellEdit: true,
                enableSorting: true,
                enableHiding: false,
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
                                    featureData.cellClass = function(grid, row, col, rowRenderIndex, colRenderIndex) {
                                        return 'showCell';
                                    }
                                    $scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN);
                                }
                                else {
                                    featureData.maxWidth = '15';
                                    featureData.displayName = "";
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
                    title: 'Delete Feature',
                    icon: 'fa fa-trash-o',
                    action: function($event) {
                        $scope.deleteFeature(featureName);
                    }
                }
               ]
            };
        switch(featureType) {
            case 'boolean':
                columnDef.editableCellTemplate = 'ui-grid/dropdownEditor';
                columnDef.editDropdownValueLabel= 'bool';
                columnDef.editDropdownOptionsArray = [
                    { id: 'Yes', bool: 'Yes' },
                    { id: 'No', bool: 'No' }
                ];
                break;
            case 'number':
                columnDef.type = 'number';
                break;

        }
        return columnDef;
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

    /**
     * Validate type of cells
     */
    $scope.validate = function() {
      // TODO : validate cells in the editor
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
	



