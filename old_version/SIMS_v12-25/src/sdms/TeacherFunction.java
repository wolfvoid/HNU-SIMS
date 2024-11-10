package sdms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class TeacherFunction {
    String teacher_num;
    JFrame studentJFrame = new JFrame("学校信息管理系统-----[教师模式]");
    Container stuCon = studentJFrame.getContentPane();
    JPanel pn_function = new JPanel(); //放置各种功能页面的容器
    JLabel lb_tips = new JLabel(); //提示窗口的内容

    public TeacherFunction(String teacher_num) { //整体界面
        this.teacher_num = teacher_num;
        studentJFrame.setSize(1300, 800);
        studentJFrame.setLocationRelativeTo(null);
        studentJFrame.setResizable(false);
        studentJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stuCon.setLayout(null);
        pn_function.setLayout(null);
        pn_function.setBorder(BorderFactory.createEtchedBorder());
        pn_function.setBounds(250, 30, 1045, 735);
        JPanel pn1 = new JPanel(); //顶部信息栏，操作者
        pn1.setBackground(Color.white);
        pn1.setBounds(0, 0, 250, 30);
        JLabel lb1 = new JLabel("教师：" + teacher_num);
        lb1.setFont(new Font("黑体", 0, 18));
        lb1.setForeground(Color.blue);
        pn1.add(lb1);
        JPanel pn2 = new JPanel(); //顶部信息栏，当前功能
        pn2.setBackground(Color.white);
        pn2.setBounds(250, 0, 1045, 30);
        JLabel lb2 = new JLabel("");
        lb2.setFont(new Font("黑体", 0, 20));
        lb2.setForeground(Color.black);
        pn2.add(lb2);
        JPanel pn_menu = new JPanel(); //菜单，进行功能选择
        pn_menu.setBackground(new Color(249, 250, 252));
        pn_menu.setBorder(BorderFactory.createEtchedBorder());
        pn_menu.setLayout(null);
        pn_menu.setBounds(0, 30, 250, 735);

        JButton bt1 = new JButton("个人信息"); //[个人信息]
        bt1.setFont(new Font("黑体", 0, 20));
        bt1.setContentAreaFilled(false);
        bt1.setBounds(0, 50, 249, 50);

        JButton bt2 = new JButton("任教课程"); //[任教课程]
        bt2.setFont(new Font("黑体", 0, 20));
        bt2.setContentAreaFilled(false);
        bt2.setBounds(0, 105, 249, 50);

        JButton bt3 = new JButton("成绩登记"); //[成绩登记]
        bt3.setFont(new Font("黑体", 0, 20));
        bt3.setContentAreaFilled(false);
        bt3.setBounds(0, 160, 249, 50);

        JButton bt4 = new JButton("修改密码"); //[修改密码]
        bt4.setFont(new Font("黑体", 0, 20));
        bt4.setContentAreaFilled(false);
        bt4.setBounds(0, 215, 249, 50);

        JButton bt5 = new JButton("退出"); //[退出]
        bt5.setFont(new Font("黑体", 0, 20));
        bt5.setContentAreaFilled(false);
        bt5.setBounds(0, 270, 249, 50);

        pn_menu.add(bt1);
        pn_menu.add(bt2);
        pn_menu.add(bt3);
        pn_menu.add(bt4);
        pn_menu.add(bt5);
        JPanel pn_welcome = new JPanel(); //欢迎页
        pn_welcome.setBorder(BorderFactory.createEtchedBorder());
        pn_welcome.setLayout(new BorderLayout());
        pn_welcome.setBounds(0, 0, 1045, 735);
        JLabel lb_welcome = new JLabel("欢迎使用");
        lb_welcome.setFont(new Font("黑体", 0, 100));
        lb_welcome.setHorizontalAlignment(SwingConstants.CENTER);
        pn_welcome.add(lb_welcome, BorderLayout.CENTER);

        stuCon.add(pn1);
        stuCon.add(pn2);
        stuCon.add(pn_menu);
        stuCon.add(pn_function);
        pn_function.add(pn_welcome);
        studentJFrame.setVisible(true);
        bt1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { //查看个人信息
                lb2.setText("[个人信息]");
                pn_function.removeAll();
                studentJFrame.repaint();
                pn_function.add(info());
                studentJFrame.validate();
            }
        });
        bt2.addActionListener(new ActionListener() { //任教课程
            public void actionPerformed(ActionEvent e) {
                lb2.setText("[选课]");
                pn_function.removeAll();
                studentJFrame.repaint();
                pn_function.add(teaching_course());
                studentJFrame.validate();
            }
        });
        bt3.addActionListener(new ActionListener() { //成绩登记
            public void actionPerformed(ActionEvent e) {
                lb2.setText("[我的课程]");
                pn_function.removeAll();
                studentJFrame.repaint();
                pn_function.add(grade_register());
                studentJFrame.validate();
            }
        });
        bt4.addActionListener(new ActionListener() { //修改密码
            public void actionPerformed(ActionEvent e) {
                lb2.setText("[修改密码]");
                pn_function.removeAll();
                studentJFrame.repaint();
                pn_function.add(changePassword());
                studentJFrame.validate();
            }
        });
        bt5.addActionListener(new ActionListener() { //退出教师模式
            public void actionPerformed(ActionEvent e) {
                lb_tips.setText("是否退出教师模式？");
                choiceTips();
            }
        });
    }

    public JPanel info() { //[个人信息]功能
        String[] teacher_info = new String[3]; //教师信息
        Calendar cal = Calendar.getInstance();
        try {
            String sql = "SELECT * FROM teacher WHERE tno=" + teacher_num; //SQL语句
            PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
            ps = HomePage.connection.prepareStatement(sql); //把操作数据库返回的结果保存到ps中
            ResultSet rs = ps.executeQuery(sql); //ResultSet类，用来存放获取的结果集
            while (rs.next()) {
                teacher_info[0] = rs.getString("tno");
                teacher_info[1] = rs.getString("tname");
                teacher_info[2] = rs.getString("tsex");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JPanel pn_info = new JPanel();
        pn_info.setLayout(null);
        pn_info.setSize(1045, 735);
        pn_info.setBorder(BorderFactory.createEtchedBorder());
        JLabel lb_infoImage = new JLabel(new ImageIcon("image/info.png"));
        lb_infoImage.setBounds(700, 130, 200, 200);
        JLabel[] lb = new JLabel[3]; //教师信息
        for (int i = 0; i < 3; i++) {
            lb[i] = new JLabel();
            lb[i].setFont(new Font("黑体", 0, 30));
            lb[i].setBounds(100, 102 + i * 60, 500, 50);
            pn_info.add(lb[i]);
        }
        lb[0].setText("工    号： " + teacher_info[0]);
        lb[1].setText("姓    名： " + teacher_info[1]);
        lb[2].setText("性    别： " + teacher_info[2]);
        pn_info.add(lb_infoImage);
        return pn_info;
    }

    public JPanel teaching_course() { //[个人信息]功能
        String[] teacher_info = new String[3]; //教师信息
        Calendar cal = Calendar.getInstance();
        try {
            String sql = "SELECT * FROM teacher WHERE tno=" + teacher_num; //SQL语句
            PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
            ps = HomePage.connection.prepareStatement(sql); //把操作数据库返回的结果保存到ps中
            ResultSet rs = ps.executeQuery(sql); //ResultSet类，用来存放获取的结果集
            while (rs.next()) {
                teacher_info[0] = rs.getString("tno");
                teacher_info[1] = rs.getString("tname");
                teacher_info[2] = rs.getString("tsex");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JPanel pn_info = new JPanel();
        pn_info.setLayout(null);
        pn_info.setSize(1045, 735);
        pn_info.setBorder(BorderFactory.createEtchedBorder());
        JLabel lb_infoImage = new JLabel(new ImageIcon("image/info.png"));
        lb_infoImage.setBounds(700, 130, 200, 200);
        JLabel[] lb = new JLabel[3]; //教师信息
        for (int i = 0; i < 3; i++) {
            lb[i] = new JLabel();
            lb[i].setFont(new Font("黑体", 0, 30));
            lb[i].setBounds(100, 102 + i * 60, 500, 50);
            pn_info.add(lb[i]);
        }
        lb[0].setText("course_teaching： " + teacher_info[0]);
        lb[1].setText("姓    名： " + teacher_info[1]);
        lb[2].setText("性    别： " + teacher_info[2]);
        pn_info.add(lb_infoImage);
        return pn_info;
    }

    public JPanel grade_register() { //[个人信息]功能
        String[] teacher_info = new String[3]; //教师信息
        Calendar cal = Calendar.getInstance();
        try {
            String sql = "SELECT * FROM teacher WHERE tno=" + teacher_num; //SQL语句
            PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
            ps = HomePage.connection.prepareStatement(sql); //把操作数据库返回的结果保存到ps中
            ResultSet rs = ps.executeQuery(sql); //ResultSet类，用来存放获取的结果集
            while (rs.next()) {
                teacher_info[0] = rs.getString("tno");
                teacher_info[1] = rs.getString("tname");
                teacher_info[2] = rs.getString("tsex");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JPanel pn_info = new JPanel();
        pn_info.setLayout(null);
        pn_info.setSize(1045, 735);
        pn_info.setBorder(BorderFactory.createEtchedBorder());
        JLabel lb_infoImage = new JLabel(new ImageIcon("image/info.png"));
        lb_infoImage.setBounds(700, 130, 200, 200);
        JLabel[] lb = new JLabel[3]; //教师信息
        for (int i = 0; i < 3; i++) {
            lb[i] = new JLabel();
            lb[i].setFont(new Font("黑体", 0, 30));
            lb[i].setBounds(100, 102 + i * 60, 500, 50);
            pn_info.add(lb[i]);
        }
        lb[0].setText("register： " + teacher_info[0]);
        lb[1].setText("姓    名： " + teacher_info[1]);
        lb[2].setText("性    别： " + teacher_info[2]);
        pn_info.add(lb_infoImage);
        return pn_info;
    }

        public JPanel changePassword() { //[修改密码]功能
            JPanel pn_changePassword = new JPanel();
            pn_changePassword.setLayout(null);
            pn_changePassword.setSize(1045, 735);
            pn_changePassword.setBorder(BorderFactory.createEtchedBorder());
            JLabel lb_old = new JLabel("旧密码："), lb_new1 = new JLabel("新密码："), lb_new2 = new JLabel("确认密码："), lb = new JLabel("（密码不超过20位）");
            JPasswordField pf_old = new JPasswordField(), pf_new1 = new JPasswordField(), pf_new2 = new JPasswordField();
            lb_old.setFont(new Font("黑体", 0, 25));
            lb_old.setBounds(300, 200, 100, 50);
            lb_new1.setFont(new Font("黑体", 0, 25));
            lb_new1.setBounds(300, 260, 100, 50);
            lb_new2.setFont(new Font("黑体", 0, 25));
            lb_new2.setBounds(275, 320, 125, 50);
            lb.setFont(new Font("黑体", 0, 18));
            lb.setBounds(650, 260, 200, 50);
            pf_old.setFont(new Font(null, 0, 30));
            pf_old.setBounds(400, 205, 250, 40);
            pf_new1.setFont(new Font(null, 0, 30));
            pf_new1.setBounds(400, 265, 250, 40);
            pf_new2.setFont(new Font(null, 0, 30));
            pf_new2.setBounds(400, 325, 250, 40);
            JLabel lb_image = new JLabel(new ImageIcon("image/修改密码.png"));
            lb_image.setBounds(447, 20, 150, 150);
            pn_changePassword.add(lb_old);
            pn_changePassword.add(lb_new1);
            pn_changePassword.add(lb_new2);
            pn_changePassword.add(lb);
            pn_changePassword.add(pf_old);
            pn_changePassword.add(pf_new1);
            pn_changePassword.add(pf_new2);
            pn_changePassword.add(lb_image);
            JButton bt_confirm = new JButton("确认"), bt_reset = new JButton("重置");
            bt_confirm.setFont(new Font("黑体", 0, 20));
            bt_confirm.setBounds(405, 410, 100, 50);
            bt_confirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
            bt_reset.setFont(new Font("黑体", 0, 20));
            bt_reset.setBounds(540, 410, 100, 50);
            bt_reset.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pn_changePassword.add(bt_confirm);
            pn_changePassword.add(bt_reset);
            bt_reset.addActionListener(new ActionListener() { //重置，将密码框内容清空
                public void actionPerformed(ActionEvent e) {
                    pf_old.setText("");
                    pf_new1.setText("");
                    pf_new2.setText("");
                }
            });
            bt_confirm.addActionListener(new ActionListener() { //确认
                public void actionPerformed(ActionEvent e) {
                    String password = null;
                    if (!((!String.valueOf(pf_old.getPassword()).equals("")) //若未填写完整，则进行提示
                            && (!String.valueOf(pf_new1.getPassword()).equals(""))
                            && (!String.valueOf(pf_new2.getPassword()).equals("")))) {
                        lb_tips.setText("请填写完整！");
                        functionTips();
                    } else { //若填写完整，则进行密码判断
                        try { //获取原密码
                            String sql = "SELECT password FROM teacher WHERE tno=" + teacher_num; //SQL语句
                            PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
                            ps = HomePage.connection.prepareStatement(sql); //把操作数据库返回的结果保存到ps中
                            ResultSet rs = ps.executeQuery(sql); //ResultSet类，用来存放获取的结果集
                            while (rs.next()) { //遍历结果集
                                password = rs.getString("password");
                            }
                            rs.close();
                            ps.close();
                        } catch (SQLException se) {
                            se.printStackTrace();
                        }
                        if (!String.valueOf(pf_old.getPassword()).equals(password)) { //若输入的旧密码错误，则进行提示
                            lb_tips.setText("旧密码错误！");
                            functionTips();
                        }
                        //若输入的旧密码正确，将判断两次新密码是否相同
                        else if (!String.valueOf(pf_new1.getPassword()).equals(String.valueOf(pf_new2.getPassword()))) { //若两次新密码不相同，则进行提示
                            lb_tips.setText("两次新密码不同！");
                            functionTips();
                        }
                        //若输入的两次新密码相同，将判断密码是否不超过20位
                        else if (String.valueOf(pf_new1.getPassword()).length() > 20) { //若输入的密码超过20位，则进行提示
                            lb_tips.setText("密码超过20位！");
                            functionTips();
                        } else { //符合改密条件，进行改密
                            try {
                                String sql = "UPDATE teacher set password=? WHERE tno=?"; //SQL语句
                                PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
                                ps = HomePage.connection.prepareStatement(sql); //把操作数据库返回的结果保存到ps中
                                ps.setString(1, String.valueOf(pf_new1.getPassword())); //SQL语句第一个?值
                                ps.setString(2, teacher_num); //SQL语句第二个?值
                                ps.executeUpdate(); //更新，执行修改操作
                                ps.close();
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                            pf_old.setText("");
                            pf_new1.setText("");
                            pf_new2.setText("");
                            lb_tips.setText("密码修改成功！");
                            functionTips();
                        }
                    }
                }
            });
            return pn_changePassword;
        }

        public void functionTips() { //操作时弹出的提示信息窗口
            JDialog tips = new JDialog(studentJFrame, "  提示", true);
            JPanel pn_tips = new JPanel();
            JButton bt_tips = new JButton("确定");
            tips.setSize(500, 200);
            tips.setLocationRelativeTo(null);
            tips.setResizable(false);
            tips.setLayout(null);
            pn_tips.setBounds(0, 30, 500, 70);
            lb_tips.setFont(new Font("黑体", 0, 25));
            bt_tips.setFont(new Font("黑体", 0, 20));
            bt_tips.setBounds(200, 100, 100, 50);
            bt_tips.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pn_tips.add(lb_tips);
            tips.add(pn_tips);
            tips.add(bt_tips);
            bt_tips.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tips.dispose();
                }
            });
            tips.setVisible(true);
        }

        public void choiceTips() { //操作时弹出的提示选择窗口
            JDialog choiceTips = new JDialog(studentJFrame, "  提示", true);
            JPanel pn_tips = new JPanel();
            JButton bt_yes = new JButton("是(Y)");
            JButton bt_no = new JButton("否(N)");
            choiceTips.setSize(500, 200);
            choiceTips.setLocationRelativeTo(null);
            choiceTips.setResizable(false);
            choiceTips.setLayout(null);
            pn_tips.setBounds(0, 30, 500, 70);
            lb_tips.setFont(new Font("黑体", 0, 25));
            bt_yes.setFont(new Font("黑体", 0, 20));
            bt_yes.setBounds(135, 100, 100, 50);
            bt_yes.setCursor(new Cursor(Cursor.HAND_CURSOR));
            bt_no.setFont(new Font("黑体", 0, 20));
            bt_no.setBounds(260, 100, 100, 50);
            bt_no.setCursor(new Cursor(Cursor.HAND_CURSOR));
            pn_tips.add(lb_tips);
            choiceTips.add(pn_tips);
            choiceTips.add(bt_yes);
            choiceTips.add(bt_no);
            bt_yes.addActionListener(new ActionListener() { //选择“是”，进行相应操作
                public void actionPerformed(ActionEvent e) {
                    choiceTips.dispose();
                    yesOperation();
                }
            });
            bt_no.addActionListener(new ActionListener() { //选择“否”，关闭提示选择窗口
                public void actionPerformed(ActionEvent e) {
                    choiceTips.dispose();
                }
            });
            bt_yes.addKeyListener(new KeyListener() { //“是”按钮的快捷键“Y”，“否”按钮的快捷键“N”
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_Y) {
                        choiceTips.dispose();
                        yesOperation();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_N) {
                        choiceTips.dispose();
                    }
                }

                public void keyReleased(KeyEvent e) {
                }

                public void keyTyped(KeyEvent e) {
                }
            });
            bt_no.addKeyListener(new KeyListener() { //“是”按钮的快捷键“Y”，“否”按钮的快捷键“N”
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_Y) {
                        choiceTips.dispose();
                        yesOperation();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_N) {
                        choiceTips.dispose();
                    }
                }

                public void keyReleased(KeyEvent e) {
                }

                public void keyTyped(KeyEvent e) {
                }
            });
            choiceTips.setVisible(true);
        }

        public void yesOperation() { //选择“是”之后，根据提示内容，选择要进行的操作
            if (lb_tips.getText().equals("是否退出教师模式？")) { //退出教师模式，返回到欢迎界面
                try {
                    HomePage.connection.close(); //关闭数据库连接
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                studentJFrame.dispose();
                HomePage.con.removeAll();
                HomePage.mainJFrame.repaint();
                HomePage.welcomePage();
                HomePage.mainJFrame.validate();
            }
        }
}
