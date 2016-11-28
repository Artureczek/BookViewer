(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('purchase', {
            parent: 'entity',
            url: '/purchase',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Purchases'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/purchase/purchases.html',
                    controller: 'PurchaseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('purchase-detail', {
            parent: 'entity',
            url: '/purchase/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Purchase'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/purchase/purchase-detail.html',
                    controller: 'PurchaseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Purchase', function($stateParams, Purchase) {
                    return Purchase.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'purchase',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('purchase-detail.edit', {
            parent: 'purchase-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/purchase/purchase-dialog.html',
                    controller: 'PurchaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Purchase', function(Purchase) {
                            return Purchase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('purchase.new', {
            parent: 'purchase',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/purchase/purchase-dialog.html',
                    controller: 'PurchaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                value: null,
                                bookId: null,
                                userId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('purchase', null, { reload: 'purchase' });
                }, function() {
                    $state.go('purchase');
                });
            }]
        })
        .state('purchase.edit', {
            parent: 'purchase',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/purchase/purchase-dialog.html',
                    controller: 'PurchaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Purchase', function(Purchase) {
                            return Purchase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('purchase', null, { reload: 'purchase' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('purchase.delete', {
            parent: 'purchase',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/purchase/purchase-delete-dialog.html',
                    controller: 'PurchaseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Purchase', function(Purchase) {
                            return Purchase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('purchase', null, { reload: 'purchase' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
