/**
 * Created by hvallee on 5/22/15.
 */

pcmApp.controller("PCMEditorCustomFilterController", function($rootScope, $scope, $timeout, $compile) {
    var $elm;

    $scope.$on.showAgeModal = function() {
        $scope.listOfAges = [];

        $scope.col.grid.appScope.gridOptions.data.forEach( function ( row ) {
            if ( $scope.listOfAges.indexOf( row.age ) === -1 ) {
                $scope.listOfAges.push( row.age );
            }
        });
        $scope.listOfAges.sort();

        $scope.gridOptions = {
            data: [],
            enableColumnMenus: false,
            onRegisterApi: function( gridApi) {
                $scope.gridApi = gridApi;

                if ( $scope.colFilter && $scope.colFilter.listTerm ){
                    $timeout(function() {
                        $scope.colFilter.listTerm.forEach( function( age ) {
                            var entities = $scope.gridOptions.data.filter( function( row ) {
                                return row.age === age;
                            });

                            if( entities.length > 0 ) {
                                $scope.gridApi.selection.selectRow(entities[0]);
                            }
                        });
                    });
                }
            }
        };

        $scope.listOfAges.forEach(function( age ) {
            $scope.gridOptions.data.push({age: age});
        });

        var html = '<div class="modal" ng-style="{display: \'block\'}"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">Filter Ages</div><div class="modal-body"><div id="grid1" ui-grid="gridOptions" ui-grid-selection class="modalGrid"></div></div><div class="modal-footer"><button id="buttonClose" class="btn btn-primary" ng-click="close()">Filter</button></div></div></div></div>';
        $elm = angular.element(html);
        angular.element(document.body).prepend($elm);

        $compile($elm)($scope);

    };

    $scope.close = function() {
        var ages = $scope.gridApi.selection.getSelectedRows();
        $scope.colFilter.listTerm = [];

        ages.forEach( function( age ) {
            $scope.colFilter.listTerm.push( age.age );
        });

        $scope.colFilter.term = $scope.colFilter.listTerm.join(', ');
        $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));

        if ($elm) {
            $elm.remove();
        }
    };
})
