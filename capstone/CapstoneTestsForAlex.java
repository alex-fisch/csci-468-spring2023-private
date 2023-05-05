package edu.montana.csci.csci468;

import edu.montana.csci.csci468.tokenizer.Token;
import org.junit.jupiter.api.Test;

import java.util.List;

import static edu.montana.csci.csci468.tokenizer.TokenType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class CapstoneTestsAF extends CatscriptTestBase {

@Test
void test1() {  
	assertEquals("20\n", compile("20"));

    assertEquals("false\n", compile("false"));

    assertEquals("100\n", compile("20 * 5"));

}



@Test
void test2() {
    assertEquals("37\n", compile("function foo(x) { print(x) }" + "foo(37)"));

    assertEquals("[9, 57, 2]\n", compile("function foo(x : list) { print(x) }" + “foo([9, 57, 2])"));

    assertEquals("21\n", compile("var y = 21\n" + "function foo() {}" + "foo()" + "print(y)"));
}



@Test
void test3() {
    assertEquals("orange\n49\n", compile("print(orange)\n" + "print(49)"));

    assertEquals("99\n", compile("var x = 99/n" + "var y = x*n" + "print(y)"));

    assertEquals("2\n3\n", compile("for(x in [1, 2, 3]) { if(x!=1){print(x)} }"));
}
