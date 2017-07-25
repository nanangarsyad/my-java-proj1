package com.steapps;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

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
