package sdms;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login { //登录类

    private static final Logger logger = LoggerUtil.getLogger(Login.class);
    private static final String FONT_NAME = "黑体";
    private static final int FONT_SIZE_LABEL = 25;
    private static final int FONT_SIZE_BUTTON = 20;
    JLabel lb_tips = new JLabel(); //提示窗口的内容

    public void login(String account, String password, String tableName, String idColumn, String passwordColumn, Consumer<String> successFunction) {
        boolean flag = false;
        String id = null, pass;
        try {
            String sql = "SELECT " + idColumn + ", " + passwordColumn + " FROM " + tableName + " WHERE " + idColumn + " = ? AND " + passwordColumn + " = ?";
            PreparedStatement ps = HomePage.connection.prepareStatement(sql);
            ps.setString(1, account);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getString(idColumn);
                pass = rs.getString(passwordColumn);
                if (account.equals(id) && password.equals(pass)) {
                    flag = true;
                    break;
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "在登录时发生异常", e);
        }
        if (flag) {
            lb_tips.setText("登录成功！");
            showLoginTips();
            successFunction.accept(id);
            HomePage.mainFrame.dispose();
        } else {
            lb_tips.setText("账号或密码错误！");
            showLoginTips();
        }
    }

    public void studentLogin(String account, String password) {
        login(account, password, "student", "stu_num", "password", StudentFunction::new);
    }

    public void dor_adminLogin(String account, String password) {
        login(account, password, "admin", "admin_num", "password", DorAdminFunction::new);
    }

    public void teacherLogin(String account, String password) {
        login(account, password, "teacher", "tno", "password", TeacherFunction::new);
    }

    public void adminLogin(String account, String password) {
        if (account.equals("admin") && password.equals("admin")) {
            lb_tips.setText("登录成功！");
            showLoginTips();
            new SysAdminFunction();
            HomePage.mainFrame.dispose();
        }
    }

    private void showLoginTips() {
        JDialog tips = createTipsDialog();
        JButton bt_tips = createOkButton();

        tips.add(createTipsPanel());
        tips.add(bt_tips);

        bt_tips.addActionListener(e -> tips.dispose());

        tips.setVisible(true);
    }

    private JDialog createTipsDialog() {
        JDialog tips = new JDialog(HomePage.mainFrame, "  提示", true);
        tips.setSize(450, 200);
        tips.setLocationRelativeTo(null);
        tips.setResizable(false);
        tips.setLayout(null);
        return tips;
    }

    private @NotNull JPanel createTipsPanel() {
        JPanel pn_tips = new JPanel();
        pn_tips.setBounds(0, 30, 450, 70);
        lb_tips.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE_LABEL));
        pn_tips.add(lb_tips);
        return pn_tips;
    }

    private JButton createOkButton() {
        JButton bt_tips = new JButton("确定");
        bt_tips.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE_BUTTON));
        bt_tips.setBounds(175, 100, 100, 50);
        bt_tips.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return bt_tips;
    }

}