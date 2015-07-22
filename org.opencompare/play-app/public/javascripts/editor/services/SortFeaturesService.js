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
        var featureGroupsPos = [];
        var currentIndex =0;

        for(var i = 0; i < featureGroups.length; i++) {
            if(featureGroups[i].hasOwnProperty('name')) {
                var featureGroup = [];
                featureGroup.push(featureGroups[i].name);
                featureGroup.push(currentIndex);
                featureGroupsPos[currentIndex] = featureGroup;
                currentIndex++;
            }
        }
        sortedFeatures.splice(0, 0, features[0]);
        sortedFeatures.splice(1, 0, features[1]);

        for(var i = 2; i < features.length; i++) {
            var currentfeature = features[i];
            var index = 0;
            for(var j = 0; j < featureGroupsPos.length; j++) {
                if(featureGroupsPos[j][0] == currentfeature.superCol) {
                    index = j;
                    break;
                }
            }
            var position = featureGroupsPos[index][1];
            console.log(currentfeature.name);

            for(var k = index; k < featureGroupsPos.length; k++) {
                featureGroupsPos[k][1] = featureGroupsPos[k][1]+1;
            }
            sortedFeatures.splice(position, 0, currentfeature);
            console.log(sortedFeatures);
        }
        return sortedFeatures;
    };

});

