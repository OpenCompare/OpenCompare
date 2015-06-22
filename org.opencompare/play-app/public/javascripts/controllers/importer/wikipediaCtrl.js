/**
 * Created by smangin on 19/05/15.
 */

pcmApp.controller("WikipediaImportController", function($rootScope, $scope, $http, $modalInstance) {
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
                $rootScope.$broadcast('import', response);
                $modalInstance.close();
            }).error(function(data, status, headers, config) {
                $scope.loading = false;
                $scope.message = data
            });
    }
});





