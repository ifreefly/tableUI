package taskModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import staticVar.StaticVar;
import downloadWorker.DownloadWorker;

public class TaskTableModel extends AbstractTableModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<FileInfo> rows;
	private Map<FileInfo,DownloadWorker> mapLookUp;
	private String titles[];
	public TaskTableModel(){
		titles=FileInfo.getTITILES();
		rows=new ArrayList<>();
		mapLookUp=new HashMap<>();
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return titles.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return rows.size();
	}
	
	@Override
	public String getColumnName(int coloumn){
		String name="??";
		switch(coloumn){
			case 0:name=titles[0];break;
			case 1:name=titles[1];break;
			case 2:name=titles[2];break;
			case 3:name=titles[3];break;
			case 4:name=titles[4];break;
			case 5:name=titles[5];break;
			case 6:name=titles[6];break;
			//case 7:name=titles[7];break;
		}
		return name;
	}
	@Override
	public Object getValueAt(int rowIndex, int coloumnIndex) {
		// TODO Auto-generated method stub
		FileInfo fileInfo=rows.get(rowIndex);
		Object value=null;
		switch(coloumnIndex){
			case 0:value=fileInfo.getFileStatus();break;
			case 1:value=fileInfo.getFileIcon();break;
			case 2:value=fileInfo.getFileName();break;
			case 3:value=fileInfo.getFileURL();break;
			case 4:value=fileInfo.getProgress();break;
			//case 4:value=20;break;
			case 5:value=fileInfo.getFileSize();break;
			case 6:value=fileInfo.getSavePath();break;
		}
		return value;
	}
	
	@Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        FileInfo fileInfo = rows.get(rowIndex);
        switch (columnIndex) {
            case 4:
                if (aValue instanceof Float) {
                    fileInfo.setProgress((float) aValue);
                }
                break;
        }
    }
	public void addTask(FileInfo fileInfo,DownloadWorker downloadWorker){
		mapLookUp.put(fileInfo, downloadWorker);
		rows.add(0, fileInfo);
		fireTableRowsInserted(0,0);
		//System.out.println(mapLookUp);
	}
	
	public void removeTask(int rowIndex){
		FileInfo row=rows.get(rowIndex);
		mapLookUp.remove(row);
		rows.remove(rowIndex);
		fireTableRowsInserted(0,0);
		//System.out.println(rows);
		//System.out.println(mapLookUp);
	}
	
	public void updateStatus(FileInfo fileInfo,int progress){
		//TODO
		//System.out.println("wtf");	
		int rowIndex=rows.indexOf(fileInfo);
		float p=(float)progress/100f;
		setValueAt(p,rowIndex,4);
		fireTableCellUpdated(rowIndex,4);
	}
	
	public void startTask(int rowIndex){
		//TODO
		FileInfo row=rows.get(rowIndex);
		if(row!=null){
			DownloadWorker downloadWorker=mapLookUp.get(row);
			downloadWorker.startTask();
		}
	}
	public void pauseTask(int rowIndex){
		FileInfo row=rows.get(rowIndex);
		if(row!=null){
			DownloadWorker downloadWorker=mapLookUp.get(row);
			downloadWorker.pauseTask();
		}
	}
	
	public void exitOnClose(){
		//TODO关闭软件时进行善后处理
		//System.out.println("exitOnClose");
		if(rows.size()>0){
			for(FileInfo row:rows){
				DownloadWorker downloadWorker=mapLookUp.get(row);
				if(StaticVar.RUN_DOWNLOAD==downloadWorker.getDownloaddStatus()){
					downloadWorker.pauseTask();
				}
			}
		}
	}
	
}
