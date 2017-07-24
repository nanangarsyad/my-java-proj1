package com.steapps;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.json.JSONObject;

import com.alee.laf.WebLookAndFeel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.miginfocom.swing.MigLayout;

public class STEApps {
	
	private JFrame frame;
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
	private JList b2JlistPost;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		FB.setupFirebase();		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WebLookAndFeel.install();
					STEApps window = new STEApps();
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
		
		// request user list to "use list panel"
		FB.listenToUserList(b4JlistUserList);
		
		
		EventHandler.setSingleClickListener(b4JlistUserList, (Map<String, Object> params) -> {
			String sel = (String) b4JlistUserList.getSelectedValue();			
			
			FB.listenToValueForSpecificPath(b4TextFieldNama, "/users/"+sel, (success, snapshot, error) -> {
				final Map<String, String> asMap  = new HashMap<>();
				final StringWriter asString  = new StringWriter();
				
				if (success) {
					asMap.putAll((Map<String, String>) snapshot.getValue()); 
					asString.append(snapshot.getValue(true).toString().replaceAll("=", ":"));					
					
				} else {
					asString.append(error.getMessage());
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
					
					
					b3TextAreaUserDetailRaw.setText(new JSONObject(asString.toString()).toString(4));
				});
			});
		});
		
		FB.listenToValueForSpecificPath(b4TextPaneUserList, "/users/", (success, snapshot, error) -> {
			StringWriter writer = new StringWriter();
			if (success) {				
				writer.append(snapshot.getValue(true).toString());				
			} else {
				writer.append(error.getMessage());
			}			
			SwingUtilities.invokeLater(() -> b4TextPaneUserList.setText(new JSONObject(writer.toString().replaceAll("=",":")).toString(4)));
			
		});
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 1080, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(7, 7, 84, 16);
		frame.getContentPane().add(toolBar);
		
		JTabbedPane b1TabPaneUserPost = new JTabbedPane(JTabbedPane.BOTTOM);
		b1TabPaneUserPost.setBorder(new TitledBorder(null, "User Post", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		b1TabPaneUserPost.setBounds(7, 23, 503, 400);
		frame.getContentPane().add(b1TabPaneUserPost);		
		
		b2JlistPost  = new JList();
		JScrollPane _b2JScrollPostGui = new JScrollPane(b2JlistPost);
		b1TabPaneUserPost.addTab("Graphical", null, _b2JScrollPostGui, null);
		
		
		JTextArea b2TextAreaPostRaw = new JTextArea();
		JScrollPane _b2JScrollPostRaw = new JScrollPane(b2TextAreaPostRaw);
		b1TabPaneUserPost.addTab("Raw", null, _b2JScrollPostRaw, null);		
		
		
		JTabbedPane b1TabbedPanelPostDetail = new JTabbedPane();
		b1TabbedPanelPostDetail.setBorder(new TitledBorder(null, "Post Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		b1TabbedPanelPostDetail.setTabPlacement(JTabbedPane.BOTTOM);
		b1TabbedPanelPostDetail.setBounds(512, 23, 557, 400);
		frame.getContentPane().add(b1TabbedPanelPostDetail);
		
		JPanel panel = new JPanel();
		b1TabbedPanelPostDetail.addTab("New tab", null, panel, null);
		
		JPanel panel_1 = new JPanel();
		b1TabbedPanelPostDetail.addTab("New tab", null, panel_1, null);
		
		JPanel b1PanelUserDetail = new JPanel();
		b1PanelUserDetail.setBounds(512, 434, 555, 246);
		frame.getContentPane().add(b1PanelUserDetail);
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
		b1PanelUserList.setBounds(7, 434, 497, 246);
		frame.getContentPane().add(b1PanelUserList);
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


class FB {
	
	
	interface FbCallback {
		void onDone(boolean success, DataSnapshot snapshot, DatabaseError erorr);
	}
	
	private static String KEY_FB_LISTENER = "fb-listener";
	
	
	static FirebaseDatabase sFbInstance;
	
	static void setupFirebase() {
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		File file = new File(classLoader.getResource("steapps-50634-firebase-adminsdk-qkku2-516454a8d6.json").getFile());
		System.out.println(file);
		FileInputStream certificate = null;
		try {
			certificate = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FirebaseOptions options = null;
		try {
			options = new FirebaseOptions.Builder()
			  .setCredential(FirebaseCredentials.fromCertificate(certificate))
			  .setDatabaseUrl("https://steapps-50634.firebaseio.com/")
			  .build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FirebaseApp.initializeApp(options);		
		sFbInstance = FirebaseDatabase.getInstance();
	}
	
	static void listenToForm(JTree tree) {
		
		/*DatabaseReference formRef = sFbInstance.getReference("/forms/A");
		formRef.addListenerForSingleValueEvent(new ValueEventListener() {
			
			public void onDataChange(DataSnapshot snapshot) {				
				Map<String, ?> map = (Map<String, ?>) snapshot.getValue();				
				DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
				root.add(new DefaultMutableTreeNode());
				System.out.println(new JSONObject(map));
				
				//tree.setModel(new DefaultTreeModel(root));				
				
			}
			
			public void onCancelled(DatabaseError error) {
				// TODO Auto-generated method stub
				
			}
		});*/
	}
	
	static void listenToValueForSpecificPath(JComponent c, String fbPath, FbCallback callback) {
		ValueEventListener vel = (ValueEventListener) c.getClientProperty(KEY_FB_LISTENER);
		
		if (vel == null) {
			vel = new ValueEventListener() {
				
				@Override
				public void onDataChange(DataSnapshot snapshot) {
					callback.onDone(true, snapshot, null);
					
				}
				
				@Override
				public void onCancelled(DatabaseError error) {
					callback.onDone(false, null, error);
					
				}
			};
		}
		
		sFbInstance.getReference(fbPath).addValueEventListener(vel);
		c.putClientProperty(KEY_FB_LISTENER, vel); 	// store the reference of the listener, 
													// in case we wanted to remove the listener later time

		
	};
	
	static void unlistenToValueForSpecificPath(JComponent c, String path, boolean deleteListener) {		 
		sFbInstance.getReference(path).removeEventListener((ValueEventListener) c.getClientProperty(KEY_FB_LISTENER));
		if (deleteListener) {
			c.putClientProperty(KEY_FB_LISTENER, null); // remove the listener
		}
	}
	
	
	
	static void listenToUserList(final JList<String> jlist) {		 
		DatabaseReference userRef = sFbInstance.getReference("/users");
		
		ValueEventListener vel = (ValueEventListener) jlist.getClientProperty(KEY_FB_LISTENER);
		
		if (vel == null) {
			vel = new ValueEventListener() {
				
				public void onDataChange(DataSnapshot snapshot) {				
					jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);				
					DefaultListModel<String> listModel = new DefaultListModel<String>();								
					for (DataSnapshot snap: snapshot.getChildren()) {					
						listModel.addElement(snap.getKey());
					}
					
					SwingUtilities.invokeLater(()->{						
						jlist.setModel(listModel);
						jlist.revalidate();
						jlist.repaint();
					});
				}
				
				public void onCancelled(DatabaseError error) {
					// TODO Auto-generated method stub
					
				}
			};
		}
		userRef.addListenerForSingleValueEvent(vel);
		
		jlist.putClientProperty(KEY_FB_LISTENER, vel); 	// store the reference of the listener, 
														// in case we wanted to remove the listener later time
		
	}
	
	static void unlistenToUserList(final JList<String> jlist, boolean deleteListener) {
		DatabaseReference userRef = sFbInstance.getReference("/users");
		userRef.removeEventListener((ValueEventListener) jlist.getClientProperty(KEY_FB_LISTENER));
		if (deleteListener) {
			jlist.putClientProperty(KEY_FB_LISTENER, null); // remove the listener
		}
		
	}
}

class EventHandler {
	
	interface OnEvent {
		
		public static final String EVENT_TYPE_MOUSE = "mouse"; 
		public static final String EVENT_TYPE = "type";
		public static final String EVENT_SOURCE = "source";
		
		void dispatch(Map<String, Object> params);
	}
	
	public static void setSingleClickListener(JComponent c, OnEvent onEvent) {
		final String objectKey = "mouse-single-click";
		
		// check if the component ever had a listener, remove it if true
		MouseAdapter prevMa = (MouseAdapter) c.getClientProperty(objectKey);
		if ( prevMa != null) {
			c.removeMouseListener(prevMa);
		}
				
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					Map<String, Object> map = new HashMap<>();
					map.put(OnEvent.EVENT_SOURCE, e);
					onEvent.dispatch(map);
					map = null;
				}
			}
		};
		c.addMouseListener(ma);
		c.putClientProperty(objectKey, ma);
	}
	
	public static void setDoubleClickListener(JComponent c, OnEvent onEvent) {
		final String objectKey = "mouse-double-click";
		
		// check if the component ever had a listener, remove it if true
		MouseAdapter prevMa = (MouseAdapter) c.getClientProperty(objectKey);
		if ( prevMa != null) {
			c.removeMouseListener(prevMa);
		}
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Map<String, Object> map = new HashMap<>();
					map.put(OnEvent.EVENT_SOURCE, e);
					onEvent.dispatch(map);
					map = null;
				}
			}
		};
		c.addMouseListener(ma);
		c.putClientProperty(objectKey, ma);
	}
}
