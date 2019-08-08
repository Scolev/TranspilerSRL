package no.nav.grammar.SRL2Kotlin;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class BaseListenerSRL extends no.nav.grammar.SRL2Kotlin.SRL2KotlinBaseListener {

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
                temp.append("OG{");
                break;

            case("if"):
                temp.append("HVIS{");
                break;

            case("<EOF>"):
                break;

            case("="):
                if(node.getParent().getParent() instanceof SRL2KotlinParser.Boolean_blockContext || node.getParent().getParent() instanceof SRL2KotlinParser.BooldContext){
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
                if(node.getParent() instanceof SRL2KotlinParser.Date_blockContext){
                    temp.append("før");
                } else {
                    temp.append("<");
                }
                break;

            case(">"):
                if(node.getParent() instanceof SRL2KotlinParser.Date_blockContext){
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
                !(node.getParent() instanceof SRL2KotlinParser.DotchainContext)

        ){
            temp.append(' ');
        }
        //-----------------------------------------------------------------------
    }


    @Override public void enterBoolean_block(SRL2KotlinParser.Boolean_blockContext ctx){
        temp.append("(");
    }

    @Override public void exitBoolean_block(SRL2KotlinParser.Boolean_blockContext ctx){
        temp.append("}"+'\n'+"SÅ");
    }

    @Override public void exitAssignment(SRL2KotlinParser.AssignmentContext ctx){
        //SRL can end statements with blank space, period or semicolon.
        if (!ctx.getText().endsWith(";")){
            temp.append(";");
        }
        temp.append('\n');
    }

    @Override public void enterBoold(SRL2KotlinParser.BooldContext ctx){
        temp.append('}');
        temp.append('\n');
    }



    @Override public void exitDotchain(SRL2KotlinParser.DotchainContext ctx){
        temp.append(" ");
    }




}