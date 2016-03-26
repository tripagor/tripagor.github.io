'use strict';


angular.module('hotelCtrls', ['ngResource']).controller('hotelDetailsCtrl', ['$scope', '$resource', '$routeParams', function($scope, $resource, $routeParams) {
    $scope.item = $resource(
            'http://api.tripagor.com/hotels/:id')
        .get({
            id: $routeParams.id
        });
    $scope.item.$promise.then(function(result) {
        $scope.item = result;

        $scope.class = result.hotelClass;
        if (result.hotelClass == 0) {
            $scope.class = 1;
        }

        $scope.rate = Math.floor(Math.random() * 8) + 6;
    });

}]);
