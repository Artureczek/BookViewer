(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('JhiUserDialogController', JhiUserDialogController);

    JhiUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'JhiUser'];

    function JhiUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, JhiUser) {
        var vm = this;

        vm.jhiUser = entity;
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
            if (vm.jhiUser.id !== null) {
                JhiUser.update(vm.jhiUser, onSaveSuccess, onSaveError);
            } else {
                JhiUser.save(vm.jhiUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bookViewerApp:jhiUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDay = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
