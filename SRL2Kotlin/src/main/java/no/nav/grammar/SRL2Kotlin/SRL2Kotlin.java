package no.nav.grammar.SRL2Kotlin;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.tree.gui.TreeViewer;

import java.awt.*;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.junit.Assert;

import static org.hamcrest.MatcherAssert.assertThat;
import static sun.nio.cs.Surrogate.is;

public class SRL2Kotlin {
    public static void main(String[] args) throws Exception {
        // create a CharStream that reads from standard input
        //  ANTLRInputStream input = new ANTLRInputStream("if a=true and b>=5 and a=true or b>=5 then {c = a+b;} else if a=b then {a is an b;} else {c=a;} "+
        //         "while a < b then {b+1; c+2;}"+"a=a+b;");
        // ANTLRInputStream input = new ANTLRInputStream( "Arpapr=hei.plei;");
        //ANTLRInputStream input = new ANTLRInputStream("while a < b then {b+1;}");
        //ANTLRInputStream input =new ANTLRInputStream("if a < b then {c=a+b;}");
        //ANTLRInputStream input = new ANTLRInputStream("if a=true and b>=5 then {c=a+b;}");
        String ruleinput = "if innBruker.vedtak.forsteVirk as a date < '01-jan-1991'\n" +
                "\n" +
                "\tand innBruker.bosattLand.kode <> LandkodeEnum.NOR as a string\n" +
                "\n" +
                "\tand innBruker.generellHistorikk is not null\n" +
                "\n" +
                "\tand innBruker.generellHistorikk.fravik_19_3 is not null\n" +
                "\n" +
                "\tand innBeregning.ektefelleMottarPensjon\n" +
                "\n" +
                "\tand innTilknyttet is not null\n" +
                "\n" +
                "\tand innTilknyttet.forsteVirk as a date < '01-jan-1991'\n" +
                "\n" +
                "then {\n" +
                "\n" +
                "\tberegningType = GrunnpensjonBeregningEnum.GP_FRAVIK_19_3;\n" +
                "\n" +
                "}";
        ANTLRInputStream input = new ANTLRInputStream(ruleinput);

        // create a lexer that feeds off of input CharStream
        no.nav.grammar.SRL2Kotlin.SRL2KotlinLexer lexer = new no.nav.grammar.SRL2Kotlin.SRL2KotlinLexer(input);


        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        no.nav.grammar.SRL2Kotlin.SRL2KotlinParser parser = new no.nav.grammar.SRL2Kotlin.SRL2KotlinParser(tokens);

        ParseTree tree = parser.parse(); // begin parsing at init rule
        ((no.nav.grammar.SRL2Kotlin.SRL2KotlinParser.ParseContext) tree).inspect(parser);

        ParseTreeWalker walker = new ParseTreeWalker();
        //org.abcd.examples.ArrayInit.ArrayInitBaseListener listener= new org.abcd.examples.ArrayInit.ArrayInitBaseListener();
        BaseListenerSRL listener = new BaseListenerSRL();
        walker.walk((ParseTreeListener) listener, tree);

        System.out.println("Original SRL code:");
        System.out.println(input +"" + '\n');
        System.out.println("Translated code from SRL to Kotlin DSL: ");
        System.out.println(listener.temp.toString().trim());
        //System.out.println(tree.toStringTree(parser)); // print LISP-style tree


        JFrame frame = new JFrame("Antlr AST");
        JPanel panel = new JPanel();
        TreeViewer viewr = new TreeViewer(Arrays.asList(
                parser.getRuleNames()),tree);
        viewr.setScale(1.3);//scale a little

        panel.add(viewr);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600,900);
        frame.setVisible(true);


    }
}
