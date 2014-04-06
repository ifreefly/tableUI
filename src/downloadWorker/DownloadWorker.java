package downloadWorker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingWorker;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import configXml.Config;
import staticVar.StaticVar;
import taskModel.FileInfo;
import taskModel.TaskManager;


public class DownloadWorker extends SwingWorker<List<Piece>,Piece>{
	private HttpDownload  httpDownload ;
	private TaskManager taskManager;
	private List<Piece> pieces=new ArrayList<Piece>();
	private int maxThreads,freeThreads;
	private long begPos=0,endPos=0;
	private List<PieceDownload> piecesDownloading=new ArrayList<PieceDownload>();;
	private List<PieceDownload> freePieceDownload=new ArrayList<PieceDownload>();
	private long downloadedBytes=0;
	private long fileSize=0;
	private FileInfo fileInfo;
	private int downloadStatus=StaticVar.READY_DOWNLOAD;
	private Config config;
	public DownloadWorker(HttpDownload httpDownload,FileInfo fileInfo ,TaskManager taskManager){
		maxThreads=StaticVar.DEFUALT_MAX_THREADS;
		freeThreads=maxThreads;
		this.httpDownload=httpDownload;
		this.fileInfo=fileInfo;
		this.taskManager=taskManager;
		addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("progress")) {
                    //model.updateStatus(currentFile, (int) evt.getNewValue());
                	System.out.println(evt.getNewValue());
                    DownloadWorker.this.taskManager.getTaskTableModel().updateStatus(DownloadWorker.this.fileInfo, (int) evt.getNewValue());
                }
            }
        });
	}
	
	public void createConfig(){
		//TODO
		config=new Config(fileInfo.getSavePath()+fileInfo.getFileName()+".xml");
		config.setFileInfo(fileInfo);
		initPieces();
	}
	
	public void loadConfig() throws ParserConfigurationException, SAXException, IOException{
		//TODO
		File configFile=new File(fileInfo.getSavePath()+fileInfo.getFileName()+".xml");
		config=new Config(configFile);
		loadPieces();
	}
	
	public void initPieces(){//下载任务初次运行，没有任何pieces的信息
		fileSize=Long.valueOf(httpDownload.getContentLength());
		//List<Piece> pieces=new ArrayList<Piece>(); 
		long blocks=(long)Math.ceil(fileSize/maxThreads);
		//System.out.println("blocks="+blocks);
		//System.out.println("pieces="+maxThreads);
		for(int i=0;i<maxThreads;i++){
			//System.out.println("initPieces");
			if(i!=maxThreads-1){
				endPos=begPos+blocks;
			}
			else{
				endPos=fileSize;
				//out.println(i+"个"+endPos);
			}
			Piece piece=new Piece(begPos,begPos,endPos);
			begPos=endPos;
			this.pieces.add(0, piece);
			
			//publish(piece);
		}
		config.setPieces(this.pieces);
		config.writeToDisk();
		System.out.println("initPieces");
	}
	
	

	public void loadPieces(){//从配置文件导入pieces信息
		this.pieces=Arrays.asList(config.loadPieces());
	}
	@Override
	protected List<Piece> doInBackground() {
		// TODO Auto-generated method stub
		fileSize=Long.valueOf(httpDownload.getContentLength());
		System.out.println(pieces.toArray());
		//publish((Piece[]) this.pieces.toArray());
		doProcess();
		System.out.println("doInBackGround ");
		setProgress(0);
		System.out.println("fileSzie:"+fileSize);
		System.out.println(getDownloadedBytes());
		while(getDownloadedBytes()<fileSize){
			int progress = (int) Math.round((getDownloadedBytes() * 100d/ fileSize) );
			//System.out.println(getDownloadedBytes());
			config.setReadBytes(getDownloadedBytes());
			System.out.println("wtf");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			config.setPieces(this.pieces);
			config.writeToDisk();
			setProgress(progress);
		}
		setProgress(100);
		return pieces;
	}
	
	protected void doProcess(){//process没有得到很好的执行故更改为doProcess来替代
		while(freeThreads>0){
			System.out.println("process");
			Piece piece=this.pieces.get(freeThreads-1);
			PieceDownload pieceDownload=new PieceDownload(piece,this);
			piecesDownloading.add(pieceDownload);
			downloadStatus=StaticVar.RUN_DOWNLOAD;
			pieceDownload.start();
			freeThreads--;
			System.out.println("freeThreads="+freeThreads);
		}
	}
	@Override
	protected void process(List<Piece> pieces){
		
		/*for(Piece piece:pieces){
			this.pieces.add(piece);
		}*/
		System.out.println("process");
		while(freeThreads>0){
			System.out.println("process");
			Piece piece=this.pieces.get(freeThreads-1);
			PieceDownload pieceDownload=new PieceDownload(piece,this);
			piecesDownloading.add(pieceDownload);
			downloadStatus=StaticVar.RUN_DOWNLOAD;
			pieceDownload.start();
			freeThreads--;
			System.out.println("freeThreads="+freeThreads);
		}
	}
			
	
	@Override 
	protected void done(){//善后，处理相关配置文件等
		//TODO
		setProgress(100);
		config.writeToDisk();
		config.deleteConfig();
		taskManager.addToHistory(fileInfo);
		System.out.println("done");
	}
	
	protected synchronized void growBytes(long downloadBytes){
		this.downloadedBytes+=downloadBytes;
	}
	private synchronized long getDownloadedBytes(){
		return downloadedBytes;
	}
	protected HttpDownload getHttpDownload(){
		return httpDownload;
	}
	public void startTask(){
		this.downloadStatus=StaticVar.RUN_DOWNLOAD;
		for(PieceDownload pieceDownload : piecesDownloading){
			pieceDownload.toContinue();
		}
		
	}
	public void pauseTask(){
		this.downloadStatus=StaticVar.PAUSE_DOWNLOAD;
	}
	public int getDownloaddStatus(){
		return this.downloadStatus;
	}
	
	
}
