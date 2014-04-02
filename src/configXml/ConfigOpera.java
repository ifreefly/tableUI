package configXml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/*提供xml文件的document的创建以及文件的写入*/
public class ConfigOpera {
	protected static void writeToDisk(Document document,File configFile){
		try {
			//开始把Document映射到文件
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transFormer = transFactory.newTransformer();
            
            //设置输出结果
            DOMSource domSource = new DOMSource(document);
            
			FileOutputStream out=new FileOutputStream(configFile);
			StreamResult xmlResult=new StreamResult(out);
			transFormer.transform(domSource, xmlResult);
			out.close();
		} catch (TransformerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	protected static Document createDocument(){
		DocumentBuilderFactory docuFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder docuBuilder;
		Document document = null;
		try {
			docuBuilder=docuFactory.newDocumentBuilder();
			document=docuBuilder.newDocument();
			document.setXmlVersion("1.0");
			//return document;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		//Document document = null;
		return document;
	}
	
	protected static Document creatDocument(File configFile) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory docuFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder docuBuilder=docuFactory.newDocumentBuilder();
		//System.out.println("hello");
		return docuBuilder.parse(configFile);
		
	}

}
