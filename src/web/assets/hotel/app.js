'use strict';

angular.module('hotel', ['ngRoute', 'hotelCtrls']).config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/:id/:name', {
        //templateUrl: 'hotel/html/details.html',
        template: ' ',
        controller: 'hotelDetailsCtrl'
    });
}]).config(['$locationProvider', function($locationProvider) {
    $locationProvider.hashPrefix('!');
}]);
