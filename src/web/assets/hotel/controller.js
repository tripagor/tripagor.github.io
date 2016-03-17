'use strict';


angular.module('hotelCtrls', ['ngResource']).controller('hotelDetailsCtrl', ['$scope', '$resource', '$routeParams', function($scope, $resource, $routeParams) {
    $scope.item = $resource(
            'http://api.tripagor.com/api/hotels/:id')
        .get({
            id: $routeParams.id
        });
}])
