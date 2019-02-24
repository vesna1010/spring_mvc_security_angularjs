var myApp = angular.module('myApp');

myApp.service('BookService', BookService);

BookService.$inject = [ '$http', '$location' ];

function BookService($http, $location) {

	return {
		findBooksByPage : findBooksByPage,
		findBooks : findBooks,
		findBooksByTitle : findBooksByTitle,
		findBooksByAuthorName : findBooksByAuthorName,
		findBooksByCategory : findBooksByCategory,
		findBooksByLanguage : findBooksByLanguage,
		findAllLanguages : findAllLanguages,
		findBookByIsbn : findBookByIsbn,
		newBook : newBook,
		downloadBookByIsbn : downloadBookByIsbn,
		saveBook : saveBook,
		deleteBookByIsbn : deleteBookByIsbn
	}

	function findBooksByPage(params) {
		return $http({
			url : 'books/page',
			method : 'GET',
			params : {
				page : params.page,
				size : params.size,
				sort : 'isbn,asc'
			}
		}).then(function(response) {
			return response.data;
		});
	}

	function findBooks(params) {
		return $http({
			url : 'books',
			method : 'GET',
			params : {
				page : params.page,
				size : params.size,
				sort : [ 'date,desc', 'isbn,asc' ]
			}
		}).then(function(response) {
			return response.data;
		});
	}

	function findBooksByTitle(params) {
		return $http({
			url : 'books/title/' + params.title,
			method : 'GET',
			params : {
				page : params.page,
				size : params.size,
				sort : [ 'date,desc', 'isbn,asc' ]
			}
		}).then(function(response) {
			return response.data;
		});
	}

	function findBooksByAuthorName(params) {
		return $http({
			url : 'books/author/' + params.name,
			method : 'GET',
			params : {
				page : params.page,
				size : params.size,
				sort : [ 'date,desc', 'isbn,asc' ]
			}
		}).then(function(response) {
			return response.data;
		});
	}

	function findBooksByCategory(params) {
		return $http({
			url : 'books/category/' + params.categoryId,
			method : 'GET',
			params : {
				page : params.page,
				size : params.size,
				sort : [ 'date,desc', 'isbn,asc' ]
			}
		}).then(function(response) {
			return response.data;
		});
	}

	function findBooksByLanguage(params) {
		return $http({
			url : 'books/language/' + params.language,
			method : 'GET',
			params : {
				page : params.page,
				size : params.size,
				sort : [ 'date,desc', 'isbn,asc' ]
			}
		}).then(function(response) {
			return response.data;
		});
	}

	function findAllLanguages() {
		return $http({
			url : 'languages',
			method : 'GET',
		}).then(function(response) {
			return response.data;
		});
	}

	function findBookByIsbn(isbn) {
		return $http({
			url : 'books/' + isbn,
			method : 'GET'
		}).then(function(response) {
			return response.data;
		}, function(response) {
			if (response.status === 404) {
				alert(response.data.message);
				$location.path("/books-form");
			}
		});
	}

	function newBook() {
		return $http({
			url : 'books/new',
			method : 'GET'
		}).then(function(response) {
			return response.data;
		});
	}

	function downloadBookByIsbn(isbn) {
		return $http({
			url : 'books/download/' + isbn,
			method : 'GET',
			responseType : 'arraybuffer'
		});
	}

	function saveBook(book) {
		return $http({
			url : 'books',
			method : 'POST',
			data : book,
			transformResponse : angular.identity
		}).then(function(response) {
			alert(response.data);
		});
	}

	function deleteBookByIsbn(isbn) {
		return $http({
			url : 'books/' + isbn,
			method : 'DELETE'
		});
	}

}