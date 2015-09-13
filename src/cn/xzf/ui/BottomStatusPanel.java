package cn.xzf.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

import cn.xzf.ui.listener.BottomStatusPanelListener;

public class BottomStatusPanel extends JPanel implements BottomStatusPanelListener{
	private static final long serialVersionUID = -2969206051513681720L;
	private JLabel label_statusMessage;
	private JLabel label_time;
	private JProgressBar progressBar;

	public BottomStatusPanel() {
		init();
	}

	private void init() {
		JLabel lblNewLabel_1 = new JLabel("温馨提示：");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);

		label_statusMessage = new JLabel("欢迎使用XXX系统");

		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);

		label_time = new JLabel("");
		label_time.setHorizontalAlignment(SwingConstants.CENTER);
		label_time.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));

		JSeparator separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);

		progressBar = new JProgressBar();
		progressBar.setVisible(false);

		/*JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);*/
		GroupLayout gl__bottom_panel = new GroupLayout(this);
		gl__bottom_panel.setHorizontalGroup(
			gl__bottom_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl__bottom_panel.createSequentialGroup()
					.addGap(8)
					.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(label_statusMessage, GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
					.addGap(10)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE)
					.addGap(6)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(label_time, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(5))
		);
		gl__bottom_panel.setVerticalGroup(
			gl__bottom_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl__bottom_panel.createSequentialGroup()
					.addGap(6)
					.addComponent(lblNewLabel_1))
				.addGroup(gl__bottom_panel.createSequentialGroup()
					.addGap(1)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl__bottom_panel.createSequentialGroup()
					.addGap(6)
					.addComponent(label_statusMessage, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl__bottom_panel.createSequentialGroup()
					.addGap(1)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl__bottom_panel.createSequentialGroup()
					.addGap(7)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl__bottom_panel.createSequentialGroup()
					.addGap(1)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl__bottom_panel.createSequentialGroup()
					.addGap(1)
					.addComponent(label_time, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
		);
		setLayout(gl__bottom_panel);

		Timer timer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isDisplayable()) {
					return;
				}
				showTime();
			}
		});
		timer.start();

	}

	private void showTime() {
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		label_time.setText(sdf.format(dt));
	}
	
	@Override
	public void showStatusMessage(String message) {
		if (message == null) {
			message = "欢迎使用XXX系统";
		}
		label_statusMessage.setText(message);
	}

	@Override
	public void showStatusProcessBar() {
		progressBar.setVisible(true);
		progressBar.setIndeterminate(true);
	}

	@Override
	public void hideStatusProcessBar() {
		progressBar.setVisible(false);
	}
	
}
