/**
 * Created by smangin on 19/05/15.
 */


pcmApp.controller("CsvExportController", function($rootScope, $scope, $http, $modal, $modalInstance) {

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
    $scope.export_content = "";

    $scope.valid = function(){
        // Request must be a multipart form data !
        var fd = new FormData();
        fd.append('file', $scope.file);
        fd.append('title', $scope.title);
        fd.append('productAsLines', $scope.productAsLines);
        fd.append('separator', $scope.separator);
        fd.append('quote', $scope.quote);

        $scope.export_content = "";
        $scope.loading = true;

        $http.post(
            "/api/export/csv",
            {
                file: JSON.stringify($scope.pcmObject),
                title: $scope.pcm.title,
                productAsLines: true,
                separator: ',',
                quote: '"'
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
    // TODO : force method call to wait for options working
    $scope.valid()
});

