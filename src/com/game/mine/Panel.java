package com.game.mine;

import java.util.Map;
import java.util.HashMap;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class Panel extends JPanel implements MouseListener,ActionListener
{
	
	 int falts=0;
	 int process=0;
	 String []story= new String[4];
	 int[] storyFalts=new int[4];
	private Logic logic;
	final int EASY = 1;
	final int NORMAL = 2;
	final int HARD = 3;
	private int oldLevel = -1;//游戏难度
	private int newLevel = 0;
	int rows;
	int columns;


	final int gridSize = 20;//网格尺寸
	private int gridsWidth;//网格宽度
	private int gridsHeight;//网格高度

	int mineNum;


	private JPanel panelTip = new JPanel();//提示区
	private JPanel panelMine = new JPanel();//地雷区
	private JButton buttonFace = new JButton();//笑脸按钮
	JLabel labelMineTip[] = new JLabel[3];//地雷提示三行标签
	JLabel labelTimeTip[] = new JLabel[3];//时间提示三行
	JButton[][] buttonMine = new JButton[16][30];//总地雷分布按钮
        
	/**
	 * 地雷信息数组<br>
	 * number->地雷数：-1-地雷，0到8-周围地雷数<br>
	 * flag->地雷状态：0-未打开，1-已打开，2-插小旗，3-插问号<br>
	 */
	Map<String,Integer>[][] mapMine = new Map[16][30];


	private ImageIcon[] imageIconFace = new ImageIcon[2];//笑脸
	ImageIcon[] imageIconNumberTip = new ImageIcon[10];//时钟数字
	ImageIcon[] imageIconNumber = new ImageIcon[9];//白格数字
	ImageIcon imageIconSquare = new ImageIcon();//方格
	ImageIcon imageIconFlag = new ImageIcon();//红旗
	ImageIcon imageIconQuestion = new ImageIcon();//问号
	ImageIcon imageIconMine = new ImageIcon();//地雷照片
	ImageIcon[] imageBlackNumber = new ImageIcon[10];//地雷数字图片
	ImageIcon storyBegin = new ImageIcon();//故事开始图片
	ImageIcon storyTrouble = new ImageIcon();//故事遭受5次怪图片
	ImageIcon storyProcess = new ImageIcon();//故事第一次遭到怪图片
	ImageIcon storyEnd = new ImageIcon();//故事结束图片
	
        

	int timeTip = 0;//时间提醒
	int mineTip = 0;//地雷数目


	Timer timer = new Timer(1000,this);//计时器
	

	boolean isStart = false;//游戏是否开始
	boolean isGameOver = true;//游戏是否结束

	public void readFile() throws IOException{//C:/java/Mineweeper/res/resource/mine/story.txt
		Path path=Paths.get("res/resource/mine/story.txt");
		BufferedReader reader=Files.newBufferedReader(path);
		
		for(int i=0;i<4;i++)
		{
			String []a=new String[3];
			for(int j=0;j<3;j++)
			{
				 a[j]=reader.readLine();
			}
		this.story[i]=a[0]+"\n"+a[1]+"\n"+a[2];
		}
		reader.close();
	}
	public Panel() throws IOException
	{
		this.readFile();
		//主面板初始化
		this.setBackground(Color.RED);
		this.setLayout(null); //自由

		//提示区面板初始化
		this.panelTip.setBackground(Color.BLUE);
	
		this.panelTip.setLayout(null);
		this.add(this.panelTip);
		
		//地雷区面板初始化
		
		this.add(this.panelMine);
                
		//地雷数提示标签初始化
		for(int i=0;i<this.labelMineTip.length;i++)
		{
			this.labelMineTip[i] = new JLabel();
		}

		//笑脸按钮初始化
		this.buttonFace.addActionListener(this);     //事件监听只能添加一次，若多次添加会造成监听事件重复执行。
                
		//时间提示标签初始化
		for(int i=0;i<this.labelTimeTip.length;i++)
		{
			this.labelTimeTip[i] = new JLabel();
		}
                
		//地雷按钮初始化
		for(int row=0;row<this.buttonMine.length;row++)
		{
		    for(int column=0;column<this.buttonMine[0].length;column++)
		    {
		        this.buttonMine[row][column] = new JButton();
		        this.buttonMine[row][column].addMouseListener(this);
		        this.buttonMine[row][column].setName(row+"_"+column);         //用名字来区分行列坐标
		    }
		}
                
		//地雷信息初始化
		for(int row=0;row<this.mapMine.length;row++)
		{
		    for(int column=0;column<this.mapMine[0].length;column++)
		    {
		    	this.mapMine[row][column] = new HashMap<String,Integer>();
		    	this.mapMine[row][column].put("number",0);	//0个雷
		    	this.mapMine[row][column].put("flag",0);	//未打开
		    }
		}

		//加载图片
		this.loadImage();

		//游戏逻辑
		this.logic = new Logic(this);

		//设置游戏难度并调整各组件大小
		this.setLevel(this.EASY);
		
	}
	

	private void loadImage()//加载图片
	{
		try
		{
			//笑脸图片
			this.imageIconFace[0] = new ImageIcon(this.getClass().getResource("/resource/mine/smile.gif"));
			this.imageIconFace[1] = new ImageIcon(this.getClass().getResource("/resource/mine/ooo.gif"));
			//数字提示图片
			for(int i=0;i<10;i++)
			{
			    this.imageIconNumberTip[i] = new ImageIcon(this.getClass().getResource("/resource/mine/c"+i+".gif"));
			}
			//数字图片
			for(int i=0;i<9;i++)
			{
			    this.imageIconNumber[i] = new ImageIcon(this.getClass().getResource("/resource/mine/"+i+".gif"));
			}
			//方格图片
			this.imageIconSquare = new ImageIcon(this.getClass().getResource("/resource/mine/cell.gif"));
			//红旗图片
			this.imageIconFlag = new ImageIcon(this.getClass().getResource("/resource/mine/flag.gif"));
			//问号图片
			this.imageIconQuestion = new ImageIcon(this.getClass().getResource("/resource/mine/question.gif"));
			//地雷图片
			this.imageIconMine = new ImageIcon(this.getClass().getResource("/resource/mine/mine.gif"));
			//地雷数字图片
			for(int i=1;i<10;i++)
			{
			    this.imageBlackNumber[i] = new ImageIcon(this.getClass().getResource("/resource/mine/w"+i+".gif"));
			}
			this.storyBegin=new ImageIcon(this.getClass().getResource("/resource/mine/begin.jpg"));
			this.storyProcess=new ImageIcon(this.getClass().getResource("/resource/mine/Process.jpg"));
			this.storyTrouble=new ImageIcon(this.getClass().getResource("/resource/mine/touble.jpg"));
			this.storyEnd=new ImageIcon(this.getClass().getResource("/resource/mine/end.jpg"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initGame()
	{
		//重新设置参数
		this.mineTip = this.mineNum;
		this.logic.setNumberTip(this.mineNum,0);
		this.timeTip = 0;
		this.logic.setNumberTip(0,1);
		this.isGameOver = false;
		this.isStart = false;
		this.timer.stop();
		for(int i=0;i<4;i++)
		{
			this.storyFalts[i]=0;
		}
		//重置所有的信息
		this.logic.resetAll();
		//随机生雷并记录周围地雷数
		this.logic.randomMine();
	}
	
	//设计难度
	public void setLevel(int levels)
	{
		//判断难度是否有变化
		if(this.oldLevel == levels)
		{
			return;
		}
		//记录难度
		this.oldLevel = levels;
		this.newLevel = levels;
		//开始调试
		if(this.newLevel == this.EASY)
		{
			this.rows = 10;
			this.columns = 10;
			this.mineNum = 7;
		}
		else if(this.newLevel == this.NORMAL)
		{
			this.rows = 15;
			this.columns = 16;
			this.mineNum = 80;
		}
		else if(this.newLevel == this.HARD)
		{
			this.rows = 15;
			this.columns = 30;
			this.mineNum = 200;
		}
		this.gridsWidth = this.gridSize * (this.columns);
		this.gridsHeight = this.gridSize * (this.rows);
		//重新设置主面板尺寸
		this.setBounds(0,0,this.gridsWidth + 20,this.gridsHeight + 60);
		//重新设置提示区面板尺寸并清除上面的组件
		this.panelTip.setBounds(7,7,this.getWidth() - 17,36);
		//this.panelTip.removeAll();
		//重新设置地雷区面板尺寸并清除上面的组件
		this.panelMine.setBounds(8,50,this.gridsWidth + 5,this.gridsHeight + 5);//左肩宽，上肩高，内宽，内高
		this.panelMine.removeAll();
		//重新设置笑脸按钮
		this.buttonFace.setBounds((this.panelTip.getX() + this.panelTip.getWidth() - 30)/2,5,27,27);//离左肩距离，离地距离，宽，长
		this.buttonFace.setIcon(this.imageIconFace[0]);
		this.buttonFace.setPressedIcon(this.imageIconFace[1]);
		this.panelTip.add(this.buttonFace);
		//重新设置地雷数提示标签并显示总地雷数
		this.labelMineTip[0].setBounds(7,8,14,24);
		this.labelMineTip[1].setBounds(20,8,14,24);
		this.labelMineTip[2].setBounds(33,8,14,24);
		this.logic.setNumberTip(this.mineNum,0);
		this.panelTip.add(this.labelMineTip[0]);
		this.panelTip.add(this.labelMineTip[1]);
		this.panelTip.add(this.labelMineTip[2]);
		//重新设置时间提示标签并显示时间数
		this.labelTimeTip[0].setBounds(this.panelTip.getX() + this.panelTip.getWidth() - 53,7,14,24);
		this.labelTimeTip[1].setBounds(this.panelTip.getX() + this.panelTip.getWidth() - 40,7,14,24);
		this.labelTimeTip[2].setBounds(this.panelTip.getX() + this.panelTip.getWidth() - 27,7,14,24);
		this.logic.setNumberTip(0,1);
		this.panelTip.add(this.labelTimeTip[0]);
		this.panelTip.add(this.labelTimeTip[1]);
		this.panelTip.add(this.labelTimeTip[2]);
		//重新设置地雷区按钮
		this.panelMine.setLayout(null);
		for(int row=0;row<this.rows;row++)
		{
		    for(int column=0;column<this.columns;column++)
		    {
		    	this.buttonMine[row][column].setIcon(this.imageIconSquare);
		    	this.buttonMine[row][column].setBounds(column * this.gridSize + 2,row * this.gridSize + 2,this.gridSize,this.gridSize);
		    	this.panelMine.add(this.buttonMine[row][column]);
		    }
		}
		//初始化游戏
		this.initGame();
	}
	
	
	public void newGame()
	{
		this.initGame();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == this.timer)		//定时器
		{
			this.timeTip++;
			this.logic.setNumberTip(this.timeTip,1);
		}
		else if(e.getSource() == this.buttonFace)
		{
			this.newGame();
		}
	}
	public void mousePressed(MouseEvent e)//鼠标按下开始
	{
		//游戏结束，退出
		if(this.isGameOver) {return;}
		
		//游戏未开始，开始游戏，不退出
	    if(this.isStart == false)
		{
			this.isStart = true;
			this.timer.start();
		}
		
        

	}

	public void mouseReleased(MouseEvent e) //鼠标释放监听
	{
		//游戏结束，退出
		if(this.isGameOver) {return;}
		
		//游戏未开始，开始游戏，不退出
	    if(this.isStart == false)
		{
			this.isStart = true;
			this.timer.start();
		}
		
        Object obj = e.getSource();
        
        //鼠标左键点击
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(obj instanceof JButton)
            {
            	JButton jbMine = (JButton)obj;
            	String location[] = jbMine.getName().split("_");
            	int row = Integer.parseInt(location[0]);
            	int column = Integer.parseInt(location[1]);
            	this.logic.openCell(row,column);	//打开该格子
            	if(this.falts==0&&this.storyFalts[0]==0)
            	{
            		this.storyFalts[0]=1;
            		JOptionPane.showMessageDialog(null, this.story[0], "一切的开始还是结束？", JOptionPane.INFORMATION_MESSAGE,this.storyBegin);	
            	}
            	else if(this.falts==1&&this.storyFalts[1]==0) {
            		this.storyFalts[1]=1;
            		JOptionPane.showMessageDialog(null, this.story[1], "遭遇敌人", JOptionPane.INFORMATION_MESSAGE,this.storyProcess);
            	}
            	else if(this.falts==5) {
            		JOptionPane.showMessageDialog(null, this.story[2], "旅行结束", JOptionPane.INFORMATION_MESSAGE,this.storyTrouble);
            	}
            	
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON3)    //鼠标右键点击
        {
            if(obj instanceof JButton)
            {
	        	JButton jbMine = (JButton)obj;
	        	String location[] = jbMine.getName().split("_");
	        	int row = Integer.parseInt(location[0]);
	        	int column = Integer.parseInt(location[1]);
	        	if(this.mapMine[row][column].get("flag") == 0)		//处于未打开状态插红旗
	        	{
	        		this.mapMine[row][column].put("flag",2);
	        		this.buttonMine[row][column].setIcon(this.imageIconFlag);
	        		this.buttonMine[row][column].setPressedIcon(this.imageIconFlag);
	        		//更改地雷提示数字
	        		this.mineTip--;
	        		this.logic.setNumberTip(this.mineTip,0);
	        	}
	        	else if(this.mapMine[row][column].get("flag") == 2)	//处于插红旗状态变问号
	        	{
	        		this.mapMine[row][column].put("flag",3);
	        		this.buttonMine[row][column].setIcon(this.imageIconQuestion);
	        		this.buttonMine[row][column].setPressedIcon(this.imageIconQuestion);
	        		//更改地雷提示数字
	        		this.mineTip++;
	        		this.logic.setNumberTip(this.mineTip,0);
	        	}
	        	else if(this.mapMine[row][column].get("flag") == 3)	//处于问号状态变未打开
	        	{
	        		this.mapMine[row][column].put("flag",0);
	        		this.buttonMine[row][column].setIcon(this.imageIconSquare);
	        		
	        	}
            }
        }
		
	}

	@Override
	public void mouseClicked(MouseEvent e){}
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
