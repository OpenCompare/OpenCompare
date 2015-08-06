/**
 * Created by hvallee on 6/19/15.
 */

pcmApp.controller("ConfiguratorCtrl", function($rootScope, $scope, editorUtil, typeService) {

    $scope.data = {};
    $scope.slider = [];
    $scope.booleanFeatures = [];
    $scope.stringFeatures  = [];
    $scope.numberFeatures = [];

    $scope.filteredFeatures = [];

    $scope.$on('initConfigurator', function(event, args) {
        var features = args.features;
        $scope.data = args.pcmData;

        features.forEach(function (feature) {
            switch(feature.type) {
                case 'string':
                    $scope.stringFeatures.push(feature);
                    break;
                case 'boolean':
                    $scope.booleanFeatures.push(feature);
                    break;
                case 'number':
                    $scope.numberFeatures.push(feature);
                    var minAndMax =  editorUtil.findMinAndMax(feature.name, $scope.data);
                    $scope.slider[feature.name] = {};
                    $scope.slider[feature.name].options = {
                        range: true,
                        min: minAndMax[0],
                        max: minAndMax[1]
                    };
                    $scope.slider[feature.name].values = minAndMax;
                    break;
            }
        });
    });

    $scope.getUniqueValues = function(colName) {
        var uniqueValues = [];
        $scope.data.forEach(function (product) {
            var productValue = product[colName];
            var index = uniqueValues.indexOf(productValue);
            if(index == -1) {
                uniqueValues.push(productValue);
            }
        });
        return uniqueValues;
    };

    $scope.hasThisFeature = function(value) {
        return typeService.getBooleanValue(value) == 'yes';
    };

    $scope.changeFilterWithThisFeature = function(feature) {
        if(!$scope.filteredFeatures[feature]) {
            $scope.filteredFeatures[feature] = true;
        }
        else {
            delete $scope.filteredFeatures[feature];
        }
    };

    $scope.isInFilter = function(product) {
        /* Check for boolean filters */
        for(var filteredFeature in $scope.boleanfilteredFeatures) {
            if(filteredFeature != "move") {
                if(! (typeService.getBooleanValue(product[filteredFeature]) == 'yes')) {
                    return false;
                }
            }
        }
        for(var filteredFeature in $scope.filteredFeatures) {
            if(filteredFeature != "move") {
                if(! (typeService.getBooleanValue(product[filteredFeature]) == 'yes')) {
                    return false;
                }
            }
        }
        for(var filteredFeature in $scope.filteredFeatures) {
            if(filteredFeature != "move") {
                if(! (typeService.getBooleanValue(product[filteredFeature]) == 'yes')) {
                    return false;
                }
            }
        }
        return true;
    };



});





