/**
 * Created by smangin on 19/05/15.
 */


pcmApp.controller("PCMImporterController", function($rootScope, $scope, $http, $location) {

    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();

    // ref: http://stackoverflow.com/a/1293163/2343
    // This will parse a delimited string into an array of
    // arrays. The default delimiter is the comma, but this
    // can be overriden in the second argument.
    function CSVToArray( strData, strDelimiter ) {
        // Check to see if the delimiter is defined. If not,
        // then default to comma.
        strDelimiter = (strDelimiter || ",");

        // Create a regular expression to parse the CSV values.
        var objPattern = new RegExp(
            (
                // Delimiters.
                "(\\" + strDelimiter + "|\\r?\\n|\\r|^)" +

                    // Quoted fields.
                "(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +

                    // Standard fields.
                "([^\"\\" + strDelimiter + "\\r\\n]*))"
            ),
            "gi"
        );


        // Create an array to hold our data. Give the array
        // a default empty first row.
        var arrData = [[]];

        // Create an array to hold our individual pattern
        // matching groups.
        var arrMatches = null;


        // Keep looping over the regular expression matches
        // until we can no longer find a match.
        while (arrMatches = objPattern.exec(strData)) {

            // Get the delimiter that was found.
            var strMatchedDelimiter = arrMatches[1];

            // Check to see if the given delimiter has a length
            // (is not the start of string) and if it matches
            // field delimiter. If id does not, then we know
            // that this delimiter is a row delimiter.
            if (
                strMatchedDelimiter.length &&
                strMatchedDelimiter !== strDelimiter
            ) {

                // Since we have reached a new row of data,
                // add an empty row to our data array.
                arrData.push([]);

            }

            var strMatchedValue;

            // Now that we have our delimiter out of the way,
            // let's check to see which kind of value we
            // captured (quoted or unquoted).
            if (arrMatches[2]) {

                // We found a quoted value. When we capture
                // this value, unescape any double quotes.
                strMatchedValue = arrMatches[2].replace(
                    new RegExp("\"\"", "g"),
                    "\""
                );

            } else {

                // We found a non-quoted value.
                strMatchedValue = arrMatches[3];

            }


            // Now that we have our value string, let's add
            // it to the data array.
            arrData[arrData.length - 1].push(strMatchedValue);
        }
        // Return the parsed data.
        return( arrData );
    }

    function convertCsvToPCM(title, file, separator, header) {
        var pcm = factory.createPCM();
        pcm.name = title;

        array_pcm = CSVToArray(file, separator)
        var featuresMap = [];
        if (header) {
            var headers = array_pcm[0];
            delete array_pcm[0];
            var i = 0;
            headers.forEach(function(name) {
                // Create feature if not exsisting
                if (!featuresMap.hasOwnProperty(name)) {
                    var feature = factory.createFeature();
                    feature.name = name;
                    pcm.addFeatures(feature);
                    featuresMap[i] = feature;
                }
                i += 1;
            });
        }

        array_pcm.forEach(function(productData) {
            // Create product
            var product = factory.createProduct();

            // Create cells
            var i = 0;
            productData.forEach(function(featureData) { // FIXME : order is not preserved
                var feature = featuresMap[i];
                // Create cell
                var cell = factory.createCell();
                cell.feature = feature;
                cell.content = featureData;
                product.addValues(cell);
                i += 1;
            })
            pcm.addProducts(product);
        });
        return pcm;
    }

    /**
     * Validate
     */
    $scope.confirm = function() {
        var f = document.getElementById('import_file').files[0];
        var title = document.getElementById('pcm_title').value;
        var separator = document.getElementById('csv_separator').value;
        var header = document.getElementById('csv_header').value;
        var r = new FileReader();
        // FileReader is asynchronous.
        // The handler is mandatory.
        // Prefered 'onloadend' which is called whatever appends (success, error)
        r.onloadend = function(evt) {
            var data = evt.target.result;
            var pcm = convertCsvToPCM(title, data, separator, header)
            var jsonModel = serializer.serialize(pcm);
            console.log(jsonModel);
            $http.post("/api/create", JSON.parse(jsonModel)).success(function(data) {
                id = data;
                console.log("model created with id=" + id);
                $location.path('/list');
            });
        };
        r.readAsText(f);

    };

});
	



