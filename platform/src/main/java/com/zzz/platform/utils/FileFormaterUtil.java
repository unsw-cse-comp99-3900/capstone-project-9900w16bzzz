package com.zzz.platform.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.zzz.platform.common.enumeration.FileType;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/19
 */
@Slf4j
public class FileFormaterUtil {

    public static byte[] formatFile(byte[] content, FileType fileType) {
        byte[] formattedData;
        try {
            if (fileType.equals(FileType.JSON)) {
                formattedData = formatJson(content);
            } else if (fileType.equals(FileType.XML)) {
                formattedData = formatXml(content);
            } else {
                return content;
            }
        } catch (Exception e) {
            log.error("Format file error", e);
            return content;
        }
        return formattedData;
    }


    private static byte[] formatJson(byte[] content) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(new ByteArrayInputStream(content), Object.class);
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writer.writeValue(outputStream, json);
        return outputStream.toByteArray();
    }

    private static byte[] formatXml(byte[] content) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(content));

        OutputFormat format = OutputFormat.createPrettyPrint();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XMLWriter writer = new XMLWriter(outputStream, format);
        writer.write(document);
        return outputStream.toByteArray();
    }
}
