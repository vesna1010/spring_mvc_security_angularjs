var myApp = angular.module('myApp');

myApp.controller('AuthorsPageController', AuthorsPageController);

AuthorsPageController.$inject = [ '$scope', '$window', 'authenticated', 'page',
		'AuthorService' ];

function AuthorsPageController($scope, $window, authenticated, page,
		AuthorService) {

	$scope.$parent.authenticated = authenticated;
	$scope.authors = page.content;
	$scope.totalPages = page.totalPages;
	$scope.pageSize = page.size;
	$scope.currentPage = page.number;

	$scope.deleteAuthorById = function(id) {
		var promise = AuthorService.deleteAuthorById(id);

		promise.then(function(response) {
			$window.location.reload();
		});
	}

}