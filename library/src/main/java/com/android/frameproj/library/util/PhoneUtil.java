package com.android.frameproj.library.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WE-WIN-027 on 2016/7/5.
 *
 * @des ${TODO}
 */
public class PhoneUtil {

    /**
     * 手机号验证
     *
     * @param  str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
}
