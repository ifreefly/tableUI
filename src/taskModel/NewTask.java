package taskModel;

public class NewTask {
	private String src;
	private String name;
	private String savePath;
	public NewTask(String src,String name,String savePath){
		this.src=src;
		this.name=name;
		this.savePath=savePath;
	}
	
	public String getSrc() {
		return src;
	}
	public String getName(){
		return name;
	}
	public String getSavePath(){
		return savePath;
	}
}
