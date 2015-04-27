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

    getColumn: function(feature,featureOrder){

        for (var i = 0; i < featureOrder.length; i++) {
            if(feature.name === featureOrder[i].name)
            {
                return i ;
            }
        }
        return -1 ;
},
        getHTMLProduct: function(products,featureOrder,NbOfFeatures)
    {
       var prodMatrix = new Array(products.size() ) ;
        for(var i = 0 ; i < products.size() ; i ++)

        {

           var currProd = products.get(i);
            var cells = currProd.values ;
            var prodArr = new Array(NbOfFeatures) ;

            for (var j = 0 ; j < cells.size() ; j++){

                var currCell = cells.get(j);
                prodArr[this.getColumn(currCell.feature,featureOrder)] = currCell ;
            }
            prodMatrix[i] = prodArr ;
        }


        var html = " <tr> \n " ;
        for (var i = 0 ; i < prodMatrix.length ; i ++)
        {

            var prod =    prodMatrix[i] ;

            html = html +'<td id="'+ products.get(i).path()+'" class="prod">' + products.get(i).name +'</td> \n ' ;
            for (var j = 0 ; j < prod.length ;j ++)
            {

                var cell = prod[j];

                if(cell != null)
                {
                    html = html + '<td id="'+ cell.path()+'"  class="cell">' + cell.content +'</td> \n ' ;

                }
                else{
                    html =html + "<td></td> \n " ;
            }

            }
            html = html +" </tr> " ;

        }

return html;

    },


   getPCMHtml: function(PCM)
   {
       var html ='<table class="table" id="pcm">' + '\n <thead>' ;
       var depth = this.getMaxDepth(PCM.features);
       var vect =  Array(depth).join(".").split(".");
       var featureOrder = new Array() ;
       this.buildPcmHeader(PCM.features,vect,0,featureOrder);



       for(var i = 0 ; i < vect.length ; i ++)
       {
           if(i == 0)
           {
               html = html +' <tr><th rowspan=2> Product </th>  '  + vect[i] + '\n </tr>';

           }else{
               html = html +' <tr>' + vect[i] + '\n </tr>';
           }

       }
       html = html  + '\n </thead>' ;
       html = html  + '\n <tbody>' ;
       html = html + this.getHTMLProduct(PCM.products, featureOrder,this.getNumberOfFeatures(PCM.features));
       html = html  + '\n </tbody>' ;
       html = html + '\n </table>' ;

       return html ;
   },

    buildPcmHeader: function(featureCollection, vect, rank,featureOrder)
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
            vect[rank] = vect[rank] + '<th id="'+ currFeature.generated_KMF_ID+'" rowspan="'+ rawspan+'" colspan="'+nbChild+'">' + currFeature.name + '</th>';

            if ("pcm.FeatureGroup" === currFeature.metaClassName()) {
                var rank2 = rank +1 ;
                this.buildPcmHeader(currFeature.subFeatures, vect, rank2,featureOrder);

            }
            else{
                featureOrder.push(currFeature);
            }

        }
    },
    addProduct: function(PCM, Name){
        var KPCMMM = Kotlin.modules['pcm'].pcm ;
        var factory  = new KPCMMM.factory.DefaultPcmFactory();
        var prod = factory.createProduct();
        prod.name = Name ;
        var features = PCM.features ;
        console.log(  PCM.products);

       this. populateProd(prod,PCM.features,factory);
        PCM.addProducts(prod);

    },

    populateProd: function(product, featureCollection, factory)
    {
        for (var i = 0; i < featureCollection.size(); i++) {

            var currFeature = featureCollection.get(i);

            if ("pcm.FeatureGroup" === currFeature.metaClassName()) {
                this.populateProd(product,currFeature.subFeatures,factory);

            }
            else{
              var cell = factory.createCell();
               cell.content ="";
                cell.feature =currFeature;
                product.addValues(cell);
            }

        }

    },

    addFeature: function(PCM, Name){
        var KPCMMM = Kotlin.modules['pcm'].pcm ;
        var factory  = new KPCMMM.factory.DefaultPcmFactory();
        var feature = factory.createFeature();
        feature.name = Name ;
        PCM.addFeatures(feature) ;
        for (var i = 0 ; i < PCM.products.size(); i++){
            var currProd = PCM.products.get(i);
            var cell = factory.createCell();
            cell.content ="";
            cell.feature =feature;
            currProd.addValues(cell);

        }

    }




});

