package com.sr;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

import static com.sr.Info.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/water")
public class WaterController {

    /**
     * 去水印
     * @param testcase mp4文件
     * @param x y 水印坐标
     * @param w h 水印大小
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/hand", method = RequestMethod.POST)
    public String WaterHand(@RequestParam("file") MultipartFile[] testcase,
                            @RequestParam(value = "x", defaultValue = "0") String x,
                            @RequestParam(value = "y", defaultValue = "0") String y,
                            @RequestParam(value = "w", defaultValue = "0") String w,
                            @RequestParam(value = "h", defaultValue = "0") String h) throws FileUploadException, IOException {
        JSONObject jsonObject = new JSONObject();
        String submission_id = RandomStringUtils.randomAlphanumeric(20);
        OS.mkdir(water_path + submission_id);
        FileCopyUtils.copy(testcase[0].getBytes(), new File(water_path + submission_id + "/" + water_mp4));
        System.out.println("begin====" + new Date());
        OS.exce("python3 " + path + " 4 " +
                water_path + submission_id + "/" + water_mp4 + " " +
                water_path + submission_id + "/" + water_overmp4 + " " + x + " " + y + " " + w + " " + h);
        System.out.println("end====" + new Date());
        jsonObject.put("submission_id", submission_id);
        return jsonObject.toJSONString();
    }

    /**
     * 下载合成字幕
     * @param submission_id mp4文件文件夹hash码
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void WaterDownload(HttpServletRequest request,
                              @RequestParam("submission_id") String submission_id,
                              HttpServletResponse response) throws UnsupportedEncodingException {
        File file = new File(water_path + submission_id + "/" + water_overmp4);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Content-Disposition", "attachment;filename=" + water_overmp4);
        response.addHeader("Content-Length", "" + file.length());
        response.setContentType("application/octet-stream");
        OutputStream toClient = null;
        InputStream fis = null;
        try {
            toClient = new BufferedOutputStream(response.getOutputStream());
            fis = new BufferedInputStream(new FileInputStream(file));
            System.out.println(fis.available());
            //通过ioutil 对接输入输出流，实现文件下载
            IOUtils.copy(fis, toClient);
            toClient.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(toClient);
        }
    }


}
