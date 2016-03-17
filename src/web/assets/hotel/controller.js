'use strict';


angular.module('hotelCtrls', ['ngResource']).controller('hotelDetailsCtrl', ['$scope', '$resource', '$routeParams', function($scope, $resource, $routeParams) {
    var id = $routeParams.id;
    $scope.item = $resource(
            'http://api.tripagor.com/api/hotels/:id')
        .get({
            id: id
        });
}]);