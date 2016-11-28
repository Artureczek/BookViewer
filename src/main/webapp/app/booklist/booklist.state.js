(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('booklist', {
            parent: 'app',
            url: '/booklist',
            data: {
                authorities: ['ROLE_USER'],
                name: 'BOOKLIST'
            },
            views: {
                'content@': {
                    templateUrl: 'app/booklist/booklist.html',
                    controller: 'BooklistController',
                    controllerAs: 'vm'
                }
            }
        }).state('booklist-details-id', {
                parent: 'app',
                url: '/booklist/detail/:id',
                data: {
                    authorities: ['ROLE_USER'],
                    name: 'BOOKLIST-DETAILS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/booklist/booklist-details.html',
                        controller: 'BooklistDetailsController',
                        controllerAs: 'vm'
                    }
                }
            }
        ).state('createbook', {

            parent: 'app',
            url: '/createbook',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/booklist/create-book-dialog.html',
                    controller: 'BookCreateController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null, title: null, author: null, isbn: null, year: null,
                                publisherId: null, imgLarge: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('booklist', null, { reload: true });
                }, function() {
                    $state.go('booklist');
                });
            }]
            }
        ).state('editbook', {
            parent: 'app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/booklist/create-book-dialog.html',
                    controller: 'BookCreateController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Book', function(Book) {
                            return Book.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('booklist', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('buybook', {
            parent: 'app',
            url: '/{id}/buy',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/booklist/buy-book-dialog.html',
                    controller: 'BuyBookController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Book', function(Book) {
                            return Book.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('booklist', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
