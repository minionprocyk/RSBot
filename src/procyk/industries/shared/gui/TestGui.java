package procyk.industries.shared.gui;

import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class TestGui extends JFrame{
	private static TestGui gui;
	public boolean wait=true;
	public boolean stop=false;
	private static final long serialVersionUID = 556665748748751532L;
	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	//Variables that need to be carried over
	private TextArea txtDebug;
	String skillArea="";
	boolean isBanking=false;
	SkillType skill=null;
	private TestGui()
	{
		setTitle("TestGui");
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 429, 222);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent arg0) {
				super.windowClosed(arg0);
				stop=true;
			}
		});
		JComboBox<String> cboLocation = new JComboBox<String>();
		cboLocation.setEditable(true);
		cboLocation.setBounds(260, 14, 150, 20);
		contentPane.add(cboLocation);
		cboLocation.addItem(new String("Draynor Manor"));
		
		JPanel chkbxPanel = new JPanel();
		chkbxPanel.setBorder(new TitledBorder(null, "Additional Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		chkbxPanel.setBounds(141, 14, 109, 72);
		contentPane.add(chkbxPanel);
		chkbxPanel.setLayout(null);
		
		JCheckBox chckbxBanking = new JCheckBox("Banking");
		chckbxBanking.setBounds(6, 16, 97, 23);
		chkbxPanel.add(chckbxBanking);
		
		JCheckBox chkbxDrop = new JCheckBox("Drop");
		chkbxDrop.setBounds(6, 42, 97, 23);
		chkbxPanel.add(chkbxDrop);
		
		JPanel rdoPanel = new JPanel();
		rdoPanel.setBorder(new TitledBorder(null, "Select skill", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		rdoPanel.setBounds(10, 14, 121, 100);
		contentPane.add(rdoPanel);
		rdoPanel.setLayout(null);
		
		JRadioButton rdbtnFishing = new JRadioButton("Fishing");
		rdbtnFishing.setSelected(true);
		buttonGroup.add(rdbtnFishing);
		rdbtnFishing.setBounds(6, 16, 109, 23);
		rdoPanel.add(rdbtnFishing);
		
		JRadioButton rdbtnMining = new JRadioButton("Mining");
		buttonGroup.add(rdbtnMining);
		rdbtnMining.setBounds(6, 44, 109, 23);
		rdoPanel.add(rdbtnMining);
		
		JRadioButton rdbtnWoodcutting = new JRadioButton("WoodCutting");
		buttonGroup.add(rdbtnWoodcutting);
		rdbtnWoodcutting.setBounds(6, 70, 109, 23);
		rdoPanel.add(rdbtnWoodcutting);
		
		txtDebug = new TextArea();
		txtDebug.setBounds(140, 92, 270, 84);
		contentPane.add(txtDebug);
		
		JButton btnStart = new JButton("START");
		btnStart.setBounds(10, 125, 124, 51);
		contentPane.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//set the values of everything
				skillArea = cboLocation.getSelectedItem().toString();
				isBanking = chckbxBanking.isSelected();
				
				if(rdbtnMining.isSelected())skill = SkillType.Mining;
				if(rdbtnWoodcutting.isSelected())skill = SkillType.Woodcutting;
				if(rdbtnFishing.isSelected())skill = SkillType.Fishing;
				
				setVisible(false);
				wait=false;
			}
		});
		
		
	}
	public static TestGui GetInstance()
	{
		if(gui==null)gui=new TestGui();
		return gui;
	}
	public void destroy()
	{
		dispose();
		gui=null;
	}
	public void AppendDebugText(String text)
	{
		txtDebug.append(text);
	}
	public String getSkillArea()
	{
		return this.skillArea;
	}
	public SkillType getSkillType()
	{
		return this.skill;
	}
	public boolean getBanking()
	{
		return this.isBanking;
	}
	
	
}
