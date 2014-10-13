/**
 * Created by Aymeric on 13/10/2014.
 */

define(['require'],{


// recursive function to compute
// the final number of column in the matrix

        getNumberOfFeatures: function(featureCollection)
        {
            var val = 0
            for(var i = 0 ; i < featureCollection.size() ; i++)
            {
                var currFeature = featureCollection.get(i);

                if( "pcm.FeatureGroup" === currFeature.metaClassName()){
                    val = val + this.getNumberOfFeatures(currFeature.subFeatures);
            }else{
                val = val + 1;
            }

            }
            return val ;
        },


    buildHeader: function(featureCollection) {
        console.log("New line ");
        var collection;
        var val = 0
        for (var i = 0; i < featureCollection.size(); i++) {
            var currFeature = featureCollection.get(i);

            if ("pcm.FeatureGroup" === currFeature.metaClassName()) {

                val = val + this.getNumberOfFeatures(currFeature.subFeatures);
            } else {
                val = val + 1;
            }

        }
        return val;
    },

    getDepth: function(featureCollection) {

        var res = Array.apply(null, new Array(featureCollection.size())).map(Number.prototype.valueOf,1);

        for (var i = 0; i < featureCollection.size(); i++) {
            var currFeature = featureCollection.get(i);

            if ("pcm.FeatureGroup" === currFeature.metaClassName()) {
               var res2 =  this.getDepth(currFeature.subFeatures) ;
                var max =   Math.max.apply(Math, res2);

                  res[i] = res[i] + max ;


            }
        }
        return res ;
    },

    getMaxDepth: function(featureCollection){
        return Math.max.apply(Math, this.getDepth(featureCollection));
    },


    getNumberOfChildRec: function(feature)
    {
        var res = 0 ;
        if ("pcm.FeatureGroup" === feature.metaClassName()) {
          for (var i = 0 ; i < feature.subFeatures.size(); i++)
          {
              res = res + this.getNumberOfChildRec(feature.subFeatures.get(i));
          }

        }else
        {
            res = res + 1 ;
        }
        return res ;
    },

    buildPcmHeader: function(featureCollection)
    {
        var htmlCode = "" ;


        return htmlCode ;
    }

});

