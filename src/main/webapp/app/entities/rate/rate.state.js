(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rate', {
            parent: 'entity',
            url: '/rate',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Rates'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rate/rates.html',
                    controller: 'RateController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('rate-detail', {
            parent: 'entity',
            url: '/rate/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Rate'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rate/rate-detail.html',
                    controller: 'RateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Rate', function($stateParams, Rate) {
                    return Rate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rate-detail.edit', {
            parent: 'rate-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rate/rate-dialog.html',
                    controller: 'RateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rate', function(Rate) {
                            return Rate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rate.new', {
            parent: 'rate',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rate/rate-dialog.html',
                    controller: 'RateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                bookId: null,
                                rate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rate', null, { reload: 'rate' });
                }, function() {
                    $state.go('rate');
                });
            }]
        })
        .state('rate.edit', {
            parent: 'rate',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rate/rate-dialog.html',
                    controller: 'RateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rate', function(Rate) {
                            return Rate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rate', null, { reload: 'rate' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rate.delete', {
            parent: 'rate',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rate/rate-delete-dialog.html',
                    controller: 'RateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Rate', function(Rate) {
                            return Rate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rate', null, { reload: 'rate' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();