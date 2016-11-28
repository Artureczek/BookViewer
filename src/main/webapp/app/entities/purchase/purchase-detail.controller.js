(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('PurchaseDetailController', PurchaseDetailController);

    PurchaseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Purchase'];

    function PurchaseDetailController($scope, $rootScope, $stateParams, previousState, entity, Purchase) {
        var vm = this;

        vm.purchase = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bookViewerApp:purchaseUpdate', function(event, result) {
            vm.purchase = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
