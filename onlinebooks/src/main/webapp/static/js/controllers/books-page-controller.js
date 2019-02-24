var myApp = angular.module('myApp');

myApp.controller('BooksPageController', BooksPageController);

BooksPageController.$inject = [ '$scope', '$window', 'authenticated', 'page',
		'BookService' ];

function BooksPageController($scope, $window, authenticated, page, BookService) {

	$scope.$parent.authenticated = authenticated;
	$scope.books = page.content;
	$scope.totalPages = page.totalPages;
	$scope.pageSize = page.size;
	$scope.currentPage = page.number;

	$scope.deleteBookByIsbn = function(isbn) {
		var promise = BookService.deleteBookByIsbn(isbn);

		promise.then(function(response) {
			$window.location.reload();
		});
	}

}