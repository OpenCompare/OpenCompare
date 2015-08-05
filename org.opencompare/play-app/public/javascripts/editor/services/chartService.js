/**
 * Created by hvallee on 8/4/15.
 */

/**
 * chartService
 * Keep trace of columns in chart for the main controller
 */
pcmApp.service('chartService', function() {

    var columnsInLineChart = [];

    this.addInLineChart = function(colName) {
        columnsInLineChart.push(colName);
    };

    this.removeFromLineChart = function(colName) {
        var index = columnsInLineChart.indexOf(colName);
        columnsInLineChart.splice(index, 1);
    };

    this.isInLineChart = function(colName) {
        var index = columnsInLineChart.indexOf(colName);
        return index != -1;
    };

    var columnsInBarChart = [];

    this.addInBarChart = function(colName) {
        columnsInBarChart.push(colName);
    };

    this.removeFromBarChart = function(colName) {
        var index = columnsInBarChart.indexOf(colName);
        columnsInBarChart.splice(index, 1);
    };

    this.isInBarChart = function(colName) {
        var index = columnsInBarChart.indexOf(colName);
        return index != -1;
    };

});