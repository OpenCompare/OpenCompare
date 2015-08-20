/**
 * Created by gbecan on 12/17/14.
 * Updated by hvalle on 8/17/15
 */


/**
* EditorCtrl.js
* Main controller for OpenCompare Editor
*/
pcmApp.controller("EditorCtrl", function($controller, $rootScope, $scope, $http, $timeout, uiGridConstants, $compile, $modal, expandeditor,  $location, pcmApi, editorUtil) {

    /* Load material design */
    if($.material) {
        $.material.init();
    }


    /* Define subControllers, because we're in the grid, we can't create new controller on sub div */
    var subControllers = {
        $scope: $scope,
        $location: $location
    };

    $controller('GridCtrl', subControllers);
    $controller('UndoRedoCtrl', subControllers);
    $controller('CommandsCtrl', subControllers);
    $controller('FiltersCtrl', subControllers);
    $controller('TypesCtrl', subControllers);
    $controller('ShareCtrl', subControllers);
    $controller('FeatureGroupCtrl', subControllers);

    // Load PCM
    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();

    //Export
    $scope.export_content = null;

    //Use for modals
    $scope.oldFeatureName = "";
    $scope.featureName = "";

    $scope.loaded = false;
    $scope.lineView = true;

    // Set grid in edit/view mode
    $scope.setEdit = function(bool, reload) {

        $scope.gridOptions.columnDefs = [];
        $scope.gridOptions.rowHeight = 35;
        $scope.edit = bool;
        if(reload) {
            $timeout(function(){
                $scope.initializeEditor($scope.pcm, $scope.metadata, false, true);
            }, 100);
        }
        $rootScope.$broadcast('setToolbarEdit', bool);


    };

    // Main entry of the editor

    if (typeof id === 'undefined' && typeof data === 'undefined') {
        /* Create an empty PCM */
        $scope.pcm = factory.createPCM();
    }
    else if (typeof data != 'undefined')  {
        /* Load PCM from import */
        $scope.pcm = loader.loadModelFromString(data).get(0);
        pcmApi.decodePCM($scope.pcm); // Decode PCM from Base64
        $scope.metadata = data.metadata;
        $scope.initializeEditor($scope.pcm, $scope.metadata, false, true);
    } else{
        /* Load a PCM from database */
        $scope.loading = true;
        $scope.setEdit(false, false);
        $scope.updateShareLinks();
        $http.get("/api/get/" + id).
            success(function (data) {
                $scope.pcm = loader.loadModelFromString(JSON.stringify(data.pcm)).get(0);
                pcmApi.decodePCM($scope.pcm); // Decode PCM from Base64
                $scope.metadata = data.metadata;
                $scope.initializeEditor($scope.pcm, $scope.metadata, false, true);
                $rootScope.$broadcast('saved', id);
            })
            .finally(function () {
                $scope.loading = false;
            })
    }
    /* Load modal for import */
    if (typeof modal != 'undefined') {
        $scope.setEdit(false, false);
        // Open the given modal
        $modal.open({
            templateUrl: modalTemplatePath + "modal" + modal + ".html",
            controller: modal + "Controller",
            scope: $scope
        })
    }
    $scope.$on('initializeFromExternalSource', function(event, args) {
        $scope.pcm = loader.loadModelFromString(JSON.stringify(args.pcm)).get(0);
        pcmApi.decodePCM($scope.pcm); // Decode PCM from Base64
        $scope.metadata = args.metadata;
        $scope.initializeEditor($scope.pcm, $scope.metadata, false, true);
    });

    $scope.$on('setConfiguratorMode', function(event, arg) {
        $scope.configurator = arg;
    });

    $scope.$on('setLineView', function(event, arg) {
        $scope.lineView = arg;
    });

    /* Button to increase row height */
    $scope.$on('increaseHeight', function(event, arg) {
        switch(arg){
            case 1:
                $scope.gridOptions.rowHeight = 35;
                break;
            case 2:
                $scope.gridOptions.rowHeight = 60;
                break;
            case 4:
                $scope.gridOptions.rowHeight = 120;
                break;
            case 8:
                $scope.gridOptions.rowHeight = 240;
                break;
        }
        $scope.initializeEditor($scope.pcm, $scope.metadata, false, true);
        $scope.setGridHeight();
    });


    $scope.setGridHeight = function() {

        if($scope.pcmData) {
            if($scope.pcmData.length * $scope.gridOptions.rowHeight + 100 > $(window).height()* 2 / 3 && !editorUtil.GetUrlValue('enableEdit')) {
                $scope.height = $(window).height() * 2 / 3;
            }
            else if($scope.pcmData.length * $scope.gridOptions.rowHeight + 100 > $(window).height() && editorUtil.GetUrlValue('enableEdit')) {
                var height = 20;

                if(editorUtil.GetUrlValue('enableExport') == 'true' || editorUtil.GetUrlValue('enableShare') == 'true') {
                        height += 40;

                }
                if(editorUtil.GetUrlValue('enableTitle') == 'true') {
                    if($scope.pcm.name.length > 30) {
                        height += 120;
                    }
                    else {
                        height += 60;
                    }

                }
                if(editorUtil.GetUrlValue('enableEdit') == 'true') {
                    if($scope.edit) {
                        height += 80;
                    }
                    else {
                        height += 40;
                    }
                }
                $scope.height = $(window).height()-height;
            }
            else{
                $scope.height = $scope.pcmData.length * $scope.gridOptions.rowHeight + 130;
            }
        }
    };

    /* Convert grid to pcm for mongoDB */
    function convertGridToPCM(pcmData) {
        var pcm = factory.createPCM();
        pcm.name = $scope.pcm.name;

        var featuresMap = {};
        var featureGroupsMap = {};

        var index = 0;
        pcmData.forEach(function(productData) {
            // Create product
            var product = factory.createProduct();
            product.name = productData.name;
            pcm.addProducts(product);
            var featureGroups = $scope.gridOptions.superColDefs;
            var features = $scope.gridOptions.columnDefs;

            if(featureGroups.length > 0) {
                for(var i = 0; i < featureGroups.length; i++) {

                    var decodedFeatureGroupName = editorUtil.convertStringToPCMFormat(featureGroups[i].name);
                    var codedFeatureGroupName = featureGroups[i].name;

                    if (!featureGroupsMap.hasOwnProperty(codedFeatureGroupName)) {
                        if (codedFeatureGroupName != 'emptyFeatureGroup') {
                            var featureGroup = factory.createFeatureGroup();
                            featureGroup.name = decodedFeatureGroupName;
                            featureGroupsMap[decodedFeatureGroupName] = featureGroup;

                            var featuresWithThisFeatureGroup = $scope.getFeaturesWithThisFeatureGroup(codedFeatureGroupName, features);

                            for (var j = 0; j < featuresWithThisFeatureGroup.length; j++) {
                                if (!featuresMap.hasOwnProperty(editorUtil.convertStringToPCMFormat(featuresWithThisFeatureGroup[j]))
                                    && featuresWithThisFeatureGroup[j] !== " "
                                    && featuresWithThisFeatureGroup[j] !== "Product") {
                                    var featureToAdd = factory.createFeature();
                                    featureToAdd.name = editorUtil.convertStringToPCMFormat(featuresWithThisFeatureGroup[j]);
                                    featureGroup.addSubFeatures(featureToAdd);
                                    featuresMap[editorUtil.convertStringToPCMFormat(featuresWithThisFeatureGroup[j])] = featureToAdd;
                                }
                            }
                            pcm.addFeatures(featureGroup);
                        }
                        else {
                            var featuresWithThisFeatureGroup = $scope.getFeaturesWithThisFeatureGroup(featureGroups[i].name, features);
                            for (var k = 0; k < featuresWithThisFeatureGroup.length; k++) {
                                if (!featuresMap.hasOwnProperty(featuresWithThisFeatureGroup[k])
                                && featuresWithThisFeatureGroup[k] !== " "
                                 && featuresWithThisFeatureGroup[k] !== "Product")  {
                                    featureGroupsMap[editorUtil.convertStringToPCMFormat(featureGroups[i].name)] = 'empty';
                                    var featureToAdd = factory.createFeature();
                                    featureToAdd.name = editorUtil.convertStringToPCMFormat(featuresWithThisFeatureGroup[k]);
                                    featuresMap[editorUtil.convertStringToPCMFormat(featuresWithThisFeatureGroup[k])] = featureToAdd;
                                    pcm.addFeatures(featureToAdd);
                            }

                            }

                        }
                    }
                }
                $scope.gridOptions.columnDefs.forEach(function (featureData) {

                    var decodedFeatureName = editorUtil.convertStringToPCMFormat(featureData.name);
                    var codedFeatureName = featureData.name;
                    if(productData.hasOwnProperty(decodedFeatureName)  && decodedFeatureName !== " "
                        && decodedFeatureName !== "Product") {
                        var feature = featuresMap[decodedFeatureName];

                        // Create cell
                        var cell = factory.createCell();

                        cell.feature = feature;
                        cell.content = productData[codedFeatureName];
                        cell.rawContent = $scope.pcmDataRaw[index][codedFeatureName];
                        product.addCells(cell);
                    }
                });
            }
            else {
                $scope.gridOptions.columnDefs.forEach(function (featureData) {

                    var decodedFeatureName = editorUtil.convertStringToPCMFormat(featureData.name);
                    var codedFeatureName = featureData.name;

                    if(productData.hasOwnProperty(decodedFeatureName)  && codedFeatureName !== " "
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
            }

            index++;
        });

        // Encode PCM in Base64
        pcmApi.encodePCM(pcm);

        return pcm;
    }

    /**
     *  Use after adding a product
     */
    $scope.scrollToFocus = function( rowIndex, colIndex ) {

        $scope.gridApi.cellNav.scrollToFocus( $scope.pcmData[rowIndex], $scope.gridOptions.columnDefs[colIndex]);
    };

    /**
     * Save PCM on the server
     */
    $scope.save = function() {

        var pcmToSave = convertGridToPCM($scope.pcmData);
        $scope.metadata = generateMetadata($scope.pcmData, $scope.gridOptions.columnDefs);
        var jsonModel = JSON.parse(serializer.serialize(pcmToSave));

        var pcmObject = {};
        pcmObject.metadata = $scope.metadata;
        pcmObject.pcm = jsonModel;

        if (typeof id === 'undefined') {
            $http.post("/api/create", pcmObject).success(function(data) {
                id = data;
                $scope.updateShareLinks();
                console.log("model created with id=" + id);
                $rootScope.$broadcast('savedFromCreator', id);
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

    /**
     * Generate metadata like products and features positions
     * @param product
     * @param columns
     * @returns {{}}
     */
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
            object.feature = editorUtil.convertStringToPCMFormat(column.name);
            object.position = index;
            metadata.featurePositions.push(object);
            index++;
        });
        return metadata;
    }

    /**
     * Check for name modification, if so, update the toolbar
     */
    $scope.$watch('pcm.name', function() {

        if($scope.edit) {
            $rootScope.$broadcast('setPcmName', $scope.pcm.name);
        }
    });

    /**
     * Launch creation when in creator mode
     */
    $scope.$on('launchCreation', function(event, args) {

        $rootScope.$broadcast('launchFromCreator');
        $scope.pcm = factory.createPCM();
        $scope.setEdit(true, false);
        $scope.initializeEditor($scope.pcm, $scope.metadata, false, true);
        $scope.pcm.name = args.title;

        for(var i = 0; i < args.rows; i++) {
            var productName = "Product " + (i + 1);
            $scope.addProduct(productName);
        }

        for(var j = 0; j < args.columns; j++) {
            var featureName = "Feature " + (j + 1);
            $scope.addFeature(featureName);
        }
    });


    /**
     * Bind events from toolbar to functions of the editor
      */

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
        $scope.setEdit(args[0], args[1]);
    });

    /**
     * Launch initialization when importing
     */
    $scope.$on('import', function(event, args) {
        $scope.pcm = loader.loadModelFromString(JSON.stringify(args.pcm)).get(0);
        pcmApi.decodePCM($scope.pcm);
        $scope.metadata = args.metadata;
        $scope.initializeEditor($scope.pcm, $scope.metadata);
    });


    /**
     * Launch Exportation
     */
    $scope.$on('export', function (event, args) {
        var pcmToExport = convertGridToPCM($scope.pcmData);
        $scope.metadata = generateMetadata($scope.pcmData, $scope.gridOptions.columnDefs);
        var jsonModel = JSON.parse(serializer.serialize(pcmToExport));
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
