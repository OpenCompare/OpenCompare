/**
 * Created by hvallee on 6/19/15.
 */

    pcmApp.directive('openCompareEditor', function() {
        return {
            templateUrl: '/assets/editor/templates/pcmEditor.html'
        };
    });

    pcmApp.directive('embedOpenCompareEditor', function() {
        return {
            templateUrl: 'pcmEditor.html'
        };
    });