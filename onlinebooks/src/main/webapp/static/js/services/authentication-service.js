var myApp = angular.module('myApp');

myApp.service('AuthenticationService', AuthenticationService);

AuthenticationService.$inject = [ '$http', '$location' ];

function AuthenticationService($http, $location) {

	return {
		isAuthenticated : isAuthenticated,
		isAuthorized : isAuthorized
	};

	function isAuthenticated() {
		return $http({
			url : 'authenticated',
			method : 'GET'
		}).then(function(response) {
			return response.data;
		});
	}

	function isAuthorized(url) {
		return $http({
			url : url,
			method : 'GET'
		}).then(function(response) {
			if (response.status === 200) {
				return true;
			}
		}, function(response) {
			if (response.status === 401) {
				$location.path('/login');
			}

			if (response.status === 403) {
				$location.path('/denied');
			}
		});
	}

}