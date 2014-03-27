package configXml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import staticVar.StaticVar;
import taskModel.FileInfo;
import taskModel.TaskManager;

/*生成任务信息，放置在特定的目录下，以便再次开启软件时能继续上次的任务
 * <Tasks items="0">
 * 	<Task fileName="" savePath="" fileUrl="" fileSize="" fileType="" fileStatus="">
 * 	<Task fileName="" savePath="" fileUrl="" fileSize="" fileType="" fileStatus="">
 * </Tasks>
 * 
 * 配置文件存储路径：config\tasksAll.xml
 * 			   \config\hitoryTasks.xml
 * */
public class ConfigTasks {
	private String absolutePath;
	private File configFile;
	private Document document;
	private Element root;
	//private NodeList  nTasks=null;
	public ConfigTasks(){
		absolutePath=System.getProperty("java.class.path");
		if(StaticVar.FILE_AVAILABLE==TaskManager.fileAvailable(absolutePath+"/"+StaticVar.CONFIG_TASKALL_PATH)){
			configFile=new File(absolutePath+"/"+StaticVar.CONFIG_TASKALL_PATH);
			if(configFile.exists()){
				loadConfig();
			}else{
				createConfig();
			}
		}
		//System.out.println(absolutePath);
	}
	
	public void createConfig(){
		
		try {
			configFile.createNewFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		document=ConfigOpera.createDocument();
		root=document.createElement("Tasks");
		root.setAttribute("items", "0");
		document.appendChild(root);
	}
	
	public void loadConfig(){
		try {
			document=ConfigOpera.creatDocument(configFile);
			root=(Element) document.getFirstChild();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addTask(FileInfo fileInfo){
		//NodeList tasksNode=document.getElementsByTagName("Tasks");
		//Node taskNode=tasksNode.item(0);
		//if(Node.ELEMENT_NODE==taskNode.getNodeType()){
			//Element eNode=(Element)taskNode;
			//nTasks=root.getElementsByTagName("task");
			Element eTask=document.createElement("task");
			eTask.setAttribute("fileName", fileInfo.getFileName());
			eTask.setAttribute("savePath", fileInfo.getSavePath());
			eTask.setAttribute("fileUrl", fileInfo.getFileURL());
			eTask.setAttribute("fileSize", fileInfo.getFileSize());
			eTask.setAttribute("fileType", fileInfo.getFileType());
			eTask.setAttribute("fileSatus", fileInfo.getFileStatus());
			root.insertBefore(eTask, root.getFirstChild());
			
		//}
	}
	
	public void setFileStatus(){//设置下载状态，暂停？停止？
		
	}
	/*public FileInfo[] loadTasks(){
		FileInfo fileInfo[];
		NodeList nTasks=root.getElementsByTagName("task");
		fileInfo=new FileInfo[nTasks.getLength()];
		for(int i=0;i<nTasks.getLength();i++){
			fileInfo[i]=new FileInfo();
			Element eTask=(Element)nTasks.item(i);
			fileInfo[i].setFileName(eTask.getAttribute("fileName"));
			fileInfo[i].setSavePath(eTask.getAttribute("savePath"));
			fileInfo[i].setFileURL(eTask.getAttribute("fileUrl"));
			fileInfo[i].setFileSize(eTask.getAttribute("fileSize"));
			fileInfo[i].setFileType(eTask.getAttribute("fileType"));
			fileInfo[i].setFileStatus(eTask.getAttribute("fileStatus"));
		}
			
		return fileInfo;
	}*/	
	
	public String[] loadTasks(){
		String savePath[];
		NodeList nTasks=root.getElementsByTagName("tasks");
		savePath=new String[nTasks.getLength()];
		for(int i=0;i<nTasks.getLength();i++){
			Element eTask=(Element)nTasks.item(i);
			savePath[i]=new String(eTask.getAttribute("savePath"));
		}
		return null;
	}
	
	public Element getRoot(){
		return this.root;
	}

}