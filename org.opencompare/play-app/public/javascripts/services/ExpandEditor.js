/**
 * Created by hvallee on 7/3/15.
 */

pcmApp.service('expandeditor', function($rootScope) {
    this.expandAfterCellEdit = function(functionToAdd) {
        $rootScope.$broadcast('extendEditorFeatures', {function: functionToAdd, type: 'afterCellEdit'});
    };

    this.expandBeginCellEdit = function(functionToAdd) {
        $rootScope.$broadcast('extendEditorFeatures', {function: functionToAdd, type: 'beginCellEdit'});
    };

    this.expandColumnsMoved = function(functionToAdd) {
        $rootScope.$broadcast('extendEditorFeatures', {function: functionToAdd, type: 'columnsMoved'});
    };

    this.expandNavigateFunctions = function(functionToAdd) {
        $rootScope.$broadcast('extendEditorFeatures', {function: functionToAdd, type: 'onNavigate'});
    };


});
