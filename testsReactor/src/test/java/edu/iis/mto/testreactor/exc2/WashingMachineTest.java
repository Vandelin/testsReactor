package edu.iis.mto.testreactor.exc2;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WashingMachineTest {

    WashingMachine washingMachine;
    LaundryBatch laundryBatch;
    ProgramConfiguration programConfiguration;

    @Mock
    DirtDetector dirtDetector;
    @Mock
    Engine engine;
    @Mock
    WaterPump waterPump;


    @Before
    public void setUp() throws Exception {
        when(dirtDetector.detectDirtDegree(anyObject())).thenReturn(new Percentage(20));
        laundryBatch = laundryBatch.builder().withType(Material.COTTON).withWeightKg(2).build();
        programConfiguration = programConfiguration.builder().withSpin(true).withProgram(Program.AUTODETECT).build();
        washingMachine = new WashingMachine(dirtDetector, engine, waterPump);
    }

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    @Test
    public void givenRightMaterialAndWeightWhenStartThenLaundryStatusIsntOverweight() {
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration);
        assertFalse(laundryStatus.getErrorCode() == ErrorCode.TOO_HEAVY);
    }

    @Test
    public void givenProgramAutodetectWhenStartThenDetectDirtDegreeCalledOnce(){
        washingMachine.start(laundryBatch, programConfiguration);
        verify(dirtDetector, times(1)).detectDirtDegree(anyObject());
    }

    @Test
    public void givenProgramAnyOtherThanAutodetectWhenStartThenDetectDirtDegreeIsntCalled(){
        washingMachine.start(laundryBatch, programConfiguration);
        verify(dirtDetector).detectDirtDegree(anyObject());
    }




}
