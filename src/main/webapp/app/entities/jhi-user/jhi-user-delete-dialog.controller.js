(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('JhiUserDeleteController',JhiUserDeleteController);

    JhiUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'JhiUser'];

    function JhiUserDeleteController($uibModalInstance, entity, JhiUser) {
        var vm = this;

        vm.jhiUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            JhiUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
