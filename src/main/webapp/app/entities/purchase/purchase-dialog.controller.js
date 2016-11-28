(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('PurchaseDialogController', PurchaseDialogController);

    PurchaseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Purchase'];

    function PurchaseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Purchase) {
        var vm = this;

        vm.purchase = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.purchase.id !== null) {
                Purchase.update(vm.purchase, onSaveSuccess, onSaveError);
            } else {
                Purchase.save(vm.purchase, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bookViewerApp:purchaseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
