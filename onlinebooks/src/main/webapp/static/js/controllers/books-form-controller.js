var myApp = angular.module('myApp');

myApp.controller('BooksFormController', BooksFormController);

BooksFormController.$inject = [ '$scope', '$window', 'authenticated', 'book',
		'categories', 'authors', 'languages', 'BookService' ];

function BooksFormController($scope, $window, authenticated, book, categories,
		authors, languages, BookService) {

	$scope.$parent.authenticated = authenticated;
	$scope.book = book;
	$scope.categories = categories;
	$scope.authors = authors;
	$scope.languages = languages;
	$scope.book.category = setCategory($scope.book);
	$scope.book.authors = setAuthors($scope.book);

	function setCategory(book) {
		if (!book.category) {
			return undefined;
		}

		for (let i = 0; i < $scope.categories.length; i++) {
			if (book.category.id === $scope.categories[i].id) {
				return $scope.categories[i];
			}
		}

	}

	function setAuthors(book) {
		var authors = [];

		if (!book.authors) {
			return authors;
		}

		for (let i = 0; i < book.authors.length; i++) {
			for (let j = 0; j < $scope.authors.length; j++) {
				if (book.authors[i].id === $scope.authors[j].id) {
					authors.push($scope.authors[j]);
				}
			}
		}

		return authors;
	}

	$scope.fileChanged = function(element) {
		var reader = new FileReader();

		reader.readAsArrayBuffer(element.files[0]);

		reader.onload = function() {
			var arrayBuffer = reader.result;
			var uint8 = new Uint8Array(arrayBuffer);

			var result = [];

			for (var i = 0; i < uint8.length; i++) {
				result.push(uint8[i]);
			}

			$scope.book.contents = result;
		};

	}

	$scope.saveBook = function(book) {
		var promise = BookService.saveBook(book);

		promise.then(function(response) {
			$window.location.reload();
		});
	}

}