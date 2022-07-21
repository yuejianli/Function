package top.yueshushu.itextpdf.simpletest.event;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-21
 */

public class WaterMarkEvent extends PdfPageEventHelper {
	private Font font ;
	// 水印内容
	private String waterCont;
	
	public WaterMarkEvent() {
		initFont();
	}
	
	private void initFont() {
		try{
			BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			font = new Font(baseFont);
			font.setSize(8);
			font.setColor(BaseColor.BLACK);
			
			
		}catch (Exception e){
			font = new Font();
		}
	}
	public WaterMarkEvent(String waterCont) {
		initFont();
		this.waterCont = waterCont;
	}
	
	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		for(int i=0 ; i<5; i++) {
			for(int j=0; j<5; j++) {
				ColumnText.showTextAligned(writer.getDirectContentUnder(),
						Element.ALIGN_CENTER,
						new Phrase(this.waterCont == null ? "岳叔叔教你背古诗" : this.waterCont, font),
						(50.5f+i*350),
						(40.0f+j*150),
						writer.getPageNumber() % 2 == 1 ? 45 : -45);
			}
		}
	}
}
