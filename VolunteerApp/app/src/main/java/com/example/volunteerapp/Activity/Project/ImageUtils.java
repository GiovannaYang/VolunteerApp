package com.example.volunteerapp.Activity.Project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtils {

    public static String saveImageToLocal(Context context, Uri imageUri) {
        // 获取图片输入流
        InputStream inputStream;
        try {
            inputStream = context.getContentResolver().openInputStream(imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // 创建保存图片的目录
        File imageDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "project_images");
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        // 生成图片文件名
        String fileName = "project_" + System.currentTimeMillis() + ".jpg";
        File imageFile = new File(imageDir, fileName);

        // 保存图片到本地文件
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(imageFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return imageFile.getAbsolutePath();
    }

    public static Bitmap decodeSampledBitmapFromPath(String imagePath, int reqWidth, int reqHeight) {
        // 获取图片的宽和高，并设置到options中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // 计算图片的缩放比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 重新加载图片，设置缩放比例
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原图片的宽和高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // 计算宽高的压缩比例
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}

