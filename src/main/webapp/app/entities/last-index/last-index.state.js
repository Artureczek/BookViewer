(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('last-index', {
            parent: 'entity',
            url: '/last-index',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'LastIndices'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/last-index/last-indices.html',
                    controller: 'LastIndexController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('last-index-detail', {
            parent: 'entity',
            url: '/last-index/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'LastIndex'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/last-index/last-index-detail.html',
                    controller: 'LastIndexDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'LastIndex', function($stateParams, LastIndex) {
                    return LastIndex.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'last-index',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('last-index-detail.edit', {
            parent: 'last-index-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/last-index/last-index-dialog.html',
                    controller: 'LastIndexDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LastIndex', function(LastIndex) {
                            return LastIndex.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('last-index.new', {
            parent: 'last-index',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/last-index/last-index-dialog.html',
                    controller: 'LastIndexDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                table: null,
                                value: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('last-index', null, { reload: 'last-index' });
                }, function() {
                    $state.go('last-index');
                });
            }]
        })
        .state('last-index.edit', {
            parent: 'last-index',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/last-index/last-index-dialog.html',
                    controller: 'LastIndexDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LastIndex', function(LastIndex) {
                            return LastIndex.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('last-index', null, { reload: 'last-index' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('last-index.delete', {
            parent: 'last-index',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/last-index/last-index-delete-dialog.html',
                    controller: 'LastIndexDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LastIndex', function(LastIndex) {
                            return LastIndex.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('last-index', null, { reload: 'last-index' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
