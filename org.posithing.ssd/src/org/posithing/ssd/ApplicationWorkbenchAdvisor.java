package org.posithing.ssd;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.posithing.ssd.constants.SSMConstants;
import org.posithing.ssd.model.IdentifiableExtension;
import org.posithing.ssd.model.SchoolDataProvider;
import org.posithing.ssd.model.ShapeProvider;
import org.posithing.ssd.preferences.ExtensionManager;
import org.posithing.ssd.ui.perspective.SchoolDataPerspective;
import org.posithing.ssd.utils.Messenger;
import org.posithing.ssd.utils.StringUtil;


public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static Log LOG = LogFactory.getLog(ApplicationWorkbenchAdvisor.class);

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return SchoolDataPerspective.ID;
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);

		configurer.setSaveAndRestore(true);

		initializeExtensions();
	}

	private void initializeExtensions() {

		initializeShapeProviders();
		initializeDataProviders();
	}

	private void initializeShapeProviders() {

		final List<ShapeProvider> ready = new LinkedList<ShapeProvider>();

		try {
			IConfigurationElement[] spConfig = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(SSMConstants.EXT_SHAPEPROVIDER_ID);
			if (spConfig.length == 0) {
				Messenger
						.openWarningBox(
								"No Shape Providers",
								"There were no shape providers found. You will not be able to use shapes for the different time slots.");
			} else {
				for (IConfigurationElement e : spConfig) {
					final Object o = e.createExecutableExtension("class");
					if (o instanceof ShapeProvider) {
						ready.add((ShapeProvider) o);
					}
				}
			}
		} catch (Exception e) {

			StringBuilder sb = new StringBuilder();
			sb.append("Could not propertly initialize all the Shape Providers. ");

			if (ready.size() > 0) {
				sb.append("Successfully initialized Shape Providers:\n");
				for (int i = 0; i < ready.size(); i++) {
					ShapeProvider shapeProvider = ready.get(i);
					addIdentifiable(sb, shapeProvider);
					sb.append(StringUtil.NEWLINE);
				}
			}

			Messenger.openExceptionBox(e, "Failed Shape Providers ", sb.toString());
		} finally {

			// TODO: fix setting the default data provider
			if (ready.size() > 0) {

				ExtensionManager extensionManager = ExtensionManager.getInstance();
				for (ShapeProvider shapeProvider : ready) {
					extensionManager.addShapeProvider(shapeProvider);

					StringBuilder sb = new StringBuilder();
					addIdentifiable(sb, shapeProvider);
					LOG.info("Added Shape Provider: " + sb.toString());
				}
			}
		}
	}

	private void initializeDataProviders() {
		final List<SchoolDataProvider> sdps = new LinkedList<SchoolDataProvider>();
		final List<SchoolDataProvider> tobe = new LinkedList<SchoolDataProvider>();

		try {
			IConfigurationElement[] sdpConfig = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(SSMConstants.EXT_SCHOOLDATAPROVIDER_ID);

			if (sdpConfig.length == 0) {
				Messenger
						.openWarningBox("No Data Providers",
								"There were no data providers found. You will not be able to save your data.");
			} else {

				for (IConfigurationElement e : sdpConfig) {
					final Object o = e.createExecutableExtension("class");
					if (o instanceof SchoolDataProvider) {
						ISafeRunnable runnable = new ISafeRunnable() {

							private SchoolDataProvider sdp;

							@Override
							public void handleException(Throwable exception) {

								StringBuilder sb = new StringBuilder();

								sb.append("Could not initialize the Data Provider: ");
								sb.append(StringUtil.NEWLINE);
								sb.append(sdp.getName());
								sb.append(StringUtil.OPENED_BRACKET_SQUARE_WITH_SPACE);
								sb.append(sdp.getId());
								sb.append(StringUtil.CLOSED_BRACKET_SQUARE);

								Messenger.openExceptionBox(exception,
										"Data Provider not initialized", sb.toString());
							}

							@Override
							public void run() throws Exception {
								if (o instanceof SchoolDataProvider) {
									sdp = (SchoolDataProvider) o;
									tobe.add(sdp);

									if (sdps.contains(sdp)) {
										Messenger.openWarningBox("Data Provider already exists",
												"A data provider with the same id is already registered. Id: "
														+ sdp.getId());
									}

									sdp.initialize();
									tobe.remove(sdp);
									sdps.add(sdp);
								}
							}
						};
						SafeRunner.run(runnable);
					}
				}
			}

		} catch (Exception ex) {

			StringBuilder sb = new StringBuilder();
			sb.append("Could not propertly initialize all the Data Providers. ");

			if (sdps.size() > 0) {
				sb.append("Successfully initialized Data Providers:\n");
				for (int i = 0; i < sdps.size(); i++) {
					SchoolDataProvider schoolDataProvider = sdps.get(i);
					addIdentifiable(sb, schoolDataProvider);
					sb.append(StringUtil.NEWLINE);
				}
			}

			sb.append(StringUtil.NEWLINE);
			sb.append("Failed Data Providers:\n");

			Messenger.openExceptionBox(ex, "Failed initilizing the Data Providers ", sb.toString());

		} finally {

			// TODO: fix setting the default data provider
			if (sdps.size() > 0) {

				ExtensionManager extensionManager = ExtensionManager.getInstance();
				for (SchoolDataProvider schoolDataProvider : tobe) {
					extensionManager.addSchoolDataProvider(schoolDataProvider);

					StringBuilder sb = new StringBuilder();
					addIdentifiable(sb, schoolDataProvider);
					LOG.info("Added Shool Data Provider: " + sb.toString());
				}

				SchoolDataProvider defSchoolDataProvider = sdps.get(0);
				extensionManager.setDefaultSchoolDataProvider(defSchoolDataProvider);

				StringBuilder sb = new StringBuilder();
				addIdentifiable(sb, defSchoolDataProvider);
				LOG.info("Added Shool Data Provider (default): " + sb.toString());
			}
		}
	}

	private void addIdentifiable(StringBuilder sb, IdentifiableExtension identifiable) {
		sb.append(identifiable.getName());
		sb.append(StringUtil.OPENED_BRACKET_SQUARE_WITH_SPACE);
		sb.append(identifiable.getId());
		sb.append(StringUtil.CLOSED_BRACKET_SQUARE);
	}
}
