ItextPDF 生成PDF文件

1. 添加依赖
~~~xml
        <!-- 导入 pdf 的包-->
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>itextpdf</artifactId>
            <version>5.5.10</version>
        </dependency>
        <!--支持中文使用-->
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>itext-asian</artifactId>
            <version>5.2.0</version>
        </dependency>
        <!--支持密码加密-->
        <dependency>
            <groupId>org.bouncycastle</groupId>
            <artifactId>bcprov-jdk15on</artifactId>
            <version>1.54</version>
        </dependency>
~~~

2. 代码事例编写

~~~java

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

import lombok.extern.slf4j.Slf4j;

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
		
		log.info(">>> 生成一个简单的 PDF文件成功");
	}
~~~

主要分成五部分:

1. 构建  Document 文档

2. 生成 PdfWriter 生成器

3. 打开文档  .open()

4. 创建元素， document 添加元素

5. 关闭文档  close()

