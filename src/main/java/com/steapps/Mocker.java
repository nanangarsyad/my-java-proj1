package com.steapps;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import com.steapps.Constant.ThreeChoice;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

public class Mocker {

	private JFrame frame;
	private JTextField textFieldCodeTruck;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mocker window = new Mocker();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Mocker() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 673, 822);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel b2PanelPostDetailGui  = new JPanel();
		JScrollPane _b2ScrollPanePostDetailGui = new JScrollPane(b2PanelPostDetailGui);
		b2PanelPostDetailGui.setLayout(new BoxLayout(b2PanelPostDetailGui, BoxLayout.Y_AXIS));
		
		JPanel panelCodeTruck = new JPanel();
		b2PanelPostDetailGui.add(panelCodeTruck);
		panelCodeTruck.setLayout(new MigLayout("", "[125.00][grow]", "[]"));
		
		JLabel lblCodeTruck = new JLabel("Code Truck");
		panelCodeTruck.add(lblCodeTruck, "cell 0 0,alignx leading");
		
		textFieldCodeTruck = new JTextField();
		panelCodeTruck.add(textFieldCodeTruck, "cell 1 0,growx");
		textFieldCodeTruck.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		b2PanelPostDetailGui.add(panel_4);
		
		frame.getContentPane().add(_b2ScrollPanePostDetailGui);
	}

}
