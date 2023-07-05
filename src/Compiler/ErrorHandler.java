package Compiler;

import java.util.ArrayList;

public class ErrorHandler {
    public ArrayList<String> errors = new ArrayList<String>();
    public String errorMaker(int error,String type , int line ,int coulmn ,String name, String message){
        return "Error"+error+": in line ["+line+":"+coulmn+"], "+type+" "+name+" "+message+"";
    }

    public void printErrors(){
        for (String error : this.errors) {
            System.out.println(error);
        }
    }
}
