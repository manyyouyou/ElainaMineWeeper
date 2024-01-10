package com.game.mine;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


public class Frame extends JFrame implements ActionListener
{


	/** 游戏面板 */
	private Panel panel;
	
	// 菜单控件
	JMenuItem JmEasy,JmNormal,JmHard;
        
	
	public Frame()//主窗口
	{
		try
		{
			this.setTitle("伊蕾娜扫雷");
			this.setLayout(null);
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击关闭程序
			//菜单
			JMenuBar jmb = new JMenuBar();
			JMenu jm = new JMenu("游戏");
			JMenuItem jmi_new = jm.add("  开局");
			
			jmi_new.addActionListener(this);
			jmi_new.setActionCommand("new");
			jm.addSeparator();//分割线
			
			this.JmEasy = jm.add("√ 简单");
			this.JmEasy.addActionListener(this);
			this.JmEasy.setActionCommand("easy");
			
			this.JmNormal = jm.add("  正常");
			this.JmNormal.addActionListener(this);
			this.JmNormal.setActionCommand("normal")
			;
			this.JmHard = jm.add("  地狱");
			this.JmHard.addActionListener(this);
			this.JmHard.setActionCommand("hard");
			jm.addSeparator();
			
			JMenuItem jmi_exit = jm.add("  退出");
			jmi_exit.addActionListener(this);
			jmi_exit.setActionCommand("exit");
			
			jmb.add(jm);
			JMenu jm_help = new JMenu("世界观");
			JMenuItem jmi_about = jm_help.add("关于");
			
			jmi_about.addActionListener(this);
			jmi_about.setActionCommand("about");
			jmb.add(jm_help);
			this.setJMenuBar(jmb);
			//面板
			this.panel = new Panel();
			this.add(this.panel);
			//显示
			this.panel.setLevel(this.panel.EASY);
			this.setSize(this.panel.getWidth() + 15,this.panel.getHeight() + 60);
			this.setLocationRelativeTo(null);   //居中
			this.setBounds(this.getX() - 63,this.getY() - 63,this.getWidth(),this.getHeight());     //因为初级、中级、高级窗口大小不一样，但都以左上角位置为固定点，向右向下拉伸，所以考虑左上角位置再向左上方多挪点。
			this.setVisible(true);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(this,"程序出现异常错误，即将退出"+e.toString(),"提示",JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	

	public void actionPerformed(ActionEvent e)//监听
	{
		String command = e.getActionCommand();
		if("new".equals(command))
		{
			this.panel.newGame();
		}
		else if("easy".equals(command))
		{
			this.JmEasy.setText("√ 简单");
			this.JmNormal.setText("  正常");
			this.JmHard.setText("  地狱");
			this.panel.setLevel(this.panel.EASY);
			this.setSize(this.panel.getWidth() + 15,this.panel.getHeight() + 60);
		}
		else if("normal".equals(command))
		{
			this.JmEasy.setText("  简单");
			this.JmNormal.setText("√ 正常");
			this.JmHard.setText("  地狱");
			this.panel.setLevel(this.panel.NORMAL);
			this.setSize(this.panel.getWidth() + 15,this.panel.getHeight() + 60);
		}
		else if("hard".equals(command))
		{
			this.JmEasy.setText("  简单");
			this.JmNormal.setText("  正常");
			this.JmHard.setText("√ 地狱");
			this.panel.setLevel(this.panel.HARD);
			this.setSize(this.panel.getWidth() + 15,this.panel.getHeight() + 60);
		}
		else if("exit".equals(command))
		{
			System.exit(0);
		}
		else if("about".equals(command))
		{
			JOptionPane.showMessageDialog(this,"灰之魔女伊蕾娜小姐，\n对幼时阅读的《《妮可冒险记》》抱怀憧憬，\n因而成为旅人，并进行世界范围内的旅行","前情提要",JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
