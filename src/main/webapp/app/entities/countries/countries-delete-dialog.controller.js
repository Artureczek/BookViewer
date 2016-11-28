(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('CountriesDeleteController',CountriesDeleteController);

    CountriesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Countries'];

    function CountriesDeleteController($uibModalInstance, entity, Countries) {
        var vm = this;

        vm.countries = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Countries.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
