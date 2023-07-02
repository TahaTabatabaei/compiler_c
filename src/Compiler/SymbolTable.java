package Compiler;

import java.util.Hashtable;
import java.util.Map;

public class SymbolTable {
    public int lineNumber = 0;
    public SymbolTable parentNode;
    public String tableName="";
    public Hashtable<String,String> map = new Hashtable<String,String>();

    public String toString(){
        return "------------- " + this.tableName + " : " + this.lineNumber + " -------------\n" +
                this.printItems() +
                "-----------------------------------------\n";
    }
    public String printItems(){

        String itemsStr = "";
        for (Map.Entry<String,String> entry : this.map.entrySet()) {
            itemsStr += "Key : " + entry.getKey() + " | Value : " + entry.getValue()
                    + "\n";
        }
        return itemsStr;
    }

    public void insert(String idefName, String attributes){
        this.map.put(idefName, attributes);
    }

    public String lookUp(String idefName){
        return this.map.get(idefName);
    }
    public void tablePrinter(){
        System.out.print(this.toString());
        System.out.println("==============================================");
        if(this instanceof GlobalTable){
            for (MethodTable method : ((GlobalTable) this).methods)
                method.tablePrinter();
        }
        else if(this instanceof MethodTable){
            for (BlockTable block : ((MethodTable) this).blocks)
                block.tablePrinter();
        }
        else if(this instanceof BlockTable){
            for (BlockTable block : ((BlockTable) this).blocks)
                block.tablePrinter();
        }
    }
}

