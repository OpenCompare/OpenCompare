/**
 * Created by hvallee on 8/4/15.
 */

pcmApp.controller("CreatorCtrl", function($rootScope, $scope) {

    $scope.title = "";
    $scope.rows = 1;
    $scope.columns = 1;
    $scope.loading = false;


    $(window).load(function() {
        $('#modalCreator').modal('show');
    });

    $scope.launchCreation = function() {
        $rootScope.$broadcast('launchCreation', {"title": $scope.title, "rows": $scope.rows, "columns":  $scope.columns});
    }

});