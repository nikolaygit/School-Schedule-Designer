package org.posithing.ssd.jobs;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.IProgressConstants;
import org.eclipse.ui.progress.UIJob;
import org.posithing.ssd.Activator;
import org.posithing.ssd.jaxb.ClassType;
import org.posithing.ssd.jaxb.ClassesType;
import org.posithing.ssd.jaxb.ForClassType;
import org.posithing.ssd.jaxb.SchoolScheduleType;
import org.posithing.ssd.jaxb.SubjectType;
import org.posithing.ssd.jaxb.SubjectsType;
import org.posithing.ssd.jaxb.TeacherSubjectType;
import org.posithing.ssd.jaxb.TeacherType;
import org.posithing.ssd.jaxb.TeachersType;
import org.posithing.ssd.jaxb.TimeSlotAssignmentType;
import org.posithing.ssd.jaxb.TimeSlotsAssignmentType;
import org.posithing.ssd.model.Assignment;
import org.posithing.ssd.model.AssignmentSlot;
import org.posithing.ssd.model.AssignmentValue;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.model.TeachingSubject;
import org.posithing.ssd.model.TeachingSubjects;
import org.posithing.ssd.model.impl.XMLDataProvider;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class SaveDataJob extends UIJob {

	public static final String NAME = "Save Data";
	private File file;

	public SaveDataJob(File file) {
		super(NAME);
		this.file = file;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {

		ExtensionManager extensionManager = ExtensionManager.getInstance();
		SchoolDataProvider dataProvider = extensionManager.getDefaultSchoolDataProvider();

		Exception exception = null;
		SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
		subMonitor.beginTask("Saving Data", 10);

		try {

			Set<Teacher> teachers = dataProvider.getTeachers();
			Map<Teacher, TeachingSubjects> teacherSubjects = dataProvider.getTeacherSubjects();
			Set<Subject> subjects = dataProvider.getSubjects();
			Set<SchoolClass> schoolClasses = dataProvider.getSchoolClasses();
			List<Assignment> assignments = dataProvider.getAssignments();

			if (dataProvider instanceof XMLDataProvider) {
				XMLDataProvider xmlDataProvider = (XMLDataProvider) dataProvider;

				SchoolScheduleType schedule = new SchoolScheduleType();

				subMonitor.beginTask("Teachers", 10);
				createTeachers(schedule, teachers, dataProvider.getTeacherSubjects());

				subMonitor.beginTask("Subjects", 20);
				createSubjects(schedule, subjects);

				subMonitor.beginTask("Classes", 20);
				createSchoolClasses(schedule, schoolClasses);

				subMonitor.beginTask("Assignments", 20);
				createAssignments(schedule, assignments);

				subMonitor.beginTask("Saving File ...", 20);
				xmlDataProvider.saveFile(schedule, file);
			}

			// if(months.size() > 0){
			//
			// // make sure the model is loaded
			// if (!cardsModel.isLoaded()) {
			// SubMonitor loadModelMonitor = subMonitor.newChild(10);
			// loadModelMonitor.setTaskName("Loading data from the web service ...");
			// boolean loadedSuccessfully = cardsModel.load();
			// if(!loadedSuccessfully){
			// return new Status(IStatus.ERROR, CardsProviderPlugin.PLUGIN_ID,
			// IStatus.ERROR,
			// "Synchronization exception! Could not load the cards model",
			// exception);
			// }
			// }
			// // monitor.beginTask(SynchronizeMonthsJob.NAME, months.size());
			//				
			// subMonitor.setWorkRemaining(90);
			// SubMonitor loopMonitor = subMonitor.newChild(90);
			// int workForOneMonth = 90/months.size();
			// loopMonitor.setTaskName("Synchronizing "+months.size()+" months ... "
			// + workForOneMonth);
			// loopMonitor.setWorkRemaining(90);
			// showStatusFromThread("Syncing ... ");
			// for (int i = 0; i < months.size(); i++) {
			// int month = months.get(i);
			// int year = years.get(i);
			// JaretDate begin = new JaretDate(1, month, year, 0, 0, 0);
			// JaretDate end = begin.copy().advanceMonths(1).backMinutes(1);
			// List<Appointment> modelEntries = calendarModel.getAppointments(
			// synchronizerId, begin, end);
			//					
			// // do the complete synchronization
			// // monitor.subTask("Synchronize " + month + "/" + year);
			// Synchronizer.synchronize(cardsModel, synchronizerId,
			// calendarModel, month, year, modelEntries,
			// loopMonitor.newChild(workForOneMonth));
			// // monitor.worked(1);
			// }
			// }
		} catch (Exception e) {
			exception = e;
		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}

		if (exception != null) {
			final Exception threadException = exception;
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					// show exception
					Messenger.openExceptionBox(threadException, "Synchronization exception!",
							"There was an error in the synchronization of the data. "
									+ "Please refer to the details.");
				}
			});
			Status cancelStatus = new Status(IStatus.CANCEL, Activator.PLUGIN_ID, IStatus.CANCEL,
					"Saving Data canceled!", exception);
			return cancelStatus;
		} else {
			Status okStatus = new Status(IStatus.OK, Activator.PLUGIN_ID, IStatus.OK,
					"Synchronization successful", exception);

			if (isModal(this)) {

				// CardsProviderPlugin plugin =
				// CardsProviderPlugin.getDefault();
				// IPreferenceStore store = plugin.getPreferenceStore();
				// boolean showMessagePref = store
				// .getBoolean(CardsPreferenceConstants.SHOW_SYNCH_COMPLETE_MSG);
				//
				// if (showMessage && showMessagePref) {
				// // The progress dialog is still open so
				// // just open the message
				// showResults();
				// }
			} else {
				// setProperty(IProgressConstants.KEEP_PROPERTY, Boolean.TRUE);
			}

			return okStatus;
		}
	}

	private void createAssignments(SchoolScheduleType schedule, List<Assignment> assignments) {
		TimeSlotsAssignmentType timeSlotsAssignmentType = new TimeSlotsAssignmentType();
		List<TimeSlotAssignmentType> timeSlotAssignmentList = timeSlotsAssignmentType
				.getTimeSlotAssignment();

		for (Assignment assignment : assignments) {

			AssignmentSlot slot = assignment.getSlot();
			AssignmentValue value = assignment.getValue();

			TimeSlotAssignmentType timeSlotAssignmentType = new TimeSlotAssignmentType();
			timeSlotAssignmentType.setDay(slot.getDay());
			timeSlotAssignmentType.setDaySlot(slot.getTimeSlot());
			timeSlotAssignmentType.setClassId(slot.getClassId());
			timeSlotAssignmentType.setTeacherId(value.getTeacher().getId());
			timeSlotAssignmentType.setSubjectId(value.getSubject().getId());

			timeSlotAssignmentList.add(timeSlotAssignmentType);
		}

		schedule.setTimeSlotsAssignment(timeSlotsAssignmentType);
	}

	private void createTeachers(SchoolScheduleType schedule, Set<Teacher> teachers,
			Map<Teacher, TeachingSubjects> teachingMap) throws Exception {

		TeachersType teachersType = new TeachersType();
		List<TeacherType> teachersTypeList = teachersType.getTeacher();

		for (Teacher teacher : teachers) {
			TeacherType teacherType = new TeacherType();
			teacherType.setFirstName(teacher.getFirstName());
			teacherType.setLastName(teacher.getLastName());
			teacherType.setId(teacher.getId());
			teacherType.setColor(getRGBString(teacher.getColor()));
			teacherType.setShape(teacher.getShape());

			TeachingSubjects teachingSubjects = teachingMap.get(teacher);
			if (teachingSubjects != null) {

				Set<TeachingSubject> teachingSubjectsSet = teachingSubjects.getTeachingSubjects();
				for (TeachingSubject teachingSubject : teachingSubjectsSet) {

					String subjectId = teachingSubject.getSubject().getId();
					Set<SchoolClass> schoolClasses = teachingSubject.getSchoolClasses();

					TeacherSubjectType teacherSubjectType = new TeacherSubjectType();
					teacherSubjectType.setId(subjectId);

					for (SchoolClass schoolClass : schoolClasses) {
						ForClassType forClassType = new ForClassType();
						forClassType.setId(schoolClass.getId());
						teacherSubjectType.getForClass().add(forClassType);
					}

					teacherType.getTeacherSubject().add(teacherSubjectType);
				}
			}

			teachersTypeList.add(teacherType);
		}

		schedule.setTeachers(teachersType);
	}

	private void createSubjects(SchoolScheduleType schedule, Set<Subject> subjects) {

		SubjectsType subjectsType = new SubjectsType();
		List<SubjectType> subjectsTypeList = subjectsType.getSubject();

		for (Subject subject : subjects) {
			SubjectType subjectType = new SubjectType();
			subjectType.setId(subject.getId());
			subjectType.setName(subject.getName());

			subjectsTypeList.add(subjectType);
		}

		schedule.setSubjects(subjectsType);
	}

	private void createSchoolClasses(SchoolScheduleType schedule, Set<SchoolClass> schoolClasses) {

		ClassesType classesType = new ClassesType();
		List<ClassType> classesTypeList = classesType.getClazz();

		for (SchoolClass schoolClass : schoolClasses) {
			ClassType classType = new ClassType();
			classType.setId(schoolClass.getId());
			classType.setName(schoolClass.getName());
			classType.setGrade(schoolClass.getGrade());

			classesTypeList.add(classType);
		}

		schedule.setClasses(classesType);
	}

	private String getRGBString(RGB rgb) {
		StringBuilder sb = new StringBuilder();
		sb.append(rgb.red);
		sb.append(StringUtil.COMMA);
		sb.append(rgb.green);
		sb.append(StringUtil.COMMA);
		sb.append(rgb.blue);
		return sb.toString();
	}

	public static boolean isModal(Job job) {
		Boolean isModal = (Boolean) job.getProperty(IProgressConstants.PROPERTY_IN_DIALOG);
		if (isModal == null)
			return false;
		return isModal.booleanValue();
	}
}
