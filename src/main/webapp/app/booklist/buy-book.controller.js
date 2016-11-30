(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('BuyBookController', BuyBookController);

    BuyBookController.$inject = ['$state', 'Principal', '$http', '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Book'];

    function BuyBookController ($state, Principal, $http, $timeout, $scope, $stateParams, $uibModalInstance, entity, Book) {
        var vm = this;

        vm.book = entity;
        vm.clear = clear;
        vm.save = save;
        vm.title = 'Zakup dostęp do książki';
        vm.count = 0;
        vm.date = new Date();
        vm.addDays = addDays;
        vm.createPurchase = createPurchase;
        vm.settingsAccount = null;

        function addDays(date, days) {
            var result = new Date(date);
            result.setDate(result.getDate() + days);
            return result;
        }

        function createPurchase(){
            $http.put('api/purchase/' + vm.settingsAccount.login + '/' + vm.book.id + '/' + vm.count).success(function() {
                $state.go('booklist');
                $uibModalInstance.dismiss('cancel');
            });

        }

        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });




        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.book.id !== null) {
                Book.update(vm.book, onSaveSuccess, onSaveError);
            } else {
                Book.save(vm.book, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bookViewerApp:bookUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
