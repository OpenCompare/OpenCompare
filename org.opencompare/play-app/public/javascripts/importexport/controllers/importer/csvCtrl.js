/**
 * Created by smangin on 19/05/15.
 */


pcmApp.controller("CsvImportController", function($rootScope, $scope, $http, $modalInstance) {

    $scope.loading = false;
    $scope.cancel = function() {
        $modalInstance.close();
    };

    // Default values
    $scope.file = null;
    $scope.title = "";
    $scope.productAsLines = true;
    $scope.separator = ',';
    $scope.quote = '"';

    $scope.valid = function(){
        // Request must be a multipart form data !
        var fd = new FormData();
        fd.append('file', $scope.file);
        fd.append('title', $scope.title);
        fd.append('productAsLines', $scope.productAsLines);
        fd.append('separator', $scope.separator);
        fd.append('quote', $scope.quote);

        $scope.loading = true;

        $http.post(
            "/api/import/csv",
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

