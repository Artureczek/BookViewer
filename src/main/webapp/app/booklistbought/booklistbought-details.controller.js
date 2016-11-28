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
        vm.settingsAccount = null;
        vm.showContent = showContent;
        vm.login = LoginService.open;
        $scope.id = $stateParams.id;

        vm.downloadData = function () {

            $http.get('api/books/details/' + $scope.id).success(function(bookData) {
                vm.book = bookData;
            });
        };
        vm.downloadData();

        function showContent() {
            $state.go('bookcontent');
        }

        function backToList() {
            $state.go('booklist');
        }
    }
})();
