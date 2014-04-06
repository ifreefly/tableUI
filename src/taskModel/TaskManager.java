package taskModel;

//import javax.swing.JOptionPane;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.xml.parsers.ParserConfigurationException;

import mainUI.NewTaskDialog;

import org.xml.sax.SAXException;

import configXml.Config;
import configXml.ConfigTasks;
import staticVar.StaticVar;
import varFilter.UrlFilter;
import downloadWorker.DownloadWorker;
import downloadWorker.HttpDownload;

import java.io.File;
import java.io.IOException;

public class TaskManager {
	private int availableDownload ;
	private TaskTableModel taskTableModel;
	private ConfigTasks configTasks;
	private ConfigTasks historyTasks;
	//private JTable parentComponent;
	public TaskManager() {
		availableDownload=StaticVar.CONCURRENCY_DOWNLOAD;
		taskTableModel=new TaskTableModel();
		historyTasks=new ConfigTasks(StaticVar.CONFIG_HISTORY_TASK_PATH);
	}
	
	public void initConfigTasks()throws ParserConfigurationException, SAXException, IOException{
		configTasks=new ConfigTasks(StaticVar.CONFIG_TASKALL_PATH);
		System.out.println(configTasks.getRoot());
		System.out.println(configTasks.getRoot().getAttribute("items"));
		//System.out.println(configTasks.getRoot().getElementsByTagName("task").item(0));
		if(!configTasks.getRoot().getAttribute("items").equals("0")){//有历史任务，载入任务
			loadTasks();
			//System.out.println("wtf");
		}
		//new WriteConfig().start();;
	}
	/*public void setParentComponent(JTable parentComponent){
		this.parentComponent=parentComponent;
	}*/
	public TaskTableModel getTaskTableModel(){
		return this.taskTableModel;
	}
	
