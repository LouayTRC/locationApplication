package services;

import android.graphics.Bitmap;

public interface PictureService {
    String compressImageToBase64(Bitmap bitmap);
    Bitmap decompressBase64ToImage(String base64Image);
}
