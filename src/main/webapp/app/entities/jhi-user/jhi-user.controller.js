(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('JhiUserController', JhiUserController);

    JhiUserController.$inject = ['$scope', '$state', 'JhiUser'];

    function JhiUserController ($scope, $state, JhiUser) {
        var vm = this;
        
        vm.jhiUsers = [];

        loadAll();

        function loadAll() {
            JhiUser.query(function(result) {
                vm.jhiUsers = result;
            });
        }
    }
})();
