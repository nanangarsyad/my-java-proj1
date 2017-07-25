package com.steapps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
