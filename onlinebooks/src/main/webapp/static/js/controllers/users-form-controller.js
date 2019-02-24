var myApp = angular.module('myApp');

myApp.controller('UsersFormController', UsersFormController);

UsersFormController.$inject = [ '$scope', '$window', 'authenticated', 'user',
		'authorities', 'UserService' ];

function UsersFormController($scope, $window, authenticated, user, authorities,
		UserService) {
	$scope.$parent.authenticated = authenticated;
	$scope.user = user;
	$scope.authorities = authorities;

	$scope.saveUser = function(user) {
		var promise = UserService.saveUser(user);

		promise.then(function(response) {
			$window.location.reload();
		});
	}

}