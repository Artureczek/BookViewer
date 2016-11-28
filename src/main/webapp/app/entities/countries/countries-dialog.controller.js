(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('CountriesDialogController', CountriesDialogController);

    CountriesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Countries'];

    function CountriesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Countries) {
        var vm = this;

        vm.countries = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.countries.id !== null) {
                Countries.update(vm.countries, onSaveSuccess, onSaveError);
            } else {
                Countries.save(vm.countries, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bookViewerApp:countriesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
