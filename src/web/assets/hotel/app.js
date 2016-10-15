'use strict';

angular.module('hotel', ['ngRoute', 'ngSanitize', , 'ui.bootstrap', 'hotelCtrls']).config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/:id/:name', {
        templateUrl: 'hotel/html/details.html',
        controller: 'hotelDetailsCtrl'
    });
}]).config(['$locationProvider', function($locationProvider) {
    $locationProvider.hashPrefix('!');
}]).filter('break', function() {
    return function(input) {
        if (input) {
            return input.split(",").join("<br>")
        }
    }
});
