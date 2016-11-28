(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('RateController', RateController);

    RateController.$inject = ['$scope', '$state', 'Rate'];

    function RateController ($scope, $state, Rate) {
        var vm = this;
        
        vm.rates = [];

        loadAll();

        function loadAll() {
            Rate.query(function(result) {
                vm.rates = result;
            });
        }
    }
})();
