package org.posithing.ssd.ui.views;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.posithing.ssd.constants.SSMConstants.DAY;
import org.posithing.ssd.utils.MessagesUtil;
import org.posithing.ssd.utils.StringUtil;


public class Copy_2_of_ScheduleView extends ViewPart {
	
	public static final String ID = "org.posithing.ssd.ui.ScheduleView"; //$NON-NLS-1$

	public Copy_2_of_ScheduleView() {
	}

	@Override
	public void createPartControl(Composite comp) {

//		MigLayout layout = new MigLayout("",
//                "[pref!][0:0,grow 20,fill][0:0,grow 20,fill][0:0,grow 20,fill]" +
//                	"[0:0,grow 20,fill][0:0,grow 20,fill]",
//				"[]20[]");

		int numberOfDaySlots = 7;
		int numberOfColumnsPerDay = numberOfDaySlots + 1;
		int numberOfColumns = 1 + numberOfColumnsPerDay * 5 + 1;
		int lastColumnIndex = numberOfColumns - 1;

		final ScrolledComposite scrolledComposite = new ScrolledComposite(comp, 
				SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setEnabled(true);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite parent = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(parent);
		
		LC layoutLC = new LC().alignY("top").wrapAfter(numberOfColumns).fill()
						;
		
		AC layoutColumnAC = new AC().size("10:15:20", 0).size("40:50:60", lastColumnIndex)
						.align("left", 0).align("right", lastColumnIndex)
						.gap("10!", 0).gap("15!", lastColumnIndex-1)
						.gap("15::20", 0*numberOfColumnsPerDay)
						.gap("15::20", 1*numberOfColumnsPerDay)
						.gap("15::20", 2*numberOfColumnsPerDay)
						.gap("15::20", 3*numberOfColumnsPerDay)
						.gap("15::20", 4*numberOfColumnsPerDay)
						
						;
		
		int[] indexes = new int[numberOfColumnsPerDay * 5];
		int indexCount = 0;
		for (int i = 0; i < numberOfColumnsPerDay * 5; i++) { // for every day
			indexCount++;
			indexes[indexCount - 1] = i + 1;
		}
		layoutColumnAC.size("50::70", indexes);
		
		AC layoutRowsAC = new AC().grow(1, 2)
//						.size("50:70", 2)
						.align("top", 2)
						;
		
		MigLayout layout = new MigLayout(
				layoutLC, 
				layoutColumnAC, 
				layoutRowsAC
		);
		parent.setLayout(layout);
		parent.setLayoutData("hmin 0");
		
		createLabel(parent, getLabelStyleLeft(), "Class", 
				new CC().growX().alignY("top"));

		createLabel(parent, getLabelStyleCenter(), MessagesUtil.getDayName(DAY.MONDAY), 
				new CC().growX().spanX(numberOfColumnsPerDay).maxHeight("40").alignY("top"));
		createLabel(parent, getLabelStyleCenter(), MessagesUtil.getDayName(DAY.TUESDAY), 
				new CC().growX().spanX(numberOfColumnsPerDay).maxHeight("40").alignY("top"));
		createLabel(parent, getLabelStyleCenter(), MessagesUtil.getDayName(DAY.WEDNESDAY), 
				new CC().growX().spanX(numberOfColumnsPerDay).maxHeight("40").alignY("top"));
		createLabel(parent, getLabelStyleCenter(), MessagesUtil.getDayName(DAY.THURSDAY), 
				new CC().growX().spanX(numberOfColumnsPerDay).maxHeight("40").alignY("top"));
		createLabel(parent, getLabelStyleCenter(), MessagesUtil.getDayName(DAY.FRIDAY), 
				new CC().growX().spanX(numberOfColumnsPerDay).maxHeight("40").alignY("top"));
		
		createLabel(parent, getLabelStyleRight(), "Notes", 
				new CC().growX().alignY("top"));
		
		
		addClassRowHeader(parent, numberOfColumnsPerDay);
		addClassRow(parent, numberOfColumnsPerDay, "5a");
		addClassRow(parent, numberOfColumnsPerDay, "5b");
		addClassRow(parent, numberOfColumnsPerDay, "5v");
		addClassRow(parent, numberOfColumnsPerDay, "5g");
		addClassRow(parent, numberOfColumnsPerDay, "5d");
		addClassRow(parent, numberOfColumnsPerDay, "5e");
		
		addClassRow(parent, numberOfColumnsPerDay, "6a");
		addClassRow(parent, numberOfColumnsPerDay, "6b");
		addClassRow(parent, numberOfColumnsPerDay, "6v");
		addClassRow(parent, numberOfColumnsPerDay, "6g");
		addClassRow(parent, numberOfColumnsPerDay, "6d");
		addClassRow(parent, numberOfColumnsPerDay, "6e");
		
		addClassRow(parent, numberOfColumnsPerDay, "7a");
		addClassRow(parent, numberOfColumnsPerDay, "7b");
		addClassRow(parent, numberOfColumnsPerDay, "7v");
		addClassRow(parent, numberOfColumnsPerDay, "7g");
		addClassRow(parent, numberOfColumnsPerDay, "7d");
		addClassRow(parent, numberOfColumnsPerDay, "7e");
		
		addClassRow(parent, numberOfColumnsPerDay, "8a");
		addClassRow(parent, numberOfColumnsPerDay, "8b");
		addClassRow(parent, numberOfColumnsPerDay, "8v");
		addClassRow(parent, numberOfColumnsPerDay, "8g");
		addClassRow(parent, numberOfColumnsPerDay, "8d");
		addClassRow(parent, numberOfColumnsPerDay, "8e");
		
		scrolledComposite.setMinSize(parent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	private void addClassRowHeader(Composite parent, int numberOfColumnsPerDay) {
		createLabel(parent, getLabelStyleLeft(), "t", 
				new CC().alignY("top"));
		
		for (int i = 0; i < 5; i++) { // for every day
			int columnIndex = i*8;
			
			for (int j = 0; j < numberOfColumnsPerDay; j++) { // for every day slot
				columnIndex++;
				
				String title = StringUtil.EMPTY;
				if(j != numberOfColumnsPerDay -1){
					title += j+1;
				}
				createLabel(parent, getLabelStyleCenter(), title, 
						new CC().growX().alignY("top"));
			}
		}
		
		createLabel(parent, getLabelStyleRight(), StringUtil.EMPTY, 
				new CC().alignY("top"));
	}
	
	private void addClassRow(Composite parent, int numberOfColumnsPerDay, String className) {
		createLabel(parent, getLabelStyleLeft(), className, new CC());
		
		for (int i = 0; i < 5; i++) { // for every day
			int columnIndex = i*8;
			
			for (int j = 0; j < numberOfColumnsPerDay; j++) { // for every day slot
				columnIndex++;
				
				String title = StringUtil.EMPTY;
				if(j != numberOfColumnsPerDay -1){
					title += j+1;
				}
				createLabel(parent, getLabelStyleCenter(), title, 
						new CC().grow());
			}
		}
		
		createLabel(parent, getLabelStyleRight(), StringUtil.EMPTY, new CC());
	}

	public Label createLabel(Composite parent, int style, String text, Object layoutData){
		Label dayLabel = new Label(parent, style | 0);
		if(text != null){
			dayLabel.setText(text);
		}
		if(layoutData != null){
			dayLabel.setLayoutData(layoutData);
		}
		
		dayLabel.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseDown(MouseEvent e) {
				System.out.println("pressed");
				super.mouseDown(e);
			}
		});
		
		return dayLabel;
	}
	
	public int getLabelStyleCenter(){
		return SWT.CENTER | SWT.BORDER;
	}
	
	public int getLabelStyleLeft(){
		return SWT.LEFT | SWT.BORDER;
	}
	
	public int getLabelStyleRight(){
		return SWT.RIGHT | SWT.BORDER;
	}

	@Override
	public void setFocus() {
	}

}
