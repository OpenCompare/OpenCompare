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


});

