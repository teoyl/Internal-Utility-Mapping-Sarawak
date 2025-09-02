package com.sains.framework.base;

import com.sains.framework.base.LogFunction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import java.util.*;
import java.io.OutputStream;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

/**
 *
 * @author thensw
 */
public class ReportGenerator {

//    public static void print(final String jasperRpt, final String outFileName, final Map param){
//        BaseDAO baseDAO = new RptBaseDAOImpl();
//        try {
//            baseDAO.getSession().doWork(new Work() {
//                @Override
//                public void execute(java.sql.Connection conn) throws SQLException {
//                    try {
//                        //System.out.println("jasperRpt = " + jasperRpt);
//                        //System.out.println("outFileName = " + outFileName);
//                        // Fill the report using an empty data source
//                        //JasperPrint print = JasperFillManager.fillReport(file.getPath(), hm, new JREmptyDataSource());
//                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
//                        // Create a PDF exporter
//                        JRExporter exporter = new JRPdfExporter();
//                        // Configure the exporter (set output file name and print object)
//                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
//                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//                        // Export the PDF file
//                        exporter.exportReport();
//                    } catch (JRException e) {
//                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
//                    } catch (Exception e) {
//                    }
//                }
//            });
//        } catch (Exception e) {
//            new LogFunction().logError(ReportGenerator.class, "in Exception", e);
//        } finally {
//            baseDAO.closeSession();
//        }
//    }

//    public static void printFromStream(final InputStream jasperRpt, final String outFileName, final Map param){
//        BaseDAO baseDAO = new RptBaseDAOImpl();
//        try {
//            baseDAO.getSession().doWork(new Work() {
//                @Override
//                public void execute(java.sql.Connection conn) throws SQLException {
//                    try {
//                        // Fill the report using an empty data source
//                        //JasperPrint print = JasperFillManager.fillReport(file.getPath(), hm, new JREmptyDataSource());
//                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
//                        // Create a PDF exporter
//                        JRExporter exporter = new JRPdfExporter();
//                        // Configure the exporter (set output file name and print object)
//                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
//                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//                        // Export the PDF file
//                        exporter.exportReport();
//                    } catch (JRException e) {
//                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
//                    } catch (Exception e) {
//                    }
//                }
//            });
//        } catch (Exception e) {
//            new LogFunction().logError(ReportGenerator.class, "in Exception", e);
//        } finally {
//            baseDAO.closeSession();
//        }
//    }


