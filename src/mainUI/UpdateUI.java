package mainUI;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import staticVar.StaticVar;
import tableCellRender.ProgressCellRenderer;
import taskModel.TaskManager;
import taskModel.TaskTableModel;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
public class UpdateUI {
	public static void main(String args[]){
		new UpdateUI();
	}
	public UpdateUI(){
		EventQueue.invokeLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                	} 
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                	}
				final TaskManager taskManager=new TaskManager();
				TaskTableModel model=taskManager.getTaskTableModel();
				final JTable table=new JTable(model);
				table.getColumn("progress").setCellRenderer(new ProgressCellRenderer());
				JScrollPane scrollPane=new JScrollPane(table);
				JToolBar toolBar=new JToolBar();
				Action add = new AbstractAction("Add Download") {

		            /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
		            public void actionPerformed(ActionEvent e) {
						JTextArea textArea=new JTextArea(5,20);
		                int okclk=JOptionPane.showConfirmDialog(
		                		table, 
		                		textArea,
		                		"please input urls",
		                		JOptionPane.OK_CANCEL_OPTION);
		                if(okclk==JOptionPane.OK_OPTION){
		                	taskManager.addTask(table,textArea.getText());
		                }
		            }
		        };
		        Action puase=new AbstractAction("Pause Download"){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						int index=table.getSelectedRow();
						if(index<0){
							JOptionPane.showMessageDialog(
									table, 
									"Select a download to pause",
									"error",
									JOptionPane.ERROR_MESSAGE);
						}else{
							taskManager.pauseTask(index);
						}
					}
		        	
		        };
		        Action delete=new AbstractAction("Delete Download"){
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						int index=table.getSelectedRow();
						if(index<0){
							JOptionPane.showMessageDialog(
									table, 
									"Select a download to delete",
									"error",
									JOptionPane.ERROR_MESSAGE);
						}else{
							taskManager.removeTask(index);
						}
					}
		        	
		        };
		        
		        toolBar.add(add);
		        toolBar.addSeparator();
		        toolBar.add(delete);
		        JFrame frame=new JFrame("FlashGot");
		        // Ensures JVM closes after frame(s) closed and
                // all non-daemon threads are finished
				//frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(toolBar,BorderLayout.NORTH);
                frame.add(scrollPane);
                frame.pack();
                //frame.setLocationRelativeTo(null);
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                System.out.println(StaticVar.DEV_SAVE_PATH);
			}
			
		});
	}
	
}
