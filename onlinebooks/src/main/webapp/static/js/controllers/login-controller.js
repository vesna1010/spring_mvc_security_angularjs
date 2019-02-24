var myApp = angular.module('myApp');

myApp.controller('LoginController', LoginController);

LoginController.$inject = [ '$scope', '$http', '$window', 'authenticated' ];

function LoginController($scope, $http, $window, authenticated) {
	$scope.$parent.authenticated = authenticated;

	$scope.login = function(username, password) {
		$http({
			url : 'login',
			method : 'POST',
			params : {
				username : username,
				password : password
			}
		}).then(function(response) {
			if (response.status === 200) {
				$window.location.reload();
			}
		}, function(response) {
			if (response.status === 401) {
				$scope.message = 'Plase check username and password';
				$scope.username = '';
				$scope.password = '';
			}
		});

	}

}