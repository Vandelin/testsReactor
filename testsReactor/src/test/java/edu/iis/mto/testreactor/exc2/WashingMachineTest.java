package edu.iis.mto.testreactor.exc2;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
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
    public void givenProgramAutodetectWhenStartThenDetectDirtDegreeCalledOnce() {
        washingMachine.start(laundryBatch, programConfiguration);
        verify(dirtDetector, times(1)).detectDirtDegree(anyObject());
    }

    @Test
    public void givenHeavyLaundryWhenStartThenLaundryStatusOverweight() {
        laundryBatch = laundryBatch.builder().withType(Material.WOOL).withWeightKg(20).build();
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration);
        assertTrue(laundryStatus.getErrorCode() == ErrorCode.TOO_HEAVY);
    }

    @Test
    public void givenRightLaundryWhenStartThenLaundryStatusSuccess() {
        laundryBatch = laundryBatch.builder().withType(Material.WOOL).withWeightKg(1).build();
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration);
        assertThat(laundryStatus.getResult(), is(Result.SUCCESS));
    }

    @Test
    public void givenHighDirtDegreeAndAutoDetectWhenStartThenProgramIsLong() {
        when(dirtDetector.detectDirtDegree(anyObject())).thenReturn(new Percentage(50));
        programConfiguration = programConfiguration.builder().withSpin(true).withProgram(Program.AUTODETECT).build();
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration);
        assertThat(laundryStatus.getRunnedProgram().getTimeInMinutes(), is(Program.LONG.getTimeInMinutes()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenMinusWeightWhenLaundryBatchThenReturnException() {
        laundryBatch.builder().withWeightKg(-2).build();
    }



}
