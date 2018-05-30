package com.zz;

import com.zz.lib.orm.utils.JavaFileUtils;

public class CodeGenerate {
    public static void main(String[] args) {
        JavaFileUtils.createModuleByTableName("sys_table_info");
    }
}
