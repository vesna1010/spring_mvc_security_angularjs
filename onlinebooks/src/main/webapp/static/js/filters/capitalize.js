var myApp = angular.module('myApp');

myApp.filter('capitalize', Capitalize);

function Capitalize() {

	return function(input) {
		var strs = input.trim().split(/\s+/);

		for (var i = 0; i < strs.length; i++) {
			strs[i] = strs[i].charAt(0).toUpperCase()
					+ strs[i].substring(1).toLowerCase();
		}

		return strs.join(' ');
	}

}