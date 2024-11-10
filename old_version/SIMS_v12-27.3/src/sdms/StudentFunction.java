package sdms;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import static sdms.HomePage.returnHomepage;


public class StudentFunction implements ActionListener { //学生功能类
    private static final Logger logger = LoggerUtil.getLogger(StudentFunction.class);
    private static final String[] BUTTON_LABELS = {
            "个人信息", "选课", "我的课程", "宿舍报修", "宿舍反馈", "修改密码", "退出"
    };
    private final JPanel pn_menu = new JPanel(); // 菜单，进行功能选择
    private final JPanel pn_welcome = new JPanel(); // 欢迎页
    String stu_num;
    JFrame studentJFrame = new JFrame("学校信息管理系统-----[学生模式]");
    Container stuCon = studentJFrame.getContentPane();
    JPanel pn_function = new JPanel(); //放置各种功能页面的容器
    JLabel lb_tips = new JLabel(); //提示窗口的内容
    JPanel pn_first = new JPanel(); //选项卡1
    JPanel pn_first_1 = new JPanel(); //选项卡1_1

    JPanel pn_second = new JPanel(); //选项卡2
    JPanel pn_second_1 = new JPanel(); //选项卡2_1

    public StudentFunction(String stu_num) {
        this.stu_num = stu_num;
        initializeMainFrame();
        createTopInfoPanel();
        createMenuPanel();
        createWelcomePanel();
        initializeButtons();
        studentJFrame.setVisible(true);
    }

    private void initializeMainFrame() {
        studentJFrame.setSize(1300, 800);
        studentJFrame.setLocationRelativeTo(null);
        studentJFrame.setResizable(false);
        studentJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        stuCon.setLayout(new BorderLayout());
        pn_function.setLayout(new BorderLayout());
        pn_function.setBorder(BorderFactory.createEtchedBorder());
        pn_menu.setLayout(new GridLayout(7, 1));
        pn_menu.setBackground(new Color(249, 250, 252));
        pn_menu.setBorder(BorderFactory.createEtchedBorder());
    }

    private void createTopInfoPanel() {
        JPanel pn1 = createTopPanel("学生：" + stu_num);
        JPanel pn2 = createTopPanel("");
        stuCon.add(pn1, BorderLayout.NORTH);
        stuCon.add(pn2, BorderLayout.CENTER);
    }

