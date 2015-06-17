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
                $rootScope.$broadcast('import', response);
                $modalInstance.close();
            }).error(function(data, status, headers, config) {
                $scope.loading = false;
                $scope.message = data
            });

    }
});

pcmApp.controller("WikipediaImportController", function($rootScope, $scope, $http, $modalInstance) {
    $scope.loading = false;
    $scope.cancel = function() {
        $modalInstance.close();
    };

    // Default values
    $scope.title = ""
    $scope.valid = function(){

        $scope.loading = true;

        $http.post(
            "/api/import/wikipedia",
            {
                title: $scope.title,
            })
            .success(function(response, status, headers, config) {
                $scope.loading = false;
                $rootScope.$broadcast('import', response);
                $modalInstance.close();
            }).error(function(data, status, headers, config) {
                $scope.loading = false;
                $scope.message = data
            });
    }
});





