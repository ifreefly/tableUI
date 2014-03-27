package varFilter;
/*閾炬帴浠モ�;鈥濈粨灏�/
 * 
 */
import staticVar.StaticVar;

public class UrlFilter {
	private String urls[];
	public UrlFilter(String urls[]){
		this.urls=urls;
	}
	
	public boolean isValid(){
		for(String url :urls){
			if(!url.matches(StaticVar.URL_HTTP_REGEX)){
				return false;
			}
		}
		return true;
	}
}
