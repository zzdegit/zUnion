package com.zz.custom.${moduleName}.po;

<#list importSet as item>
import ${item};
</#list>

public class ${className} {

<#list fieldList as item>
    /**
     * ${item.remarks}
     */
    private ${item.type} ${item.name};
</#list>

    public ${className}() {
    	
    }
    
    public ${className}(<#list fieldList as item>${item.type} ${item.name}<#if item_has_next>,</#if></#list>) {
<#list fieldList as item>
        this.${item.name} = ${item.name};
</#list>
    }
    
<#list fieldList as item>
    public ${item.type} get${item.name?cap_first}() {
        return ${item.name};
    }
    
    public void set${item.name?cap_first}(${item.type} ${item.name}) {
        this.${item.name} = ${item.name};
    }
    
</#list>
}
