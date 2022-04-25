package tech.jeanpaultossou;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class App {

    private boolean createDirs(String dir){
        boolean ret = false;
        File theDir = new File(dir);
        if (!theDir.exists()){
            ret = theDir.mkdirs();
        }
        return ret;
    }

    public void split(File file) throws IOException{
        PDDocument document = PDDocument.load(file);
        Splitter splitter = new Splitter();
        List<PDDocument> pages = splitter.split(document);
        String root = System.getProperty("user.dir");
        String one = root+"/invoice-commercial/";
        String two = root+"/invoice-stelia/";
        String other = root+"/invoice-other/";

        createDirs(one);
        createDirs(two);
        createDirs(other);

        int n=1;
        for (PDDocument doc : pages) {
            PDFTextStripper stripper = new PDFTextStripper();
            String orderNum = stripper.getText(doc).split("\n")[13];

            if (orderNum.startsWith("DL0") || orderNum.startsWith("CA") || orderNum.startsWith("630") || orderNum.startsWith("930")){
                doc.save(new File(one+orderNum+""+n+".pdf"));
            }else {
                doc.save(new File(two+orderNum+""+n+".pdf"));
            }
            n++;
            doc.close();
        }
        document.close();
    }
}
