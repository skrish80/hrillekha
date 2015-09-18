package com.techlords.crown.mvc.reports;


public final class CrownReportGenerator {
//	private static final Color LIGHT_BG = new Color(203, 224, 253);
//	private static final Color DARK_BG = new Color(49, 133, 247);
//
//	private Style detailStyle;
//	private Style headerStyle;
//	private Style headerVariables;
//	private Style groupVariables;
//	private Style titleStyle;
//	private Style importStyle;
//	private Style oddRowStyle;
//
//	public static final CrownReportGenerator INSTANCE = new CrownReportGenerator();
//
//	private CrownReportGenerator() {
//		loadStyles();
//	}
//
//	private void loadStyles() {
//		detailStyle = new Style("detail");
//		detailStyle.setFont(Font.VERDANA_SMALL);
//		detailStyle.setPaddingLeft(5);
//		detailStyle.setHorizontalAlign(HorizontalAlign.CENTER);
//		detailStyle.setVerticalAlign(VerticalAlign.MIDDLE);
//
//		headerStyle = new Style("header");
//		headerStyle.setFont(Font.VERDANA_MEDIUM_BOLD);
//		headerStyle.setBorderBottom(Border.PEN_1_POINT);
//		headerStyle.setBackgroundColor(DARK_BG);
//		headerStyle.setTextColor(Color.WHITE);
//		headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
//		headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
//		headerStyle.setTransparency(Transparency.OPAQUE);
//
//		headerVariables = new Style("headerVariables");
//		headerVariables.setFont(Font.VERDANA_BIG_BOLD);
//		headerVariables.setTextColor(DARK_BG);
//		headerVariables.setBorderBottom(Border.THIN);
//		headerVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
//		headerVariables.setVerticalAlign(VerticalAlign.TOP);
//		headerVariables.setStretchWithOverflow(true);
//
//		groupVariables = new Style("groupVariables");
//		groupVariables.setFont(Font.VERDANA_MEDIUM_BOLD);
//		groupVariables.setTextColor(DARK_BG);
//		groupVariables.setBorderBottom(Border.THIN);
//		groupVariables.setBorderColor(DARK_BG);
//		groupVariables.setHorizontalAlign(HorizontalAlign.RIGHT);
//		groupVariables.setPaddingRight(5);
//		groupVariables.setVerticalAlign(VerticalAlign.MIDDLE);
//
//		titleStyle = new Style("titleStyle");
//		titleStyle.setFont(new Font(18, Font._FONT_VERDANA, true));
//		titleStyle.setTextColor(DARK_BG);
//
//		importStyle = new Style();
//		importStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
//
//		oddRowStyle = new Style();
//		oddRowStyle.setBorder(Border.NO_BORDER);
//		oddRowStyle.setBackgroundColor(LIGHT_BG);
//		oddRowStyle.setTransparency(Transparency.OPAQUE);
//	}
//
//	private DynamicReportBuilder createReportBuilder(CrownReport report) {
//		final DynamicReportBuilder reportBuilder = new DynamicReportBuilder();
//		final Integer margin = report.getMargin();
//		reportBuilder
//				.setTitleStyle(titleStyle)
//				.setTitle(report.getTitle())
//				// defines the title of the report
//				.setSubtitle(report.getSubTitle())
//				.setDetailHeight(report.getDetailHeight())
//				.setLeftMargin(margin).setRightMargin(margin)
//				.setTopMargin(margin).setBottomMargin(margin)
//				.setPrintBackgroundOnOddRows(true)
//				.setGrandTotalLegend("Grand Total")
//				.setGrandTotalLegendStyle(headerVariables)
//				.setOddRowBackgroundStyle(oddRowStyle);
//		return reportBuilder;
//	}
//
//	private void createColumns(DynamicReportBuilder reportBuilder,
//			CrownReport report, boolean isGroup) throws ColumnBuilderException {
//		final List<ColumnParams> columnParams = report.getColumnParams();
//
//		for (ColumnParams param : columnParams) {
//			final Style colStyle = param.isCriteriaColumn() && isGroup ? groupVariables
//					: detailStyle;
//			AbstractColumn column = ColumnBuilder
//					.getNew()
//					.setColumnProperty(param.getColumnName(),
//							param.getClassName())
//					.setTitle(param.getColumnTitle())
//					.setWidth(param.getColumnWidth()).setStyle(colStyle)
//					.setHeaderStyle(headerStyle).build();
//			if (param.getClassName().equals("java.util.Date")) {
//				column.setTextFormatter(CrownMVCHelper.DATE_FORMAT);
//			}
//
//			reportBuilder.addColumn(column);
//			if (isGroup) {
//				addGroup(reportBuilder, param, column);
//			}
//		}
//
//	}
//
//	private void addGroup(DynamicReportBuilder builder, ColumnParams param,
//			AbstractColumn column) {
//		final GroupBuilder groupBuilder = new GroupBuilder();
//		if (param.isCriteriaColumn()) {
//			// define the criteria column to group by
//			DJGroup group = groupBuilder.setCriteriaColumn(
//					(PropertyColumn) column).build();
//			builder.addGroup(group);
//		}
//	}
//
//	private JasperPrint generateReport(CrownReport report, Collection<?> data,
//			boolean isGroup) throws ColumnBuilderException, JRException {
//		final DynamicReportBuilder reportBuilder = createReportBuilder(report);
//		createColumns(reportBuilder, report, isGroup);
//
//		reportBuilder.setUseFullPageWidth(true);
//		reportBuilder.addAutoText(AutoText.AUTOTEXT_PAGE_X_SLASH_Y,
//				AutoText.POSITION_FOOTER, AutoText.ALIGNMENT_RIGHT);
//
//		final DynamicReport dynamicReport = reportBuilder.build();
//
//		// Create the data source
//		final Collection<?> sortedCollection = SortUtils.sortCollection(data,
//				dynamicReport.getColumns());
//
//		final JRDataSource dataSource = new JRBeanCollectionDataSource(
//				sortedCollection);
//		return DynamicJasperHelper.generateJasperPrint(dynamicReport,
//				new ClassicLayoutManager(), dataSource);
//	}
//
//	public JasperPrint generateGroupReport(CrownReport report,
//			Collection<?> data) throws ColumnBuilderException, JRException {
//		return generateReport(report, data, true);
//	}
//
//	public JasperPrint generateRegularReport(CrownReport report,
//			Collection<?> data) throws ColumnBuilderException, JRException {
//		return generateReport(report, data, false);
//	}

}
