package sdms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyButtonEditor extends DefaultCellEditor {
    private final MyButton bt;
    private MyEvent event;

    public MyButtonEditor(String operation) {
        super(new JTextField());
        bt = new MyButton(operation);
        bt.setFont(new Font("黑体", 0, 20));
        bt.setForeground(Color.red.darker());
        bt.setContentAreaFilled(false);
        bt.setBorderPainted(false);
        bt.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 将按钮点击事件委托给 MyEvent 处理
        bt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (event != null) {
                    event.invoke(e);
                }
            }
        });
    }

    public MyButtonEditor(MyEvent e, String operation) {
        this(operation);
        this.event = e;
    }

    // 重写编辑器方法，返回一个按钮给 JTable
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        setClickCountToStart(0); // 点击1次触发事件
        // 将这个被点击的按钮所在的行和列放进 button 里面
        bt.setRow(row);
        bt.setColumn(column);
        return bt;
    }
}
