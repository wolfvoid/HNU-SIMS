package sdms;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import static sdms.HomePage.returnHomepage;

public class TeacherFunction {
    String teacher_num;
    JFrame teacherJFrame = new JFrame("学校信息管理系统-----[教师模式]");
    Container stuCon = teacherJFrame.getContentPane();
    JPanel pn_function = new JPanel(); //放置各种功能页面的容器
    JLabel lb_tips = new JLabel(); //提示窗口的内容

    public TeacherFunction(String teacher_num) { //整体界面
        this.teacher_num = teacher_num;
        teacherJFrame.setSize(1300, 800);
        teacherJFrame.setLocationRelativeTo(null);
        teacherJFrame.setResizable(false);
        teacherJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        teacherJFrame.setVisible(true);
        bt1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { //查看个人信息
                lb2.setText("[个人信息]");
                pn_function.removeAll();
                teacherJFrame.repaint();
                pn_function.add(info());
                teacherJFrame.validate();
            }
        });
        bt2.addActionListener(new ActionListener() { //任教课程
            public void actionPerformed(ActionEvent e) {
                lb2.setText("[任教课程]");
                pn_function.removeAll();
                teacherJFrame.repaint();
                pn_function.add(teaching_course());
                teacherJFrame.validate();
            }
        });
        bt3.addActionListener(new ActionListener() { //成绩登记
            public void actionPerformed(ActionEvent e) {
                lb2.setText("[成绩登记]");
                pn_function.removeAll();
                teacherJFrame.repaint();
                pn_function.add(grade_register());
                teacherJFrame.validate();
            }
        });
        bt4.addActionListener(new ActionListener() { //修改密码
            public void actionPerformed(ActionEvent e) {
                lb2.setText("[修改密码]");
                pn_function.removeAll();
                teacherJFrame.repaint();
                pn_function.add(changePassword());
                teacherJFrame.validate();
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

    public JPanel teaching_course() { //[任教课程]功能
        JPanel tp_mycourse = new JPanel();
        JPanel tp_mycourse_1 = new JPanel();
        tp_mycourse.setLayout(null);
        tp_mycourse_1.setLayout(null);

        tp_mycourse.setBounds(0, 0, 1045, 735);
        tp_mycourse_1.setBounds(0, 0, 1045, 50);
        String[] columnNames = {"课程号", "课程名", "选课人数"}; //表格列名
        String[][] rowData = null; //表格数据
        int count = 0; //表的元组总数
        try { //获取course表信息
            String sql = "SELECT cno,cname FROM course WHERE tno=" + teacher_num; //SQL语句
            PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
            ps = HomePage.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_FORWARD_ONLY); //把操作数据库返回的结果保存到ps中
            ResultSet rs = ps.executeQuery(sql); //ResultSet类，用来存放获取的结果集
            rs.last();
            count = rs.getRow(); //获取宿舍元组总数
            if (count == 0) { //若dormitory表无元组
                rowData = new String[1][3];
                for (int i = 0; i < 3; i++)
                    rowData[0][i] = "无";
            } else { //若dormitory表有元组
                rowData = new String[count][3];
                rs.first();
                int i = 0;
                do { //获取该宿管管理的宿舍楼的所有宿舍信息
                    ResultSet rs2 = null;
                    rowData[i][0] = rs.getString("cno"); //课程号
                    rowData[i][1] = rs.getString("cname"); //课程名
                    try { //获取学生人数信息
                        String sql2 = "SELECT count(*) FROM select_course WHERE cno = '" + rowData[i][0] + "'"; //SQL语句
                        PreparedStatement ps2; //创建PreparedStatement类对象ps，用来执行SQL语句
                        ps2 = HomePage.connection.prepareStatement(sql2, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_FORWARD_ONLY); //把操作数据库返回的结果保存到ps中
                        rs2 = ps2.executeQuery(sql2); //ResultSet类，用来存放获取的结果集
                        rs2.last();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    rowData[i][2] = String.valueOf(rs2.getInt(1)); //学生人数
                    i++;
                } while (rs.next());
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel pn_top = new JPanel();
        pn_top.setBounds(0, 0, 1045, 50);
        JLabel lb_num;

        if (count == 0)
            lb_num = new JLabel("  无任教信息（快去开设课程吧！）  ");
        else
            lb_num = new JLabel("你有" + count + "条课程信息~  ");
        lb_num.setFont(new Font("黑体", 0, 25));
        JButton bt_export = new JButton("导出", new ImageIcon("image/导出.png"));

        bt_export.setBackground(Color.green.darker());
        bt_export.setFont(new Font("黑体", 0, 20));
        bt_export.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pn_top.add(lb_num);
        pn_top.add(bt_export);
        JTable table = new JTable(new MyTableModel(columnNames, rowData, 2));
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(1, 35)); //设置表头高度
        header.setFont(new Font("黑体", Font.BOLD, 23));
        table.setRowHeight(35); //设置各行高度
        table.setFont(new Font("黑体", 0, 20));
        table.setBackground(null);
        table.getTableHeader().setReorderingAllowed(false); //不允许移动各列
        JScrollPane scrollPane = new JScrollPane(table); //滚动条
        scrollPane.setBounds(0, 50, 1045, 645);
        tp_mycourse_1.add(pn_top);
        tp_mycourse.add(tp_mycourse_1);
        tp_mycourse.add(scrollPane);
        bt_export.addActionListener(new ActionListener() { //将表格导出成Excel文件
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(teacherJFrame, "请设置导出位置和文件名！", FileDialog.SAVE);
                fd.setVisible(true);
                String file = fd.getDirectory() + fd.getFile() + ".xls";
                if (fd.getFile() != null)
                    JTableToExcel.stu_export(new File(file), table);
            }
        });
        return tp_mycourse;
    }

    public JPanel grade_register() { //[成绩登记]功能
        JPanel tp_mycourse = new JPanel();
        JPanel tp_mycourse_1 = new JPanel();
        tp_mycourse.setLayout(null);
        tp_mycourse_1.setLayout(null);

        tp_mycourse.setBounds(0, 0, 1045, 735);
        tp_mycourse_1.setBounds(0, 0, 1045, 50);
        String[] columnNames = {"课程号", "课程名", "学生号", "学生名", "成绩", "操作"}; //表格列名
        String[][] rowData = null; //表格数据
        int count = 0; //表的元组总数
        try { //获取course表信息
            String sql = "SELECT course.cno,cname,stu_num,name,mark FROM course,select_course,student WHERE course.cno=select_course.cno && stu_num=sno && course.tno=" + teacher_num; //SQL语句
            PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
            ps = HomePage.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_FORWARD_ONLY); //把操作数据库返回的结果保存到ps中
            ResultSet rs = ps.executeQuery(sql); //ResultSet类，用来存放获取的结果集
            rs.last();
            count = rs.getRow(); //获取元组总数
            if (count == 0) { //若dormitory表无元组
                rowData = new String[1][6];
                for (int i = 0; i < 6; i++)
                    rowData[0][i] = "无";
            } else { //若有元组
                rowData = new String[count][6];
                rs.first();
                int i = 0;
                do { //获取该宿管管理的宿舍楼的所有宿舍信息
                    ResultSet rs2 = null;
                    rowData[i][0] = rs.getString("course.cno"); //课程号
                    rowData[i][1] = rs.getString("cname"); //课程名
                    rowData[i][2] = rs.getString("stu_num"); //学生号
                    rowData[i][3] = rs.getString("name"); //学生名
                    rowData[i][4] = rs.getString("mark"); //成绩
                    //rowData[i][5] = rs.getString("cname"); //操作
                    i++;
                } while (rs.next());
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        JPanel pn_top = new JPanel();
        pn_top.setBounds(0, 0, 1045, 50);
        JLabel lb_num;

        if (count == 0)
            lb_num = new JLabel("  无需登记成绩（快去开设课程吧！）  ");
        else
            lb_num = new JLabel("你有" + count + "条成绩需登记~  ");
        lb_num.setFont(new Font("黑体", 0, 25));
        JButton bt_export = new JButton("导出", new ImageIcon("image/导出.png"));

        bt_export.setBackground(Color.green.darker());
        bt_export.setFont(new Font("黑体", 0, 20));
        bt_export.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pn_top.add(lb_num);
        pn_top.add(bt_export);
        JTable table = new JTable(new MyTableModel(columnNames, rowData, 6));
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(1, 35)); //设置表头高度
        header.setFont(new Font("黑体", Font.BOLD, 23));
        table.setRowHeight(35); //设置各行高度
        table.setFont(new Font("黑体", 0, 20));
        table.setBackground(null);
        table.getTableHeader().setReorderingAllowed(false); //不允许移动各列
        JScrollPane scrollPane = new JScrollPane(table); //滚动条
        scrollPane.setBounds(0, 50, 1045, 645);
        tp_mycourse_1.add(pn_top);
        tp_mycourse.add(tp_mycourse_1);
        tp_mycourse.add(scrollPane);

        if (count != 0) {
            MyEvent e = new MyEvent() { //点击“查看”按钮，查看信息，可修改和删除信息
                public void invoke(ActionEvent e) {
                    MyButton button = (MyButton) e.getSource();
                    register_mark((String) table.getValueAt(button.getRow(), button.getColumn() - 5), (String) table.getValueAt(button.getRow(), button.getColumn() - 3));
                }
            };
            table.getColumnModel().getColumn(5).setCellRenderer(new MyButtonRender("登记")); //设置表格的渲染器
            MyButtonEditor editor = new MyButtonEditor(e, "登记");
            table.getColumnModel().getColumn(5).setCellEditor(editor); //设置表格的编辑器
        }

        bt_export.addActionListener(new ActionListener() { //将表格导出成Excel文件
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(teacherJFrame, "请设置导出位置和文件名！", FileDialog.SAVE);
                fd.setVisible(true);
                String file = fd.getDirectory() + fd.getFile() + ".xls";
                if (fd.getFile() != null)
                    JTableToExcel.stu_export(new File(file), table);
            }
        });
        return tp_mycourse;
    }

    private void register_mark(String cno, String sno) {
        JDialog choiceTips = new JDialog(teacherJFrame, "请输入成绩", true);
        JPanel jPanel = new JPanel();
        choiceTips.setSize(500, 200);
        choiceTips.setLocationRelativeTo(null);
        choiceTips.setResizable(false);
        choiceTips.setLayout(null);
        JTextField markField = new JTextField();
        JButton Button = new JButton("确认");
        Button.setFont(new Font("黑体", Font.PLAIN, 20));
        Button.setBounds(180, 100, 100, 50);
        Button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        markField.setFont(new Font("黑体", Font.PLAIN, 25));
        markField.setBounds(120, 40, 230, 40);
        choiceTips.add(markField);
        choiceTips.add(Button);
        choiceTips.dispose();

//        Button.addMouseListener(new MouseAdapter() {
//            public void mouseEntered(MouseEvent arg0) {
//                Button.setForeground(Color.blue);
//            }
//
//            public void mouseExited(MouseEvent arg0) {
//                Button.setForeground(null);
//            }
//
//            public void mouseClicked(MouseEvent arg0) {
//                choiceTips.dispose();
//            }
//        });

        Button.addActionListener(new ActionListener() { //选择“是”，进行相应操作
            public void actionPerformed(ActionEvent e) {
                choiceTips.dispose();
                perform_register(Integer.valueOf(markField.getText()), cno, sno);
            }
        });
        Button.addKeyListener(new KeyListener() { //“是”按钮的快捷键“Y”，“否”按钮的快捷键“N”
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Y) {
                    choiceTips.dispose();
                    perform_register(Integer.valueOf(markField.getText()), cno, sno);
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


    private void perform_register(Integer mark, String cno, String sno) {
        String sql = "UPDATE select_course SET mark=? where cno=? && sno=?";
        //String sql = "INSERT INTO select_course(sno,cno,grade) VALUES"+"("+"'"+stu_num+"'"+"'"+str+"'"+""+")"; //SQL语句
        PreparedStatement ps = null;
        try {
            ps = HomePage.connection.prepareStatement(sql);
            ps.setInt(1, mark);  // 设置第一个参数，对应 SQL 语句中的第一个问号
            ps.setString(2, cno);      // 设置第二个参数，对应 SQL 语句中的第二个问号
            ps.setString(3, sno);  // 设置第三个参数，对应 SQL 语句中的第三个问号，替换为实际的 grade 数据

            int rowsAffected = ps.executeUpdate();  // 执行插入操作，并返回受影响的行数

            if (rowsAffected > 0) {
                System.out.println("插入成功！");
            } else {
                System.out.println("插入失败！");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public JPanel changePassword() { //[修改密码]功能
        JPanel pn_changePassword = new JPanel();
        pn_changePassword.setLayout(null);
        pn_changePassword.setSize(1045, 735);
        pn_changePassword.setBorder(BorderFactory.createEtchedBorder());
        JLabel lb_old = new JLabel("旧密码："), lb_new1 = new JLabel("新密码："), lb_new2 = new JLabel("确认密码："), lb = new JLabel("（密码不超过20位）");
        JPasswordField pf_old = new JPasswordField(), pf_new1 = new JPasswordField(), pf_new2 = new JPasswordField();
        lb_old.setFont(new Font("黑体", Font.PLAIN, 25));
        lb_old.setBounds(300, 200, 100, 50);
        lb_new1.setFont(new Font("黑体", Font.PLAIN, 25));
        lb_new1.setBounds(300, 260, 100, 50);
        lb_new2.setFont(new Font("黑体", Font.PLAIN, 25));
        lb_new2.setBounds(275, 320, 125, 50);
        lb.setFont(new Font("黑体", Font.PLAIN, 18));
        lb.setBounds(650, 260, 200, 50);
        pf_old.setFont(new Font(null, Font.PLAIN, 30));
        pf_old.setBounds(400, 205, 250, 40);
        pf_new1.setFont(new Font(null, Font.PLAIN, 30));
        pf_new1.setBounds(400, 265, 250, 40);
        pf_new2.setFont(new Font(null, Font.PLAIN, 30));
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
        JDialog tips = new JDialog(teacherJFrame, "  提示", true);
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
        JDialog choiceTips = new JDialog(teacherJFrame, "  提示", true);
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

            teacherJFrame.dispose();
            returnHomepage();
        }
    }
}
