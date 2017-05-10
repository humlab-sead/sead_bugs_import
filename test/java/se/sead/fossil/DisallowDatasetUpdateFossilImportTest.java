package se.sead.fossil;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
        properties = {
                "allow.dataset.updates:false"
        }
)
public class DisallowDatasetUpdateFossilImportTest extends FossilImportTest {

    @Override
    @Test
    public void test() {
        //run();
    }
}
