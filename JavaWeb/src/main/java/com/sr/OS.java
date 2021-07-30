package com.sr;

import java.io.*;

public class OS {
    public static String path_join(String... paths) {
        StringBuffer finpath = new StringBuffer("");
        boolean first = true;
        String[] var3 = paths;
        int var4 = paths.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String path = var3[var5];
            if (first) {
                finpath = new StringBuffer(path);
                first = false;
            } else if (finpath.substring(finpath.length() - 1) == "/" && path.substring(0, 1) == "/") {
                finpath.append(path.substring(1));
            } else if (finpath.substring(finpath.length() - 1) == "/" && path.substring(0, 1) != "/") {
                finpath.append(path);
            } else if (finpath.substring(finpath.length() - 1) != "/" && path.substring(0, 1) == "/") {
                finpath.append(path);
            } else if (finpath.substring(finpath.length() - 1) != "/" && path.substring(0, 1) != "/") {
                finpath.append("/");
                finpath.append(path);
            }
        }

        return finpath.toString();
    }

    public static String dirname(String path) {
        return (new File(path)).getParentFile().getPath();
    }

    public static Boolean mkdir(String path) {
        return (new File(path)).mkdirs();
    }

    public static String chown(String path, String uid, String gid) {
        if ("-1".equals(uid) && "-1".equals(gid)) {
            return "";
        } else {
            String exce = "chown " + ("-1".equals(uid) ? "" : uid) + ("-1".equals(gid) ? "" : ":" + gid) + " " + path;
            return exce(exce);
        }
    }

    public static String chmod(String path, Integer permission) {
        String exce = "chmod " + String.valueOf(permission) + " " + path;
        return exce(exce);
    }

    public static String pwd() {
        String exce = "pwd";
        return exce(exce);
    }

    public static Boolean exists(String path) {
        return (new File(path)).exists();
    }

    public static void remove(String path) {
        (new File(path)).delete();
    }

    public static String getpwnam(String username) {
        String exce = "id -u " + username;
        String infos = exce(exce).trim();
        return "".equals(infos) ? "-1" : infos;
    }

    public static String getgrnam(String groupname) {
        String exce = "id -g " + groupname;
        String infos = exce(exce).trim();
        return "".equals(infos) ? "-1" : infos;
    }

    public static String old(String path) {
        String new_path = "";

        for(Integer i = 0; i < path.length(); i = i + 1) {
            char c = path.charAt(i);
            if (i == 92) {
                new_path = new_path + "\\\\";
            } else {
                new_path = new_path + String.valueOf(c);
            }
        }

        return new_path;
    }

    public static String exce(String cmd) {
        Runtime run = Runtime.getRuntime();

        try {
            String result = "";
            Process process = run.exec(cmd);
            SequenceInputStream sis = new SequenceInputStream (process.getInputStream (),process.getErrorStream ());
            InputStreamReader isr = new InputStreamReader (sis, "utf-8");
            BufferedReader br = new BufferedReader (isr);
            OutputStreamWriter osw = new OutputStreamWriter (process.getOutputStream ());
            BufferedWriter bw = new BufferedWriter (osw);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
            br.close();
            isr.close();
            process.destroy();
            return result;
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "";
    }
}
