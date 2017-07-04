package it.geosolutions.geoserver.rest.patch;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.methods.RequestEntity;

/**
 * Геосервер настолько непроходимо туп,
 * что не принимает заголовок, в котором указан charset.
 * Поэтому StringRequestEntity для работы с ним не подходит,
 * вызывая ошибки сервера либо кракозябли вместо кириллицы.
 * Этот класс посылает контент в заданной кодировке,
 * а в заголовке о ней не упоминает.
 * @author s.raevskiy
 *
 */
public class GeoserverStringRequestEntity implements RequestEntity {

    /** The content */
    private byte[] content;

    /** The charset */
    private static String charset = null;

    /** The content type (i.e. text/html; charset=EUC-JP). */
    private String contentType;

    public GeoserverStringRequestEntity(String content, String contentType)
        throws UnsupportedEncodingException {
        super();
        if (content == null) {
            throw new IllegalArgumentException("The content cannot be null");
        }

        this.contentType = contentType;

        // resolve the content type and the charset
        if (charset != null) {
            this.content = content.getBytes(charset);
        } else {
            this.content = content.getBytes();
        }
    }

    /**
     * Задать кодировку
     * @param charset
     */
    public static void setCharset(String charset)
    {
        GeoserverStringRequestEntity.charset = charset;
    }

    /* (non-Javadoc)
     * @see org.apache.commons.httpclient.methods.RequestEntity#getContentType()
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    /**
     * @return <code>true</code>
     */
    @Override
    public boolean isRepeatable() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.apache.commons.httpclient.RequestEntity#writeRequest(java.io.OutputStream)
     */
    @Override
    public void writeRequest(OutputStream out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }
        out.write(this.content);
        out.flush();
    }

    /**
     * @return The length of the content.
     */
    @Override
    public long getContentLength() {
        return this.content.length;
    }
}