    public static InputStream printStream(final InputStream jasperRpt, final Map param){
        BaseDAO baseDAO = new RptBaseDAOImpl();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            baseDAO.getSession().doWork(new Work() {
                @Override
                public void execute(java.sql.Connection conn) throws SQLException {
                    try {
                        // Fill the report using an empty data source
                        //JasperPrint print = JasperFillManager.fillReport(file.getPath(), hm, new JREmptyDataSource());
                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
                        // Create a PDF exporter
                        // Export the PDF file
                        JasperExportManager.exportReportToPdfStream(print, os);
                    } catch (JRException e) {
                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
            new LogFunction().logError(ReportGenerator.class, "in Exception", e);
        } finally {
            baseDAO.closeSession();
        }
        return convertToInputStream(os);
    }
    private static JRExporter exporter = new JRPdfExporter();
    /** this one is to pass 2nd connection to report **/
    /*
    public static InputStream printPdfBufferWithLasisConnection(final InputStream jasperRpt, final Map param, BaseDAO baseDAO) {
//        java.sql.Connection conn = SessionFactoryImpl.getConnection();
//        BaseDAO baseDAO = new BaseDAOImpl();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            // Fill the report using an empty data source
            //JasperPrint print = JasperFillManager.fillReport(file.getPath(), hm, new JREmptyDataSource());
            int cnt = baseDAO.getSession().doReturningWork(new ReturningWork<Integer>() {

                @Override
                public Integer execute(final java.sql.Connection conn) throws SQLException {
                    Session lasisSession = SessionFactoryImpl.getSession_lns();
                    int cnt = lasisSession.doReturningWork(new ReturningWork<Integer>() {

                    @Override
                    public Integer execute(java.sql.Connection conn) throws SQLException {
                        try {
                            param.put("pElasisConn", conn);
                            return 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }
                });
                    try {
                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
                        // Create a PDF exporter
                        // Export the PDF file
                        //            JasperExportManager.exportReportToHtmlFile(print, outFileName);
                        // Configure the exporter (set output file name and print object)
                        //            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                        // Export the PDF file
                        exporter.exportReport();
                        return 0;
                    } catch (JRException e) {
                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (lasisSession.isOpen()) {
                            lasisSession.close();
                        }
                    }
                    return 1;
                }
            });
//            JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
//            // Create a PDF exporter
//            // Export the PDF file
//
////            JasperExportManager.exportReportToHtmlFile(print, outFileName);
//            JRExporter exporter = new JRPdfExporter();
//            // Configure the exporter (set output file name and print object)
////            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
//            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
//            // Export the PDF file
//            exporter.exportReport();

        } catch (Exception e) {
            new LogFunction().logError(ReportGenerator.class, "in Exception", e);
//        } finally {
////            SessionFactoryImpl.closeConnection(conn);
//            baseDAO.closeSession();
        }
        return convertToInputStream(os);
    }
    */
    
    public static InputStream printPdfBuffer(final InputStream jasperRpt, final Map param, BaseDAO baseDAO) {
//        java.sql.Connection conn = SessionFactoryImpl.getConnection();
//        BaseDAO baseDAO = new BaseDAOImpl();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            // Fill the report using an empty data source
            //JasperPrint print = JasperFillManager.fillReport(file.getPath(), hm, new JREmptyDataSource());
            int cnt = baseDAO.getSession().doReturningWork(new ReturningWork<Integer>() {

                @Override
                public Integer execute(java.sql.Connection conn) throws SQLException {
                    try {
                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
                        // Create a PDF exporter
                        // Export the PDF file

                        //            JasperExportManager.exportReportToHtmlFile(print, outFileName);
                        JRExporter exporter = new JRPdfExporter();
                        // Configure the exporter (set output file name and print object)
                        //            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                        // Export the PDF file
                        exporter.exportReport();
                        return 0;
                    } catch (JRException e) {
                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
                    } catch (Exception e) {
                    }
                    return 1;
                }
            });
//            JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
//            // Create a PDF exporter
//            // Export the PDF file
//
////            JasperExportManager.exportReportToHtmlFile(print, outFileName);
//            JRExporter exporter = new JRPdfExporter();
//            // Configure the exporter (set output file name and print object)
////            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
//            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
//            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
//            // Export the PDF file
//            exporter.exportReport();

        } catch (Exception e) {
            new LogFunction().logError(ReportGenerator.class, "in Exception", e);
//        } finally {
////            SessionFactoryImpl.closeConnection(conn);
//            baseDAO.closeSession();
        }
        return convertToInputStream(os);
    }

    public static InputStream printXlsBuffer(final InputStream jasperRpt, final Map param, BaseDAO baseDAO) {
//        BaseDAO baseDAO = new BaseDAOImpl();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            baseDAO.getSession().doWork(new Work() {
                @Override
                public void execute(java.sql.Connection conn) throws SQLException {
                    try {
                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);

                        JRExporter exporter = new JRXlsExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                        exporter.exportReport();
                    } catch (JRException e) {
                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            new LogFunction().logError(ReportGenerator.class, "in Exception", e);
//        } finally {
//            baseDAO.closeSession();
        }
        return convertToInputStream(os);
    }
    public static InputStream printDocxBuffer(final InputStream jasperRpt, final Map param, BaseDAO baseDAO) {
//        BaseDAO baseDAO = new BaseDAOImpl();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            baseDAO.getSession().doWork(new Work() {
                @Override
                public void execute(java.sql.Connection conn) throws SQLException {
                    try {
                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);

                        JRExporter exporter = new JRDocxExporter();
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                        exporter.exportReport();
                    } catch (JRException e) {
                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
            new LogFunction().logError(ReportGenerator.class, "in Exception", e);
//        } finally {
//            baseDAO.closeSession();
        }
        return convertToInputStream(os);
    }

    public static StringBuffer printHtmlBuffer(final InputStream jasperRpt, final Map param){
        BaseDAO baseDAO = new BaseDAOImpl();
        StringBuffer sf = new StringBuffer();
        try {
            baseDAO.getSession().doWork(new Work() {
                @Override
                public void execute(java.sql.Connection conn) throws SQLException {
                    try {
                        // Fill the report using an empty data source
                        //JasperPrint print = JasperFillManager.fillReport(file.getPath(), hm, new JREmptyDataSource());
                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
                        // Create a PDF exporter
                        // Export the PDF file
                        JRExporter exporter = new HtmlExporter();
                        // Configure the exporter (set output file name and print object)
            //            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                        exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sf);
                        // Export the PDF file
                        exporter.exportReport();
                    } catch (JRException e) {
                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
            new LogFunction().logError(ReportGenerator.class, "in Exception", e);
        } finally {
            baseDAO.closeSession();
        }
        return sf;
    }

    private static InputStream convertToInputStream(ByteArrayOutputStream os){
        return new ByteArrayInputStream(os.toByteArray());
    }
    
    
    //copy from elodgement - ahmadni @ 28-Jul-2016
    public static void printFromStream(final InputStream jasperRpt, final String outFileName, final Map param) {
        BaseDAO baseDAO = new BaseDAOImpl();
        final StringBuffer sf = new StringBuffer();
        try {
            baseDAO.getSession().doWork(new Work() {
                @Override
                public void execute(java.sql.Connection conn) throws SQLException {
                    try {
                         // Fill the report using an empty data source
                        //JasperPrint print = JasperFillManager.fillReport(file.getPath(), hm, new JREmptyDataSource());
                        JasperPrint print = JasperFillManager.fillReport(jasperRpt, param, conn);
                        // Create a PDF exporter
                        JRExporter exporter = new JRPdfExporter();
                        // Configure the exporter (set output file name and print object)
                        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFileName);
                        exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                        // Export the PDF file
                        exporter.exportReport();
                    } catch (JRException e) {
                        new LogFunction().logError(ReportGenerator.class, "in JREException", e);
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
            new LogFunction().logError(ReportGenerator.class, "in Exception", e);
        } finally {
            baseDAO.closeSession();
        }
    }
}

/*
 * jars required.
 *  * jasperreports-1.2.0.jar,
    * commons-beanutils-1.5.jar,
    * commons-collections-2.1.jar,
    * commons-digester-1.7.jar,
    * commons-logging-1.0.2.jar.
    * iText-2.1.7.jar
 */