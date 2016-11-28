(function() {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('LastIndexController', LastIndexController);

    LastIndexController.$inject = ['$scope', '$state', 'LastIndex'];

    function LastIndexController ($scope, $state, LastIndex) {
        var vm = this;
        
        vm.lastIndices = [];

        loadAll();

        function loadAll() {
            LastIndex.query(function(result) {
                vm.lastIndices = result;
            });
        }
    }
})();
