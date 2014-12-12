/**
 * Created by gbecan on 12/12/14.
 */

var pcmApp = angular.module("pcmApp", []);

pcmApp.controller("PCMViewerController", function($scope, $http) {
    $scope.name = "Toto";
    $scope.id = id;

    $http.get("/get/" + id).success(function(data) {
        $scope.pcm = data
    });



//    $('#viewer').DataTable( {
//        data: data,
//        columns: [
//            { data: 'name' },
//            { data: 'position' },
//            { data: 'salary' },
//            { data: 'office' }
//        ],
//        "sDom": '<"H"r>t'
//    } );

});

