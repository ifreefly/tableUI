package tableCellRender;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import taskModel.FileInfo;

public class IconLabelRenderer extends JLabel implements TableCellRenderer {

	/**
	 * version:1.0
	 */
	private static final long serialVersionUID = -4135541965820860359L;

	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		// TODO Auto-generated method stub
		if(value instanceof FileInfo){
			String iconType=((FileInfo) value).getFileType();
			URL imageURL=IconLabelRenderer.class.getResource("icon/"+iconType+"png");
			if(imageURL!=null){
				ImageIcon icon=new ImageIcon(imageURL);
				setIcon(icon);
			}else{
				setText("noIcon");
			}
		}
		return this;
	}

}
