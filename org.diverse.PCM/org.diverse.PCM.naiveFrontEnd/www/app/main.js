define(function (require) {
    // Load any app-specific modules
    // with a relative require call,
    // like:
    var messages = require('./messages');

    console.log("main");


    // Load library/vendor modules using
    // full IDs, like:
    var pcmlib = require('pcm.merged');
    var print = require('print');

    print(messages.getHello());




    <!-- core -->

    var KPCMMM = Kotlin.modules['pcm'].pcm;
    var kev =    Kotlin.modules['pcm'] ;
    var factory  = new KPCMMM.factory.DefaultPcmFactory();
    <!-- ModelLoader -->
    var loader = factory.createJSONLoader();
    var saver = factory.createJSONSerializer();
    var modelElm = null;

    var compare = factory.createModelCompare();

    $.getJSON('/org.diverse.PCM/org.diverse.PCM.naiveFrontEnd/www/PCM_data/Boomboxes.json', function(data) {


        console.log(data);
        <!-- Loading the model -->
        var  kPCM =   loader.loadModelFromString(JSON.stringify(data)).get(0);
        <!-- Accessing to the attributes PCM class cf metamodel in the project PCM.Model -->
        console.log(kPCM.generated_KMF_ID);

        console.log(kPCM.metaClassName());
        console.log(kPCM.name);
        <!-- Number of product -->
        console.log( + kPCM.products.size());
        <!-- Number of Abstract feature -->
        console.log( + kPCM.features.size());

        // Test type (a better solution exists...)
        var aFeature = kPCM.features.get(0);
        console.log("pcm.FeatureGroup" === aFeature.metaClassName())

        <!-- Go other all the products -->
        var products = kPCM.products ;
        for (var i = 0; i < products.size(); i++) {
            console.log(products.get(i).name);
        }

        //Model Modification
        // changing the name :
        kPCM.name = "new name" ;
        console.log(kPCM.name);

        var jsonModelData = saver.serialize(kPCM);
        // to do implement the saveusing https://github.com/eligrey/FileSaver.js


    });


});
