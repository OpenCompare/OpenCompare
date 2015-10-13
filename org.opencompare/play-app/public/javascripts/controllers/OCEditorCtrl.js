/**
 * Created by gbecan on 9/16/15.
 */
angular.module("openCompare")
    .controller("OCEditorCtrl", function($rootScope, $scope, $modal, pcmApi, openCompareServer) {

        openCompareServer.useLocalServer();
        $scope.config = {
            serverMode: "local"
        };
        $scope.state = {};
        $scope.pcmContainer = {};

        $scope.$watch("pcmContainer.metadata.source", function(newSource) {
            if (typeof newSource !== "undefined") {
                var link = document.createElement("a");
                link.href = newSource;

                if (newSource.indexOf("wikipedia.org") !== -1) {
                    $scope.sourceType = "wikipedia";
                    $scope.sourceName = link.pathname;
                } else if ((newSource.indexOf("http://") !== -1) || (newSource.indexOf("https://") !== -1)) {
                    $scope.sourceType = "url";
                    $scope.sourceName = link.hostname + link.pathname;
                } else {
                    $scope.sourceType = "text";
                }
            }
        });

        $scope.$watch("pcmContainer.metadata.license", function(newLicense) {
            if (typeof newLicense !== "undefined") {
                if (newLicense.toLowerCase().indexOf("creative commons") !== -1) {
                    $scope.licenseType = "cc";
                } else {
                    $scope.licenseType = "unknown";
                }
            }
        });

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
                    $scope.state.saved = true;

                }, function(error) {
                    console.log(error);
                })
                .finally(function () {
                    $scope.loading = false;
                })
        }



        if (typeof data !== 'undefined') {
            var pcm = pcmApi.loadPCMModelFromString(JSON.stringify(JSON.parse(data).pcm)); // FIXME : mmhh ugly...
            pcmApi.decodePCM(pcm);
            $scope.pcmContainer.pcm = pcm;
        }

        $scope.$watch("csvApi", function(api) {
            if (typeof api !== "undefined" && modal === "Csv") {
                api.open();
            }
        });

        $scope.$watch("htmlApi", function(api) {
            if (typeof api !== "undefined" && modal === "Html") {
                api.open();
            }
        });

        $scope.$watch("mediaWikiApi", function(api) {
            if (typeof api !== "undefined" && modal === "MediaWiki") {
                api.open();
            }
        });

        if (typeof user !== 'undefined') {
            $scope.user = user;
        }

    });