/**
 * Created by hvallee on 8/4/15.
 */

pcmApp.controller("ChartsCtrl", function($rootScope, $scope, $http, $timeout, uiGridConstants, $compile, $modal) {
    $scope.showLineChart = false;

    $scope.labels = [];
    $scope.series = [];
    $scope.data = [];

    $scope.onClick = function (points, evt) {
        console.log(points, evt);
    };

    $scope.$on('lineChart', function(event, args) {
        var colName = args.col.name;
        var pcmData = args.pcmData;

        if($scope.showLineChart) { // If the diagram is already there
            console.log($scope.series);
            var index = $scope.series.indexOf(colName);
            if(index != -1) { // If already present, we delete it
                    $scope.series.splice(index, 1);
                    $scope.data.splice(index, 1);

                if($scope.series.length == 0) { // If there is no more columns, we hide it
                    $scope.showLineChart = false;
                }
            }
            else {// If not present we add it
                var data = [];
                pcmData.forEach(function (product) {
                    data.push(parseInt(product[colName]) || 0);
                });
                $scope.data.push(data);
                $scope.series.push(colName);
            }
            console.log($scope.series);
        }
        else {
            $scope.showLineChart = true;
            $scope.series = [colName];
            var data = [];
            var labels = [];
            pcmData.forEach(function (product) {
                labels.push(product.name);
                data.push(parseInt(product[colName]) || 0);
            });
            $scope.data = [data];
            $scope.labels = labels;
        }
    });

    $scope.showNumberDiagram = function(col) { console.log("called");


    }
});