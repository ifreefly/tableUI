package staticVar;

public class StaticVar {
	public static final int CONCURRENCY_DOWNLOAD=5;
	public static final int DEFUALT_MAX_THREADS=2;
	public static final int READY_DOWNLOAD=0;
	public static final int RUN_DOWNLOAD=1;
	public static final int PAUSE_DOWNLOAD=2;
	public static final int FINISH_DOWNLOAD=3;
	public static final int DELETE_DOWNLOAD=4;
	public static final String DEV_SAVE_PATH="H:\\home\\workspace\\tableUI\\down\\";
	public static int countProcess=0;
	
	public static final String CONFIG_TASKALL_PATH="\\config\\tasksAll.xml";
	public static final String CONFIG_HISTORY_TASK_PATH="\\config\\hitoryTasks.xml";
	//to make sure the inputed url uses http protocal
	public static final String URL_HTTP_REGEX="((h|H)(t|T){2}(p|P)://)+.*";
	
	//fileAvailable检测文件保存路径是否可用
	public static final int FILE_AVAILABLE=0;
	//保存路径父目录存在但不是目录，而是文件
	public static final int FILE_PARENT_NDIR=1;
	//文件存在且存在配置文件，需要软件倒入配置文件
	public static final int FILE_LOAD_CONFIG=2;
	//文件已经存在但没有配置文件
	public static final int FILE_NAVAILABLE=3;
	
	public static final String SYSTEM_SEPARATOR=System.getProperty("file.separator");
}
