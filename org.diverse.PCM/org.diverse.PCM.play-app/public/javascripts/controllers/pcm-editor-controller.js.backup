/**
 * Created by gbecan on 17/12/14.
 */


/**
 * Created by gbecan on 12/12/14.
 */

var pcmApp = angular.module("pcmApp", []);

pcmApp.controller("PCMEditorController", function($scope, $http) {
    $scope.id = id;

    // Load PCM
    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();

    /**
     * Convert PCM to HTML
     * @param pcm
     * @returns {{name: *, features: Array, products: Array}}
     */
    var pcmToDataTable = function(pcm) {

        var name = pcm.name;

        var kFeatures = pcm.features.array.sort(function (a, b) {
            if (a.name < b.name) {
                return -1;
            } else if (a.name > b.name) {
                return 1;
            } else {
                return 0;
            }
        });

        var features = [];
        for (var i = 0; i <  kFeatures.length; i++) {
            features.push({
                name: kFeatures[i].name
            });
        }

        var kProducts = pcm.products.array.sort(function (a, b) {
            if (a.name < b.name) {
                return -1;
            } else if (a.name > b.name) {
                return 1;
            } else {
                return 0;
            }
        });

        var products = [];
        for (var i = 0; i < kProducts.length; i++) {
            var product = kProducts[i];

            var kCells = product.values.array.sort(function (a, b) {
                if (a.feature.name < b.feature.name) {
                    return -1;
                } else if (a.feature.name > b.feature.name) {
                    return 1;
                } else {
                    return 0;
                }
            });

            var cells = [];
            for (var j = 0; j < kCells.length; j++) {
                cells.push(kCells[j].content);
            }

            products.push({
                name: product.name,
                cells: cells
            });
        }

        var table = {
            name: name,
            features: features,
            products: products
        };
        return table;
    };


    $http.get("/get/" + $scope.id).success(function(data) {
        $scope.pcm = loader.loadModelFromString(JSON.stringify(data)).get(0);
        $scope.table = pcmToDataTable($scope.pcm);

        // Test handsontable
        var container = document.getElementById('hot');

        var features = [];
        for (var i = 0; i < $scope.table.features.length; i++) {
            features.push($scope.table.features[i].name);
        }

        var products = [];
        var data = [];
        for (var i = 0; i < $scope.table.products.length; i++) {

            products.push($scope.table.products[i].name);

            var cellsData = [];
            for (var j = 0; j < $scope.table.products[i].cells.length; j++) {
                cellsData.push($scope.table.products[i].cells[j]);
            }
            data.push(cellsData);
        }

        var hot = new Handsontable(container,
            {
                data: data,
                rowHeaders: products,
                colHeaders: features,
                contextMenu: true,
                stretchH: 'all', // Scroll bars ?!
                manualColumnMove: true,
                manualRowMove: true
            });
    });



    $scope.save = function() {
        console.log($scope.table);
        var jsonModel = serializer.serialize($scope.pcm);

        $http.post("/save/" + $scope.id, JSON.parse(jsonModel)).success(function(data) {
            console.log("model saved");
        });

    };

});



