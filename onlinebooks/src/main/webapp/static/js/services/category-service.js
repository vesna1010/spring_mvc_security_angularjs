var myApp = angular.module('myApp');

myApp.service('CategoryService', CategoryService);

CategoryService.$inject = [ '$http', '$location' ];

function CategoryService($http, $location) {

	return {
		findAllCategories : findAllCategories,
		findCategoriesByPage : findCategoriesByPage,
		findCategoryById : findCategoryById,
		newCategory : newCategory,
		saveCategory : saveCategory,
		deleteCategoryById : deleteCategoryById
	}

	function findAllCategories() {
		return $http({
			url : 'categories',
			method : 'GET',
			params : {
				sort : [ 'name,asc', 'id,asc' ]
			}
		}).then(function(response) {
			return response.data;
		});
	}

	function findCategoriesByPage(params) {
		return $http({
			url : 'categories/page',
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

	function findCategoryById(id) {
		return $http({
			url : 'categories/' + id,
			method : 'GET'
		}).then(function(response) {
			return response.data;
		}, function(response) {
			if (response.status === 404) {
				alert(response.data.message);
				$location.path("/categories-form");
			}
		});
	}

	function newCategory() {
		return $http({
			url : 'categories/new',
			method : 'GET'
		}).then(function(response) {
			return response.data;
		});
	}

	function saveCategory(category) {
		return $http({
			url : 'categories',
			method : 'POST',
			data : category,
			transformResponse : angular.identity
		}).then(function(response) {
			alert(response.data);
		});
	}

	function deleteCategoryById(id) {
		return $http({
			url : 'categories/' + id,
			method : 'DELETE'
		});
	}

}