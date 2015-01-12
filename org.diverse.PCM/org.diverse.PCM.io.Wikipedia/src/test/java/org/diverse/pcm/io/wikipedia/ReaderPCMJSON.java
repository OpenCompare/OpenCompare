package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.Product;
import org.diverse.pcm.api.java.impl.CellImpl;

import java.util.ArrayList;
import java.util.List;

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

}
