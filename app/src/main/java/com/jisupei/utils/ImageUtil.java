package com.jisupei.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;  
import android.graphics.Color;
import android.graphics.LinearGradient;  
import android.graphics.Matrix;  
import android.graphics.Paint;  
import android.graphics.PixelFormat;  
import android.graphics.PorterDuffXfermode;  
import android.graphics.Rect;  
import android.graphics.RectF;  
import android.graphics.Bitmap.Config;  
import android.graphics.PorterDuff.Mode;  
import android.graphics.Shader.TileMode;  
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;

/**
 *	[简要描述]:   图像特效处理
 *	@author	    madi
 */

public class ImageUtil {  
    
    //放大缩小图片  
    public static Bitmap zoomBitmap(Bitmap bitmap,int w,int h){  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        Matrix matrix = new Matrix();  
        float scaleWidht = ((float)w / width);  
        float scaleHeight = ((float)h / height);  
        matrix.postScale(scaleWidht, scaleHeight);  
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);  
        return newbmp;  
    }  
    //将Drawable转化为Bitmap  
     public static Bitmap drawableToBitmap(Drawable drawable){  
            int width = drawable.getIntrinsicWidth();  
            int height = drawable.getIntrinsicHeight();  
            Bitmap bitmap = Bitmap.createBitmap(width, height,  
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                            : Bitmap.Config.RGB_565);  
            Canvas canvas = new Canvas(bitmap);  
            drawable.setBounds(0,0,width,height);  
            drawable.draw(canvas);  
            return bitmap;  
              
        }  
       
     //获得圆角图片的方法  
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){  
          
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap  
                .getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
   
        final int color = 0xff424242;  
   
        final Paint paint = new Paint();  
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);  
   
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
   
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
   
        return output;  
    }  
  //生成圆角图片
    public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);                
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());       
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = 8;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.WHITE);       
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));            
      
            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
              
            canvas.drawBitmap(bitmap, src, rect, paint);   
            return output;
        } catch (Exception e) {        
            return bitmap;
        }
    }
    //获得带倒影的图片方法  
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap){  
        final int reflectionGap = 4;  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
          
        Matrix matrix = new Matrix();  
        matrix.preScale(1, -1);  
          
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap,   
                0, height/2, width, height/2, matrix, false);  
          
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);  
          
        Canvas canvas = new Canvas(bitmapWithReflection);  
        canvas.drawBitmap(bitmap, 0, 0, null);  
        Paint deafalutPaint = new Paint();  
        canvas.drawRect(0, height,width,height + reflectionGap,  
                deafalutPaint);  
          
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);  
          
        Paint paint = new Paint();  
        LinearGradient shader = new LinearGradient(0,  
                bitmap.getHeight(), 0, bitmapWithReflection.getHeight()  
                + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);  
        paint.setShader(shader);  
        // Set the Transfer mode to be porter duff and destination in  
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));  
        // Draw a rectangle using the paint with our linear gradient  
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()  
                + reflectionGap, paint);  
   
        return bitmapWithReflection;  
    }  
      
    
    
    /** 
     *  
     * @param imgPath 
     * @param bitmap 
     * @param imgFormat 图片格式 
     * @return 
     */  
    public static String imgToBase64(String imgPath, Bitmap bitmap,String imgFormat) {  
        if (imgPath !=null && imgPath.length() > 0) {  
            bitmap = readBitmap(imgPath);  
        }  
        if(bitmap == null){  
            //bitmap not found!!  
        }  
        ByteArrayOutputStream out = null;  
        try {  
            out = new ByteArrayOutputStream();  
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);  
  
            out.flush();  
            out.close();  
  
            byte[] imgBytes = out.toByteArray();  
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            return null;  
        } finally {  
            try {  
                out.flush();  
                out.close();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }  
    public static String imgToBase641(String imgPath, Bitmap bitmap,String imgFormat) {  
    	Bitmap       bitmap1 = null;
        if (imgPath !=null && imgPath.length() > 0) {  
            bitmap = readBitmap(imgPath);  
//             bitmap1=    getRoundedCornerBitmap(bitmap, 8.0f);
             bitmap1=    GetRoundedCornerBitmap(bitmap);
        }  
        if(bitmap == null){  
            //bitmap not found!!  
        }  
        ByteArrayOutputStream out = null;  
        try {  
            out = new ByteArrayOutputStream();  
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);  
  
            out.flush();  
            out.close();  
  
            byte[] imgBytes = out.toByteArray();  
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);  
//            return Base64.encodeToString(imgBytes, 0, imgBytes.length,Base64.DEFAULT);  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            return null;  
        } finally {  
            try {  
                out.flush();  
                out.close();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }  
  
    private static Bitmap readBitmap(String imgPath) {  
        try {  
            return BitmapFactory.decodeFile(imgPath);  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            return null;  
        }  
  
    }
    
    
    
    // 根据 压缩到常见屏幕 800*480分辨率
    public static Bitmap comp(Bitmap image) { 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();          
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);   
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出     
            baos.reset();//重置baos即清空baos   
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中   
        }   
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());   
        BitmapFactory.Options newOpts = new BitmapFactory.Options();   
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了   
        newOpts.inJustDecodeBounds = true;   
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);   //读入图片
        newOpts.inJustDecodeBounds = false;   
        int w = newOpts.outWidth;   
        int h = newOpts.outHeight;   
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为   
        float hh = 800f;//这里设置高度为800f   
        float ww = 480f;//这里设置宽度为480f   
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可   
        int be = 1;//be=1表示不缩放   
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放   
            be = (int) (newOpts.outWidth / ww);   
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放   
            be = (int) (newOpts.outHeight / hh);   
        }   
        if (be <= 0)   
            be = 1;   
        newOpts.inSampleSize = be;//设置缩放比例   
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了   
        isBm = new ByteArrayInputStream(baos.toByteArray());   
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);   
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩   
  } 
    
    // 路径  压缩到常见屏幕 800*480分辨率,  写到sd卡
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        
        bitmap = compressImage(bitmap);  //质量压缩
        return bitmap;  
    }
    public static Bitmap compressImage(Bitmap image) {  
    	  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }


    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}  