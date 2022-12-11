package com.example.formularacing;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    dataAccess testDal = new dataAccess();
    String testTag = "test_msg";
    Random randnum;
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.formularacing", appContext.getPackageName());
    }

    @Test
    public void test_login_and_schedule() throws InterruptedException {
        randnum = new Random();
        int randomUserNum = randnum.nextInt();
        String testUserPhone = "testUser"+String.valueOf(randomUserNum);
        // String testUserPhone = "testUser-64830016";
        Task testTask = testDal.loginUser(testUserPhone);
        Log.d(testTag, "user "+testUserPhone+" is used for testing");
        while(!testTask.isComplete()) {}

        HashMap<String, String> testResult = (HashMap<String, String>)((DataSnapshot) testTask.getResult()).getValue();
        assertEquals(null, testResult);
        testTask = testDal.scheduleAppointment(testUserPhone, "testDate", "10:00", "Test");
        while(!testTask.isComplete()) {}
        Thread.sleep(1000);

        testDal.adminSetWorkingTimes("testDate", "10:00", "11:00");
        Thread.sleep(2000);
        testTask = testDal.loginUser(testUserPhone);
        while(!testTask.isComplete()) {}
        testResult = (HashMap<String, String>)((DataSnapshot) testTask.getResult()).getValue();
        Log.d(testTag, testResult.toString());
        assertEquals("{ =[], testDate={phone="+testUserPhone+", length=3, time=10:00, type=Test}}", testResult.toString()); // TODO assert the actual result when finished (should include an appointment at "testDate" at "10:00" of type "Test"
    }

    public void test_admin_show_appointments() {
        // TODO this ^ and change this v
        assertEquals(true, true);
    }
}