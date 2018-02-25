/*
* The MIT License (MIT)
*
* Copyright (c) 2014-2016 abel533@gmail.com
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/

package com.su.blog.model;

import com.su.blog.model.base.BaseEntity;

/**
 * @author $ekkoSu
 * @since 2018-02-14 13:30:39
 */
public class Menu extends BaseEntity {

/**
* 名字
*/
private  String name;

/**
* 父级Id
*/
private  Long parentId;


/**
* 构造方法
*/
public Menu() {
}


/**
* 获取名字
*/
public String getName() {
return name;
}

/**
* 设置名字
*/
public void setName(String name) {
this.name = name;
}

/**
* 获取父级Id
*/
public Long getParentId() {
return parentId;
}

/**
* 设置父级Id
*/
public void setParentId(Long parentId) {
this.parentId = parentId;
}

}
