package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.Product;
import org.diverse.pcm.api.java.impl.CellImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Xosteer on 12/01/2015.
 */
public class ReaderPCMJSON {



    public boolean containsAllProducts(List<Product> products, PCM pcm){
        ArrayList<Product> produits =  (ArrayList)pcm.getProducts();
        return produits.containsAll(products);
    }

    public boolean containsAllContents(List<Product> products, PCM pcm){
        ArrayList<Product> produits =  (ArrayList)pcm.getProducts();
        boolean containsAll = false;
        for(Product prod : produits){
            ArrayList<CellImpl> cellules = (ArrayList) prod.getCells();
                containsAll = cellules.containsAll(prod.getCells());
            }
        return containsAll;
        }


    public boolean containsContent(String value, PCM pcm){
        ArrayList<Product> produits =  (ArrayList)pcm.getProducts();
        boolean containsValue = false;
        for(Product prod : produits){
            ArrayList<CellImpl> cellules = (ArrayList) prod.getCells();
            for(CellImpl cellule : cellules){
                containsValue = cellule.getContent().equals(value);
            }

        }
        return containsValue;
    }

    public boolean containsProduct(String value, PCM pcm){
        ArrayList<Product> produits =  (ArrayList)pcm.getProducts();
        boolean containsValue = false;
        for(Product prod : produits){
                if(prod.getName().equals(value)){
                    containsValue = true;
                }
            }
        return containsValue;
    }

    public boolean containsName(String value, PCM pcm){
        boolean containsValue = false;
            if(pcm.getName().equals(value)){
                containsValue = true;
            }
        return containsValue;
    }

    public boolean sameRandomProduct(PCM pcm1, PCM pcm2){
        List<Product> products1 =  pcm1.getProducts();
        List<Product> products2 =  pcm2.getProducts();
        int i = randomRange(0, products1.size() - 1);
        return products1.get(i).getName() ==  products2.get(i).getName();
    }

    public boolean sameRandomCell(PCM pcm1, PCM pcm2){
        List<Product> products1 =  pcm1.getProducts();
        List<Product> products2 =  pcm2.getProducts();
        int i = randomRange(0, products1.size() - 1);
        List<Cell> cells1 = products1.get(i).getCells();
        List<Cell> cells2 = products1.get(i).getCells();
        int j = randomRange(0, cells1.size()-1);
        return cells1.get(i).getContent() ==  cells2.get(i).getContent();
    }

    public static int randomRange(int min, int max){
        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

}
