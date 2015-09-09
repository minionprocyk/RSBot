package procyk.industries.gui;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

import procyk.industries.constants.AreaConstants;
import procyk.industries.constants.FarmableLocationNameConstants;
import procyk.industries.constants.PathConstants;
import procyk.industries.engines.SkillsEngine.SkillType;

public class TrainSkillGui extends JFrame {

	private JPanel contentPane;
	private static TrainSkillGui gui;
	public boolean wait=true;
	public boolean stop=false;
	private static final long serialVersionUID = 556665748748751532L;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	//Variables that need to be carried over
	private TextArea txtDebug;
	private JButton btnadd;
	private JComboBox<String> cboLocation;
	private java.awt.List objectList;
	private JCheckBox chckbxBank;
	String skillArea="";
	boolean isBanking=false;
	SkillType skill=null;

	/**
	 * Create the frame.
	 */
	private TrainSkillGui() {
		
		
		setTitle("TrainSkillGui");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 429, 279);
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
		cboLocation = new JComboBox<String>();
		cboLocation.setBounds(192, 8, 218, 20);
		contentPane.add(cboLocation);
		for(Field f: FarmableLocationNameConstants.class.getFields())
		{
			try {
				cboLocation.addItem((String) f.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
		JPanel rdoPanel = new JPanel();
		rdoPanel.setBorder(new TitledBorder(null, "Select skill", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		rdoPanel.setBounds(10, 5, 121, 127);
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
		txtDebug.setEditable(false);
		txtDebug.setBounds(138, 138, 272, 101);
		contentPane.add(txtDebug);
		
		JButton btnStart = new JButton("START");
		btnStart.setBounds(10, 139, 121, 100);
		contentPane.add(btnStart);
		
		JComboBox<String> cboFarmObject = new JComboBox<String>();
		cboFarmObject.setEditable(true);
		cboFarmObject.setBounds(258, 33, 152, 20);
		contentPane.add(cboFarmObject);
		cboFarmObject.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)btnadd.doClick();
			}
		});
		ArrayList<String> unsorted = new ArrayList<String>();
		for(Field object : procyk.industries.constants.FarmableObjectNameConstants.class.getFields())
		{
			try {
				unsorted.add((String) object.get(null));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		Collections.sort(unsorted);
		for(String item : unsorted)cboFarmObject.addItem(item);
		
		JLabel lblObjectName = new JLabel("Farm: ");
		lblObjectName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblObjectName.setBounds(204, 33, 56, 20);
		contentPane.add(lblObjectName);
		
		
		JLabel lblLocation = new JLabel("Location: ");
		lblLocation.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLocation.setBounds(138, 11, 56, 14);
		contentPane.add(lblLocation);

		btnadd = new JButton("Add");
		
		btnadd.setBounds(321, 64, 89, 33);
		contentPane.add(btnadd);
		
		objectList = new java.awt.List();
		objectList.setBounds(137, 62, 178, 70);
		contentPane.add(objectList);
	
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//remove an item from the list if it is selected
				if(objectList.getSelectedItem()!=null)objectList.remove(objectList.getSelectedItem());
			}
		});
		btnRemove.setBounds(321, 95, 89, 33);
		contentPane.add(btnRemove);
		
		chckbxBank = new JCheckBox("Bank");
		chckbxBank.setSelected(true);
		chckbxBank.setBounds(142, 32, 56, 23);
		contentPane.add(chckbxBank);
		btnadd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(cboFarmObject.getSelectedItem() != null && cboFarmObject.getSelectedIndex() != -1)
				{
					for(String listItems : objectList.getItems())
					{	
						if(listItems == cboFarmObject.getSelectedItem().toString())
						{
							cboFarmObject.setSelectedIndex(-1);
							return;
						}
					}
					objectList.add(cboFarmObject.getSelectedItem().toString());
					cboFarmObject.setSelectedIndex(-1);
				
				}
			}
			
		});
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//set the values of everything
				skillArea = cboLocation.getSelectedItem().toString();
				isBanking = chckbxBank.isSelected();
				
				if(rdbtnMining.isSelected())skill = SkillType.Mining;
				if(rdbtnWoodcutting.isSelected())skill = SkillType.Woodcutting;
				if(rdbtnFishing.isSelected())skill = SkillType.Fishing;
				
				setVisible(false);
				wait=false;
			}
		});
	}
	public static TrainSkillGui GetInstance()
	{
		if(gui==null)gui=new TrainSkillGui();
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
	public Area getSiteArea()
	{
		//loop through the fields in farmable locations for a location that fits the selected location
		//use the field identifier to find the corresponding site location and return the value
		String skillArea = "";
		for(Field f: FarmableLocationNameConstants.class.getFields())
		{
			try {
				System.out.println(f.get(null).toString()+" | "+cboLocation.getSelectedItem().toString());
				if(((String)f.get(null)).equals(cboLocation.getSelectedItem().toString()))
				{
					skillArea = f.getName()+"_SITE";
					for(Field f2: AreaConstants.class.getFields())
					{
						if(skillArea.equals(f2.getName()))
							try {
								return (Area)f2.get(null);
							} catch (IllegalArgumentException | IllegalAccessException e) {
								e.printStackTrace();
							}
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
		throw new NullPointerException("TranSkillGui: No skill location has a matching area");
	}
	public Area getBankArea()
	{
		String bankArea="";
		for(Field f: FarmableLocationNameConstants.class.getFields())
		{
			try {
				System.out.println(f.get(null).toString()+" | "+cboLocation.getSelectedItem().toString());
				if(((String)f.get(null)).equals(cboLocation.getSelectedItem().toString()))
				{
					bankArea = f.getName()+"_BANKSITE";
					for(Field f2 : AreaConstants.class.getFields())
					{
						if(bankArea.equals(f2.getName()))
							try {
								return (Area)f2.get(null);
							} catch (IllegalArgumentException | IllegalAccessException e) {
								e.printStackTrace();
							}
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
		throw new NullPointerException("TrainSkillGui: No bank location has a matching area");
	}
	public Tile[] getPathToBank()
	{
		String pathName="";
		for(Field f : FarmableLocationNameConstants.class.getFields())
		{
			try {
				System.out.println(f.get(null).toString()+" | "+cboLocation.getSelectedItem().toString());
				if(((String)f.get(null)).equals(cboLocation.getSelectedItem().toString()))
				{
					
					pathName = f.getName()+"_SITE_TO_BANK";
					for(Field f2 : PathConstants.class.getFields())
					{
						if(pathName.equals(f2.getName()))
							try {
								return (Tile[])f2.get(null);
							} catch (IllegalArgumentException | IllegalAccessException e) {
								e.printStackTrace();
							}
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
		}
		throw new NullPointerException("TrainSkillGui: No preset path from site to bank found");
	}
	public String[] getObjects()
	{
		return objectList.getItems();
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
