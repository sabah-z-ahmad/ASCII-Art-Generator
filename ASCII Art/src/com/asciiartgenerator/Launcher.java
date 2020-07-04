package com.asciiartgenerator;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Launcher extends JPanel implements	ActionListener, ChangeListener, ItemListener	{
	protected JLabel labelPreviewTitle;
	protected JLabel labelPreview;
	protected JLabel labelGrayTitle;
	protected JLabel labelSizeTitle;
	protected JButton buttonOpen;
	protected JButton buttonGenerate;
	protected JButton buttonSave;
	protected JRadioButton radioAverage;
	protected JRadioButton radioLightness;
	protected JRadioButton radioLuminosity;
	protected ButtonGroup radioGroup;
	protected JTextArea textArea;
	protected JSlider sliderOutput;
	
	private Font font;
	
	private String pathInput;
	private String pathOutput;
	
	private ImageHandler grayImage;
	private BufferedImage preview; 
	private EmptyBorder previewBorder;
	
	private Converter converter;
	private File fileInput; 
	
	String grayMode;
	
	int imageWidth;
	int imageHeight;
	
	int maxOutputWidth;
	int maxOutputHeight;
	
	int maxPreviewWidth;
	int maxPreviewHeight;
	
	public Launcher() {
		super(new GridBagLayout());
		
		font = new Font("monospaced", Font.PLAIN, 6);
		grayMode = "Average";
		
		maxOutputWidth = 200;
		maxOutputHeight = 200;
		
		maxPreviewWidth = 100;
		maxPreviewHeight = 100;

		labelPreview = new JLabel("", SwingConstants.CENTER);
		labelPreview.setBorder(new EmptyBorder(0,50,100,50));
		
		labelPreviewTitle = new JLabel("Input Preview", SwingConstants.CENTER);
		labelGrayTitle = new JLabel("<html><div style='text-align: center;'>Grayscale <br>Conversion Mode</html>", SwingConstants.CENTER);
		labelSizeTitle = new JLabel("Output Size", SwingConstants.CENTER);
		
		buttonOpen = new JButton("Open");
		buttonOpen.addActionListener(this);
		
		buttonGenerate = new JButton("Generate");
		buttonGenerate.addActionListener(this);
		
		buttonSave = new JButton("Save As");
		buttonSave.addActionListener(this);
		
		radioAverage = new JRadioButton();
		radioLightness = new JRadioButton();
		radioLuminosity = new JRadioButton();
		
		radioAverage.setText("Average");
		radioLightness.setText("Lightness");
		radioLuminosity.setText("Luminosity");
		
		radioAverage.setSelected(true);
		
		radioAverage.addItemListener(this);
		radioLightness.addItemListener(this);
		radioLuminosity.addItemListener(this);
		
		radioGroup = new ButtonGroup();
		
		sliderOutput = new JSlider();
		sliderOutput.addChangeListener(this);
				
		textArea = new JTextArea(100,300);
		textArea.setEditable(false);
		textArea.setFont(font);
		JScrollPane scrollPane = new JScrollPane(textArea);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.ipady = 20;
		gbc.insets = new Insets(10,0,0,0);
		add(buttonOpen, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.ipady = 0;
		gbc.insets = new Insets(10,0,0,0);
		add(labelPreviewTitle, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.insets = new Insets(0,0,0,0);
		add(labelPreview, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		add(labelGrayTitle, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(radioAverage, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 5;
		add(radioLightness, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 6;
		add(radioLuminosity, gbc);
		
		radioGroup.add(radioAverage);
		radioGroup.add(radioLightness);
		radioGroup.add(radioLuminosity);
		
		
		gbc.gridx = 1;
		gbc.gridy = 7;
		add(Box.createHorizontalStrut(200), gbc);
		
		
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.insets = new Insets(50,0,0,0);
		add(labelSizeTitle, gbc);
		
		
		gbc.gridx = 1;
		gbc.gridy = 9;
		gbc.insets = new Insets(10,0,0,0);
		add(sliderOutput, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 10;
		gbc.ipady = 20;
		gbc.insets = new Insets(50,0,0,0);
		add(buttonGenerate, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 11;
		gbc.anchor = GridBagConstraints.PAGE_END;
		add(buttonSave, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 12;
		gbc.ipady = 0;
		gbc.insets = new Insets(0,0,0,0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		add(scrollPane, gbc);
		
	}
	
	private EmptyBorder getPreviewBorder(int width, int height)	{
		int horizontal = 20;
		int vertical = 20;
		if(width < maxPreviewWidth || height < maxPreviewHeight)	{
			horizontal = ((maxPreviewWidth - width) / 2) + 20; 
			vertical = ((maxPreviewHeight - height) / 2) + 20;
		}
		
		return new EmptyBorder(vertical,horizontal,vertical,horizontal);
	}
	
	private ImageIcon getIcon(BufferedImage image)	{
		int origW = image.getWidth();
		int origH = image.getHeight();
		float newW = origW;
		float newH = origH;
		float scale;
		
		if(origW > maxPreviewWidth) {
			scale = (float)maxPreviewWidth / (float)origW;
			newW = (origW*scale);
			newH = (origH*scale);
		}
		if(newH > maxPreviewHeight) {
			scale = (float)maxPreviewHeight / newH;
			newW = (newW*scale);
			newH = (newH*scale);
		}
					
		Image resized = image.getScaledInstance((int)newW, (int)newH, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resized); 
		return resizedIcon;
	}
	
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("ASCII Art Generator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new Launcher());
		
		frame.pack();
		frame.setVisible(true);		
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		String com = evt.getActionCommand();
		if(com.contentEquals("Open"))	{
			System.out.println("Open was pressed");
			
			//Create an object of the JFileChooser class
			JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			chooser.setDialogTitle("Select an image file");
			
			//Restrict the user to select files of all types
			chooser.setAcceptAllFileFilterUsed(false);
			
			//Only allow image ('jpg', 'jpeg', 'png') files
			FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only image files", "jpg", "jpeg", "png");
			chooser.addChoosableFileFilter(restrict);
			
			//Invoke the showOpenDialog function to show the open dialog
			int r = chooser.showOpenDialog(null);
			
			//If the user selects a file
			if(r == JFileChooser.APPROVE_OPTION)	{
				pathInput = chooser.getSelectedFile().getAbsolutePath();
				fileInput = new File(pathInput);
				
				try	{
					preview = ImageIO.read(fileInput);
				} catch(IOException e)	{
					e.printStackTrace();
				}
				
				imageWidth = preview.getWidth();
				imageHeight = preview.getHeight();
				
				float scale = (float)sliderOutput.getValue() / 100f;
				maxOutputWidth = (int)((float)imageWidth * scale);
				maxOutputHeight = (int)((float)imageHeight * scale);
				
				previewBorder = getPreviewBorder(imageWidth,imageHeight);
				labelPreview.setBorder(previewBorder);
				labelPreview.setIcon(getIcon(preview));
			}
			else
				System.out.println("The user cancelled the operation");

		}
		else if(com.contentEquals("Save As"))	{
			System.out.println("Save As was pressed");
			
			//Create an object of the JFileChooser class
			JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			chooser.setDialogTitle("Choose a filename");
			
			//Invoke the showSaveDialog function to show the save dialog
			int r = chooser.showSaveDialog(null);
			
			//If the user selects a file
			if(r == JFileChooser.APPROVE_OPTION)	{
				pathOutput = chooser.getSelectedFile().getAbsolutePath();
				converter.writeToFile(pathOutput);
			}
			else
				System.out.println("The user cancelled the operation");
			
			
		}
		else if(com.contentEquals("Generate"))	{
			System.out.println("Generate was pressed");
			
			grayImage = new ImageHandler(fileInput, maxOutputWidth, maxOutputHeight);
			grayImage.toGrayScale(grayMode);
			converter = new Converter(grayImage);
			converter.process();
			textArea.setText(null);
			textArea.append(converter.getAsciiString());
		}		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		float scale = (float)sliderOutput.getValue() / 100f;
		maxOutputWidth = (int)((float)imageWidth * scale);
		maxOutputHeight = (int)((float)imageHeight * scale);
	}	
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == radioAverage)	{
			if(e.getStateChange() == 1)	{
				grayMode = "Average";
				System.out.println(grayMode);
			}
		}
		if(e.getSource() == radioLightness)	{
			if(e.getStateChange() == 1)	{
				grayMode = "Lightness";
				System.out.println(grayMode);
			}
		}
		else if(e.getSource() == radioLuminosity)	{
			if(e.getStateChange() == 1)	{
				grayMode = "Luminosity";
				System.out.println(grayMode);
			}
		}
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run()	{
				createAndShowGUI();
			}
		});		
	}


	
}
