/**
 * Created by gbecan on 9/16/15.
 */
angular.module("openCompare")
    .controller("OCEditorCtrl", function($rootScope, $scope, $modal, pcmApi, openCompareServer) {

        openCompareServer.useLocalServer();

        $scope.data = {
            configuration: {
                serverMode: "local"
            }
        };

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

                    $scope.data.pcm = pcm;
                    $scope.data.metadata = data.metadata;
                    $scope.data.id = id;
                    $scope.state = {
                        saved: true
                    };

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
            $scope.data.pcm = pcm;
        }

        /* Load modal for import */
        if (typeof modal != 'undefined') {
            // Open the given modal
            $modal.open({
                templateUrl: "templates/modal/modal" + modal + ".html",
                controller: modal + "Controller",
                scope: $scope
            })
        }



    });