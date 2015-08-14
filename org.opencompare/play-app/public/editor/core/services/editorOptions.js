/**
 * Created by hvallee on 7/2/15.
 */


pcmApp.service('editorOptions', function($rootScope) {

    this.initialize = function(data) {
        $rootScope.$broadcast('initializeFromExternalSource', data);
    };

    this.initializeFromArray = function(data) {
        $rootScope.$broadcast('initializeFromArray', data);
    };

    var enableEdit = true;
    this.enableEdit = function(bool) {
        return{
            get: enableEdit,
            set: function() {
                enableEdit = bool;
            }
        }
    };

    var enableExport = true;
    this.enableExport = function(bool) {
        return{
            get: enableExport,
            set: function() {
                enableExport = bool;
            }
        }
    };

    var enableShare = true;
    this.enableShare = function(bool) {
        return{
            get: enableShare,
            set: function() {
                enableShare = bool;
            }
        }
    };

    var setEdit = true;
    this.setEdit = function(bool) {
        return{
            get: setEdit,
            set: function() {
                setEdit = bool;
            }
        }
    };


});
