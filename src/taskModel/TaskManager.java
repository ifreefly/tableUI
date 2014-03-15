package taskModel;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import staticVar.StaticVar;
import downloadWorker.DownloadWorker;
import downloadWorker.HttpDownload;

public class TaskManager {
	private int availableDownload ;
	private TaskTableModel taskTableModel;
	public TaskManager(){
		availableDownload=StaticVar.CONCURRENCY_DOWNLOAD;
		taskTableModel=new TaskTableModel();
	}
	public TaskTableModel getTaskTableModel(){
		return this.taskTableModel;
	}
	public void addTask(JTable parentComponent,String url){
		//TODO to add one or more tasks
		String urls[]=url.split("\n");
		for(int i=0;i<urls.length;i++){
			HttpDownload httpDownload=new HttpDownload(urls[i],StaticVar.DEV_SAVE_PATH);
			FileInfo fileInfo=new FileInfo(httpDownload);
			DownloadWorker downloadWorker=new DownloadWorker(httpDownload,this.taskTableModel);
			taskTableModel.addTask(downloadWorker, fileInfo);
			//JOptionPane.showConfirmDialog(parentComponent, "ok");
			if(availableDownload>0){
				availableDownload--;
				//downloadWorker.execute();
				//System.out.println("wtf");
			}else{
				taskTableModel.addTask(downloadWorker, fileInfo);
			}
		}
	}
	public void removeTask(int rowIndex){
		//TODO to delete a task,it needs to be fixed
		taskTableModel.removeTask(rowIndex);
	}
	
	public void pauseTask(int rowIndex){
		//TODO to pause a task ,and write config into hard drive
		taskTableModel.pauseTask(rowIndex);
	}
}
