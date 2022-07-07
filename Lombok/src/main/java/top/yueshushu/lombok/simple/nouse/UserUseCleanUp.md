@Cleanup
生成关闭的方法， 如文件关闭。


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
	
	

会变成:

public void cleanUp() throws Exception {
        FileInputStream in = new FileInputStream("D:\\input.txt");

        try {
            FileOutputStream out = new FileOutputStream("D:\\out.txt");

            try {
                byte[] b = new byte[1024];

                while(true) {
                    int r = in.read(b);
                    if (r == -1) {
                        return;
                    }

                    out.write(b, 0, r);
                }
            } finally {
                if (Collections.singletonList(out).get(0) != null) {
                    out.close();
                }

            }
        } finally {
            if (Collections.singletonList(in).get(0) != null) {
                in.close();
            }

        }
    }