/**
 * Created by gbecan on 17/12/14.
 */


/**
 * Created by gbecan on 12/12/14.
 */

var pcmApp = angular.module("pcmApp", []);

/**
 * Sort two elements by their names (accessed with x.name)
 * @param a
 * @param b
 * @returns {number}
 */
function sortByName(a, b) {
    if (a.name < b.name) {
        return -1;
    } else if (a.name > b.name) {
        return 1;
    } else {
        return 0;
    }
}

pcmApp.controller("PCMEditorController", function($scope, $http) {
    $scope.id = id;

    // Load PCM
    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();

    $http.get("/get/" + $scope.id).success(function(data) {
        $scope.pcm = loader.loadModelFromString(JSON.stringify(data)).get(0);

        var container = document.getElementById('hot');

        // Transform features to handonstable data structures
        var features = [];
        var featureHeaders = [];

        var kFeatures = $scope.pcm.features.array.sort(sortByName);
        for (var i = 0; i <  kFeatures.length; i++) {
            features.push({
                data: property(kFeatures[i].name)
            });
            featureHeaders.push(kFeatures[i].name);
        }

        // Transform products to handonstable data structures
        var productHeaders = [];
        var products = [];

        var kProducts = $scope.pcm.products.array.sort(sortByName);
        for (var i = 0; i < kProducts.length; i++) {
            var product = kProducts[i];

            productHeaders.push(product.name);
            products.push(model(product));

        }

        /**
         * Synchronization function between handsontable and a PCM model of a product
         * @param product : KMF model of a product
         * @returns synchronization object
         */
        function model(product) {
            var sync = {};

            // FIXME : this function is also used when creating a new product

            // FIXME : ugly stuff to get and set a value... We need to work with the ID !
            sync.attr = function (attr, val) {
                if (typeof val === 'undefined') {
                    var kCells = product.values.array;
                    for (var j = 0; j < kCells.length; j++) {
                        var kCell = kCells[j];
                        if (kCell.feature.name == attr) {
                            return kCell.content;
                        }
                    }
                } else {
                    var kCells = product.values.array;
                    for (var j = 0; j < kCells.length; j++) {
                        var kCell = kCells[j];
                        if (kCell.feature.name == attr) {
                            kCell.content = val;
                        }
                    }

                    return sync;
                }
            };

            return sync;
        }

        /**
         * Bind handsontable cells to PCM cells
         * @param attr
         * @returns synchronization function
         */
        function property(attr) {
            return function (row, value) {
                return row.attr(attr, value);
            }
        }

        // Initialize handsontable
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


    /**
     * Save PCM on the server
     */
    $scope.save = function() {
        var jsonModel = serializer.serialize($scope.pcm);

        $http.post("/save/" + $scope.id, JSON.parse(jsonModel)).success(function(data) {
            console.log("model saved");
        });

    };

});



