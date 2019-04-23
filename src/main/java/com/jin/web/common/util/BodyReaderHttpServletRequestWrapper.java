package com.jin.web.common.util;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request)
            throws IOException {
        super(request);
        body = getBytes(request.getReader());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {}
        };
    }

    private byte[] getBytes(BufferedReader reader) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        try{
            char[]buff = new char[1024];
            int len;
            while((len = reader.read(buff)) != -1) {
                sb.append(buff,0, len);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().getBytes("utf-8");
    }

}
