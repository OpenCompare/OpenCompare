/**
 * Sort two elements by their names (accessed with x.name)
 * @param a
 * @param b
 * @returns {number}
 */
function sortByName(a, b) {
    if (a.name < b.name) {
        return -1;
    } else if (a.name > b.name) {
        return 1;
    } else {
        return 0;
    }
}


function createFeature(name, pcm, factory) {
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
}

function getConcreteFeatures(pcm) {

    var aFeatures = pcm.features.array;

    var features = [];
    for (var i = 0; i < aFeatures.length; i++) {
        var aFeature = aFeatures[i];
        features = features.concat(getConcreteFeaturesRec(aFeature))
    }

    return features;
}

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

function findCell(product, feature) {
    var cells = product.values.array;
    for (var i = 0; i < cells.length; i++) {
        var cell = cells[i];
        if (cell.feature.name === feature.name) {
            return cell;
        }
    }
}