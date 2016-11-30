(function () {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('BooklistBoughtDetailsController', BooklistBoughtDetailsController);

    BooklistBoughtDetailsController.$inject = ['$stateParams', '$scope', '$http', '$state', 'LoginService'];


    function BooklistBoughtDetailsController($stateParams,  $scope, $http, $state, LoginService) {
        var vm = this;
        vm.backToList = backToList;
        vm.book;
        vm.purchase;
        vm.settingsAccount = null;
        vm.showContent = showContent;
        vm.login = LoginService.open;
        vm.isDisabled = isDisabled;
        $scope.id = $stateParams.id;
        $scope.idPur = $stateParams.idPur;

        function isDisabled(){
           return !angular.equals(vm.purchase.status, 'Aktywny');
        }

        vm.downloadData = function () {

            $http.get('api/books/details/' + $scope.id).success(function(bookData) {
                vm.book = bookData;
            });
        };
        vm.downloadData();

        vm.downloadPurchaseData = function () {

            $http.get('api/purchase/details/' + $scope.idPur).success(function(purchaseData) {
                vm.purchase = purchaseData;
            });
        };
        vm.downloadPurchaseData();

        function showContent() {
            $state.go('bookcontent');
        }

        function backToList() {
            $state.go('booklist');
        }
    }
})();