    private JPanel createTopPanel(String labelText) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.white);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("黑体", Font.PLAIN, 18));
        label.setForeground(Color.blue);
        panel.add(label);
        return panel;
    }

    private void createMenuPanel() {
        for (String label : BUTTON_LABELS) {
            JButton button = createMenuButton(label);
            pn_menu.add(button);
        }
        stuCon.add(pn_menu, BorderLayout.WEST);
    }

    private JButton createMenuButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("黑体", Font.PLAIN, 20));
        button.setContentAreaFilled(false);
        return button;
    }

    private void createWelcomePanel() {
        JLabel lb_welcome = new JLabel("欢迎使用");
        lb_welcome.setFont(new Font("黑体", Font.PLAIN, 100));
        lb_welcome.setHorizontalAlignment(SwingConstants.CENTER);
        pn_welcome.setLayout(new BorderLayout());
        pn_welcome.add(lb_welcome, BorderLayout.CENTER);
        pn_function.add(pn_welcome);
        stuCon.add(pn_function, BorderLayout.CENTER);
    }

    private void initializeButtons() {
        for (int i = 0; i < BUTTON_LABELS.length; i++) {
            JButton button = (JButton) pn_menu.getComponent(i);
            button.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        String buttonText = source.getText();

        switch (buttonText) {
            case "个人信息":
                showInfo();
                break;
            case "选课":
                showSelectCourse();
                break;
            case "我的课程":
                showMyCourse();
                break;
            case "宿舍报修":
                showRepair();
                break;
            case "宿舍反馈":
                showAdvise();
                break;
            case "修改密码":
                showChangePassword();
                break;
            case "退出":
                showExitConfirmation();
                break;
        }
    }

    private void showInfo() {
        pn_function.removeAll();
        studentJFrame.repaint();
        pn_function.add(info());
        studentJFrame.validate();
    }

    private void showSelectCourse() {
        pn_function.removeAll();
        studentJFrame.repaint();
        pn_function.add(select_course());
        studentJFrame.validate();
    }

    private void showMyCourse() {
        pn_function.removeAll();
        studentJFrame.repaint();
        pn_function.add(my_course());
        studentJFrame.validate();
    }

    private void showRepair() {
        pn_function.removeAll();
        studentJFrame.repaint();
        pn_function.add(repair());
        studentJFrame.validate();
    }

    private void showAdvise() {
        pn_function.removeAll();
        studentJFrame.repaint();
        pn_function.add(advise());
        studentJFrame.validate();
    }

    private void showChangePassword() {
        //lb2.setText("[修改密码]");
        pn_function.removeAll();
        studentJFrame.repaint();
        pn_function.add(changePassword());
        studentJFrame.validate();
    }

    private void showExitConfirmation() {
        lb_tips.setText("是否退出学生模式？");
        choiceTips();
    }

    public JPanel info() {
        String[] stu_info = fetchStudentInfo();
        String[] stay_info = {"无", "无", "无", "无"};

        if (stu_info[8].equals("是")) {
            stay_info = fetchStayInfo();
        }

        JPanel pn_info = new JPanel();
        pn_info.setLayout(null);
        pn_info.setSize(1045, 735);
        pn_info.setBorder(BorderFactory.createEtchedBorder());

        JLabel lb_infoImage = new JLabel(new ImageIcon("image/info.png"));
        lb_infoImage.setBounds(700, 130, 200, 200);
        pn_info.add(lb_infoImage);

        JLabel[] lb = new JLabel[9];
        for (int i = 0; i < 9; i++) {
            lb[i] = new JLabel();
            lb[i].setFont(new Font("黑体", Font.PLAIN, 30));
            lb[i].setBounds(100, 102 + i * 60, 500, 50);
            pn_info.add(lb[i]);
        }

        lb[0].setText("学    号： " + stu_info[0]);
        lb[1].setText("姓    名： " + stu_info[1]);
        lb[2].setText("性    别： " + stu_info[2]);
        lb[3].setText("年    龄： " + stu_info[3]);
        lb[4].setText("年    级： " + stu_info[4] + "级");
        lb[5].setText("院    系： " + stu_info[5]);
        lb[6].setText("班    级： " + stu_info[6]);
        lb[7].setText("手    机： " + stu_info[7]);
        lb[8].setText("是否入住： " + stu_info[8]);

        JLabel[] lb1 = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            lb1[i] = new JLabel();
            lb1[i].setFont(new Font("黑体", Font.PLAIN, 30));
            lb1[i].setBounds(610, 402 + i * 60, 400, 50);
            pn_info.add(lb1[i]);
        }

        lb1[0].setText("楼    号： " + stay_info[0]);
        lb1[1].setText("楼    层： " + stay_info[1]);
        lb1[2].setText("宿 舍 号： " + stay_info[2]);
        lb1[3].setText("入住时间： " + stay_info[3]);

        return pn_info;
    }


    private String[] fetchStudentInfo() {
        String[] stu_info = new String[9];
        try {
            String sql = "SELECT * FROM student WHERE stu_num=?";
            PreparedStatement ps = HomePage.connection.prepareStatement(sql);
            ps.setString(1, stu_num);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                stu_info[0] = rs.getString("stu_num");
                stu_info[1] = rs.getString("name");
                stu_info[2] = rs.getString("sex");
                Calendar cal = Calendar.getInstance();
                stu_info[3] = String.valueOf(cal.get(Calendar.YEAR) - rs.getInt("birth"));
                stu_info[4] = rs.getString("grade");
                stu_info[5] = rs.getString("faculty");
                stu_info[6] = rs.getString("class");
                stu_info[7] = rs.getString("phone");
                stu_info[8] = rs.getString("yes_no");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "在查询学生信息时发生异常", e);
        }
        return stu_info;
    }

    private String[] fetchStayInfo() {
        String[] stay_info = {"无", "无", "无", "无"};
        try {
            String sql = "SELECT * FROM stayinfo WHERE stu_num=?";
            PreparedStatement ps = HomePage.connection.prepareStatement(sql);
            ps.setString(1, stu_num);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                stay_info[0] = rs.getString("floor_num");
                stay_info[1] = rs.getString("layer");
                stay_info[2] = rs.getString("room_num");
                stay_info[3] = rs.getString("time");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "在查询住宿情况时发生异常", e);
        }
        return stay_info;
    }

    public JTabbedPane select_course() { //[选课]功能
        JTabbedPane tp_selcourse = new JTabbedPane();
        tp_selcourse.setFont(new Font("黑体", Font.PLAIN, 25));
        tp_selcourse.setBounds(0, 0, 1045, 735);
        allcourses();
        tp_selcourse.addTab(" 全部 ", pn_first);
        queryCourseInfo_1();
        tp_selcourse.addTab(" 查询 ", pn_second);
        return tp_selcourse;
    }

    public void allcourses() {
        String[] columnNames = {"课程号", "课程名", "教师名", "操作"}; //表格列名
        String[][] rowData = null; //表格数据
        int count = 0; //表的元组总数
        try { //获取dormitory表信息
            String sql = "SELECT cno,cname,tname FROM course,teacher WHERE cno NOT IN (SELECT cno FROM select_course WHERE sno=" + stu_num + ")" + "AND course.tno=teacher.tno"; //SQL语句
            PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
            ps = HomePage.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_FORWARD_ONLY); //把操作数据库返回的结果保存到ps中
            ResultSet rs = ps.executeQuery(sql); //ResultSet类，用来存放获取的结果集
            rs.last();
            count = rs.getRow(); //获取课程元组总数
            if (count == 0) { //若course表无元组
                rowData = new String[1][4];
                for (int i = 0; i < 4; i++)
                    rowData[0][i] = "无";
            } else { //若dormitory表有元组
                rowData = new String[count][4];
                rs.first();
                int i = 0;
                do { //获取该宿管管理的宿舍楼的所有宿舍信息
                    rowData[i][0] = rs.getString("cno"); //课程号
                    rowData[i][1] = rs.getString("cname"); //楼层
                    rowData[i][2] = rs.getString("tname"); //教师名
                    i++;
                } while (rs.next());
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "在查询授课表时发生异常", e);
        }
        //界面
        pn_first.setLayout(null);
        pn_first_1.removeAll();
        pn_first_1.setBounds(0, 0, 1045, 695);
        pn_first_1.setLayout(null);
        JPanel pn_top = new JPanel();
        pn_top.setBounds(0, 0, 1045, 50);
        JLabel lb_num;
        if (count == 0)
            lb_num = new JLabel("  无课可选qwq  ");
        else
            lb_num = new JLabel("共有" + count + "条课程信息！  ");
        lb_num.setFont(new Font("黑体", Font.PLAIN, 25));
        JButton bt_export = new JButton("导出", new ImageIcon("image/导出.png"));
        bt_export.setBackground(Color.green.darker());
        bt_export.setFont(new Font("黑体", Font.PLAIN, 20));
        bt_export.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pn_top.add(lb_num);
        pn_top.add(bt_export);
        JTable table = new JTable(new MyTableModel(columnNames, rowData, 9));
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(1, 35)); //设置表头高度
        header.setFont(new Font("黑体", Font.BOLD, 23));
        table.setRowHeight(35); //设置各行高度
        table.setFont(new Font("黑体", Font.PLAIN, 20));
        table.setBackground(null);
        table.getTableHeader().setReorderingAllowed(false); //不允许移动各列
        JScrollPane scrollPane = new JScrollPane(table); //滚动条
        scrollPane.setBounds(0, 50, 1045, 645);
        pn_first_1.add(pn_top);
        pn_first_1.add(scrollPane);

        if (count != 0) {
            MyEvent e = new MyEvent() { //点击“查看”按钮，查看信息，可修改和删除信息
                public void invoke(ActionEvent e) {
                    MyButton button = (MyButton) e.getSource();
                    SelectCourse((String) table.getValueAt(button.getRow(), button.getColumn() - 3));
                }
            };

            table.getColumnModel().getColumn(3).setCellRenderer(new MyButtonRender("选课")); //设置表格的渲染器
            MyButtonEditor editor = new MyButtonEditor(e, "选课");
            table.getColumnModel().getColumn(3).setCellEditor(editor); //设置表格的编辑器
        }

        //将表格导出成Excel文件
        bt_export.addActionListener(e -> {
            FileDialog fd = new FileDialog(studentJFrame, "请设置导出位置和文件名！", FileDialog.SAVE);
            fd.setVisible(true);
            String file = fd.getDirectory() + fd.getFile() + ".xls";
            if (fd.getFile() != null)
                JTableToExcel.export(new File(file), table);
        });
        pn_first.removeAll();
        studentJFrame.repaint();
        pn_first.add(pn_first_1);
        studentJFrame.validate();
    }


    public void queryCourseInfo_1() { //查询课程信息
        pn_second.setLayout(null);
        pn_second_1.removeAll();
        pn_second_1.setLayout(null);
        pn_second_1.setBounds(0, 0, 1045, 695);
        pn_second_1.setBorder(BorderFactory.createEtchedBorder());
        JButton bt_query = new JButton("查询", new ImageIcon("image/query.png"));
        bt_query.setFont(new Font("黑体", Font.PLAIN, 17));
        bt_query.setBounds(447, 520, 150, 50);
        bt_query.setContentAreaFilled(false);
        bt_query.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel lb_floorImage = new JLabel(new ImageIcon("image/floor.png"));
        lb_floorImage.setBounds(750, 205, 200, 200);
        pn_second_1.add(bt_query);
        pn_second_1.add(lb_floorImage);
        JLabel[] lb = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            lb[i] = new JLabel();
            lb[i].setFont(new Font("黑体", Font.PLAIN, 25));
            lb[i].setBounds(260, 150 + i * 55, 130, 50);
            pn_second_1.add(lb[i]);
        }
        lb[0].setText("课 程 号：");
        lb[1].setText("课程名：");
        lb[2].setText("教师名：");

        JTextField[] tf = new JTextField[3];
        for (int i = 0; i < 3; i++) {
            tf[i] = new JTextField();
            tf[i].setFont(new Font("黑体", Font.PLAIN, 25));
            tf[i].setBounds(392, 155 + i * 55, 260, 40);
            pn_second_1.add(tf[i]);
        }
        //查询课程信息
        bt_query.addActionListener(e -> {
            String sql = "SELECT cno,cname,tname FROM course,teacher WHERE course.tno=teacher.tno";
            if (!(tf[0].getText().isEmpty() && tf[1].getText().isEmpty() && tf[2].getText().isEmpty())) { //有查询条件
                //String sql = "SELECT cno,cname,tname FROM course,teacher WHERE cno NOT IN (SELECT cno FROM select_course WHERE sno="+stu_num+")"+"AND course.tno=teacher.tno";
                if (!tf[0].getText().isEmpty())
                    sql = sql + " AND course.cno='" + tf[0].getText() + "'";
                if (!tf[1].getText().isEmpty())
                    sql = sql + " AND course.cname='" + tf[1].getText() + "'";
                if (!tf[2].getText().isEmpty())
                    sql = sql + " AND teacher.tname='" + tf[2].getText() + "'";
            }
            queryCourseInfo_2(sql);
        });
        pn_second.removeAll();
        studentJFrame.repaint();
        pn_second.add(pn_second_1);
        studentJFrame.validate();
    }

    public void queryCourseInfo_2(String sql) { //查询宿舍信息结果
        String[] columnNames = {"课程号", "课程名", "教师名", "操作"}; //表格列名
        String[][] rowData = null; //表格数据
        int count = 0; //查询到的宿舍个数
        try {
            PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
            ps = HomePage.connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.TYPE_FORWARD_ONLY); //把操作数据库返回的结果保存到ps中
            ResultSet rs = ps.executeQuery(sql); //ResultSet类，用来存放获取的结果集
            rs.last();
            count = rs.getRow(); //获取查询到的宿舍个数
            if (count == 0) { //若无符合条件的宿舍
                rowData = new String[1][4];
                for (int i = 0; i < 4; i++)
                    rowData[0][i] = "无";
            } else { //若有符合条件的宿舍
                rowData = new String[count][4];
                rs.first();
                int i = 0;
                do { //获取符合条件的宿舍信息
                    rowData[i][0] = rs.getString("cno"); //课程号
                    rowData[i][1] = rs.getString("cname"); //楼层
                    rowData[i][2] = rs.getString("tname"); //教师名
                    i++;
                } while (rs.next());
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "在查询课程信息时发生异常", e);
        }
        //界面
        pn_second_1.removeAll();
        pn_second_1.setBounds(0, 0, 1045, 695);
        pn_second_1.setLayout(null);
        JPanel pn_top = new JPanel();
        pn_top.setBounds(0, 0, 1045, 50);
        JLabel lb_num = new JLabel(" 共查询到" + count + "条课程信息！ ");
        lb_num.setFont(new Font("黑体", Font.PLAIN, 25));
        JButton bt_back = new JButton("返回", new ImageIcon("image/返回.png"));
        bt_back.setFont(new Font("黑体", Font.PLAIN, 17));
        bt_back.setContentAreaFilled(false);
        bt_back.setBorderPainted(false);
        bt_back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton bt_export = new JButton("导出", new ImageIcon("image/导出.png"));
        bt_export.setBackground(Color.green.darker());
        bt_export.setFont(new Font("黑体", Font.PLAIN, 20));
        bt_export.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pn_top.add(bt_back);
        pn_top.add(lb_num);
        pn_top.add(bt_export);
        JTable table = new JTable(new MyTableModel(columnNames, rowData, 9));
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(1, 35)); //设置表头高度
        header.setFont(new Font("黑体", Font.BOLD, 23));
        table.setRowHeight(35); //设置各行高度
        table.setFont(new Font("黑体", Font.PLAIN, 20));
        table.setBackground(null);
        table.getTableHeader().setReorderingAllowed(false); //不允许移动各列
        JScrollPane scrollPane = new JScrollPane(table); //滚动条
        scrollPane.setBounds(0, 50, 1045, 645);
        pn_second_1.add(pn_top);
        pn_second_1.add(scrollPane);
        if (count != 0) {
            MyEvent e = new MyEvent() { //点击“查看”按钮，查看信息，可修改和删除信息
                public void invoke(ActionEvent e) {
                    MyButton button = (MyButton) e.getSource();

                    SelectCourse((String) table.getValueAt(button.getRow(), button.getColumn() - 3));
                }
            };
            table.getColumnModel().getColumn(3).setCellRenderer(new MyButtonRender("选课")); //设置表格的渲染器
            MyButtonEditor editor = new MyButtonEditor(e, "选课");
            table.getColumnModel().getColumn(3).setCellEditor(editor); //设置表格的编辑器
        }
        bt_back.addMouseListener(new MouseListener() { //返回
            public void mouseEntered(MouseEvent arg0) {
                bt_back.setForeground(Color.blue);
            }

            public void mouseExited(MouseEvent arg0) {
                bt_back.setForeground(null);
            }

            public void mouseClicked(MouseEvent arg0) {
                queryCourseInfo_1();
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });
        //将表格导出成Excel文件
        bt_export.addActionListener(e -> {
            FileDialog fd = new FileDialog(studentJFrame, "请设置导出位置和文件名！", FileDialog.SAVE);
            fd.setVisible(true);
            String file = fd.getDirectory() + fd.getFile() + ".xls";
            if (fd.getFile() != null)
                JTableToExcel.export(new File(file), table);
        });
        pn_second.removeAll();
        studentJFrame.repaint();
        pn_second.add(pn_second_1);
        studentJFrame.validate();
    }


    public void SelectCourse(String str) { //操作时弹出的提示选择窗口
        lb_tips.setText("是否选择这门课程");
        JDialog choiceTips = new JDialog(studentJFrame, "  提示", true);
        JPanel pn_tips = new JPanel();
        JButton bt_yes = new JButton("是(Y)");
        JButton bt_no = new JButton("否(N)");
        choiceTips.setSize(500, 200);
        choiceTips.setLocationRelativeTo(null);
        choiceTips.setResizable(false);
        choiceTips.setLayout(null);
        pn_tips.setBounds(0, 30, 500, 70);
        lb_tips.setFont(new Font("黑体", Font.PLAIN, 25));
        bt_yes.setFont(new Font("黑体", Font.PLAIN, 20));
        bt_yes.setBounds(135, 100, 100, 50);
        bt_yes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bt_no.setFont(new Font("黑体", Font.PLAIN, 20));
        bt_no.setBounds(260, 100, 100, 50);
        bt_no.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pn_tips.add(lb_tips);
        choiceTips.add(pn_tips);
        choiceTips.add(bt_yes);
        choiceTips.add(bt_no);
        //选择“是”，进行相应操作
        bt_yes.addActionListener(e -> {
            choiceTips.dispose();
            yesOperation2(str);
        });
        //选择“否”，关闭提示选择窗口
            bt_no.addActionListener(e -> choiceTips.dispose());

        bt_yes.addKeyListener(new KeyListener() { //“是”按钮的快捷键“Y”，“否”按钮的快捷键“N”
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Y) {
                    choiceTips.dispose();
                    yesOperation2(str);
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
                    yesOperation2(str);
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

    public void yesOperation2(String str) { //选择“是”之后，根据提示内容，选择要进行的操作
        String sql = "INSERT IGNORE INTO select_course (sno, cno, mark) VALUES (?, ?, ?)";
        //String sql = "INSERT INTO select_course(sno,cno,grade) VALUES"+"("+"'"+stu_num+"'"+"'"+str+"'"+""+")"; //SQL语句
        PreparedStatement ps;
        try {
            ps = HomePage.connection.prepareStatement(sql);
            ps.setString(1, stu_num);  // 设置第一个参数，对应 SQL 语句中的第一个问号
            ps.setString(2, str);      // 设置第二个参数，对应 SQL 语句中的第二个问号
            ps.setInt(3, 0);  // 设置第三个参数，对应 SQL 语句中的第三个问号，替换为实际的 grade 数据

            int rowsAffected = ps.executeUpdate();  // 执行插入操作，并返回受影响的行数

            if (rowsAffected > 0) {
                System.out.println("插入成功！");
                lb_tips.setText("选课成功！");
                JDialog choiceTips = new JDialog(studentJFrame, "  提示", true);
                JPanel pn_tips = new JPanel();
                JButton bt_yes = new JButton("好的");
                choiceTips.setSize(500, 200);
                choiceTips.setLocationRelativeTo(null);
                choiceTips.setResizable(false);
                choiceTips.setLayout(null);
                pn_tips.setBounds(0, 30, 500, 70);
                lb_tips.setFont(new Font("黑体", Font.PLAIN, 25));
                bt_yes.setFont(new Font("黑体", Font.PLAIN, 20));
                bt_yes.setBounds(190, 100, 100, 50);
                bt_yes.setCursor(new Cursor(Cursor.HAND_CURSOR));
                pn_tips.add(lb_tips);
                choiceTips.add(pn_tips);
                choiceTips.add(bt_yes);
                bt_yes.addActionListener(e -> choiceTips.dispose());
                bt_yes.addKeyListener(new KeyListener() { //“是”按钮的快捷键“Y”，“否”按钮的快捷键“N”
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_Y) {
                            choiceTips.dispose();
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

            } else {
                System.out.println("插入失败！");
                lb_tips.setText("选课失败！请确保课程未被选过");
                JDialog choiceTips = new JDialog(studentJFrame, "  提示", true);
                JPanel pn_tips = new JPanel();
                JButton bt_yes = new JButton("好的");
                choiceTips.setSize(500, 200);
                choiceTips.setLocationRelativeTo(null);
                choiceTips.setResizable(false);
                choiceTips.setLayout(null);
                pn_tips.setBounds(0, 30, 500, 70);
                lb_tips.setFont(new Font("黑体", Font.PLAIN, 25));
                bt_yes.setFont(new Font("黑体", Font.PLAIN, 20));
                bt_yes.setBounds(190, 100, 100, 50);
                bt_yes.setCursor(new Cursor(Cursor.HAND_CURSOR));
                pn_tips.add(lb_tips);
                choiceTips.add(pn_tips);
                choiceTips.add(bt_yes);
                bt_yes.addActionListener(e -> choiceTips.dispose());
                bt_yes.addKeyListener(new KeyListener() { //“是”按钮的快捷键“Y”，“否”按钮的快捷键“N”
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_Y) {
                            choiceTips.dispose();
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

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "在选课时发生异常", e);
        }
        allcourses();

    }


    public JPanel my_course() { //[我的课程]功能
        JPanel tp_mycourse = new JPanel();
        JPanel tp_mycourse_1 = new JPanel();
        tp_mycourse.setLayout(null);
        tp_mycourse_1.setLayout(null);

        tp_mycourse.setBounds(0, 0, 1045, 735);
        tp_mycourse_1.setBounds(0, 0, 1045, 50);
        String[] columnNames = {"学号", "课程号", "分数"}; //表格列名
        String[][] rowData = null; //表格数据
        int count = 0; //表的元组总数
        try { //获取dormitory表信息
            String sql = "SELECT * FROM select_course WHERE sno=" + stu_num; //SQL语句
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
                    rowData[i][0] = rs.getString("sno"); //学号
                    rowData[i][1] = rs.getString("cno"); //课程号
                    rowData[i][2] = rs.getString("mark"); //分数
                    i++;
                } while (rs.next());
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "在查询选课表时发生异常", e);
        }

        JPanel pn_top = new JPanel();
        pn_top.setBounds(0, 0, 1045, 50);
        JLabel lb_num;

        if (count == 0)
            lb_num = new JLabel("  无课程信息（你被退学了！）  ");
        else
            lb_num = new JLabel("你有" + count + "条课程信息~  ");
        lb_num.setFont(new Font("黑体", Font.PLAIN, 25));
        JButton bt_export = new JButton("导出", new ImageIcon("image/导出.png"));

        bt_export.setBackground(Color.green.darker());
        bt_export.setFont(new Font("黑体", Font.PLAIN, 20));
        bt_export.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pn_top.add(lb_num);
        pn_top.add(bt_export);
        JTable table = new JTable(new MyTableModel(columnNames, rowData, 2));
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(1, 35)); //设置表头高度
        header.setFont(new Font("黑体", Font.BOLD, 23));
        table.setRowHeight(35); //设置各行高度
        table.setFont(new Font("黑体", Font.PLAIN, 20));
        table.setBackground(null);
        table.getTableHeader().setReorderingAllowed(false); //不允许移动各列
        JScrollPane scrollPane = new JScrollPane(table); //滚动条
        scrollPane.setBounds(0, 50, 1045, 645);
        tp_mycourse_1.add(pn_top);
        tp_mycourse.add(tp_mycourse_1);
        tp_mycourse.add(scrollPane);
        //将表格导出成Excel文件
        bt_export.addActionListener(e -> {
            FileDialog fd = new FileDialog(studentJFrame, "请设置导出位置和文件名！", FileDialog.SAVE);
            fd.setVisible(true);
            String file = fd.getDirectory() + fd.getFile() + ".xls";
            if (fd.getFile() != null)
                JTableToExcel.stu_export(new File(file), table);
        });
        return tp_mycourse;
    }

    public JPanel repair() { //[宿舍报修]功能
        JPanel pn_repair = new JPanel();
        pn_repair.setLayout(null);
        pn_repair.setSize(1045, 735);
        pn_repair.setBorder(BorderFactory.createEtchedBorder());
        JLabel lb_floor = new JLabel("楼号："), lb_layer = new JLabel("楼层："), lb_room = new JLabel("宿舍号："), lb_info = new JLabel("报修详细：");
        JTextField tf_floor = new JTextField(), tf_layer = new JTextField(), tf_room = new JTextField();
        JTextArea ta_info = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(); //滚动条
        JButton bt = new JButton("提交");
        lb_floor.setFont(new Font("黑体", Font.PLAIN, 25));
        lb_floor.setBounds(197, 190, 125, 50);
        lb_layer.setFont(new Font("黑体", Font.PLAIN, 25));
        lb_layer.setBounds(197, 250, 125, 50);
        lb_room.setFont(new Font("黑体", Font.PLAIN, 25));
        lb_room.setBounds(172, 310, 125, 50);
        lb_info.setFont(new Font("黑体", Font.PLAIN, 25));
        lb_info.setBounds(147, 370, 125, 50);
        tf_floor.setFont(new Font(null, Font.PLAIN, 25));
        tf_floor.setBounds(272, 195, 250, 40);
        tf_layer.setFont(new Font(null, Font.PLAIN, 25));
        tf_layer.setBounds(272, 255, 250, 40);
        tf_layer.setDocument(new NumLimit()); //限制文本框只能输入数字
        tf_room.setFont(new Font(null, Font.PLAIN, 25));
        tf_room.setBounds(272, 315, 250, 40);
        tf_room.setDocument(new NumLimit()); //限制文本框只能输入数字
        ta_info.setFont(new Font(null, Font.PLAIN, 25));
        ta_info.setBounds(272, 375, 500, 160);
        ta_info.setLineWrap(true);
        ta_info.setWrapStyleWord(true);
        scrollPane.setBounds(272, 375, 500, 160);
        scrollPane.setViewportView(ta_info);
        bt.setFont(new Font("黑体", Font.PLAIN, 20));
        bt.setBounds(472, 560, 100, 50);
        bt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel lb_image = new JLabel(new ImageIcon("image/宿舍报修.png"));
        lb_image.setBounds(800, 205, 200, 200);
        pn_repair.add(lb_floor);
        pn_repair.add(lb_layer);
        pn_repair.add(lb_room);
        pn_repair.add(lb_info);
        pn_repair.add(tf_floor);
        pn_repair.add(tf_layer);
        pn_repair.add(tf_room);
        pn_repair.add(scrollPane);
        pn_repair.add(bt);
        pn_repair.add(lb_image);
        //提交宿舍报修
        bt.addActionListener(e -> {
            String floor = tf_floor.getText(), layer = tf_layer.getText(), room = tf_room.getText(), info = ta_info.getText();
            if (floor.isEmpty() || layer.isEmpty() || room.isEmpty() || info.isEmpty()) { //若未填写完整，则进行提示
                lb_tips.setText("请填写完整！");
                functionTips();
            } else { //若填写完整，则检测宿舍是否存在
                try { //查询宿舍是否存在
                    String sql = "SELECT floor_num, layer, room_num FROM dormitory WHERE BINARY floor_num='" + floor + "' AND layer=" + layer + " AND room_num=" + room; //SQL语句
                    PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
                    ps = HomePage.connection.prepareStatement(sql); //把操作数据库返回的结果保存到ps中
                    ResultSet rs = ps.executeQuery(sql); //ResultSet类，用来存放获取的结果集
                    if (!rs.next()) { //若宿舍不存在，则进行提示
                        lb_tips.setText("宿舍不存在！");
                        functionTips();
                    } else { //若宿舍存在，则提交报修
                        sql = "INSERT INTO repair VALUES(?,?,?,?,?,'否')"; //SQL语句
                        ps = HomePage.connection.prepareStatement(sql); //把操作数据库返回的结果保存到ps中
                        ps.setString(1, stu_num); //SQL语句第一个?值
                        ps.setString(2, floor); //SQL语句第二个?值
                        ps.setInt(3, Integer.parseInt(layer)); //SQL语句第三个?值
                        ps.setInt(4, Integer.parseInt(room)); //SQL语句第四个?值
                        ps.setString(5, info); //SQL语句第五个?值
                        ps.executeUpdate(); //更新，执行插入操作
                        tf_floor.setText("");
                        tf_layer.setText("");
                        tf_room.setText("");
                        ta_info.setText("");
                        lb_tips.setText("提交成功，请等待工作人员处理！");
                        functionTips();
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException se) {
                    logger.log(Level.SEVERE, "在提交宿舍报修时发生异常", se);
                }
            }
        });
        return pn_repair;
    }

    public JPanel advise() { //[建议与反馈]功能
        JPanel pn_advise = new JPanel();
        pn_advise.setLayout(null);
        pn_advise.setSize(1045, 735);
        pn_advise.setBorder(BorderFactory.createEtchedBorder());
        JLabel lb_info = new JLabel("建议与反馈：");
        JTextArea ta_info = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(); //滚动条
        JButton bt = new JButton("提交");
        lb_info.setFont(new Font("黑体", Font.PLAIN, 25));
        lb_info.setBounds(172, 217, 150, 50);
        ta_info.setFont(new Font(null, Font.PLAIN, 25));
        ta_info.setBounds(172, 267, 700, 250);
        ta_info.setLineWrap(true);
        ta_info.setWrapStyleWord(true);
        scrollPane.setBounds(172, 267, 700, 250);
        scrollPane.setViewportView(ta_info);
        bt.setFont(new Font("黑体", Font.PLAIN, 20));
        bt.setBounds(472, 555, 100, 50);
        bt.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JLabel lb_image = new JLabel(new ImageIcon("image/建议与反馈.png"));
        lb_image.setBounds(422, 20, 200, 200);
        pn_advise.add(lb_info);
        pn_advise.add(scrollPane);
        pn_advise.add(bt);
        pn_advise.add(lb_image);
        //提交建议与反馈
        bt.addActionListener(e -> {
            try {
                String sql = "INSERT INTO advice VALUES(?,?)"; //SQL语句
                PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
                ps = HomePage.connection.prepareStatement(sql); //把操作数据库返回的结果保存到ps中
                ps.setString(1, stu_num); //SQL语句第一个?值
                ps.setString(2, ta_info.getText()); //SQL语句第二个?值
                ps.executeUpdate(); //更新，执行插入操作
                ta_info.setText("");
                lb_tips.setText("提交成功，感谢您的建议与反馈！");
                functionTips();
                ps.close();
            } catch (SQLException se) {
                logger.log(Level.SEVERE, "在反馈时发生异常", se);
            }
        });
        return pn_advise;
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
        bt_confirm.setFont(new Font("黑体", Font.PLAIN, 20));
        bt_confirm.setBounds(405, 410, 100, 50);
        bt_confirm.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bt_reset.setFont(new Font("黑体", Font.PLAIN, 20));
        bt_reset.setBounds(540, 410, 100, 50);
        bt_reset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pn_changePassword.add(bt_confirm);
        pn_changePassword.add(bt_reset);
        //重置，将密码框内容清空
        bt_reset.addActionListener(e -> {
            pf_old.setText("");
            pf_new1.setText("");
            pf_new2.setText("");
        });
        //确认
        bt_confirm.addActionListener(e -> {
            String password = null;
            if (!((!String.valueOf(pf_old.getPassword()).isEmpty()) //若未填写完整，则进行提示
                    && (!String.valueOf(pf_new1.getPassword()).isEmpty())
                    && (!String.valueOf(pf_new2.getPassword()).isEmpty()))) {
                lb_tips.setText("请填写完整！");
                functionTips();
            } else { //若填写完整，则进行密码判断
                try { //获取原密码
                    String sql = "SELECT password FROM student WHERE stu_num=" + stu_num; //SQL语句
                    PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
                    ps = HomePage.connection.prepareStatement(sql); //把操作数据库返回的结果保存到ps中
                    ResultSet rs = ps.executeQuery(sql); //ResultSet类，用来存放获取的结果集
                    while (rs.next()) { //遍历结果集
                        password = rs.getString("password");
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException se) {
                    logger.log(Level.SEVERE, "在获取原密码时发生异常", e);
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
                        String sql = "UPDATE student set password=? WHERE stu_num=?"; //SQL语句
                        PreparedStatement ps; //创建PreparedStatement类对象ps，用来执行SQL语句
                        ps = HomePage.connection.prepareStatement(sql); //把操作数据库返回的结果保存到ps中
                        ps.setString(1, String.valueOf(pf_new1.getPassword())); //SQL语句第一个?值
                        ps.setString(2, stu_num); //SQL语句第二个?值
                        ps.executeUpdate(); //更新，执行修改操作
                        ps.close();
                    } catch (SQLException se) {
                        logger.log(Level.SEVERE, "在修改新密码时发生异常", se);
                    }
                    pf_old.setText("");
                    pf_new1.setText("");
                    pf_new2.setText("");
                    lb_tips.setText("密码修改成功！");
                    functionTips();
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
        lb_tips.setFont(new Font("黑体", Font.PLAIN, 25));
        bt_tips.setFont(new Font("黑体", Font.PLAIN, 20));
        bt_tips.setBounds(200, 100, 100, 50);
        bt_tips.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pn_tips.add(lb_tips);
        tips.add(pn_tips);
        tips.add(bt_tips);
        bt_tips.addActionListener(e -> tips.dispose());
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
        lb_tips.setFont(new Font("黑体", Font.PLAIN, 25));
        bt_yes.setFont(new Font("黑体", Font.PLAIN, 20));
        bt_yes.setBounds(135, 100, 100, 50);
        bt_yes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bt_no.setFont(new Font("黑体", Font.PLAIN, 20));
        bt_no.setBounds(260, 100, 100, 50);
        bt_no.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pn_tips.add(lb_tips);
        choiceTips.add(pn_tips);
        choiceTips.add(bt_yes);
        choiceTips.add(bt_no);
        //选择“是”，进行相应操作
        bt_yes.addActionListener(e -> {
            choiceTips.dispose();
            yesOperation();
        });
        //选择“否”，关闭提示选择窗口
        bt_no.addActionListener(e -> choiceTips.dispose());
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
        if (lb_tips.getText().equals("是否退出学生模式？")) { //退出学生模式，返回到欢迎界面
            studentJFrame.dispose();
            returnHomepage();
        }
    }
}
