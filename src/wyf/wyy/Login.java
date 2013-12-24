package wyf.wyy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Login extends JFrame implements ActionListener
{
	private JPanel jp=new JPanel();//创建面板容器
	//创建标签数组
	private JLabel[] jlArray={new JLabel("用户名"),new JLabel("密   码"),new JLabel(""),};
	//创建按钮数组
	private JButton[] jbArray={new JButton("登录"),new JButton("注册"),
							   new JButton("修改密码"),new JButton("删除用户")};
	private JTextField jtf=new JTextField();//填写用户名的文本框
	private JPasswordField jpf=new JPasswordField();//填写密码的文本框
	public Login()
	{
		jp.setLayout(null);//设置布局管理器为空布局
		for(int i=0;i<2;i++)
		{
			jlArray[i].setBounds(30,20+i*50,80,26);
			jbArray[i].setBounds(50+i*110,120,90,26);
			//将标签与按钮添加到JPanel容器中
			jp.add(jlArray[i]);
			jp.add(jbArray[i]);
			jbArray[i].addActionListener(this);//为按钮注册动作事件监听器
		}
		//添加删除用户和修改密码的按钮
		for(int i=0;i<2;i++)
		{
			jbArray[i+2].setBounds(50+i*110,160,90,26);
			jp.add(jbArray[i+2]);
			jbArray[i+2].addActionListener(this);//为按钮注册动作事件监听器
		}
		jtf.setBounds(80,20,180,30);	//设置文本框大小
		jp.add(jtf);					//将文本框添加到JPanel里
		jtf.addActionListener(this);	//为文本框注册动作事件监听器
		jpf.setBounds(80,70,180,30);	//设置密码框大小
		jp.add(jpf);					//将密码框添加到JPanel里
		jpf.setEchoChar('*');			//设置密码框的回显字符		
		jpf.addActionListener(this);	//为密码框注册动作事件监听器
		//设置用于显示登陆状态的标签大小位置，并将其添加进JPanel容器
		jlArray[2].setBounds(10,180,300,30);
		jp.add(jlArray[2]);
		//设置窗体的图标
		Image icon=Toolkit.getDefaultToolkit().getImage("img/ico.gif");
		this.setIconImage(icon);
		this.add(jp);//将窗体添加到面板中
		this.setTitle("登陆");//设置窗体标题
		this.setResizable(false);//设置窗体不让用户调整大小
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100,100,300,250);//设置窗体的大小
		this.setVisible(true);//设置窗体的可见性
	}
	public void clear()
	{//清空输入框的信息;并把输入焦点到用户名框
		jtf.setText("");
		jpf.setText("");
		jtf.requestFocus();
	}
	@Override//声明此方法为重写方法
	public void actionPerformed(ActionEvent e)
	{/*实现登陆窗体业务功能的方法*/
		//得到用户名和密码
		String user=jtf.getText().trim();
		String pwd=String.valueOf(jpf.getPassword());
		String sql="";//声明SQL语句
		if(e.getSource()==jtf)
		{//事件源为文本框 切换输入焦点到密码框
			jpf.requestFocus();
		}
		else if(e.getSource()==jbArray[0]||e.getSource()==jpf)
		{//判断用户名和密码是否匹配  查询数据库		
			if(DButil.check(user,pwd))
			{//登陆成功
				MainFrame mf=new MainFrame(jtf.getText()); //登陆进主窗体					
				this.dispose();//释放登陆窗体
			}
			else
			{//登陆失败
				jlArray[2].setText("对不起，非法的用户名和密码！！！");
				this.clear();//清空输入窗口
			}
		}
		else if(e.getSource()==jbArray[1])
		{//事件源为注册按钮
			if(user.equals("")||pwd.equals(""))
			{//如果注册的用户名为空或者密码为空
				jlArray[2].setText("用户名和密码都不得为空！！！");
				this.clear();//清空输入文本框
			}
			else
			{
			    sql="select uid from user where uid='"+user+"'";
				if(DButil.isExist(sql))
				{//用户名已经存在
					jlArray[2].setText("对不起，用户名已存在！！！");
					this.clear();//清空输入文本框
				}
				else
				{
					sql="insert into user values('"+user+"','"+pwd+"')";					
					if(DButil.update(sql)>0)
					{//注册成功
						jlArray[2].setText("恭喜您！！！注册成功，请登陆");
					}					
				}
			}			
		}		
		else if(e.getSource()==jbArray[2])//修改密码的监听
		{
			//先判断是否输入用户名和密码
			if(user.equals("")||pwd.equals(""))
			{
				jlArray[2].setText("修改密码先输入正确的用户名和密码！！！");
				this.clear();//清空输入文本框
			}
			//判断是否输入了正确的用户名和密码
			else if(DButil.check(user,pwd))
			{
				//正确的用户名和密码
				String password=JOptionPane.showInputDialog(this,"修改密码:","请输入新密码",
														JOptionPane.PLAIN_MESSAGE);
				//得到新的密码为空
				if(password==null||password.equals(""))
				{
					JOptionPane.showMessageDialog(this,"密码不得为空！！！","错误",
											JOptionPane.WARNING_MESSAGE);					
				}				
				else
				{//密码不为空
					sql="update user set pwd='"+password+"' where uid='"+user+"'";//更新密码的SQL
					if(DButil.update(sql)>0)
					{//密码修改成功
						this.clear();//清空输入文本框
						jlArray[2].setText("恭喜您！！！密码修改成功，请用新密码登陆");
					}					
				}				
			}
			else
			{			
				JOptionPane.showMessageDialog(this,"用户名或者密码错误！！！","错误",
											JOptionPane.WARNING_MESSAGE);
				this.clear();//清空输入文本框
			}
		}		
		else if(e.getSource()==jbArray[3])//删除用户
		{			
				if(DButil.check(user,pwd))
				{//密码和用户都对的情况
					int yn=JOptionPane.showConfirmDialog(this,"是否删除？","删除",
											JOptionPane.YES_NO_OPTION);
					if(yn==JOptionPane.YES_OPTION)
					{
						int count=DButil.delUser(user);
						jlArray[2].setText("用户"+user+"删除成功"+"共删除了"+count+"个联系人");
						this.clear();//清空输入文本框
					}
				}
				else
				{//密码和用户名不匹配
					jlArray[2].setText("对不起，非法的用户名和密码！！！");
					this.clear();//清空输入文本框
				}			
		}		
	} 
	public static void main(String []args)
	{
		new Login();//创建登陆窗体
	}
}