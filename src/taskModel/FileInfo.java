/*璐熻矗璁板綍鏂囦欢鐨勫熀鏈俊鎭�
 * */

package taskModel;

import downloadWorker.HttpDownload;

public class FileInfo {
	private static String TITILES[]={"status","icon","fileName","URL","progress","size","savePath"};
	private String fileStatus;//TODO
	private String fileIcon;//TODO
	private String fileType;
	private String fileName;
	private String fileURL;
	private float  progress;
	private String fileSize;
	private String savePath;
	public FileInfo(HttpDownload httpDownload){
		this.fileStatus="??";
		this.fileIcon="??";
		this.fileType=httpDownload.getFileType();
		this.fileName=httpDownload.getFileName();
		this.fileURL=httpDownload.getSrcUrl();
		this.fileSize=httpDownload.getContentLength();
		this.savePath=httpDownload.getSavePath();
		this.progress=0;
	}
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getFileIcon() {
		return fileIcon;
	}
	public void setFileIcon(String fileIcon) {
		this.fileIcon = fileIcon;
	}
	public static String[] getTITILES() {
		return TITILES;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	public float getProgress() {
		return progress;
	}
	public void setProgress(float progress) {
		this.progress = progress;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getSavePath() {
		return savePath;
	}
}
