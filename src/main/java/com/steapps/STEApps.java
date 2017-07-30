package com.steapps;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.json.JSONObject;

import com.alee.laf.WebLookAndFeel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.steapps.Constant.ThreeChoice;
import com.steapps.Post.PostEntry;

import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;


class ShowWaitAction extends AbstractAction {
	   protected static final long SLEEP_TIME = 3 * 1000;

	   public ShowWaitAction(String name) {
	      super(name);
	   }

	   @Override
	   public void actionPerformed(ActionEvent evt) {
	      SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>(){
	         @Override
	         protected Void doInBackground() throws Exception {

	            // mimic some long-running process here...
            Thread.sleep(SLEEP_TIME);
            return null;
         }
      };

      Window win = SwingUtilities.getWindowAncestor((AbstractButton)evt.getSource());
      final JDialog dialog = new JDialog(win, "Dialog", ModalityType.APPLICATION_MODAL);

      mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {

         @Override
         public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("state")) {
               if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
                  dialog.dispose();
               }
            }
         }
      });
      mySwingWorker.execute();

      JProgressBar progressBar = new JProgressBar();
      progressBar.setIndeterminate(true);
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(progressBar, BorderLayout.CENTER);
      panel.add(new JLabel("Please wait......."), BorderLayout.PAGE_START);
      dialog.add(panel);
      dialog.pack();
      dialog.setLocationRelativeTo(win);
      dialog.setVisible(true);
   }
}

public class STEApps {
	
	private static final String PANEL_MAIN = "panel-main";
	private static final String PANEL_LOGIN = "panel-login";
	
	private JFrame frmSteapps;
	private JTextPane b4TextPaneUserList;
	private JList b4JlistUserList;	
	private JTextField b4TextFieldNama;
	private JTextField b4TextFieldUserName;
	private JTextField b4TextFieldIdCard;
	private JTextField b4TextFieldTTL;
	private JTextField b4TextFieldTanggaLMasuk;
	private JTextField b4TextFieldEmail;
	private JTextField b4TextFieldNoTelp;
	private JTextArea b3TextAreaUserDetailRaw;
	private JList<PostEntry> b2JlistPost;
	private JTextArea b2TextAreaPostRaw;
	private JCheckBox f_chckbxHelm;
	private JCheckBox f_chckbxSafetyShoes;
	private JCheckBox f_chckbxSafetyGlasses;
	private JCheckBox f_chckbxBodyVest;
	private JCheckBox f_chckbxSarungTangan;
	private JCheckBox f_chckbxDuskMask;
	private JCheckBox f_chckbxApar;
	private JCheckBox f_chckbxSkop;
	private JCheckBox f_chckbxSapuLidi;
	private JCheckBox f_chckbxSawDust;
	private JCheckBox f_chckbxGps;
	private JCheckBox f_chckbxLampuRotary;
	private JCheckBox f_chckbxRambuPortable;
	private JCheckBox f_chckbxKerucutPengaman;
	private JCheckBox f_chckbxSegtigaPengaman;
	private JCheckBox f_chckbxDongkrak;
	private JCheckBox f_chckbxPitaPembatas;
	private JCheckBox f_chckbxGanjalRoda;
	private JCheckBox f_chckbxKotakObat;
	private JComboBox f_comboBoxKanan;
	private JComboBox f_comboBoxKiri;
	private JComboBox f_comboBoxDepan;
	private JComboBox f_comboBoxBelakang;
	private JTextField f_textFieldIjinB3;
	private JTextField f_textFieldSim;
	private JTextField f_textFieldKir;
	private JTextField f_textFieldStnk;
	private JTextField f_textFieldUjiEmisi;
	private JTextField f_textFieldKetDokter;
	private JCheckBox f_chckbxSertifikat;
	private JCheckBox f_chckbxIdCard;
	private JTextField f_textFieldCodeTruck;
	private JTextPane b2JTextPanePostDetailRaw;
	private JButton btnLogin;
	private JPasswordField textFieldAdminPass;
	private JTextField textFieldAdminName;
	private JToolBar b2JToolBarPostGui;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		FB.setupFirebase();		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IconFontSwing.register(FontAwesome.getIconFont());
					WebLookAndFeel.install();
					STEApps window = new STEApps();
					window.frmSteapps.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public STEApps() {
		initialize();		
		setupGui();
	}
	
