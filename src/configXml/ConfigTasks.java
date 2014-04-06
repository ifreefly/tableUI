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
	public ConfigTasks(String configType){
		absolutePath=System.getProperty("java.class.path");
		//System.out.println(TaskManager.fileAvailable(absolutePath+"/"+StaticVar.CONFIG_TASKALL_PATH));
		if(StaticVar.FILE_PARENT_NDIR!=TaskManager.fileAvailable(absolutePath+"/"+configType)){
			configFile=new File(absolutePath+"/"+configType);
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
		root=document.createElement("Tasks");
		root.setAttribute("items", "0");
		//root.getAttribute("items");
		document.appendChild(root);
		
	}
	
	public void loadConfig(){
		try {
			document=ConfigOpera.creatDocument(configFile);
			root=(Element) document.getElementsByTagName("Tasks").item(0);
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
			int items=Integer.valueOf(root.getAttribute("items"));
			items++;
			root.setAttribute("items", String.valueOf(items));
			writeToDisk();
		//}
	}
	
	public void removeTask(FileInfo fileInfo){//将任务从列表中删除
		NodeList taskList=document.getElementsByTagName("task");
		for(int i=0;i<taskList.getLength();i++){
			Element eTask=(Element)taskList.item(i);
			if(eTask.getAttribute("savePath").equals(fileInfo.getSavePath())){
				eTask.getParentNode().removeChild(eTask);
				int items=Integer.valueOf(root.getAttribute("items"));
				items--;
				root.setAttribute("items", String.valueOf(items));
			}
		}
		writeToDisk();
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
		NodeList nTasks=root.getElementsByTagName("task");
		savePath=new String[nTasks.getLength()];
		for(int i=0;i<nTasks.getLength();i++){
			Element eTask=(Element)nTasks.item(i);
			savePath[i]=new String(eTask.getAttribute("savePath")+eTask.getAttribute("fileName"));
		}
		return savePath;
	}
	
	public void writeToDisk(){
		ConfigOpera.writeToDisk(document,configFile);
	}
	
	public Element getRoot(){
		return this.root;
	}
	
	public void setItems(){
		
	}
}
