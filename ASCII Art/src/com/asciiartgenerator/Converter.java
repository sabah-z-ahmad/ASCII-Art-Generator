package com.asciiartgenerator;

import java.io.FileWriter;
import java.io.IOException;

public class Converter {
	private int w, h;
	private float scale;
	private char[][] ascii;
	private ImageHandler img = null;
    private char[] chars = {'$','@','B','%','8','&','W','M','#','*','o','a','h','k','b','d',
             				'p','q','w','m','Z','O','0','Q','L','C','J','U','Y','X','z','c',
             				'v','u','n','x','r','j','f','t','/','\\','|','(',')','1','{','}',
             				'[',']','?','-','_','+','~','<','>','i','!','l','I',';',':',',',
             				'"','^','`','\'','.',' '};
    
	public Converter(ImageHandler img) {
		this.img = img;
		this.w = img.getW() * 2;
		this.h = img.getH();
		this.ascii = new char[h][w];
		this.scale = (float)(chars.length - 1) / 255;
	}
	
	public void process()	{
		int shift = 0;
		for(int y=0; y<img.getH(); y++) {
			for(int x=0; x<img.getW(); x++) {
				ascii[y][shift] = pixelToChar(img.getPixels()[y][x]);
				shift += 1;
				ascii[y][shift] = pixelToChar(img.getPixels()[y][x]);
				shift += 1;
			}
			shift = 0;
		}
	}
	
	private char pixelToChar(int pixel)	{
		char c = chars[(int)(pixel * scale)];
		return c;
	}
	
	public void writeToFile(String pathOutput)	{
		try	{
			FileWriter writer = new FileWriter(pathOutput);
			for(int y=0; y<h; y++) {
				for(int x=0; x<w; x++) {
					writer.write(ascii[y][x]);
				}
				writer.write("\n");
			}
			writer.close();
		} catch(IOException e)	{
			e.printStackTrace();
		}
	}
	
	public char[][] getAscii()	{
		return ascii;
	}
	
	public String getAsciiString() {
		StringBuilder sb = new StringBuilder();
		for(int y=0; y<h; y++) {
			for(int x=0; x<w; x++) {
				sb.append(ascii[y][x]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