	private static void testFirebase() {
		FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
			
			public void onDataChange(DataSnapshot arg0) {				
				System.out.println(arg0.getValue());
			}
			
			public void onCancelled(DatabaseError arg0) {
				System.out.println("Failed " + arg0.getCode());
				
			}
		});
	}
	
	private void setupGui() {
		
		// Login GUI
		btnLogin.setAction(new ShowWaitAction("Log in"));	
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				
				FB.listenToValueForSpecificPath(btnLogin, "/admins/", (success, snapshot, error) -> {
					FB.unlistenToValueForSpecificPath(btnLogin, "/admins/", false);
					
					String adminName = textFieldAdminName.getText();
					String adminPass = textFieldAdminPass.getText();
					
					textFieldAdminName.setText(null);
					textFieldAdminPass.setText(null);
					
					if (success) {
						for (DataSnapshot snapChild : snapshot.getChildren()) {
							Map<String, String> map = (Map<String, String>) snapChild.getValue();
							if (adminName.equals(map.get(DBKey.ADMIN_USERNAME)) 
									&& adminPass.equals(map.get(DBKey.ADMIN_PASSWORD))) {
								
								// Show main panel
								SwingUtilities.invokeLater(() -> {									
									((CardLayout)frmSteapps.getContentPane().getLayout()).show(frmSteapps.getContentPane(), PANEL_MAIN);									
								});
								return;
								
							}
						}
					}
					
					SwingUtilities.invokeLater(() -> {						
						JOptionPane.showMessageDialog(frmSteapps, "Failed. Either Username or Password is Wrong", "Warning", JOptionPane.WARNING_MESSAGE);
					});
					
				});
			}
		});
		
		
		// request user list to "use list panel"
		FB.listenToUserList(b4JlistUserList);
		
		// Listen  to Single click on user list
		// by updating the user info panel
		EventHandler.setSingleClickListener(b4JlistUserList, (Map<String, Object> params) -> {
			String sel = (String) b4JlistUserList.getSelectedValue();			
			
			// Update the user info detail by sending request to Firebase Server
			FB.listenToValueForSpecificPath(b4TextFieldNama, "/users/"+sel, (success, snapshot, error) -> {
				final Map<String, String> asMap  = new HashMap<>();
				final StringWriter asString  = new StringWriter();
				
				if (success) {
					asMap.putAll((Map<String, String>) snapshot.getValue()); 
					asString.append(new JSONObject(asMap).toString(4));					
				} else {					
					JSONObject json = new JSONObject();
					json.put("error", error.getMessage());
					asString.append(json.toString(4));
					
				
				}
				
				SwingUtilities.invokeLater(() -> {
					if (success) {
						b4TextFieldNama.setText(asMap.get(DBKey.USER_FULLNAME));
						b4TextFieldUserName.setText(asMap.get(DBKey.USER_USERNAME));
						b4TextFieldIdCard.setText(asMap.get(DBKey.USER_IDCARD));
						b4TextFieldTTL.setText(asMap.get(DBKey.USER_TTL));
						b4TextFieldTanggaLMasuk.setText(asMap.get(DBKey.USER_TANGGAL_MASUK));
						b4TextFieldNoTelp.setText(asMap.get(DBKey.USER_PHONE));
						b4TextFieldEmail.setText(asMap.get(DBKey.USER_EMAIL));
					} else {
						final String str = "ERROR";
						b4TextFieldNama.setText(str);
						b4TextFieldUserName.setText(str);
						b4TextFieldIdCard.setText(str);
						b4TextFieldTTL.setText(str);
						b4TextFieldTanggaLMasuk.setText(str);
						b4TextFieldNoTelp.setText(str);
						b4TextFieldEmail.setText(str);
					}
					
					
					b3TextAreaUserDetailRaw.setText(asString.toString());
				});
			});
		});
		
		
		// Listen  to double click on user list
		// by updating the user post list		
		EventHandler.setDoubleClickListener(b4JlistUserList, (Map<String, Object> params) -> {
			String sel = (String) b4JlistUserList.getSelectedValue();			
			
			FB.listenToValueForSpecificPath(
				b2JlistPost, "/forms/" + sel, (boolean success, DataSnapshot snapshot, DatabaseError error) -> {					
					
					final Map<String, String> asMap  = new HashMap<>();
					final StringWriter asString  = new StringWriter();					
					
					b2JlistPost.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);				
					DefaultListModel<Post.PostEntry> listModel = new DefaultListModel<>();					
					
					if (snapshot.getChildrenCount() == 0) {
						listModel.removeAllElements();
					} else {
						
						if (success) {
							asMap.putAll((Map<String, String>) snapshot.getValue()); 
							asString.append(new JSONObject(asMap).toString(4));					
						} else {					
							JSONObject json = new JSONObject();
							json.put("error", error.getMessage());
							asString.append(json.toString(4));						
						}
						
						for (DataSnapshot snap: snapshot.getChildren()) {
							PostEntry entry = new Post.PostEntry(sel, snap.getKey());
							listModel.addElement(entry);
						}
					}				
					
					
					SwingUtilities.invokeLater(() -> {												
						b2JlistPost.setModel(listModel);												
						b2JlistPost.revalidate();
						b2JlistPost.repaint();
						if (b2JlistPost.getModel().getSize() != 0) {
							b2TextAreaPostRaw.setText(asString.toString());
						} else {
							b2TextAreaPostRaw.setText(null);
						}
						
					});
			});
		});
		
		EventHandler.setSingleClickListener(b2JlistPost, (Map<String, Object> params) -> {
			String selUser = (String) b4JlistUserList.getSelectedValue();
			PostEntry selPost = (PostEntry) b2JlistPost.getSelectedValue();
			FB.listenToValueForSpecificPath(f_chckbxHelm, "/forms/" + selUser + "/" + selPost.asTime, 
				(boolean success, DataSnapshot snapshot, DatabaseError error) -> {
					final Map<String, Object> asMap  = new HashMap<>();
					final StringWriter asString  = new StringWriter();					
					
					b2JlistPost.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);				
					DefaultListModel<String> listModel = new DefaultListModel<String>();	
					
								
					if (success) {
						asMap.putAll((Map<String, String>) snapshot.getValue()); 
						asString.append(new JSONObject(asMap).toString(4));					
					} else {					
						JSONObject json = new JSONObject();
						json.put("error", error.getMessage());
						asString.append(json.toString(4));						
					}
					
					SwingUtilities.invokeLater(() -> {
						if (success) {
							updatePostDetailForm(asMap, null, asString.toString());
						} else {							
							updatePostDetailForm(asMap, "ERROR", asString.toString());
						}												
						
					});
				}
			);
		}); 
		
		FB.listenToValueForSpecificPath(b4TextPaneUserList, "/users/", (success, snapshot, error) -> {
			
			StringWriter str = new StringWriter(); 
			if (success) {		
				str.append(new JSONObject((HashMap<String, Object>)snapshot.getValue()).toString(4));				
								
			} else {
				JSONObject json = new JSONObject();
				json.put("error", error.getMessage());
				str.append(json.toString(4));				
			}			
			SwingUtilities.invokeLater(() -> b4TextPaneUserList.setText(str.toString()));
			
		});
		
		setupPostGuiToolbar();
		
	}
	
	private void setupPostGuiToolbar() {		
		JButton buttonDelete = new JButton(IconFontSwing.buildIcon(FontAwesome.TIMES_CIRCLE, 20));
		buttonDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {				
				PostEntry selPost = (PostEntry) b2JlistPost.getSelectedValue();
				int selIndex = b2JlistPost.getSelectedIndex();
				FB.removeChild("/forms/" + selPost.userName+ "/" + selPost.asTime, (succes, error, ref) -> {
					((DefaultListModel<PostEntry>)b2JlistPost.getModel()).remove(selIndex);
				});
			}
		});
		b2JToolBarPostGui.add(buttonDelete);
		
	}

	private void updatePostDetailForm(Map<String, Object> map, String mapDefValue ,String json) {
		// FROM APD DRIVER
		f_chckbxHelm.setSelected((Boolean)map.get(DBKey.FORM_APD_HELM));
		f_chckbxSafetyShoes.setSelected((Boolean)map.get(DBKey.FORM_APD_SAFETY_SHOES));
		f_chckbxSafetyGlasses.setSelected((Boolean)map.get(DBKey.FORM_APD_SAFETY_GLASSES));
		f_chckbxBodyVest.setSelected((Boolean)map.get(DBKey.FORM_APD_BODY_VEST));
		f_chckbxSarungTangan.setSelected((Boolean)map.get(DBKey.FORM_APD_SARUNG_TANGAN));
		f_chckbxDuskMask.setSelected((Boolean)map.get(DBKey.FORM_APD_DUSK_MASK));
		
		// FROM KELENGKAPAN KENDARAAN
		f_chckbxApar.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_APAR));
		f_chckbxSkop.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_SPILL_SKOP));
		f_chckbxSapuLidi.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_SPILL_SAPU_LIDI));
		f_chckbxSawDust.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_SPILL_SAW_DUST));
		f_chckbxGps.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_GPS));
		f_chckbxLampuRotary.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_LAMPU_ROTARY));
		f_chckbxRambuPortable.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_RAMBU_PORTABLE));
		f_chckbxKerucutPengaman.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_KERUCUT_PENGAMAN));
		f_chckbxSegtigaPengaman.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_SEGITIGA_PENGAMAN));
		f_chckbxDongkrak.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_DONGKRAK));
		f_chckbxPitaPembatas.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_PITA_PEMBATAS));
		f_chckbxGanjalRoda.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_GANJAL_RODA));
		f_chckbxKotakObat.setSelected((Boolean)map.get(DBKey.FORM_KELENGKAPAN_KOTAK_OBAT));
		
		// FROM PLACARD
		f_comboBoxKanan.setSelectedIndex(((Long)map.get(DBKey.FORM_PLACARD_KANAN)).intValue());
		f_comboBoxKiri.setSelectedIndex(((Long)map.get(DBKey.FORM_PLACARD_KIRI)).intValue());
		f_comboBoxDepan.setSelectedIndex(((Long)map.get(DBKey.FORM_PLACARD_DEPAN)).intValue());
		f_comboBoxBelakang.setSelectedIndex(((Long)map.get(DBKey.FORM_PLACARD_BELAKANG)).intValue());
		
		// FROM SURAT DAN MASA BERLAKU
		f_textFieldIjinB3.setText((String) map.get(DBKey.FORM_SRT_BERLAKU_IJIN_B3));
		f_textFieldSim.setText((String) map.get(DBKey.FORM_SRT_BERLAKU_SIM));
		f_textFieldKir.setText((String) map.get(DBKey.FORM_SRT_BERLAKU_KIR));
		f_textFieldStnk.setText((String) map.get(DBKey.FORM_SRT_BERLAKU_STNK));
		f_textFieldUjiEmisi.setText((String) map.get(DBKey.FORM_SRT_BERLAKU_UJI_EMISI));
		f_textFieldKetDokter.setText((String) map.get(DBKey.FORM_SRT_BERLAKU_KET_DOKTER));
		f_chckbxSertifikat.setSelected((Boolean)map.get(DBKey.FORM_SRT_BERLAKU_SERTIFIKAT));
		f_chckbxIdCard.setSelected((Boolean)map.get(DBKey.FORM_SRT_BERLAKU_IDCARD));
		
		// FROM CODE TRUCK
		f_textFieldCodeTruck.setText((String) map.get(DBKey.FORM_TRUCK_CODE));
		
		b2JTextPanePostDetailRaw.setText(new JSONObject(json).toString(4));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		
		frmSteapps = new JFrame();
		frmSteapps.setTitle("STEApps");
		frmSteapps.setResizable(false);
		frmSteapps.setBounds(100, 100, 1080, 720);
		frmSteapps.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSteapps.getContentPane().setLayout(new CardLayout());
		
		ImageIcon iconApps = new ImageIcon(classLoader.getResource("ico.png"));
		frmSteapps.setIconImage(iconApps.getImage());
		
		
		JPanel panelLogin = new JPanel();
		JPanel panelMain = new JPanel();
		
		frmSteapps.getContentPane().add(panelLogin, PANEL_LOGIN);
		panelLogin.setLayout(new BorderLayout(0, 0));
		
		frmSteapps.getContentPane().add(panelMain, PANEL_MAIN);
		panelMain.setLayout(null);
		
		JPanel _panelLogin = new JPanel();
		_panelLogin.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelLogin.add(_panelLogin, BorderLayout.CENTER);
		_panelLogin.setLayout(new MigLayout("", "[grow][108.00][171.00][grow]", "[grow][][][][][][][grow]"));		
		
		ImageIcon icon = new ImageIcon(classLoader.getResource("logo.png"));
		JLabel lblLogo = new JLabel(icon);
		_panelLogin.add(lblLogo, "cell 1 1 2 1,alignx center");
		
		JLabel lblAdminName = new JLabel("Admin Name");
		_panelLogin.add(lblAdminName, "cell 1 3,alignx leading");
		
		textFieldAdminName = new JTextField();
		_panelLogin.add(textFieldAdminName, "cell 2 3,growx");
		textFieldAdminName.setColumns(10);
		
		JLabel lblAdminPass = new JLabel("Password");
		_panelLogin.add(lblAdminPass, "cell 1 4,alignx leading");
		
		textFieldAdminPass = new JPasswordField();
		_panelLogin.add(textFieldAdminPass, "cell 2 4,growx");
		textFieldAdminPass.setColumns(10);
		
		btnLogin = new JButton("Login");
		btnLogin.setMinimumSize(new Dimension(150, 23));
		
		_panelLogin.add(btnLogin, "cell 1 6 2 1,alignx center");		
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1074, 23);
        //ImageIcon icon = new ImageIcon("exit.png");

        JMenu menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);

        JMenuItem menuItemLogout = new JMenuItem("Logout");
        menuItemLogout.setMnemonic(KeyEvent.VK_L);
        menuItemLogout.setToolTipText("Logout from admin");
        menuItemLogout.addActionListener((ActionEvent event) -> {
        	JPanel parent = (JPanel) frmSteapps.getContentPane();
            ((CardLayout)parent.getLayout()).show(parent, PANEL_LOGIN);
        });
        
        menuFile.add(menuItemLogout);
        
        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.setMnemonic(KeyEvent.VK_E);
        menuItemExit.setToolTipText("Exit application");
        menuItemExit.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
        menuFile.add(menuItemExit);

        menuBar.add(menuFile);

		panelMain.add(menuBar);
		
		JTabbedPane b1TabPaneUserPost = new JTabbedPane(JTabbedPane.BOTTOM);
		b1TabPaneUserPost.setBorder(new TitledBorder(null, "User Post", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		b1TabPaneUserPost.setBounds(10, 32, 502, 382);
		panelMain.add(b1TabPaneUserPost);		
		
		b2JlistPost  = new JList<Post.PostEntry>();
		b2JlistPost.setCellRenderer(new Post.PostRenderer());
		JScrollPane _b2JScrollPostGui = new JScrollPane(b2JlistPost);
		b1TabPaneUserPost.addTab("Graphical", null, _b2JScrollPostGui, null);
		
		b2JToolBarPostGui = new JToolBar(JToolBar.VERTICAL);		
		_b2JScrollPostGui.setRowHeaderView(b2JToolBarPostGui);
		
		
		b2TextAreaPostRaw = new JTextArea();
		JScrollPane _b2JScrollPostRaw = new JScrollPane(b2TextAreaPostRaw);
		b1TabPaneUserPost.addTab("Raw", null, _b2JScrollPostRaw, null);		
		
		
		JTabbedPane b1TabPanelPostDetail = new JTabbedPane();
		b1TabPanelPostDetail.setBorder(new TitledBorder(null, "Post Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		b1TabPanelPostDetail.setTabPlacement(JTabbedPane.BOTTOM);
		b1TabPanelPostDetail.setBounds(522, 32, 542, 382);
		panelMain.add(b1TabPanelPostDetail);
		
		JPanel b2PanelPostDetailGui  = new JPanel();
		JScrollPane _b2ScrollPanePostDetailGui = new JScrollPane(b2PanelPostDetailGui);
		_b2ScrollPanePostDetailGui.getVerticalScrollBar().setUnitIncrement(25);
		b2PanelPostDetailGui.setLayout(new BoxLayout(b2PanelPostDetailGui, BoxLayout.Y_AXIS));
		
		JPanel panelApd = new JPanel();
		panelApd.setBorder(new TitledBorder(null, "Kelengkapan APD Driver", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		b2PanelPostDetailGui.add(panelApd);
		panelApd.setLayout(new MigLayout("", "[125.00][grow]", "[][][][][][]"));
		
		JLabel lblHelm = new JLabel("Helm");
		panelApd.add(lblHelm, "cell 0 0");
		
		f_chckbxHelm = new JCheckBox("Ada");
		panelApd.add(f_chckbxHelm, "cell 1 0");
		
		JLabel lblSafetyShoes = new JLabel("Safety Shoes");
		panelApd.add(lblSafetyShoes, "cell 0 1");
		
		f_chckbxSafetyShoes = new JCheckBox("Ada");
		panelApd.add(f_chckbxSafetyShoes, "cell 1 1");
		
		JLabel lblSafetyGlasses = new JLabel("Safety Glasses");
		panelApd.add(lblSafetyGlasses, "cell 0 2");
		
		f_chckbxSafetyGlasses = new JCheckBox("Ada");
		panelApd.add(f_chckbxSafetyGlasses, "cell 1 2");
		
		JLabel lblBodyVest = new JLabel("Body Vest");
		panelApd.add(lblBodyVest, "cell 0 3");
		
		f_chckbxBodyVest = new JCheckBox("Ada");
		panelApd.add(f_chckbxBodyVest, "cell 1 3");
		
		JLabel lblSarungTangan = new JLabel("Sarung Tangan");
		panelApd.add(lblSarungTangan, "cell 0 4");
		
		f_chckbxSarungTangan = new JCheckBox("Ada");
		panelApd.add(f_chckbxSarungTangan, "cell 1 4");
		
		JLabel lblDuskMask = new JLabel("Dusk mask");
		panelApd.add(lblDuskMask, "cell 0 5");
		
		f_chckbxDuskMask = new JCheckBox("Ada");
		panelApd.add(f_chckbxDuskMask, "cell 1 5");
		
		JPanel panelKelengkapanKendaraan = new JPanel();
		panelKelengkapanKendaraan.setBorder(new TitledBorder(null, "Kelengkapan Kendaraan", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		b2PanelPostDetailGui.add(panelKelengkapanKendaraan);
		panelKelengkapanKendaraan.setLayout(new MigLayout("", "[125.00,leading][grow]", "[][grow][][][][]"));
		
		JLabel lblApar = new JLabel("Apar");
		panelKelengkapanKendaraan.add(lblApar, "cell 0 0");
		
		f_chckbxApar = new JCheckBox("Ada");
		panelKelengkapanKendaraan.add(f_chckbxApar, "cell 1 0");
		
		JPanel panelSpillKits = new JPanel();
		panelSpillKits.setBorder(new TitledBorder(null, "Spill Kits", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelKelengkapanKendaraan.add(panelSpillKits, "cell 0 1 2 1,grow");
		panelSpillKits.setLayout(new MigLayout("", "[112.00][grow]", "[][][]"));
		
		JLabel lblSkop = new JLabel("Skop");
		panelSpillKits.add(lblSkop, "cell 0 0");
		
		f_chckbxSkop = new JCheckBox("Ada");
		panelSpillKits.add(f_chckbxSkop, "cell 1 0");
		
		JLabel lblSapuLidi = new JLabel("Sapu Lidi");
		panelSpillKits.add(lblSapuLidi, "cell 0 1");
		
		f_chckbxSapuLidi = new JCheckBox("Ada");
		panelSpillKits.add(f_chckbxSapuLidi, "cell 1 1");
		
		JLabel lblSawDust = new JLabel("Saw Dust");
		panelSpillKits.add(lblSawDust, "cell 0 2");
		
		f_chckbxSawDust = new JCheckBox("Ada");
		panelSpillKits.add(f_chckbxSawDust, "cell 1 2");
		
		JLabel lblGps = new JLabel("GPS");
		panelKelengkapanKendaraan.add(lblGps, "cell 0 2");
		
		f_chckbxGps = new JCheckBox("Ada");
		panelKelengkapanKendaraan.add(f_chckbxGps, "cell 1 2");
		
		JLabel lblLampuRotary = new JLabel("Lampu Rotary");
		panelKelengkapanKendaraan.add(lblLampuRotary, "cell 0 3");
		
		f_chckbxLampuRotary = new JCheckBox("Ada");
		panelKelengkapanKendaraan.add(f_chckbxLampuRotary, "cell 1 3");
		
		JLabel lblRambuPortable = new JLabel("Rambu Portable");
		panelKelengkapanKendaraan.add(lblRambuPortable, "cell 0 4");
		
		f_chckbxRambuPortable = new JCheckBox("Ada");
		panelKelengkapanKendaraan.add(f_chckbxRambuPortable, "cell 1 4");
		
		JLabel lblKerucutPengaman = new JLabel("Kerucut Pengaman");
		panelKelengkapanKendaraan.add(lblKerucutPengaman, "cell 0 5");
		
		f_chckbxKerucutPengaman = new JCheckBox("Ada");
		panelKelengkapanKendaraan.add(f_chckbxKerucutPengaman, "cell 1 5");
		
		JLabel lblSegitigaPengaman = new JLabel("Segitiga Pengaman");
		panelKelengkapanKendaraan.add(lblSegitigaPengaman, "cell 0 6");
		
		f_chckbxSegtigaPengaman = new JCheckBox("Ada");
		panelKelengkapanKendaraan.add(f_chckbxSegtigaPengaman, "cell 1 6");
		
		JLabel lblDongkrak = new JLabel("Dongkrak");
		panelKelengkapanKendaraan.add(lblDongkrak, "cell 0 7");
		
		f_chckbxDongkrak = new JCheckBox("Ada");
		panelKelengkapanKendaraan.add(f_chckbxDongkrak, "cell 1 7");
		
		JLabel lblPitaPembatas = new JLabel("Pita Pembatas");
		panelKelengkapanKendaraan.add(lblPitaPembatas, "cell 0 8");
		
		f_chckbxPitaPembatas = new JCheckBox("Ada");
		panelKelengkapanKendaraan.add(f_chckbxPitaPembatas, "cell 1 8");
		
		JLabel lblGanjalRoda = new JLabel("Ganjal Roda");
		panelKelengkapanKendaraan.add(lblGanjalRoda, "cell 0 9");
		
		f_chckbxGanjalRoda = new JCheckBox("Ada");
		panelKelengkapanKendaraan.add(f_chckbxGanjalRoda, "cell 1 9");
		
		JLabel lblKotakObat = new JLabel("Kotak Obat");
		panelKelengkapanKendaraan.add(lblKotakObat, "cell 0 10");
		
		f_chckbxKotakObat = new JCheckBox("Ada");
		panelKelengkapanKendaraan.add(f_chckbxKotakObat, "cell 1 10");
		
		JPanel panelPlacard = new JPanel();
		panelPlacard.setBorder(new TitledBorder(null, "Placard", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		b2PanelPostDetailGui.add(panelPlacard);
		panelPlacard.setLayout(new MigLayout("", "[129.00][118.00,grow,fill]", "[][][][]"));
		
		JLabel lblKanan = new JLabel("Kanan");
		panelPlacard.add(lblKanan, "cell 0 0,alignx leading");
		
		f_comboBoxKanan = new JComboBox();
		f_comboBoxKanan.setModel(new DefaultComboBoxModel(ThreeChoice.values()));
		panelPlacard.add(f_comboBoxKanan, "cell 1 0,growx");
		
		JLabel lblKiri = new JLabel("Kiri");
		panelPlacard.add(lblKiri, "cell 0 1,alignx leading");
		
		f_comboBoxKiri = new JComboBox();
		f_comboBoxKiri.setModel(new DefaultComboBoxModel(ThreeChoice.values()));
		panelPlacard.add(f_comboBoxKiri, "cell 1 1,growx");
		
		JLabel lblDepan = new JLabel("Depan");
		panelPlacard.add(lblDepan, "cell 0 2,alignx leading");
		
		f_comboBoxDepan = new JComboBox();
		f_comboBoxDepan.setModel(new DefaultComboBoxModel(ThreeChoice.values()));
		panelPlacard.add(f_comboBoxDepan, "cell 1 2,growx");
		
		JLabel lblBelakang = new JLabel("Belakang");
		panelPlacard.add(lblBelakang, "cell 0 3,alignx leading");
		
		f_comboBoxBelakang = new JComboBox();
		f_comboBoxBelakang.setModel(new DefaultComboBoxModel(ThreeChoice.values()));
		panelPlacard.add(f_comboBoxBelakang, "cell 1 3,growx");		
		
		b2PanelPostDetailGui.add(panelPlacard, "cell 0 2,grow");
		
		JPanel panelSuratDanBerlaku = new JPanel();
		panelSuratDanBerlaku.setBorder(new TitledBorder(null, "Surat & Masa Berlaku", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		b2PanelPostDetailGui.add(panelSuratDanBerlaku);
		panelSuratDanBerlaku.setLayout(new MigLayout("", "[125.00][grow]", "[][][][][][][][]"));
		
		JLabel lblIjinB = new JLabel("Ijin B3");
		panelSuratDanBerlaku.add(lblIjinB, "cell 0 0,alignx leading");
		
		f_textFieldIjinB3 = new JTextField();
		panelSuratDanBerlaku.add(f_textFieldIjinB3, "cell 1 0,growx");
		f_textFieldIjinB3.setColumns(10);
		
		JLabel lblSim = new JLabel("SIM");
		panelSuratDanBerlaku.add(lblSim, "cell 0 1,alignx leading");
		
		f_textFieldSim = new JTextField();
		panelSuratDanBerlaku.add(f_textFieldSim, "cell 1 1,growx");
		f_textFieldSim.setColumns(10);
		
		JLabel lblKir = new JLabel("KIR");
		panelSuratDanBerlaku.add(lblKir, "cell 0 2,alignx leading");
		
		f_textFieldKir = new JTextField();
		panelSuratDanBerlaku.add(f_textFieldKir, "cell 1 2,growx");
		f_textFieldKir.setColumns(10);
		
		JLabel lblStnk = new JLabel("STNK");
		panelSuratDanBerlaku.add(lblStnk, "cell 0 3,alignx leading");
		
		f_textFieldStnk = new JTextField();
		panelSuratDanBerlaku.add(f_textFieldStnk, "cell 1 3,growx");
		f_textFieldStnk.setColumns(10);
		
		JLabel lblUjiEmisi = new JLabel("Uji Emisi");
		panelSuratDanBerlaku.add(lblUjiEmisi, "cell 0 4,alignx leading");
		
		f_textFieldUjiEmisi = new JTextField();
		panelSuratDanBerlaku.add(f_textFieldUjiEmisi, "cell 1 4,growx");
		f_textFieldUjiEmisi.setColumns(10);
		
		JLabel lblKetDokter = new JLabel("Ket. Dokter");
		panelSuratDanBerlaku.add(lblKetDokter, "cell 0 5,alignx leading");
		
		f_textFieldKetDokter = new JTextField();
		panelSuratDanBerlaku.add(f_textFieldKetDokter, "cell 1 5,growx");
		f_textFieldKetDokter.setColumns(10);
		
		JLabel lblSertifikat = new JLabel("Sertifikat");
		panelSuratDanBerlaku.add(lblSertifikat, "cell 0 6,alignx leading");
		
		f_chckbxSertifikat = new JCheckBox("Ada");
		panelSuratDanBerlaku.add(f_chckbxSertifikat, "cell 1 6");
		
		JLabel lblIdCard = new JLabel("ID Card");
		panelSuratDanBerlaku.add(lblIdCard, "cell 0 7,alignx leading");
		
		f_chckbxIdCard = new JCheckBox("Ada");
		panelSuratDanBerlaku.add(f_chckbxIdCard, "cell 1 7");
		
		b2PanelPostDetailGui.add(panelSuratDanBerlaku);
		
		JPanel panelCodeTruck = new JPanel();
		panelCodeTruck.setBorder(new TitledBorder(null, "Code Truck", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		b2PanelPostDetailGui.add(panelCodeTruck);
		panelCodeTruck.setLayout(new MigLayout("", "[125.00][grow]", "[]"));
		
		JLabel lblCodeTruck = new JLabel("Code Truck");
		panelCodeTruck.add(lblCodeTruck, "cell 0 0,alignx leading");
		
		f_textFieldCodeTruck = new JTextField();
		panelCodeTruck.add(f_textFieldCodeTruck, "cell 1 0,growx");
		f_textFieldCodeTruck.setColumns(10);
		
		b2PanelPostDetailGui.add(panelCodeTruck);
		b1TabPanelPostDetail.addTab("Graphical", null, _b2ScrollPanePostDetailGui, null);		
		
		b2JTextPanePostDetailRaw = new JTextPane();
		JScrollPane _b2ScrollPanePostDetailRaw = new JScrollPane(b2JTextPanePostDetailRaw);
		b1TabPanelPostDetail.addTab("Raw", null, _b2ScrollPanePostDetailRaw, null);
		
		JPanel b1PanelUserDetail = new JPanel();
		b1PanelUserDetail.setBounds(522, 425, 542, 255);		
		panelMain.add(b1PanelUserDetail);
		b1PanelUserDetail.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane b2TabbedPanelDetails = new JTabbedPane(JTabbedPane.BOTTOM);		
		b2TabbedPanelDetails.setBorder(new TitledBorder(null, "User Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		b1PanelUserDetail.add(b2TabbedPanelDetails, BorderLayout.CENTER);
		
				
		JPanel b3PanelUserDetailGui = new JPanel();		
		b3PanelUserDetailGui.setLayout(new MigLayout("", "[166.00][grow]", "[][][][][][][]"));
		JScrollPane _b3ScrollPanelUserDetailGui = new JScrollPane();
		_b3ScrollPanelUserDetailGui.setViewportView(b3PanelUserDetailGui);
		
		JLabel b4LblNama = new JLabel("Nama Lengkap");
		b3PanelUserDetailGui.add(b4LblNama, "cell 0 0,alignx leading");
		
		b4TextFieldNama = new JTextField();
		b4TextFieldNama.setEditable(false);
		b3PanelUserDetailGui.add(b4TextFieldNama, "cell 1 0,growx");
		b4TextFieldNama.setColumns(10);
		
		JLabel b4LblUserName = new JLabel("User Name");
		b3PanelUserDetailGui.add(b4LblUserName, "cell 0 1,alignx leading");
		
		b4TextFieldUserName = new JTextField();
		b4TextFieldUserName.setEditable(false);
		b3PanelUserDetailGui.add(b4TextFieldUserName, "cell 1 1,growx");
		b4TextFieldUserName.setColumns(10);
		
		JLabel b4LblIdCard = new JLabel("ID Card");
		b3PanelUserDetailGui.add(b4LblIdCard, "cell 0 2,alignx leading");
		
		b4TextFieldIdCard = new JTextField();
		b4TextFieldIdCard.setEditable(false);
		b3PanelUserDetailGui.add(b4TextFieldIdCard, "cell 1 2,growx");
		b4TextFieldIdCard.setColumns(10);
		
		JLabel b4LblTtl = new JLabel("TTL");
		b3PanelUserDetailGui.add(b4LblTtl, "cell 0 3,alignx leading");
		
		b4TextFieldTTL = new JTextField();
		b4TextFieldTTL.setEditable(false);
		b3PanelUserDetailGui.add(b4TextFieldTTL, "cell 1 3,growx");
		b4TextFieldTTL.setColumns(10);
		
		JLabel b4LblTanggalMasukPerusahaan = new JLabel("Tanggal Masuk Perusahaan");
		b3PanelUserDetailGui.add(b4LblTanggalMasukPerusahaan, "cell 0 4,alignx leading");
		
		b4TextFieldTanggaLMasuk = new JTextField();
		b4TextFieldTanggaLMasuk.setEditable(false);
		b3PanelUserDetailGui.add(b4TextFieldTanggaLMasuk, "cell 1 4,growx");
		b4TextFieldTanggaLMasuk.setColumns(10);
		
		JLabel b4LblNoTelp = new JLabel("No. Telp");
		b3PanelUserDetailGui.add(b4LblNoTelp, "cell 0 5,alignx leading");
		
		b4TextFieldNoTelp = new JTextField();
		b4TextFieldNoTelp.setEditable(false);
		b3PanelUserDetailGui.add(b4TextFieldNoTelp, "cell 1 5,growx");
		b4TextFieldNoTelp.setColumns(10);
		
		JLabel b4LblEmail = new JLabel("Email");
		b3PanelUserDetailGui.add(b4LblEmail, "cell 0 6,alignx leading");
		
		b4TextFieldEmail = new JTextField();
		b4TextFieldEmail.setEditable(false);
		b3PanelUserDetailGui.add(b4TextFieldEmail, "cell 1 6,growx");
		b4TextFieldEmail.setColumns(10);
		
		b2TabbedPanelDetails.addTab("Graphical", null, _b3ScrollPanelUserDetailGui , null);
		
		b3TextAreaUserDetailRaw = new JTextArea();
		JScrollPane _b3PanelUserDetailRaw = new JScrollPane(b3TextAreaUserDetailRaw);
		b2TabbedPanelDetails.addTab("Raw", null, _b3PanelUserDetailRaw, null);
		
		
		
		JPanel b1PanelUserList = new JPanel();
		b1PanelUserList.setBounds(10, 425, 502, 255);		
		panelMain.add(b1PanelUserList);
		b1PanelUserList.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane b2TabbedPanelUserList = new JTabbedPane(JTabbedPane.BOTTOM);
		b2TabbedPanelUserList.setBorder(new TitledBorder(null, "User List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		b1PanelUserList.add(b2TabbedPanelUserList, BorderLayout.CENTER);
		
		b4JlistUserList = new JList();
		b4JlistUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane b3ScrollPaneUserList1 = new JScrollPane(b4JlistUserList);
		b2TabbedPanelUserList.addTab("Graphical", null, b3ScrollPaneUserList1, null);		
		
		b4TextPaneUserList = new JTextPane();
		
		JScrollPane b3ScrollPaneUserList2 = new JScrollPane(b4TextPaneUserList);
		b2TabbedPanelUserList.addTab("Raw", null, b3ScrollPaneUserList2, null);		
		
		
		
	}
}

