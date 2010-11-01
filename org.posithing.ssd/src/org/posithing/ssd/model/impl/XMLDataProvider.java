package org.posithing.ssd.model.impl;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.FileLocator;
import org.posithing.ssd.jaxb.ClassType;
import org.posithing.ssd.jaxb.ClassesType;
import org.posithing.ssd.jaxb.ForClassType;
import org.posithing.ssd.jaxb.ObjectFactory;
import org.posithing.ssd.jaxb.SchoolScheduleType;
import org.posithing.ssd.jaxb.SubjectType;
import org.posithing.ssd.jaxb.SubjectsType;
import org.posithing.ssd.jaxb.TeacherSubjectType;
import org.posithing.ssd.jaxb.TeacherType;
import org.posithing.ssd.jaxb.TeachersType;
import org.posithing.ssd.jaxb.TimeSlotAssignmentType;
import org.posithing.ssd.jaxb.TimeSlotsAssignmentType;
import org.posithing.ssd.model.AbstractSimpleSchoolDataProvider;
import org.posithing.ssd.model.Assignment;
import org.posithing.ssd.model.AssignmentSlot;
import org.posithing.ssd.model.AssignmentValue;
import org.posithing.ssd.model.SchoolClass;
import org.posithing.ssd.model.SchoolDataProviderException;
import org.posithing.ssd.model.Subject;
import org.posithing.ssd.model.Teacher;
import org.posithing.ssd.model.TeachingSubjects;
import org.posithing.ssd.utils.EclipseUtil;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class XMLDataProvider extends AbstractSimpleSchoolDataProvider {

	private static Log LOG = LogFactory.getLog(XMLDataProvider.class);

	private static final String ID = "org.posithing.ssd.extensions.schooldataprovider.xml";
	private static final String NAME = "XML Data Provider";

	private static final String PACKAGE_NAME = "org.posithing.ssd.jaxb";

	private JAXBContext jc;
	private Unmarshaller unmarshaller;
	private boolean toSynchronize;

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void initialize() throws SchoolDataProviderException {
		LOG.info("initializing ... " + toString());
		try {
			jc = JAXBContext.newInstance(PACKAGE_NAME);
			unmarshaller = jc.createUnmarshaller();

			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

			URL pluginFolderUrl = EclipseUtil.getPluginFolderUrl();
			URL localURL = FileLocator.toFileURL(pluginFolderUrl);
			String schemaPath = localURL.getPath() + "resources/xsd/schoolschedule.xsd";
			File schemaFile = new File(schemaPath);
			if (schemaFile.exists()) {
				schemaFactory.newSchema(schemaFile);
			}

			toSynchronize = false;

		} catch (Exception e) {
			throw new SchoolDataProviderException(e);
		}
	}

	@Override
	public void synchronize() throws SchoolDataProviderException {
		LOG.info("syncing ..." + toString());
	}

	public void parseFile(File file) throws SchoolDataProviderException {
		try {
			JAXBElement<?> jaxbElement = (JAXBElement<?>) unmarshaller.unmarshal(file);
			Object xmlRoot = jaxbElement.getValue();
			if (xmlRoot instanceof SchoolScheduleType) {
				SchoolScheduleType schedule = (SchoolScheduleType) xmlRoot;

				// convert to OWN model
				loadClasses(schedule);
				loadSubjects(schedule);
				loadTeachers(schedule);
				loadAssignments(schedule);
			}
		} catch (JAXBException e) {
			throw new SchoolDataProviderException(e);
		} catch (Exception e) {
			throw new SchoolDataProviderException(e);
		}
	}

	private void loadClasses(SchoolScheduleType schedule) throws SchoolDataProviderException {
		ClassesType SchoolClasses = schedule.getClasses();
		if (SchoolClasses != null) {
			List<ClassType> schoolClassesTypes = SchoolClasses.getClazz();
			for (int i = 0; i < schoolClassesTypes.size(); i++) {
				ClassType classType = schoolClassesTypes.get(i);
				String id = classType.getId();
				String name = classType.getName();
				int grade = classType.getGrade();

				if (StringUtil.isEmpty(name)) {
					name = id;
				}

				SchoolClass schoolClass = new SchoolClass(id, name, grade);
				addSchoolClass(schoolClass, toSynchronize);
				LOG.info("School Class '" + schoolClass + "' was added.");
			}
		}
	}

	private void loadSubjects(SchoolScheduleType schedule) throws SchoolDataProviderException {
		SubjectsType subjects = schedule.getSubjects();
		if (subjects != null) {
			List<SubjectType> subjectTypes = subjects.getSubject();
			for (int i = 0; i < subjectTypes.size(); i++) {
				SubjectType subjectType = subjectTypes.get(i);
				String id = subjectType.getId();
				String name = subjectType.getName();

				if (StringUtil.isEmpty(name)) {
					name = id;
				}

				Subject subject = new Subject(id, name);
				addSubject(subject, toSynchronize);
				LOG.info("Subject '" + subject + "' was added.");
			}
		}
	}

	private void loadTeachers(SchoolScheduleType schedule) throws SchoolDataProviderException {
		TeachersType teachers = schedule.getTeachers();
		if (teachers != null) {
			List<TeacherType> teacherTypes = teachers.getTeacher();
			for (int i = 0; i < teacherTypes.size(); i++) {
				TeacherType teacherType = teacherTypes.get(i);
				String firstName = teacherType.getFirstName();
				String lastName = teacherType.getLastName();
				String id = teacherType.getId();
				String colorStr = teacherType.getColor();
				String shape = teacherType.getShape();

				Teacher teacher = new Teacher(id, firstName, lastName);
				teacher.setColor(EclipseUtil.getRGBColor(colorStr));
				teacher.setShape(shape);
				addTeacher(teacher, toSynchronize);
				LOG.info("Teacher '" + teacher + "' was added.");

				// load teacher subjects
				List<TeacherSubjectType> teacherSubjectTypes = teacherType.getTeacherSubject();
				for (TeacherSubjectType teacherSubjectType : teacherSubjectTypes) {
					String subjectId = teacherSubjectType.getId();
					Subject subject = getSubject(subjectId);
					if (subject == null) {
						LOG.warn("Teacher '" + teacher + "' has unknown subject ID :" + subjectId);
					} else {
						TeachingSubjects teachingSubjects = new TeachingSubjects();

						Set<SchoolClass> schoolClasses = new TreeSet<SchoolClass>();

						List<ForClassType> forClasses = teacherSubjectType.getForClass();
						for (int j = 0; j < forClasses.size(); j++) {
							ForClassType forClassType = forClasses.get(j);
							String classId = forClassType.getId();
							SchoolClass schoolClass = getSchoolClass(classId);
							if (schoolClass == null) {
								LOG.warn("Teacher '" + teacher + "' has for subject ID "
										+ subjectId + " an unknown class ID: " + classId);
							} else {
								schoolClasses.add(schoolClass);
							}
						}

						teachingSubjects.addSubject(subject, schoolClasses);
						addTeacherSubjects(teacher, teachingSubjects, toSynchronize);
						LOG.info("Teacher '" + teacher + "' has teaching subjects: "
								+ teachingSubjects.toString());
					}
				}
			}
		}
	}

	private void loadAssignments(SchoolScheduleType schedule) throws SchoolDataProviderException {
		TimeSlotsAssignmentType timeSlotsAssignment = schedule.getTimeSlotsAssignment();
		if (timeSlotsAssignment == null)
			return;
		List<TimeSlotAssignmentType> timeSlotAssignmentList = timeSlotsAssignment
				.getTimeSlotAssignment();
		for (TimeSlotAssignmentType timeSlotAssignmentType : timeSlotAssignmentList) {
			int day = timeSlotAssignmentType.getDay();
			int daySlot = timeSlotAssignmentType.getDaySlot();
			String classId = timeSlotAssignmentType.getClassId();
			String teacherId = timeSlotAssignmentType.getTeacherId();
			String subjectId = timeSlotAssignmentType.getSubjectId();

			AssignmentSlot slot = new AssignmentSlot(day, daySlot, classId);

			Teacher teacher = getTeacher(teacherId);
			Subject subject = getSubject(subjectId);

			AssignmentValue assignmentValue = new AssignmentValue(teacher, subject);
			Assignment assignment = new Assignment(slot, assignmentValue);
			addAssignment(assignment, toSynchronize);
		}
	}

	public void saveFile(SchoolScheduleType schedule, File file) {

		try {
			ObjectFactory objFactory = new ObjectFactory();
			JAXBElement<SchoolScheduleType> rootElement = objFactory.createSchoolschedule(schedule);

			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(rootElement, file);
		} catch (JAXBException e) {
			Messenger.openExceptionBox(e, "Can not create JAXB Marschaller",
					"Could not create JAXB Marshaller");
		} catch (Exception e) {
			Messenger.openExceptionBox(e, "Can not marshal file", "Can not marshal file: "
					+ file.toString());
		}
	}

}
