import java.util.ArrayList;
import java.util.List;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public abstract class SingleThreadRenderer {
    void renderPage(CharSequence source) {
        // 文本
        renderText(source);
        // 图像
        List<ImageData> imageData = new ArrayList<>();
        for (ImageInfo imageInfo : scanForImageInfo(source)) {
            imageData.add(imageInfo.downloadImage());
        }
        for (ImageData data : imageData) {
            renderImage(data);
        }
    }

    interface ImageData {
    }

    interface ImageInfo {
        ImageData downloadImage();
    }

    abstract void renderText(CharSequence s);
    abstract List<ImageInfo> scanForImageInfo(CharSequence s);
    abstract void renderImage(ImageData i);
}