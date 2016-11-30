(function () {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('PurchaseManagementController', PurchaseManagementController);

    PurchaseManagementController.$inject = ['ngTableParams', '$http', '$state', 'usSpinnerService'];

    function PurchaseManagementController(ngTableParams, $http, $state, usSpinnerService) {
        var vm = this;
        vm.resourceUrl = 'api/purchases/all';
        vm.title = 'Lista Dokonanych Zakupów';
        vm.filter = {};
        vm.loginUser = '';
        vm.sort = {'id': 'asc'};
        vm.doSearch = doSearch;
        vm.tableParams = getTableParams();
        vm.activatePurchase = activatePurchase;
        vm.purchase = null;


        function getTableParams() {
            return new ngTableParams({
                page: 1,
                count: 10,
                sorting: vm.sort
            }, {
                counts: [5, 10, 20],
                getData: function ($defer, params) {
                    var s = JSON.stringify(params.sorting()).replace(/"|{|}/g, '').split(':');
                    startSpin();
                    $http.post(vm.resourceUrl, {
                        filter: vm.filter,
                        param: s[0],
                        order: s[1]
                    }, {params: {page: params.page() - 1, offset: params.count()}})
                        .success(function (data) {
                            params.total(data.total);
                            $defer.resolve(data.result);
                            stopSpin();
                        }).error(function (error) {
                        stopSpin();
                    });
                }
            });
        }

        function doSearch(filterParams) {
            vm.filter = filterParams;
            vm.tableParams.reload();
        }

        function activatePurchase(id) {
            $http.post('api/purchases/activate/' + id).success(function(bookData) {
                vm.purchase = bookData;
            });
            $state.reload();
        }

        function startSpin() {
            usSpinnerService.spin('spinner-1');
        }

        function stopSpin() {
            usSpinnerService.stop('spinner-1');
        }
    }
})();
