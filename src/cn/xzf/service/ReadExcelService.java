package cn.xzf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import jodd.io.FileNameUtil;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class ReadExcelService {
	private File file;
	private String excelType;

	public ReadExcelService(String fileName) {
		file = new File(fileName);
		excelType = FileNameUtil.getExtension(file.getName()).toLowerCase();
	}

	public int getExcelSheetCount() {
		int sheetCount = 0;
		if ("xlsx".equalsIgnoreCase(excelType)) {
			try {
				OPCPackage pkg = OPCPackage.open(file);
				XSSFReader reader = new XSSFReader(pkg);
				Iterator<InputStream> worksheets;
				worksheets = reader.getSheetsData();
				for (; worksheets.hasNext(); sheetCount++) {
					InputStream inputStream = worksheets.next();
					inputStream.close();
				}
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OpenXML4JException e) {
				e.printStackTrace();
			}
		} else if ("xls".equalsIgnoreCase(excelType)) {
			Excel2003SheetListen sheetListen = new Excel2003SheetListen(file);
			sheetCount = sheetListen.getSheetCount();
		}
		return sheetCount;
	}

	public List<List<Object>> excelToList(int sheetNumber) {
		List<List<Object>> results = null;
		if ("xls".equalsIgnoreCase(excelType)) {
			results = excel2003ToList(sheetNumber);
		} else if ("xlsx".equalsIgnoreCase(excelType)) {
			results = excel2007ToList(sheetNumber);
		}
		return results;
	}

	/**
	 * 
	 * 读取2007兼容版的EXCEL文件的内容，以List方式返回<br>
	 * 
	 * @param fileName
	 *            2007兼容版EXCEL文件名
	 * @param sheetNumber
	 *            读取sheet的编号（以0开始，-1为读取所有的sheet）
	 * @return 返回文件中的内容<br>
	 */
	public List<List<Object>> excel2007ToList(int sheetNumber) {
		final List<List<Object>> results = new ArrayList<List<Object>>();

		SheetContentsHandler sheetContentsHandler = new SheetContentsHandler() {
			List<Object> row = null;

			public void startRow(int rowNum) {
				row = new ArrayList<Object>();
			}

			public void headerFooter(String text, boolean isHeader,
					String tagName) {

			}

			public void endRow(int rowNum) {
				results.add(rowNum, row);
			}

			public void cell(String cellReference, String formattedValue,
					XSSFComment comment) {
				int i = nameToColumn(cellReference.split("[0-9]*$")[0]);
				if (row.size() <= i) {
					row.addAll(Arrays.asList(new Object[i - row.size() + 1]));
				}
				row.set(i, formattedValue);
			}
		};

		try {
			OPCPackage pkg = OPCPackage.open(file);
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			XMLReader sheetParser = saxFactory.newSAXParser().getXMLReader();
			ReadOnlySharedStringsTable rsst = new ReadOnlySharedStringsTable(
					pkg);
			XSSFReader reader = new XSSFReader(pkg);
			StylesTable stylesTable = reader.getStylesTable();
			Iterator<InputStream> worksheets;

			worksheets = reader.getSheetsData();
			for (int i = 0; worksheets.hasNext(); i++) {
				InputStream inputStream = worksheets.next();
				if (sheetNumber == -1 || i == sheetNumber) {
					ContentHandler handler = new XSSFSheetXMLHandler(
							stylesTable, rsst, sheetContentsHandler, false);
					sheetParser.setContentHandler(handler);
					sheetParser.parse(new InputSource(inputStream));
				}
				inputStream.close();
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (OpenXML4JException e) {
			e.printStackTrace();
		}

		return results;
	}

	private int nameToColumn(String name) {
		int column = -1;
		for (int i = 0; i < name.length(); ++i) {
			int c = name.charAt(i);
			column = (column + 1) * 26 + c - 'A';
		}
		return column;
	}

	/**
	 * 
	 * 读取2003兼容版的EXCEL文件的内容，以List方式返回<br>
	 * 
	 * @param fileName
	 *            2003兼容版EXCEL文件名
	 * @param sheetNumber
	 *            读取sheet的编号（以0开始，-1为读取所有的sheet）
	 * @return 返回文件中的内容<br>
	 */
	public List<List<Object>> excel2003ToList(int sheetNumber) {
		Excel2003Listener listenter = new Excel2003Listener();
		return listenter.excel2003ToList(file, sheetNumber);
	}

	private class Excel2003Listener implements HSSFListener {
		List<List<Object>> results = new ArrayList<List<Object>>();
		private int sheetNumber;
		private POIFSFileSystem fs;

		/** Should we output the formula, or the value it has? */
		private boolean outputFormulaValues = true;

		/** For parsing Formulas */
		private SheetRecordCollectingListener workbookBuildingListener;
		private HSSFWorkbook stubWorkbook;

		// Records we pick up as we process
		private SSTRecord sstRecord;
		private FormatTrackingHSSFListener formatListener;

		/** So we known which sheet we're on */
		private int sheetIndex = -1;
		/*
		 * private BoundSheetRecord[] orderedBSRs; private
		 * List<BoundSheetRecord> boundSheetRecords = new
		 * ArrayList<BoundSheetRecord>();
		 */

		// For handling formulas with string results
		/*
		 * private int nextRow; private int nextColumn; private boolean
		 * outputNextStringRecord;
		 */

		private Workbook workbook;

		List<Object> listRow = new ArrayList<Object>();

		public void processRecord(Record record) {
			int thisRow = -1;
			int thisColumn = -1;
			String thisStr = null;
			switch (record.getSid()) {
			case BoundSheetRecord.sid:
				// 处理新的sheet
				/*
				 * BoundSheetRecord bsr = (BoundSheetRecord) record;
				 * boundSheetRecords.add(bsr);
				 */
				// System.out.println("New sheet named: " +
				// bsr.getSheetname());
				break;
			case BOFRecord.sid:
				BOFRecord br = (BOFRecord) record;
				if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
					sheetIndex++;
					// Create sub workbook if required
					if (workbookBuildingListener != null
							&& stubWorkbook == null) {
						stubWorkbook = workbookBuildingListener
								.getStubHSSFWorkbook();
					}
					// Output the worksheet name
					// Works by ordering the BSRs by the location of
					// their BOFRecords, and then knowing that we
					// process BOFRecords in byte offset order

					/*
					 * if (orderedBSRs == null) { orderedBSRs = BoundSheetRecord
					 * .orderByBofPosition(boundSheetRecords); }
					 */
					// output.println(orderedBSRs[sheetIndex].getSheetname()
					// + " [" + (sheetIndex + 1) + "]:");

				}
				break;

			case SSTRecord.sid:
				sstRecord = (SSTRecord) record;
				break;

			case BlankRecord.sid:
				BlankRecord brec = (BlankRecord) record;

				thisRow = brec.getRow();
				thisColumn = brec.getColumn();
				listRow = addListRow(listRow, thisColumn, "");
				break;
			case BoolErrRecord.sid:
				BoolErrRecord berec = (BoolErrRecord) record;

				thisRow = berec.getRow();
				thisColumn = berec.getColumn();
				listRow = addListRow(listRow, thisColumn,
						berec.getBooleanValue());
				break;

			case FormulaRecord.sid:
				FormulaRecord frec = (FormulaRecord) record;

				thisRow = frec.getRow();
				thisColumn = frec.getColumn();

				if (outputFormulaValues) {
					Sheet sheet = workbook.getSheetAt(sheetIndex);
					Row row = sheet.getRow(thisRow);
					Cell cell = row.getCell(thisColumn);
					FormulaEvaluator evaluator = workbook.getCreationHelper()
							.createFormulaEvaluator();
					CellValue cellValue = evaluator.evaluate(cell);
					switch (cellValue.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						listRow = addListRow(listRow, thisColumn,
								cellValue.getBooleanValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						listRow = addListRow(listRow, thisColumn,
								cellValue.getNumberValue());
						break;
					case Cell.CELL_TYPE_STRING:
						listRow = addListRow(listRow, thisColumn,
								cellValue.getStringValue());
						break;
					case Cell.CELL_TYPE_BLANK:
						listRow = addListRow(listRow, thisColumn, null);
						break;
					case Cell.CELL_TYPE_ERROR:
						listRow = addListRow(listRow, thisColumn, null);
						break;
					// CELL_TYPE_FORMULA will never happen
					case Cell.CELL_TYPE_FORMULA:
						break;
					}

					/*
					 * if (Double.isNaN(frec.getValue())) { // Formula result is
					 * a string // This is stored in the next record
					 * outputNextStringRecord = true; nextRow = frec.getRow();
					 * nextColumn = frec.getColumn(); } else { thisStr =
					 * formatListener.formatNumberDateCell(frec); } thisStr =
					 * formatListener.formatNumberDateCell(frec);
					 */
				} else {
					thisStr = HSSFFormulaParser.toFormulaString(stubWorkbook,
							frec.getParsedExpression());
					listRow = addListRow(listRow, thisColumn, thisStr);
				}
				break;
			case StringRecord.sid:
				/*
				 * if (outputNextStringRecord) { // String for formula
				 * StringRecord srec = (StringRecord) record; thisStr =
				 * srec.getString(); thisRow = nextRow; thisColumn = nextColumn;
				 * outputNextStringRecord = false; }
				 */
				break;

			case LabelRecord.sid:
				LabelRecord lrec = (LabelRecord) record;

				thisRow = lrec.getRow();
				thisColumn = lrec.getColumn();
				listRow = addListRow(listRow, thisColumn, lrec.getValue());
				break;
			case LabelSSTRecord.sid:
				LabelSSTRecord lsrec = (LabelSSTRecord) record;

				thisRow = lsrec.getRow();
				thisColumn = lsrec.getColumn();
				if (sstRecord == null) {
					listRow = addListRow(listRow, thisColumn, null);
				} else {
					listRow = addListRow(listRow, thisColumn,
							sstRecord.getString(lsrec.getSSTIndex()));
				}
				break;
			case NoteRecord.sid:
				/*
				 * NoteRecord nrec = (NoteRecord) record;
				 * 
				 * thisRow = nrec.getRow(); thisColumn = nrec.getColumn();
				 */
				break;
			case NumberRecord.sid:
				NumberRecord numrec = (NumberRecord) record;

				thisRow = numrec.getRow();
				thisColumn = numrec.getColumn();

				// Format
				thisStr = formatListener.formatNumberDateCell(numrec);
				listRow = addListRow(listRow, thisColumn, thisStr);
				break;
			case RKRecord.sid:
				/*
				 * RKRecord rkrec = (RKRecord) record;
				 * 
				 * thisRow = rkrec.getRow(); thisColumn = rkrec.getColumn();
				 */
				break;
			default:
				break;
			}

			// Handle missing column
			if (record instanceof MissingCellDummyRecord) {
				MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
				thisRow = mc.getRow();
				thisColumn = mc.getColumn();
				listRow = addListRow(listRow, thisColumn, null);
			}

			// Handle end of row
			if (record instanceof LastCellOfRowDummyRecord) {
				// We're onto a new row
				// End the row
				if (sheetNumber == -1 || sheetNumber == sheetIndex) {
					results.add(listRow);
					listRow = new ArrayList<Object>();
				}
			}

		}

		public List<List<Object>> excel2003ToList(File file, int sheetNumber) {
			this.sheetNumber = sheetNumber;
			HSSFEventFactory factory = new HSSFEventFactory();
			HSSFRequest request = new HSSFRequest();

			MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(
					this);

			formatListener = new FormatTrackingHSSFListener(listener);
			if (outputFormulaValues) {
				request.addListenerForAllRecords(formatListener);
			} else {
				workbookBuildingListener = new SheetRecordCollectingListener(
						formatListener);
				request.addListenerForAllRecords(workbookBuildingListener);
			}

			try {
				FileInputStream fis = new FileInputStream(file);
				fs = new POIFSFileSystem(fis);
				workbook = new HSSFWorkbook(fs);
				factory.processWorkbookEvents(request, fs);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return results;
		}

		private List<Object> addListRow(List<Object> row, int i, Object value) {
			if (sheetNumber == -1 || sheetNumber == sheetIndex) {
				if (row.size() <= i) {
					row.addAll(Arrays.asList(new Object[i - row.size() + 1]));
				}
				row.set(i, value);
			}
			return row;
		}
	}

	private class Excel2003SheetListen implements HSSFListener {
		private int sheetCount = 0;

		public Excel2003SheetListen(File file) {
			try {
				FileInputStream fis = new FileInputStream(file);
				HSSFRequest req = new HSSFRequest();
				req.addListenerForAllRecords(this);
				HSSFEventFactory factory = new HSSFEventFactory();
				POIFSFileSystem fs = new POIFSFileSystem(fis);
				factory.processWorkbookEvents(req, fs);
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void processRecord(Record record) {
			switch (record.getSid()) {
			case BOFRecord.sid:
				BOFRecord br = (BOFRecord) record;
				if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
					sheetCount++;
				}
				break;
			default:
				break;
			}
		}

		public int getSheetCount() {
			return sheetCount;
		}
	}

}
