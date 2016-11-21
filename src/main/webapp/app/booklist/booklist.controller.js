(function () {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('BooklistController', BooklistController);

    BooklistController.$inject = ['ngTableParams', '$http', '$state', 'usSpinnerService' /*'NotificationService', 'sidebarService'*/];

    function BooklistController(ngTableParams, $http, $state, usSpinnerService/*, NotificationService, sidebarService*/) {
        var vm = this;
        vm.resourceUrl = 'users/all';
        vm.title = 'Lista Uzytkownikow';
        vm.filter = {};
        vm.loginUser = '';
        vm.sort = {'id': 'asc'};
        vm.doSearch = doSearch;
      //  vm.showDetailsBtn = showDetailsBtn;
      //  vm.showDetails = showDetails;
       // vm.displayPagesRange = displayPagesRange;

        //initMenu();
        vm.tableParams = getTableParams();

       /*function initMenu() {
            sidebarService.getMenu(function (items) {
                sidebarService.setMenu(items);
            });

            sidebarService.getQueuesProcessing();
        }*/

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
                        //NotificationService.error('Błąd: ' + error.message);
                    });
                }
            });
        }

        function doSearch(filterParams) {
            vm.filter = filterParams;
            vm.tableParams.reload();
        }

 /*       function showDetails(number, id) {
            $state.go('app.processed-id', {number: number, id: id});
        }

        function showDetailsBtn(item) {
            vm.loginUser = item.loginUser;
            return true;
        }*/

        function displayPagesRange() {
            var page = vm.tableParams.page();
            var count = vm.tableParams.count();
            var total = vm.tableParams.total();

            if (page * count > total) {
                var startItem = (page - 1) * count + 1;
                return startItem + '-' + total + ' z ' + total;
            }
            else {
                var startItem = (page - 1) * count + 1;
                var endItem = (page - 1) * count + count;
                return startItem + '-' + endItem + ' z ' + total;
            }
        }

        function startSpin() {
            usSpinnerService.spin('spinner-1');
        }

        function stopSpin() {
            usSpinnerService.stop('spinner-1');
        }
    }
})();
