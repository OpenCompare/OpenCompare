/**
 * Created by gbecan on 6/23/15.
 */

pcmApp.config(function ($translateProvider) {
    $translateProvider.translations('embedded', {

        'view.button.edit':'Edit',
        'edit.title':'Title',
        'edit.title.placeholder':'Title',
        'edit.title.createNewFeature':'Create a new feature',
        'edit.title.featureName':'Feature Name',
        'edit.title.featureType':'Feature type',
        'edit.title.renameFeature':'Rename',
        'edit.title.changeType':'Change type',
        'edit.title.selectRange':'Select a range to filter',
        'edit.title.from':'From',
        'edit.title.to':'To',
        'edit.title.selectProducts':'Select products to filter',
        'edit.title.embed':'Embed',
        'edit.title.showTitle':'Show title',
        'edit.title.allowEdition':'Allow edition',
        'edit.title.allowExportation':'Allow exportation',
        'edit.title.allowSharing':'Allow sharing',
        'edit.title.confirm':'Confirm',

        'edit.button.edit':'Edit',
        'edit.button.export':'Export',
        'edit.button.save':'Save',
        'edit.button.validate':'Validate',
        'edit.button.embed':'Embed',
        'edit.button.remove':'Remove',
        'edit.button.addfeature':'Add feature',
        'edit.button.addproduct':'Add product',
        'edit.button.cancel':'Cancel',
        'edit.button.apply':'Apply',
        'edit.button.confirm':'Are you sure?',
        'edit.button.no':'No',
        'edit.button.yes':'Yes',
        'edit.button.share':'Share',
        'edit.button.height':'Row height',

        'edit.type.string':'String',
        'edit.type.boolean':'Boolean',
        'edit.type.number':'Number',


        'edit.warning.removeIsDefinitive':'Removing the comparison matrix is definitive and cannot be undone. Please, confirm.',
        'edit.warning.cancelWillLoseChanges':'Cancel will lose all unsaved changes are you sure?',
        'edit.validation.warning':'This value doesn\'t seem to match the feature type, validate if you want to keep it.',

        'importer.select':'Select a file to import',
        'importer.select.url':'Give a wikipedia matrix title',
        'importer.type':'Select a file extention',
        'importer.csv.separator':'Separator',
        'importer.csv.quote':'Quote',
        'importer.csv.header':'Has header ?',
        'importer.pcm.title':'Title',
        'importer.pcm.productAsLines':'Product as lines ?',
        'importer.button.getfile':'Select a file',
        'importer.button.cancel':'Cancel',
        'importer.button.confirm':'Confirm',
        'importer.button.confirm.message':'Are you sure to import?',
        'importer.button.confirm.desc':'Please, give a name to this new PCM and confirm.'

    });
    $translateProvider.useSanitizeValueStrategy('escaped');
    $translateProvider.useLoader('i18nLoader');
    $translateProvider.preferredLanguage('oc');
});

pcmApp.factory('i18nLoader', function($http, $q, $translate) {

    return function(options) {
        var deferred = $q.defer();

        $http.get("/api/i18n").success(function (data) {
            $translate.use('oc');
            return deferred.resolve(data);
        }).
        error(function(data, status, headers, config) {
                $translate.use('embedded');
        });

        return deferred.promise;
    }
});


pcmApp.controller("I18nCtrl", function($scope, $http) {

    $scope.changeLanguage = function(langKey) {
        $http.get("/api/i18n/" + langKey).success(function (data) {
            window.location.reload();
        });
    };


});