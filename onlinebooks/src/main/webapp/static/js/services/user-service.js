var myApp = angular.module('myApp');

myApp.service('UserService', UserService);

UserService.$inject = [ '$http' ];

function UserService($http) {

	return {
		findUsersByPage : findUsersByPage,
		newUser : newUser,
		findAuthorities : findAuthorities,
		saveUser : saveUser,
		deleteUserByUsername : deleteUserByUsername,
		disableUserByUsername : disableUserByUsername,
	}

	function findUsersByPage(params) {
		return $http({
			url : 'users',
			method : 'GET',
			params : {
				page : params.page,
				size : params.size,
				sort : 'username,asc'
			}
		}).then(function(response) {
			return response.data;
		});
	}

	function newUser() {
		return $http({
			url : 'users/new',
			method : 'GET'
		}).then(function(response) {
			return response.data;
		});
	}

	function findAuthorities() {
		return $http({
			url : 'authorities',
			method : 'GET'
		}).then(function(response) {
			return response.data;
		});
	}

	function saveUser(user) {
		return $http({
			url : 'users',
			method : 'POST',
			data : user,
			transformResponse : angular.identity
		}).then(function(response) {
			alert(response.data);
		});
	}

	function deleteUserByUsername(username) {
		return $http({
			url : 'users/' + username,
			method : 'DELETE'
		});
	}

	function disableUserByUsername(username) {
		return $http({
			url : 'users/' + username,
			method : 'PUT'
		});
	}

}