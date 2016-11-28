(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('booklistbought', {
                parent: 'app',
                url: '/booklistbought',
                data: {
                    authorities: ['ROLE_USER'],
                    name: 'BOOKLISTBOUGHT'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/booklistbought/booklistbought.html',
                        controller: 'BooklistBoughtController',
                        controllerAs: 'vm'
                    }
                }
            }).state('booklistbought-details-id', {
                parent: 'app',
                url: '/booklistbought/detail/:id',
                data: {
                    authorities: ['ROLE_USER'],
                    name: 'BOOKLISTBOUGHT-DETAILS'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/booklistbought/booklistbought-details.html',
                        controller: 'BooklistBoughtDetailsController',
                        controllerAs: 'vm'
                    }
                }
            }).state('bookcontent', {
                parent: 'app',
                url: '/bookContent',
                data: {
                    authorities: ['ROLE_USER'],
                    name: 'BOOKCONTENT'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/booklistbought/bookcontent.html'
                    }
                }
            }
        );
    }
})();
