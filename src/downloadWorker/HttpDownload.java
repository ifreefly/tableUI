package downloadWorker;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;


public class HttpDownload {
	//private String srcFile;
	private static int maxConnectTime=5;
	private String ContentLength="0";//,ContentRange;
	private URL url=null;
	private String srcUrl;
	private HttpURLConnection httpUrl=null;
	private String fileName=null;
	private String fileType=null;
	private String savePath=null;
	private int connectTime=0;
	public HttpDownload(String srcFile,String fileName,String savePath){
		/*需要改进的地方:本处连接应该采用多线程进行连接，如果不采用，本处将会可能出现无限连接导致
		 * 程序无法响应，出现假死情况。*/
		this.srcUrl=srcFile;
		this.savePath=savePath;
		this.fileName=fileName;
		this.savePath=savePath;
		System.out.println("HttpDownload:"+this.savePath);
		int respCode=-1;
		while(connectTime<=maxConnectTime){
			try {
				url=new URL(srcFile);
				httpUrl=(HttpURLConnection)url.openConnection();
				//System.out.println(url.getHost());
				int timeout=2000;
				httpUrl.setConnectTimeout(timeout);
				httpUrl.setRequestMethod("HEAD");
				respCode=httpUrl.getResponseCode();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//if(this.connectTime==HttpDownload.maxConnectTime)
				this.connectTime++;
				System.out.println("获取资源失败");
				//System.out.println(respCode);
			}
		}
		if(this.connectTime<HttpDownload.maxConnectTime){
			//System.out.println("HttpDownload:connectTime="+this.connectTime);
			if(200==respCode){
				System.out.println("finally");
				if(httpUrl.getHeaderField("Content-Length")!=null){
					ContentLength=httpUrl.getHeaderField("Content-Length");
					httpUrl.disconnect();//指示服务器近期不太可能有其他请求
				}
			}
		}else{
			//TODO 经过最大连接尝试后文件依旧无法连接，此时应该给该任务做相应的处理，但是此处还不清楚需要做哪些处理，故TODO
			//System.out.println("HttpDownload:connectTime="+this.connectTime);
		}
	}
	public String getSrcUrl(){
		return this.srcUrl;
	}
	
	/*public String getContentRange() {
		ContentRange=httpUrl.getHeaderField("Content-Range");
		return ContentRange;
	}*/
	public String getContentLength() {
		
		return ContentLength;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String srcFile) {
		this.fileType = srcFile.substring(srcFile.lastIndexOf(".")+1, srcFile.length());
	}
	
	public String getSavePath() {
		return savePath;
	}
	
	public boolean isHttpUrlNull(){
		if(null==httpUrl){
			return true;
		}
		return false;
	}
}