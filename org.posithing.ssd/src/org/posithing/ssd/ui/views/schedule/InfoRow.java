package org.posithing.ssd.ui.views.schedule;


public class InfoRow implements ScheduleTableRow {

	private int day;
	private String[] infos;

	public InfoRow(int day, int numberOfAssignments) {
		this.day = day;
		infos = new String[numberOfAssignments];
	}

	public int getSize() {
		return infos.length;
	}

	public String getInfo(int index) {
		return infos[index];
	}

	public void setInfo(String info, int index) {
		infos[index] = info;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String[] getInfos() {
		return infos;
	}

	public void setInfos(String[] infos) {
		this.infos = infos;
	}
}
