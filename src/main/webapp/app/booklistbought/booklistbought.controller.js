(function () {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('BooklistBoughtController', BooklistBoughtController);

    BooklistBoughtController.$inject = ['Principal', 'ngTableParams', '$http', '$state', 'usSpinnerService' /*'NotificationService', 'sidebarService'*/];

    function BooklistBoughtController(Principal, ngTableParams, $http, $state, usSpinnerService/*, NotificationService, sidebarService*/) {
        var vm = this;
        vm.title = 'Lista Zakupionych Książek';
        vm.filter = {};
        vm.loginUser = '';
        vm.sort = {'title': 'asc'};
        vm.doSearch = doSearch;
        vm.showDetails = showDetails;
        vm.tableParams = getTableParams();
        vm.settingsAccount = null;

        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            };
        };

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
        });


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
                    $http.post('api/purchasedbooks/' + vm.settingsAccount.login, {
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

        function showDetails(id) {
            $state.go('booklistbought-details-id', {id: id});
        }


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
