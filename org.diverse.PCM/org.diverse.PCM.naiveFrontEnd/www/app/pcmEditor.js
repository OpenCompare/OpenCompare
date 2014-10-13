/**
 * Created by Aymeric on 13/10/2014.
 */
include('js/require.js');

var _getAllFilesFromFolder  = require(['require', 'fs'], function (dir) {
    var filesystem = require("fs");
    var results = [];

    filesystem.readdirSync(dir).forEach(function(file) {

        file = dir+'/'+file;
        var stat = filesystem.statSync(file);

        if (stat && stat.isDirectory()) {
            results = results.concat(_getAllFilesFromFolder(file))
        } else results.push(file);

    });

    return results;
});

