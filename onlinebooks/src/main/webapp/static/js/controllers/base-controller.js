var myApp = angular.module('myApp');

myApp.controller('BaseController', BaseController);

BaseController.$inject = [ '$scope', '$http', '$location' ];

function BaseController($scope, $http, $location) {

	$scope.logout = function() {
		$http({
			url : 'logout',
			method : 'POST'
		}).then(function(response) {
			$location.path('/login');
		});
	}

}