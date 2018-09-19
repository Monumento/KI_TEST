/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;
public class Create
{
    public static void main(String[] args)
    {
        try
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("public class Test\n");
            stringBuilder.append("{\n");
            stringBuilder.append("\tpublic void printHello()\n");
            stringBuilder.append("\t{\n");
            stringBuilder.append("\t\tSystem.err.println(\"Hello World!\");\n");
            stringBuilder.append("\t}\n");
            stringBuilder.append("}\n");
            
            ClassFactory classFactory = new ClassFactory("Test.java");
            classFactory.setCode(stringBuilder.toString());
            
            boolean compileSucces = classFactory.compile();
            
            if(compileSucces)
            {
                classFactory.invokeMethod("printHello", null, null);
            }
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
            
                
    }
}
