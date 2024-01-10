package com.game.mine;

import java.awt.Color;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.BorderFactory;

public class Logic
{
	/** 游戏面板 */
	private Panel panel;

	public Logic(Panel gamePanel)
	{
		this.panel = gamePanel;
	}
        

	public void setNumberTip(int number,int flag)//显示时间设置，number为要设置的时间或地雷数，flag为0时为地雷数，1为时间
	{
		if(number <0){number = 0;}
		
		//将数字转换为3位长字符串，前面不足补零
		String tip = "000" + Integer.toString(number);
		tip = tip.substring(tip.length() - 3,tip.length());
		if(flag == 0)      //显示剩余地雷数
		{
			if(number >= this.panel.mineNum){number = this.panel.mineNum;}
		    for(int i=0;i<3;i++)
		    {
		        this.panel.labelMineTip[i].setIcon(this.panel.imageIconNumberTip[Integer.parseInt(tip.substring(i,i+1))]);
		    }
		}
		else if(flag == 1)     //显示游戏时间
		{
			if(number > 999){number = 999;}
		    for(int i=0;i<3;i++)
		    {
		        this.panel.labelTimeTip[i].setIcon(this.panel.imageIconNumberTip[Integer.parseInt(tip.substring(i,i+1))]);
		    }
		}

	}


	
	public void resetAll()//重置所有的信息
	{
		this.panel.falts=0;
		for(int row=0;row<this.panel.rows;row++)
		{
		    for(int column=0;column<this.panel.columns;column++)
		    {
		    	this.panel.buttonMine[row][column].setIcon(this.panel.imageIconSquare);
		    }
		}
		//重置地雷信息
		for(int row=0;row<this.panel.rows;row++)
		{
		    for(int column=0;column<this.panel.columns;column++)
		    {
		    	this.panel.mapMine[row][column].put("number",0);	//0个雷
		    	this.panel.mapMine[row][column].put("flag",0);		//未打开
		    }
		}
	}
	
	public void randomMine()//随机生成怪物
	{
		//随机生成地雷
		for(int i=0;i<this.panel.mineNum;)
		{
			int row = (int)(Math.random() * this.panel.rows);
			int column = (int)(Math.random() * this.panel.columns);
			
			//判断该位置是否已经有雷
			if(this.panel.mapMine[row][column].get("number") ==0)
			{
				this.panel.mapMine[row][column].put("number",-1);
				i++;
			}
		}
		
		for(int row=0;row<this.panel.rows;row++)//记录周围的雷数
		{
		    for(int column=0;column<this.panel.columns;column++)
			{
				if(this.panel.mapMine[row][column].get("number") <0)
				{
					this.panel.mapMine[row][column].put("number",this.countMineAround(row,column));
				}
				else {
					this.panel.mapMine[row][column].put("number",this.countWhiteAround(row,column));
				}
			}
		}
	}	
	

	private int countMineAround(int _row,int _column)//计算周围地雷数
	{
		int count = 0;
		for(int row=_row-1;row<=_row+1;row++)
		{
			if(row < 0 || row >= this.panel.rows)
			{
				continue;
			}	//行出界
			for(int column=_column-1;column<=_column+1;column++)
			{
				if(column < 0 || column >= this.panel.columns)//列出界
				{
					continue;
				}	
				if(row == _row && column == _column)//除去自己
				{
					continue;
				}	
				if(this.panel.mapMine[row][column].get("number") <= -1)
				{
					count++;
				}
			}
		}
		if(count==0)
		{
			count=9;
		}
		return -count;
	}
	private int countWhiteAround(int rows,int columns)//计算周围白格数
	{
		int count =0;
		for(int row =rows-1;row<=rows+1;row++)
		{
			if(row < 0 || row >= this.panel.rows)
			{
				continue;
			}
			for(int column=columns-1;column<=columns+1;column++)
			{
				if(column < 0 || column >= this.panel.columns){continue;}	//列出边界了
				if(row == rows && column == columns){continue;}	//自身不计算在内
				if(this.panel.mapMine[row][column].get("number") >=0){count++;}
			}
		}
		return count;
	}
	

	public void showMine()//显示所有雷
	{
		for(int row=0;row<this.panel.rows;row++)
		{
		    for(int column=0;column<this.panel.columns;column++)
		    {
		    	if(this.panel.mapMine[row][column].get("number") <= -1)
		    	{
		    		this.panel.buttonMine[row][column].setIcon(this.panel.imageIconMine);
		    		this.panel.buttonMine[row][column].setPressedIcon(this.panel.imageIconMine);
		    		this.panel.mapMine[row][column].put("flag",1);
		    	}
		    }
		}
	}
	
	
	public void openCell(int row,int column)
	{
    	
		//如果打开或者标红旗则跳过
		if(this.panel.mapMine[row][column].get("flag") == 1 || this.panel.mapMine[row][column].get("flag") == 2){return;}
		
		if(this.panel.mapMine[row][column].get("number") <= -1)			//踩到地雷了
		{
			this.panel.falts++;
			this.panel.mineTip--;
			this.setNumberTip(this.panel.mineTip,0);
			this.panel.mapMine[row][column].put("flag",1);
			if(this.panel.falts==5)
			{
				this.showMine();//显示所以雷
			}
			
    	    this.panel.buttonMine[row][column].setIcon(this.panel.imageBlackNumber[-this.panel.mapMine[row][column].get("number")]);
    		this.panel.buttonMine[row][column].setPressedIcon(this.panel.imageBlackNumber[-this.panel.mapMine[row][column].get("number")]);
			
    		
    		if(this.panel.falts==5)
    		{
    		
    			this.panel.isGameOver = true;
    			this.panel.isStart = false;
    			return;
    		}
    		
		}
		
		else	//踩到数字处
		{
			this.panel.mapMine[row][column].put("flag",1);
			this.panel.buttonMine[row][column].setIcon(this.panel.imageIconNumber[this.panel.mapMine[row][column].get("number")]);
    		this.panel.buttonMine[row][column].setPressedIcon(this.panel.imageIconNumber[this.panel.mapMine[row][column].get("number")]);
			
		}
		
		//判断游戏是否结束
		if(this.GameOver())
		{
			this.panel.isGameOver = true;
    		this.panel.isStart = false;
			this.panel.timer.stop();
		}
		
	}

	public boolean GameOver()//结束游戏
	{
		//判断未被打开的方格数与雷数是否相等
		int count = 0;
		for(int row=0;row<this.panel.rows;row++)
		{
		    for(int column=0;column<this.panel.columns;column++)
		    {
	    		if(this.panel.mapMine[row][column].get("flag") != 1){count++;}
		    }
		}
		if(count -this.panel.falts== this.panel.mineNum)
		{
			this.panel.isGameOver = true;
			JOptionPane.showMessageDialog(null, this.panel.story[3], "一次难忘的旅行", JOptionPane.INFORMATION_MESSAGE,this.panel.storyEnd);
			return true;
		}
		
		return false;
	}
}
