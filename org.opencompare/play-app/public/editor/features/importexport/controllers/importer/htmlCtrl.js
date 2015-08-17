/**
 * Created by smangin on 19/05/15.
 */


pcmApp.controller("HtmlImportController", function($rootScope, $scope, $http, $modalInstance) {

    $scope.loading = false;
    $scope.cancel = function() {
        $modalInstance.close();
    };

    // Default values
    $scope.file = null;
    $scope.title = "";
    $scope.productAsLines = true;

    $scope.valid = function(){
        // Request must be a multipart form data !
        var fd = new FormData();
        var blob = new Blob([$scope.content], {type: "text/html"});
        fd.append("file", blob);
        fd.append('title', $scope.title);
        fd.append('productAsLines', $scope.productAsLines);

        $scope.loading = true;

        $http.post(
            "/api/import/html",
            fd,
            {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
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