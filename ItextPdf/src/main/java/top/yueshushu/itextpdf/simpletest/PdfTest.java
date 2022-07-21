package top.yueshushu.itextpdf.simpletest;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import top.yueshushu.itextpdf.model.MyUser;
import top.yueshushu.itextpdf.simpletest.event.HeaderFooterEvent;
import top.yueshushu.itextpdf.simpletest.event.WaterMarkEvent;
/**
 * ItextPdf 简单的测试
 *
 * @author yuejianli
 * @date 2022-07-21
 */
@Slf4j
public class PdfTest {
	private String PDF_DIRECTORY ;
	
	@Before
	public void init (){
		PDF_DIRECTORY = "D:\\pdf"+ File.separator;
	}
	/**
	  生成一个简单的 pdf 文件，
	 主要用于演示相应的方法.
	 
	 分成五步:
	 
	 */
	@Test
	public void simpleTest() throws Exception{
		//1. 构建文档   com.itextpdf.text.Document;
		Document document = new Document();
		//2. 实例化读取方法
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(PDF_DIRECTORY + "simple.pdf"));
		
		//3. 打开文档， 必须要先 open
		
		document.open();
		
		//4. 设置填充数据  是文档 Document 填充
		// Paragraph 段落
		Paragraph paragraph = new Paragraph();
		paragraph.add("Hello Itext PDF");
		
		
		document.add(paragraph);
		
		//5. 填充元素之后，文档关闭
		document.close();
		
