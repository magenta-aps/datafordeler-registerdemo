package dk.magenta.datafordeler.registerdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lars on 16-03-17.
 */
public class RequestExpector {

    public interface Callback {
        public void run(String key, RequestExpector expector);
    }

    private static HashMap<String, ArrayList<RequestExpector>> expectors = new HashMap<>();

    private String expectedKey;
    private ArrayList<Callback> callbacks = new ArrayList<>();

    public RequestExpector(String expectedKey) {
        this.expectedKey = expectedKey;
        ArrayList<RequestExpector> list = expectors.get(expectedKey);
        if (list == null) {
            list = new ArrayList<>();
            expectors.put(expectedKey, list);
        }
        list.add(this);
    }

    public void addCallback(Callback callback) {
        this.callbacks.add(callback);
    }

    private void engage(String key) {
        for (Callback callback : this.callbacks) {
            try {
                callback.run(key, this);
            } catch (Throwable e) {
            }
        }
    }

    public static int engageExpectors(String key) {
        int called = 0;
        List<RequestExpector> list = expectors.get(key);
        if (list != null) {
            for (RequestExpector expector : list) {
                expector.engage(key);
            }
            called += list.size();
        }

        list = expectors.get(null);
        if (list != null) {
            for (RequestExpector expector : list) {
                expector.engage(key);
            }
            called += list.size();
        }
        return called;
    }
}
