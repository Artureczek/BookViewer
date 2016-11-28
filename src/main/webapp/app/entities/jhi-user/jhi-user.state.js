(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('jhi-user', {
            parent: 'entity',
            url: '/jhi-user',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JhiUsers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/jhi-user/jhi-users.html',
                    controller: 'JhiUserController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('jhi-user-detail', {
            parent: 'entity',
            url: '/jhi-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'JhiUser'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/jhi-user/jhi-user-detail.html',
                    controller: 'JhiUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'JhiUser', function($stateParams, JhiUser) {
                    return JhiUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'jhi-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('jhi-user-detail.edit', {
            parent: 'jhi-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jhi-user/jhi-user-dialog.html',
                    controller: 'JhiUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JhiUser', function(JhiUser) {
                            return JhiUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('jhi-user.new', {
            parent: 'jhi-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jhi-user/jhi-user-dialog.html',
                    controller: 'JhiUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                login: null,
                                password: null,
                                firstName: null,
                                lastName: null,
                                activated: null,
                                langKey: null,
                                createdBy: null,
                                createdDay: null,
                                countryId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('jhi-user', null, { reload: 'jhi-user' });
                }, function() {
                    $state.go('jhi-user');
                });
            }]
        })
        .state('jhi-user.edit', {
            parent: 'jhi-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jhi-user/jhi-user-dialog.html',
                    controller: 'JhiUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['JhiUser', function(JhiUser) {
                            return JhiUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('jhi-user', null, { reload: 'jhi-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('jhi-user.delete', {
            parent: 'jhi-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/jhi-user/jhi-user-delete-dialog.html',
                    controller: 'JhiUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['JhiUser', function(JhiUser) {
                            return JhiUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('jhi-user', null, { reload: 'jhi-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
