(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('countries', {
            parent: 'entity',
            url: '/countries',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Countries'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/countries/countries.html',
                    controller: 'CountriesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('countries-detail', {
            parent: 'entity',
            url: '/countries/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Countries'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/countries/countries-detail.html',
                    controller: 'CountriesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Countries', function($stateParams, Countries) {
                    return Countries.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'countries',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('countries-detail.edit', {
            parent: 'countries-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/countries/countries-dialog.html',
                    controller: 'CountriesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Countries', function(Countries) {
                            return Countries.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('countries.new', {
            parent: 'countries',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/countries/countries-dialog.html',
                    controller: 'CountriesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                currency: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('countries', null, { reload: 'countries' });
                }, function() {
                    $state.go('countries');
                });
            }]
        })
        .state('countries.edit', {
            parent: 'countries',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/countries/countries-dialog.html',
                    controller: 'CountriesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Countries', function(Countries) {
                            return Countries.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('countries', null, { reload: 'countries' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('countries.delete', {
            parent: 'countries',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/countries/countries-delete-dialog.html',
                    controller: 'CountriesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Countries', function(Countries) {
                            return Countries.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('countries', null, { reload: 'countries' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
