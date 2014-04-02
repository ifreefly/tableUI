package downloadWorker;

public class Piece {
	private long begPos;
	private long curPos;
	private long endPos;
	public Piece(long begPos,long curPos,long endPos){
		this.begPos=begPos;
		this.curPos=curPos;
		this.endPos=endPos;
	}
	public Piece(){
		
	}
	public long getEndPos() {
		return endPos;
	}
	public void setEndPos(long endPos) {
		this.endPos = endPos;
	}
	public long getBegPos() {
		return begPos;
	}
	public void setBegPos(long begPos) {
		this.begPos = begPos;
	}
	public long getCurPos() {
		return curPos;
	}
	public void setCurPos(long curPos) {
		this.curPos = curPos;
	}
	protected Piece toCutPieces(){
		long toCut=endPos-curPos;
		if(toCut>4*1024){
			long begPos=(long) (curPos+Math.ceil(toCut/2));
			long curPos=begPos;
			long endPos=this.endPos;
			Piece piece=new Piece(begPos,curPos,endPos);
			return piece;
		}
		return null;
	}
}