	public void addTask(JTable parentComponent,NewTaskDialog newTaskDialog) throws ParserConfigurationException, SAXException, IOException{
		//TODO to add one or more tasks
		String urls[]=newTaskDialog.getSrcField().getText().split(";");
		if(new UrlFilter(urls).isValid()){
			String filePath=newTaskDialog.getSavePathField()+StaticVar.SYSTEM_SEPARATOR+urls[0].substring(urls[0].lastIndexOf("/")+1, urls[0].length());
			int fileAvailable=fileAvailable(filePath);
			for(int i=0;i<urls.length;i++){
				//TODO如下语句可更改为switch/case语句，增强代码的可读性
				if(StaticVar.FILE_AVAILABLE==fileAvailable){//初次新建任务
					HttpDownload httpDownload=new HttpDownload(urls[i],newTaskDialog.getNameField().getText(),newTaskDialog.getSavePathField().getText()+StaticVar.SYSTEM_SEPARATOR);
					FileInfo fileInfo=new FileInfo(httpDownload);
					DownloadWorker downloadWorker=new DownloadWorker(httpDownload,fileInfo,this);
					downloadWorker.createConfig();
					taskTableModel.addTask(fileInfo, downloadWorker);
					configTasks.addTask(fileInfo);
					configTasks.writeToDisk();
					//JOptionPane.showConfirmDialog(parentComponent, "ok");
					if(availableDownload>0){
						//System.out.println("初次新建任务");
						availableDownload--;
						downloadWorker.execute();
					}
				}else if(StaticVar.FILE_LOAD_CONFIG==fileAvailable){//文件曾创建过，导入配置文件进行下载,运行情况 很少！
					//TODO
					File configFile=new File(filePath+".xml"); 
					Config config=new Config(configFile);
					HttpDownload httpDownload=config.loadHttpDownload();
					FileInfo fileInfo=new FileInfo(httpDownload);
					DownloadWorker downloadWorker=new DownloadWorker(httpDownload,fileInfo,this);
					taskTableModel.addTask(fileInfo, downloadWorker);
					//JOptionPane.showConfirmDialog(parentComponent, "ok");
					if(availableDownload>0){
						availableDownload--;
						downloadWorker.execute();
					}
				}else if(StaticVar.FILE_PARENT_NDIR==fileAvailable){//指定的存储目录有误，请重新选择
					//TODO
				}else if(StaticVar.FILE_NAVAILABLE==fileAvailable){//当前目录已存在同名文件
					System.out.println("当前目录已存在同名文件");
				}
			}
		}else{
			JOptionPane.showMessageDialog(
					parentComponent, 
					"please input a correct url!",
					"error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void addTaskByConfig(String configPath) throws ParserConfigurationException, SAXException, IOException{//从配置文件中导入任务
		File configFile=new File(configPath); 
		Config config=new Config(configFile);
		HttpDownload httpDownload=config.loadHttpDownload();
		FileInfo fileInfo=new FileInfo(httpDownload);
		DownloadWorker downloadWorker=new DownloadWorker(httpDownload,fileInfo,this);
		downloadWorker.loadConfig();
		taskTableModel.addTask(fileInfo, downloadWorker);
		
		//JOptionPane.showConfirmDialog(parentComponent, "ok");
		if(availableDownload>0){
			availableDownload--;
			downloadWorker.execute();
		}
	}
	public void removeTask(int rowIndex){
		//TODO to delete a task,it needs to be fixed
		taskTableModel.removeTask(rowIndex);
	}
	
	public void startTask(int rowIndex){
		//TODO
		taskTableModel.startTask(rowIndex);
	}
	public void pauseTask(int rowIndex){
		//TODO to pause a task ,and write config into hard drive
		taskTableModel.pauseTask(rowIndex);
	}
	
	public static int fileAvailable(String filePath){//检测当前目录下是否已存在同名文件
		File file=new File(filePath);
		if(file.exists()){
			String configFile=filePath+".xml";
			File config=new File(configFile);
			if(config.exists()){//当前目录下曾创建过文件并下载
				return StaticVar.FILE_LOAD_CONFIG;
			}else{
				return StaticVar.FILE_NAVAILABLE;
			}
		}else{
			if(file.getParentFile().exists()){
				if(!file.getParentFile().isDirectory()){//父目录是文件而不是目录
					return StaticVar.FILE_PARENT_NDIR;
				}
				else{
					return StaticVar.FILE_AVAILABLE;
				}
			}else{
				file.getParentFile().mkdirs();
				return StaticVar.FILE_AVAILABLE;
			}
		}
	}
	
	private void loadTasks() throws ParserConfigurationException, SAXException, IOException{
		//File configTasksFile=new File(System.getProperty("java.class.path")+"/"+StaticVar.CONFIG_TASKALL_PATH);
		String savePath[]=configTasks.loadTasks();
		for(int i=0;i<savePath.length;i++){
			//File configFile=new File(savePath[i].substring(0,savePath[i].lastIndexOf("."))+".xml");
			System.out.println("loadTasks");
			//System.out.println(savePath[i]+".xml");
			addTaskByConfig(savePath[i]+".xml");
		}
	}
	
	public void addToHistory(FileInfo fileInfo){
		configTasks.removeTask(fileInfo);
		historyTasks.addTask(fileInfo);
		this.availableDownload++;
	}

	public int getAvailableDownload() {
		return availableDownload;
	}

	public void setAvailableDownload(int availableDownload) {
		this.availableDownload = availableDownload;
	}

	public void setTaskTableModel(TaskTableModel taskTableModel) {
		this.taskTableModel = taskTableModel;
	}

	public ConfigTasks getConfigTasks() {
		return configTasks;
	}

	public void setConfigTasks(ConfigTasks configTasks) {
		this.configTasks = configTasks;
	}

	public ConfigTasks getHistoryTasks() {
		return historyTasks;
	}

	public void setHistoryTasks(ConfigTasks historyTasks) {
		this.historyTasks = historyTasks;
	}
	
	
	/*public class WriteConfig extends Thread {
		//TODO
		//定时将配置文件写入硬盘
		@Override
		public void run(){
			while(true){
				configTasks.writeToDisk();
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}*/
}
