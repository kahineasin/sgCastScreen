package com.sellgirl.castScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;
import com.sellgirl.sgGameHelper.ISGLanguage;
import com.sellgirl.sgJavaHelper.config.SGDataHelper;

import java.util.Locale;

public class Language implements Disposable , ISGLanguage {
    private final String tag="Language";
    private  I18NBundle myBundle;
    public Language(AssetManager assetManager
    ){
        assetManager.load("i18n/MyBundle", I18NBundle.class);
        assetManager.finishLoadingAsset("i18n/MyBundle");
        myBundle = assetManager.get("i18n/MyBundle", I18NBundle.class);
    }

    /**
     * 指定语言
     * Locale locale = new Locale("fr", "CA", "VAR1");
     * @param locale
     */
    public Language(
        Locale locale
    ){
        FileHandle baseFileHandle = Gdx.files.internal("i18n/MyBundle");
         myBundle = I18NBundle.createBundle(baseFileHandle, locale);
    }

    public String g(String key){
        try {
            String value = myBundle.get(key);
            return value;
        }catch (Throwable e){
            SGDataHelper.getLog().printException(e,tag);
            return key;
        }
    }

    @Override
    public void dispose() {
        myBundle=null;
    }
//
//    public String g(String key, Object... args){
//        String value = myBundle.format(key, args);
//
//        return value;
//    }
//    private static final String DEFAULT_ENCODING = "UTF-8";
//    public static I18NBundle createBundle (FileHandle baseFileHandle, Locale locale, String encoding) {
//        return createBundleImpl(baseFileHandle, locale, encoding);
//    }
//    private static List<Locale> getCandidateLocales (Locale locale) {
//        String language = locale.getLanguage();
//        String country = locale.getCountry();
//        String variant = locale.getVariant();
//        List<Locale> locales = new ArrayList<Locale>(4);
//        if (variant.length() > 0) {
//            locales.add(locale);
//        }
//        if (country.length() > 0) {
//            locales.add((locales.size() == 0) ? locale : new Locale(language, country));
//        }
//        if (language.length() > 0) {
//            locales.add((locales.size() == 0) ? locale : new Locale(language));
//        }
//        locales.add(Locale.ROOT);
//        return locales;
//    }
}
