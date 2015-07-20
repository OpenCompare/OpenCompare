/**
 * Created by hvallee on 7/6/15.
 */

pcmApp.controller("OpenCompareCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, $compile, $modal, embedService) {

    embedService.enableEdit(true).set();
    embedService.enableShare(true).set();
    embedService.enableExport(true).set();

});