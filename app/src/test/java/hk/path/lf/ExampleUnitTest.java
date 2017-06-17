package hk.path.lf;

import org.junit.Test;

import hk.path.lf.entities.API_GetLost;
import hk.path.lf.entities.API_GetLost_Ret;
import hk.path.lf.entities.API_Return;
import hk.path.lf.entities.LostItem;
import hk.path.lf.net.API;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test() throws InterruptedException {
        API.Request(new API_GetLost(2), new API_Return<API_GetLost_Ret>() {
            @Override
            public void ret(int Code, API_GetLost_Ret ret) {
                if (Code == 0) {
                    LostItem[] a = ret.getLostItems();
                } else {
                    System.out.println("error:" + Code);
                }
            }
        });


        Thread.sleep(1000 * 100);
    }
}