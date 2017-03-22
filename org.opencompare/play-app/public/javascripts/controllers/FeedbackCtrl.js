/**
 * Created by François on 28/09/16.
 */

angular.module("openCompare",[]).controller("FeedbackController", ['$scope', '$http', function($scope, $http) {
	$scope.responseType = "success";
	$scope.response = "";
	$scope.formEnable = true;

	$scope.post = function(feedback, pcmid) {
		$scope.formEnable = false;


        if (feedback === undefined) {
              $scope.responseType = "error";
              $scope.response = "Form is empty, please fill it!";
              return;
        }

        var email = '';
        if (feedback.hasOwnProperty('email'))
            email = feedback['email']; // === 'undefined'

        var subject = '';
        if (feedback.hasOwnProperty('subject'))
            subject = feedback['subject'];

        var content = '';
        if (!feedback.hasOwnProperty('content')) {
              $scope.responseType = "error";
              $scope.response = "The field content is empty, please fill it!";
              return;
        }
        else
            content = feedback.content;


		$http({
			method: 'post',
			url: '/feedback',
			data: {email: email, subject: subject, content: content, pcmid: pcmid}
		}).success(function(data){
			$scope.formEnable = true;
			if(!data.error) {
				console.log("success");
				$scope.responseType = "success";
				$scope.response = "Feedback send";
			} else {
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
