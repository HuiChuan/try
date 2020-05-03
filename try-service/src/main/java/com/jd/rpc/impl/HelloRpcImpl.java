package com.jd.rpc.impl;

import com.jd.rpc.HelloRpc;

public class HelloRpcImpl implements HelloRpc {
    @Override
    public String say() {
        return "rpc say hello!";
    }
}