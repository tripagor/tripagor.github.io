'use strict';


angular.module('hotelCtrls', ['ngResource']).controller('hotelDetailsCtrl', ['$scope', '$resource', '$routeParams', function($scope, $resource, $routeParams) {
    var id = $routeParams.id;
    $scope.item = $resource(
            'http://ltripagor.github.io/api/hotels/:id')
        .get({
            id: id
        });
}]);
gcloud compute firewall-rules create default-allow-mongo  --allow tcp:27017   --source-ranges 0.0.0.0/0  --target-tags mongodb   --description "Allow mongodb access to all IPs"