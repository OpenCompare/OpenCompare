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
        var arr =this.getDepth(featureCollection)

        var max = Math.max.apply(Math,arr);

        return max;
    },

    getDepthFt : function(feature)
    {
        var res = 0 ;


        if ("pcm.FeatureGroup" === feature.metaClassName()) {

            res = 1 + this.getMaxDepth(feature.subFeatures) ;

        };
        return res;
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

   getPCMHtml: function(PCM)
   {
       var html ='<table class="table">' + '\n <thead>' ;
       var depth = this.getMaxDepth(PCM.features);
       var vect =  Array(depth).join(".").split(".");
       this.buildPcmHeader(PCM.features,vect,0);
       for(var i = 0 ; i < vect.length ; i ++)
       {
          html = html +' <tr>' + vect[i] + '\n </tr>';
       }
       html = html  + '\n </thead>' ;
       html = html + '\n </table>' ;
       return html ;
   },

    buildPcmHeader: function(featureCollection, vect, rank)
    {
        var maxDepth = this.getMaxDepth(featureCollection) ;
        for (var i = 0; i < featureCollection.size(); i++) {
            var currFeature = featureCollection.get(i);
            var nbChild= this.getNumberOfChildRec(currFeature) ;
            var rawspan = maxDepth - rank - this.getDepthFt(currFeature);
            if(nbChild == 0)
            {
                nbChild = 1 ;
            }
            vect[rank] = vect[rank] + "<th id="+ currFeature.generated_KMF_ID+" rowspan="+ rawspan+" colspan="+nbChild+">" + currFeature.name + "</th>";

            if ("pcm.FeatureGroup" === currFeature.metaClassName()) {
                var rank2 = rank +1 ;
                this.buildPcmHeader(currFeature.subFeatures, vect, rank2);

            }
        }
    }

});

