package GenerateNgram;

import Model.Gram;

import java.util.Comparator;

/**
 * Created by DuongPham on 10/04/2016.
 */
//So sanh theo counter
public class GramComparator implements Comparator<Gram>  {

    @Override
    public int compare(Gram o1, Gram o2) {
        int counter1=o1.getCounter();
        int counter2=o2.getCounter();
        if(counter1 < counter2){
            return 1;
        } else if (counter1 == counter2) {
            return 0;
        } else {
            return -1;
        }
    }
}
