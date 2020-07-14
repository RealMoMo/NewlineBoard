package com.newline.borad.widget.note

import kotlin.math.absoluteValue

/**
 * @name NewlineBoard
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/7/13 11:21
 * @describe
 */
class NoteTouchBean {

    companion object{
        const val DEFAULT_COORDINATE = -1F
    }

    private val id :Int
    private var lastX:Float = DEFAULT_COORDINATE
    private var lastY:Float = DEFAULT_COORDINATE
    private var currentX:Float = DEFAULT_COORDINATE
    private var currentY:Float = DEFAULT_COORDINATE


    constructor(id:Int){
        this.id = id
    }


    fun reset(){
        lastX = DEFAULT_COORDINATE
        lastY = DEFAULT_COORDINATE
        currentX = DEFAULT_COORDINATE
        currentY = DEFAULT_COORDINATE
    }


    fun setCurrentCoordinate(x:Float,y:Float){

        lastX = currentX
        lastY = currentY

        currentX = x
        currentY = y

    }


    fun getDx():Float{

        if(lastX == DEFAULT_COORDINATE){
            return DEFAULT_COORDINATE
        }
        return currentX - lastX
    }

    fun getDy():Float{

        if(lastY == DEFAULT_COORDINATE){
            return DEFAULT_COORDINATE
        }
        return currentY - lastY
    }



}