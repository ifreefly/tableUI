/*author:idevcod@163.com
 *description:对文件片段进行下载，实际的文件下载
 * */
package downloadWorker;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import staticVar.StaticVar;

public class PieceDownload extends Thread{
	private InputStream input=null;
	private RandomAccessFile rfwrite=null;
	private Piece piece;
	private DownloadWorker downloadWorker;
	private int connectTime=0;
	private static int maxConnectTime=5;
	//private HttpDownload httpDownload=null;
	private byte b[]=new byte[1024];
	private HttpURLConnection  httpUrl=null;
	public PieceDownload(Piece piece,DownloadWorker downloadWorker){
		this.piece=piece;
		this.downloadWorker=downloadWorker;
		//以下三行是调试代码：
		System.out.println("DonwloadWorker:Beg"+piece.getBegPos());
		System.out.println("DonwloadWorker:Cur"+piece.getCurPos());
		System.out.println("DonwloadWorker:End"+piece.getEndPos());
	}
	
	@Override
	public void run(){
		StaticVar.countProcess++;
		//需要增加while循环检测connectionTimeOut并作相应处理，使其重新连接
		while(this.connectTime<=maxConnectTime){ 
			try {
				this.httpUrl=(HttpURLConnection)new URL(downloadWorker.getHttpDownload().getSrcUrl()).openConnection();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//需要重写，在catch到IOException（connectionTimeOut）后将连接次数+1
				this.connectTime++;
				e.printStackTrace();
			} 
		}
		
		try{
			if(this.connectTime<maxConnectTime){
				if(piece.getCurPos()<piece.getEndPos()){
					rfwrite =new RandomAccessFile(downloadWorker.getHttpDownload().getSavePath()+downloadWorker.getHttpDownload().getFileName(),"rw");
					httpUrl.setRequestProperty("User-Agent", "jmultidownload");//设置user-agent
					String contentRange="bytes="+piece.getCurPos()+"-";//设置文件流开始位置
					httpUrl.setRequestProperty("RANGE", contentRange);//设置文件流开始位置
					//out.println("线程"+name+"ContentRange="+httpurl.getHeaderField("Content-Range"));
					if(206==httpUrl.getResponseCode()){
						//服务器响应206：partial content，该响应对应Range字段
						input = httpUrl.getInputStream();
						rfwrite.seek(piece.getCurPos());
						int c=0;
						while((c=input.read(b,0,1024))>0){
							synchronized (this){
								if(StaticVar.PAUSE_DOWNLOAD==downloadWorker.getDownloaddStatus()){
									this.wait();
								}
							}
							System.out.println("begPos="+piece.getBegPos()+"/curPos"+piece.getCurPos()+"/endPos"+piece.getEndPos());
							piece.setCurPos(piece.getCurPos()+c);
							if(piece.getCurPos()>=piece.getEndPos()){//该块已传输结束
								int realWrite=c-(int)(piece.getCurPos()-piece.getEndPos());//读写修正
								rfwrite.write(b, 0, realWrite);
								piece.setCurPos(piece.getEndPos());
								downloadWorker.growBytes(realWrite);
								break;
							}else{
								rfwrite.write(b,0,c);
								downloadWorker.growBytes(c);
							}
						}
						//out.println("线程"+name+"结束字节是"+currentPos);
						input.close();
						rfwrite.close();
					}
				}
			}else{
				//TODO超出最大连接次数，应进行出错处理
			}
		}catch(IOException | InterruptedException e){
			e.printStackTrace();
		}
	}
	
	protected synchronized void toContinue(){
		this.notifyAll();
	}
}
