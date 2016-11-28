(function() {
    'use strict';
    angular
        .module('bookViewerApp')
        .factory('JhiUser', JhiUser);

    JhiUser.$inject = ['$resource', 'DateUtils'];

    function JhiUser ($resource, DateUtils) {
        var resourceUrl =  'api/jhi-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDay = DateUtils.convertLocalDateFromServer(data.createdDay);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdDay = DateUtils.convertLocalDateToServer(copy.createdDay);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdDay = DateUtils.convertLocalDateToServer(copy.createdDay);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
