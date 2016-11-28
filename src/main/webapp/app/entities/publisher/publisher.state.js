(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('publisher', {
            parent: 'entity',
            url: '/publisher',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Publishers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/publisher/publishers.html',
                    controller: 'PublisherController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('publisher-detail', {
            parent: 'entity',
            url: '/publisher/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Publisher'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/publisher/publisher-detail.html',
                    controller: 'PublisherDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Publisher', function($stateParams, Publisher) {
                    return Publisher.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'publisher',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('publisher-detail.edit', {
            parent: 'publisher-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publisher/publisher-dialog.html',
                    controller: 'PublisherDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Publisher', function(Publisher) {
                            return Publisher.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('publisher.new', {
            parent: 'publisher',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publisher/publisher-dialog.html',
                    controller: 'PublisherDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('publisher', null, { reload: 'publisher' });
                }, function() {
                    $state.go('publisher');
                });
            }]
        })
        .state('publisher.edit', {
            parent: 'publisher',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publisher/publisher-dialog.html',
                    controller: 'PublisherDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Publisher', function(Publisher) {
                            return Publisher.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('publisher', null, { reload: 'publisher' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('publisher.delete', {
            parent: 'publisher',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/publisher/publisher-delete-dialog.html',
                    controller: 'PublisherDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Publisher', function(Publisher) {
                            return Publisher.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('publisher', null, { reload: 'publisher' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
