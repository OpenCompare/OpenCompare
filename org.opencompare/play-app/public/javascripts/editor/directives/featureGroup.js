pcmApp.directive('superColWidthUpdate', ['$timeout', '$rootScope', function ($timeout, $rootScope) {
        return {
            'restrict': 'A',
            'link': function (scope, element) {
                var _colId = scope.col.colDef.superCol,
                    _el = jQuery(element);
                _el.on('resize', function () {
                    _updateSuperColWidth();
                });
                var _updateSuperColWidth = function () {
                    var _colId = scope.col.colDef.superCol,
                        _el = jQuery(element);
                    $timeout(function () {
                            if(_colId) {
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
                            }
                        }
                       , 0);
                };
                _updateSuperColWidth();

                var reloadFeatureGroup  = function () {
                    var _colId = scope.col.colDef.superCol,
                        _el = jQuery(element);
                    $timeout(function () {
                            if(_colId) {
                                var _parentCol = jQuery('.ui-grid-header-cell[col-name="' + _colId + '"]');
                                _parentCol.css({
                                    'min-width': 0 + 'px',
                                    'max-width': 0 + 'px'
                                });
                                _updateSuperColWidth();
                            }
                        }
                        , 0);

                };

                $rootScope.$on('reloadFeatureGroup', reloadFeatureGroup);

            }
        };
    }]);

