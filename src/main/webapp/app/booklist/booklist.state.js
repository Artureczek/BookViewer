(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('booklist', {
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
        });
    }
})();
