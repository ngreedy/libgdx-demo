package vip.skyhand.libgdxtextureview;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * game_werewolf
 * 2018/4/3 下午1:57
 * Mystery
 */

public class ZipFileToolKit {
    /**
     * 这是一个坑方法，unzipPath要以 「File.separator」结尾
     *
     * @param path
     * @param unzipPath
     * @throws Exception
     */
    public static void unPackZip(String path, String unzipPath) throws Exception {
        InputStream is = null;
        ZipInputStream zis = null;
        try {
            String filename;

            is = new FileInputStream(path);
            zis = new ZipInputStream(new BufferedInputStream(is));

            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(unzipPath + filename);
                    fmd.mkdirs();
                    continue;
                }

                File file = new File(unzipPath + filename);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                FileOutputStream fout = new FileOutputStream(file);
                try {
                    // cteni zipu a zapis
                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (zis != null) {
                zis.closeEntry();
            }
        }
    }

    public static String unZip(String zipFile, String targetDir) throws Exception {

        int BUFFER = 4096; //这里缓冲区我们使用4KB，
        String strEntry; //保存每个zip的条目名称

        ZipInputStream zis = null;
        try {
            BufferedOutputStream dest = null; //缓冲输出流
            FileInputStream fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry; //每个zip条目的实例
            File entryDir = null;
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();

                    File entryFile = new File(targetDir + strEntry);

                    if (entryFile.getName().startsWith(".")) {
                        /**
                         * 忽略掉已 「 . 」开头的文件
                         */
                        continue;
                    }

                    if (entryFile.getName().contains("MACOSX")) {
                        /**
                         * 忽略掉Mac文件
                         */
                        continue;
                    }

                    entryDir = new File(entryFile.getParent());

                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(entryFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        /**
                         * 出错则直接跳过
                         */
                        Log.i("MiniGameManager", " error file " + entryFile.getName());
                        continue;
                    }
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    if (entryFile.getName().contains("mp4")) {
                        throw new IllegalArgumentException(entryFile.getAbsolutePath());
                    }

                } catch (Exception ex) {
                    if (ex instanceof IllegalArgumentException) {
                        return ex.getMessage();
                    }
                    ex.printStackTrace();
                } finally {
                }
            }
            return entryDir.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
        }

    }

    /**
     * 解压assets的zip压缩文件到指定目录
     *
     * @param context上下文对象
     * @param assetName压缩文件名
     * @param outputDirectory输出目录
     * @param isReWrite是否覆盖
     * @throws IOException
     */
    public static void unZip(Context context, String assetName,
                             String outputDirectory, boolean isReWrite) throws IOException {
        //创建解压目标目录
        File file = new File(outputDirectory);
        //如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        //打开压缩文件
        InputStream inputStream = context.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        //读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        //使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        //解压时字节计数
        int count = 0;
        //如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            //如果是一个目录
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                //文件需要覆盖或者是文件不存在
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                //如果是文件
                file = new File(outputDirectory + File.separator
                        + zipEntry.getName());
                //文件需要覆盖或者文件不存在，则解压文件
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
            }
            //定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }
}
