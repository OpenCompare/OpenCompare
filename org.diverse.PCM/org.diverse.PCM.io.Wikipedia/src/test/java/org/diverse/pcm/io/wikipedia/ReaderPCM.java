package org.diverse.pcm.io.wikipedia;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* This class is use to parse PCM, the first line starts at zero, as the first column */
public class ReaderPCM {

    /* Return the entire line number */
    public static List getLineNumber(int number, Document doc){
        Element table = doc.select("table").first();
        Iterator<Element> ite = table.select("tr").iterator();
        List<String> lineTable = new ArrayList<String>();
        for(int i=0; i < number; i++){
            ite.next();
        }
        Element line = ite.next();
        Iterator<Element> ite2 = line.select("td").iterator();
        while(ite2.hasNext()){
            lineTable.add(ite2.next().text());
        }
        return lineTable;
    }

    /* Return the cell line/colum */
    public static String getCell(int line, int column, Document doc){
        List<String> ColumnTable = getColumnNumber(column, doc);
        return new ArrayList<String>(ColumnTable).get(line-1);
    }

    /* Return the entire column number */
    public static List getColumnNumber(int number, Document doc){
        Element table = doc.select("table").first();
        Iterator<Element> ite = table.select("tr").iterator();
        List<String> columnTable = new ArrayList<String>();
        Element value = null;
        if(ite.hasNext()){
            ite.next();
        }
        while(ite.hasNext()){
            Element line = ite.next();
            Iterator<Element> ite2 = line.select("td").iterator();
            for(int i=0; i < number && ite2.hasNext(); i++){
                value = ite2.next();
            }
            if(value != null) {
                columnTable.add(value.text());
            }
        }
        return columnTable;
    }

    /* Return the title line */
    public static List getTitleLine(Document doc){
        Element tr = doc.select("tr").first();
        Iterator<Element> ite = tr.select("th").iterator();
        List<String> lineTable = new ArrayList<String>();
        while(ite.hasNext()){
            lineTable.add(ite.next().text());
        }
        return lineTable;
    }

    /* Return the title column */
    public static List getTitleColumn(Document doc){
        Element table = doc.select("table").first();
        Iterator<Element> ite = table.select("tr").iterator();
        List<String> columnTable = new ArrayList<String>();
        if(ite.hasNext()){
            ite.next();
        }
        while(ite.hasNext()) {
            Iterator<Element> ite2 = ite.next().select("th").iterator();
            while (ite2.hasNext()) {
                columnTable.add(ite2.next().text());
            }
        }
        return columnTable;
    }


}
