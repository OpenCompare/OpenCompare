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


    function initializeHOT() {
        // Transform features to handonstable data structures
        var kFeatures = getConcreteFeatures($scope.pcm).sort(sortByName); // $scope.pcm.features.array
        for (var i = 0; i < kFeatures.length; i++) {
            features.push({
                data: property(kFeatures[i].generated_KMF_ID),
		ID : kFeatures[i].generated_KMF_ID
            });
            featureHeaders.push(kFeatures[i].name);
        }

        // Transform products to handonstable data structures
        var kProducts = $scope.pcm.products.array.sort(sortByName);
        for (var i = 0; i < kProducts.length; i++) {
            var product = kProducts[i];

            productHeaders.push(product.name);
            products.push(model(product));

        }

        var container = document.getElementById('hot');
        var hot = new Handsontable(container,
            {
                data: products,
                //dataSchema: schema,
                rowHeaders: productHeaders,
                colHeaders: featureHeaders,
                columns: features,
                //contextMenu: true,
                contextMenu: contextMenu(),
                //stretchH: 'all', // Scroll bars ?!
                manualColumnMove: true,
                manualRowMove: true
            });

	  function contextMenu () {
		  return {
		  /*add_col : {
			  name : 'add a column (DON\'T SAVE !!!)',
			  callback: function (key, selection) {
			      var header = prompt("Please enter your column name", "");
			      if (header != null) {
				  featureHeaders.push(header);
				  var feature = factory.createFeature();
				  features.push({
				      data: feature,
				      ID : feature.generated_KMF_ID
				  });
				  for (var i = 0 ; i < $scope.pcm.products.array.length ; i++) {
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
		  },*/
		  remove_column : {
			  name : 'remove colomn(s)',
			  callback: function (key, selection) {
			      var start = selection.start.col;
			      var end = selection.end.col;
			      for (var i = 0 ; i < $scope.pcm.products.array.length ; i++) {
				  var product = $scope.pcm.products.array[i];
				  var array = product.values.array;
				  for (var j = 0 ; j < array.length ; j++) {
				      var cell = array[j];
				      for (var k = start ; k <= end ; k++) {
					  if (cell.feature.generated_KMF_ID == features[k].ID) {
					      alert(array.length);
					      product.removeValues(cell);
					      alert(array.length);
					      break;
					  }
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
		  },
		  set_column_name : {
			  name : 'set column name',
			  callback: function (key, selection) {
				  var header = prompt("Please enter your column name", "");
				  if (header != null) {
				      featureHeaders.splice(selection.start.col, 1, header);
				      features[selection.start.col].data.name = header;
				      //$scope.pcm.findFeaturesById(features[selection.start.col].ID).name = header;
				      
				      var feature;
				      for (var i = 0 ; i < $scope.pcm.features.array.length ; i++) {
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
		  add_row : {
			  name : 'add a row',
			  callback: function (key, selection) {
			      var header = prompt("Please enter your row name", "");
			      if (header != null) {
				  productHeaders.push(header);
				  var product = factory.createProduct();
				  product.name = header;
				  for (var i = 0; i < $scope.pcm.features.array.length; i++) {
				      var cell = factory.createCell();
				      cell.feature = $scope.pcm.features.array[i];
				      cell.content = "";
				      product.addValues(cell);
				  }
				  $scope.pcm.addProducts(product);
				  products.push(model(product));
				  
				  hot.render();
			      }
			  },
			  disabled: function () {
			      return false;
			  }
		  },
		  remove_row : {
			  name : 'remove row(s)',
			  callback: function (key, selection) {
			      var start = selection.start.row;
			      var end = selection.end.row;
			      
			      var array = $scope.pcm.products.array;
			      for (var i = 0 ; i < array.length ; i++) {
				  var product = array[i];
				  for (var j = start ; j <= end ; j++) {
				      if (product.generated_KMF_ID == products[j]) {
					  $scope.pcm.removeProducts(product);
					  break;
				      }
				  }
			      }
			      
			      products.splice(start, end-start+1);
			      productHeaders.splice(start, end-start+1);
				  
			      hot.render();
			  },
			  disabled: function () {
			      return false;
			  }
		  },
		  /*set_row_name : {
			  name : 'set row name',
			  callback: function (key, selection) {
				  var header = prompt("Please enter your row name", "");
				  if (header != null) {
				      productHeaders.splice(selection.start.row, 1, header);
				      //products[selection.start.row].data.name = header;
				      //$scope.pcm.findproductsById(products[selection.start.col].ID).name = header;
				      
				      var product;
				      for (var i = 0 ; i < $scope.pcm.products.array.length ; i++) {
					  product = $scope.pcm.products.array[i];
					  if (product.generated_KMF_ID == products[selection.start.row].ID) break;
				      }
				      product.name = header;
				      
				      hot.render();
				  }
			  },
			  disabled: function () {
			      // if multiple columns selected : disable
			      return hot.getSelected()[0] != hot.getSelected()[2];
			  }
		  },*/
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
            var cells = product.values.array
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

    $scope.remove = function() {
        if (typeof id !== 'undefined') {
            $http.get("/api/remove/" + id).success(function(data) {
                window.location.href = "/";
                console.log("model removed");
            });
        }
    };

});



