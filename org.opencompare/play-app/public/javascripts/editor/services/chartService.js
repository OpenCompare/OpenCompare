/**
 * Created by hvallee on 8/4/15.
 */

/**
 * chartService
 * Keep trace of columns in chart for the main controller
 */
pcmApp.service('chartService', function() {

    /* Line chart */
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

    /* Bar chart */
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

    /* Pie chart */
    var columnInPieChart = [];

    this.addInPieChart = function(colName) {
        columnInPieChart.splice(0, 1, colName);
    };

    this.removeFromPieChart = function(colName) {
        var index = columnInPieChart.indexOf(colName);
        columnInPieChart.splice(index, 1);
    };

    this.isInPieChart = function(colName) {
        var index = columnInPieChart.indexOf(colName);
        return index != -1;
    };

    /* Radar chart */
    var columnsInRadarChart = [];

    this.addInRadarChart = function(colName) {
        columnsInRadarChart.push(colName);
    };

    this.removeFromRadarChart = function(colName) {
        var index = columnsInRadarChart.indexOf(colName);
        columnsInRadarChart.splice(index, 1);
    };

    this.isInRadarChart = function(colName) {
        var index = columnsInRadarChart.indexOf(colName);
        return index != -1;
    };

    /* String Pie chart */
    var columnInStringPieChart = [];

    this.addInStringPieChart = function(colName) {
        columnInStringPieChart.splice(0, 1, colName);
    };

    this.removeFromStringPieChart = function(colName) {
        var index = columnInStringPieChart.indexOf(colName);
        columnInStringPieChart.splice(index, 1);
    };

    this.isInStringPieChart = function(colName) {
        var index = columnInStringPieChart.indexOf(colName);
        return index != -1;
    };

    /* String Radar chart */
    var columnInStringRadarChart = [];

    this.addInStringRadarChart = function(colName) {
        columnInStringRadarChart.splice(0, 1, colName);
    };

    this.removeFromStringRadarChart = function(colName) {
        var index = columnInStringRadarChart.indexOf(colName);
        columnInStringRadarChart.splice(index, 1);
    };

    this.isInStringRadarChart = function(colName) {
        var index = columnInStringRadarChart.indexOf(colName);
        return index != -1;
    };

});