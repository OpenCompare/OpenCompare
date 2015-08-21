/**
 * Created by hvallee on 7/6/15.
 */


pcmApp.service('typeService', function(editorUtil) {

    this.getType = function(featureName, data) {
        var rowIndex = 0;
        var isImg = 0;
        var isInt = 0;
        var isBool = 0;
        var isString = 0;
        var codedFeatureName = editorUtil.convertStringToEditorFormat(featureName);
        while(data[rowIndex]) {
            if(data[rowIndex][codedFeatureName]) {
                if(this.isABase64Image(data[rowIndex][codedFeatureName])) {
                    isImg++;
                }
                if (!angular.equals(parseInt(data[rowIndex][codedFeatureName]), NaN)) {
                    isInt++;
                }
                else if (this.isBooleanValue(data[rowIndex][codedFeatureName])) {
                    isBool++;
                }
                else if (!editorUtil.isEmptyCell(data[rowIndex][codedFeatureName])) {
                    isString++;
                }
            }
            rowIndex++;
        }
        var type = "";
        var max = Math.max(isImg, isInt, isBool, isString);
        if(max == 0) {
            type = "string";
        }
        else if(isImg == max) {
            type = "image";
        }
        else if(isInt == max) {
            type = "num";
        }
        else if(isBool == max) {
            type = "bool";
        }
        else {
            type = "string";
        }
        return type;
    };

    this.validateType = function (productName, featureType) {

        var type = "";
        if(!angular.equals(parseInt(productName), NaN)) {
            type = "num";
        }
        else if(this.isBooleanValue(productName)) {
            type = "bool";
        }
        else if(!editorUtil.isEmptyCell(productName)){
            type = "string";
        }
        else {
            type = "none"
        }
        if(type == "none") {
            return true;
        }
        else if (featureType == "string") {
            return true;
        }
        else {
            return type === featureType;
        }
    };

    this.isBooleanValue = function (productName) {

        return((productName.toLowerCase() === "yes") ||  (productName.toLowerCase() === "true") ||  (productName.toLowerCase() === "no") ||  (productName.toLowerCase() === "false"));
    };

    this.getBooleanValue = function(name){

        if(name.toLowerCase() === "yes" || name.toLowerCase() === "true") {
            return "yes";
        }
        else  if(name.toLowerCase() === "no" || name.toLowerCase() === "false") {
            return "no";
        }
        else {
            return "unknown";
        }
    };

    this.isABase64Image = function(value) {
        return(value.indexOf("data:image/png;base64,") != -1);
    }
});

