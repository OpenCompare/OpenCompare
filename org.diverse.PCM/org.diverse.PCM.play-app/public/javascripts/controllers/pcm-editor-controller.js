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

    $http.get("/get/" + $scope.id).success(function(data) {
        $scope.pcm = loader.loadModelFromString(JSON.stringify(data)).get(0);

        // Test handsontable
        var container = document.getElementById('hot');



        var features = [];
        var featureHeaders = [];

        function sortByName(a, b) {
            if (a.name < b.name) {
                return -1;
            } else if (a.name > b.name) {
                return 1;
            } else {
                return 0;
            }
        }

        var kFeatures = $scope.pcm.features.array.sort(sortByName);
        for (var i = 0; i <  kFeatures.length; i++) {
            features.push({
                data: property(kFeatures[i].name)
            });
            featureHeaders.push(kFeatures[i].name);
        }

        var productHeaders = [];
        var products = [];

        var kProducts = $scope.pcm.products.array.sort(sortByName);
        for (var i = 0; i < kProducts.length; i++) {
            var product = kProducts[i];

            productHeaders.push(product.name);
            products.push(model(product));

        }

        function model(product) {
            var _pub = {};
            var _priv = {};

            if (typeof  product === 'undefined') {
                return _pub;
            }

            var kCells = product.values.array.sort(function (a, b) { sortByName(a.feature, b.feature) });
            for (var j = 0; j < kCells.length; j++) {
                var kCell = kCells[j];
                _priv[kCell.feature.name] = kCell.content;
            }

            _pub.attr = function (attr, val) {
                if (typeof val === 'undefined') {
                    return _priv[attr];
                } else {
                    _priv[attr] = val;

                    var kCells = product.values.array;
                    for (var j = 0; j < kCells.length; j++) {
                        var kCell = kCells[j];
                        if (kCell.feature.name == attr) {
                            kCell.content = val;
                        }
                    }

                    return _pub;
                }
            };

            return _pub;
        }

        function property(attr) {
            return function (row, value) {
                return row.attr(attr, value);
            }
        }

        var hot = new Handsontable(container,
            {
                data: products,
                dataSchema: model,
                rowHeaders: productHeaders,
                colHeaders: featureHeaders,
                columns: features,
                contextMenu: true,
                //stretchH: 'all', // Scroll bars ?!
                manualColumnMove: true,
                manualRowMove: true
            });


    });



    $scope.save = function() {
        var jsonModel = serializer.serialize($scope.pcm);

        $http.post("/save/" + $scope.id, JSON.parse(jsonModel)).success(function(data) {
            console.log("model saved");
        });

    };

});



