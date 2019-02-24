var myApp = angular.module('myApp');

myApp.controller('CategoriesPageController', CategoriesPageController);

CategoriesPageController.$inject = [ '$scope', '$window', 'authenticated',
		'page', 'CategoryService' ];

function CategoriesPageController($scope, $window, authenticated, page,
		CategoryService) {

	$scope.$parent.authenticated = authenticated;
	$scope.categories = page.content;
	$scope.totalPages = page.totalPages;
	$scope.pageSize = page.size;
	$scope.currentPage = page.number;

	$scope.deleteCategoryById = function(id) {
		var promise = CategoryService.deleteCategoryById(id);

		promise.then(function(response) {
			$window.location.reload();
		});
	}

}