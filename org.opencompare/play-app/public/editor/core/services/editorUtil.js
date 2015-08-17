/**
 * Created by hvallee on 7/2/15.
 */


pcmApp.service('editorUtil', function() {

    this.getCellClass = function (value, featureType) {
        if(value && featureType == 'bool') {
            if(value.toLowerCase().indexOf('yes') != -1 || value.toLowerCase().indexOf('oui') != -1) {
                return 'yesCell';
            }
            else if(value.toLowerCase().indexOf('dunno') == -1 && (value.toLowerCase().indexOf('no') != -1 || value.toLowerCase().indexOf('non') != -1)) {
                return 'noCell';
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    };

    this.getFeaturesFromArray = function(data) {
        var features = [];
        data.forEach(function (product) {
            for(var feature in product) {
                if(features.indexOf(feature) == -1 && feature != '$$hashKey') {
                    features.push(feature);
                }
            }
        });
        return features;
    };

    this.getCellTooltip  = function(value) {

        if(value) {
            if(value.toLowerCase().indexOf('<ref') != -1) {
                var index = value.toLowerCase().indexOf('<ref');
                var refPart = value.substring(index+11);
                var endIndex = refPart.replace(/\s/g, '').indexOf('"/>');
                return refPart.substring(0, endIndex);
            }
            else if(value.toLowerCase().indexOf('<ref>{{') != -1) {
                var index = value.toLowerCase().indexOf('<ref>{{');
                var refPart = value.substring(index+11);
                var endIndex = refPart.replace(/\s/g, '').indexOf('}}</ref>');
                return refPart.substring(0, endIndex);
            }
            else if(value.toLowerCase().indexOf('<ref>{{') != -1) {
                var index = value.toLowerCase().indexOf('<ref>{{');
                var refPart = value.substring(index+11);
                var endIndex = refPart.replace(/\s/g, '').indexOf('}}</ref>');
                return refPart.substring(0, endIndex);
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }

    };

    this.sortProducts = function(products, position) {
        var sortedProducts = [];
        position.sort(function (a, b) {
            if(a.position == -1) {
                return 1;
            }
            else if(b.position == -1) {
                return -1;
            }
            else {
                return a.position - b.position;
            }
        });
        for(var i = 0; i < position.length; i++) {
            products.forEach(function (product) {
                if(position[i].product == product.name) {
                    sortedProducts.push(product);
                }
            });
        }
        return sortedProducts;
    };

    this.sortRawProducts = function(rawProducts, products) {
        var sortedProducts = [];
        products.forEach(function (product) {
            rawProducts.forEach(function (rawProduct) {
                if (rawProduct.name == product.name) {
                    sortedProducts.push(rawProduct);
                }
            });
        });
        return sortedProducts;
    };

    this.convertStringToEditorFormat = function(name) {

        return name.replace(/\(/g, '%28').replace(/\)/g, '%29');
    };

    this.convertStringToPCMFormat = function(name) {

        return name.replace(/%28/g, '(').replace(/%29/g, ')');
    };

    this.sortFeatures = function(columns, position){
        var sortedColumns = [];
        position.sort(function (a, b) {
            if(a.position == -1) {
                return 1;
            }
            else if(b.position == -1) {
                return -1;
            }
            else {
                return a.position - b.position;
            }
        });
        for(var i = 0; i < position.length; i++) {
            columns.forEach(function (feature) {
                var featureName = position[i].feature.toString().replace(/\(/g, '%28').replace(/\)/g, '%29');
                if(position[i].feature == "") {
                    featureName = " ";
                }
                if(featureName == feature.name) {
                    sortedColumns.push(feature);
                }
            });
        }
        return sortedColumns;
    };



    this.findMinAndMax = function(featureName, data) {

        var min = 0;
        var max = 0;
        data.forEach(function (product) {
            if(parseInt(product[featureName]) > max) {
                max = parseFloat(product[featureName].replace(/\s/g, "").replace(",", "."));
            }
            if(parseInt(product[featureName]) < min) {
                min = parseFloat(product[featureName].replace(/\s/g, "").replace(",", "."));
            }
        });
        return [min, max];
    };


    this.isEmptyCell = function(name) {

        return (!name.toLowerCase()
        || name.toLowerCase() == ""
        || name.toLowerCase() == "N/A"
        || name.toLowerCase() == "?"
        || name.toLowerCase() == "unknown");
    };

    this.checkIfNameExists = function(name, columns) {

        var newName = "";
        if(!name) {
            newName = "New Feature";
        }
        else {
            newName = name;
        }
        var index = 0;
        columns.forEach(function(featureData) {
            var featureDataWithoutNumbers = featureData.name.replace(/[0-9]/g, '');
            if(featureDataWithoutNumbers === newName ){
                index++;
            }
        });
        if(index != 0) {
            newName = newName + index;
        }
        return newName;
    };

    this.GetUrlValue = function(VarSearch){
        var SearchString = document.location.search.substring(1);
        var VariableArray = SearchString.split('&');
        for(var i = 0; i < VariableArray.length; i++){
            var KeyValuePair = VariableArray[i].split('=');
            if(KeyValuePair[0] == VarSearch){
                return KeyValuePair[1];
            }
        }
    };

    this.getNumberOfFeaturesWithThisFeatureGroup = function(cols, featureGroup) {
        var count = 0;
        for(var i = 0; i < cols.length; i++) {
            if(cols[i].superCol == featureGroup) {
                count ++;
            }
        }
        return count;
    };

});
