package top.yueshushu.lombok.simple.nouse;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Cleanup;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-07
 */

public class UserUseCleanUp {
	
	public void cleanUp() throws Exception{
		@Cleanup
		InputStream in = new FileInputStream("D:\\input.txt");
		@Cleanup
		OutputStream out = new FileOutputStream("D:\\out.txt");
		byte[] b = new byte[1024];
		while (true) {
			int r = in.read(b);
			if (r == -1) break;
			out.write(b, 0, r);
		}
	}
}
