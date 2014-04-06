package configXml;
/*author:idevcod@163.com
 *description:该配置文件主要设置文件的默认保存路径
 *文件格式：
 *<SavePath path="defaultSavePath"/>
 * */
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import staticVar.StaticVar;
import taskModel.TaskManager;


public class ConfigPath {
	private String path;
	private File configFile;
	private Document document;
	private Element root;
	//private NodeList  nTasks=null;
	public ConfigPath(String configName){
		//absolutePath=System.getProperty("java.class.path");
		path=System.getProperty("java.class.path")+"/"+configName;
		//System.out.println(TaskManager.fileAvailable(absolutePath+"/"+StaticVar.CONFIG_TASKALL_PATH));
		if(StaticVar.FILE_PARENT_NDIR!=TaskManager.fileAvailable(path)){
			configFile=new File(path);
			if(configFile.exists()){
				
				loadConfig();
			}else{
				createConfig();
				writeToDisk();
			}
		}
	}
	public void createConfig(){
		try {
			configFile.createNewFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		document=ConfigOpera.createDocument();
		root=document.createElement("SavePath");
		File file=new File("d:\\flashGot");
		if(!file.exists()){
			file.mkdir();
		}
		root.setAttribute("path", "d:\\flashGot");
		//root.getAttribute("items");
		document.appendChild(root);
		
	}
	
	public void loadConfig(){
		try {
			document=ConfigOpera.creatDocument(configFile);
			root=(Element) document.getElementsByTagName("SavePath").item(0);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeToDisk(){
		ConfigOpera.writeToDisk(document,configFile);
	}
	
	public String getPath() {
		return root.getAttribute("path");
	}
	public void setPath(String path) {
		this.path = path;
		root.setAttribute("path", this.path);
	}
}
