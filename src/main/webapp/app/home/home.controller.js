(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Auth', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, Auth, Principal, LoginService, $state) {
        var vm = this;

        vm.logout = logout;
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function logout() {
            Auth.logout();
            $state.go('home');
        }
    }
})();
