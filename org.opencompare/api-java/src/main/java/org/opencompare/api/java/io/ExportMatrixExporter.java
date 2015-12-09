package org.opencompare.api.java.io;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.opencompare.api.java.*;
import org.opencompare.api.java.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gbecan on 08/12/15.
 */
public class ExportMatrixExporter {

    public ExportMatrix export(PCMContainer pcmContainer) {
        PCM pcm = pcmContainer.getPcm();

        final PCMMetadata metadata;
        if (pcmContainer.getMetadata() == null) {
            metadata = new PCMMetadata(pcm);
        } else {
            metadata = pcmContainer.getMetadata();
        }


        ExportMatrix matrix = new ExportMatrix();

        // Export name
        matrix.setName(pcm.getName());

        int productsStartRow = exportFeatures(metadata, matrix);

        exportProducts(pcm, metadata, matrix, productsStartRow);

        // Transpose matrix if necessary
        if (!metadata.getProductAsLines()) {
            matrix.transpose();
        }

        return matrix;
    }


    private int exportFeatures(PCMMetadata metadata, ExportMatrix matrix) {
        List<Pair<AbstractFeature, Pair<Integer, Integer>>> currentFeatureLevel = new ArrayList<>();

        for (Feature feature : metadata.getSortedFeatures()) {
            currentFeatureLevel.add(new Pair<>(feature, new Pair<>(1, 1)));
        }

        List<List<ExportCell>> exportCellRows = new ArrayList<>();
        boolean noParents = false;

        while(!currentFeatureLevel.isEmpty() && !noParents) {

            List<Pair<AbstractFeature, Pair<Integer, Integer>>> nextFeatureLevel = new ArrayList<>();
            List<Pair<AbstractFeature, Pair<Integer, Integer>>> row = new ArrayList<>();

            // Detect if current level of features have at least one parent
            noParents = true;
            for (Pair<AbstractFeature, Pair<Integer, Integer>> data : currentFeatureLevel) {
                if (data._1.getParentGroup() != null) {
                    noParents = false;
                }
            }

            // Analyze hierarchy of features
            int i = 0;
            while (i < currentFeatureLevel.size()) {
                Pair<AbstractFeature, Pair<Integer, Integer>> data = currentFeatureLevel.get(i);
                AbstractFeature aFeature = data._1;
                Pair<Integer, Integer> span = data._2;

                // Compute colspan
                int colspan = 1;
                while ((i + 1) < currentFeatureLevel.size() && aFeature.equals(currentFeatureLevel.get(i + 1)._1)) {
                    i++;
                    colspan++;
                }

                // Compute rowspan and prepare for next iteration
                AbstractFeature parentGroup = aFeature.getParentGroup();
                if (parentGroup == null) {
                    int rowspan = span._1 + 1;
                    nextFeatureLevel.add(new Pair<>(aFeature, new Pair<>(rowspan, colspan)));
                    if (noParents) {
                        row.add(new Pair<>(aFeature, new Pair<>(span._1, colspan)));
                    }
                } else {
                    row.add(new Pair<>(aFeature, new Pair<>(span._1, colspan)));
                    nextFeatureLevel.add(new Pair<>(parentGroup, new Pair<>(1, 1)));
                }
                i++;
            }

            // Create cells
            List<ExportCell> exportCellRow = new ArrayList<>();
            for (Pair<AbstractFeature, Pair<Integer, Integer>> data : row) {
                AbstractFeature aFeature = data._1;
                Pair<Integer, Integer> span = data._2;

                ExportCell exportCell = new ExportCell(aFeature.getName(), span._1, span._2);
                exportCell.setFeature(true);
                exportCell.setInProductsKeyColumn(false);

                exportCellRow.add(exportCell);
            }
            exportCellRows.add(exportCellRow);

            currentFeatureLevel = nextFeatureLevel;
        }

        // Add rows to table
        Collections.reverse(exportCellRows);
        int row = 0;
        for (List<ExportCell> exportCellRow : exportCellRows) {

            int column = 0;

            // Skip cells created by rowspan
            while (matrix.isPositionOccupied(row, column)) {
                column++;
            }

            for (ExportCell exportCell : exportCellRow) {
                matrix.setCell(exportCell, row, column);
                column += exportCell.getColspan();
            }

            row++;
        }

        return row;
    }

    private void exportProducts(PCM pcm, PCMMetadata metadata, ExportMatrix matrix, int productsStartRow) {
        int row = productsStartRow;
        for (Product product : metadata.getSortedProducts()) {

            int column = 0;
            for (Feature feature : metadata.getSortedFeatures()) {
                Cell cell = product.findCell(feature);

                if (cell != null) {
                    ExportCell exportCell = new ExportCell(cell.getContent(), cell.getRawContent());
                    exportCell.setFeature(false);
                    exportCell.setInProductsKeyColumn(pcm.getProductsKey().equals(feature));
                    matrix.setCell(exportCell, row, column);
                }

                column++;
            }

            row++;
        }
    }
}
