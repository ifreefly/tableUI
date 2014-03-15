package downloadWorker;

import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingWorker;

import staticVar.StaticVar;
import taskModel.TaskTableModel;

public class DownloadWorker extends SwingWorker<List<Piece>,Piece>{
	private HttpDownload  httpDownload ;
	private TaskTableModel taskTableModel;
	private List<Piece> pieces=new ArrayList<Piece>();
	private int freeThreads;
	private long begPos=0,endPos=0;
	private List<PieceDownload> piecesDownloading=new ArrayList<PieceDownload>();;
	private List<PieceDownload> freePieceDownload=new ArrayList<PieceDownload>();
	private long downloadedBytes=0;
	private long fileSize=0;
	
	public DownloadWorker(HttpDownload httpDownload,TaskTableModel taskTableModel){
		freeThreads=StaticVar.DEFUALT_MAX_THREADS;
		this.httpDownload=httpDownload;
		System.out.println("DownloadWorker");
		fileSize=Long.valueOf(httpDownload.getContentLength());
		this.taskTableModel=taskTableModel;
		addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            	//System.out.println(evt);
                if (evt.getPropertyName().equals("progress")) {
                    //model.updateStatus(currentFile, (int) evt.getNewValue());
                	System.out.println(evt.getNewValue());
                    DownloadWorker.this.taskTableModel.updateStatus(DownloadWorker.this, (int) evt.getNewValue());
                }
            }
        });
	}

	@Override
	protected List<Piece> doInBackground() throws Exception {
		// TODO Auto-generated method stub
		long blocks=(long)Math.ceil(fileSize/freeThreads);
		//System.out.println("blocks="+blocks);
		System.out.println("pieces="+freeThreads);
		for(int i=0;i<freeThreads;i++){
			
			if(i!=freeThreads-1){
				endPos=begPos+blocks;
			}
			else{
				endPos=fileSize;
				//out.println(i+"个"+endPos);
			}
			Piece piece=new Piece(begPos,endPos);
			begPos=endPos;
			publish(piece);
		}
		
		setProgress(0);
		while(getDownloadedBytes()<fileSize){
			int progress = (int) Math.round((getDownloadedBytes() * 100d/ fileSize) );
			System.out.println(getDownloadedBytes());
			Thread.sleep(1000);
			setProgress(progress);
			System.out.println("Active="+piecesDownloading.size());
		}
		setProgress(100);
		return pieces;
	}
	
	@Override
	protected void process(List<Piece> pieces){
		
		for(Piece piece:pieces){
			this.pieces.add(piece);
		}
		while(freeThreads>0){
			Piece piece=this.pieces.get(freeThreads-1);
			PieceDownload pieceDownload=new PieceDownload(piece,this);
			piecesDownloading.add(pieceDownload);
			pieceDownload.start();
			freeThreads--;
			System.out.println("freeThreads="+freeThreads);
		}
		//System.out.println("ok2");
	}
			
	
	@Override 
	protected void done(){//善后，处理相关配置文件等
		//TODO
		System.out.println("done");
	}
	
	protected synchronized void growBytes(long downloadBytes){
		this.downloadedBytes+=downloadBytes;
	}
	private long getDownloadedBytes(){
		return downloadedBytes;
	}
	protected HttpDownload getHttpDownload(){
		return httpDownload;
	}
}
