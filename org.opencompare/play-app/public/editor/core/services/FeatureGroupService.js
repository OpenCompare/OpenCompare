/**
 * Created by hvallee on 7/23/15.
 */

pcmApp.service('featureGroupService', function() {

    var currentFeatureGroup = '';

    this.setCurrentFeatureGroup = function(featureGroup) {
        currentFeatureGroup = featureGroup;
    };

    this.getCurrentFeatureGroup = function() {
        return currentFeatureGroup;
    };



});