/**
 * Created by gbecan on 17/12/14.
 */
pcmApp.controller("EditorCtrl", function($controller, $rootScope, $scope, $http, $timeout, uiGridConstants, $compile, $modal) {

    var subControllers = {
        $scope: $scope
    };
    $controller('UndoRedoCtrl', subControllers);
    $controller('CommandsCtrl', subControllers);
    $controller('FiltersCtrl', subControllers);
    $controller('TypesCtrl', subControllers);
    $controller('InitializerCtrl', subControllers);

    // Load PCM
    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();

    //Export
    $scope.export_content = null;

    $scope.setEdit = function(bool, reload) {

        $scope.gridOptions.columnDefs = [];
        $scope.edit = bool;
        if(reload) {
            $timeout(function(){ $scope.initializeEditor($scope.pcm, $scope.metadata)}, 100);
        }
        $rootScope.$broadcast('setToolbarEdit', bool);
    };

    if (typeof id === 'undefined' && typeof data === 'undefined') {
        /* Create an empty PCM */
        $scope.pcm = factory.createPCM();
        $scope.setEdit(true, false);
        $scope.initializeEditor($scope.pcm, $scope.metadata);

    } else if (typeof data != 'undefined')  {
        /* Load PCM from import */
        $scope.pcm = loader.loadModelFromString(data).get(0);
        $scope.metadata = data.metadata;
        $scope.initializeEditor($scope.pcm, $scope.metadata);
    } else {
        /* Load a PCM from database */
        $scope.loading = true;
        $scope.setEdit(false, false);
        $http.get("/api/get/" + id).
            success(function (data) {
                $scope.pcm = loader.loadModelFromString(JSON.stringify(data.pcm)).get(0);
                $scope.metadata = data.metadata;
                $scope.initializeEditor($scope.pcm, $scope.metadata);
                $rootScope.$broadcast('saved');
            })
            .finally(function () {
                $scope.loading = false;
            })
    }
    if (typeof modal != 'undefined') {
        $scope.setEdit(false, false);
        // Open the given modal
        $modal.open({
            templateUrl: modalTemplatePath + "modal" + modal + ".html",
            controller: modal + "Controller",
            scope: $scope
        })
    }

    $scope.setGridHeight = function() {

        if($scope.pcmData) {
            if($scope.pcmData.length * 28 + 90 > $(window).height()* 2 / 3) {
                $scope.height = $(window).height() * 2 / 3;
            }
            else{
                $scope.height = $scope.pcmData.length * 28 + 90;
            }
        }
    };

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

                var decodedFeatureName = convertStringToPCMFormat(featureData.name);
                var codedFeatureName = featureData.name;

                if(productData.hasOwnProperty(codedFeatureName)  && codedFeatureName !== "$$hashKey"
                    && codedFeatureName !== "Product") {
                    // Create feature if not existing
                    if (!featuresMap.hasOwnProperty(decodedFeatureName)) {
                        var feature = factory.createFeature();
                        feature.name = decodedFeatureName;
                        pcm.addFeatures(feature);
                        featuresMap[decodedFeatureName] = feature;
                    }
                    var feature = featuresMap[decodedFeatureName];

                    // Create cell
                    var cell = factory.createCell();
                    cell.feature = feature;
                    cell.content = productData[codedFeatureName];
                    cell.rawContent = $scope.pcmDataRaw[index][codedFeatureName];
                    product.addCells(cell);
                }
            });
            index++;
        });
        return pcm;
    }

    $scope.scrollToFocus = function( rowIndex, colIndex ) {

        $scope.gridApi.cellNav.scrollToFocus( $scope.pcmData[rowIndex], $scope.gridOptions.columnDefs[colIndex]);
    };

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
            object.feature = convertStringToPCMFormat(column.name);
            object.position = index;
            metadata.featurePositions.push(object);
            index++;
        });
        return metadata;
    }

    $scope.$watch('pcm.name', function() {

        if($scope.edit) {
            $rootScope.$broadcast('setPcmName', $scope.pcm.name);
        }
    });

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
        $scope.initializeEditor($scope.pcm, $scope.metadata);
    });

    $scope.$on('setGridEdit', function(event, args) {
        $scope.setEdit(args[0], args[1]);
    });

    $scope.$on('export', function (event, args) {
        $scope.pcm = convertGridToPCM($scope.pcmData);
        $scope.metadata = generateMetadata($scope.pcmData, $scope.gridOptions.columnDefs);
        var jsonModel = JSON.parse(serializer.serialize($scope.pcm));
        $scope.pcmObject = {};
        $scope.pcmObject.metadata = $scope.metadata;
        $scope.pcmObject.pcm = jsonModel;

        var ctrlArg = args.toUpperCase().charAt(0) + args.substring(1);
        $modal.open({
            templateUrl: modalTemplatePath + "modal" + ctrlArg + "Export.html",
            controller: ctrlArg + "ExportController",
            scope: $scope,
            size: "lg"
        })

    });

});
