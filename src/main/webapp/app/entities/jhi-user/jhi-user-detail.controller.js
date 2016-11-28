(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('JhiUserDetailController', JhiUserDetailController);

    JhiUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'JhiUser'];

    function JhiUserDetailController($scope, $rootScope, $stateParams, previousState, entity, JhiUser) {
        var vm = this;

        vm.jhiUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bookViewerApp:jhiUserUpdate', function(event, result) {
            vm.jhiUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
