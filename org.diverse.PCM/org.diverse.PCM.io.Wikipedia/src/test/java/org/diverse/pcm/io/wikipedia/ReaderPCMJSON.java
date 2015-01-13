package org.diverse.pcm.io.wikipedia;

import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.PCM;
import org.diverse.pcm.api.java.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Xosteer on 12/01/2015.
 */
public class ReaderPCMJSON {



    public boolean containsAllProducts(PCM pcm1, PCM pcm2){
        List<Product> products1 =  pcm1.getProducts();
        List<String> products1names = new ArrayList<String>();
        for(Product product1: products1){
            products1names.add(product1.getName());
        }
        List<Product> products2 =  pcm2.getProducts();
        List<String> products2names = new ArrayList<String>();
        for(Product product2: products2){
            products2names.add(product2.getName());
        }

        return products1names.containsAll(products2names);
    }

    public boolean containsAllContents(PCM pcm1, PCM pcm2){
        int countDif = 0;
        List<Product> products1 =  pcm1.getProducts();
        List<String> cellsNames1 = new ArrayList<String>();
        for(Product product1: products1){
            List<Cell> product1cells = product1.getCells();
            int i = 0;
            for(Cell product1cell: product1cells){
               cellsNames1.add(i, product1cell.getContent());
                i++;
            }
        }
        List<Product> products2 =  pcm2.getProducts();
        List<String> cellsNames2 = new ArrayList<String>();

        for(Product product2: products2){
            List<Cell> product2cells = product2.getCells();
            int j = 0;
            for(Cell product2cell: product2cells){
                cellsNames2.add(j, product2cell.getContent());
                j++;
            }
        }
            for(String cellname: cellsNames1){
                if(!cellsNames2.contains(cellname)){
                    countDif++;
                }
            }
        return countDif == 0;
        }


    public boolean containsContent(String value, PCM pcm){
        List<Product> produits =  pcm.getProducts();
        boolean containsValue = false;
        for(Product prod : produits){
            List<Cell> cellules = prod.getCells();
            for(Cell cellule : cellules){
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
        if(pcm1.getProducts().size() == pcm2.getProducts().size()) {
            int i = randomRange(0, products1.size() - 1);
            return products1.get(i).getName() == products2.get(i).getName();
        }
        else{
            return false;
        }
    }

    public boolean sameRandomCell(PCM pcm1, PCM pcm2){
        List<Product> products1 =  pcm1.getProducts();
        List<Product> products2 =  pcm2.getProducts();
        if(pcm1.getProducts().size() == pcm2.getProducts().size()) {
            int i = randomRange(0, products1.size() - 1);
            List<Cell> cells1 = products1.get(i).getCells();
            List<Cell> cells2 = products1.get(i).getCells();
            int j = randomRange(0, cells1.size() - 1);
            return cells1.get(i).getContent() == cells2.get(i).getContent();
        }
        else{
            return false;
        }
    }

    public static int randomRange(int min, int max){
        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

}
