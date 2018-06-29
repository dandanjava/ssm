package com.gbq.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gbq.vo.ExcelColum;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.multipart.MultipartFile;


/**
 * @ClassName: ExcelUtils
 * @Description: excel工具类
 * @author Kai.Zhang
 * @date 2017年06月22日
 */
public final class ExcelUtils {

	/*最大导出行数*/
	private static final int MAX_OUTPUT_ROW_COUNT = 10000;

	private ExcelUtils(){}
	
	public static void customExport(String[] titleArr,String[] fieldNameArr, List<?> list, String fileName, HttpServletResponse response){
//		HttpSession session= ActionUtil.getRequest().getSession();
//		//为前端进度条使用：template表示开始加载excel模板
//		session.setAttribute("exportedFlag","template");
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Sheet1");
		/*如果list为空则返回空excel文件*/
		if( !list.isEmpty() ){
			Class<?> clazz = list.get(0).getClass();
			Row titleRow = sheet.createRow(0);
			/*设置标题居中*/
			CellStyle titleCellStyle = wb.createCellStyle();
			titleCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			/*设置标题加粗*/
			Font titleCellStyleFont = wb.createFont();
			titleCellStyleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			titleCellStyle.setFont( titleCellStyleFont );
			/*解析excel-bean获取map*/
			Map<String, Object> map = ExcelUtils.dom2Map(clazz);
			/*循环生成标题行*/
			for (int i = 0; i < titleArr.length; i++) {
				Cell titleCell = titleRow.createCell( i );
				String titleName = titleArr[i];
				titleCell.setCellValue( titleName );
				titleCell.setCellStyle( titleCellStyle );
				/*设置标题列宽度*/
				int colWidth = titleName.length()*1024 ;
				sheet.setColumnWidth( i, colWidth );
			}
			int percent=0;//上一次写入session的百分比
			float currentPercent=0.00f;//当前的百分比
			//为前端进度条使用：write表示正在写入，逗号后面表示写入的百分比
//			session.setAttribute("exportedFlag","write,0");
			/*通过list循环生成每行数据*/
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i+1);
				/*通过map循环生成行内每个单元格*/
				for (int j = 0; j < fieldNameArr.length; j++) {
					/*获得实体类的字段名*/
					String value = getFieldValueByName(fieldNameArr[j], list.get(i));
					/*创建行内待单元格，并赋值*/
					row.createCell(j).setCellValue(value);
				}
				//为前端进度条使用：算出当前进度百分比与上一次写入session的百分比差值是否大于1，如果大于1，将更新session里的进度值
				currentPercent=((i+1)/(float)list.size())*100;
				if((int)Math.floor(currentPercent)-percent>=1){
					percent= (int) Math.floor(currentPercent);
//					session.setAttribute("exportedFlag","write,"+percent);
				}
			}
		}
		/*Excel文件的输出流*/
		OutputStream output = null;
		response.reset();
		try {
			output = response.getOutputStream();
			/*设置响应编码*/
			response.setCharacterEncoding("utf-8");
			/*设置文件名*/
			response.setHeader("Content-disposition", "attachment; filename="
					+new String(fileName.getBytes("gbk"), "ISO-8859-1")+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx");
			/*设置文件类型*/
			response.setContentType("application/msexcel");
			//为前端进度条使用：写入结束，将从这里开始将数据返回给前端
//			session.setAttribute("exportedFlag","end");
			/*写入到输出流*/
			wb.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if( null != output ){
					output.close();
				}
				//为前端进度条使用：导出结束后删去标识进度条的属性exportedFlag
//				session.removeAttribute("exportedFlag");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 读取excel并返回List
	 * @param uploadFile 上传的excel文件
	 * @param clazz 实体类型
	 * @param startReadIndex 数据开始行号（从1开始计数）
	 * @return 转换后的实体集合
	 */
	public static List<Object> readExcel(MultipartFile uploadFile, Class<?> clazz, int startReadIndex){
		List<Object> resultList = new ArrayList<>();
		InputStream inputStream = null;
		Workbook workbook = null;
		try {
			/*获取excel文件的输入流*/
			inputStream = uploadFile.getInputStream();
			/*输入流转为Workbook对象*/
			workbook = new XSSFWorkbook(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*获取第一个Sheet*/
		if(null==workbook){
			workbook = new XSSFWorkbook();
		}
		Sheet sheet = workbook.getSheetAt(0);
		/*获取行数*/
		int rowCount = sheet.getPhysicalNumberOfRows();
		/*解析excel-bean获取map*/
		Map<String,Object> map = ExcelUtils.dom2Map(clazz);
		/*从第startReadIndex行还是读取*/
		if( startReadIndex < 1 || startReadIndex > rowCount ){
			startReadIndex = 0;
		}
		for (int i = (startReadIndex-1); i < rowCount; i++) {
			Row row = sheet.getRow(i);
			Object obj = null;
			try {
				obj = clazz.newInstance();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			for (Entry<String, Object>  entry : map.entrySet() ) {
				int index = Integer.parseInt(entry.getKey());
				if (row.getCell(index) != null) {
					Cell cell = row.getCell(index);
					ExcelColum e = (ExcelColum) entry.getValue();
					String fieldName = e.getFieldName();
					setFieldValueFromCell(obj, fieldName, cell);
				}
			}
			resultList.add(obj);
		}
		return resultList;
	}

	/**
	 * TUDO : 精简代码，直接调用list2Excel
	 * 通过sql导出excel
	 * @param sql 查询的sql语句
	 * @param tagetClazz 目标对象的Class
	 * @param fileName 导出的文件名
	 * @param response
	 */
//	public static void sql2Excel(String sql, Class<?> tagetClazz, String fileName, HttpServletResponse response){
//		List<?> list = new ArrayList<>();
//		HttpSession session= ActionUtil.getRequest().getSession();
//		//为前端进度条使用：read表示开始查询数据库
//		session.setAttribute("exportedFlag","read");
//		try {
//			list = SpringUtil.getJdbcTemplate().query( "select * from ("+ sql +")  where rownum<="+MAX_OUTPUT_ROW_COUNT, new BeanPropertyRowMapper(tagetClazz));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		//为前端进度条使用：template表示开始加载excel模板
//		session.setAttribute("exportedFlag","template");
//		Workbook wb = new XSSFWorkbook();
//		Sheet sheet = wb.createSheet("Sheet1");
//		/*如果list为空则返回空excel文件*/
//		if( !list.isEmpty() ){
//			Class<?> clazz = list.get(0).getClass();
//			Row titleRow = sheet.createRow(0);
//			/*设置标题居中*/
//			CellStyle titleCellStyle = wb.createCellStyle();
//			titleCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//			/*设置标题加粗*/
//			Font titleCellStyleFont = wb.createFont();
//			titleCellStyleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
//			titleCellStyle.setFont( titleCellStyleFont );
//			/*解析excel-bean获取map*/
//			Map<String, Object> map = ExcelUtils.dom2Map(clazz);
//			/*循环生成标题行*/
//
//			for (Entry<String, Object>  entry : map.entrySet() ) {
//				int columIndex = Integer.parseInt(entry.getKey());
//				Cell titleCell = titleRow.createCell( columIndex );
//				String columName = ((ExcelColum)entry.getValue()).getColumnName();
//				titleCell.setCellValue( columName );
//				titleCell.setCellStyle( titleCellStyle );
//				/*设置标题列宽度*/
//				int colWidth = columName.length()*1024 ;
//				sheet.setColumnWidth( columIndex, colWidth );
//			}
//
//			int percent=0;//上一次写入session的百分比
//			float currentPercent=0.00f;//当前的百分比
//			//为前端进度条使用：write表示正在写入，逗号后面表示写入的百分比
//			session.setAttribute("exportedFlag","write,0");
//
//			/*通过list循环生成每行数据*/
//			for (int i = 0; i < list.size(); i++) {
//				Row row = sheet.createRow(i+1);
//				/*通过map循环生成行内每个单元格*/
//				for (Entry<String, Object>  entry : map.entrySet() ) {
//					/*获得实体类的字段名*/
//					String fieldName = ((ExcelColum)entry.getValue()).getFieldName();
//					String value = getFieldValueByName(fieldName, list.get(i));
//					/*创建行内待单元格，并赋值*/
//					row.createCell(Integer.valueOf(entry.getKey())).setCellValue(value);
//				}
//				//为前端进度条使用：算出当前进度百分比与上一次写入session的百分比差值是否大于1，如果大于1，将更新session里的进度值
//				currentPercent=((i+1)/(float)list.size())*100;
//				if((int)Math.floor(currentPercent)-percent>=1){
//					percent= (int) Math.floor(currentPercent);
//					session.setAttribute("exportedFlag","write,"+percent);
//				}
//			}
//		}
//
//		/*Excel文件的输出流*/
//		OutputStream output = null;
//		response.reset();
//		try {
//			output = response.getOutputStream();
//			/*设置响应编码*/
//			response.setCharacterEncoding("utf-8");
//			/*设置文件名*/
//			response.setHeader("Content-disposition", "attachment; filename="
//					+new String(fileName.getBytes("gbk"), "ISO-8859-1")+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx");
//			/*设置文件类型*/
//			response.setContentType("application/msexcel");
//			//为前端进度条使用：写入结束，将从这里开始将数据返回给前端
//			session.setAttribute("exportedFlag","end");
//			/*写入到输出流*/
//			wb.write(output);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				if(null != output){
//					output.close();
//				}
//				//为前端进度条使用：导出结束后删去标识进度条的属性exportedFlag
//				session.removeAttribute("exportedFlag");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	/**
	 * List2Excel并输出
	 * @param
	 * @param list 实体对象集合
	 * @param
	 * @param response
	 */
	public static void list2Excel(List<?> list, String fileName, HttpServletResponse response){
//		HttpSession session= ActionUtil.getRequest().getSession();
		//为前端进度条使用：template表示开始加载excel模板
//		session.setAttribute("exportedFlag","template");
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Sheet1");
		/*如果list为空则返回空excel文件*/
		if( !list.isEmpty() ){
			Class<?> clazz = list.get(0).getClass();
			Row titleRow = sheet.createRow(0);
			/*设置标题居中*/
			CellStyle titleCellStyle = wb.createCellStyle();
			titleCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			/*设置标题加粗*/
			Font titleCellStyleFont = wb.createFont();
			titleCellStyleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			titleCellStyle.setFont( titleCellStyleFont );
			/*解析excel-bean获取map*/
			Map<String, Object> map = ExcelUtils.dom2Map(clazz);
			/*循环生成标题行*/
			for (Entry<String, Object>  entry : map.entrySet() ) {
				int columIndex = Integer.parseInt(entry.getKey());
				Cell titleCell = titleRow.createCell( columIndex );
				String columName = ((ExcelColum)entry.getValue()).getColumnName();
				titleCell.setCellValue( columName );
				titleCell.setCellStyle( titleCellStyle );
				/*设置标题列宽度*/
				int colWidth = columName.length()*1024 ;
				sheet.setColumnWidth( columIndex, colWidth );
			}
//			int percent=0;//上一次写入session的百分比
//			float currentPercent=0.00f;//当前的百分比
			//为前端进度条使用：write表示正在写入，逗号后面表示写入的百分比
//			session.setAttribute("exportedFlag","write,0");
			/*通过list循环生成每行数据*/
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i+1);
				/*通过map循环生成行内每个单元格*/
				for (Entry<String, Object>  entry : map.entrySet() ) {
					/*获得实体类的字段名*/
					String fieldName = ((ExcelColum)entry.getValue()).getFieldName();
					String value = getFieldValueByName(fieldName, list.get(i));
					/*创建行内待单元格，并赋值*/
					row.createCell(Integer.valueOf(entry.getKey())).setCellValue(value);
				}
				//为前端进度条使用：算出当前进度百分比与上一次写入session的百分比差值是否大于1，如果大于1，将更新session里的进度值
//				currentPercent=((i+1)/(float)list.size())*100;
//				if((int)Math.floor(currentPercent)-percent>=1){
//					percent= (int) Math.floor(currentPercent);
//					session.setAttribute("exportedFlag","write,"+percent);
//				}
			}
		}
		/*Excel文件的输出流*/
		OutputStream output = null;
		response.reset();
		try {
			output = response.getOutputStream();
			/*设置响应编码*/
			response.setCharacterEncoding("utf-8");
			/*设置文件名*/
			response.setHeader("Content-disposition", "attachment; filename="
					+new String(fileName.getBytes("gbk"), "ISO-8859-1")+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xlsx");
			/*设置文件类型*/
			response.setContentType("application/msexcel");
			//为前端进度条使用：写入结束，将从这里开始将数据返回给前端
//			session.setAttribute("exportedFlag","end");
			/*写入到输出流*/
			wb.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if( null != output ){
					output.close();
				}
				//为前端进度条使用：导出结束后删去标识进度条的属性exportedFlag
//				session.removeAttribute("exportedFlag");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 从excel-bean配置文件中读取数据，返回map
	 * @param
	 * @return map { key : 列的索引, value : ExcelColum对象{ columnName : 列名, fieldName : 字段名称 } }
	 */
	private static Map<String,Object> dom2Map(Class<?> clazz){
		Map<String,Object> map = new LinkedHashMap<>();
		String clazzPathName = clazz.getName();
        SAXReader reader = new SAXReader();
        Document document;
        Element root = null;
        List<Element> excelElelist = new ArrayList<>();
		try {
			document = reader.read(new ClassPathResource("excelBean/excel-bean.xml").getFile());
			root = document.getRootElement();
			excelElelist = root.elements();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Element excelEle : excelElelist) {
			/*获取xml中配置的类完全限定名*/
			String classAttVla = excelEle.attribute("class").getValue();
			if(clazzPathName.equals(classAttVla)){
				List<Element> ColumElelist = excelEle.elements();
				for (int i = 0; i < ColumElelist.size(); i++) {
					String fieldName = ColumElelist.get(i).element("fieldName").getText();
					String columnName = ColumElelist.get(i).element("columnName").getText();
					ExcelColum excelColum = new ExcelColum();
					excelColum.setFieldName(fieldName);
					excelColum.setColumnName(columnName);
					map.put(String.valueOf(i), excelColum);
				}
			}
		}
		return map;
	}

	/**
	 * 通过字段类型和单元格对象获取对应的值
	 * @param typeClazz 字段类型
	 * @param cell 单元格对象
	 * @return 单元格的值
	 */
	private static Object getValueByTypeAndCell(Cell cell, Class<?> typeClazz){
		Object value = null;
		if( String.class == typeClazz ){
			cell.setCellType(Cell.CELL_TYPE_STRING );
			value = cell.getStringCellValue();
		}else if( Integer.class == typeClazz ||  int.class== typeClazz ){
			cell.setCellType(Cell.CELL_TYPE_STRING );
			value = Integer.valueOf(cell.getStringCellValue());
		}else if( Boolean.class== typeClazz ){
			if( cell.getCellType() != Cell.CELL_TYPE_BOOLEAN ){
				cell.setCellType(Cell.CELL_TYPE_BOOLEAN );
			}
			value = cell.getBooleanCellValue();
		}else if( Double.class== typeClazz ||  double.class== typeClazz  ){
			if( cell.getCellType() != Cell.CELL_TYPE_NUMERIC ){
				cell.setCellType(Cell.CELL_TYPE_STRING );
				value = Double.valueOf(cell.getStringCellValue());
			}else{
				value =  cell.getNumericCellValue();
			}
		}else if( Date.class== typeClazz ){
			if( cell.getCellType() != Cell.CELL_TYPE_NUMERIC ){
				cell.setCellType(Cell.CELL_TYPE_STRING );
				try {
					String valueStr = cell.getStringCellValue();
					value = Pattern.matches("^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30))) (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$", valueStr)
								? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(valueStr) : null;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				value = cell.getDateCellValue();
			}
		}
		return value;
	}

	/**
	 * 从cell读取数据，并赋值给对象
	 * @param obj
	 * @param fieldName
	 * @param cell
	 */
	private static void setFieldValueFromCell(Object obj, String fieldName, Cell cell) {
		Class<?> typeClazz = getDeclaredField(obj.getClass(), fieldName).getType();
		Object value = getValueByTypeAndCell(cell, typeClazz);
		String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			Method method = getDeclaredMethod(obj, methodName, typeClazz);
			method.invoke(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 利用反射，根据属性名获取属性值
	 * @param fieldName 字段名称
	 * @param targetObj 目标对象
	 * @return 属性值
	 */
	private static String getFieldValueByName(String fieldName, Object targetObj) {
		String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			Method method = getDeclaredMethod(targetObj, methodName, null);
			Object value = method.invoke(targetObj);
			if( null == value ){
				return "";
			}
			if( Date.class == value.getClass() ){
//				return DateUtils.dateTimeToString( (Date)value );
				return "";
			}
			return String.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 对应有继承情况返回method
	 * @param object
	 * @param methodName
	 * @param typeClazz
	 * @return
	 */
	private static Method getDeclaredMethod(Object object, String methodName, Class<?> typeClazz){
		Method method = null ;
		Class<?> clazz = object.getClass();
		try {
			method = null==typeClazz?clazz.getDeclaredMethod(methodName):clazz.getDeclaredMethod(methodName, typeClazz);
		} catch (Exception e) {
			try {
				method = null==typeClazz?clazz.getSuperclass().getDeclaredMethod(methodName):clazz.getSuperclass().getDeclaredMethod(methodName, typeClazz);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return method;
    }

	/**
	 * 对应有继承情况返回field
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	private static Field getDeclaredField( Class<?> clazz, String fieldName){
		Field field = null ;
		try {
			field = clazz.getDeclaredField(fieldName) ;
		} catch (Exception e) {
			try {
				field = clazz.getSuperclass().getDeclaredField(fieldName) ;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return field;
    }
}
