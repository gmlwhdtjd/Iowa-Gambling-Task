package IGT;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class MessageLabel extends JLabel {
	private static final long serialVersionUID = 7299935258451216006L;

	private ImageIcon positiveIcon;
	private ImageIcon negativeIcon;
	
	public MessageLabel(String defualtText) {			
		positiveIcon = new ImageIcon(getClass().getResource("/Icon_Positive.png"));
		negativeIcon = new ImageIcon(getClass().getResource("/Icon_Negative.png"));
		
		setDefualtStatus(defualtText);
	}
	
	@Override
	public void setPreferredSize(Dimension preferredSize) {
		
		Image imgTmp = positiveIcon.getImage();
		imgTmp = imgTmp.getScaledInstance(preferredSize.height, preferredSize.height, java.awt.Image.SCALE_SMOOTH);
		positiveIcon = new ImageIcon(imgTmp);
		
		imgTmp = negativeIcon.getImage();
		imgTmp = imgTmp.getScaledInstance(preferredSize.height, preferredSize.height, java.awt.Image.SCALE_SMOOTH);
		negativeIcon = new ImageIcon(imgTmp);
		
		super.setPreferredSize(preferredSize);
	}
	
	public void setDefualtStatus(String text) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				setText(text);
				setIcon(null);
			}
		});
	}
	public void setPositiveStatus(String text) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				setText(text);
				setIcon(positiveIcon);
			}
		});
	}
	public void setNegativeStatus(String text) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				setText(text);
				setIcon(negativeIcon);
			}
		});
	}
}
