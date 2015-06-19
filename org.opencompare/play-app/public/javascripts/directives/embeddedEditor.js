/**
 * Created by hvallee on 6/19/15.
 */

    pcmApp.directive('embeddedEditor', function() {
        return {
            templateUrl: 'pcmEditor.html'
        };
    });

    pcmApp.directive('openCompareEditor', function() {
        return {
            templateUrl: '/assets/editor/pcmEditor.html'
        };
    });