/**
 * Created by François on 28/09/16.
 */

angular.module("openCompare",[]).controller("FeedbackController", ['$scope', '$http', function($scope, $http) {
	$scope.responseType = "success";
	$scope.response = "";
	$scope.formEnable = true;

	$scope.post = function(feedback) {
		$scope.formEnable = false;

		$http({
			method: 'post',
			url: '/feedback',
			data: {email: feedback.email, subject: feedback.subject, content: feedback.content}
		}).success(function(data){
			$scope.formEnable = true;
			if(!data.error){
				console.log("success");
				$scope.responseType = "success";
				$scope.response = "Feedback send";
			}else{
				console.log("error");
				$scope.responseType = "error";
				$scope.response = "Error";
			}
		}).error(function(data){
			$scope.formEnable = true;
			console.log("Error : ");
			console.log(data);
		});
    };
}]);
