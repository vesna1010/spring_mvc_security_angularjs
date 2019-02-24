var myApp = angular.module('myApp');

myApp.controller('AuthorsFormController', AuthorsFormController);

AuthorsFormController.$inject = [ '$scope', '$window', 'authenticated',
		'author', 'AuthorService' ];

function AuthorsFormController($scope, $window, authenticated, author,
		AuthorService) {

	$scope.$parent.authenticated = authenticated;
	$scope.author = author;

	$scope.saveAuthor = function(author) {
		var promise = AuthorService.saveAuthor(author);

		promise.then(function(response) {
			$window.location.reload();
		});
	}

}