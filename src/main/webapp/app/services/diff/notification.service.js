(function () {
    'use strict';

    angular
        .module('bookViewerApp')
        .service('NotificationService', NotificationService)
        .config(function (NotificationProvider) {
            notificationServiceConfig(NotificationProvider);
        });

    function notificationServiceConfig(NotificationProvider) {
        NotificationProvider.setOptions({
            delay: 10000,
            startTop: 20,
            startRight: 10,
            verticalSpacing: 10,
            horizontalSpacing: 10,
            positionX: 'right',
            positionY: 'top'
        });
    }

    NotificationService.$inject = ['Notification'];
    function NotificationService(Notification) {
        var service = {
            primary: primary,
            success: success,
            error: error
        };

        return service;

        ////////////

        function primary(data) {
            Notification.primary(data);
        }

        function success(data) {
            Notification.success(data);
        }

        function error(data) {
            Notification.error(filterErrorMessage(data));

        }

        function filterErrorMessage(data) {
            if (data.indexOf('expired') !== -1) {
                return 'Twoja sesja wygasłą. Zaloguj się ponownie, aby korzystać z serwisu.';
            }
            return data;
        }


    }

})();
