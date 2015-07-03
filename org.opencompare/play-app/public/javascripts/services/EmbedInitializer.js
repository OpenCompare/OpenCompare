/**
 * Created by hvallee on 7/2/15.
 */


pcmApp.service('opencompareinitializer', function($rootScope) {
    this.initialize = function(data) {
        $rootScope.$broadcast('initializeFromExternalSource', data);
    }


});
