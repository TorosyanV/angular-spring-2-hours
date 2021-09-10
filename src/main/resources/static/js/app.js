(function () {

    var app = angular.module('notesApp', ['ngRoute', 'ngMaterial', 'ngStorage']);

    app.config(['$locationProvider', '$routeProvider',
        function ($locationProvider, $routeProvider) {

            $routeProvider
                .when('/', {
                    templateUrl: '/partials/notes-view.html',
                    controller: 'notesController'
                })
                .when('/login', {
                    templateUrl: '/partials/login.html',
                    controller: 'loginController',
                })
                .otherwise('/');
        }
    ]);

    app.run(['$rootScope', '$location', 'AuthService', function ($rootScope, $location, AuthService) {
        $rootScope.$on('$routeChangeStart', function (event) {

            if ($location.path() == "/login") {
                return;
            }

            if (!AuthService.isLoggedIn()) {
                console.log('DENY');
                event.preventDefault();
                $location.path('/login');
            }
        });
    }]);


    app.service('AuthService', function ($http, $q, $localStorage) {
        var loggedUser = $localStorage.user;

        function login(username, password) {
            var def = $q.defer();

            $http.post("api/login", {username: username, password: password}).then(function (response) {
                loggedUser = response.data;
                $localStorage.user = loggedUser;
                def.resolve(response.data);
            }, function (error) {
                def.reject("Failed to login");
                loggedUser = null;
            })
            return def.promise;
        }

        function logout() {
            var def = $q.defer();

            $http.post("api/logout").then(function (response) {
                loggedUser = null;
                $localStorage.user = loggedUser;
                def.resolve(response.data);
            }, function (error) {
                def.reject("Failed to logout");
            })
            return def.promise;
        }

        function isLoggedIn() {
            return loggedUser != null;
        }

        return {
            login: login,
            isLoggedIn: isLoggedIn,
            logout: logout
        }
    });

    app.service('NoteService', function ($http) {

        function create(note) {
            return $http.post("api/notes", {name: note.name, text: note.text}).then(function (response) {
                console.info('note created', response.data);
                return response.data;
            }, function (error) {
                console.error(error);
            })
        }

        function edit(note) {
            return $http.put("api/notes/" + note.id, {name: note.name, text: note.text}).then(function (response) {
                console.info('note created', response.data);
                return response.data;
            }, function (error) {
                console.error(error);
            })
        }

        function getAll() {
            return $http.get("api/notes").then(function (response) {
                console.info('notes retrieved', response.data);
                return response.data;
            }, function (error) {
                console.error(error);
            })
        }


        return {
            create: create,
            edit: edit,
            getAll: getAll
        }
    });
    app.controller('loginController', function ($scope, AuthService, $location) {

        $scope.invalidCreds = false;
        $scope.login = {
            username: null,
            password: null
        };

        $scope.login = function () {
            AuthService.login($scope.login.username, $scope.login.password).then(function (user) {
                console.log(user);
                $location.path("/");
            }, function (error) {
                console.log(error);
                $scope.invalidCreds = true;
            });
        };

    });

    app.controller('logoutController', function ($scope, $location, AuthService) {
        $scope.logout = function () {
            AuthService.logout().then(function () {
                $location.path("/logout");
            }, function (error) {
                console.log(error);
            });
        };
        $scope.isLoggedIn = function () {
            return AuthService.isLoggedIn();
        }
    })

    app.controller('notesController', function ($scope, NoteService) {
        init();
        var vm = this;
        $scope.notes = [];
        $scope.isEditCreateView = false;
        $scope.note = {name: '', text: ''};
        $scope.newNoteView = function () {
            _newNote({name: '', text: ''})

        };

        $scope.addOrEditNote = function () {

            if ($scope.note.hasOwnProperty("id")) {
                NoteService.edit($scope.note).then(function (note) {
                    $scope.isEditCreateView = false;

                }, function (error) {
                    console.log(error);
                });
            } else {
                NoteService.create($scope.note).then(function (note) {
                    $scope.notes.push(note);
                    $scope.note = {name: '', text: ''};
                    $scope.isEditCreateView = false;

                }, function (error) {
                    console.log(error);
                });
            }


        };

        function init() {
            NoteService.getAll().then(function (data) {
                console.log(data);
                $scope.notes = data;
            }, function (error) {
                console.log(error);
            });
        };


        $scope.deleteNote = function (i) {
            var r = confirm("Are you sure you want to delete this note?");
            if (r == true) {
                //TODO delete the note
            }
        };

        var _newNote = function (note) {
            $scope.isEditCreateView = true;
            $scope.note = note;
        }
        $scope.viewNote = function (note) {
            _newNote(note);
        }

        $scope.cancelEdit = function () {
            $scope.isEditCreateView = false;
        }
    });

})();