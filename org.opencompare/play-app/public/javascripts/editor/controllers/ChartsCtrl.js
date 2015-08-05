/**
 * Created by hvallee on 8/4/15.
 */

pcmApp.controller("ChartsCtrl", function($rootScope, $scope, chartService) {
    $scope.showLineChart = false;
    $scope.showBarChart = false;

    $scope.lineLabels = [];
    $scope.lineSeries = [];
    $scope.lineData = [];

    $scope.onClick = function (points, evt) {
        console.log(points, evt);
    };

    $scope.$on('lineChart', function(event, args) {
        var colName = args.col.name;
        var pcmData = args.pcmData;

        if($scope.showLineChart) { // If the diagram is already there
            var index = $scope.lineSeries.indexOf(colName);
            if(index != -1) { // If already present, we delete it
                    $scope.lineSeries.splice(index, 1);
                    chartService.removeFromLineChart(colName);
                    $scope.lineData.splice(index, 1);


                if($scope.lineSeries.length == 0) { // If there is no more columns, we hide it
                    $scope.showLineChart = false;
                }
            }
            else {// If not present we add it
                var data = [];
                pcmData.forEach(function (product) {
                    data.push(parseInt(product[colName]) || 0);
                });
                $scope.lineData.push(data);
                $scope.lineSeries.push(colName);
                chartService.addInLineChart(colName);
            }
        }
        else {
            $scope.showLineChart = true;
            $scope.lineSeries = [colName];
            chartService.addInLineChart(colName);
            var data = [];
            var labels = [];
            pcmData.forEach(function (product) {
                labels.push(product.name);
                data.push(parseInt(product[colName]) || 0);
            });
            $scope.lineData = [data];
            $scope.lineLabels = labels;
        }
    });

    $scope.$on('barChart', function(event, args) {
        var colName = args.col.name;
        var pcmData = args.pcmData;

        if($scope.showBarChart) { // If the diagram is already there
            var index = $scope.barSeries.indexOf(colName);
            if(index != -1) { // If already present, we delete it
                $scope.barSeries.splice(index, 1);
                chartService.removeFromBarChart(colName);
                $scope.barData.splice(index, 1);


                if($scope.barSeries.length == 0) { // If there is no more columns, we hide it
                    $scope.showBarChart = false;
                }
            }
            else {// If not present we add it
                var data = [];
                pcmData.forEach(function (product) {
                    data.push(parseInt(product[colName]) || 0);
                });
                $scope.barData.push(data);
                $scope.barSeries.push(colName);
                chartService.addInBarChart(colName);
            }
        }
        else {
            $scope.showBarChart = true;
            $scope.barSeries = [colName];
            chartService.addInBarChart(colName);
            var data = [];
            var labels = [];
            pcmData.forEach(function (product) {
                labels.push(product.name);
                data.push(parseInt(product[colName]) || 0);
            });
            $scope.barData = [data];
            $scope.barLabels = labels;
        }
    });

    $scope.isInLineChart = function(col) {
        var colName = col.name;
        var index = $scope.lineSeries.indexOf(colName);
        return index != -1;
    }

});