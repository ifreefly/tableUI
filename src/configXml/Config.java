package configXml;
/*保存文件的基本信息，如断点，url等。文件格式为
 * <File>
 * 	<FileInfo name="" savePath="" fileSize="" fileType="" fileUrl="" readBytes="">
 * 	<Pieces>
 * 		<piece start="" cur="" end=""/>
 * 		<piece start="" cur="" end=""/>
 * 		...
 * 	</Pieces>
 * </File>
 * */
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import staticVar.StaticVar;
import taskModel.FileInfo;
import downloadWorker.HttpDownload;
import downloadWorker.Piece;

public class Config {
	private Document document;
	private Element root,info,pieces;
	private File configFile;
	
	public Config(String configPath){
		//TODO
		document=ConfigOpera.createDocument();
		configFile=new File(configPath);
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		root=document.createElement("File");
		info=document.createElement("FileInfo");
		pieces=document.createElement("Pieces");
		document.appendChild(root);
		root.appendChild(info);
		root.appendChild(pieces);
	}
	
	public Config(File configFile) throws ParserConfigurationException, SAXException, IOException{
		//TODO
		this.configFile=configFile;
		this.document=ConfigOpera.creatDocument(configFile);
	}
	
	/* 设置文件信息到配置文件中，以便再次打开软件能够继续下载
	 * */
	public void setFileInfo(FileInfo fileInfo){
		
		info.setAttribute("fileName", fileInfo.getFileName());
		info.setAttribute("savePath", fileInfo.getSavePath());
		info.setAttribute("fileSize", fileInfo.getFileSize());
		info.setAttribute("fileType", fileInfo.getFileType());
		info.setAttribute("fileUrl",  fileInfo.getFileURL());
	}
	
	public void setReadBytes(long readBytes){
		info.setAttribute("readBytes", String.valueOf(readBytes));
		System.out.println("readBytes="+info.getAttribute("readBytes"));
	}
	
	/*保存文件的断点信息，其中piecces中的piece是无序的，但新加入的piece在最后
	 * 需要优化。
	 * */
	public void setPieces(List<Piece> pieces){
		 NodeList piecesNode=document.getElementsByTagName("Pieces");
		 Node node=piecesNode.item(0);
		 if(Node.ELEMENT_NODE==node.getNodeType()){
			 Element eNode=(Element)node;
			 NodeList nPieces=eNode.getElementsByTagName("Piece");
			 if(nPieces.getLength()<=0){//配置文件尚无任何节点信息
				 for(Piece piece : pieces){
					 Element ePiece=document.createElement("Piece");
					 ePiece.setAttribute("start", String.valueOf(piece.getBegPos()));
					 ePiece.setAttribute("cur", String.valueOf(piece.getCurPos()));
					 ePiece.setAttribute("end", String.valueOf(piece.getEndPos()));
					 eNode.appendChild(ePiece);
				 }
			 }else if(nPieces.getLength()==pieces.size()){//更新文件节点信息，没有新增片段，直接更新cur即可
				for(int i=0;i<nPieces.getLength();i++){
					Element ePiece=(Element)nPieces.item(i);
					ePiece.setAttribute("cur", String.valueOf(pieces.get(i).getCurPos()));
				}
			 }else{//新增了文件片段，更新文件信息
				 for(int i=0;i<nPieces.getLength();i++){//更新原节点信息
					 Element ePiece=(Element)nPieces.item(i);
					 ePiece.setAttribute("start", String.valueOf(pieces.get(i).getBegPos()));
					 ePiece.setAttribute("cur", String.valueOf(pieces.get(i).getCurPos()));
					 ePiece.setAttribute("end", String.valueOf(pieces.get(i).getEndPos()));
				 }
				 for(int i=nPieces.getLength();i<pieces.size();i++){//增加新增节点信息
					 Element ePiece=document.createElement("Piece");
					 ePiece.setAttribute("start", String.valueOf(pieces.get(i).getBegPos()));
					 ePiece.setAttribute("cur", String.valueOf(pieces.get(i).getCurPos()));
					 ePiece.setAttribute("end", String.valueOf(pieces.get(i).getEndPos()));
					 eNode.appendChild(ePiece);
				 }
			 }
		 }
	}
	
	public Piece[] loadPieces(){
		Piece pieces[];
		NodeList nodeList=document.getElementsByTagName("Pieces");
		Node node=nodeList.item(0);
		if(Node.ELEMENT_NODE==node.getNodeType()){
			Element eNode=(Element)node;
			NodeList npieces=eNode.getElementsByTagName("Piece");
			if(npieces.getLength()>0){
				pieces=new Piece[npieces.getLength()];
				for(int i=0;i<npieces.getLength();i++){
					Element nNode=(Element)npieces.item(i);
					//System.out.println(nNode.getAttribute("start"));
					pieces[i]=new Piece();
					pieces[i].setBegPos(Integer.valueOf(nNode.getAttribute("start")));
					pieces[i].setCurPos(Integer.valueOf(nNode.getAttribute("cur")));
					pieces[i].setEndPos(Integer.valueOf(nNode.getAttribute("end")));
				}
				return pieces;
			}else{
				return null;
			}
		}
		return null;
	}
	
	public HttpDownload loadHttpDownload(){
		HttpDownload httpDownload=null;
		NodeList nodeList=document.getElementsByTagName("FileInfo");
		Node node=nodeList.item(0);
		if(Node.ELEMENT_NODE==node.getNodeType()){
			Element eNode=(Element)node;
			httpDownload=new HttpDownload(eNode.getAttribute("fileUrl"),
					eNode.getAttribute("savePath").substring(0, eNode.getAttribute("savePath").lastIndexOf(StaticVar.SYSTEM_SEPARATOR))+StaticVar.SYSTEM_SEPARATOR);
		}
		
		return httpDownload;
	}
	
	public void writeToDisk(){
		ConfigOpera.writeToDisk(document,configFile);
	}
	
	public boolean deleteConfig(){//在文件下载完毕后删除配置文件
		return this.configFile.delete();
	}
	
	public Element getRoot(){
		return this.root;
	}
	
}
