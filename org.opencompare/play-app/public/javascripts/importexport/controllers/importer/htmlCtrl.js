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
        fd.append('file', $scope.file);
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
                var pcmContainer = response[0];
                $rootScope.$broadcast('import', pcmContainer);
                $modalInstance.close();
            }).error(function(data, status, headers, config) {
                $scope.loading = false;
                $scope.message = data
            });

    }
});

