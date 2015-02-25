/**
 * Created by gbecan on 17/12/14.
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

    // Load PCM
    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();
    var temp1; //Temp for HandsOnTable
    // Init
    var features = [];
    var featureHeaders = [];
    var productHeaders = [];
    var products = [];
    var exp;
    var ipValidatorRegexp = /^(?:\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\b|null)$/,
    number = /[0-9]+/,
    bool=/(Yes|No)/,
    text = /[a-z]+/;

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

        initializeHOT();

    } else {

        $http.get("/api/get/" + id).success(function (data) {
            $scope.pcm = loader.loadModelFromString(JSON.stringify(data)).get(0);

            initializeHOT();

        });

    }

    //Function to get a random number between [min-max]
    function getRandomNumber(min, max) {
        return Math.random() * (max - min) + min;
    }

   /**
   * Get a random type :
   * 1: Number
   * 2: Bool (Yes/No)
   * 3: Text
   */
    var getType= function(value){
        switch (value){
            case 0:
                return number;
            case 1:
                return bool;
            case 2:
                return text;
            defaul:
                return text;
        }
    }


    function initializeHOT() {
        // Transform features to handonstable data structures
        var kFeatures = $scope.pcm.features.array.sort(sortByName);

        for (var i = 0; i < kFeatures.length; i++) {

            features.push({
                // Associate a type to a columns
                data: property(kFeatures[i].generated_KMF_ID), validator: getType(Math.round(getRandomNumber(0,2))), allowInvalid: true,color: 'orange'
            });
            console.log(features);
            featureHeaders.push(kFeatures[i].name);

        }
        // Transform products to handonstable data structures
        var kProducts = $scope.pcm.products.array.sort(sortByName);
        for (var i = 0; i < kProducts.length; i++) {
            var product = kProducts[i];

            productHeaders.push(product.name);
            products.push(model(product));
            //console.log(products);
        }

        var container = document.getElementById('hot');
        var hot = new Handsontable(container,
            {
                data: products,
                dataSchema: schema,
                rowHeaders: productHeaders,
                colHeaders: featureHeaders,
                columns: features,
                contextMenu: true,

                //stretchH: 'all', // Scroll bars ?!
                manualColumnMove: true,
                manualRowMove: true
            });
        temp1=hot;
    }

    /**
     * Synchronization function between handsontable and a PCM model of a product
     * @param product : KMF model of a product
     * @returns synchronization object
     */
    function model(product) {
        var idKMF = product.generated_KMF_ID;
        return idKMF;
    }

    function schema() {
        var newProduct = factory.createProduct();
        $scope.pcm.addProducts(newProduct);

        for (var i = 0; i < $scope.pcm.features.array.length; i++) {
            var cell = factory.createCell();
            cell.feature = $scope.pcm.features.array[i];
            cell.content = "";
            newProduct.addValues(cell);
        }

        return model(newProduct);
    }

    /**
     * Bind handsontable cells to PCM cells
     * @param attr
     * @returns synchronization function
     */
    function property(attr) {
        return function (row, value) {
            var product = $scope.pcm.findProductsByID(row);
            //var cell = product.select("values[feature/id == " + attr + "]").get(0); // FIXME : does not work ! We need to find the cell that correponds to the feature id
            var cells = product.values.array;
            for (var i = 0; i < cells.length; i++) {
                var cell = cells[i];
                if (cell.feature.generated_KMF_ID === attr) {
                    break;
                }
            }

            if (typeof value === 'undefined') {
                return cell.content;
            } else {
                cell.content = value;
                return row;
            }
        }
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
            });
        } else {
            $http.post("/api/save/" + id, JSON.parse(jsonModel)).success(function(data) {
                console.log("model saved");
            });
        }
    };
    /**
    *Remove PCM from server
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
    * Validate the type of each columns
    */
    $scope.valider=function(){
        // TO DO
       temp1.setDataAtCell(0, 0, 'new value');
    };

});



