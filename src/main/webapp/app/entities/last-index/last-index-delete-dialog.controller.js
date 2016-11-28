(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('LastIndexDeleteController',LastIndexDeleteController);

    LastIndexDeleteController.$inject = ['$uibModalInstance', 'entity', 'LastIndex'];

    function LastIndexDeleteController($uibModalInstance, entity, LastIndex) {
        var vm = this;

        vm.lastIndex = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LastIndex.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
