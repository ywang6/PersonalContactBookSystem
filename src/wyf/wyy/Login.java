package wyf.wyy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Login extends JFrame implements ActionListener
{
	private JPanel jp=new JPanel();//�����������
	//������ǩ����
	private JLabel[] jlArray={new JLabel("�û���"),new JLabel("��   ��"),new JLabel(""),};
	//������ť����
	private JButton[] jbArray={new JButton("��¼"),new JButton("ע��"),
							   new JButton("�޸�����"),new JButton("ɾ���û�")};
	private JTextField jtf=new JTextField();//��д�û������ı���
	private JPasswordField jpf=new JPasswordField();//��д������ı���
	public Login()
	{
		jp.setLayout(null);//���ò��ֹ�����Ϊ�ղ���
		for(int i=0;i<2;i++)
		{
			jlArray[i].setBounds(30,20+i*50,80,26);
			jbArray[i].setBounds(50+i*110,120,90,26);
			//����ǩ�밴ť��ӵ�JPanel������
			jp.add(jlArray[i]);
			jp.add(jbArray[i]);
			jbArray[i].addActionListener(this);//Ϊ��ťע�ᶯ���¼�������
		}
		//���ɾ���û����޸�����İ�ť
		for(int i=0;i<2;i++)
		{
			jbArray[i+2].setBounds(50+i*110,160,90,26);
			jp.add(jbArray[i+2]);
			jbArray[i+2].addActionListener(this);//Ϊ��ťע�ᶯ���¼�������
		}
		jtf.setBounds(80,20,180,30);	//�����ı����С
		jp.add(jtf);					//���ı�����ӵ�JPanel��
		jtf.addActionListener(this);	//Ϊ�ı���ע�ᶯ���¼�������
		jpf.setBounds(80,70,180,30);	//����������С
		jp.add(jpf);					//���������ӵ�JPanel��
		jpf.setEchoChar('*');			//���������Ļ����ַ�		
		jpf.addActionListener(this);	//Ϊ�����ע�ᶯ���¼�������
		//����������ʾ��½״̬�ı�ǩ��Сλ�ã���������ӽ�JPanel����
		jlArray[2].setBounds(10,180,300,30);
		jp.add(jlArray[2]);
		//���ô����ͼ��
		Image icon=Toolkit.getDefaultToolkit().getImage("img/ico.gif");
		this.setIconImage(icon);
		this.add(jp);//��������ӵ������
		this.setTitle("��½");//���ô������
		this.setResizable(false);//���ô��岻���û�������С
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100,100,300,250);//���ô���Ĵ�С
		this.setVisible(true);//���ô���Ŀɼ���
	}
	public void clear()
	{//�����������Ϣ;�������뽹�㵽�û�����
		jtf.setText("");
		jpf.setText("");
		jtf.requestFocus();
	}
	@Override//�����˷���Ϊ��д����
	public void actionPerformed(ActionEvent e)
	{/*ʵ�ֵ�½����ҵ���ܵķ���*/
		//�õ��û���������
		String user=jtf.getText().trim();
		String pwd=String.valueOf(jpf.getPassword());
		String sql="";//����SQL���
		if(e.getSource()==jtf)
		{//�¼�ԴΪ�ı��� �л����뽹�㵽�����
			jpf.requestFocus();
		}
		else if(e.getSource()==jbArray[0]||e.getSource()==jpf)
		{//�ж��û����������Ƿ�ƥ��  ��ѯ���ݿ�		
			if(DButil.check(user,pwd))
			{//��½�ɹ�
				MainFrame mf=new MainFrame(jtf.getText()); //��½��������					
				this.dispose();//�ͷŵ�½����
			}
			else
			{//��½ʧ��
				jlArray[2].setText("�Բ��𣬷Ƿ����û��������룡����");
				this.clear();//������봰��
			}
		}
		else if(e.getSource()==jbArray[1])
		{//�¼�ԴΪע�ᰴť
			if(user.equals("")||pwd.equals(""))
			{//���ע����û���Ϊ�ջ�������Ϊ��
				jlArray[2].setText("�û��������붼����Ϊ�գ�����");
				this.clear();//��������ı���
			}
			else
			{
			    sql="select uid from user where uid='"+user+"'";
				if(DButil.isExist(sql))
				{//�û����Ѿ�����
					jlArray[2].setText("�Բ����û����Ѵ��ڣ�����");
					this.clear();//��������ı���
				}
				else
				{
					sql="insert into user values('"+user+"','"+pwd+"')";					
					if(DButil.update(sql)>0)
					{//ע��ɹ�
						jlArray[2].setText("��ϲ��������ע��ɹ������½");
					}					
				}
			}			
		}		
		else if(e.getSource()==jbArray[2])//�޸�����ļ���
		{
			//���ж��Ƿ������û���������
			if(user.equals("")||pwd.equals(""))
			{
				jlArray[2].setText("�޸�������������ȷ���û��������룡����");
				this.clear();//��������ı���
			}
			//�ж��Ƿ���������ȷ���û���������
			else if(DButil.check(user,pwd))
			{
				//��ȷ���û���������
				String password=JOptionPane.showInputDialog(this,"�޸�����:","������������",
														JOptionPane.PLAIN_MESSAGE);
				//�õ��µ�����Ϊ��
				if(password==null||password.equals(""))
				{
					JOptionPane.showMessageDialog(this,"���벻��Ϊ�գ�����","����",
											JOptionPane.WARNING_MESSAGE);					
				}				
				else
				{//���벻Ϊ��
					sql="update user set pwd='"+password+"' where uid='"+user+"'";//���������SQL
					if(DButil.update(sql)>0)
					{//�����޸ĳɹ�
						this.clear();//��������ı���
						jlArray[2].setText("��ϲ�������������޸ĳɹ��������������½");
					}					
				}				
			}
			else
			{			
				JOptionPane.showMessageDialog(this,"�û�������������󣡣���","����",
											JOptionPane.WARNING_MESSAGE);
				this.clear();//��������ı���
			}
		}		
		else if(e.getSource()==jbArray[3])//ɾ���û�
		{			
				if(DButil.check(user,pwd))
				{//������û����Ե����
					int yn=JOptionPane.showConfirmDialog(this,"�Ƿ�ɾ����","ɾ��",
											JOptionPane.YES_NO_OPTION);
					if(yn==JOptionPane.YES_OPTION)
					{
						int count=DButil.delUser(user);
						jlArray[2].setText("�û�"+user+"ɾ���ɹ�"+"��ɾ����"+count+"����ϵ��");
						this.clear();//��������ı���
					}
				}
				else
				{//������û�����ƥ��
					jlArray[2].setText("�Բ��𣬷Ƿ����û��������룡����");
					this.clear();//��������ı���
				}			
		}		
	} 
	public static void main(String []args)
	{
		new Login();//������½����
	}
}