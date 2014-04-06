package mainUI;
/*description:当用户新建任务时所弹出的对话框
 *author:idevcod@163.com
 *date:2014-04-03
 * */

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import configXml.ConfigPath;


public class NewTaskDialog extends JDialog implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String SAVE_PATH="\\config\\savePath.xml";
	
	private JPanel srcPanel=new JPanel();
	private JPanel namePanel=new JPanel();
	private JPanel savePanel=new JPanel();
	private JPanel decisionPanel=new JPanel();
	private JLabel srcLabel=new JLabel("url");
	private JLabel nameLabel=new JLabel("fileName");
	private JLabel savePathLabel=new JLabel("savePath");
	private JTextField srcField=new JTextField();
	private JTextField nameField=new JTextField();
	private JTextField savePathField=new JTextField();
	private JButton savePathButton=new JButton("file");
	private JButton okButton=new JButton("ok");
	private JButton cancelButton=new JButton("cancel");
	private JFileChooser chooser=new JFileChooser();
	
	public static int OK_BUTTON=1;
	public static int CANCEL_BUTTON=2;
	private int decision=0;
	
	private ConfigPath config=new ConfigPath(SAVE_PATH);
	public NewTaskDialog(JFrame owner,boolean modal){
		super(owner,modal);
		init();
	}
	
	public void init(){
		this.setTitle("new Task");
		this.setLayout(new GridLayout(0,1));
		//this.setSize(preferSize);
		srcPanel.setLayout(null);
		srcPanel.add(srcLabel);
		srcPanel.add(srcField);
		//preferSize.width/5
		srcLabel.setBounds(0,0,60,30);
		srcField.setBounds(60,0,320,30);
		srcField.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				change();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				change();
			}
			
			public void change(){
				String url=srcField.getText();
				if(url.length()>0){
					String fileName=url.substring(url.lastIndexOf("/")+1, url.length());
					/*if(fileName.matches("*.*")){
						nameField.setText(fileName);
					}else{
						nameField.setText("unknown");
					}*/
					nameField.setText(fileName);
				}
			}
			
		});
		this.add(srcPanel);
		namePanel.setLayout(null);
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		nameLabel.setBounds(0,0,60,30);
		nameField.setBounds(60,0,320,30);
		this.add(namePanel);
		savePanel.setLayout(null);
		//savePanel.setLayout(new GridLayout(0,3));
		savePanel.add(savePathLabel);
		savePanel.add(savePathField);
		savePanel.add(savePathButton);
		savePathLabel.setBounds(0,0,60,30);
		savePathField.setBounds(60,0,260,30);
		savePathButton.setBounds(320, 0, 60, 30);
		savePathField.setText(config.getPath());
		savePathButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource()==savePathButton){
					openFile();
				}
			}});
		this.add(savePanel);
		decisionPanel.add(okButton);
		decisionPanel.add(cancelButton);
		okButton.setBounds(60, 0, 60, 30);
		cancelButton.setBounds(150, 0, 60, 30);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		this.add(decisionPanel);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		//System.out.println(this.getSize());
		//System.out.println("height="+preferSize.height+"width="+preferSize.width);
		
	}
	
	
	public int showDialog(){
		this.setSize(400,200);
		this.setVisible(true);
		this.setResizable(false);
		return getDecision();
	}
	
	public void openFile(){
		if(chooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
			File file=chooser.getSelectedFile();
			if(file!=null){
				savePathField.setText(file.getPath());
			}
		};
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==okButton){
			setDecision(OK_BUTTON);
			config.setPath(savePathField.getText());
			config.writeToDisk();
			this.dispose();
		}else{
			setDecision(CANCEL_BUTTON);
			this.dispose();
		}
	}

	private int getDecision() {
		return decision;
	}

	private void setDecision(int decision) {
		this.decision = decision;
	}

	public JTextField getSrcField() {
		return srcField;
	}

	public JTextField getNameField() {
		return nameField;
	}

	public JTextField getSavePathField() {
		return savePathField;
	}
	
}

