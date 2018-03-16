package com.imooc.miaosha.exception;

import com.imooc.miaosha.result.CodeMsg;

/**
 * Created by liliwei on 2018/3/5.
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm=cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
