package com.techlords.crown.mvc.flow;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.techlords.crown.mvc.util.CrownFileNameFilter;
import com.techlords.infra.config.CrownFlow;
import com.techlords.infra.config.FlowSteps;
import com.techlords.infra.config.Step;

public final class CrownFlowService {

	private static final Logger LOGGER = Logger.getLogger(CrownFlowService.class);
	private static final Map<String, CrownFlow> CROWNFLOW_MAP = new HashMap<String, CrownFlow>();
	private static final Properties UNIQUEFIELD_MAP = new Properties();
	public final static CrownFlowService INSTANCE = new CrownFlowService();

	private CrownFlowService() {
		loadFlowConfigFiles();
		loadUniqueFields();
	}

	private void loadUniqueFields() {
		try {
			UNIQUEFIELD_MAP.load(CrownFlowService.class
					.getResourceAsStream("unique-fields.properties"));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	private void loadFlowConfigFiles() {
		try {
			final JAXBContext context = JAXBContext.newInstance(CrownFlow.class
					.getPackage().getName(), CrownFlow.class.getClassLoader());
			final Unmarshaller unmarshaller = context.createUnmarshaller();
			final File currentDir = new File(getClass().getResource("").toURI());
			final File[] flowFiles = currentDir
					.listFiles(new CrownFileNameFilter(
							"Crown flow XML Documents", "xml"));
			for (final File flowFile : flowFiles) {
				final CrownFlow crownFlow = (CrownFlow) unmarshaller
						.unmarshal(flowFile);
				CROWNFLOW_MAP.put(crownFlow.getFlowClass(), crownFlow);
			}
		} catch (JAXBException e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	public boolean isFirstStep(String flowClass, int stepID) {
		final Step step = getStep(flowClass, stepID);
		if (step == null) {
			return false;
		}
		return step.isFirstStep();
	}

	public List<Integer> getNextSteps(String flowClass, int stepID) {
		final Step step = getStep(flowClass, stepID);
		if (step == null) {
			return null;
		}
		return step.getNextSteps();
	}

	public Integer getFirstStepID(String flowClass) {
		final List<Step> steps = getAllSteps(flowClass);
		for (final Step step : steps) {
			if (step.isFirstStep()) {
				return step.getStepId();
			}
		}
		return -1;
	}

	private List<Step> getAllSteps(final String flowClass) {
		final CrownFlow crownFlow = CROWNFLOW_MAP.get(flowClass);
		final FlowSteps flowSteps = crownFlow.getFlowSteps();
		return flowSteps.getSteps();
	}

	private Step getStep(final String flowClass, final int stepID) {
		final List<Step> steps = getAllSteps(flowClass);
		for (final Step step : steps) {
			if (stepID == step.getStepId()) {
				return step;
			}
		}
		return null;
	}

	public String getTableName(String uniqueField) {
		return UNIQUEFIELD_MAP.getProperty(uniqueField);
	}
}
