package GUI;

import javax.swing.JFrame;

public class TestGui {
	JFrame jframe = new JFrame();
	public TestGui()
	{
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jframe.setSize(800, 600);
		jframe.pack();
	}
	
}
