(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('LastIndexDialogController', LastIndexDialogController);

    LastIndexDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LastIndex'];

    function LastIndexDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LastIndex) {
        var vm = this;

        vm.lastIndex = entity;
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
            if (vm.lastIndex.id !== null) {
                LastIndex.update(vm.lastIndex, onSaveSuccess, onSaveError);
            } else {
                LastIndex.save(vm.lastIndex, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bookViewerApp:lastIndexUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
