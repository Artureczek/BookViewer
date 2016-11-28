(function () {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('BooklistDetailsController', BooklistDetailsController);

    BooklistDetailsController.$inject = ['$stateParams', '$scope', 'Principal', '$http', '$state', 'LoginService'];


    function BooklistDetailsController($stateParams,  $scope, Principal, $http, $state, LoginService) {
        var vm = this;
        vm.backToList = backToList;
        vm.book;
        vm.settingsAccount = null;
        vm.createPurchase = createPurchase;
        vm.login = LoginService.open;
        $scope.id = $stateParams.id;


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

        vm.downloadData = function () {

            $http.get('api/books/details/' + $scope.id).success(function(bookData) {
                vm.book = bookData;
            });
        };
        vm.downloadData();

        function createPurchase(){

            $http.put('api/purchase/' + vm.settingsAccount.login + '/' + $scope.id).success(function() {

            });

           // $http.put('api/purchase/create/user/' + $scope.id);
        }


        function backToList() {
            $state.go('booklist');
        }
    }
})();
