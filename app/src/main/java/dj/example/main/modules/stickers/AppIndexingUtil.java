package dj.example.main.modules.stickers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseAppIndexingInvalidArgumentException;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.StickerBuilder;
import com.google.firebase.appindexing.builders.StickerPackBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dj.example.main.MyApplication;
import dj.example.main.model.response.HomeDataResponse;
import dj.example.main.utils.MyPrefManager;

/**
 * See firebase app indexing api code lab
 * https://codelabs.developers.google.com/codelabs/app-indexing/#0
 */

public class AppIndexingUtil {
    private static final String STICKER_FILENAME_PATTERN = "sticker%s.png";
    private static final String STICKER_URL_PATTERN = "mystickers://sticker/%s";
    private static final String STICKER_PACK_URL_PATTERN = "mystickers://sticker/pack/%s";
    private static final String STICKER_PACK_NAME = "TamilMoji Content Pack";
    private static final String TAG = "AppIndexingUtil";
    public static final String FAILED_TO_INSTALL_STICKERS = "Failed to install stickers";

    public static void setStickers(final Context context, FirebaseAppIndex firebaseAppIndex) {
        try {
            List<Indexable> stickers = /*getIndexableStickers()*/getIndexableStickersNew();;
            Indexable stickerPack = getIndexableStickerPack(context);

            List<Indexable> indexables = new ArrayList<>(stickers);
            indexables.add(stickerPack);

            Task<Void> task = firebaseAppIndex.update(
                    indexables.toArray(new Indexable[indexables.size()]));

            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    MyPrefManager.getInstance().setIsStickersAdded(true);
                    Toast.makeText(context, "Successfully added stickers", Toast.LENGTH_SHORT)
                            .show();
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, FAILED_TO_INSTALL_STICKERS, e);
                    MyPrefManager.getInstance().setIsStickersAdded(false);
                    Toast.makeText(context, FAILED_TO_INSTALL_STICKERS, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } catch (IOException | FirebaseAppIndexingInvalidArgumentException e) {
            Log.e(TAG, "Unable to set stickers", e);
        }
    }

    @Deprecated
    private static Indexable getIndexableStickerPack(List<Indexable> stickers)
            throws IOException, FirebaseAppIndexingInvalidArgumentException {
        MyApplication myApplication = MyApplication.getInstance();
        if (myApplication.getPrimaryEmojis().size() <= 0)
            return null;
        Indexable.Builder indexableBuilder = getIndexableBuilder(myApplication.getPrimaryEmojis().get(0).img, STICKER_PACK_URL_PATTERN, stickers.size());
        indexableBuilder.put("hasSticker", stickers.toArray(new Indexable[stickers.size()]));
        return indexableBuilder.build();
    }

    @Deprecated
    private static List<Indexable> getIndexableStickers() throws IOException,
            FirebaseAppIndexingInvalidArgumentException {
        MyApplication myApplication = MyApplication.getInstance();
        if (myApplication.getPrimaryEmojis().size() <= 0)
            return new ArrayList<>();
        List<Indexable> indexableStickers = new ArrayList<>();
        for (int i = 1; i < myApplication.getPrimaryEmojis().size(); i++) {
            HomeDataResponse.HomeResponse.HomeData.Emojis item = myApplication.getPrimaryEmojis().get(i);
            String[] temp = item.tags.split(",");
            Indexable.Builder indexableStickerBuilder = getIndexableBuilder(item.img, STICKER_URL_PATTERN, i);
            indexableStickerBuilder.put("keywords", /*"tag1_" + i, "tag2_" + i*/temp)
                    // StickerPack object that the sticker is part of.
                    .put("partOf", new Indexable.Builder("StickerPack")
                            .setName(STICKER_PACK_NAME)
                            .build());
            indexableStickers.add(indexableStickerBuilder.build());
        }
        return indexableStickers;
    }

