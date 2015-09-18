package com.techlords.crown.mvc.reports.config;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.techlords.crown.mvc.util.CrownFileNameFilter;

public final class CrownReportService {
	private static final Map<String, CrownReport> CROWNREPORT_MAP = new HashMap<String, CrownReport>();
	public final static CrownReportService INSTANCE = new CrownReportService();

	private CrownReportService() {
		loadReportConfigFiles();
	}

	private void loadReportConfigFiles() {
		try {
			final JAXBContext context = JAXBContext.newInstance(CrownReport.class
					.getPackage().getName(), CrownReport.class.getClassLoader());
			final Unmarshaller unmarshaller = context.createUnmarshaller();
			final File currentDir = new File(getClass().getResource("").toURI());
			final File[] reportFiles = currentDir
					.listFiles(new CrownFileNameFilter(
							"Crown Report XML Documents", "xml"));
			for (final File reportFile : reportFiles) {
				final CrownReport crownReport = (CrownReport) unmarshaller
						.unmarshal(reportFile);
				CROWNREPORT_MAP.put(crownReport.getBeanName(), crownReport);
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public CrownReport getReportTemplate(String beanName) {
		return CROWNREPORT_MAP.get(beanName);
	}

	public static void main(String[] args) throws Exception {

		for (String flowClass : CROWNREPORT_MAP.keySet()) {
			System.out.println("CLASS ::: " + flowClass);
			CrownReport report = CROWNREPORT_MAP.get(flowClass);
			System.out.println("FLOW ::: " + report);
		}
	}
}
