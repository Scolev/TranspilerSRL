package org.abcd.examples.ArrayInit;


import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;



public class ArrayInitTest
{

    private static StringBuilder removeBlankSpace(StringBuilder sb){
        for(int i=0;i<sb.length();++i){
            if(Character.isWhitespace(sb.charAt(i))){
                sb.deleteCharAt(i);
                i--;
            }
        }
        return sb;
    }

    @Test
    public void testIfStatement(){
        ANTLRInputStream input = new ANTLRInputStream("if a=true and b>=5 and a=true or b>=5 and a=true and b>=5 then {c=a+b;}");
        StringBuilder expected = new StringBuilder();
        expected.append("if (a == true && b >= 5 && a = true || b >= 5 && a == true && b >= 5 ){ c = a + b ; }");

        // create a lexer that feeds off of input CharStream
        org.abcd.examples.ArrayInit.ArrayInitLexer lexer = new org.abcd.examples.ArrayInit.ArrayInitLexer(input);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BaseListenerSRL listener = new BaseListenerSRL();

        // create a parser that feeds off the tokens buffer
        org.abcd.examples.ArrayInit.ArrayInitParser parser = new org.abcd.examples.ArrayInit.ArrayInitParser(tokens);

        ParseTreeWalker walker = new ParseTreeWalker();
        ParseTree tree = parser.parse(); // begin parsing at init rule
        //System.out.println(tree.toStringTree(parser)); // print LISP-style tree
        walker.walk((ParseTreeListener) listener, tree);

       Assert.assertEquals(removeBlankSpace(expected).toString(),removeBlankSpace(listener.temp).toString());
    }




}
