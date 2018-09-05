package edu.colostate.cs.cs414.a1.bdeining;

import junit.framework.JUnit4TestAdapter;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  CompanyTest.class,
  ProjectTest.class,
  QualificationTest.class,
  WorkerTest.class
})
public class Runner {

  public static void main(String args[]) {
    junit.textui.TestRunner.run(suite());
  }

  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(Runner.class);
  }
}
