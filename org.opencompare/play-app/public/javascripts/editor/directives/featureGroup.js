pcmApp.directive('superColWidthUpdate', ['$timeout', function ($timeout) {
        return {
            'restrict': 'A',
            'link': function (scope, element) {
                var _colId = scope.col.colDef.superCol,
                    _el = jQuery(element);
                _el.on('resize', function () {
                    _updateSuperColWidth();
                });
                var _updateSuperColWidth = function () {
                    $timeout(function () {
                        var _parentCol = jQuery('.ui-grid-header-cell[col-name="' + _colId + '"]');
                        var _parentWidth = _parentCol.outerWidth(),
                            _width = _el.outerWidth();

                        if (_parentWidth + 1 >= _width) {
                            _parentWidth = _parentWidth + _width;
                        } else {
                            _parentWidth = _width;
                        }

                        _parentCol.css({
                            'min-width': _parentWidth + 'px',
                            'max-width': _parentWidth + 'px',
                            'text-align': "center"
                        });
                        }, 0);
                };
                _updateSuperColWidth();
            }
        };
    }]);

