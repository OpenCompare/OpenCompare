import org.junit.Test;
import org.kevoree.modeling.api.KMFContainer;
import org.kevoree.modeling.api.json.JSONModelLoader;
import org.kevoree.modeling.api.json.JSONModelSerializer;
import org.opencompare.model.*;
import org.opencompare.model.pcm.factory.DefaultPcmFactory;
import org.opencompare.model.pcm.factory.PcmFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by gbecan on 6/29/15.
 */
public class PCMMetamodelTest {

    private PcmFactory factory = new DefaultPcmFactory();
    private JSONModelSerializer serializer = factory.createJSONSerializer();
    private JSONModelLoader loader = factory.createJSONLoader();

    private PCM createModel() {
        // Create PCM
        PCM pcm = factory.createPCM();
        pcm.setName("pcm title");


        // Create features
        FeatureGroup featureGroup = factory.createFeatureGroup();
        featureGroup.setName("fg");
        pcm.addFeatures(featureGroup);

        Feature feature1 = factory.createFeature();
        feature1.setName("f1");
        featureGroup.addSubFeatures(feature1);

        Feature feature2 = factory.createFeature();
        feature2.setName("f2");
        featureGroup.addSubFeatures(feature2);

        // Create products
        Product product1 = factory.createProduct();
        pcm.addProducts(product1);

        Product product2 = factory.createProduct();
        pcm.addProducts(product2);

        // Set products key
        pcm.setProductsKey(feature1);

        // Create cells
        Cell c1 = factory.createCell();
        c1.setContent("p1f1");
        c1.setRawContent("raw p1f1");
        c1.setFeature(feature1);
        product1.addCells(c1);

        Cell c2 = factory.createCell();
        c2.setContent("p1f2");
        c2.setRawContent("raw p1f2");
        c2.setFeature(feature2);
        product1.addCells(c2);

        Cell c3 = factory.createCell();
        c3.setContent("p2f1");
        c3.setRawContent("raw p2f1");
        c3.setFeature(feature1);
        product2.addCells(c3);

        Cell c4 = factory.createCell();
        c4.setContent("p2f2");
        c4.setRawContent("raw p2f2");
        c4.setFeature(feature2);
        product2.addCells(c4);

        // Create interpretation
        BooleanValue interpretation = factory.createBooleanValue();
        interpretation.setValue(true);
        c1.setInterpretation(interpretation);

        return pcm;
    }

    @Test
    public void testModelCreation() {
        createModel();
    }

    @Test
    public void testSerialization() {

        PCM pcm = createModel();

        // Serialize
        String serializedPCM = serializer.serialize(pcm);

        // Load
        List<KMFContainer> containers = loader.loadModelFromString(serializedPCM);
        assertEquals("number of pcms", 1 , containers.size());

        PCM loadedPCM = (PCM) containers.get(0);
        assertTrue("load(serialize(pcm)) = pcm", pcm.deepModelEquals(loadedPCM));
    }


}
