/**
 * Created by gbecan on 17/12/14.
 */


pcmApp.controller("PCMEditorController", function($rootScope, $scope, $http, uiGridConstants) {

    // Load PCM
    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();

    $scope.gridOptions = {
        columnDefs: [],
        data: 'pcmData',
        //enableSorting: true
        onRegisterApi: function( gridAPI ) {
            $scope.gridAPI = gridAPI;
        }
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
        $scope.gridOptions.columnDefs = pcm.features.array.map(function (feature) {
            return {
                name: feature.name,
                enableCellEdit: true,
                enableSorting: true,
                enableHiding: true
            }
        });

        // Row header
        var cellTemplate = 'ui-grid/selectionRowHeader';
        $scope.gridAPI.core.addRowHeaderColumn( { name: 'rowHeaderCol', displayName: 'Product', cellTemplate: cellTemplate} );

    }


    /**
     * Save PCM on the server
     */
    $scope.save = function() {
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

    $scope.$on('validate', function(event, args) {
        $scope.validate();
    });

});
	



