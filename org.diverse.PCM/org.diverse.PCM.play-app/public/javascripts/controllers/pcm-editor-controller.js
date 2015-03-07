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
                data: property(kFeatures[i].generated_KMF_ID)
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
           var id = product.generated_KMF_ID;

            // FIXME : this function is also used when creating a new product

            // FIXME : ugly stuff to get and set a value... We need to work with the ID !
//            sync.attr = function (attr, val) {
//                if (typeof val === 'undefined') {
//                    var kCells = product.values.array;
//                    for (var j = 0; j < kCells.length; j++) {
//                        var kCell = kCells[j];
//                        if (kCell.feature.name == attr) {
//                            return kCell.content;
//                        }
//                    }
//                } else {
//                    var kCells = product.values.array;
//                    for (var j = 0; j < kCells.length; j++) {
//                        var kCell = kCells[j];
//                        if (kCell.feature.name == attr) {
//                            kCell.content = val;
//                        }
//                    }
//
//                    return sync;
//                }
//            };

            return id;
        }

		// add product
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
		
		// add feature
		function addFeature () {
			var newFeature = factory.createFeature();
			$scope.pcm.addFeature(newFeature);
			
			for (var i = 0 ; i < $scope.pcm.product.array.length ; i++) {
				var cell = factory.createCell();
				cell.feature = newFeature;
				cell.content = "";
				$scope.pcm.product.array[i].addValues(cell);
			}
			
			return model(newFeature);
		}
		
		// set feature name
		function setFeatureName (name) {
			
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

        // Initialize handsontable
		
		var matrice = {
                data: products,
                dataSchema: schema,
                 rowHeaders: productHeaders,
                 colHeaders: featureHeaders,
                 columns: features,
                 //contextMenu: true,
                 currentRowClassName: 'currentRow', 
                 currentColClassName: 'currentCol',   
                 fixedRowsTop: 0, //fixer les lignes 
                 fixedColumnsLeft: 0, //fixer les colonnes
                 //afterChange :;
              };

        var hot = new Handsontable(container,matrice);
         resize();

    });


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

        $http.post("/save/" + $scope.id, JSON.parse(jsonModel)).success(function(data) {
            console.log("model saved");
        });

    };
});
	



