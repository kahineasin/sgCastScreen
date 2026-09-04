package com.sellgirl.castScreen.language;

import com.sellgirl.sgGameHelper.ISGLanguage;

public class SGTxt implements ISGLanguage {

    @Override
    public String g(String key) {
        return TXT.g(key);
    }
}
