package downloadWorker;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
public class HttpDownload {
	//private String srcFile;
	private String ContentLength,ContentRange;
	private URL url=null;
	private String srcUrl;
	private HttpURLConnection httpUrl=null;
	private String fileName=null;
	private String fileType=null;
	private String savePath=null;
	public HttpDownload(String srcFile,String savePath){
		//this.srcFile=srcFile;
		this.srcUrl=srcFile;
		this.savePath=savePath;
		//System.out.println(this.savePath);
		setFileName(srcFile);
		this.savePath=savePath+getFileName();
		try {
			url=new URL(srcFile);
			httpUrl=(HttpURLConnection)url.openConnection();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String getSrcUrl(){
		return this.srcUrl;
	}
	
	public String getContentRange() {
		ContentRange=httpUrl.getHeaderField("Content-Range");
		return ContentRange;
	}
	public String getContentLength() {
		ContentLength=httpUrl.getHeaderField("Content-Length");
		return ContentLength;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String srcFile) {
		fileName=srcFile.substring(srcFile.lastIndexOf("/")+1, srcFile.length());
		//System.out.println(fileName);
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
	
}