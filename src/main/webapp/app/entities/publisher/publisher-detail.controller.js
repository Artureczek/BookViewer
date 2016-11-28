(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('PublisherDetailController', PublisherDetailController);

    PublisherDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Publisher'];

    function PublisherDetailController($scope, $rootScope, $stateParams, previousState, entity, Publisher) {
        var vm = this;

        vm.publisher = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bookViewerApp:publisherUpdate', function(event, result) {
            vm.publisher = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
