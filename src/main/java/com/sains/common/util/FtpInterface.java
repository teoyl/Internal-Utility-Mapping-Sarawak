package com.sains.common.util;

import com.sains.framework.base.BaseDAO;
import com.sains.framework.base.CustomBaseException;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

public abstract class FtpInterface {
    public final String ThumbnailSurfix = "_tmb";
    public static final class ExtensionWhiteList {
        public static final String[] IMAGE = {"JPEG","Exif","TIFF","GIF","BMP","PNG","JPG"};
        public static final String[] IMAGE_and_PDF = {"JPEG","Exif","TIFF","GIF","BMP","PNG","JPG","PDF"};
        public static final String[] DOC = {"DOC","DOCX","PDF","XLS","XLSX","ODS","PPT","PPTX","TXT"};
        public static final String[] ZIP = {"ZIP","7z","RAR"};
    }
    public void checkStreamContent(String fileName, InputStream file) throws Exception {
        
    }
    public void checkFileContent(String fileName, File file) throws Exception {
        
    }
    public void checkExtension(String fileName, String[] extensionWhiteList) throws Exception {
        if (fileName.lastIndexOf(".") < 0) {
            throw new CustomBaseException("ftp.invalidExtention", "noExt");
        }
        if (extensionWhiteList == null) {
            throw new CustomBaseException("No white list, rejected");
        }
        String extension = fileName.substring(fileName.lastIndexOf(".")+1).trim();
        if (Validator.isEmpty(extension)) {
            throw new CustomBaseException("ftp.invalidExtention", "noExt");
        }
        Stream<String> stream1 = Arrays.stream(extensionWhiteList);
        if (stream1.filter(x -> x.trim().equalsIgnoreCase(extension)).count() == 0) {
            throw new CustomBaseException("ftp.invalidExtention", extension);
        }
    }
    public abstract Boolean isFileExists(String fileDir, Boolean closeSftp) throws Exception;
    public abstract InputStream getFile(String fileDir) throws Exception;
    public abstract InputStream getFile_noAppend(String fileDir) throws Exception;
    @Deprecated
    public abstract Boolean createFile(String fileDir, File file) throws Exception;
    @Deprecated
    public abstract Boolean createFile(String fileDir, InputStream file) throws Exception;
    public abstract Boolean createFile(String fileDir, File file, String fileName, String[] extWhiteList) throws Exception;
    public abstract Boolean createFile(String fileDir, InputStream file, String fileName, String[] extWhiteList) throws Exception;
    public abstract Boolean deleteFile(String fileDir) throws Exception;
    public abstract Boolean deleteFolder(String fileDir) throws Exception;
    public abstract Boolean renameFile(String fileDir, String newFileDir) throws Exception;
    public abstract Boolean isDirExists(String fileDir, Boolean closeSftp) throws Exception;
    public abstract Boolean createDirIfNotExists(String fileDir, Boolean closeAfterUse) throws Exception;
    public abstract Boolean copyFile(String fileDir, String targetFileDir) throws Exception;
    public abstract void disconnect() throws Exception;
    public abstract void insertToFileDirectory(String filePath, String fileName, String mime_type, String fileId, BaseDAO dao, String docApplication, Boolean isTemp) throws Exception;
    public abstract void moveTempFile(String newPath, String drDocId, org.hibernate.Session session) throws Exception;
    private int thumbNailHeight = 100;
    public int getThumbNailHeight() {
        return thumbNailHeight;
    }
    public void setThumbNailHeight(int thumbNailHeight) {
        this.thumbNailHeight = thumbNailHeight;
    }
    
    public InputStream createThumbnail(String filePath, String fileName, String mime_type, InputStream fileStream) throws Exception{
        if (Validator.isEmpty(ThumbnailSurfix)) {
            throw new CustomBaseException("Thumbnail Surfix is not set");
        }
        BufferedImage originalImage = ImageIO.read(fileStream);
        Double IMG_WIDTH = originalImage.getWidth() / (originalImage.getHeight()*1.0/thumbNailHeight);
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH.intValue(), thumbNailHeight, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, IMG_WIDTH.intValue(), thumbNailHeight, null);
	g.dispose();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, fileName.substring(fileName.lastIndexOf(".")+1), os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        createFile(filePath+ThumbnailSurfix, is);
	return is;
    }
}