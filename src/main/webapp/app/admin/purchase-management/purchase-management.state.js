(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('purchase-management', {
                parent: 'admin',
                url: '/purchase-management',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    name: 'PURCHASEMANAGEMENT'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/admin/purchase-management/purchase-management.html',
                        controller: 'PurchaseManagementController',
                        controllerAs: 'vm'
                    }
                }
            });
    }
})();
