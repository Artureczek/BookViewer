(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('LastIndexDetailController', LastIndexDetailController);

    LastIndexDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LastIndex'];

    function LastIndexDetailController($scope, $rootScope, $stateParams, previousState, entity, LastIndex) {
        var vm = this;

        vm.lastIndex = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bookViewerApp:lastIndexUpdate', function(event, result) {
            vm.lastIndex = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