    @Deprecated
    private static Indexable.Builder getIndexableBuilder(String stickerURL, String urlPattern, int index)
            throws IOException {
        String url = String.format(urlPattern, index);

        Indexable.Builder indexableBuilder = new Indexable.Builder("StickerPack")
                // name of the sticker pack
                .setName(STICKER_PACK_NAME)
                // Firebase App Indexing unique key that must match an intent-filter
                // (e.g. mystickers://stickers/pack/0)
                .setUrl(url)
                // (Optional) - Defaults to the first sticker in "hasSticker"
                // displayed as a category image to select between sticker packs that should
                // be representative of the sticker pack
                //.setImage(contentUri.toString())
                .setImage(stickerURL)
                // (Optional) - Defaults to a generic phrase
                // content description of the image that is used for accessibility
                // (e.g. TalkBack)
                .setDescription("Indexable description");

        return indexableBuilder;
    }




    private static Indexable getIndexableStickerPack(Context context)
            throws IOException, FirebaseAppIndexingInvalidArgumentException {
        List<StickerBuilder> stickerBuilders = getStickerBuilders();
        if (stickerBuilders.size() <= 0)
            return null;
        File stickersDir = new File(context.getFilesDir(), "stickers");

        if (!stickersDir.exists() && !stickersDir.mkdirs()) {
            throw new IOException("Stickers directory does not exist");
        }

        // Use the last sticker for category image for the sticker pack.
        //final int lastIndex = stickerBuilders.size() - 1;
        final int firstIndex = 0;
        final String imageUrl = MyApplication.getInstance().getPrimaryEmojis().get(firstIndex).img;

        StickerPackBuilder stickerPackBuilder = Indexables.stickerPackBuilder()
                .setName(STICKER_PACK_NAME)
                // Firebase App Indexing unique key that must match an intent-filter.
                // (e.g. mystickers://sticker/pack/0)
                .setUrl(String.format(STICKER_PACK_URL_PATTERN, firstIndex))
                // Defaults to the first sticker in "hasSticker". Used to select between sticker
                // packs so should be representative of the sticker pack.
                .setImage(imageUrl)
                .setHasSticker(stickerBuilders.toArray(new StickerBuilder[stickerBuilders.size()]))
                .setDescription("Pack 0");
        return stickerPackBuilder.build();
    }

    private static List<Indexable> getIndexableStickersNew() throws IOException,
            FirebaseAppIndexingInvalidArgumentException {
        List<Indexable> indexableStickers = new ArrayList<>();
        List<StickerBuilder> stickerBuilders = getStickerBuilders();
        if (stickerBuilders.size() <= 0)
            return null;
        for (StickerBuilder stickerBuilder : stickerBuilders) {
            /*stickerBuilder
                    .setIsPartOf(Indexables.stickerPackBuilder()
                            .setName(STICKER_PACK_NAME))
                    .put("keywords", "tag1", "tag2");*/
            indexableStickers.add(stickerBuilder.build());
        }

        return indexableStickers;
    }

    private static List<StickerBuilder> getStickerBuilders(){
        MyApplication myApplication = MyApplication.getInstance();
        if (myApplication.getPrimaryEmojis().size() <= 0)
            return new ArrayList<>();
        List<StickerBuilder> stickerBuilders = new ArrayList<>();
        for (int i = 1; i < myApplication.getPrimaryEmojis().size(); i++){
            HomeDataResponse.HomeResponse.HomeData.Emojis item = myApplication.getPrimaryEmojis().get(i);
            String[] temp = item.tags.split(",");
            StickerBuilder stickerBuilder = Indexables.stickerBuilder()
                    .setName(getStickerFilename(i))
                    // Firebase App Indexing unique key that must match an intent-filter
                    // (e.g. mystickers://sticker/0)
                    .setUrl(String.format(STICKER_URL_PATTERN, i))
                    // http url or content uri that resolves to the sticker
                    // (e.g. http://www.google.com/sticker.png or content://some/path/0)
                    .setImage(item.img)
                    .setDescription("content description")
                    .setIsPartOf(Indexables.stickerPackBuilder()
                            .setName(STICKER_PACK_NAME))
                    .put("keywords", temp);
            stickerBuilders.add(stickerBuilder);
        }
        return stickerBuilders;
    }

    private static String getStickerFilename(int index) {
        return String.format(STICKER_FILENAME_PATTERN, index);
    }

}