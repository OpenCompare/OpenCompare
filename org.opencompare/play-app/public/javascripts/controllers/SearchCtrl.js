/**
 * Created by gbecan on 11/12/14.
 */

angular.module("openCompare")
    .controller("SearchController", function($scope, $http) {


    $scope.request = '';

    $scope.getSuggestions = function() {
        return $http.get("/api/search?request=" + $scope.request).then(function (response) {
            return response.data.map(function(result) {
                return result.name;
            }).slice(0, 10);
        });

    }

});