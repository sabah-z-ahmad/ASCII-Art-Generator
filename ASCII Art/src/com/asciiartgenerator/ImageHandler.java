package com.asciiartgenerator;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHandler {
	private int w, h;			//width and height
	private int[][] pixels;		//pixel matrix
	
	public ImageHandler(File file, int maxW, int maxH)	{
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.w = img.getWidth();
		this.h = img.getHeight();
		
		if(this.w > maxW || this.h > maxH)	{
			System.out.println("Resizing image...");
			int dim[] = getScaledDimensions(img.getWidth(),img.getHeight(),maxW,maxH);
			img = resize(img, dim[0], dim[1]);
			this.w = img.getWidth();
			this.h = img.getHeight();
		}		
		
		this.pixels = new int[h][w];
		
		for(int y=0; y<h; y++) {
			for(int x=0; x<w; x++) {
				pixels[y][x] = img.getRGB(x,y);
			}
		}
		
		img.flush();
	}
	
	
	private BufferedImage resize(BufferedImage img, int newW, int newH)	{
		Image temp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(temp, 0, 0, null);
		g2d.dispose();     
		return resized;
	}
	
	
	//Modes are "Average", "Lightness", and "Luminosity"
	public void toGrayScale(String mode)	{
		int R,G,B;
			
		if(mode.equals("Average"))	{
			for(int y=0; y<h; y++) {
				for(int x=0; x<w; x++) {
					int pixel = pixels[y][x];
					R = (pixel >> 16) & 0xFF;
					G = (pixel >> 8) & 0xFF;
					B = pixel & 0xFF;
					pixels[y][x] = (R + G + B) / 3;
				}
			}
		}			
		else if(mode.equals("Lightness"))	{
			for(int y=0; y<h; y++) {
				for(int x=0; x<w; x++) {
					int pixel = pixels[y][x];
					R = (pixel >> 16) & 0xFF;
					G = (pixel >> 8) & 0xFF;
					B = pixel & 0xFF;
					pixels[y][x] = (max(R, G, B) + min(R, G, B)) / 2;
				}
			}			
		}			
		else if(mode.equals("Luminosity"))	{
			for(int y=0; y<h; y++) {
				for(int x=0; x<w; x++) {
					int pixel = pixels[y][x];
					R = (pixel >> 16) & 0xFF;
					G = (pixel >> 8) & 0xFF;
					B = pixel & 0xFF;
					pixels[y][x] = (int)((0.21f*R) + (0.72f*G) + (0.07f*B));
				}
			}
		}
	}
	
	private int max(int a, int b, int c)	{
		if(a >= b && a >= c)
			return a;
		else if(b >= a && b >= c)
			return b;
		else
			return c;
	}
	
	private int min(int a, int b, int c)	{
		if(a <= b && a <= c)
			return a;
		else if(b <= a && b <= c)
			return b;
		else
			return c;
	}
	
	
	private int[] getScaledDimensions(int origW, int origH, int maxW, int maxH)	{
		int[] dim = new int[2];	//[w,h]
		
		float newW = origW;
		float newH = origH;
		float scale;
		
		if(origW > maxW) {
			scale = (float)maxW / (float)origW;
			newW = (origW*scale);
			newH = (origH*scale);
		}
		if(newH > maxH) {
			scale = (float)maxH / newH;
			newW = (newW*scale);
			newH = (newH*scale);
		}

		dim[0] = (int)newW;
		dim[1] = (int)newH;
		
		return dim;
	}

	
	public int getW() {
		return w;
	}


	public int getH() {
		return h;
	}


	public int[][] getPixels() {
		return pixels;
	}



}
