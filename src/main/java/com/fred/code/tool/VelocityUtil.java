package com.fred.code.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class VelocityUtil {

    private static final String ENCODING = "utf-8";

    public static void render(File templateFile, Map<String, Object> parameters, Writer writer) throws Exception {
        Properties properties = new Properties();
        properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templateFile.getParent());
        properties.setProperty(Velocity.ENCODING_DEFAULT, ENCODING);
        properties.setProperty(Velocity.INPUT_ENCODING, ENCODING);
        properties.setProperty(Velocity.OUTPUT_ENCODING, ENCODING);
        VelocityEngine ve = new VelocityEngine(properties);
        Template template = ve.getTemplate(templateFile.getName(), ENCODING);
        VelocityContext context = new VelocityContext(parameters);
        template.merge(context, writer);
    }

    public static void render(File templateFile, Map<String, Object> parameters, File destFile) throws Exception {
        Properties properties = new Properties();
        properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templateFile.getParent());
        properties.setProperty(Velocity.ENCODING_DEFAULT, ENCODING);
        properties.setProperty(Velocity.INPUT_ENCODING, ENCODING);
        properties.setProperty(Velocity.OUTPUT_ENCODING, ENCODING);
        VelocityEngine ve = new VelocityEngine(properties);
        Template template = ve.getTemplate(templateFile.getName(), ENCODING);
        VelocityContext context = new VelocityContext(parameters);
        destFile.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(destFile);
        template.merge(context, writer);
        writer.close();
    }
}
