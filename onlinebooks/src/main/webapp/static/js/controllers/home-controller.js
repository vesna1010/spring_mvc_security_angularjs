var myApp = angular.module('myApp');

myApp.controller('HomeController', HomeController);

HomeController.$inject = [ '$scope', '$location', '$routeParams',
		'authenticated', 'page', 'categories', 'languages', 'BookService' ];

function HomeController($scope, $location, $routeParams, authenticated, page,
		categories, languages, BookService) {

	$scope.$parent.authenticated = authenticated;
	$scope.language = $routeParams.language;
	$scope.categoryId = $routeParams.categoryId;
	$scope.categories = categories;
	$scope.languages = languages;
	$scope.title = $routeParams.title;
	$scope.name = $routeParams.name;
	$scope.books = page.content;
	$scope.totalPages = page.totalPages;
	$scope.pageSize = page.size;
	$scope.currentPage = page.number;

	$scope.url = $location.path().substring(0,
			$location.path().lastIndexOf('/'));
	$scope.url = '#!' + $scope.url.substring(0, $scope.url.lastIndexOf('/'));

	$scope.booksByCategory = function(categoryId) {
		if (categoryId) {
			$location.path('books-category/' + categoryId + '/'
					+ ($routeParams.page ? ($routeParams.page + '/') : '')
					+ ($routeParams.size ? ($routeParams.size + '/') : ''));
		}
	}

	$scope.booksByLanguage = function(language) {
		if (language) {
			$location.path('books-language/' + language + '/'
					+ ($routeParams.page ? ($routeParams.page + '/') : '')
					+ ($routeParams.size ? ($routeParams.size + '/') : ''));
		}
	}

	$scope.booksByTitle = function(title) {
		if (title.length) {
			$location.path('books-title/' + title + '/'
					+ ($routeParams.page ? ($routeParams.page + '/') : '')
					+ ($routeParams.size ? ($routeParams.size + '/') : ''));
		}
	}

	$scope.booksByAuthorName = function(name) {
		if (name.length) {
			$location.path('books-author/' + name + '/'
					+ ($routeParams.page ? ($routeParams.page + '/') : '')
					+ ($routeParams.size ? ($routeParams.size + '/') : ''));
		}
	}

	$scope.downloadBook = function(isbn, title) {
		var promise = BookService.downloadBookByIsbn(isbn, title);

		promise.then(function(response) {
			var file = new Blob([ response.data ], {
				type : 'application/pdf'
			});
			var fileURL = URL.createObjectURL(file);
			var a = document.createElement("a");

			a.href = fileURL;
			a.download = title;
			a.click();
		});
	}

}