/**
 * Created by smangin on 19/05/15.
 */


pcmApp.controller("PCMImporterController", function($rootScope, $scope, $http) {

    // Default values
    $scope.type = 'csv';
    $scope.file = null;
    $scope.pcm = {
        title: "",
        productAsLines: true,
    };
    $scope.csv = {
        separator: ',',
        quote: '"',
    };

    /**
     * Validate
     */
    $scope.confirm = function() {
        var r = new FileReader();
        // FileReader is asynchronous.
        // The handler is mandatory.
        // Prefered 'onloadend' which is called whatever appends (success, error)
        r.onloadend = function(evt) {
            var datas = {
                title: $scope.pcm.title,
                separator: $scope.csv.separator,
                quote: $scope.csv.quote,
                productAsLines: $scope.pcm.productAsLines,
                fileContent: evt.target.result,
                type: $scope.type,
            }
            $http.post("/api/import", datas)
                .success(function(data, status, headers, config) {
                    console.log("model created with id=" + data);
                    window.location = '/view/' + data;
                })
                .error(function(data, status, headers, config){
                    alert(data);
                });
        };
        r.readAsText(document.getElementById('file').files[0]);

    };

});
	



