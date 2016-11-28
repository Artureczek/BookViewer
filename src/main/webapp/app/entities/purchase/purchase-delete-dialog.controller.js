(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('PurchaseDeleteController',PurchaseDeleteController);

    PurchaseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Purchase'];

    function PurchaseDeleteController($uibModalInstance, entity, Purchase) {
        var vm = this;

        vm.purchase = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Purchase.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
