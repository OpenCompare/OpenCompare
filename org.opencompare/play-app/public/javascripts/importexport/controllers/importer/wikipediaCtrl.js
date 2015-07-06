/**
 * Created by smangin on 19/05/15.
 */

pcmApp.controller("WikipediaImportController", function($rootScope, $scope, $http, $modalInstance, base64) {

    $scope.pcmContainers = [];
    $scope.pcmContainerNames = [];

    $scope.loading = false;
    $scope.cancel = function() {
        $modalInstance.close();
    };

    // Default values
    $scope.url = "";
    $scope.valid = function(){

        $scope.loading = true;

        $http.post(
            "/api/import/wikipedia",
            {
                url: $scope.url
            })
            .success(function(response, status, headers, config) {
                $scope.loading = false;
                $scope.pcmContainers = response;

                if (response.length === 1) {
                    $scope.selectPCM(0);
                } else {
                    $scope.pcmContainers.forEach(function (pcmContainer, containerIndex){
                        $scope.pcmContainerNames.push({
                            name: base64.decode(pcmContainer.pcm.name),
                            index: containerIndex
                        });
                    });
                }

            }).error(function(data, status, headers, config) {
                $scope.loading = false;
                $scope.message = data
            });
    };

    $scope.selectPCM = function(index) {
        var selectedPCMContainer = $scope.pcmContainers[index];
        $rootScope.$broadcast('import', selectedPCMContainer);
        $modalInstance.close();
    };
});





