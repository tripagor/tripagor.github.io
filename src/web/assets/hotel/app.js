'use strict';

angular.module('hotel', ['ngRoute', 'hotelCtrls']).config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/:id/:name', {
        templateUrl: 'hotel/html/details.html',
        controller: 'hotelDetailsCtrl'
    });
}]).config(['$locationProvider', function($locationProvider) {
    $locationProvider.hashPrefix('!');
}]).factory('authInterceptor', [
    "$q", "$window", "$location",
    function($q, $window, $location) {
        return {
            request: function(config) {
                config.headers = config.headers || {};
                config.headers.Authorization = 'Bearer e3436a40-eb21-4fbb-ac41-11f11716c85a'; // + session.get('token'); // add your token from your service or whatever
                return config;
            },
            response: function(response) {
                return response || $q.when(response);
            },
            responseError: function(rejection) {
                // your error handler
            }
        };
    }
]).config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('authInterceptor');
}]);
