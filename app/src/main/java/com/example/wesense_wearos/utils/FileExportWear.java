package com.example.wesense_wearos.utils;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileExportWear {
    static String sParentFileName = "SensorDataStore";         /** Root path(can be changed) */
    static String sFileName = "senseData.csv";         /** File Name(can be changed) */

    public static File ExportToCSV(Cursor c, String fileName,String pParentFileName) {
            if(pParentFileName!=null) sParentFileName = pParentFileName;
            if(fileName!=null) sFileName = fileName;
            int rowCount = 0;
            int colCount = 0;
            FileWriter fw;
            BufferedWriter bfw;
            File sdCardDir = Environment.getExternalStorageDirectory();
            File parentFile = new File(sdCardDir, sParentFileName);
            if(!parentFile.exists()) parentFile.mkdirs();
            File saveFile = new File(parentFile, sFileName);
            if(saveFile.exists() && saveFile.isFile()) {
                saveFile.delete();
            }
            try {
                rowCount = c.getCount();
                colCount = c.getColumnCount();
                fw = new FileWriter(saveFile);
                bfw = new BufferedWriter(fw);
                if (rowCount > 0) {
                    c.moveToFirst();
                    for (int i = 0; i < colCount; i++) {
                        if (i != colCount - 1)
                            bfw.write(c.getColumnName(i) + ',');
                        else
                            bfw.write(c.getColumnName(i));
                    }
                    bfw.newLine();
                    // 写入数据
                    for (int i = 0; i < rowCount; i++) {
                        c.moveToPosition(i);
                        //Log.v("数据导出", "导出第" + (i + 1) + "条");
                        for (int j = 0; j < colCount; j++) {
                            if (j != colCount - 1)
                                bfw.write(c.getString(j) + ',');
                            else {
                                String tempS = c.getString(j);
                                if(tempS == null) bfw.write("null");
                                else bfw.write(tempS);
                            }
                        }
                        bfw.newLine();
                    }
                }
                bfw.flush();
                bfw.close();
                fw.close();
                Log.v("数据导出", "数据导出完成！");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                c.close();
            }
            return saveFile;
        }

    public static File exportToTextForEachSensor(Cursor cur, String fileName, String pParentFileName) {
        if(pParentFileName!=null) sParentFileName = pParentFileName;
        if(fileName!=null) sFileName = fileName;
        int rowCount = 0;
        int colCount = 0;
        FileWriter fw;
        BufferedWriter bfw;
        File sdCardDir = Environment.getStorageDirectory();
        File parentFile = new File(sdCardDir, sParentFileName);
        if(!parentFile.exists()) parentFile.mkdirs();
        File saveFile = new File(parentFile, sFileName);
        if(saveFile.exists() && saveFile.isFile()) {
            saveFile.delete();
        }
        try {
            rowCount = cur.getCount();
            colCount = cur.getColumnCount();
            fw = new FileWriter(saveFile);
            bfw = new BufferedWriter(fw);
            if (rowCount > 0) {
                cur.moveToFirst();
                /** Write the data */
                for (int i = 0; i < rowCount; i++) {
                    cur.moveToPosition(i);
                    bfw.write(cur.getString(1) + "/");
                    for (int j = 2; j < colCount; j++) {
                        if (j != colCount - 1)
                            bfw.write(cur.getString(j) + '_');
                        else {
                            String tempS = cur.getString(j);
                            if(tempS == null) bfw.write("null");
                            else bfw.write(tempS);
                        }
                    }
                    bfw.newLine();
                }
            }
            bfw.flush();
            bfw.close();
            Log.v("数据导出", "数据导出完成！");
        } catch (IOException exp) {
            // TODO Auto-generated catch block
            exp.printStackTrace();
        } finally {
            cur.close();
        }
        return saveFile;
    }
}
