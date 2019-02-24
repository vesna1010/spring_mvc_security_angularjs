var myApp = angular.module('myApp');

myApp.controller('CategoriesFormController', CategoriesFormController);

CategoriesFormController.$inject = [ '$scope', '$window', 'authenticated',
		'category', 'CategoryService' ];

function CategoriesFormController($scope, $window, authenticated, category,
		CategoryService) {

	$scope.$parent.authenticated = authenticated;
	$scope.category = category;

	$scope.saveCategory = function(category) {
		var promise = CategoryService.saveCategory(category);

		promise.then(function(response) {
			$window.location.reload();
		});
	}

}