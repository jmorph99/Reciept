/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intersys.reciept;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.cos.COSDocument;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
/**
 *
 * @author murphy
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        String path = args[0];  //"/home/murphy/Documents/Reciepts/2-16-2018";
        listFilesForFolder(new File(path));
    }
    
    public static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                rename(fileEntry);
            }
        }
    
    }
    public static void rename(File file){
        
        HashSet<Float> hs = new HashSet<>();
        try {
            PDDocument document = PDDocument.load(file);
           document.getClass();        
           if (!document.isEncrypted()) {
               PDFTextStripperByArea stripper = new PDFTextStripperByArea();
               stripper.setSortByPosition(true);
               PDFTextStripper tStripper = new PDFTextStripper();
               String pdfFileInText = tStripper.getText(document).replace("\t", " ");
               //System.out.println(pdfFileInText);
               Pattern p = Pattern.compile("[$](([0-9]+\\.?\\d*)|([0]\\.\\d\\d)|[0])");
               Matcher m = p.matcher(pdfFileInText);
               while(m.find()){
                   hs.add(Float.valueOf(pdfFileInText.substring(m.start()+1, m.end())));
                   
               }
               Iterator<Float> iter = hs.iterator();
               float price = 0.0f;
               while(iter.hasNext()){
                   float num = (iter.next());
                   if(num > price)
                       price = num;
               }
               
               
               String name = file.getAbsolutePath() + "-" + price + ".pdf";
               System.out.println(name);
               file.renameTo(new File(name));
           }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        } 
    }

}
