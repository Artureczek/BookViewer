(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('CountriesController', CountriesController);

    CountriesController.$inject = ['$scope', '$state', 'Countries'];

    function CountriesController ($scope, $state, Countries) {
        var vm = this;
        
        vm.countries = [];

        loadAll();

        function loadAll() {
            Countries.query(function(result) {
                vm.countries = result;
            });
        }
    }
})();
