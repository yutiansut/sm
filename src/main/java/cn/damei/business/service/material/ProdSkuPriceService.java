package cn.damei.business.service.material;

import cn.damei.business.constants.PropertyHolder;
import cn.damei.business.dao.material.ProdSkuDao;
import cn.damei.business.dao.material.ProdSkuPriceDao;
import cn.damei.business.entity.commodity.prodsku.ProdSku;
import cn.damei.business.entity.commodity.prodskuprice.ProdSkuPrice;
import cn.mdni.commons.file.FileUtils;
import cn.mdni.commons.file.UploadCategory;
import cn.mdni.commons.file.compress.ZipFileUtils;
import cn.damei.core.base.service.CrudService;
import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProdSkuPriceService extends CrudService<ProdSkuPriceDao, ProdSkuPrice> {
    @Autowired
    private ProdSkuDao prodSkuDao;

    public List<ProdSkuPrice> findByTypeAndSkuId(Map<String, Object> params) {
        return entityDao.findByTypeAndSkuId(params);
    }

    public void downLabel(Long id, String type, HttpServletResponse resp,HttpServletRequest httpServletRequest) {
        //查询价格
        ProdSku byIdAndType = prodSkuDao.getByIdAndType(id, type, new Date());
        try {
            drawImage(byIdAndType, type, resp,httpServletRequest);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public  boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * 目录下的文件
     *
     * @param dir
     *            要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public  boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        return flag;
    }



    /**
     * @param ids   sku id
     * @param type 价格类型
     * @param resp
     * @param request
     * @Description: 大美智装 下载标签
     * @date: 2018/1/4  10:54
     */
    public String downLabelList(List<ProdSku> ids, String type, HttpServletResponse resp, HttpServletRequest request) throws Exception {
        //查询价格
        for (ProdSku id:ids) {
            try {
                drawImageList(id, type, resp,request);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        //压缩
        String jpg =request.getSession().getServletContext().getRealPath("/") + "/WEB-INF/views/business/material/resource/jpgtemp";
        String fileName="sku_"+System.nanoTime()+ ".zip";
        String fileFullPath = FileUtils.saveFilePath(UploadCategory.ZIP,  PropertyHolder.getUploadDir(), fileName);
        ZipFileUtils.compressZip(jpg, fileFullPath);
        //删除临时文件
        boolean b = deleteDirectory(jpg);
        if(!b){
            throw new Exception("删除图片文件失败");
        }
        return fileFullPath;
    }


    private void drawImage(ProdSku byIdAndType, String type, HttpServletResponse resp,HttpServletRequest httpServletRequest) throws WriterException {
        BufferedImage image = getBufferedImage(byIdAndType, type,httpServletRequest);
        drawImage(byIdAndType, resp, image);
    }
    private void drawImageList(ProdSku byIdAndType, String type, HttpServletResponse resp,HttpServletRequest httpServletRequest) throws WriterException {
        BufferedImage image = getBufferedImage(byIdAndType, type,httpServletRequest);
        drawImageTemp(byIdAndType, resp, image,httpServletRequest);
    }

    private BufferedImage getBufferedImage(ProdSku byIdAndType, String type,HttpServletRequest httpServletRequest) throws WriterException {
        Color colorOriange = new Color(208, 98, 21);
        Color colorBlue = new Color(24, 109, 208);
        Color colorBlack = new Color(17, 12, 5);
        // 图片的宽度
        int imageWidth = 1063;
        // 图片的高度
        int imageHeight = 638;
        Color color = colorBlue;
        if ("UPGRADE".equals(type)) {
            //背景颜色 橙色
            color = colorOriange;
        }
        //背景颜色白色
        Color colorWhite = new Color(255, 255, 255);
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D main = image.createGraphics();
        main.setColor(color);
        main.fillRect(0, 0, imageWidth, imageHeight);
        Graphics2D tip = image.createGraphics();
        Font font=null;
        tip.setColor(colorWhite);
        try {
             font = Font.createFont(Font.TRUETYPE_FONT, new File(httpServletRequest.getSession().getServletContext().getRealPath("/") + "/WEB-INF/views/business/material/resource/fzht.ttf"));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        font=font.deriveFont(Font.PLAIN, 42);
        tip.setFont(font);
        tip.drawString("北京大美数联整体家装", 340, 70);
        Graphics2D tip1 = image.createGraphics();
        tip.setColor(colorWhite);
        Font tipFont1 = font.deriveFont(Font.PLAIN, 18);
        tip1.setFont(tipFont1);
        tip1.drawString("BEI JING MEDILL Y DECORATION DESIGN CO.LTD.", 350, 95);
        Graphics2D center = image.createGraphics();
        center.setColor(colorWhite);
        center.fillRect(30, 120, imageWidth - 60, imageHeight - 150);
        Graphics2D f1 = image.createGraphics();
        f1.setColor(color);
        font=font.deriveFont(Font.PLAIN, 36);
        f1.setFont(font);
        int xBegin = 560;
        int xLineBegin = 720, xLineEnd = 1000;
        int yBegin = 210, yBetween = 60;
        String label1 = byIdAndType.getLabel1();
        String label2 = byIdAndType.getLabel2();
        String label3 = byIdAndType.getLabel3();
        if (StringUtils.isBlank(label2)) {
            yBegin += yBetween / 3;
        }
        if (StringUtils.isBlank(label3)) {
            yBegin += yBetween / 3;
        }
        font=font.deriveFont(Font.PLAIN, 36);
        f1.setFont(font);
        f1.drawString("商品品牌", xBegin, yBegin);
        font=font.deriveFont(Font.PLAIN, 28);
        f1.setFont(font);
        f1.drawString(StringUtils.defaultString(byIdAndType.getProductBrandName(),""), xBegin + 180, yBegin);
        f1.drawLine(xLineBegin, yBegin + 3, xLineEnd, yBegin + 3);
        String name = byIdAndType.getName();
        font=font.deriveFont(Font.PLAIN, 36);
        f1.setFont(font);
        f1.drawString("商品名称", xBegin, yBegin += yBetween);
        font=font.deriveFont(Font.PLAIN, 28);
        f1.setFont(font);
        f1.drawString(StringUtils.defaultString(name,""), xBegin + 180, yBegin);
        font=font.deriveFont(Font.PLAIN, 36);
        f1.setFont(font);
        f1.drawLine(xLineBegin, yBegin + 3, xLineEnd, yBegin + 3);
        f1.drawString("商品型号", xBegin, yBegin += yBetween);
        font=font.deriveFont(Font.PLAIN, 28);
        f1.setFont(font);
        f1.drawString(StringUtils.defaultString(byIdAndType.getProductModel(),""), xBegin + 180, yBegin);
        font=font.deriveFont(Font.PLAIN, 36);
        f1.setFont(font);
        f1.drawLine(xLineBegin, yBegin + 3, xLineEnd, yBegin + 3);
        if (StringUtils.isNotBlank(label1)) {
            f1.drawString(label1, xBegin, yBegin += yBetween);
            font=font.deriveFont(Font.PLAIN, 28);
            f1.setFont(font);
            f1.drawString(StringUtils.defaultString(byIdAndType.getAttribute1(),""), xBegin + 180, yBegin);
            font=font.deriveFont(Font.PLAIN, 36);
            f1.setFont(font);
            f1.drawLine(xLineBegin, yBegin + 3, xLineEnd, yBegin + 3);
        }
        if (StringUtils.isNotBlank(label2)) {
            f1.drawString(label2, xBegin, yBegin += yBetween);
            font=font.deriveFont(Font.PLAIN, 28);
            f1.setFont(font);
            f1.drawString(StringUtils.defaultString(byIdAndType.getAttribute2(),""), xBegin + 180, yBegin);
            font=font.deriveFont(Font.PLAIN, 36);
            f1.setFont(font);
            f1.drawLine(xLineBegin, yBegin + 3, xLineEnd, yBegin + 3);
        }
        if (StringUtils.isNotBlank(label3)) {
            f1.drawString(label3, xBegin, yBegin += yBetween);
            font=font.deriveFont(Font.PLAIN, 28);
            f1.setFont(font);
            f1.drawString(StringUtils.defaultString(byIdAndType.getAttribute3(),""), xBegin + 180, yBegin);
            font=font.deriveFont(Font.PLAIN, 36);
            f1.setFont(font);
            f1.drawLine(xLineBegin, yBegin + 3, xLineEnd, yBegin + 3);
        }
        BufferedImage bimg = null;
        String content = name + "_" + byIdAndType.getProductBrandName() + "_" + byIdAndType.getCode();
        // 图像类型
        Map<EncodeHintType, Object> hints = Maps.newHashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 生成矩阵
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, 400, 400, hints);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        BufferedImage bimg1 = null;
        try {
            bimg1 = javax.imageio.ImageIO.read(new java.io.File(httpServletRequest.getSession().getServletContext().getRealPath("/") + "/WEB-INF/views/business/material/resource/biao.jpg"));
        } catch (Exception e) {
        }
        if (main != null) {
            main.drawImage(bimg1, 80, 10, 222, 95, null);
            main.drawImage(bufferedImage, 80, 150, 400, 400, null);
            if ("UPGRADE".equals(type)) {
                font=font.deriveFont(Font.PLAIN, 36);
                f1.setFont(font);
                f1.setColor(colorBlack);
                f1.drawString("升级(套餐)", 140, 540);
                font=font.deriveFont(Font.PLAIN, 34);
                f1.setFont(font);
                f1.setColor(colorOriange);
                BigDecimal price = byIdAndType.getPrice();
                f1.drawString("+" + new java.text.DecimalFormat("#.00").format(price) + "元", 320, 540);
            } else {
                font=font.deriveFont(Font.PLAIN, 36);
                f1.setFont(font);
                f1.setColor(colorBlack);
                f1.drawString("套餐", 222, 540);
            }
            main.dispose();
        }
        return image;
    }
    private void drawImage(ProdSku byIdAndType, HttpServletResponse resp, BufferedImage image) {
        BufferedOutputStream bos = null;
        try {
            resp.setHeader("Content-Type", "application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment;filename=" + byIdAndType.getCode() + "_" + System.nanoTime() + ".png");
            ServletOutputStream outputStream = resp.getOutputStream();
            bos = new BufferedOutputStream(outputStream);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
            encoder.encode(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * @Description: 大美智装 用流写出到临时目录
     * @date: 2018/1/4  15:59
     *
     */
    private void drawImageTemp(ProdSku byIdAndType, HttpServletResponse resp, BufferedImage image,HttpServletRequest request) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new java.io.File(request.getSession().getServletContext().getRealPath("/") + "/WEB-INF/views/business/material/resource/jpgtemp", byIdAndType.getCode()+ "_" + System.nanoTime() + ".png"));
            bos = new BufferedOutputStream(fos);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bos);
            encoder.encode(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}