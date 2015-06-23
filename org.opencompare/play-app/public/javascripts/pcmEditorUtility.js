/**
 * Created by hvallee on 6/19/15.
 */


    function getCellClass (value) {
        if(value) {
            if(value.toLowerCase().indexOf('{{yes') != -1) {
                return 'yesCell';
            }
            else if(value.toLowerCase().indexOf('{{no') != -1) {
                return 'noCell';
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    function getCellTooltip (value) {

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

    }

    function sortProducts(products, position) {
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
    }

    function sortRawProducts(rawProducts, products) {
        var sortedProducts = [];
        products.forEach(function (product) {
            rawProducts.forEach(function (rawProduct) {
                if (rawProduct.name == product.name) {
                    sortedProducts.push(rawProduct);
                }
            });
        });
        return sortedProducts;
    }

    function sortFeatures(columns, position){
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
                var featureName = convertStringToEditorFormat(position[i].feature.toString());
                if(position[i].feature == "") {
                    featureName = " ";
                }
                if(featureName == feature.name) {
                    sortedColumns.push(feature);
                }
            });
        }
        return sortedColumns;
    }

    function convertStringToEditorFormat(name) {

        return name.replace(/\(/g, '%28').replace(/\)/g, '%29');
    }

    function convertStringToPCMFormat(name) {

        return name.replace(/%28/g, '(').replace(/%29/g, ')');
    }

    function findMinAndMax(featureName, data) {

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
    }

    /* Move object in array */
    Array.prototype.move = function (old_index, new_index) {

        if (new_index >= this.length) {
            var k = new_index - this.length;
            while ((k--) + 1) {
                this.push(undefined);
            }
        }
        this.splice(new_index, 0, this.splice(old_index, 1)[0]);
        return this;
    };

    function isEmptyCell(name) {

        return (!name.toLowerCase()
        || name.toLowerCase() == ""
        || name.toLowerCase() == "N/A"
        || name.toLowerCase() == "?"
        || name.toLowerCase() == "unknown");
    }

    function checkIfNameExists(name, columns) {

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
    }

    function GetUrlValue(VarSearch){
        var SearchString = document.location.search.substring(1);
        var VariableArray = SearchString.split('&');
        for(var i = 0; i < VariableArray.length; i++){
            var KeyValuePair = VariableArray[i].split('=');
            if(KeyValuePair[0] == VarSearch){
                return KeyValuePair[1];
            }
        }
    }


