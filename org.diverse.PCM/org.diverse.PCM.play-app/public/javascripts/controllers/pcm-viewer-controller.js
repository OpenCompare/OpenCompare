/**
 * Created by gbecan on 12/12/14.
 */

var pcmApp = angular.module("pcmApp", []);

pcmApp.controller("PCMViewerController", function($scope, $http) {
    $scope.name = "Toto";
    $scope.id = id;

    // Load PCM
    var pcmMM = Kotlin.modules['pcm'].pcm;
    var factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = factory.createJSONLoader();
    var serializer = factory.createJSONSerializer();

    var pcmToDataTable = function(pcm) {

    }

    $http.get("/get/" + $scope.id).success(function(data) {
        console.log(data);
        $scope.pcm = loader.loadModelFromString(JSON.stringify(data)).get(0);
        $scope.table = pcmToDataTable($scope.pcm);
        console.log($scope.pcm);
        console.log($scope.pcm.metaClassName());
    });



    $scope.save = function() {
        var jsonModel = serializer.serialize($scope.pcm);
        console.log(JSON.parse(jsonModel));

        $http.post("/save/" + $scope.id, JSON.parse(jsonModel)).success(function(data) {
            console.log("model saved");
        });

    };
});

