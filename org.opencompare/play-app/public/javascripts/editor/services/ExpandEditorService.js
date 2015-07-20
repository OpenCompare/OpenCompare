/**
 * Created by hvallee on 7/3/15.
 */

pcmApp.service('expandeditor', function() {

    var afterCellEditFunctions = [];
    this.expandAfterCellEdit = function(functionToAdd) {
        return{
            afterCellEditFunctions: afterCellEditFunctions,
                addFunction: function() {
                    afterCellEditFunctions.push(functionToAdd);
            }
        }
    };

    var beginCellEditFunctions = [];
    this.expandBeginCellEdit = function(functionToAdd) {
        return{
            beginCellEditFunctions: beginCellEditFunctions,
                addFunction: function() {
                    beginCellEditFunctions.push(functionToAdd);
            }
        }
    };

    var columnMovedFunctions = [];
    this.expandColumnsMoved = function(functionToAdd) {
        return{
            columnMovedFunctions: columnMovedFunctions,
                addFunction: function() {
                    columnMovedFunctions.push(functionToAdd);
            }
        }
    };

    var navigateFunctions = [];
    this.expandNavigateFunctions = function(functionToAdd) {
        return{
            navigateFunctions: navigateFunctions,
            addFunction: function() {
                navigateFunctions.push(functionToAdd);
            }
        }
    };


});
