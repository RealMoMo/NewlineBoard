package com.newline.draw.toolbar.data;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Realmo
 * @version 1.0.0
 * @name NewlineBoard
 * @email momo.weiye@gmail.com
 * @time 2020/7/16 10:49
 * @describe  Draw操作类型
 */

@IntDef ({DrawOperation.PENCIL,DrawOperation.SOFT_PEN,DrawOperation.SOFT_PEN_WITH_STROKE,DrawOperation.BRUSH,
DrawOperation.MARKER_PEN,DrawOperation.HIGHTLIGHT_PEN,DrawOperation.EARSE,DrawOperation.ROLLBACK,DrawOperation.RECOVER,
DrawOperation.CLEAR,DrawOperation.PRE_PAGE,DrawOperation.NEXT_PAGE,DrawOperation.ADD_PAGE,DrawOperation.PREVIEW_ALL,
DrawOperation.PICTURE_NORMAL,DrawOperation.PICTURE_SVG,DrawOperation.SHAPE,DrawOperation.SHAPE_AI,
DrawOperation.TEXT,DrawOperation.NOTE,DrawOperation.VIDEO,DrawOperation.PDF_FILE,DrawOperation.MARKDOWN_FILE,DrawOperation.FILE})
@Retention(RetentionPolicy.SOURCE)
public @interface DrawOperation {
    /**
     * 铅笔
     */
    int PENCIL = 1;
    /**
     * 软笔
     */
    int SOFT_PEN = PENCIL+1;
    /**
     * 带笔锋软笔
     */
    int SOFT_PEN_WITH_STROKE = PENCIL+2;
    /**
     * 毛笔
     */
    int BRUSH = PENCIL+3;
    /**
     * 记号笔(只短暂显示)
     */
    int MARKER_PEN = PENCIL+4;
    /**
     * 荧光笔
     */
    int HIGHTLIGHT_PEN = PENCIL+5;
    /**
     * 橡皮
     */
    int EARSE = PENCIL+6;
    /**
     * 回退
     */
    int ROLLBACK = PENCIL+7;
    /**
     * 恢复
     */
    int RECOVER = PENCIL+8;
    /**
     * 清除
     */
    int CLEAR = PENCIL+9;
    /**
     * 上一页
     */
    int PRE_PAGE = PENCIL+10;
    /**
     * 下一页
     */
    int NEXT_PAGE = PENCIL+11;
    /**
     * 新增一页
     */
    int ADD_PAGE = PENCIL+12;
    /**
     * 预览所有内容
     */
    int PREVIEW_ALL = PENCIL+13;
    /**
     * 普通格式的图片
     */
    int PICTURE_NORMAL = PENCIL+14;
    /**
     * svg格式的图片
     */
    int PICTURE_SVG = PENCIL+15;
    /**
     * 图形
     */
    int SHAPE = PENCIL+16;
    /**
     * 智能识别图形
     */
    int SHAPE_AI = PENCIL+17;
    /**
     * 纯文本
     */
    int TEXT = PENCIL+18;
    /**
     * 便签
     */
    int NOTE = PENCIL+19;
    /**
     * 视频
     */
    int VIDEO = PENCIL+20;
    /**
     * pdf文件
     */
    int PDF_FILE = PENCIL+21;
    /**
     * md格式文件
     */
    int MARKDOWN_FILE = PENCIL+22;
    /**
     * 其他常用办公文件 txt/doc/ppt/etc...
     */
    int FILE = PENCIL+23;


}
