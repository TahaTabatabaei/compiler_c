package Compiler;

import java.util.ArrayList;

public class MethodTable extends SymbolTable {
    public ArrayList<BlockTable> blocks = new ArrayList<BlockTable>();
    public String paramList="";
    public int paramNum = 0;

}
