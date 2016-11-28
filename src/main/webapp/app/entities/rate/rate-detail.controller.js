(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('RateDetailController', RateDetailController);

    RateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Rate'];

    function RateDetailController($scope, $rootScope, $stateParams, previousState, entity, Rate) {
        var vm = this;

        vm.rate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bookViewerApp:rateUpdate', function(event, result) {
            vm.rate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
