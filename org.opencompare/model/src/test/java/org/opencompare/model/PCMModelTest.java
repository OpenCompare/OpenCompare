package org.opencompare.model;

import org.junit.Test;
import org.kevoree.modeling.api.Callback;
import org.kevoree.modeling.api.KObject;
import org.kevoree.modeling.api.json.JsonModelLoader;
import org.kevoree.modeling.api.json.JsonModelSerializer;
import pcm.*;
import pcm.impl.PcmViewImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gbecan on 5/19/15.
 */
public class PCMModelTest {

    @Test
    public void testModelCreation() {
        PcmModel pcmModel = new PcmModel();
        pcmModel.connect();
        PcmUniverse universe = pcmModel.newUniverse();
        PcmView view = universe.time(0l);

        PCM pcm = view.createPCM();
        pcm.setName("PCM name");

        new File("resources").mkdirs();
        JsonModelSerializer.serialize(pcm, new Callback<String>() {
            @Override
            public void on(String s) {
                try {
                    FileWriter writer = new FileWriter("resources/pcm.json");
                    writer.append(s);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        assertEquals("PCM name", pcm.getName());

    }

    @Test
    public void testModelLoading() throws Exception {
        String path = "resources/pcm.json";
        List<String> lines = Files.readAllLines(Paths.get(path));
        StringBuffer buffer = new StringBuffer();
        for (String line : lines) {
            buffer.append(line);
            buffer.append("\n");
        }
        String json = buffer.toString();

        PcmModel pcmModel = new PcmModel();
        pcmModel.connect();
        PcmUniverse universe = pcmModel.newUniverse();
        final PcmView view = universe.time(0l);


        // Asynchronous
        view.json().load(json).then(new Callback<Throwable>() {
            @Override
            public void on(Throwable throwable) {
                view.lookup(1l).then(new Callback<KObject>() {
                    @Override
                    public void on(KObject kObject) {
                        PCM pcm = (PCM) kObject;
                        assertEquals("PCM name", pcm.getName());
                    }
                });
            }
        });

        // Synchronous
        view.json().load(json).getResult();
        KObject kObject = view.lookup(1l).getResult();
        PCM pcm = (PCM) kObject;
        assertEquals("PCM name", pcm.getName());

        System.out.println(view.now());
    }
}
