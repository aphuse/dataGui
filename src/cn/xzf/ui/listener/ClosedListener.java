package cn.xzf.ui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClosedListener extends WindowAdapter {
	private String message;
	private String title;
	private Action closeAction;
	private boolean disposeOnClose = false;

	public ClosedListener(String message, String title) {
		this(message, title, null);
	}

	public ClosedListener(Action closeAction) {
		this(null, null, closeAction);
	}

	public ClosedListener(String message, String title, Action closeAction) {
		this.message = message;
		this.title = title;
		this.closeAction = closeAction;
	}

	public void setDisposeOnClose(boolean disposeOnClose) {
		this.disposeOnClose = disposeOnClose;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		JFrame frame = (JFrame) e.getComponent();

		if (!confirmWindowClosing(frame)) {
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			return;
		}

		if (disposeOnClose)
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		else
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (closeAction != null) {
			ActionEvent ae = new ActionEvent(frame,
					ActionEvent.ACTION_PERFORMED, "");
			closeAction.actionPerformed(ae);
		}
	}

	private boolean confirmWindowClosing(JFrame frame) {
		if (message == null)
			return true;

		int result = JOptionPane.showConfirmDialog(frame, message, title,
				JOptionPane.YES_NO_OPTION);

		return (result == JOptionPane.YES_OPTION) ? true : false;
	}
}
