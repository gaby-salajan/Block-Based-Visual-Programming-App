package com.gabys.sscproject.logic;

import android.content.Context;
import android.widget.Toast;

import com.gabys.sscproject.model.Block;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Modifier;


public final class Compiler {
    Context context;
    Block startBlock;
    String code;
    int a;

    public Compiler(Context context) {
        this.context = context;
        this.code = "";
    }

    public String processCmd(String cmd, String rightCmd) {
        switch (cmd) {
            case "start":
                return "";
            case "print" :
                return "System.out.println("+rightCmd+")";
            case "assign" :
                a = 3;
                break;
            default :
                return "";
        }
        return "";
    }



    public void compileCode(Block startBlock) {
        if (startBlock == null){
            Toast.makeText(context, "Cannot compile; no start block", Toast.LENGTH_LONG).show();
        }
        else{
            Block current = startBlock;
            Block next = startBlock.getChild();

            while(next != null){
                if(current.getRightChild() != null){
                    code += processCmd(current.getCmd(), current.getRightChild().getCmd());
                }
                current = next;
                next = next.getChild();
            }
            Toast.makeText(context, code, Toast.LENGTH_LONG).show();
        }

        // Print expression result.
        //System.out.println(code);

    }

//    public void runCode(String arg) throws Exception {
//        DexMaker dexMaker = new DexMaker();
//
//        // Generate a HelloWorld class.
//        TypeId<?> helloWorld = TypeId.get("LHelloWorld;");
//        dexMaker.declare(helloWorld, "HelloWorld.generated", Modifier.PUBLIC, TypeId.OBJECT);
//        generateHelloMethod(dexMaker, helloWorld);
//
//        // Create the dex file and load it.
//        File dexCache = context.getDir("dx", Context.MODE_PRIVATE);
//        File outputDir = new File(dexCache, "/" + ".");
//        ClassLoader loader = dexMaker.generateAndLoad(Compiler.class.getClassLoader(), outputDir);
//        Class<?> helloWorldClass = loader.loadClass("HelloWorld");
//
//        // Execute our newly-generated code in-process.
//        helloWorldClass.getMethod("hello").invoke(null);
//    }
//
//    private void generateHelloMethod(DexMaker dexMaker, TypeId<?> declaringType) {
//        // Lookup some types we'll need along the way.
//        TypeId<System> systemType = TypeId.get(System.class);
//        TypeId<PrintStream> printStreamType = TypeId.get(PrintStream.class);
//
//        // Identify the 'hello()' method on declaringType.
//        MethodId hello = declaringType.getMethod(TypeId.VOID, "hello");
//
//        // Declare that method on the dexMaker. Use the returned Code instance
//        // as a builder that we can append instructions to.
//        Code code = dexMaker.declare(hello, Modifier.STATIC | Modifier.PUBLIC);
//
//        // Declare all the locals we'll need up front. The API requires this.
//        Local<Integer> a = code.newLocal(TypeId.INT);
//        Local<Integer> b = code.newLocal(TypeId.INT);
//        Local<Integer> c = code.newLocal(TypeId.INT);
//        Local<String> s = code.newLocal(TypeId.STRING);
//        Local<PrintStream> localSystemOut = code.newLocal(printStreamType);
//
//        // int a = 0xabcd;
//        code.loadConstant(a, 0xabcd);
//
//        // int b = 0xaaaa;
//        code.loadConstant(b, 0xaaaa);
//
//        // int c = a - b;
//        code.op(BinaryOp.SUBTRACT, c, a, b);
//
//        // String s = Integer.toHexString(c);
//        MethodId<Integer, String> toHexString
//                = TypeId.get(Integer.class).getMethod(TypeId.STRING, "toHexString", TypeId.INT);
//        code.invokeStatic(toHexString, s, c);
//
//        // System.out.println(s);
//        FieldId<System, PrintStream> systemOutField = systemType.getField(printStreamType, "out");
//        code.sget(systemOutField, localSystemOut);
//        MethodId<PrintStream, Void> printlnMethod = printStreamType.getMethod(
//                TypeId.VOID, "println", TypeId.STRING);
//        code.invokeVirtual(printlnMethod, null, localSystemOut, s);
//
//        // return;
//        code.returnVoid();
//    }
}
