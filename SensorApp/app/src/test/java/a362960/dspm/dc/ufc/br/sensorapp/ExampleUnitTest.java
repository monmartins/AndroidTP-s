package a362960.dspm.dc.ufc.br.sensorapp;

import org.junit.Test;

import arcatch.ArCatch;
import arcatch.rule.ArCatchModule;

import static org.junit.Assert.*;



/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    private static ArCatchModule view = ArCatch.newModule("VIEW");
    private static ArCatchModule control = ArCatch.newModule("CTL");

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}