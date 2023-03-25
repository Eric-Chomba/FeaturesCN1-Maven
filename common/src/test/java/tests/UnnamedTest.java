package tests;

import com.codename1.testing.AbstractTest;

import com.codename1.ui.Display;

public class UnnamedTest extends AbstractTest {
    public boolean runTest() throws Exception {
        waitForFormTitle("Features lang :en");
        pointerPress(0.45106384f, 0.23163842f, new int[]{0, 39});
        waitFor(116);
        pointerRelease(0.45106384f, 0.23163842f, new int[]{0, 39});
        waitForUnnamedForm();
        Display.getInstance().getCurrent().setName("Form_1");
        waitForFormTitle("Fingerprint Authentication");
        Display.getInstance().getCurrent().setName("Form_2");
        setText(new int[]{0, 0, 0, 0, 0}, "eric");
        setText(new int[]{0, 0, 0, 1, 0}, "123");
        clickButtonByLabel("Send");
        pointerPress(0.3970588f, 0.21691176f, new int[]{0, 0, 1, 0, 0});
        waitFor(83);
        pointerRelease(0.3970588f, 0.21691176f, new int[]{0, 0, 1, 0, 0});
        pointerPress(0.3970588f, 0.4375f, new int[]{0, 0, 1, 0, 0});
        waitFor(146);
        pointerRelease(0.3970588f, 0.4375f, new int[]{0, 0, 1, 0, 0});
        assertEqual(getToolbarCommands().length, 2);
        executeToolbarCommandAtOffset(0);
        waitForFormTitle("Features lang :en");
        Display.getInstance().getCurrent().setName("Form_3");
        pointerPress(0.35106382f, 0.37288135f, new int[]{0, 36});
        waitFor(40);
        pointerRelease(0.35106382f, 0.37288135f, new int[]{0, 36});
        waitForUnnamedForm();
        Display.getInstance().getCurrent().setName("Form_4");
        waitForFormTitle("L10N & I18N");
        Display.getInstance().getCurrent().setName("Form_5");
        clickButtonByLabel("Swahili");
        clickButtonByLabel("Sheng");
        assertEqual(getToolbarCommands().length, 2);
        executeToolbarCommandAtOffset(0);
        waitForFormName("Form_3");
        pointerPress(0.4638298f, 0.4463277f, new int[]{0, 37});
        waitFor(147);
        pointerRelease(0.4638298f, 0.4463277f, new int[]{0, 37});
        waitForUnnamedForm();
        Display.getInstance().getCurrent().setName("Form_6");
        waitForFormTitle("L10N Manager");
        Display.getInstance().getCurrent().setName("Form_7");
        assertEqual(getToolbarCommands().length, 2);
        executeToolbarCommandAtOffset(0);
        waitForFormName("Form_3");
        pointerPress(0.5425532f, 0.43220338f, new int[]{0, 33});
        waitFor(145);
        pointerRelease(0.5425532f, 0.43220338f, new int[]{0, 33});
        waitForUnnamedForm();
        Display.getInstance().getCurrent().setName("Form_8");
        waitForFormTitle("Files Parsing");
        Display.getInstance().getCurrent().setName("Form_9");
        clickButtonByLabel("JSON");
        clickButtonByLabel("XML");
        assertEqual(getToolbarCommands().length, 2);
        executeToolbarCommandAtOffset(0);
        waitForFormName("Form_3");
        return true;
    }
}
