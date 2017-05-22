package com.core;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;



public class Start {
	
	static Timer time;
	public static void main(String[] args) {
		
		Frame frame = new MyFrame().getFrame();
		MyCanvas canvas = new MyCanvas();
//		canvas.createBufferStrategy(1024);
		frame.add(canvas);
		
		Thread th = new Thread(){
			@Override
			public void run() {
				while(true){
					synchronized (Point.curr_dir) {
						canvas.repaint();
					}
					try {
						Thread.currentThread().sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		
		KeyAdapter kapt = new KeyAdapter() {
			String dir;
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()){
				case KeyEvent.VK_UP:
					dir="up";
					break;
				case KeyEvent.VK_DOWN:
					dir="down";
					break;
				case KeyEvent.VK_LEFT:
					dir="left";
					break;
				case KeyEvent.VK_RIGHT:
					dir="right";
					break;
				}
				if(!dir.equals(Point.curr_dir)){
					synchronized (Point.curr_dir) {

						Point.getPoints().setcurr(dir);
						System.out.println("按下键盘"+Point.curr_dir);
						
//						Point.getPoints().Updateparrays(dir);
					}
				}
			}
		};
		
		new Thread(){
			public void run() {
				while(true){
					try {
						Point.getPoints().Updateparrays(Point.curr_dir);
						sleep(Basic.speed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
		
		frame.addKeyListener(kapt);
		canvas.addKeyListener(kapt);
		
		th.start();
	}

}
class MyCanvas extends Canvas{
	static boolean flag=true;
	Random random = new Random();
	static int apple_x;
	static int apple_y;
	@Override
	public void paint(Graphics g) {
		Graphics _this_g = g;
		Image createImage = createImage(520, 530);
		Graphics graphics = createImage.getGraphics();
		g=graphics;
		/*g.setColor(Color.red);
		for(int i=1;i<=Basic.gs;i++){
				g.drawLine(0+Basic.p, Basic.mgdx*i+Basic.p, Basic.mgdx*Basic.gs+Basic.p, Basic.mgdx*i+Basic.p);
				g.drawLine(Basic.mgdx*i+Basic.p, 0+Basic.p,Basic.mgdx*i+Basic.p,Basic.mgdx*Basic.gs+Basic.p );
		}*/
		g.setColor(Color.blue);
		g.drawLine(Basic.p, Basic.p, Basic.mgdx*Basic.gs+Basic.p, Basic.p);
		g.drawLine(Basic.p, Basic.p, Basic.p, Basic.mgdx*Basic.gs+Basic.p);
		g.drawLine(0+Basic.p,Basic.mgdx*Basic.gs+Basic.p, Basic.mgdx*Basic.gs+Basic.p,Basic.mgdx*Basic.gs+Basic.p);
		g.drawLine(Basic.mgdx*Basic.gs+Basic.p,0+Basic.p, Basic.mgdx*Basic.gs+Basic.p,Basic.mgdx*Basic.gs+Basic.p);
		
		g.setColor(Color.GREEN);
//		System.out.println("画画"+Point.curr_dir);
		  ArrayList<int[]> arrayList = Point.getPoints().get();
		  Iterator<int[]> iterator = arrayList.iterator();

//		  System.out.println(arrayList.size());
		  while(iterator.hasNext()){
			  int[] ar = iterator.next();
			  g.fillRect(ar[0], ar[1], Basic.mgdx, Basic.mgdx);
		  }
		
		  if(flag){
			apple_x=random.nextInt(50);
			apple_y = random.nextInt(50);
			System.out.println(apple_x+"   "+apple_y);
			  flag=false;
		  }
		  g.setColor(Color.black);
		  g.fillRect(apple_x*Basic.mgdx+Basic.p, apple_y*Basic.mgdx+Basic.p, Basic.mgdx, Basic.mgdx);
		  
		  
		  
		_this_g.drawImage(createImage, 0, 0, null);
		
		
		
	}
}
class Point {
	private int[] end;
	public static String curr_dir="down";
	private int speed = 1;
	private static Point points=null;
	private ArrayList<int[]> pointarray=new ArrayList<int[]>();
	private Point(){
		int[] ar1 = {20*Basic.mgdx+Basic.p,21*Basic.mgdx+Basic.p};
		int[] ar2 = {20*Basic.mgdx+Basic.p,22*Basic.mgdx+Basic.p};
		int[] ar3 = {20*Basic.mgdx+Basic.p,23*Basic.mgdx+Basic.p};
		int[] ar4 = {20*Basic.mgdx+Basic.p,24*Basic.mgdx+Basic.p};
		int[] ar5 = {20*Basic.mgdx+Basic.p,25*Basic.mgdx+Basic.p};
		int[] ar6 = {20*Basic.mgdx+Basic.p,26*Basic.mgdx+Basic.p};
		pointarray.add(ar6);
		pointarray.add(ar5);
		pointarray.add(ar4);
		pointarray.add(ar3);
		pointarray.add(ar2);
		pointarray.add(ar1);
	}
	
	public static Point getPoints(){
		if(points==null){
			points = new Point();
		}
		return points;
	}
	public ArrayList<int[]> get(){
		return pointarray;
	}
	public void setcurr(String dir){
		if(isoppose(dir)){
			int[] _ch_ar = null;
			
			for(int i=0;i<pointarray.size()/2;i++){
				_ch_ar = pointarray.get(i);
				pointarray.set(i, pointarray.get(pointarray.size()-1-i));
				pointarray.set(pointarray.size()-1-i,_ch_ar);
			}
			System.out.println("fan");
		}
		curr_dir=dir;
	}
	public ArrayList<int[]> Updateparrays(String key_dir){
		
		for(int i=pointarray.size()-1;i>0;i--){
			 pointarray.set(i,pointarray.get(i-1));
		}
		
		if(key_dir!=null){
				curr_dir=key_dir;
				changeDir(curr_dir);
		}else{
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return pointarray;
	}
	
	private boolean isoppose(String dir){
		switch(dir){
		case "up":
			dir="down";
			break;
		case "down":
			dir="up";
			break;
		case "left":
			dir="right";
			break;
		case "right":
			dir="left";
			break;
		}
		if(dir.equals(curr_dir)){
			return true;
		}else{
			return false;
		}
	}
	private void changeDir(String dir){
		int[] nw=new int[2];
		end = pointarray.get(pointarray.size()-1);
		switch (dir) {
		case "down":
			nw[0] = pointarray.get(0)[0];
			nw[1] = pointarray.get(0)[1]+speed*Basic.mgdx;
			pointarray.set(0, nw);
			break;
		case "up":
			nw[0] = pointarray.get(0)[0];
			nw[1] = pointarray.get(0)[1]-speed*Basic.mgdx;
			pointarray.set(0, nw);
			break;
		case "left":
			nw[0] = pointarray.get(0)[0]-speed*Basic.mgdx;
			nw[1] = pointarray.get(0)[1];
			pointarray.set(0, nw);
			break;
		case "right":
			nw[0] = pointarray.get(0)[0]+speed*Basic.mgdx;
			nw[1] = pointarray.get(0)[1];
			pointarray.set(0, nw);
			break;

		default:
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		//判断是否吃苹果和是否出界
		if(!is_go()){
			System.exit(0);
		}
		
	}
	private boolean is_go(){
		int[] head = pointarray.get(0);
		int x = head[0];
		int y = head[1];
		
//		Iterator<int[]> iterator = pointarray.iterator();
		
		
		if(x>=Basic.p&&x<=Basic.mgdx*49+Basic.p){
			if(y>=Basic.p&&y<=Basic.mgdx*49+Basic.p){
				
				if(x==MyCanvas.apple_x*Basic.mgdx+Basic.p&&y==MyCanvas.apple_y*Basic.mgdx+Basic.p){
					MyCanvas.flag=true;
					Basic.apple_sum++;
					if(Basic.apple_sum>0){
						//增加速度和节数
						add();
					}
					System.out.println("eat");
					
				}
				
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
		
	}
	
	private void add(){
		pointarray.add(end);
//		Basic.speed-=200;
//		if(Basic.speed<400){
//			Basic.speed=400;
//		}
	}
}


class MyFrame extends Frame{
	public Frame getFrame(){
		Frame frame = new Frame("贪吃蛇");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.setSize(550, 550);
		frame.setResizable(false);
		return frame;
	}
}

