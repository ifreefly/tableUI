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
	//private HttpDownload httpDownload=null;
	private byte b[]=new byte[1024];
	private HttpURLConnection  httpUrl=null;
	public PieceDownload(Piece piece,DownloadWorker downloadWorker){
		this.piece=piece;
		this.downloadWorker=downloadWorker;
		
	}
	
	@Override
	public void run(){
		StaticVar.countProcess++;
		try {
			this.httpUrl=(HttpURLConnection)new URL(downloadWorker.getHttpDownload().getSrcUrl()).openConnection();
			rfwrite =new RandomAccessFile(downloadWorker.getHttpDownload().getSavePath(),"rw");
			httpUrl.setRequestProperty("User-Agent", "jmultidownload");//设置user-agent
			String contentRange="bytes="+piece.getBegPos()+"-";//设置文件流开始位置
			httpUrl.setRequestProperty("RANGE", contentRange);//设置文件流开始位置
			//out.println("线程"+name+"ContentRange="+httpurl.getHeaderField("Content-Range"));
			input = httpUrl.getInputStream();
			rfwrite.seek(piece.getBegPos());
			/*调试代码
			 * out.println();
			out.println("线程"+name+"开始字节是"+currentPos);
			out.println("线程"+name+"理论结束字节是"+endPos);
			out.println();
			*/
			int c=0;
			System.out.println("downloading");
			System.out.println(downloadWorker.getHttpDownload().getSavePath());
			while((c=input.read(b,0,1024))>0){
				piece.setCurPos(piece.getBegPos()+c);;
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
