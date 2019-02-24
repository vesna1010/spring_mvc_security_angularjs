var myApp = angular.module('myApp');

myApp.directive('pagination', Pagination);

function Pagination() {

	return {
		templateUrl : 'static/js/templates/pagination.html',
		restrict : 'E',
		scope : {
			totalPages : '@',
			currentPage : '@',
			pageSize : '@',
			url : '@'
		},
		link : function(scope, element, attrs) {
			scope.numbers = [];
			scope.totalPages = Number(scope.totalPages);
			scope.currentPage = Number(scope.currentPage);
			scope.pageSize = Number(scope.pageSize);

			for (let i = scope.currentPage - 2; i <= scope.currentPage + 2; i++) {
				scope.numbers.push(i);
			}
			
			while (scope.numbers[0] < 0) {
				for (let i = 0; i < scope.numbers.length; i++) {
					scope.numbers[i] = scope.numbers[i] + 1;
				}
			}

			while (scope.numbers[scope.numbers.length - 1] >= scope.totalPages) {
				scope.numbers.pop();
			}
			
			while (scope.numbers.length < 5 && scope.numbers[0] > 0) {
				scope.numbers.unshift(scope.numbers[0] - 1);
			}
			
		}
	}

};