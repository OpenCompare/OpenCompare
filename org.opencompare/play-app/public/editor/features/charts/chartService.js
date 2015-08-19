/**
 * Created by hvallee on 8/4/15.
 */

/**
 * chartService
 * Keep trace of columns in chart for the main controller
 */
pcmApp.service('chartService', function(editorUtil) {

    this.initArrays = function() {
        columnsInLineChart = [];
        columnsInBarChart = [];
        columnInPieChart = [];
        columnsInRadarChart = [];
        columnInStringPieChart = [];
        columnInStringRadarChart = [];
    };

    /* Line chart */
    var columnsInLineChart = [];

    this.addInLineChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        columnsInLineChart.push(colName);
    };

    this.removeFromLineChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnsInLineChart.indexOf(colName);
        columnsInLineChart.splice(index, 1);
    };

    this.isInLineChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnsInLineChart.indexOf(colName);
        return index != -1;
    };

    /* Bar chart */
    var columnsInBarChart = [];

    this.addInBarChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        columnsInBarChart.push(colName);
    };

    this.removeFromBarChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnsInBarChart.indexOf(colName);
        columnsInBarChart.splice(index, 1);
    };

    this.isInBarChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnsInBarChart.indexOf(colName);
        return index != -1;
    };

    /* Pie chart */
    var columnInPieChart = [];

    this.addInPieChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        columnInPieChart.splice(0, 1, colName);
    };

    this.removeFromPieChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnInPieChart.indexOf(colName);
        columnInPieChart.splice(index, 1);
    };

    this.isInPieChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnInPieChart.indexOf(colName);
        return index != -1;
    };

    /* Radar chart */
    var columnsInRadarChart = [];

    this.addInRadarChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        columnsInRadarChart.push(colName);
    };

    this.removeFromRadarChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnsInRadarChart.indexOf(colName);
        columnsInRadarChart.splice(index, 1);
    };

    this.isInRadarChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnsInRadarChart.indexOf(colName);
        return index != -1;
    };

    /* String Pie chart */
    var columnInStringPieChart = [];

    this.addInStringPieChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        columnInStringPieChart.splice(0, 1, colName);
    };

    this.removeFromStringPieChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnInStringPieChart.indexOf(colName);
        columnInStringPieChart.splice(index, 1);
    };

    this.isInStringPieChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnInStringPieChart.indexOf(colName);
        return index != -1;
    };

    /* String Radar chart */
    var columnInStringRadarChart = [];

    this.addInStringRadarChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        columnInStringRadarChart.splice(0, 1, colName);
    };

    this.removeFromStringRadarChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnInStringRadarChart.indexOf(colName);
        columnInStringRadarChart.splice(index, 1);
    };

    this.isInStringRadarChart = function(colName) {
        colName = editorUtil.convertStringToPCMFormat(colName);
        var index = columnInStringRadarChart.indexOf(colName);
        return index != -1;
    };

});