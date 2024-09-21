package com.zheng.travel.browser.article.base64;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base64ReplaceTest {

    @Test
    public void test() {
        String base64ImgContent = "data:image/jpeg;base64,JSLKDJNUAISDUDNFADFANDLSFJASDF;JALSD;FADSFADSUHFNAJKDSF";

        Matcher matcher = Pattern.compile("^data.(.*?);base64,").matcher(base64ImgContent);
        if (matcher.find()) {
            base64ImgContent = base64ImgContent.replace(matcher.group(), "");
        }

        System.out.println(base64ImgContent);
    }
}
