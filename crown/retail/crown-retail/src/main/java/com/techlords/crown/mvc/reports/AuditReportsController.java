package com.techlords.crown.mvc.reports;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class AuditReportsController {
//	private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
//			"MM/dd/yyyy");
//	private final SimpleDateFormat TITLE_FORMAT = new SimpleDateFormat(
//			"dd-MMM-yyyy");
//
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(value = "/FilteredAuditReport", method = RequestMethod.GET)
//	public String getInvoiceReportPage() {
//		return "reports/FilteredAuditReport";
//	}
//
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(value = "/FilteredAuditReport", method = RequestMethod.POST)
//	public String getInvoiceReportPage(@RequestParam String startDate,
//			@RequestParam String endDate) {
//		String reportServletName = "GetAuditReportFiltered.do";
//		StringBuilder filterParams = new StringBuilder();
//		if (startDate != null && endDate != null) {
//			filterParams.append("startDate*" + startDate);
//			filterParams.append("@endDate*" + endDate);
//		}
//		reportServletName += "?filterParams=" + filterParams.toString();
//		return "redirect:CrownReport.do?reportServletName=" + reportServletName;
//	}
//
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(value = "/GetAuditReport", method = RequestMethod.GET)
//	public void getAuditReport(HttpServletResponse response)
//			throws BuilderException, ClassNotFoundException, JRException {
//
//		final AuditService service = CrownServiceLocator.INSTANCE
//				.getCrownService(AuditService.class);
//
//		final Collection<?> auditData = service.getAllAuditReport();
//
//		final CrownReport report = CrownReportService.INSTANCE
//				.getReportTemplate("CrownAuditBO");
//		report.setSubTitle("Overall Audit Report");
//		final JasperPrint jasperPrint = CrownReportGenerator.INSTANCE
//				.generateGroupReport(report, auditData);
//
//		try {
//			ObjectOutputStream stream = new ObjectOutputStream(
//					response.getOutputStream());
//			stream.writeObject(jasperPrint);
//			stream.flush();
//			stream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@PreAuthorize("isAuthenticated()")
//	@RequestMapping(value = "/GetAuditReportFiltered", method = RequestMethod.GET)
//	public void getAuditReport(HttpServletRequest request,
//			HttpServletResponse response) throws BuilderException,
//			ClassNotFoundException, JRException, ParseException {
//		System.err
//				.println("GVGVGV ::: " + request.getParameter("filterParams"));
//
//		final String start = "startDate*";
//		final String end = "endDate*";
//		final String params = request.getParameter("filterParams");
//		String[] split = params.split("@");
//		String startDate = split[0];
//		int startInd = startDate.indexOf(start);
//		startDate = startDate.substring(startInd + start.length());
//		String endDate = split[1];
//		int endInd = endDate.indexOf(end);
//		endDate = endDate.substring(endInd + end.length());
//
//		final AuditService service = CrownServiceLocator.INSTANCE
//				.getCrownService(AuditService.class);
//
//		Date fromDate = DATE_FORMAT.parse(startDate);
//		Date toDate = new Date();
//		if (endDate != null && endDate.length() > 0) {
//			toDate = DATE_FORMAT.parse(endDate);
//		}
//		final Collection<?> auditData = service.getAllAuditReport(fromDate,
//				toDate);
//
//		final CrownReport report = CrownReportService.INSTANCE
//				.getReportTemplate("CrownAuditBO");
//		report.setSubTitle("Audit Report From " + TITLE_FORMAT.format(fromDate)
//				+ " To " + TITLE_FORMAT.format(toDate));
//		final JasperPrint jasperPrint = CrownReportGenerator.INSTANCE
//				.generateGroupReport(report, auditData);
//
//		try {
//			ObjectOutputStream stream = new ObjectOutputStream(
//					response.getOutputStream());
//			stream.writeObject(jasperPrint);
//			stream.flush();
//			stream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
