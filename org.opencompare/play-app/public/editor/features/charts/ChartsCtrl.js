/**
 * Created by hvallee on 8/4/15.
 */

pcmApp.controller("ChartsCtrl", function($rootScope, $scope, chartService, typeService, editorUtil) {

    $scope.showChartPanel = false;

    $scope.showLineChart = false;
    $scope.showBarChart = false;
    $scope.showPieChart = false;
    $scope.showRadarChart = false;
    $scope.showStringPieChart = false;
    $scope.showStringRadarChart = false;

    $( "#chartPanel" ).draggable();
    $( "#chartPanel" ).resizable({
        aspectRatio: 4 / 3,
        minHeight: 600

    });
    $( "#chartPanel" ).on( "resize", function( event, ui ) {
        $rootScope.$broadcast('closeCharts');
    } );

    $scope.closeCharts = function(close) {
        if(close) {
            $scope.showChartPanel = false;
        }

        $scope.showLineChart = false;
        $scope.showBarChart = false;
        $scope.showPieChart = false;
        $scope.showRadarChart = false;
        $scope.showStringPieChart = false;
        $scope.showStringRadarChart = false;

        chartService.initArrays();
        $scope.pieSeries = [];
        $scope.radarSeries = [];
        $scope.stringPieSeries = [];
        $scope.stringRadarSeries = [];
    };

    $scope.$on('closeCharts', function(event, close) {
        $scope.closeCharts(close);
    });

    /* Line Chart */

    $scope.lineLabels = [];
    $scope.lineSeries = [];
    $scope.lineData = [];

    $scope.$on('lineChart', function(event, args) {
        $scope.showChartPanel = true;

        $("#lineTab").tab('show');
        var colName = editorUtil.convertStringToPCMFormat(args.col.name);
        var pcmData = [];
        args.pcmData.forEach(function (productRow) {
            pcmData.push(productRow.entity);
        });

        if($scope.showLineChart) { // If the diagram is already there
            var index = $scope.lineSeries.indexOf(colName);
            if(index != -1) { // If already present, we delete it
                    $scope.lineSeries.splice(index, 1);
                    chartService.removeFromLineChart(colName);
                    $scope.lineData.splice(index, 1);
                if($scope.lineSeries.length == 0) { // If there is no more columns, we hide it
                    $scope.showLineChart = false;
                    $scope.showChartPanel = $scope.showLineChart || $scope.showBarChart||
                        $scope.showPieChart || $scope.showRadarChart || $scope.showStringPieChart || $scope.showStringRadarChart;
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

    $scope.isInLineChart = function(col) {
        var colName = editorUtil.convertStringToPCMFormat(col.name);

        var index = $scope.lineSeries.indexOf(colName);
        return index != -1;
    };


    /* Bar Chart */

    $scope.barLabels = [];
    $scope.barSeries = [];
    $scope.barData = [];

    $scope.$on('barChart', function(event, args) {
        $scope.showChartPanel = true;
        $("#barTab").tab('show');

        var colName = editorUtil.convertStringToPCMFormat(args.col.name);
        var pcmData = [];
        args.pcmData.forEach(function (productRow) {
            pcmData.push(productRow.entity);
        });

        if($scope.showBarChart) { // If the diagram is already there
            var index = $scope.barSeries.indexOf(colName);
            if(index != -1) { // If already present, we delete it
                $scope.barSeries.splice(index, 1);
                chartService.removeFromBarChart(colName);
                $scope.barData.splice(index, 1);
                if($scope.barSeries.length == 0) { // If there is no more columns, we hide it
                    $scope.showBarChart = false;
                    $scope.showChartPanel = $scope.showLineChart || $scope.showBarChart||
                        $scope.showPieChart || $scope.showRadarChart || $scope.showStringPieChart || $scope.showStringRadarChart;
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

    $scope.isInBarChart = function(col) {
        var colName = editorUtil.convertStringToPCMFormat(col.name);
        var index = $scope.barSeries.indexOf(colName);
        return index != -1;
    };

    /* Pie Chart */

    $scope.pieLabels = [];
    $scope.pieSeries = [];
    $scope.pieData = [];

    $scope.$on('pieChart', function(event, args) {
        $scope.showChartPanel = true;
        $("#pieTab").tab('show');

        var colName = editorUtil.convertStringToPCMFormat(args.col.name);
        var pcmData = [];
        args.pcmData.forEach(function (productRow) {
            pcmData.push(productRow.entity);
        });
        $scope.colors = ['#B2FF59','#FF5722','#DCDCDC'];

        if($scope.showPieChart) { // If the diagram is already there
            var index = $scope.pieSeries.indexOf(colName);
            if(index != -1) { // If already present, we delete it
                $scope.showPieChart = false;
                $scope.pieSeries = [];
                chartService.removeFromPieChart(colName);
                $scope.showChartPanel = $scope.showLineChart || $scope.showBarChart||
                    $scope.showPieChart || $scope.showRadarChart || $scope.showStringPieChart || $scope.showStringRadarChart;
            }
            else {// If not present we add it
                var data = [0, 0, 0];
                pcmData.forEach(function (product) {
                    switch (typeService.getBooleanValue(product[colName])) {
                        case 'yes':
                            data[0]++;
                            break;
                        case 'no':
                            data[1]++;
                            break;
                        case 'unknown':
                            data[2]++;
                            break;
                    }
                });
                $scope.pieData = data;
                $scope.pieSeries = [colName];
                chartService.addInPieChart(colName);
            }
        }
        else {
            $scope.showPieChart = true;
            $scope.pieSeries = [colName];
            chartService.addInPieChart(colName);
            var data = [0, 0, 0];
            var labels = ["Yes", "No", "Unknown"];
            pcmData.forEach(function (product) {
                switch(typeService.getBooleanValue(product[colName])) {
                    case 'yes':
                        data[0]++;
                        break;
                    case 'no':
                        data[1]++;
                        break;
                    case 'unknown':
                        data[2]++;
                        break;
                }
            });
            $scope.pieData = data;
            $scope.pieLabels = labels;
        }
    });

    $scope.isInPieChart = function(col) {
        var colName = editorUtil.convertStringToPCMFormat(col.name);
        var index = $scope.pieSeries.indexOf(colName);
        return index != -1;
    };

    /* Radar Chart */

    $scope.radarLabels = [];
    $scope.radarSeries = [];
    $scope.radarData = [];

    $scope.$on('radarChart', function(event, args) {
        $scope.showChartPanel = true;
        $("#radarTab").tab('show');

        var colName = editorUtil.convertStringToPCMFormat(args.col.name);
        var pcmData = [];
        args.pcmData.forEach(function (productRow) {
            pcmData.push(productRow.entity);
        });

        if($scope.showRadarChart) { // If the diagram is already there
            var index = $scope.radarSeries.indexOf(colName);
            if(index != -1) { // If already present, we delete it
                $scope.radarSeries.splice(index, 1);
                chartService.removeFromRadarChart(colName);
                $scope.radarData.splice(index, 1);

                if($scope.radarSeries.length == 0) { // If there is no more columns, we hide it
                    $scope.showRadarChart = false;
                    $scope.showChartPanel = $scope.showLineChart || $scope.showBarChart||
                        $scope.showPieChart || $scope.showRadarChart || $scope.showStringPieChart || $scope.showStringRadarChart;
                }
            }
            else {// If not present we add it
                var data = [0, 0, 0];
                pcmData.forEach(function (product) {
                    switch (typeService.getBooleanValue(product[colName])) {
                        case 'yes':
                            data[0]++;
                            break;
                        case 'no':
                            data[1]++;
                            break;
                        case 'unknown':
                            data[2]++;
                            break;
                    }
                });
                $scope.radarData.push(data);
                $scope.radarSeries.push(colName);
                chartService.addInRadarChart(colName);
            }
        }
        else {
            $scope.showRadarChart = true;
            $scope.radarSeries = [colName];
            chartService.addInRadarChart(colName);
            var data = [0, 0, 0];
            var labels = ["Yes", "No", "Unknown"];
            pcmData.forEach(function (product) {
                switch(typeService.getBooleanValue(product[colName])) {
                    case 'yes':
                        data[0]++;
                        break;
                    case 'no':
                        data[1]++;
                        break;
                    case 'unknown':
                        data[2]++;
                        break;
                }
            });
            $scope.radarData = [data];
            $scope.radarLabels = labels;
        }
    });

    $scope.isInRadarChart = function(col) {
        var colName = editorUtil.convertStringToPCMFormat(col.name);
        var index = $scope.radarSeries.indexOf(colName);
        return index != -1;
    };

    /* String Pie Chart */

    $scope.stringPieLabels = [];
    $scope.stringPieSeries = [];
    $scope.stringPieData = [];

    $scope.$on('stringPieChart', function(event, args) {
        $scope.showChartPanel = true;
        $("#stringPieTab").tab('show');

        var colName = editorUtil.convertStringToPCMFormat(args.col.name);
        var pcmData = [];
        args.pcmData.forEach(function (productRow) {
            pcmData.push(productRow.entity);
        });
        if($scope.showStringPieChart) { // If the diagram is already there
            var index = $scope.stringPieSeries.indexOf(colName);
            if(index != -1) { // If already present, we delete it
                $scope.showStringPieChart = false;
                $scope.stringPieSeries = [];
                chartService.removeFromStringPieChart(colName);
                $scope.showChartPanel = $scope.showLineChart || $scope.showBarChart||
                    $scope.showPieChart || $scope.showRadarChart || $scope.showStringPieChart || $scope.showStringRadarChart;
            }
            else {
                var data = [];
                var labels = [];
                pcmData.forEach(function (product) {
                    var productValue = product[colName];
                    var index = labels.indexOf(productValue);
                    if(index == -1) {
                        labels.push(productValue);
                        data.push(1);
                    }
                    else {
                        data[index]++;
                    }
                });
                $scope.stringPieData = data;
                $scope.stringPieLabels = labels;
                $scope.stringPieSeries = [colName];
                chartService.addInStringPieChart(colName);
            }
        }
        else {
            $scope.showStringPieChart = true;
            $scope.stringPieSeries = [colName];
            chartService.addInStringPieChart(colName);
            var data = [];
            var labels = [];
            pcmData.forEach(function (product) {
                var productValue = product[colName];
                var index = labels.indexOf(productValue);
                if(index == -1) {
                    labels.push(productValue);
                    data.push(1);
                }
                else {
                    data[index]++;
                }
            });
            $scope.stringPieData = data;
            $scope.stringPieLabels = labels;
        }
    });

    /* String Radar Chart */

    $scope.stringRadarLabels = [];
    $scope.stringRadarSeries = [];
    $scope.stringRadarData = [];

    $scope.$on('stringRadarChart', function(event, args) {
        $scope.showChartPanel = true;
        $("#stringRadarTab").tab('show');

        var colName = editorUtil.convertStringToPCMFormat(args.col.name);
        var pcmData = [];
        args.pcmData.forEach(function (productRow) {
            pcmData.push(productRow.entity);
        });

        if($scope.showStringRadarChart) { // If the diagram is already there
            var index = $scope.stringRadarSeries.indexOf(colName);
            if(index != -1) { // If already present, we delete it
                $scope.showStringRadarChart = false;
                $scope.stringRadarSeries = [];
                chartService.removeFromStringRadarChart(colName);
                $scope.showChartPanel = $scope.showLineChart || $scope.showBarChart||
                    $scope.showPieChart || $scope.showRadarChart || $scope.showStringPieChart || $scope.showStringRadarChart;
            }
            else {
                var data = [];
                var labels = [];
                pcmData.forEach(function (product) {
                    var productValue = product[colName];
                    var index = labels.indexOf(productValue);
                    if(index == -1) {
                        labels.push(productValue);
                        data.push(1);
                    }
                    else {
                        data[index]++;
                    }
                });
                $scope.stringRadarData = [data];
                $scope.stringRadarLabels = labels;
                $scope.stringRadarSeries = [colName];
                chartService.addInStringRadarChart(colName);
            }
        }
        else {
            $scope.showStringRadarChart = true;
            $scope.stringRadarSeries = [colName];
            chartService.addInStringRadarChart(colName);
            var data = [];
            var labels = [];
            pcmData.forEach(function (product) {
                var productValue = product[colName];
                var index = labels.indexOf(productValue);
                if(index == -1) {
                    labels.push(productValue);
                    data.push(1);
                }
                else {
                    data[index]++;
                }
            });
            $scope.stringRadarData = [data];
            $scope.stringRadarLabels = labels;
        }
    });

    $scope.isInStringRadarChart = function(col) {
        var colName = editorUtil.convertStringToPCMFormat(col.name);
        var index = $scope.stringRadarSeries.indexOf(colName);
        return index != -1;
    };


});