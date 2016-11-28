(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('CountriesDetailController', CountriesDetailController);

    CountriesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Countries'];

    function CountriesDetailController($scope, $rootScope, $stateParams, previousState, entity, Countries) {
        var vm = this;

        vm.countries = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bookViewerApp:countriesUpdate', function(event, result) {
            vm.countries = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
