/**
 * Created by hvallee on 6/19/15.
 */

pcmApp.controller("ConfiguratorCtrl", function($rootScope, $scope, editorUtil, typeService) {

    $scope.data = {};
    $scope.slider = [];
    $scope.booleanFeatures = [];
    $scope.stringFeatures  = [];
    $scope.numberFeatures = [];

    $scope.booleanFilteredFeatures = [];
    $scope.stringFilteredFeatures = [];
    $scope.numberFilteredFeatures = [];

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

    $scope.updateFilterWithThisFeature = function(feature, type, value) {
        switch(type) {
            case 'boolean':
                if(!$scope.booleanFilteredFeatures[feature]) {

                    $scope.booleanFilteredFeatures[feature] = true;
                }
                else {
                    delete $scope.booleanFilteredFeatures[feature];
                }
                break;
            case 'string':
                if(!$scope.stringFilteredFeatures[feature]) {

                    $scope.stringFilteredFeatures[feature] = [];
                    $scope.stringFilteredFeatures[feature].push(value);
                }
                else {
                    var index = $scope.stringFilteredFeatures[feature].indexOf(value);
                    if(index == -1) {
                        $scope.stringFilteredFeatures[feature].push(value);
                    }
                    else {
                        $scope.stringFilteredFeatures[feature].splice(index, 1);
                    }
                }

        }

    };

    $scope.isInFilter = function(product) {
        /* Check for boolean filters */
        for(var filteredFeature in $scope.booleanFilteredFeatures) {
            if(filteredFeature != "move") {
                if(! (typeService.getBooleanValue(product[filteredFeature]) == 'yes')) {
                    return false;
                }
            }
        }
        for(var filteredFeature in $scope.stringFilteredFeatures) {
            if(filteredFeature != "move") { console.log($scope.stringFilteredFeatures[filteredFeature]);
                if($scope.stringFilteredFeatures[filteredFeature].length > 0 && $scope.stringFilteredFeatures[filteredFeature].indexOf(product[filteredFeature]) == -1) {
                    return false;
                }
            }
        }
        /*for(var filteredFeature in $scope.numberFilteredFeatures) {
            if(filteredFeature != "move") {
                if(! (typeService.getBooleanValue(product[filteredFeature]) == 'yes')) {
                    return false;
                }
            }
        }*/
        return true;
    };


});





