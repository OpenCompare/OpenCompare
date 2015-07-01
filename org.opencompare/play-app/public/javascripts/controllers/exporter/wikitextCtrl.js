/**
 * Created by smangin on 19/05/15.
 */

pcmApp.controller("WikitextExportController", function($rootScope, $scope, $http, $modal, $modalInstance) {
    $scope.loading = false;
    $scope.cancel = function() {
        $modalInstance.close();
    };

    // Default values
    $scope.title = "";
    $scope.export_content = "";
    $scope.productAsLines = true;

    $scope.valid = function(){

        $scope.export_content = "";
        $scope.loading = true;

        $http.post(
            "/api/export/wikitext",
            {
                file: JSON.stringify($scope.pcmObject),
                productAsLines: $scope.productAsLines
            }, {
                responseType: "text/plain",
                transformResponse: function(d, e) { // Needed to not interpret matrix as json (begin with '{|')
                    return d;
                }
            })
            .success(function(response, status, headers, config) {
                $scope.loading = false;
                $scope.export_content = response;
            }).error(function(data, status, headers, config) {
                $scope.loading = false;
                console.log(data)
            });
    }
});





