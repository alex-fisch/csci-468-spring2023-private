package edu.montana.csci.csci468.parser.expressions;

import edu.montana.csci.csci468.bytecode.ByteCodeGenerator;
import edu.montana.csci.csci468.eval.CatscriptRuntime;
import edu.montana.csci.csci468.parser.CatscriptType;
import edu.montana.csci.csci468.parser.SymbolTable;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static edu.montana.csci.csci468.bytecode.ByteCodeGenerator.internalNameFor;

public class ListLiteralExpression extends Expression {
    List<Expression> values;
    private CatscriptType type;

    public ListLiteralExpression(List<Expression> values) {
        this.values = new LinkedList<>();
        for (Expression value : values) {
            this.values.add(addChild(value));
        }
    }

    public List<Expression> getValues() {
        return values;
    }

    @Override
    public void validate(SymbolTable symbolTable) {
        for (Expression value : values) {
            value.validate(symbolTable);
        }
        if (values.size() > 0) {
            // TODO - generalize this looking at all objects in list
            type = CatscriptType.getListType(values.get(0).getType());
        } else {
            type = CatscriptType.getListType(CatscriptType.OBJECT);
        }
    }

    @Override
    public CatscriptType getType() {
        return type;
    }

    //==============================================================
    // Implementation
    //==============================================================

    @Override
    public Object evaluate(CatscriptRuntime runtime) {
        ArrayList<Object> expressionList = new ArrayList<>();
        for (Expression expr : values) {
            expressionList.add(expr.evaluate(runtime));
        }
        return expressionList;
    }

    @Override
    public void transpile(StringBuilder javascript) {
        super.transpile(javascript);
    }

    @Override
    public void compile(ByteCodeGenerator code) {
        String linkedListInternalClassName = internalNameFor(LinkedList.class);
        code.addTypeInstruction(Opcodes.NEW, linkedListInternalClassName);
        code.addInstruction(Opcodes.DUP);
        code.addMethodInstruction(Opcodes.INVOKESPECIAL, linkedListInternalClassName, "<init>", "()V");
        for (Expression value : values) {
            code.addInstruction(Opcodes.DUP);
            value.compile(code);
            box(code, value.getType());
            code.addMethodInstruction(Opcodes.INVOKEVIRTUAL, linkedListInternalClassName, "add", "(Ljava/lang/Object;)Z");
            code.addInstruction(Opcodes.POP);
        }
    }

}