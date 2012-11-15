package com.interaction;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ByteResponseWrapper extends HttpServletResponseWrapper {
    private final PrintWriter writer;
    private final ByteOutputStream output;
    
    public byte[] getBytes() {
        writer.flush();
        return output.getBytes();
    }
      
    public ByteResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new ByteOutputStream();
        writer = new PrintWriter(output);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }
    
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
     return output;
    }
}

class ByteOutputStream extends ServletOutputStream {
    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    @Override
    public void write(int b) throws IOException {
        bos.write(b);            
    }
    
    public byte[] getBytes() {
        return bos.toByteArray();
    }
}

