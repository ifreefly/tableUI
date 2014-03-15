package tableCellRender;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ProgressCellRenderer extends JProgressBar implements TableCellRenderer{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3311620949239317373L;

	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		// TODO Auto-generated method stub
		int progress = 0;
		//System.out.println("wtf");
		//System.out.println(column);
        if (value instanceof Float) {
            progress = Math.round(((Float) value) * 100f);
        } else if (value instanceof Integer) {
            progress = (int) value;
        }
        setValue(progress);
        return this;
	}

}
