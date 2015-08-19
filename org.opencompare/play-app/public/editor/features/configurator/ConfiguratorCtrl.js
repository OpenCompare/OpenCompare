/**
 * Created by hvallee on 6/19/15.
 */

pcmApp.controller("ConfiguratorCtrl", function($rootScope, $scope, editorUtil, typeService) {

    $scope.data = {};
    $scope.currentPage = 0;
    $scope.pageSize = 5;

    $scope.limit = {};//Allow limit of displaying items in configurator features

    $scope.productFilter = "";
    $scope.booleanFeatures = [];
    $scope.stringFeatures  = [];
    $scope.numberFeatures = [];

    $scope.booleanFilteredFeatures = [];
    $scope.stringFilteredFeatures = [];
    $scope.slider = [];

    $scope.conf = false;
    $scope.lineView = true;

    $scope.numberOfPages=function(){
        $scope.length = 0;
        if($scope.data.length > 0) {
            $scope.data.forEach(function (product) {
                if($scope.isInFilter(product)) {
                    $scope.length++;
                }
            });
        }
        return Math.ceil($scope.length/$scope.pageSize);
    };

    $scope.$on('setConfiguratorMode', function(event, arg) {
        $scope.conf = arg;
    });

    $scope.$on('setLineView', function(event, arg) {
        $scope.lineView = arg;
    });

    $scope.setLineView = function(bool) {
        $scope.lineView = bool;
        $rootScope.$broadcast('setLineView', bool);
    };

    $scope.$on('initConfigurator', function(event, args) {
        var features = args.features;
        $scope.data = args.pcmData;

        features.forEach(function (feature) {
            switch(feature.type) {
                case 'string':
                    $scope.stringFeatures.push(feature);
                    break;
                case 'bool':
                    $scope.booleanFeatures.push(feature);
                    break;
                case 'num':
                    $scope.numberFeatures.push(feature);
                    var minAndMax =  editorUtil.findMinAndMax(feature.name, $scope.data);
                    $scope.slider[feature.name] = {};
                    $scope.slider[feature.name].values = minAndMax;

                    $scope.slider[feature.name].options = {
                        range: true,
                        min: minAndMax[0],
                        max: minAndMax[1],
                        change: function (ev, ui) {
                            if($scope.conf) {
                                $scope.$emit('updateFilterFromConfigurator', {
                                    "feature": feature.name,
                                    "type": "num",
                                    "values": $scope.slider[feature.name].values
                                });
                            }
                        }
                    };
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
            case 'bool':
                if(!$scope.booleanFilteredFeatures[feature]) {

                    $scope.booleanFilteredFeatures[feature] = true;
                }
                else {
                    delete $scope.booleanFilteredFeatures[feature];
                }
                $scope.$emit('updateFilterFromConfigurator', {"feature": feature, "type": "bool", "values": $scope.booleanFilteredFeatures[feature]});
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
                $scope.$emit('updateFilterFromConfigurator', {"feature": feature, "type": "string", "values": $scope.stringFilteredFeatures[feature]});
                break;
        }
        $scope.currentPage = 0;
    };



    $scope.isInFilter = function(product) {
        /* Check for product filter */

        if($scope.productFilter && product.name.indexOf($scope.productFilter) == -1) {
            return false;
        }
        /* Check for boolean filters */
        for(var filteredFeature in $scope.booleanFilteredFeatures) {
            if(filteredFeature != "move") {
                if(! (typeService.getBooleanValue(product[filteredFeature]) == 'yes')) {
                    return false;
                }
            }
        }
        /* Check for string filters */
        for(var filteredFeature in $scope.stringFilteredFeatures) {
            if(filteredFeature != "move") {
                if ($scope.stringFilteredFeatures[filteredFeature].length > 0 && $scope.stringFilteredFeatures[filteredFeature].indexOf(product[filteredFeature]) == -1) {
                    return false;
                }
            }
        }
        /* Check for number filters */
        for(var filteredFeature in $scope.slider) {
            if($scope.slider[filteredFeature].values && ((parseFloat(product[filteredFeature].replace(/\s/g, "").replace(",", ".")) < $scope.slider[filteredFeature].values[0]) || (parseFloat(product[filteredFeature].replace(/\s/g, "").replace(",", ".")) > $scope.slider[filteredFeature].values[1]))) {

                return false;
            }
        }
        return true;
    };

    $scope.collapseAll = function() {

        $scope.collapseAll = true;
        $scope.limit.boolean = 0;
        $scope.limit.forEach(function(feature) {
           $scope.limit[feature] = 0;
        });

    }


});

pcmApp.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        if(input.length > 0) {
            return input.slice(start);
        }
        else {
            return {};
        }
    }
});




