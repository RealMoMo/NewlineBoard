package com.hhtpc.hhstrokesadr;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Realmo
 * @version 1.0.0
 * @name NewlineBoard
 * @email momo.weiye@gmail.com
 * @time 2020/8/4 10:32
 * @describe
 */
@IntDef({HHTStrokePathType.TYPE_HARDPEN,HHTStrokePathType.TYPE_SOFTPEN})
@Retention(RetentionPolicy.SOURCE)
public @interface HHTStrokePathType {
    //硬笔类型
    int TYPE_HARDPEN = 1;
    //软笔类型
    int TYPE_SOFTPEN = 2;
}
