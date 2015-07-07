/**
 * Created by Pratiti .
 */

pcmApp.directive('categoryHeader', function($window) {
    function link(scope) {


        scope.$watch('gridOptions.columnDefs', function(newValue, oldValue) {


            console.log(scope.gridOptions.columnDefs);
            scope.headerRowHeight = 30;
            scope.catHeaderRowHeight = scope.headerRowHeight + 'px';
            scope.categories = [];
            var lastDisplayName = "";
            var totalWidth = 0;
            var left = 0;
            cols = scope.gridOptions.columnDefs;
            for (var i = 0; i < cols.length; i++) {

                totalWidth += Number(cols[i].width);

                var displayName = (typeof(cols[i].categoryDisplayName) === "undefined") ?
                    "\u00A0" : cols[i].categoryDisplayName;

                if (displayName !== lastDisplayName) {

                    scope.categories.push({
                        displayName: lastDisplayName,
                        width: totalWidth - Number(cols[i].width),
                        widthPx: (totalWidth - Number(cols[i].width)) + 'px',
                        left: left,
                        leftPx: left + 'px'
                    });

                    left += (totalWidth - Number(cols[i].width));
                    totalWidth = Number(cols[i].width);
                    lastDisplayName = displayName;
                }
            }

            if (totalWidth > 0) {

                scope.categories.push({
                    displayName: lastDisplayName,
                    width: totalWidth,
                    widthPx: totalWidth + 'px',
                    left: left,
                    leftPx: left + 'px'
                });
            }
        });
    }

    return {


        templateUrl: '/assets/templates/featureGroupHeader.html',
        link: link
    };

});