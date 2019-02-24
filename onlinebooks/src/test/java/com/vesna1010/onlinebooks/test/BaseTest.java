package com.vesna1010.onlinebooks.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public abstract class BaseTest {

	public byte[] getContents() {
		File file = null;
		InputStream is = null;
		byte[] contents = null;

		try {
			file = new File(this.getClass().getResource("/book/codeconventions.pdf").toURI());
			is = new BufferedInputStream(new FileInputStream(file));
			contents = new byte[(int) file.length()];
			is.read(contents);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return contents;
	}
}