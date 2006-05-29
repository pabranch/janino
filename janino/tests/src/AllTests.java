
/*
 * Janino - An embedded Java[TM] compiler
 *
 * Copyright (c) 2006, Arno Unkrig
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *    1. Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *    3. The name of the author may not be used to endorse or promote
 *       products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import junit.framework.*;

import util.*;

import org.codehaus.janino.*;

public class AllTests extends JaninoTestSuite {
    public static Test suite() {
        return new AllTests();
    }

    private AllTests() {
        super("Suite of all Janino test cases");

        this.addTest(new JLS2Tests());

        section(1, SimpleCompiler.class.getName());

        section(2, "Default imports");
        aet(TRUE, "1", "new ArrayList().getClass().getName().equals(\"java.util.ArrayList\")").setDefaultImports(new String[] { "java.util.*" });
        ast(SCAN, "2", "xxx").setDefaultImports(new String[] { "java.util@" });
        ast(PARS, "3", "xxx").setDefaultImports(new String[] { "java.util.9" });
        ast(PARS, "4", "xxx").setDefaultImports(new String[] { "java.util.*;" });
        acbt(TRUE, "5", "public static boolean main() { return new ArrayList() instanceof List; }").setDefaultImports(new String[] { "java.util.*" });
        aet(COMP, "6", "new ArrayList()").setDefaultImports(new String[] { "java.io.*" });

        section(2, "Equals");
        this.addTest(new TestCase("1") {
            protected void runTest() throws Exception {
                // Parameters have different names in ee2 and one unnecessary cast, but the generated bytecode is identical.
                ExpressionEvaluator ee1 = new ExpressionEvaluator("a + b",          double.class, new String[] { "a", "b" }, new Class[] { int.class, double.class });
                ExpressionEvaluator ee2 = new ExpressionEvaluator("(double) c + d", double.class, new String[] { "c", "d" }, new Class[] { int.class, double.class });
                assertEquals("SimpleCompiler equality", ee1, ee2);
            }
        });
        this.addTest(new TestCase("2") {
            protected void runTest() throws Exception {
                // In ee2, the sum operands swapped, resulting in slightly different byte code.
                ExpressionEvaluator ee1 = new ExpressionEvaluator("a + b", double.class, new String[] { "a", "b" }, new Class[] { int.class, double.class });
                ExpressionEvaluator ee2 = new ExpressionEvaluator("b + a", double.class, new String[] { "a", "b" }, new Class[] { int.class, double.class });
                assertFalse("SimpleCompiler equality", ee1.equals(ee2));
            }
        });

        section(null);
        this.addTest(CompilerTests.suite());
        this.addTest(ReportedBugs.suite());
    }
}