		pdfWriter.close();
		log.info(">>> 生成一个简单的 PDF文件成功");
	}
	
	/**
	 文档  Document 的方式
	 
	 public Document() {
	 		this(PageSize.A4);
	 }
	 public Document(Rectangle pageSize) {
	 		this(pageSize, 36, 36, 36, 36);
	 }
	 public Document(Rectangle pageSize, float marginLeft, float marginRight,
	 float marginTop, float marginBottom) {
		 this.pageSize = pageSize;
		 this.marginLeft = marginLeft;
		 this.marginRight = marginRight;
		 this.marginTop = marginTop;
		 this.marginBottom = marginBottom;
	 }
	 
	 PageSize :
	 
	 llx 左上x 坐标，  lly 左上 y 坐标，     urx 右下x坐标,  ury 右下y 坐标
	public Rectangle(final float llx, final float lly, final float urx, final float ury) {
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
	}
	
	A4 纸结构:
	 
	 public static final Rectangle A4 = new RectangleReadOnly(595,842);
	 
	 
	 public RectangleReadOnly(final float urx, final float ury) {
	 		super(0, 0, urx, ury);
	 }
	 
	 */
	@Test
	public void documentTest(){
		//1. 第一种， 默认  使用的是 A4 值
		
		Document document1 = new Document();
		
		// 也可以指定其它的
		Document document2 = new Document(PageSize.A5);
		
		Document document3 = new Document(PageSize.A4.rotate());
		
		//2. 可以自定义 页面大小
		
		Rectangle pageSize = new Rectangle(0,0,100,100);
		
		Document document4 = new Document(pageSize);
		
		
		//3. 也可以进行指定 margin 边距。  顺序是 左，右，下，下
		// 默认是  36,36,36,36  margin:36
		
		Document document5 = new Document(pageSize,100,100,100,100);
	}
	
	/**
	 PdfWriter 生成器
	 通过 PdfWriter.getInstance() 获取相应的实例
	 */
	@Test
	public void pdfWriterTest() throws Exception{
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"write.pdf"));
	}
	
	/**
	 生成普通的元素，并添加监听器
	 */
	@Test
	public void documentConfigTest() throws Exception{
		//1. 创建 Document
		Document document = new Document();
		
		//2. 生成 PdfWriter 对象
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(PDF_DIRECTORY + "config.pdf"));
		
		
		//3. 开启
		document.open();
		
		//4. 元素
		Paragraph paragraph = new Paragraph();
		paragraph.add("I have a Config");
		
		document.add(paragraph);
		
		// 设置属性
		// 添加主题
		document.addTitle("PDF配置");
		// 添加主题
		document.addAuthor("岳泽霖");
		// 添加创建时期
		document.addCreationDate();
		// 添加语言
		document.addLanguage("汉语");
		// 添加 关键字
		document.addKeywords("PDF,岳泽霖");
		// 添加主题
		document.addSubject("PDF使用");
		//添加创建者
		document.addCreator("岳泽霖");
		
		//5. 关闭
		document.close();
		pdfWriter.close();
		log.info(">>> 创建文件并配置成功");
		
	}
	
	/**
	 PDF 中添加 图片操作
	 */
	@Test
	public void addImageTest() throws Exception{
		Document document = new Document();
		
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"image.pdf"));
		
		document.open();
		
		document.add(new Paragraph("This is old logo"));
		
		// 添加第一张图片 ， 后面跟的是文件的路径
		Image oldImage = Image.getInstance(PDF_DIRECTORY+"ologo.jpg");
		// 设置图片放置的位置
		oldImage.setAbsolutePosition(10,220);
		// 设置图片的宽度和高度
		oldImage.scaleAbsolute(120,200);
		
		// 按照比例进行缩放
		oldImage.scalePercent(80);
		
		document.add(oldImage);
		
		
		document.add(new Paragraph("This is new logo"));
		
		Image newImage = Image.getInstance(PDF_DIRECTORY+"nlogo.jpg");
		
		newImage.setAbsolutePosition(10,30);
		newImage.scaleAbsolute(120,200);
		
		// 设置旋转角度
		newImage.setRotation(50);
		
		document.add(newImage);
		
		document.add(new Paragraph("This is web picture"));
		
		Image webImage = Image.getInstance(new URL("https://img-blog.csdnimg.cn/2020060911013448.png"));
		
		document.add(webImage);
		
		
		
		
		// 放置信息
		document.close();
		pdfWriter.close();
		
		log.info(">>>> PDF 中插入图片成功");
	}
	
	/**
	 插入 list 列表的操作
	 */
	@Test
	public void listOrderTest() throws Exception {
	
		Document document = new Document();
		
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"列表.pdf"));
		
		document.open();
		
		// 添加 orderList 列表
		
		document.add(new Paragraph("add orderList"));
		
		// 有序的。 自动添加   1.  2. 3.
		List orderList = new List(List.ORDERED);
		
		orderList.add(new ListItem("Java"));
		orderList.add(new ListItem("Python"));
		orderList.add(new ListItem("Vue"));
		
		document.add(orderList);
		
		
		document.add(new Paragraph("add no order List"));
		
		// 无序的，  会变成  -  - -
		List noOrderList = new List();
		noOrderList.add(new ListItem("Read Book"));
		noOrderList.add(new ListItem("Coding"));
		noOrderList.add(new ListItem("Poem"));
		
		document.add(noOrderList);
		
		// 关闭
		document.close();
		pdfWriter.close();
		
		log.info(">>> 生成列表成功");
		
	
	}
	
	/**
	  插入中文时， 不使用中文语言
	 
	  最后的结果会发现 :  中文不展示
	 */
	@Test
	public void noChineseTest() throws Exception {
		Document document = new Document();
		
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"不支持中文.pdf"));
		
		document.open();
		
		document.add(new Paragraph("This is our great Chinese characte"));
		
		
		document.add(new Paragraph("中国人民万岁"));
		document.add(new Paragraph("我为我是中国人而自豪"));
		
		document.close();
		
		pdfWriter.close();
		
		log.info(">>> 生成 中文内容的 PDF文件");
	}
	/**
	  添加字段设置，使其支持中文内容
	 
	 会将中文正常展示， 并且是 18号，红色
	 */
	@Test
	public void chineseTest() throws Exception{
		
		Document document = new Document();
		
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"支持中文.pdf"));
		
		document.open();
		
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
		
		Font chineseFont = new Font(bfChinese);
		// 设置字体大小和颜色
		chineseFont.setSize(18);
		chineseFont.setColor(BaseColor.RED);
		
		
		document.add(new Paragraph("This is our great Chinese characte"));
		
		
		document.add(new Paragraph("中国人民万岁",chineseFont));
		document.add(new Paragraph("我为我是中国人而自豪",chineseFont));
		
		document.close();
		
		pdfWriter.close();
		
		log.info(">>> 生成 中文内容的 PDF文件");
	}
	
	/**
	  对段落的相关设置
	 */
	@Test
	public void paragraphTest () throws Exception {
		
		
		Document document = new Document();
		
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"段落设置.pdf"));
		
		document.open();
		
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
		
		Font chineseFont = new Font(bfChinese);
		// 设置字体大小和颜色
		chineseFont.setSize(18);
		chineseFont.setColor(BaseColor.RED);
		
		// 设置段落
		
		Paragraph paragraph1 = new Paragraph("我是岳泽霖",chineseFont);
		// 设置文字居中方式    0 为左， 1为中， 2为右
		paragraph1.setAlignment(1);
		// 设置左缩进  12位
		paragraph1.setIndentationLeft(12);
		// 设置右缩进
		paragraph1.setIndentationRight(12);
		// 设置首行缩进  2个字符
		paragraph1.setFirstLineIndent(2);
		//设置行间距
		paragraph1.setLeading(20f);
		// 设置段落上空白
		paragraph1.setSpacingBefore(5f);
		// 设置段落下空白
		paragraph1.setSpacingAfter(10f);
		
		// 添加到文档中
		
		document.add(paragraph1);
		
		
		String content = "故今日之责任，不在他人，而全在我少年。" +
				"少年智则国智，少年富则国富；少年强则国强，少年独立则国独立；" +
				"少年自由则国自由；少年进步则国进步；少年胜于欧洲，则国胜于欧洲；" +
				"少年雄于地球，则国雄于地球。红日初升，其道大光。河出伏流，一泻汪洋。潜龙腾渊，鳞爪飞扬。" +
				"乳虎啸谷，百兽震惶。鹰隼试翼，风尘翕张。奇花初胎，矞矞皇皇。干将发硎，有作其芒。" +
				"天戴其苍，地履其黄。纵有千古，横有八荒。前途似海，来日方长。" +
				"美哉我少年中国，与天不老！壮哉我中国少年，与国无疆！";
		Paragraph paragraph2 = new Paragraph(content,chineseFont);
		// 设置文字居中方式    0 为左， 1为中， 2为右
		paragraph2.setAlignment(1);
		// 设置左缩进  12位
		paragraph2.setIndentationLeft(12);
		// 设置右缩进
		paragraph2.setIndentationRight(12);
		// 设置首行缩进  2个字符
		paragraph2.setFirstLineIndent(2);
		//设置行间距
		paragraph2.setLeading(20f);
		// 设置段落上空白
		paragraph2.setSpacingBefore(5f);
		// 设置段落下空白
		paragraph2.setSpacingAfter(10f);
		
		document.add(paragraph2);
		
		
		document.close();
		
		pdfWriter.close();
		
		log.info(">>> 生成 段落设置 PDF文件");
	}
	
	/**
	 简单的表格设置
	 */
	@Test
	public void simpleTableTest() throws Exception {
		
		Document document = new Document();
		
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"普通表格.pdf"));
		
		document.open();
		
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
		
		Font chineseFont = new Font(bfChinese);
		// 设置字体大小和颜色
		chineseFont.setSize(18);
		chineseFont.setColor(BaseColor.RED);
		
		// 设置段落
		
		Paragraph paragraph1 = new Paragraph("这是一个表格的基本设置",chineseFont);
		document.add(paragraph1);
		
		// 表格配置  表示几列
		PdfPTable pdfPTable = new PdfPTable(4);
		
		// 进行添加
		PdfPCell pdfPCell11 = new PdfPCell();
		pdfPCell11.addElement(new Paragraph("Id编号",chineseFont));
		
		PdfPCell pdfPCell12 = new PdfPCell();
		pdfPCell12.addElement(new Paragraph("姓名",chineseFont));
		
		PdfPCell pdfPCell13 = new PdfPCell();
		pdfPCell13.addElement(new Paragraph("年龄",chineseFont));
		
		PdfPCell pdfPCell14 = new PdfPCell();
		pdfPCell14.addElement(new Paragraph("描述",chineseFont));
		
		// 添加头
		pdfPTable.addCell(pdfPCell11);
		pdfPTable.addCell(pdfPCell12);
		pdfPTable.addCell(pdfPCell13);
		pdfPTable.addCell(pdfPCell14);
		
		
		// 添加第二行
		
		// 进行添加
		PdfPCell pdfPCell21 = new PdfPCell();
		pdfPCell21.addElement(new Paragraph("1",chineseFont));
		
		PdfPCell pdfPCell22 = new PdfPCell();
		pdfPCell22.addElement(new Paragraph("岳泽霖",chineseFont));
		
		PdfPCell pdfPCell23 = new PdfPCell();
		pdfPCell23.addElement(new Paragraph("28",chineseFont));
		
		PdfPCell pdfPCell24 = new PdfPCell();
		pdfPCell24.addElement(new Paragraph("一个快乐的程序员",chineseFont));
		
		pdfPTable.addCell(pdfPCell21);
		pdfPTable.addCell(pdfPCell22);
		pdfPTable.addCell(pdfPCell23);
		pdfPTable.addCell(pdfPCell24);
		
		
		
		
		
		document.add(pdfPTable);
		
		
		
		document.close();
		
		pdfWriter.close();
		
		log.info(">>> 生成 普通表格 PDF文件");
	}
	
	/**
  		生成填充表格测试
	 
	    表格复杂的操作， 一般都使用模板进行处理。
	 */
	@Test
	public void tableTest() throws Exception{
		
		Document document = new Document();
		
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"表格.pdf"));
		
		document.open();
		
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
		
		Font chineseFont = new Font(bfChinese);
		// 设置字体大小和颜色
		chineseFont.setSize(18);
		chineseFont.setColor(BaseColor.RED);
		
		// 设置段落
		
		Paragraph paragraph1 = new Paragraph("表格展示",chineseFont);
		document.add(paragraph1);
		
		// 表格配置  表示几列
		PdfPTable pdfPTable = new PdfPTable(4);
		// 对表格进行配置， 配置 各个列的 占比  总的是 100%
		pdfPTable.setWidths(new float[]{15f,30f,20f,35f});
		// 设置整个表格占整个页面的百分比
		pdfPTable.setWidthPercentage(100f);
		// 设置居中方式
		pdfPTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		int elementType = Element.ALIGN_CENTER;
		// 对表头进行处理
		java.util.List<String> headerList = Arrays.asList("Id编号","姓名","年龄","描述");
		
		for(String headerText : headerList){
			PdfPCell pdfPCellText = new PdfPCell(new Phrase(elementType,headerText,chineseFont));
			pdfPCellText.setBorderWidth(1);
			pdfPCellText.setPadding(10);
			pdfPTable.addCell(pdfPCellText);
			
		}
		pdfPTable.setHeaderRows(1);
		
		// 设置实体
		java.util.List<MyUser> myUserList = findUserList();
		
		for(MyUser myUser : myUserList){
			// 添加内容信息
			PdfPCell pdfPCellId = new PdfPCell(new Phrase(elementType, myUser.getId() + "", chineseFont));
			pdfPCellId.setBorderWidth(1);
			pdfPCellId.setPadding(10);
			pdfPTable.addCell(pdfPCellId);
			
			
			PdfPCell pdfPCellName = new PdfPCell(new Phrase(elementType, myUser.getName() + "", chineseFont));
			pdfPCellName.setBorderWidth(1);
			pdfPCellName.setPadding(10);
			pdfPTable.addCell(pdfPCellName);
			
			PdfPCell pdfPCellAge = new PdfPCell(new Phrase(elementType, myUser.getAge() + "", chineseFont));
			pdfPCellAge.setBorderWidth(1);
			pdfPCellAge.setPadding(10);
			pdfPTable.addCell(pdfPCellAge);
			
			
			PdfPCell pdfPCellDesc = new PdfPCell(new Phrase(elementType, myUser.getDescription() + "", chineseFont));
			pdfPCellDesc.setBorderWidth(1);
			pdfPCellDesc.setPadding(10);
			pdfPTable.addCell(pdfPCellDesc);
			
		}
		document.add(pdfPTable);
		
		document.close();
		
		pdfWriter.close();
		
		log.info(">>> 生成 表格 PDF文件");
		
		
		
	}
	
	public java.util.List<MyUser> findUserList() {
		java.util.List<MyUser> result = new ArrayList<>();
		
		result.add(new MyUser(1,"岳泽霖",28,"一个快乐的程序员"));
		result.add(new MyUser(2,"两个蝴蝶飞",27,"你们的老朋友了"));
		result.add(new MyUser(3,"岳叔叔教你背古诗",1,"欢迎大家关注我"));
		
		return result;
	}
	
	/**
	配置线操作
	 
	 Paragraph 类似于  <p></p>

		Chunk  	类似于   <span></span>
	 */
	@Test
	public void lineTest() throws Exception{
		
		Document document = new Document();
		
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"块 Span.pdf"));
		
		document.open();
		
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
		
		Font chineseFont = new Font(bfChinese);
		// 设置字体大小和颜色
		chineseFont.setSize(18);
		chineseFont.setColor(BaseColor.RED);
		
		
		document.add(new Paragraph("线操作",chineseFont));
		
		// 段落中添加相应的信息
		
		Paragraph paragraph = new Paragraph();
		
		paragraph.add(new Chunk("添加直线",chineseFont));
		// 添加直线
		paragraph.add(new Chunk(new LineSeparator()));
		
		paragraph.add(new Chunk("添加点线",chineseFont));
		// 添加点线
		paragraph.add(new Chunk(new DottedLineSeparator()));
		
		document.add(paragraph);
		
		document.close();
		
		pdfWriter.close();
		
		log.info(">>> 生成 块 PDF文件");
	}
	
	/**
	 创建新的 页
	 document.newPage();
	 */
	@Test
	public void pageTest() throws Exception {
		
		Document document = new Document() ;
		
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"多页.pdf"));
		
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
		
		Font chineseFont = new Font(bfChinese);
		// 设置字体大小和颜色
		chineseFont.setSize(18);
		chineseFont.setColor(BaseColor.RED);
		
		document.open();
		
		document.add(new Paragraph("这是第一页",chineseFont));
		
		document.newPage();
		
		document.add(new Paragraph("这是第二页",chineseFont));
		
		document.newPage();
		
		document.add(new Paragraph("这是第三页",chineseFont));
		
		// 关闭
		document.close();
		
		pdfWriter.close();
		
		log.info(">>> 生成多页 PDF 文档成功");
	}
	
	/**
	  事件监听触发机制
	 */
	@Test
	public void eventTest() throws Exception {
		
		Document document = new Document();
		
		PdfWriter pdfWriter = PdfWriter.getInstance(document,new FileOutputStream(PDF_DIRECTORY+"水印和页脚设置.pdf"));
		
		// 添加监听器
		pdfWriter.setPageEvent(new HeaderFooterEvent());
		
		pdfWriter.setPageEvent(new WaterMarkEvent());
		
		document.open();
		
		BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
		
		Font chineseFont = new Font(bfChinese);
		// 设置字体大小和颜色
		chineseFont.setSize(18);
		chineseFont.setColor(BaseColor.RED);
		
		// 设置段落
		
		Paragraph paragraph1 = new Paragraph("我是岳泽霖",chineseFont);
		// 设置文字居中方式    0 为左， 1为中， 2为右
		paragraph1.setAlignment(1);
		// 设置左缩进  12位
		paragraph1.setIndentationLeft(12);
		// 设置右缩进
		paragraph1.setIndentationRight(12);
		// 设置首行缩进  2个字符
		paragraph1.setFirstLineIndent(2);
		//设置行间距
		paragraph1.setLeading(20f);
		// 设置段落上空白
		paragraph1.setSpacingBefore(5f);
		// 设置段落下空白
		paragraph1.setSpacingAfter(10f);
		
		// 添加到文档中
		
		document.add(paragraph1);
		
		
		String content = "故今日之责任，不在他人，而全在我少年。" +
				"少年智则国智，少年富则国富；少年强则国强，少年独立则国独立；" +
				"少年自由则国自由；少年进步则国进步；少年胜于欧洲，则国胜于欧洲；" +
				"少年雄于地球，则国雄于地球。红日初升，其道大光。河出伏流，一泻汪洋。潜龙腾渊，鳞爪飞扬。" +
				"乳虎啸谷，百兽震惶。鹰隼试翼，风尘翕张。奇花初胎，矞矞皇皇。干将发硎，有作其芒。" +
				"天戴其苍，地履其黄。纵有千古，横有八荒。前途似海，来日方长。" +
				"美哉我少年中国，与天不老！壮哉我中国少年，与国无疆！";
		Paragraph paragraph2 = new Paragraph(content,chineseFont);
		// 设置文字居中方式    0 为左， 1为中， 2为右
		paragraph2.setAlignment(1);
		// 设置左缩进  12位
		paragraph2.setIndentationLeft(12);
		// 设置右缩进
		paragraph2.setIndentationRight(12);
		// 设置首行缩进  2个字符
		paragraph2.setFirstLineIndent(2);
		//设置行间距
		paragraph2.setLeading(20f);
		// 设置段落上空白
		paragraph2.setSpacingBefore(5f);
		// 设置段落下空白
		paragraph2.setSpacingAfter(10f);
		
		document.add(paragraph2);
		
		
		document.close();
		
		pdfWriter.close();
		
		log.info(">>> 生成 水印 PDF文件");
		
	}
	
	
	
	
	
}
