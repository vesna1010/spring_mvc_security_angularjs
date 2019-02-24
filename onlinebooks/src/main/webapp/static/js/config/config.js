var myApp = angular.module('myApp');

myApp.config(Config);

Config.$inject = [ '$routeProvider' ];

function Config($routeProvider) {

	$routeProvider.caseInsensitiveMatch = true;

	$routeProvider.when('/', {
		redirectTo : '/books'
	});

	$routeProvider.when('/books/:page?/:size?', {
		templateUrl : 'static/js/templates/home.html',
		controller : 'HomeController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthenticated();
			},
			page : function(BookService, $route) {
				return BookService.findBooks($route.current.params);
			},
			categories : function(CategoryService) {
				return CategoryService.findAllCategories();
			},
			languages : function(BookService) {
				return BookService.findAllLanguages();
			},
		}
	});

	$routeProvider.when('/books-title/:title/:page?/:size?', {
		templateUrl : 'static/js/templates/home.html',
		controller : 'HomeController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthenticated();
			},
			page : function(BookService, $route) {
				return BookService
						.findBooksByTitle($route.current.params);
			},
			categories : function(CategoryService) {
				return CategoryService.findAllCategories();
			},
			languages : function(BookService) {
				return BookService.findAllLanguages();
			},
		}
	});

	$routeProvider.when('/books-author/:name/:page?/:size?', {
		templateUrl : 'static/js/templates/home.html',
		controller : 'HomeController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthenticated();
			},
			page : function(BookService, $route) {
				return BookService
						.findBooksByAuthorName($route.current.params);
			},
			categories : function(CategoryService) {
				return CategoryService.findAllCategories();
			},
			languages : function(BookService) {
				return BookService.findAllLanguages();
			},
		}
	});

	$routeProvider.when('/books-category/:categoryId/:page?/:size?', {
		templateUrl : 'static/js/templates/home.html',
		controller : 'HomeController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthenticated();
			},
			page : function(BookService, $route) {
				return BookService
						.findBooksByCategory($route.current.params);
			},
			categories : function(CategoryService) {
				return CategoryService.findAllCategories();
			},
			languages : function(BookService) {
				return BookService.findAllLanguages();
			},
		}
	});

	$routeProvider.when('/books-language/:language/:page?/:size?', {
		templateUrl : 'static/js/templates/home.html',
		controller : 'HomeController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthenticated();
			},
			page : function(BookService, $route) {
				return BookService
						.findBooksByLanguage($route.current.params);
			},
			categories : function(CategoryService) {
				return CategoryService.findAllCategories();
			},
			languages : function(BookService) {
				return BookService.findAllLanguages();
			},
		}
	});

	$routeProvider.when('/books-page/:page?/:size?', {
		templateUrl : 'static/js/templates/books.html',
		controller : 'BooksPageController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthorized('books/page');
			},
			page : function(BookService, $route) {
				return BookService.findBooksByPage($route.current.params);
			},
			
		}
	});

	$routeProvider.when('/books-form/:isbn?', {
		templateUrl : 'static/js/templates/book-form.html',
		controller : 'BooksFormController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthorized('books/new');
			},
			book : function(BookService, $route) {
				var isbn = $route.current.params.isbn;

				return (angular.isDefined(isbn) ? BookService
						.findBookByIsbn(isbn) : BookService.newBook());
			},
			categories : function(CategoryService) {
				return CategoryService.findAllCategories();
			},
			authors : function(AuthorService) {
				return AuthorService.findAllAuthors();
			},
			languages : function(BookService) {
				return BookService.findAllLanguages();
			},
		}
	});

	$routeProvider.when('/categories-page/:page?/:size?', {
		templateUrl : 'static/js/templates/categories.html',
		controller : 'CategoriesPageController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthorized('categories/page');
			},
			page : function(CategoryService, $route) {
				return CategoryService.findCategoriesByPage($route.current.params);
			},
			
		}
	});

	$routeProvider.when('/categories-form/:id?', {
		templateUrl : 'static/js/templates/category-form.html',
		controller : 'CategoriesFormController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthorized('categories/new');
			},
			category : function(CategoryService, $route) {
				var id = $route.current.params.id;

				return (angular.isDefined(id) ? CategoryService
						.findCategoryById(id) : CategoryService.newCategory());
			},
		}
	});

	$routeProvider.when('/authors-page/:page?/:size?', {
		templateUrl : 'static/js/templates/authors.html',
		controller : 'AuthorsPageController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthorized('authors/page');
			},
			page : function(AuthorService, $route) {
				return AuthorService.findAuthorsByPage($route.current.params);
			},
		}
	});

	$routeProvider.when('/authors-form/:id?', {
		templateUrl : 'static/js/templates/author-form.html',
		controller : 'AuthorsFormController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthorized('authors/new');
			},
			author : function(AuthorService, $route) {
				var id = $route.current.params.id;

				return (angular.isDefined(id) ? AuthorService
						.findAuthorById(id) : AuthorService.newAuthor());
			},
		}
	});

	$routeProvider.when('/users-page/:page?/:size?', {
		templateUrl : 'static/js/templates/users.html',
		controller : 'UsersPageController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthorized('users/page');
			},
			page : function(UserService, $route) {
				return UserService.findUsersByPage($route.current.params);
			},
		}
	});

	$routeProvider.when('/users-form', {
		templateUrl : 'static/js/templates/user-form.html',
		controller : 'UsersFormController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthorized('users/new');
			},
			user : function(UserService, $route) {
				return UserService.newUser();
			},
			authorities : function(UserService) {
				return UserService.findAuthorities();
			},
		}
	});

	$routeProvider.when('/login', {
		templateUrl : 'static/js/templates/login-form.html',
		controller : 'LoginController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthenticated();
			},
		}
	});

	$routeProvider.when('/denied', {
		templateUrl : 'static/js/templates/denied.html',
		controller : 'LoginController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthenticated();
			},
		}
	});

	$routeProvider.otherwise({
		templateUrl : 'static/js/templates/not-found.html',
		controller : 'LoginController',
		resolve : {
			authenticated : function(AuthenticationService) {
				return AuthenticationService.isAuthenticated();
			},
		}
	});

}
