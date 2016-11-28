(function() {
    'use strict';
    angular
        .module('bookViewerApp')
        .factory('Countries', Countries);

    Countries.$inject = ['$resource'];

    function Countries ($resource) {
        var resourceUrl =  'api/countries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
