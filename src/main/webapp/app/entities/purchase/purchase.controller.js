(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('PurchaseController', PurchaseController);

    PurchaseController.$inject = ['$scope', '$state', 'Purchase'];

    function PurchaseController ($scope, $state, Purchase) {
        var vm = this;
        
        vm.purchases = [];

        loadAll();

        function loadAll() {
            Purchase.query(function(result) {
                vm.purchases = result;
            });
        }
    }
})();
