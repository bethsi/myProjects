package com.interaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CharResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private ServletOutputStream sos;

	protected OutputStreamWriter writer; 
	private PrintWriter pw = new PrintWriter(baos);


	protected boolean getOutputStreamCalled;

	protected boolean getWriterCalled;

	public CharResponseWrapper(HttpServletResponse response) throws UnsupportedEncodingException {
		super(response);
		sos = new ByteArrayServletStream(baos);
		writer = new OutputStreamWriter(baos, "UTF-8"); 
	}

//	public ServletOutputStream getOutputStream() throws IOException {
//		if (getWriterCalled) {
//			throw new IllegalStateException("getWriter already called");
//		}
//
//		getOutputStreamCalled = true;
//		return super.getOutputStream();
//	}

	// public PrintWriter getWriter() throws IOException {
	// if (writer != null) {
	// return writer;
	// }
	// if (getOutputStreamCalled) {
	// throw new IllegalStateException("getOutputStream already called");
	// }
	// getWriterCalled = true;
	// writer = new PrintWriter(charWriter);
	// return writer;
	// }

	// public PrintWriter getWriter() throws UnsupportedEncodingException {
	// writer = new OutputStreamWriter(charWriter, "UTF-8");
	// return new PrintWriter(writer);
	// }

	public PrintWriter getWriter() {	
		return pw;
	}
	
	public ServletOutputStream getOutputStream() throws IOException{		
		return super.getOutputStream();
	}

	public ServletOutputStream getStream() {
		return sos;
	}

	public byte[] toByteArray() {
		pw.flush();		
		return baos.toByteArray();
	}

	public String toString() {
		String s = null;		
		s = baos.toString();
		return s;
	}

	private static class ByteArrayServletStream extends ServletOutputStream {
		ByteArrayOutputStream baos;

		ByteArrayServletStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}

		public void write(int param) throws IOException {
			baos.write(param);
		}
	}

}
