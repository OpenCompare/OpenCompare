function MyPcmApi(){
    var pcmMM = Kotlin.modules['org.opencompare.model.pcm'].org.opencompare.model.pcm;
    this.factory = new pcmMM.factory.DefaultPcmFactory();
    var loader = this.factory.createJSONLoader();
    var serializer = this.factory.createJSONSerializer();

    var api = this;

    /**
     * Sort two elements by their names (accessed with x.name)
     * @param a
     * @param b
     * @returns {number}
     */
    api.sortByName = function (a, b) {
        if (a.name < b.name) {
            return -1;
        } else if (a.name > b.name) {
            return 1;
        } else {
            return 0;
        }
    };


    api.createFeature = function (name, pcm, factory) {
        // Create feature
        var feature = factory.createFeature();
        feature.name = name;
        pcm.addFeatures(feature);

        // Create corresponding cells for all products
        for (var i = 0; i < pcm.products.array.length; i++) {
            var cell = factory.createCell();
            cell.content = "";
            cell.feature = feature;
            pcm.products.array[i].addValues(cell);
        }

        return feature;
    };

    api.getConcreteFeatures = function (pcm) {

      function getConcreteFeaturesRec(aFeature) {
          var features = [];

          if (typeof aFeature.subFeatures !== 'undefined') {
              var subFeatures = aFeature.subFeatures.array;
              for (var i = 0; i < subFeatures.length; i++) {
                  var subFeature = subFeatures[i];
                  features = features.concat(getConcreteFeaturesRec(subFeature));
              }
          } else {
              features.push(aFeature);
          }

          return features;
      }

      var features = [];

      if (typeof pcm.features !== 'undefined') {
        var aFeatures = pcm.features.array;

        for (var i = 0; i < aFeatures.length; i++) {
          var aFeature = aFeatures[i];
          features = features.concat(getConcreteFeaturesRec(aFeature))
        }
      }

      return features;
    };

    api.findCell = function (product, feature) {
        var cells = product.cells.array;
        for (var i = 0; i < cells.length; i++) {
            var cell = cells[i];
            if (cell.feature.name === feature.name) {
                return cell;
            }
        }
    };

    api.encodePCM = function (pcm) {
        this.base64PCMVisitor(pcm, true);
        return pcm;
    };

    api.decodePCM = function (pcm) {
        this.base64PCMVisitor(pcm, false);
        return pcm;
    };

    api.base64PCMVisitor = function (pcm, encoding) {
        function encodeToBase64(str, encoding) {
            if (encoding) {
                return Base64.encode(str);
            } else {
                return Base64.decode(str);
            }
        }

        function base64FeatureVisitor(feature, encoding) {
            feature.name = encodeToBase64(feature.name, encoding);

            if (typeof feature.subFeatures !== 'undefined') {
                feature.subFeatures.array.forEach(function (subFeature) {
                    base64FeatureVisitor(subFeature, encoding);
                });
            }
        }

        pcm.name = encodeToBase64(pcm.name, encoding);
        pcm.features.array.forEach(function (feature) {
            base64FeatureVisitor(feature, encoding);
        });

        pcm.products.array.forEach(function (product) {
            product.cells.array.forEach(function (cell) {
                cell.content = encodeToBase64(cell.content, encoding);
                cell.rawContent = encodeToBase64(cell.rawContent, encoding);
            });
        });
    };

    api.loadPCMModelFromString = function(json) {
      return loader.loadModelFromString(json).get(0);
    };

    api.serializePCM = function(pcm) {
      return serializer.serialize(pcm);
    };


    api.getSortedProducts = function(pcm, metadata) {

      function getPosition(product) {
        var position = 0;
        for (var i = 0; i < metadata.productPositions.length; i++) {

          var productName = api.findCell(product, pcm.productsKey).content;

          if (metadata.productPositions[i].product === productName) {
            position = metadata.productPositions[i].position;
            break;
          }
        }

        return position;
      }

      if (metadata) {
        return pcm.products.array.sort(function (p1, p2) {

          var p1Position = getPosition(p1);
          var p2Position = getPosition(p2);

          return p1Position - p2Position;
        });
      } else {
        return pcm.products.array;
      }

    }

};

mypcmApi = new MyPcmApi();
