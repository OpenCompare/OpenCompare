/**
 * Created by François on 28/09/16.
 */


angular.module("openCompare").controller("FeedbackController", ['$scope', '$http', function($scope, $http) {
	$scope.post = function(feedback) {
		/*console.log("Email="+feedback.email);
		console.log("Subject="+feedback.subject);
		console.log("Content="+feedback.content);*/
		$http({
			method: 'post',
			url: '/feedback',
			data: {email: feedback.email, subject: feedback.subject, content: feedback.content}
		}).success(function(data){
			console.log("Success : ");
			console.log(data);
		}).error(function(data){
			console.log("Error : ");
			console.log(data);
		});
    };
}]);