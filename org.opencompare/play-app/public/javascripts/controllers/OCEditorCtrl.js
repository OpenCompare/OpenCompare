/**
 * Created by gbecan on 9/16/15.
 */
angular.module("openCompare")
    .controller("OCEditorCtrl", function($rootScope, $scope, $modal, pcmApi, openCompareServer) {

        openCompareServer.useLocalServer();
        $scope.config = {
            serverMode: "local"
        };
        $scope.pcmContainer = {};

        if (typeof id !== 'undefined') {
            /* Load a PCM from database */
            $scope.id = id;
            $scope.loading = true;
            //$scope.setEdit(false, false);
            //$scope.updateShareLinks();
            openCompareServer.get("/api/get/" + $scope.id).
                then(function (response) {
                    var data = response.data;
                    var pcm = pcmApi.loadPCMModelFromString(JSON.stringify(data.pcm));
                    pcmApi.decodePCM(pcm); // Decode PCM from Base64

                    $scope.pcmContainer.pcm = pcm;
                    $scope.pcmContainer.metadata = data.metadata;
                    $scope.pcmContainer.id = id;

                }, function(error) {
                    console.log(error);
                })
                .finally(function () {
                    $scope.loading = false;
                })
        }



        if (typeof data !== 'undefined') {
            var pcm = pcmApi.loadPCMModelFromString(JSON.stringify(JSON.parse(data).pcm)); // FIXME : mmhh ugly...
            pcmApi.decode(pcm);
            $scope.pcmContainer.pcm = pcm;
        }

        /* Load modal for import */
        if (typeof modal != 'undefined') {
            // Open the given modal
            $modal.open({
                templateUrl: "templates/modal/modal" + modal + "Import.html",
                controller: modal + "ImportCtrl",
                scope: $scope
            })
        }



    });