package downloadWorker;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

import staticVar.StaticVar;
public class HttpDownload {
	//private String srcFile;
	private String ContentLength="0";//,ContentRange;
	private URL url=null;
	private String srcUrl;
	private HttpURLConnection httpUrl=null;
	private String fileName=null;
	private String fileType=null;
	private String savePath=null;
	public HttpDownload(String srcFile,String fileName,String savePath){
		//this.srcFile=srcFile;
		this.srcUrl=srcFile;
		this.savePath=savePath;
		//System.out.println(this.savePath);
		this.fileName=fileName;
		this.savePath=savePath;
		System.out.println("HttpDownload:"+this.savePath);
		try {
			url=new URL(srcFile);
			httpUrl=(HttpURLConnection)url.openConnection();
			System.out.println(httpUrl.getHeaderField("Content-Length"));
			if(httpUrl.getHeaderField("Content-Length")!=null){
				ContentLength=httpUrl.getHeaderField("Content-Length");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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