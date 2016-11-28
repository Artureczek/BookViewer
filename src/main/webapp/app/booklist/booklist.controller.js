(function () {
    'use strict';

    angular
        .module('bookViewerApp')
        .controller('BooklistController', BooklistController);

    BooklistController.$inject = ['ngTableParams', '$http', '$state', 'usSpinnerService' /*'NotificationService', 'sidebarService'*/];

    function BooklistController(ngTableParams, $http, $state, usSpinnerService/*, NotificationService, sidebarService*/) {
        var vm = this;
        vm.resourceUrl = 'api/books/all';
        vm.title = 'Lista Dostępnych Książek';
        vm.filter = {};
        vm.loginUser = '';
        vm.sort = {'title': 'asc'};
        vm.doSearch = doSearch;
        vm.showDetails = showDetails;
        vm.tableParams = getTableParams();
        vm.deleteBook=deleteBook;
        vm.createBook=createBook;


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

        function showDetails(id) {
            $state.go('booklist-details-id', {id: id});
        }

        function createBook() {
            $state.go('book-detail.edit');
        }

        function deleteBook(id) {
            $http.delete('api/books/' + id).success(function(bookData) {
                vm.book = bookData;
            });
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
