/**
 * Created by hvallee on 7/8/15.
 */

pcmApp.service('sortFeaturesService', function() {

    this.sortByType = function(features, featuresTypes) {
        var sortedFeatures = [];
        var stringStartIndex = 0;
        var numberStartIndex = 0;
        var booleanStartIndex = 0;

        for(var i = 0; i < features.length; i++) {
                var currentfeature = features[i];
            switch(featuresTypes[currentfeature.name]) {
                case 'string':
                    sortedFeatures.splice(stringStartIndex, 0, currentfeature);
                    numberStartIndex++;
                    booleanStartIndex++;
                    break;
                case 'number':
                    sortedFeatures.splice(numberStartIndex+1, 0, currentfeature);
                    booleanStartIndex++;
                    break;
                case 'boolean':
                    sortedFeatures.splice(booleanStartIndex, 0, currentfeature);
                    break;
            }
        }
        return sortedFeatures;
    };

    this.sortByFeatureGroup = function(features, featureGroups) {
        var sortedFeatures = [];
        var startIndexes = [];
        var featureGroupsNames = [];
        for(var i = 0; i < featureGroups.length; i++) {
            if(featureGroups[i].hasOwnProperty('name')) {
                var featureName = featureGroups[i].name;
                startIndexes[featureName] = i+2;
                featureGroupsNames[i] = featureName;
            }
        }
        sortedFeatures.splice(0, 0, features[0]);
        sortedFeatures.splice(1, 0, features[1]);
        for(var i = 2; i < features.length; i++) {
            var currentfeature = features[i];
            var position = startIndexes[currentfeature.superCol];
            var index = featureGroupsNames.indexOf(currentfeature.superCol);
            for(var j = index; j < startIndexes.length; j++) {
                startIndexes[j] = startIndexes[j]++;
            }
            sortedFeatures.splice(position, 0, currentfeature);
        }
        return sortedFeatures;
    }


});

