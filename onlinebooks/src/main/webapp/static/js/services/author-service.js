var myApp = angular.module('myApp');

myApp.service('AuthorService', AuthorService);

AuthorService.$inject = [ '$http', '$location' ];

function AuthorService($http, $location) {

	return {
		findAllAuthors : findAllAuthors,
		findAuthorsByPage : findAuthorsByPage,
		findAuthorById : findAuthorById,
		newAuthor : newAuthor,
		saveAuthor : saveAuthor,
		deleteAuthorById : deleteAuthorById
	}

	function findAllAuthors() {
		return $http({
			url : 'authors',
			method : 'GET',
			params : {
				sort : [ 'name,asc', 'id,asc' ]
			}
		}).then(function(response) {
			return response.data;
		});
	}

	function findAuthorsByPage(params) {
		return $http({
			url : 'authors/page',
			method : 'GET',
			params : {
				page : params.page,
				size : params.size,
				sort : 'id,asc'
			}
		}).then(function(response) {
			return response.data;
		});
	}

	function findAuthorById(id) {
		return $http({
			url : 'authors/' + id,
			method : 'GET'
		}).then(function(response) {
			return response.data;
		}, function(response) {
			if (response.status === 404) {
				alert(response.data.message);
				$location.path("/authors-form");
			}
		});
	}

	function newAuthor() {
		return $http({
			url : 'authors/new',
			method : 'GET'
		}).then(function(response) {
			return response.data;
		});
	}

	function saveAuthor(author) {
		return $http({
			url : 'authors',
			method : 'POST',
			data : author,
			transformResponse : angular.identity
		}).then(function(response) {
			alert(response.data);
		});
	}

	function deleteAuthorById(id) {
		return $http({
			url : 'authors/' + id,
			method : 'DELETE'
		});
	}

}