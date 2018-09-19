/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class ClassFactory
{

    File file;

    FileWriter fileWriter;

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    List<File> fileList;

    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

    StandardJavaFileManager compileFileManager = compiler.getStandardFileManager(null, null, null);

    StandardJavaFileManager diagnosticsFileManager = compiler.getStandardFileManager(diagnostics, null, null);

    Iterable<? extends JavaFileObject> units;

    String code;

    public ClassFactory(String filename) throws IOException
    {
        this(new File(filename));
    }

    public ClassFactory(File file) throws IOException
    {
        this.file = file;
        fileWriter = new FileWriter(file);
        fileList = Arrays.asList(file);
        units = compileFileManager.getJavaFileObjectsFromFiles(fileList);
    }

    public void setCode(String code) throws IOException
    {
        fileWriter.write(code);
        fileWriter.close();
    }

    public void writeCode(String code) throws IOException
    {
        fileWriter.write(code);
    }

    public void closeCodeWriter() throws IOException
    {
        fileWriter.close();
    }

    public boolean compile() throws IOException
    {
        boolean isCodeOK = isCodeOK();
        if(isCodeOK)
        {
            compiler.getTask(null, null, null, null, null, units).call();

            diagnosticsFileManager.close();
            compileFileManager.close();
        }

        return isCodeOK;
    }

    public boolean isCodeOK()
    {
        diagnosticsFileManager = compiler.getStandardFileManager(diagnostics, null, null);

        compiler.getTask(null, diagnosticsFileManager, diagnostics, null, null, units).call();

        boolean codeOK = true;

        for (Diagnostic diagnostic : diagnostics.getDiagnostics())
        {
            System.err.println("Error on line " + diagnostic.getLineNumber() + " in " + diagnostic.getSource());
            System.err.println(diagnostic.getMessage(Locale.GERMANY));
            codeOK = false;
        }
        
        return codeOK;
    }

    public List<Diagnostic<? extends JavaFileObject>> getErrors()
    {
        return diagnostics.getDiagnostics();
    }

    public Class getCompiledClass() throws ClassNotFoundException
    {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        return classLoader.loadClass(file.getName().replace(".java", ""));
    }

    public Object invokeMethod(String methodName, Class[] parameterTypes, Object[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
    {
        if (parameterTypes == null)
        {
            parameterTypes = new Class[0];
        }

        if (args == null)
        {
            args = new Object[0];
        }

        Class c = getCompiledClass();
        Object classInstance = c.newInstance();

        Method method = c.getMethod(methodName, parameterTypes);

        return method.invoke(classInstance, args);

    }

    public Method[] getMethods() throws SecurityException, ClassNotFoundException
    {
        return getCompiledClass().getMethods();
    }

}