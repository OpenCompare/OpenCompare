/**

 * Created by gbecan on 17/12/14.

 */

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

    // Init
    var features = [];
    var featureHeaders = [];
    var productHeaders = [];
    var products = [];
    var exp;
    var ipValidatorRegexp = /^(?:\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\b|null)$/,
    number = function(value,callback){
                            if(/[0-9]+/.test(value)){
                            callback(true);
                            }else{
                            callback(false);
                            }
                 },
    bool=function(value,callback){
                              if(/(Yes|No)/.test(value)){
                                  callback(true);
                              }else{
                                  callback(false);
                                     }
                                   },
    text = function(value,callback){
                              if(/[a-z]+/.test(value)){
                                   callback(true);
                              }else{
                                   callback(false);
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
        var kFeatures = getConcreteFeatures($scope.pcm).sort(sortByName); // $scope.pcm.features.array

        for (var i = 0; i < kFeatures.length; i++) {
            var type = getType(Math.round(getRandomNumber(0, 2)));
            features.push({
                // Associate a type to a columns
                data: property(kFeatures[i].generated_KMF_ID), validator: type, allowInvalid: true,
                Type: type + "",
                ID: kFeatures[i].generated_KMF_ID
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
                currentRowClassName: 'currentRow',
                currentColClassName: 'currentCol', 
                contextMenu: contextMenu(),
                //stretchH: 'all', // Scroll bars ?!
                manualColumnMove: true,
                manualRowMove: true,
                minSpareRows: 0,
                minSpareCols: 0,
                minRows:0,
                fixedRowsTop: 0,
                fixedColumnsLeft: 0
            });

        resize();

        $scope.hot = hot;

        function contextMenu() {
            return {
                add_col_before: {
                    name: 'add a column before',
                    callback: function (key, selection) {
                        var header = prompt("Please enter your column name", "");
                        if (header != null) {
                            featureHeaders.splice(selection.start.col, 0, header);
                            var feature = factory.createFeature();
                            feature.name = header;
                            features.splice(selection.start.col, 0, {
                                data: property(feature.generated_KMF_ID),
                                ID: feature.generated_KMF_ID
                            });
                            $scope.pcm.addFeatures(feature);
                            for (var i = 0; i < $scope.pcm.products.array.length; i++) {
                                var cell = factory.createCell();
                                cell.content = "";
                                cell.feature = feature;
                                $scope.pcm.products.array[i].addValues(cell);
                            }

                            hot.render();
                        }
                    },
                    disabled: function () {
                        return false;
                    }
                },
                add_col_after: {
                    name: 'add a column after',
                    callback: function (key, selection) {
                        var header = prompt("Please enter your column name", "");
                        if (header != null) {
                            featureHeaders.splice(selection.end.col + 1, 0, header);
                            var feature = factory.createFeature();
                            feature.name = header;
                            features.splice(selection.end.col + 1, 0, {
                                data: property(feature.generated_KMF_ID),
                                ID: feature.generated_KMF_ID
                            });
                            $scope.pcm.addFeatures(feature);
                            for (var i = 0; i < $scope.pcm.products.array.length; i++) {
                                var cell = factory.createCell();
                                cell.content = "";
                                cell.feature = feature;
                                $scope.pcm.products.array[i].addValues(cell);
                            }

                            hot.render();
                        }
                    },
                    disabled: function () {
                        return false;
                    }
                },
                /*remove_column : {
                 name : 'remove column(s) (DON\'T SAVE !!!)',
                 callback: function (key, selection) {
                 var start = selection.start.col;
                 var end = selection.end.col;
                 for (var i = 0 ; i < $scope.pcm.products.array.length ; i++) {
                 var product = $scope.pcm.products.array[i];
                 var array = product.values.array.slice(0);
                 for (var j = 0 ; j < array.length ; j++) {
                 var cell = array[j];
                 for (var k = start ; k <= end ; k++) {
                 if (cell.feature.generated_KMF_ID == features[k].ID) {
                 product.removeValues(cell);
                 break;
                 }
                 }
                 }
                 }
                 var array = $scope.pcm.features.array.slice(0);
                 for (var i = 0 ; i < array.length ; i++) {
                 var feature = array[i];
                 for (var j = start ; j <= end ; j++) {
                 if (feature.generated_KMF_ID == features[k].ID) {
                 $scope.pcm.removeFeatures(feature);
                 break;
                 }
                 }
                 }
                 features.splice(start, end-start+1);
                 featureHeaders.splice(start, end-start+1);

                 hot.render();
                 },
                 disabled: function () {
                 return false;
                 }
                 },*/
                set_column_name: {
                    name: 'set column name',
                    callback: function (key, selection) {
                        var header = prompt("Please enter your column name", "");
                        if (header != null) {
                            featureHeaders.splice(selection.start.col, 1, header);
                            features[selection.start.col].data.name = header;
                            //$scope.pcm.findFeaturesById(features[selection.start.col.ID]).name = header;

                            var feature;
                            for (var i = 0; i < $scope.pcm.features.array.length; i++) {
                                feature = $scope.pcm.features.array[i];
                                if (feature.generated_KMF_ID == features[selection.start.col].ID) break;
                            }
                            feature.name = header;

                            hot.render();
                        }
                    },
                    disabled: function () {
                        // if multiple columns selected : disable
                        return hot.getSelected()[1] != hot.getSelected()[3];
                    }
                },
                "hsep": "---------",
                add_row_before: {
                    name: 'add a row before',
                    callback: function (key, selection) {
                        var header = prompt("Please enter your row name", "");
                        if (header != null) {
                            productHeaders.splice(selection.start.row, 0, header);
                            var product = factory.createProduct();
                            product.name = header;
                            for (var i = 0; i < $scope.pcm.features.array.length; i++) {
                                var cell = factory.createCell();
                                cell.feature = $scope.pcm.features.array[i];
                                cell.content = "";
                                product.addValues(cell);
                            }
                            $scope.pcm.addProducts(product);
                            products.splice(selection.start.row, 0, model(product));

                            hot.render();
                        }
                    },
                    disabled: function () {
                        return false;
                    }
                },
                add_row_after: {
                    name: 'add a row after',
                    callback: function (key, selection) {
                        var header = prompt("Please enter your row name", "");
                        if (header != null) {
                            productHeaders.splice(selection.end.row + 1, 0, header);
                            var product = factory.createProduct();
                            product.name = header;
                            for (var i = 0; i < $scope.pcm.features.array.length; i++) {
                                var cell = factory.createCell();
                                cell.feature = $scope.pcm.features.array[i];
                                cell.content = "";
                                product.addValues(cell);
                            }
                            $scope.pcm.addProducts(product);
                            products.splice(selection.end.row + 1, 0, model(product));

                            hot.render();
                        }
                    },
                    disabled: function () {
                        return false;
                    }
                },
                remove_row: {
                    name: 'remove row(s)',
                    callback: function (key, selection) {
                        var start = selection.start.row;
                        var end = selection.end.row;

                        var array = $scope.pcm.products.array;
                        for (var i = 0; i < array.length; i++) {
                            var product = array[i];
                            for (var j = start; j <= end; j++) {
                                if (product.generated_KMF_ID == products[j]) {
                                    $scope.pcm.removeProducts(product);
                                    break;
                                }
                            }
                        }

                        products.splice(start, end - start + 1);
                        productHeaders.splice(start, end - start + 1);

                        hot.render();
                    },
                    disabled: function () {
                        return false;
                    }
                },
                set_row_name: {
                    name: 'set row name',
                    callback: function (key, selection) {
                        var header = prompt("Please enter your row name", "");
                        if (header != null) {
                            productHeaders.splice(selection.start.row, 1, header);
                            $scope.pcm.findProductsByID(products[selection.start.row]).name = header;

                            hot.render();
                        }
                    },
                    disabled: function () {
                        // if multiple columns selected : disable
                        return hot.getSelected()[0] != hot.getSelected()[2];
                    }
                },
                "hsep1": "---------",
                validator_numeric: {
                    name: "numeric",
                    callback: function (key, selection) {
                        $scope.pcm.findFeaturesByID(features[selection.end.col].ID).type = type;
                        console.log(features[selection.end.col].validator);
                        features[selection.end.col].Type = number;

                        features[selection.end.col].validator = function (value, callback) {
                            if (number.test(value)) {
                                callback(true);
                            } else {
                                callback(false);
                            }
                        };
                    }
                },

                validator_text: {
                    name: "text",
                    callback: function (key, selection) {
                        $scope.pcm.findFeaturesByID(features[selection.end.col].ID).type = type;
                        console.log(features[selection.end.col].validator);
                        features[selection.end.col].Type = text;
                        features[selection.end.col].validator = text;
                    }
                },
                validator_boolean: {
                    name: "boolean",
                    callback: function (key, selection) {
                        $scope.pcm.findFeaturesByID(features[selection.end.col].ID).type = type;
                        console.log(features[selection.end.col].validator);
                        features[selection.end.col].Type = bool;
                        features[selection.end.col].validator = bool;
                    }
                },
                validator_getype: {
                    name: "get type",
                    callback: function (key, selection) {
                        if (features[selection.end.col].Type == number) {
                            alert("Type : Number");
                        } else if (features[selection.end.col].Type == bool) {
                            alert("Type : Boolean");
                        } else {
                            alert("Type : String");
                        }
                    }
                }
            };

        }
    }


    function getConcreteFeatures(pcm) {

        var aFeatures = pcm.features.array;

        var features = [];
        for (var i = 0; i < aFeatures.length; i++) {
            var aFeature = aFeatures[i];
            features = features.concat(getConcreteFeaturesRec(aFeature))
        }

        return features;
    }

    function getConcreteFeaturesRec(aFeature) {
        var features = [];

        if (typeof aFeature.subFeatures !== 'undefined') {
            var subFeatures = aFeature.subFeatures.array;
            for (var i = 0; i < subFeatures.length; i++) {
                var subFeature = subFeatures[i];
                features = features.concat(getConcreteFeaturesRec(subFeature));
            }
        } else {
            features.push(aFeature);
        }

        return features;
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

    function schema(index) {
        var newProduct = factory.createProduct();
        if(typeof index !== 'undefined') {
            $scope.pcm.addProducts(newProduct);

            for (var i = 0; i < $scope.pcm.features.array.length; i++) {
                var cell = factory.createCell();
                cell.feature = $scope.pcm.features.array[i];
                cell.content = "";
                newProduct.addValues(cell);
            }
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

    function resize()
    {
        var g=document.querySelectorAll("colgroup");

        var i,j,tmp,MaxWidth=0;

        var w=document.getElementById("hot");

        var tab=w.getElementsByClassName("rowHeader");

        for(i=0;i<tab.length;i++)
        {
            span=tab[i];
            width=span.offsetWidth;
            if (width >= MaxWidth)
                MaxWidth=width;
        }

        MaxWidth=MaxWidth+10;//le 10 pour calucler les espaces de debut et la fin de texte

        for(i=0;i<g.length;i++)
        {
            tab=g[i].querySelectorAll("col.rowHeader");
            for(j=0;j<tab.length;j++)
            {
                tab[j].setAttribute("style", "width:"+MaxWidth+"px;");
            }
        }

        return;
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
    $scope.validate=function(){
        // TO DO
       //alert(productHeaders.length);

      /* for(var i=0;i<features.length;i++){
            for(var j=0;j<productHeaders.length;j++){
       //         temp1.setDataAtCell(j, i, temp1.getDataAtCell(j,i));
       }}*/

       //alert("done");
        $scope.hot.validateCells(function(){
            $scope.hot.render()
        });
    };


    // Bind events from toolbar to functions of the editor

    $scope.$on('save', function(event, args) {
        console.log("ok");
        $scope.save();
    });

    $scope.$on('remove', function(event, args) {
        $scope.remove();
    });

    $scope.$on('validate', function(event, args) {
        $scope.validate();
    });

});
	



