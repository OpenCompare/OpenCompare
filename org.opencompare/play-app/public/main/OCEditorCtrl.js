/**
 * Created by gbecan on 9/16/15.
 */
angular.module("openCompare")
    .controller("OCEditorCtrl", function($scope, $modal) {

        if (typeof id !== 'undefined') {
            $scope.id = id;
        }

        if (typeof data !== 'undefined') {
            $scope.data = data;
        }

        /* Load modal for import */
        if (typeof modal != 'undefined') {
            // Open the given modal
            $modal.open({
                templateUrl: "/templates/modal/modal" + modal + ".html",
                controller: modal + "Controller",
                scope: $scope
            })
        }

    });