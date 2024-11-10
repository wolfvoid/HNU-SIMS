package sdms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomePage {
    private static final Logger logger = LoggerUtil.getLogger(HomePage.class);
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db-system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWD = "root";

    public static JFrame mainFrame = new JFrame("学校信息管理系统");
    static Container container = mainFrame.getContentPane();
    static Connection connection = null;
    private static int userType;
    private static int appearance = 1;
    private static final JLabel appearanceLabel = new JLabel("<html>当前外观：<br>默认</html>");

    private static final String FONT_NAME = "黑体";


    public static void main(String[] args) {
        initializeMainFrame();
        connectToDatabase();
        welcomePage();
    }

    public static void initializeMainFrame() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        container.setLayout(null);
    }

    private static void switchToLoginPage() {
        container.remove(container.getComponent(0));
        mainFrame.repaint();
        LoginPage();
        mainFrame.validate();
    }

    private static void performLogin(String username, String password) {
        switch (userType) {
            case 1 -> new Login().studentLogin(username, password);
            case 2 -> new Login().dor_adminLogin(username, password);
            case 3 -> new Login().teacherLogin(username, password);
            case 4 -> new Login().adminLogin(username, password);
            default -> System.out.print("Unknown user type");
        }
    }

    public static void welcomePage() {
        mainFrame.setSize(550, 400);
        mainFrame.setLocationRelativeTo(null);
        JPanel panel = createPanel();
        JLabel welcomeLabel = new JLabel("欢迎使用"), systemLabel = new JLabel("学校信息管理系统");
        JButton studentButton = createButton("学生登录"), dormAdminButton = createButton("宿管登录"),
                teacherButton = createButton("教师登录"), adminButton = createButton("管理员登录"),
                changeAppearanceButton = new JButton("更换外观", new ImageIcon("image/更换外观.png"));

        customizeLabel(welcomeLabel, 35, 200, 30, 150, 100);
        customizeLabel(systemLabel, 35, 128, 100, 300, 80);
        customizeLabel(appearanceLabel, 17, 5, 5, 150, 40);

        customizeButton(studentButton, 22, 50, 180, 180, 70);
        customizeButton(dormAdminButton, 22, 300, 180, 180, 70);
        customizeButton(teacherButton, 22, 50, 270, 180, 70);
        customizeButton(adminButton, 22, 300, 270, 180, 70);

        customizeButton(changeAppearanceButton, 17, 420, 10, 130, 25);

        panel.add(welcomeLabel);
        panel.add(systemLabel);
        panel.add(appearanceLabel);
        panel.add(studentButton);
        panel.add(dormAdminButton);
        panel.add(teacherButton);
        panel.add(adminButton);
        panel.add(changeAppearanceButton);
        container.add(panel);
        mainFrame.setVisible(true);

        studentButton.addActionListener(e -> {
            userType = 1;
            switchToLoginPage();
        });

        dormAdminButton.addActionListener(e -> {
            userType = 2;
            switchToLoginPage();
        });

        teacherButton.addActionListener(e -> {
            userType = 3;
            switchToLoginPage();
        });

        adminButton.addActionListener(e -> {
            userType = 4;
            switchToLoginPage();
        });

        changeAppearanceButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent arg0) {
                changeAppearanceButton.setForeground(Color.blue);
            }

            public void mouseExited(MouseEvent arg0) {
                changeAppearanceButton.setForeground(null);
            }

            public void mouseClicked(MouseEvent arg0) {
                changeAppearance();
            }
        });
    }

    private static void customizeLabel(JLabel label, int fontSize, int x, int y, int width, int height) {
        label.setFont(new Font(FONT_NAME, Font.PLAIN, fontSize));
        label.setBounds(x, y, width, height);
    }

    private static void customizeButton(JButton button, int fontSize, int x, int y, int width, int height) {
        button.setFont(new Font(FONT_NAME, Font.PLAIN, fontSize));
        button.setBounds(x, y, width, height);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setSize(550, 400);
        panel.setLayout(null);
        return panel;
    }

    private static JButton createButton(String text) {
        return new JButton(text);
    }

    private static void changeAppearance() {
        try {
            appearance++;
            getLookAndFeel();
            setLookAndFeelAndLabel();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "在更换主题时发生异常", e);
        }
    }

    private static String getLookAndFeel() {
        switch (appearance) {
            case 1 -> {
                return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            }
            case 2 -> {
                return "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
            }
            case 3 -> {
                return "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";
            }
            case 4 -> {
                return "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            }
            case 5 -> {
                return "javax.swing.plaf.metal.MetalLookAndFeel";
            }
            default -> {
                appearance = 1;
                return getLookAndFeel(); // Recursive call to handle the default case
            }
        }
    }

    private static void setLookAndFeelAndLabel() {
        SwingUtilities.updateComponentTreeUI(mainFrame);
        updateAppearanceLabel();
    }

    private static void updateAppearanceLabel() {
        String appearanceText = switch (appearance) {
            case 1 -> "Windows";
            case 2 -> "Nimbus";
            case 3 -> "Windows Classic";
            case 4 -> "Motif";
            case 5 -> "Default";
            default -> "Unknown";
        };

        appearanceLabel.setText("<html>当前外观:<br>" + appearanceText + "</html>");
    }


    private static void LoginPage() {
        mainFrame.setSize(550, 400);
        mainFrame.setLocationRelativeTo(null);
        JPanel panel = createPanel();
        JButton backButton = createButton("返回"), loginButton = createButton("登  录");
        JLabel lb2 = new JLabel("账号："), lb3 = new JLabel("密码：");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JLabel userTypeLabel = createUserTypeLabel();

        customizeLabel(userTypeLabel, 35, 200, 30, 180, 100);
        customizeLabel(lb2, 25, 100, 100, 80, 100);
        customizeLabel(lb3, 25, 100, 150, 80, 100);

        customizeTextField(usernameField);
        customizePasswordField(passwordField);
        customizeButton(loginButton, 25, 200, 250, 150, 60);
        customizeButton(backButton, 17, 1, 10, 92, 25);

        panel.add(userTypeLabel);
        panel.add(lb2);
        panel.add(lb3);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(backButton);
        panel.add(loginButton);
        container.add(panel);

        customizeButtonActionListeners(backButton, loginButton, usernameField, passwordField);
    }

    private static JLabel createUserTypeLabel() {
        JLabel userTypeLabel;
        switch (userType) {
            case 1 -> userTypeLabel = new JLabel("学生登录");
            case 2 -> userTypeLabel = new JLabel("宿管登录");
            case 3 -> userTypeLabel = new JLabel("教师登录");
            case 4 -> userTypeLabel = new JLabel("管理员登录");
            default -> userTypeLabel = new JLabel("什么情况???");
        }
        return userTypeLabel;
    }

    private static void customizeButtonActionListeners(JButton backButton, JButton loginButton, JTextField usernameField, JPasswordField passwordField) {
        backButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent arg0) {
                backButton.setForeground(Color.blue);
            }

            public void mouseExited(MouseEvent arg0) {
                backButton.setForeground(null);
            }

            public void mouseClicked(MouseEvent arg0) {
                container.remove(container.getComponent(0));
                mainFrame.repaint();
                welcomePage();
                mainFrame.validate();
            }
        });

        loginButton.addActionListener(e -> performLogin(usernameField.getText(), String.valueOf(passwordField.getPassword())));
    }

    private static void customizeTextField(JTextField tf) {
        tf.setFont(new Font("黑体", Font.PLAIN, 25));
        tf.setBounds(170, 130, 230, 40);
    }

    private static void customizePasswordField(JPasswordField pf) {
        pf.setFont(new Font(null, Font.PLAIN, 25));
        pf.setBounds(170, 180, 230, 40);
    }

    private static void connectToDatabase() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASSWD);
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "在连接数据库时发生异常", e);
        }
    }

    public static void returnHomepage() {
        HomePage.container.removeAll();
        HomePage.mainFrame.repaint();
        HomePage.welcomePage();
        HomePage.mainFrame.validate();
    }
}

