package Model;

import Model.Gram;

import java.util.Comparator;

/**
 * Created by DuongPham on 16/05/2016.
 */
public class GramComparatorTFIDF implements Comparator<Gram> {

    @Override
    public int compare(Gram o1, Gram o2) {
        double counter1=o1.getTfidf();
        double counter2=o2.getTfidf();
        if(counter1 < counter2){
            return 1;
        } else if (counter1 == counter2) {
            return 0;
        } else {
            return -1;
        }
    }
}