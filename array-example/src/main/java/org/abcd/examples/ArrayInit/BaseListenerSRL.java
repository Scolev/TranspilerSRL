package org.abcd.examples.ArrayInit;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class BaseListenerSRL extends org.abcd.examples.ArrayInit.ArrayInitBaseListener {

    public StringBuilder temp = new StringBuilder();



    @Override public void visitTerminal(TerminalNode node) {
      // System.out.println("visited node: " + node.getSymbol().getText());

       String caseTest = node.getSymbol().getText();

       //Append node text, treat special cases
       switch(caseTest){

           case("or"):
               temp.append("||");
               break;

           case("and"):
               temp.append("&&");
               break;

           case("<EOF>"):
               break;

           case("="):
               if(node.getParent().getParent() instanceof ArrayInitParser.Boolean_blockContext || node.getParent().getParent() instanceof ArrayInitParser.BooldContext){
                    temp.append("==");
                } else { temp.append("=");}
               break;

           case("is a"):
               temp.append("=");
               break;

           case("is an"):
               temp.append("=");
               break;

           case("}"):
               temp.append("}"+'\n');
               break;

           case(";"):
               temp.append(";");
               break;

           case("as a date"):
               break;

           case("is not"):
               temp.append("!=");
               break;

           case("<>"):
               temp.append("!erLik");
               break;

           case("<"):
               if(node.getParent() instanceof ArrayInitParser.Date_blockContext){
                   temp.append("før");
               } else {
                   temp.append("<");
               }
               break;

           case(">"):
               if(node.getParent() instanceof ArrayInitParser.Date_blockContext){
                   temp.append("etter");
               } else {
                   temp.append(">");
               }
               break;

           default:
               temp.append(caseTest);
               break;
       }

       //------------------------------------------------------------------------
       //Append spaces unless special case
        if(
                //Special cases which omit appending blank space after every leaf node
                !(node.getParent() instanceof ArrayInitParser.DotchainContext)

        ){
            temp.append(' ');
        }
        //-----------------------------------------------------------------------
    }


    @Override public void enterBoolean_block(ArrayInitParser.Boolean_blockContext ctx){
       temp.append("(");
    }

    @Override public void exitBoolean_block(ArrayInitParser.Boolean_blockContext ctx){
       temp.append(")"+'\n');
    }

    @Override public void exitAssignment(ArrayInitParser.AssignmentContext ctx){
        //SRL can end statements with blank space, period or semicolon.
        if (!ctx.getText().endsWith(";")){
            temp.append(";");
        }
        temp.append('\n');
    }

    @Override public void enterBoold(ArrayInitParser.BooldContext ctx){
        temp.append('\n');
    }

    @Override public void exitDotchain(ArrayInitParser.DotchainContext ctx){
        temp.append(" ");
    }




}
