var myApp = angular.module('myApp');

myApp.controller('UsersPageController', UsersPageController);

UsersPageController.$inject = [ '$scope', '$window', 'authenticated', 'page',
		'UserService' ];

function UsersPageController($scope, $window, authenticated, page, UserService) {

	$scope.$parent.authenticated = authenticated;
	$scope.users = page.content;
	$scope.totalPages = page.totalPages;
	$scope.pageSize = page.size;
	$scope.currentPage = page.number;

	$scope.deleteUserByUsername = function(username) {
		var promise = UserService.deleteUserByUsername(username);

		promise.then(function(response) {
			$window.location.reload();
		});
	}

	$scope.disableUserByUsername = function(username) {
		var promise = UserService.disableUserByUsername(username);

		promise.then(function(response) {
			$window.location.reload();
		});
	}

}