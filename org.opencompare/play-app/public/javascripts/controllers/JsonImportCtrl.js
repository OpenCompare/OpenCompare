angular
  .module('openCompareEditor')
  .controller("JsonImportCtrl", function($rootScope, $scope, openCompareServer, pcmApi, $modal) {

    $scope.loading = false;

    // Default values
    $scope.file = null;
    $scope.title = "";


    $scope.valid = function(){
        // Request must be a multipart form data !
        var fd = new FormData();
        fd.append('file', $scope.file);
        fd.append('title', $scope.title);

        $scope.message = "";
        $scope.loading = true;

      openCompareServer.post(
            "/api/import/json",
            fd,
            {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
            .then(function(response) {
                $scope.loading = false;
                var importedPcmContainer = response.data[0];

                $scope.pcmContainer.pcm = pcmApi.loadPCMModelFromString(JSON.stringify(importedPcmContainer.pcm));
                pcmApi.decodePCM($scope.pcmContainer.pcm);
                $scope.pcmContainer.metadata = importedPcmContainer.metadata;

                $scope.modalInstance.close();
            }, function(response) {
                $scope.loading = false;
                $scope.message = response.data;
            });

    }
});
