package com.sr;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

import static com.sr.Info.*;
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/srt")
public class SrtController {
    /**
     * 生成字幕
     * @param testcase mp4文件
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/hand", method = RequestMethod.POST)
    public String SrtHand(@RequestParam("file") MultipartFile[] testcase) throws FileUploadException, IOException {
        JSONObject jsonObject = new JSONObject();
        String submission_id = RandomStringUtils.randomAlphanumeric(20);
        OS.mkdir(srt_path + submission_id);
        FileCopyUtils.copy(testcase[0].getBytes(), new File(srt_path + submission_id + "/" + srt_mp4));
        System.out.println("begin====" + new Date());
        OS.exce("python3 " + path + " 1 " + srt_path + submission_id + "/" + srt_mp4);
        System.out.println("end====" + new Date());
        jsonObject.put("submission_id", submission_id);
        return jsonObject.toJSONString();
    }

    /**
     * 生成字幕
     * @param submission_id mp4文件文件夹hash码
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void SrtDownload(HttpServletRequest request,
                            @RequestParam("submission_id") String submission_id,
                            HttpServletResponse response) throws UnsupportedEncodingException {
        File file = new File(srt_path + submission_id + "/" + srt_srt);
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Content-Disposition", "attachment;filename=" + srt_srt);
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
