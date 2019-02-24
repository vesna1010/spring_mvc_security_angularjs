var myApp = angular.module('myApp');

myApp.directive('activeLink', ActiveLink);

ActiveLink.$inject = [ '$location' ];

function ActiveLink($location) {

	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			scope.$watch(watchFun, handlerFun);

			function watchFun() {
				return $location.path();
			}

			function handlerFun(newValue) {
				var links = element.find('a');

				for (let i = 0; i < links.length; i++) {
					var href = links.eq(i).attr('href');

					href = href.substring(3);
					href = href.indexOf('/') != -1 ? href.substring(0, href.indexOf('/')) : href;

					if (newValue.match('^\/' + href)) {
						links.eq(i).addClass('active');
					} else {
						links.eq(i).removeClass('active');
					}
				}
			}
		}
	}
};