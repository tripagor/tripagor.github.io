'use strict';


angular.module('hotelCtrls', ['ngResource']).controller('hotelDetailsCtrl', ['$scope', '$resource', '$routeParams', function($scope, $resource, $routeParams) {
    $scope.item = $resource(
            'http://api.tripagor.com/hotels/:id')
        .get({
            id: $routeParams.id
        });
    $scope.item.$promise.then(function(result) {
        $scope.item = result;
        $scope.rate = result.hotelClass + 1;
    });

    $scope.max = 6;
    $scope.isReadonly = false;

    $scope.hoveringOver = function(value) {
        $scope.overStar = value;
        $scope.percent = 100 * (value / $scope.max);
    };

    $scope.ratingStates = [
        { stateOn: 'glyphicon-ok-sign', stateOff: 'glyphicon-ok-circle' },
        { stateOn: 'glyphicon-star', stateOff: 'glyphicon-star-empty' },
        { stateOn: 'glyphicon-heart', stateOff: 'glyphicon-ban-circle' },
        { stateOn: 'glyphicon-heart' },
        { stateOff: 'glyphicon-off' }
    ];
}]);
