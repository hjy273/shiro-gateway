package com.geekerstar.gateway.util;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;

import java.io.File;

/**
 * @author geekerstar
 * @date 2020/4/15 11:00
 * @description undertow查看服务器文件
 */
public class UndertowFileServer {
    public static void main(String[] args) {
        File file = new File("/");
        Undertow server = Undertow.builder().addHttpListener(8080, "localhost")
                .setHandler(Handlers.resource(new PathResourceManager(file.toPath(), 100))
                        .setDirectoryListingEnabled(true))
                .build();
        server.start();
    }
}
