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

    function compareByFeatureGroup(a,b) {
        if(a.superCol == 'emptyfeatureGroup')
            return 1;
        if(b.superCol == 'emptyFeatureGroup')
            return -1;
        if (a.superCol < b.superCol)
            return -1;
        if (a.superCol > b.superCol)
            return 1;
        return 0;
    }

    this.sortByFeatureGroup = function(features) {
        features.sort(compareByFeatureGroup);

        return features;
    };

    function compareByName(a,b) {
        if(a.name == 'emptyfeatureGroup')
            return 1;
        if(b.name == 'emptyFeatureGroup')
            return -1;
        if (a.name < b.name)
            return -1;
        if (a.name > b.name)
            return 1;
        return 0;
    }

    this.sortFeatureGroupByName = function(featureGroups) {
        featureGroups.sort(compareByName);

        return featureGroups;
    }

});

