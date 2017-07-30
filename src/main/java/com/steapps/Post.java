package com.steapps;

import java.awt.Color;
import java.awt.Component;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public abstract class Post {
	
	public static class PostEntry {
		final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");

		
		public final String userName;
		public final String asDate;
		public final String asTime;
		
		public PostEntry(String userName, String time) {
			this.userName = userName;
			asTime = time;
			asDate = Instant.ofEpochMilli(Long.valueOf(time)).atZone(ZoneId.systemDefault()).format(dtf).toString();
		}
	}
	
	public static class PostRenderer extends JLabel implements ListCellRenderer<PostEntry> {
		private static final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);


		@Override
		public Component getListCellRendererComponent(JList<? extends PostEntry> list, PostEntry value, int index,
				boolean isSelected, boolean cellHasFocus) {
			
			setText(value.asDate);
			
			return this;
		}

		
		
	}

}
