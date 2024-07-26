import java.util.*;
import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
public class Driver {
	public static void main(String[] args) throws FileNotFoundException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int frameWidth = (int)screenSize.getWidth(), frameHeight = (int)screenSize.getHeight();
		JFrame frame = new JFrame();
		
		frame.setSize(frameWidth, frameHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a CSV File");
        fileChooser.showOpenDialog(null);

        // Read the first line of the file and parse integers
        File file = fileChooser.getSelectedFile();
    	Scanner sc = new Scanner(file);
        String line = sc.nextLine();
        String[] values = line.split(","); 
        int[] A = new int[values.length];
        for (int i=0; i<values.length; i++) 
        	A[i] = Integer.parseInt(values[i].trim());
        

		
		SortVisualizer sv = new SortVisualizer(A, frameWidth);
		frame.add(sv);
		
		JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0, 70, 0);
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(5);
		slider.setPaintTicks(true);
		frame.getContentPane().add(slider, BorderLayout.SOUTH);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				sv.setSpeed(slider.getValue()+1);
				frame.repaint();		
			}
		});
		
		frame.setVisible(true);
		
		String method;
		do {
			method = (String)(JOptionPane.showInputDialog(null, "RADIX SORT METHOD", "choose a radix sort method", JOptionPane.QUESTION_MESSAGE, null, new String[] {"LSD", "MSD"}, null));
		}while(method == null);
		if(method.equals("LSD"))
			sv.radixSortLSD();
		else
			sv.radixSortMSD();
	}
}
