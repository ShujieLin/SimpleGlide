package com.shujie.simple_glide;

import com.shujie.simple_glide.utils.Tool;

/**
 * @date: 2022/5/23
 * @author: linshujie
 */
public class Key {
    private String key;

    /**
     * 转换成符合磁盘缓存规范的key
     * @param key
     */
    public Key(String key) {
        this.key = Tool.getSHA256StrJava(key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
