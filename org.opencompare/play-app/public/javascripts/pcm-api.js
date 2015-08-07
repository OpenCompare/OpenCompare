pcmApp.service('pcmApi', function(base64) {
    /**
     * Sort two elements by their names (accessed with x.name)
     * @param a
     * @param b
     * @returns {number}
     */
    this.sortByName = function (a, b) {
        if (a.name < b.name) {
            return -1;
        } else if (a.name > b.name) {
            return 1;
        } else {
            return 0;
        }
    };


    this.createFeature = function (name, pcm, factory) {
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

    this.getConcreteFeatures = function (pcm) {

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

        var aFeatures = pcm.features.array;
        var features = [];
        for (var i = 0; i < aFeatures.length; i++) {
            var aFeature = aFeatures[i];
            features = features.concat(getConcreteFeaturesRec(aFeature))
        }

        return features;
    };

    this.findCell = function (product, feature) {
        var cells = product.cells.array;
        for (var i = 0; i < cells.length; i++) {
            var cell = cells[i];
            if (cell.feature.name === feature.name) {
                return cell;
            }
        }
    };

    this.encodePCM = function (pcm) {
        this.base64PCMVisitor(pcm, true);
        return pcm;
    };

    this.decodePCM = function (pcm) {
        this.base64PCMVisitor(pcm, false);
        return pcm;
    };

    this.base64PCMVisitor = function (pcm, encoding) {
        function encodeToBase64(str, encoding) {
            if (encoding) {
                return base64.encode(str);
            } else {
                return base64.decode(str);
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
            product.name = encodeToBase64(product.name, encoding);
            product.cells.array.forEach(function (cell) {
                cell.content = encodeToBase64(cell.content, encoding);
                cell.rawContent = encodeToBase64(cell.rawContent, encoding);
            });
        });
    };


});

