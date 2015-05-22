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
        headerRowHeight: 200
    };

    $scope.gridOptions.onRegisterApi = function(gridApi){
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.cellNav.on.navigate($scope,function(newRowCol, oldRowCol){
            console.log('navigation event');
        });
    };

    if (typeof id === 'undefined') {
        // Create example PCM
        $scope.pcm = factory.createPCM();
        var exampleFeature = factory.createFeature();
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
        exampleProduct.addValues(exampleCell1);

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
            enableCellEdit: true,
            enableSorting: true,
            enableHiding: true,
            enableColumnMoving: false
        });
        // TODO : define the first column as row header (following code might help)
        // $scope.gridAPI.core.addRowHeaderColumn( { name: 'rowHeaderCol', displayName: 'Product', cellTemplate: cellTemplate} );

        pcm.features.array.forEach(function (feature) {
             columnDefs.push({
                name: feature.name,
                enableCellEdit: true,
                enableSorting: true,
                enableHiding: true,
                menuItems: [
                    {
                        title: 'Delete Feature',
                        icon: 'fa fa-trash-o',
                        action: function($event) {
                            var index = 0;
                            $scope.gridOptions.columnDefs.forEach(function(featureData) {
                               if(featureData.name === feature.name) {
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
                        }
                    },
                    {
                        title: 'Rename Feature',
                        icon: 'fa fa-pencil',
                        action: function($event) {
                            $('#modalRenameFeature').modal('show');
                            $scope.oldFeatureName = feature.name;
                            $scope.featureName = feature.name;
                        }
                    }
                ]
            });
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
        var featureName = $scope.featureName;
        var columnDef = {
            name: featureName,
            field: 'New Feature',
            enableCellEdit: true,
            enableSorting: true,
            enableHiding: true
        };

        $scope.pcmData.forEach(function (productData) {
            productData[featureName] = "";
        });

        $scope.gridOptions.columnDefs.push(columnDef);
    };

    $scope.copyPasteFeature = function(featureToPast) {
        var featureName = $scope.featureName;
        var columnDef = {
            name: featureName,
            enableCellEdit: true,
            enableSorting: true,
            enableHiding: true
        };

        $scope.pcmData.forEach(function (productData) {
            productData[featureName] = productData[featureToPast];
        });
        $scope.gridOptions.columnDefs.push(columnDef);
    };

    $scope.renameFeature = function() {
        var index = 0;
        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            if(featureData.name === $scope.oldFeatureName) {
                var featureName = $scope.featureName;
                $scope.pcmData.forEach(function (productData) {
                    productData[featureName] = productData[$scope.oldFeatureName];
                    delete productData[$scope.oldFeatureName];
                });

                var colDef = {
                    name: featureName,
                    enableCellEdit: true,
                    enableSorting: true,
                    enableHiding: true
                };
                $scope.gridOptions.columnDefs.splice(index, 1, colDef)

                console.log(index);
                //$timeout(function(){ $scope.gridApi.colMovable.moveColumn($scope.gridOptions.columnDefs.length-1, index);}, 100);
                //  $timeout(function(){ $scope.deleteFeature();}, 100);
            }
            index++;
        });
    };

    $scope.deleteFeature = function() {
        var index = 0;
        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            if(featureData.name === $scope.oldFeatureName) {
                var index2 = 0;
                $scope.pcmData.forEach(function () {
                    delete $scope.pcmData[index2][$scope.oldFeatureName];
                    index2++;
                });
                $scope.gridOptions.columnDefs.splice(index, 1);
            }
            index++;
        });
        console.log("Feature is deleted");
    };

    $scope.addProduct = function() {
        var productData = {};
        productData.name = "";

        $scope.gridOptions.columnDefs.forEach(function(featureData) {
            productData[featureData.name] = "";
        });

        $scope.pcmData.push(productData);
    };

    /**
     * Add a new product and focus on this new
     * @param row
     */
    $scope.addProductAndFocus = function(row) {
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
        console.log($scope.pcmData[6]);
        console.log("Focus");
    };

    $scope.removeProduct = function(row) {
        var index = $scope.pcmData.indexOf(row.entity);
        $scope.pcmData.splice(index, 1);
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
	



