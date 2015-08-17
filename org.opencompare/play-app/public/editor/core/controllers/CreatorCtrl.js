/**
 * Created by hvallee on 8/4/15.
 * Updated by hvallee on 17/8/15
 */

/**
 * CreatorCtrl.js
 * Use to create a pcm based on a number of rows and columns
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