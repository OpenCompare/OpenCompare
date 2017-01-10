angular
  .module('openCompareEditor')
  .directive('ocJsonImporter', function(componentUtils) {


    return {
      restrict: 'E',
      require: "^ocEditor",
      scope: {
        api: "="
      },
      controller: function($scope, $modal) {
        $scope.api = {
          open : function() {
            $scope.modalInstance = $modal.open({
              templateUrl: "../assets/modalJsonImport.html", // TODO FIXME 
              scope: $scope,
              controller: "JsonImportCtrl"
            })
          }
        };
      },
      link: function($scope, element, attrs, ctrl) {
        $scope.pcmContainer = ctrl.pcmContainer;

        $scope.$watchCollection("pcmContainer", function(newVal) {
          if (typeof newVal !== "undefined") {
            $scope.pcmContainer = ctrl.pcmContainer;
            $scope.pcm = ctrl.pcmContainer.pcm;
            $scope.metadata = ctrl.pcmContainer.metadata;
            $scope.id = ctrl.pcmContainer.id;
          }
        });

        $scope.config = ctrl.config;
        $scope.state = ctrl.state;

      }
    };
  });
