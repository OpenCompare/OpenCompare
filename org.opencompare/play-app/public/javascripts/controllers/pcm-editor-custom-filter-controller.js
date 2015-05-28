/**
 * Created by hvallee on 5/27/15.
 */

pcmApp.controller('PCMEditorCustomFilterController', function($rootScope, $scope, $compile, $timeout ) {
    var $elm;

    $scope.showFilter = function() {
        $scope.ListToFilter = [];

        $scope.pcmData.forEach( function ( productData ) {
            console.log()
            $scope.ListToFilter.push( productData[name] );
        });
        $scope.ListToFilter.sort();

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

    $scope.$on('showFilter', function(event, args) {
        console.log("dqdqsd");
        $scope.showFilter();
    });
})

    .directive('myCustomDropdown', function() {
        return {
            template: '<select class="form-control" ng-model="colFilter.term" ng-options="option.id as option.value for option in colFilter.options"></select>'
        };
    })
;