package se.sead.fossil;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
        properties = {
                "allow.dataset.updates:true"
        }
)
public class AllowDatasetUpdateFossilImportTest extends FossilImportTest {

    @Override
    @Test
    public void test() {
        run();
    }
}
