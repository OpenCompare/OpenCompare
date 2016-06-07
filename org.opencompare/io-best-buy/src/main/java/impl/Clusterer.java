package impl;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;

import java.io.*;
import java.util.TreeSet;
import java.util.Set;

/**
 * Created by jbferrei on 4/10/15.
 */
public class Clusterer {



    public int[][] calculateProductSimilarityMatrix(PCM pcm){

        int[][] simMatrix = new int[pcm.getProducts().size()][pcm.getProducts().size()];

        for(int i = 0; i < pcm.getProducts().size(); i++){

            for(int j = 0; j < pcm.getProducts().size(); j++){

                simMatrix[i][j] = calculateProductSimilarityByIntersection(pcm.getProducts().get(i), pcm.getProducts().get(j));

            }
        }
        return simMatrix;
    }

    private int calculateProductSimilarityByIntersection(Product currentProduct, Product toCompareProduct){

        Set<String> intersection = new TreeSet<String>(getFeatureNamesWithoutEmptyValuesOfAProduct(currentProduct));

        //Set<String> intersection = new TreeSet<String>(currentProduct.getCells().);
        intersection.retainAll(getFeatureNamesWithoutEmptyValuesOfAProduct(toCompareProduct));

        return intersection.size()*(-1);
    }

    private Set<String> getFeatureNamesWithoutEmptyValuesOfAProduct(Product product){

        Set<String> setOfFeaturesWithContent = new TreeSet<String>();

        for(Cell cell : product.getCells())
            if(cell.getContent()!="N/A")
                setOfFeaturesWithContent.add(cell.getFeature().getName());


        return setOfFeaturesWithContent;
    }
    public void printSimilarityMatrixOfProjects(int[][] simMatrix, String outputFolder, PCM pcm){
        StringBuffer sb = new StringBuffer();
        String[][] stringMatrix = new String[simMatrix.length+1][simMatrix.length+1];


        //get product names in an array of String

        String [] productNames = new String[pcm.getProducts().size()];
        int counter = 0;

        for(Product product : pcm.getProducts()) {
            productNames[counter] = product.getKeyContent();
            counter++;
        }


        //initialize
        for(int i = 1; i<simMatrix.length+1; i++){
            //stringMatrix[0][i] = ((String) productNames[i-1]).replace(",", "");
            stringMatrix[0][i] = "P"+ i;
        }
        for(int i = 1; i<simMatrix.length+1; i++){
            //stringMatrix[i][0] = ((String) productNames[i-1].replace(",", ""));
            stringMatrix[i][0] = "P" + i;
        }
        //fill the string matrix
        for(int i = 1; i<simMatrix.length+1; i++){

            for(int j = 1; j<simMatrix.length+1; j++){

                stringMatrix[i][j] = Integer.toString(simMatrix[i-1][j-1]);

            }

        }
        //print the string matrix
        for(int i = 0; i<stringMatrix.length; i++){

            for(int j = 0; j<stringMatrix.length; j++){
                if(j!=stringMatrix.length)
                    sb.append(stringMatrix[i][j] + ", ");
                else
                    sb.append(stringMatrix[i][j]);
            }
            sb.append("\n");
        }

        writeFile(sb, outputFolder, "SimilarityMatrix", pcm.getName());
    }
    private void writeFile(StringBuffer sb, String outputFolder, String printType, String pcmName){
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFolder + printType + pcmName + ".csv"), "utf-8"));
            writer.write(sb.toString());
        } catch (IOException ex) {
            // report
        } finally {
            try {writer.close();} catch (Exception ex) {}
        }
    }
}
