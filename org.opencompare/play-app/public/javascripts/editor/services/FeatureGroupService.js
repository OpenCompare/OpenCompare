/**
 * Created by hvallee on 7/8/15.
 */

pcmApp.service('featureGroupService', function() {

    var currentFeatureGroup = '';

    this.setCurrentFeatureGroup = function(featureGroup) {
        currentFeatureGroup = featureGroup;
    };

    this.getCurrentFeatureGroup = function() {console.log(currentFeatureGroup);
        return currentFeatureGroup;
    }

});

